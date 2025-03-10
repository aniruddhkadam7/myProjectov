package com.raybiztech.offerLetter.dto;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;

public class OfferLetterDto {
	
	private Long id;
	private String offerLetterdoc;
	private Long employeeId;
	private String createdBy;
	private String createdDate;
	private String offerLetterName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOfferLetterdoc() {
		return offerLetterdoc;
	}
	public void setOfferLetterdoc(String offerLetterdoc) {
		this.offerLetterdoc = offerLetterdoc;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getOfferLetterName() {
		return offerLetterName;
	}
	
	public void setOfferLetterName(String offerLetterName) {
		this.offerLetterName = offerLetterName;
	}
	
	
	
	
	

}
