package com.raybiztech.appraisals.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class QualificationLookUp implements Serializable{

	private static final long serialVersionUID = 3660111132696896820L;

	private Long id;
	private String qualificationCategory;
	private String qualificationName;
	private Long createdBy;
	private Second createdDate;
	private Long updatedBy;
	private Second updatedDate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQualificationCategory() {
		return qualificationCategory;
	}
	public void setQualificationCategory(String qualificationCategory) {
		this.qualificationCategory = qualificationCategory;
	}
	public String getQualificationName() {
		return qualificationName;
	}
	public void setQualificationName(String qualificationName) {
		this.qualificationName = qualificationName;
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
	
	
	

}
