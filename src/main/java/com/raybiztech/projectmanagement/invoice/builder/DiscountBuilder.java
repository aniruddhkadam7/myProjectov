package com.raybiztech.projectmanagement.invoice.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.projectmanagement.invoice.business.Discount;
import com.raybiztech.projectmanagement.invoice.business.DiscountType;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.dto.DiscountDTO;

@Component("discountBuilder")
public class DiscountBuilder {

	Logger logger = Logger.getLogger(DiscountBuilder.class);

	public Discount toEntity(DiscountDTO discountDto) {
		Discount discount = new Discount();
		if (discount != null) {
			discount.setDiscount(discountDto.getDiscount());
			discount.setDiscountRate(discountDto.getDiscountRate());
			discount.setDiscountType(DiscountType.valueOf(discountDto
					.getDiscountType()));
		}
		return discount;
	}

	public DiscountDTO toDto(Discount discount) {
		DiscountDTO discountDTO = null;
		if (discount != null) {

			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(discount.getId()), discount.getSaltKey());

			discountDTO = new DiscountDTO();
			discountDTO.setId(discount.getId());
			discountDTO
					.setDiscount(String.valueOf(discount.getDiscount() == null ? 0
							: aes256Encryption.decrypt(discount.getDiscount())));
			discountDTO.setDiscountRate(String.valueOf(discount
					.getDiscountRate() == null ? 0 : aes256Encryption
					.decrypt(discount.getDiscountRate())));

			discountDTO.setDiscountType(discount.getDiscountType()
					.getDiscountType());
		}

		return discountDTO;
	}

	public List<DiscountDTO> convertEntityListToDTOList(
			List<Discount> discountList) {

		List<DiscountDTO> discountDtoList = null;
		if (discountList != null) {

			discountDtoList = new ArrayList<DiscountDTO>();

			for (Discount discount : discountList) {
				discountDtoList.add(toDto(discount));
			}
		}
		return discountDtoList;

	}

	/*public Discount encryptAndSaveDiscount(Discount discount, Invoice invoice) {

		if (discount != null) {

			if (discount.getSaltKey() == null) {

				String saltKey = KeyGenerators.string().generateKey();
				AES256Encryption aes256Encryption = new AES256Encryption(
						String.valueOf(discount.getId()), saltKey);

				discount.setSaltKey(saltKey);
				discount.setDiscount(aes256Encryption.encrypt(invoice
						.getDiscount().getDiscount()));
				discount.setDiscountRate(aes256Encryption.encrypt(invoice
						.getDiscount().getDiscountRate()));
			} else if (discount.getSaltKey() != null) {
				AES256Encryption aes256Encryption = new AES256Encryption(
						String.valueOf(discount.getId()), discount.getSaltKey());

				discount.setDiscount(aes256Encryption.encrypt(invoice
						.getDiscount().getDiscount()));
				discount.setDiscountRate(aes256Encryption.encrypt(invoice
						.getDiscount().getDiscountRate()));

			}

		}
		return discount;

	}*/
}
