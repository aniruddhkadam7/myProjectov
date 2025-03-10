package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;


public class CountryLookUpDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7317118866153827220L;
	private Integer id;
	private String name;
	private String mobileCode;
	private String countryCode;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getMobileCode() {
		return mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}

	

}
