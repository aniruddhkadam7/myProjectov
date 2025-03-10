package com.raybiztech.appraisals.dto;

import java.io.Serializable;

public class VisaLookUpDTO implements Serializable{

	private static final long serialVersionUID = -6473847023978693084L;

	private Long visaTypeId;
	private String visaType;
	private Long countryId;
	private String countryName;
	
	public Long getVisaTypeId() {
		return visaTypeId;
	}
	public void setVisaTypeId(Long visaTypeId) {
		this.visaTypeId = visaTypeId;
	}
	public String getVisaType() {
		return visaType;
	}
	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	
}
