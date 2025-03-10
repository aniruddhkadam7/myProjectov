package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;

public class CountryAddressDTO implements Serializable{
	
	private static final long serialVersionUID = -4188595605057303402L;
	
	private Long id;
	private String country;
	private String companyName;
	private String address;
	private CountryLookUpDTO countryLookUpDTO;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public CountryLookUpDTO getCountryLookUpDTO() {
		return countryLookUpDTO;
	}
	public void setCountryLookUpDTO(CountryLookUpDTO countryLookUpDTO) {
		this.countryLookUpDTO = countryLookUpDTO;
	}
	
	

}
