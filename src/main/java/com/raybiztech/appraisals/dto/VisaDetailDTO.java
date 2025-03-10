package com.raybiztech.appraisals.dto;

import java.io.Serializable;


public class VisaDetailDTO implements Serializable {

	private static final long serialVersionUID = -999645613060140356L;

	private Long id;
	private Long empId;
	private String empName;
	private Long visaTypeId;
	private String visaType;
	private Long countryId;
	private String countryName;
	private String dateOfIssue;
	private String dateOfExpire;
	private String createdBy;
	private String updatedBy;
	private String createdDate;
	private String updatedDate;
	//for image attachments
	private String visaDetailsPath;
	private byte[] visaDetailsData;
	private String visaThumbPicture;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getDateOfIssue() {
		return dateOfIssue;
	}
	public void setDateOfIssue(String dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}
	public String getDateOfExpire() {
		return dateOfExpire;
	}
	public void setDateOfExpire(String dateOfExpire) {
		this.dateOfExpire = dateOfExpire;
	}
	public String getVisaDetailsPath() {
		return visaDetailsPath;
	}
	public void setVisaDetailsPath(String visaDetailsPath) {
		this.visaDetailsPath = visaDetailsPath;
	}
	public byte[] getVisaDetailsData() {
		return visaDetailsData;
	}
	public void setVisaDetailsData(byte[] visaDetailsData) {
		this.visaDetailsData = visaDetailsData;
	}
	public String getVisaThumbPicture() {
		return visaThumbPicture;
	}
	public void setVisaThumbPicture(String visaThumbPicture) {
		this.visaThumbPicture = visaThumbPicture;
	}
	

	
	
}
