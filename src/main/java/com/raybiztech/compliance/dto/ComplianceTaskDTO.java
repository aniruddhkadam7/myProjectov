package com.raybiztech.compliance.dto;

public class ComplianceTaskDTO {
	
	private Long complianceTaskId;
	private String complianceName;
	private String createdDate;
	private String department;
	private String complianceDate;
	private String complianceTaskStatus;
	
	public Long getComplianceTaskId() {
		return complianceTaskId;
	}
	public void setComplianceTaskId(Long complianceTaskId) {
		this.complianceTaskId = complianceTaskId;
	}
	public String getComplianceName() {
		return complianceName;
	}
	public void setComplianceName(String complianceName) {
		this.complianceName = complianceName;
	}
	
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getComplianceDate() {
		return complianceDate;
	}
	public void setComplianceDate(String complianceDate) {
		this.complianceDate = complianceDate;
	}
	public String getComplianceTaskStatus() {
		return complianceTaskStatus;
	}
	public void setComplianceTaskStatus(String complianceTaskStatus) {
		this.complianceTaskStatus = complianceTaskStatus;
	} 
	
	
}
