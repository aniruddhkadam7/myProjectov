package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;

public class TaxDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9064788593356761950L;
	private Long id;
	private String taxType;
	private String taxRate;
	private String tax;

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

}
