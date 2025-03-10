package com.raybiztech.projectmanagement.invoice.builder;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.projectmanagement.invoice.business.ReceivedInvoiceAmount;
import com.raybiztech.projectmanagement.invoice.business.ReceivedInvoiceAmountAudit;
import com.raybiztech.projectmanagement.invoice.dto.ReceivedInvoiceAmountDTO;

@Component("receivedInvoiceAmountBuilder")
public class ReceivedInvoiceAmountBuilder {

	public ReceivedInvoiceAmountDTO entityToDto(
			ReceivedInvoiceAmount receivedInvoiceAmount) {

		ReceivedInvoiceAmountDTO amountDTO = null;
		if (receivedInvoiceAmount != null) {
			amountDTO = new ReceivedInvoiceAmountDTO();
			amountDTO.setId(receivedInvoiceAmount.getId());
			amountDTO.setReceivedAmount(receivedInvoiceAmount
					.getReceivedAmount());
			amountDTO.setTdsAmount(receivedInvoiceAmount.getTdsAmount() != null ? receivedInvoiceAmount.getTdsAmount() : null);
			amountDTO.setNetAmount(receivedInvoiceAmount.getNetAmount() != null ?receivedInvoiceAmount.getNetAmount() : null);
			amountDTO.setReceivedDate(receivedInvoiceAmount.getReceivedDate()
					.toString("dd/MM/yyyy"));
		}

		return amountDTO;
	}

