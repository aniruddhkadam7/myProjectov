package com.raybiztech.projectmanagement.invoice.builder;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.projectmanagement.invoice.business.Tax;
import com.raybiztech.projectmanagement.invoice.business.TaxAudit;
import com.raybiztech.projectmanagement.invoice.dao.InvoiceDao;
import com.raybiztech.projectmanagement.invoice.dto.TaxDTO;
import com.raybiztech.projectmanagement.invoice.dto.TaxTypeLookupDTO;
import com.raybiztech.projectmanagement.invoice.lookup.TaxTypeLookup;

@Component("taxBuilder")
public class TaxBuilder {

	@Autowired
	InvoiceDao invoiceDao;

	Logger logger = Logger.getLogger(TaxBuilder.class);

	public Tax toEntity(TaxDTO taxDTO) {
		Tax tax = null;
		if (taxDTO != null) {
			tax = new Tax();
			if(taxDTO.getId()==null)
			{
				tax.setId(taxDTO.getId());
			}else{
				tax.setId(null);
			}
			
			tax.setTax(taxDTO.getTax());
			tax.setTaxRate(taxDTO.getTaxRate());
			tax.setTaxType(taxDTO.getTaxType());
		}
		return tax;
	}

	/*
	 * public TaxDTO toDto(Tax tax) { TaxDTO dto = null; if (tax != null) {
	 * AES256Encryption aes256Encryption = new AES256Encryption(
	 * String.valueOf(tax.getId()), tax.getSaltKey());
	 * 
	 * dto = new TaxDTO(); dto.setId(tax.getId());
	 * dto.setTax(aes256Encryption.decrypt(tax.getTax()));
	 * dto.setTaxRate(aes256Encryption.decrypt(tax.getTaxRate()));
	 * dto.setTaxType(String.valueOf(tax.getTaxType())); } return dto; }
	 */

	public TaxDTO toDto(Tax tax) {
		TaxDTO dto = null;
		if (tax != null) {
			AES256Encryption aes256Encryption = new AES256Encryption(
					String.valueOf(tax.getId()), tax.getSaltKey());

			dto = new TaxDTO();
			dto.setId(tax.getId());
			dto.setTax(aes256Encryption.decrypt(tax.getTax()));
			dto.setTaxRate(tax.getTaxRate());
			dto.setTaxType(tax.getTaxType());
		}
		return dto;
	}

	public Set<Tax> toEntityList(Set<TaxDTO> taxDTOs) {
		Set<Tax> taxList = null;
		if (taxDTOs != null) {
			taxList = new HashSet<Tax>();
			for (TaxDTO dto : taxDTOs) {
				taxList.add(toEntity(dto));
			}

		}
		return taxList;
	}

	public TaxTypeLookup masterTax(TaxTypeLookupDTO taxTypeLookupDTO) {

		TaxTypeLookup lookup = null;
		if (taxTypeLookupDTO != null) {
			lookup = new TaxTypeLookup();
			lookup.setCountry(taxTypeLookupDTO.getCountry());
			// converting tax name to upper case and storing in db

			lookup.setName(taxTypeLookupDTO.getName().toUpperCase());
			lookup.setTaxRate(taxTypeLookupDTO.getTaxRate());
		}
		return lookup;
	}

	public Set<TaxTypeLookupDTO> taxLookupList(Set<TaxTypeLookup> tLookups) {

		Set<TaxTypeLookupDTO> dtos = new HashSet<TaxTypeLookupDTO>();
		for (TaxTypeLookup lookup : tLookups) {
			TaxTypeLookupDTO dto = new TaxTypeLookupDTO();
			dto.setId(lookup.getId());
			dto.setCountry(lookup.getCountry());
			dto.setName(lookup.getName());
			dto.setTaxRate(lookup.getTaxRate());
			dtos.add(dto);
		}
		return dtos;
	}

	public Set<TaxDTO> toDtoList(Set<Tax> taxs) {

		Set<TaxDTO> taxDTOs = null;
		if (taxs != null) {
			taxDTOs = new HashSet<TaxDTO>();
			for (Tax tax : taxs) {
				if (tax.getTaxType() != null) {
					taxDTOs.add(toDto(tax));
				}
			}
		}
		return taxDTOs;
	}

	public Set<Tax> encryptTax(Set<Tax> taxs) {

		Set<Tax> encryptedTax = new HashSet<Tax>();
		for (Tax tax : taxs) {
			if (tax != null) {
				if (tax.getSaltKey() == null) {
					String saltKey = KeyGenerators.string().generateKey();
					AES256Encryption aes256Encryption = new AES256Encryption(
							String.valueOf(tax.getId()), saltKey);
					tax.setSaltKey(saltKey);
					tax.setTax(aes256Encryption.encrypt(tax.getTax()));
					encryptedTax.add(tax);
				} else {
					AES256Encryption aes256Encryption = new AES256Encryption(
							String.valueOf(tax.getId()), tax.getSaltKey());
					tax.setTax(aes256Encryption.encrypt(tax.getTax()));
					encryptedTax.add(tax);

				}
			}

		}

		return encryptedTax;

	}

	public Tax toEditEntity(TaxDTO taxDTO) {
		Tax tax = null;
		if (taxDTO != null) {
			if (taxDTO.getId() != null) {
				tax = invoiceDao.findBy(Tax.class, taxDTO.getId());
				tax.setId(taxDTO.getId());
				tax.setTax(taxDTO.getTax());
				tax.setTaxRate(taxDTO.getTaxRate());
				tax.setTaxType(taxDTO.getTaxType());
			} else {
				tax = new Tax();
				tax.setId(taxDTO.getId());
				tax.setTax(taxDTO.getTax());
				tax.setTaxRate(taxDTO.getTaxRate());
				tax.setTaxType(taxDTO.getTaxType());
			}
		}
		return tax;
	}

	public Set<TaxAudit> getTaxDetailsForAudit(Set<Tax> taxs) {
		Set<TaxAudit> taxAudits = null;
		if (taxs != null) {
			taxAudits = new HashSet<TaxAudit>();
			for (Tax tax : taxs) {
				TaxAudit audit = new TaxAudit();
				audit.setTaxType(tax.getTaxType());
				audit.setTaxRate(tax.getTaxRate());
				audit.setTax(tax.getTax());
				audit.setSaltKey(tax.getSaltKey());
				audit.setTaxId(tax.getId());
				taxAudits.add(audit);
			}
		}
		return taxAudits;
	}

	public Set<TaxDTO> getTaxAuditDTO(Set<TaxAudit> audits) {
		Set<TaxDTO> taxDTOs = null;
		if (audits != null) {
			taxDTOs = new HashSet<TaxDTO>();
			for (TaxAudit audit : audits) {
				TaxDTO dto = new TaxDTO();
				AES256Encryption aes256Encryption = new AES256Encryption(
						String.valueOf(audit.getTaxId()), audit.getSaltKey());
				dto.setId(audit.getId());
				dto.setTaxType(audit.getTaxType());
				dto.setTaxRate(audit.getTaxRate());
				dto.setTax(aes256Encryption.decrypt(audit.getTax()));
				taxDTOs.add(dto);
			}

		}
		return taxDTOs;
	}
	

}
