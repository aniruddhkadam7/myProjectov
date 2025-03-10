package com.raybiztech.appraisalmanagement.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;

public class ReviewAudit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3326113000377229431L;

	
	private Long id;
	private String comments;
	private String status;
	private Second createdDate;
	private Employee employee;
	private AppraisalForm appraisalForm;
	private String kpiName;
	private String oldValue;
	private String newValue;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public AppraisalForm getAppraisalForm() {
		return appraisalForm;
	}
	public void setAppraisalForm(AppraisalForm appraisalForm) {
		this.appraisalForm = appraisalForm;
	}
	public Second getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	
	
	
}
