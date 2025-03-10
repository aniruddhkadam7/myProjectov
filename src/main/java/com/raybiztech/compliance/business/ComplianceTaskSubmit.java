package com.raybiztech.compliance.business;

import java.io.Serializable;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class ComplianceTaskSubmit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1768988237497328852L;
	
	private Long complianceTaskSubmitId;
	private Date actualSubmitDate;
	private String comments;
	private ComplianceTask complianceTask;
	private Long createdBy;
	private Second createdDate;
	
	public Long getComplianceTaskSubmitId() {
		return complianceTaskSubmitId;
	}
	public void setComplianceTaskSubmitId(Long complianceTaskSubmitId) {
		this.complianceTaskSubmitId = complianceTaskSubmitId;
	}
	public Date getActualSubmitDate() {
		return actualSubmitDate;
	}
	public void setActualSubmitDate(Date actualSubmitDate) {
		this.actualSubmitDate = actualSubmitDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
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
	public ComplianceTask getComplianceTask() {
		return complianceTask;
	}
	public void setComplianceTask(ComplianceTask complianceTask) {
		this.complianceTask = complianceTask;
	}
	
}
