package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;

public class TaxTypeLookupDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1109616537540628981L;
	private Long id;
	private String name;
	private String taxRate;
	private String country;

	public String getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long long1) {
		this.id = long1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
