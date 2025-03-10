package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;

import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

public class CountryAddress  implements Serializable{
	
	private static final long serialVersionUID = -4188595605057303402L;
	
	private Long id;
	private String country;
	private String companyName;
	private String address;
	private CountryLookUp countryLookUp;
	
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
	
	public CountryLookUp getCountryLookUp() {
		return countryLookUp;
	}
	public void setCountryLookUp(CountryLookUp countryLookUp) {
		this.countryLookUp = countryLookUp;
	}
	
	
	

}
