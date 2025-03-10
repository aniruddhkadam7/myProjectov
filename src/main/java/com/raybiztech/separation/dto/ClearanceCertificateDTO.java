package com.raybiztech.separation.dto;

import java.io.Serializable;

public class ClearanceCertificateDTO implements Serializable {

	private static final long serialVersionUID = -2623220805146521930L;
	private Long ccId;
	private Long employeeId;
	private String employeeName;
	private String comments;
	private Boolean isDue;
	private String createdDate;
	private Long seperationId;
	private Long seperationEmpId;
	private String seperationEmpName;
	private String addedBy;

	public Long getCcId() {
		return ccId;
	}

	public void setCcId(Long ccId) {
		this.ccId = ccId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Boolean getIsDue() {
		return isDue;
	}

	public void setIsDue(Boolean isDue) {
		this.isDue = isDue;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Long getSeperationId() {
		return seperationId;
	}

	public void setSeperationId(Long seperationId) {
		this.seperationId = seperationId;
	}

	public Long getSeperationEmpId() {
		return seperationEmpId;
	}

	public void setSeperationEmpId(Long seperationEmpId) {
		this.seperationEmpId = seperationEmpId;
	}

	public String getSeperationEmpName() {
		return seperationEmpName;
	}

	public void setSeperationEmpName(String seperationEmpName) {
		this.seperationEmpName = seperationEmpName;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	
 
	
}
