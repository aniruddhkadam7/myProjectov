package com.raybiztech.appraisals.business;

import java.io.Serializable;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class VisaDetails implements Serializable {
	
	private static final long serialVersionUID = -3355543576793770757L;
	
	private Long id;
	private Employee employee;
	private VisaLookUp visaLookUp;
	private Date dateOfIssue;
	private Date dateOfExpire;
	private Long createdBy;
	private Second createdDate;
	private Long updatedBy;
	private Second updatedDate;
	//for visa attachments
	private String visaDetailsPath;
	private String visaThumbPicture;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public VisaLookUp getVisaLookUp() {
		return visaLookUp;
	}
	public void setVisaLookUp(VisaLookUp visaLookUp) {
		this.visaLookUp = visaLookUp;
	}
	
	public Date getDateOfIssue() {
		return dateOfIssue;
	}
	public void setDateOfIssue(Date dateOfIssue) {
		this.dateOfIssue = dateOfIssue;
	}
	public Date getDateOfExpire() {
		return dateOfExpire;
	}
	public void setDateOfExpire(Date dateOfExpire) {
		this.dateOfExpire = dateOfExpire;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Second getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Second getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getVisaDetailsPath() {
		return visaDetailsPath;
	}
	public void setVisaDetailsPath(String visaDetailsPath) {
		this.visaDetailsPath = visaDetailsPath;
	}
	public String getVisaThumbPicture() {
		return visaThumbPicture;
	}
	public void setVisaThumbPicture(String visaThumbPicture) {
		this.visaThumbPicture = visaThumbPicture;
	}
	
	
}
