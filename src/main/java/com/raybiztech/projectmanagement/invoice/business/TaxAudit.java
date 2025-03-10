package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;

public class TaxAudit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5288226911568404216L;
	private Long id;
	private String taxType;
	private String taxRate;
	private String tax;
	private Long taxId;
	private String saltKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getSaltKey() {
		return saltKey;
	}

	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}

	public Long getTaxId() {
		return taxId;
	}

	public void setTaxId(Long taxId) {
		this.taxId = taxId;
	}

}