	public ReceivedInvoiceAmount dtoToEntity(ReceivedInvoiceAmountDTO amountDTO) {
		ReceivedInvoiceAmount amount = null;
		if (amountDTO != null) {
			amount = new ReceivedInvoiceAmount();
			amount.setId(amountDTO.getId());
			amount.setReceivedAmount(amountDTO.getReceivedAmount());
			amount.setTdsAmount(amountDTO.getTdsAmount() != null ? amountDTO.getTdsAmount() : null);
			amount.setNetAmount(amountDTO.getNetAmount() != null ? amountDTO.getNetAmount() : null);
			try {
				amount.setReceivedDate(DateParser.toDate(amountDTO
						.getReceivedDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		return amount;
	}

	public Set<ReceivedInvoiceAmount> toEntityList(
			Set<ReceivedInvoiceAmountDTO> amountDTOs) {

		Set<ReceivedInvoiceAmount> receivedInvoiceAmountList = null;
		if (amountDTOs != null) {
			receivedInvoiceAmountList = new HashSet<ReceivedInvoiceAmount>();
			for (ReceivedInvoiceAmountDTO amountDTO : amountDTOs) {
				receivedInvoiceAmountList.add(dtoToEntity(amountDTO));
			}
		}

		return receivedInvoiceAmountList;

	}

	public Set<ReceivedInvoiceAmountDTO> toDTOList(
			Set<ReceivedInvoiceAmount> receivedInvoiceAmounts) {

		Set<ReceivedInvoiceAmountDTO> receivedInvoiceAmountDTOs = null;
		if (receivedInvoiceAmounts != null) {
			receivedInvoiceAmountDTOs = new HashSet<ReceivedInvoiceAmountDTO>();
			for (ReceivedInvoiceAmount amount : receivedInvoiceAmounts) {
				receivedInvoiceAmountDTOs.add(entityToDto(amount));
			}

		}

		return receivedInvoiceAmountDTOs;

	}

	public Map<String, Object> decryptReceivedInvoiceAmountList(Long id,
			Set<ReceivedInvoiceAmount> receivedInvoiceAmounts) {

		Map<String, Object> map = new HashMap<String, Object>();

		Set<ReceivedInvoiceAmountDTO> receivedInvoiceAmountDTOs = null;
		Long totalReceivedAmount = 0L;
		if (receivedInvoiceAmounts != null) {
			receivedInvoiceAmountDTOs = new HashSet<ReceivedInvoiceAmountDTO>();
			for (ReceivedInvoiceAmount amount : receivedInvoiceAmounts) {

				ReceivedInvoiceAmountDTO amountDTO = null;
				if (amount != null) {
					AES256Encryption aes256Encryption = new AES256Encryption(
							String.valueOf(id), amount.getSaltkey());
					amountDTO = new ReceivedInvoiceAmountDTO();
					amountDTO.setId(amount.getId());

					String receivedAmount = amount.getReceivedAmount();
					amountDTO
							.setReceivedAmount((receivedAmount != null) ? aes256Encryption
									.decrypt(receivedAmount) : receivedAmount);
					
	               String tdsAmount = (amount.getTdsAmount()!= null ? amount.getTdsAmount() : null);
					
					amountDTO.setTdsAmount((tdsAmount != null )? aes256Encryption.decrypt(tdsAmount) : tdsAmount);
					
                     String netAmount = (amount.getNetAmount()!= null ? amount.getNetAmount() : null);
					
					amountDTO.setNetAmount((netAmount != null )? aes256Encryption.decrypt(netAmount) : netAmount);
					
					amountDTO.setReceivedDate(amount.getReceivedDate()
							.toString("dd/MM/yyyy"));
					totalReceivedAmount = totalReceivedAmount
							+ Long.valueOf(Math.round(Double
									.valueOf(aes256Encryption
											.decrypt(receivedAmount))));
				}

				receivedInvoiceAmountDTOs.add(amountDTO);
			}

		}
		map.put("list", receivedInvoiceAmountDTOs);
		map.put("totalReceivedAmount", totalReceivedAmount);

		return map;
	}

	public Set<ReceivedInvoiceAmountAudit> getReceivedAmountAudit(
			Set<ReceivedInvoiceAmount> invoiceAmounts, Long invoiceId) {
		Set<ReceivedInvoiceAmountAudit> amountAudits = null;
		if (invoiceAmounts != null) {
			amountAudits = new HashSet<ReceivedInvoiceAmountAudit>();
			for (ReceivedInvoiceAmount amount : invoiceAmounts) {
				ReceivedInvoiceAmountAudit amountAudit = new ReceivedInvoiceAmountAudit();
				amountAudit.setReceivedAmount(amount.getReceivedAmount());
				amountAudit.setReceivedDate(amount.getReceivedDate());
				amountAudit.setTdsAmount(amount.getTdsAmount() != null ? amount.getTdsAmount() : null);
				amountAudit.setNetAmount(amount.getNetAmount() != null ? amount.getNetAmount() : null);
				amountAudit.setSaltkey(amount.getSaltkey());
				amountAudits.add(amountAudit);
			}

		}
		return amountAudits;
	}

	public Set<ReceivedInvoiceAmountDTO> getReceivedAmountAuditDTOs(
			Set<ReceivedInvoiceAmountAudit> amountAudits, Long invoiceId) {
		
		Set<ReceivedInvoiceAmountDTO> amountDTOs = null;
		
		if (amountAudits != null) {
			amountDTOs = new HashSet<ReceivedInvoiceAmountDTO>();
			for (ReceivedInvoiceAmountAudit amountAudit : amountAudits) {
				ReceivedInvoiceAmountDTO amountDTO = new ReceivedInvoiceAmountDTO();
				AES256Encryption aes256Encryption = new AES256Encryption(
						String.valueOf(invoiceId), amountAudit.getSaltkey());

				amountDTO.setId(amountAudit.getId());
				amountDTO.setReceivedAmount(aes256Encryption
						.decrypt(amountAudit.getReceivedAmount()));
				amountDTO.setTdsAmount(amountAudit.getTdsAmount() != null ?aes256Encryption.decrypt(amountAudit.getTdsAmount()): null);
				amountDTO.setNetAmount(amountAudit.getNetAmount() != null ? aes256Encryption.decrypt(amountAudit.getNetAmount()): null);
				amountDTO.setReceivedDate(amountAudit.getReceivedDate()
						.toString("dd/MM/yyyy"));

				amountDTOs.add(amountDTO);
			}

		}
		return amountDTOs;
	}
	
	
	public Map<String, Object> decryptReceivedInvoiceAmountAuditListt(Long id,
			Set<ReceivedInvoiceAmountAudit> receivedInvoiceAmounts) {
		

		Map<String, Object> map = new HashMap<String, Object>();

		Set<ReceivedInvoiceAmountDTO> receivedInvoiceAmountDTOs = null;
		Long totalReceivedAmount = 0L;
		Long totalTdsAmount =0L;
		Long totalNetAmount =0L;
		if (receivedInvoiceAmounts != null) {
			receivedInvoiceAmountDTOs = new HashSet<ReceivedInvoiceAmountDTO>();
			for (ReceivedInvoiceAmountAudit amount : receivedInvoiceAmounts) {

				ReceivedInvoiceAmountDTO amountDTO = null;
				if (amount != null) {
					AES256Encryption aes256Encryption = new AES256Encryption(
							String.valueOf(id), amount.getSaltkey());
					amountDTO = new ReceivedInvoiceAmountDTO();
					amountDTO.setId(amount.getId());

					String receivedAmount = amount.getReceivedAmount();
					amountDTO
							.setReceivedAmount((receivedAmount != null) ? aes256Encryption
									.decrypt(receivedAmount) : receivedAmount);
					String tdsAmount = (amount.getTdsAmount()!= null ? amount.getTdsAmount() : null);
					
					amountDTO.setTdsAmount((tdsAmount != null )? aes256Encryption.decrypt(tdsAmount) : tdsAmount);
					
                     String netAmount = (amount.getNetAmount()!= null ? amount.getNetAmount() : null);
					
					amountDTO.setNetAmount((netAmount != null )? aes256Encryption.decrypt(netAmount) : netAmount);
					
					
					
					amountDTO.setReceivedDate(amount.getReceivedDate()
							.toString("dd/MM/yyyy"));
					totalReceivedAmount = totalReceivedAmount
							+ Long.valueOf(Math.round(Double
									.valueOf(aes256Encryption
											.decrypt(receivedAmount))));
					
					
					
					if(tdsAmount != null){
					totalTdsAmount = totalTdsAmount
							+ Long.valueOf(Math.round(Double
									.valueOf(aes256Encryption
											.decrypt(tdsAmount))));
					}
					if(netAmount != null){
					totalNetAmount = totalNetAmount
							+ Long.valueOf(Math.round(Double
									.valueOf(aes256Encryption
											.decrypt(netAmount))));
					}
				}

				receivedInvoiceAmountDTOs.add(amountDTO);
			}

		}
		map.put("list", receivedInvoiceAmountDTOs);
		map.put("totalReceivedAmount", totalReceivedAmount);
		map.put("totalTdsAmount", totalTdsAmount);
		map.put("totalNetAmount", totalNetAmount);

		return map;
	}

	
	public static void main(String[] args) {
		String saltKey = KeyGenerators.string().generateKey();
		AES256Encryption encrypt = new AES256Encryption(
				String.valueOf(1025), saltKey);
		
		System.out.println("saltkey"+saltKey);
		String var = "i love india";
		String encStr = encrypt.encrypt(var);
		System.out.println(encStr);
		System.out.println("....../n");
		/*AES256Encryption decrypt = new AES256Encryption(
				String.valueOf(641), "10e93f2e75f8df1c");
		System.out.println(decrypt.decrypt("0be0a3ae1998483ae508fe3423242d5f5b247ac94b83a3bd70a5da7f9d381335"));*/
		
		/*AES256Encryption decrypt = new AES256Encryption(
				String.valueOf(640), "7899c21c74d520bb");
		System.out.println(decrypt.decrypt("8494ead3c041a16dd1b798acd74b931f3615a3198d87818be7a3ac6b49111ddf"));
			*/	
	}
}
