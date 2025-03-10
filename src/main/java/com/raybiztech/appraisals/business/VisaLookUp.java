package com.raybiztech.appraisals.business;

import java.io.Serializable;

import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

public class VisaLookUp implements Serializable{
	
	private static final long serialVersionUID = -7603378054901982279L;
	
	private Long id;
	private CountryLookUp country;
	private String visaType;
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CountryLookUp getCountry() {
		return country;
	}
	public void setCountry(CountryLookUp country) {
		this.country = country;
	}
	public String getVisaType() {
		return visaType;
	}
	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}
	
	


	
	
}
