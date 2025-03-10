package com.raybiztech.appraisals.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class Qualification implements Serializable {

	private static final long serialVersionUID = 3786957196538277729L;

	private Long id;
	private String qualificationDetails;
	private Employee employee;
	private String sscName;
	private String hscName;
	private String others;
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
	public String getQualificationDetails() {
		return qualificationDetails;
	}

	public void setQualificationDetails(String qualificationDetails) {
		this.qualificationDetails = qualificationDetails;
	}
	
	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getSscName() {
		return sscName;
	}

	public void setSscName(String sscName) {
		this.sscName = sscName;
	}

	public String getHscName() {
		return hscName;
	}

	public void setHscName(String hscName) {
		this.hscName = hscName;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	

}
