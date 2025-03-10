package com.raybiztech.appraisals.certification.business;

import java.io.Serializable;

import com.raybiztech.date.Date;
import com.raybiztech.recruitment.business.Technology;

public class CertificateType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1531597939963507510L;
	
	private Long id;
	private Technology technology;
	private String certificateType;
	private Long createdBy;
	private Long updatedBy;
	private Date createdDate;
	private Date updatedDate;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Technology getTechnology() {
		return technology;
	}
	public void setTechnology(Technology technology) {
		this.technology = technology;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Override
	public String toString() {
		return "CertificationType [id=" + id + ", technology=" + technology + ", certificateType=" + certificateType
				+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy + ", createdDate=" + createdDate
				+ ", updatedDate=" + updatedDate + "]";
	}
	
	
	
	

	
}
