package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;

public class Tax implements Serializable {

	private static final long serialVersionUID = 6720147120019331590L;

	private Long id;
	private String taxType;
	private String taxRate;
	private String tax;
	private String saltKey;

	public Tax() {

	}

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

}
