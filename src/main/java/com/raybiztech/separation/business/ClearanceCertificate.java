package com.raybiztech.separation.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;

public class ClearanceCertificate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1806598011134205483L;
	private Long ccId;
	private Employee employee;
	private String comments;
	private Boolean isDue;
	private Second createdDate;
	private String addedBy;

	public Long getCcId() {
		return ccId;
	}

	public void setCcId(Long ccId) {
		this.ccId = ccId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	public Boolean getIsDue() {
		return isDue;
	}

	public void setIsDue(Boolean isDue) {
		this.isDue = isDue;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	
}
