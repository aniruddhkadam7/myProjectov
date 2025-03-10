package com.raybiztech.compliance.dto;

import java.util.Set;

import com.raybiztech.appraisals.dto.EmpDepartmentDTO;

public class ComplianceDTO {

	private Long complianceId;
	private String departmentName;	
	private String emailTo;
	private String complianceName;
	private String complianceDate;
	private Byte beforeNotification;
	private String recurring;
	private Byte escalation;
	private String escalationEmail;
	private String description;
	private Set<ComplianceTaskDTO> tasks;
	private String priority;
	
	public Long getComplianceId() {
		return complianceId;
	}
	public void setComplianceId(Long complianceId) {
		this.complianceId = complianceId;
	}
	
	
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getComplianceName() {
		return complianceName;
	}
	public void setComplianceName(String complianceName) {
		this.complianceName = complianceName;
	}
	public String getComplianceDate() {
		return complianceDate;
	}
	public void setComplianceDate(String complianceDate) {
		this.complianceDate = complianceDate;
	}
	public Byte getBeforeNotification() {
		return beforeNotification;
	}
	public void setBeforeNotification(Byte beforeNotification) {
		this.beforeNotification = beforeNotification;
	}
	public String getRecurring() {
		return recurring;
	}
	public void setRecurring(String recurring) {
		this.recurring = recurring;
	}
	public Byte getEscalation() {
		return escalation;
	}
	public void setEscalation(Byte escalation) {
		this.escalation = escalation;
	}
	public String getEscalationEmail() {
		return escalationEmail;
	}
	public void setEscalationEmail(String escalationEmail) {
		this.escalationEmail = escalationEmail;
	}
	public Set<ComplianceTaskDTO> getTasks() {
		return tasks;
	}
	public void setTasks(Set<ComplianceTaskDTO> tasks) {
		this.tasks = tasks;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
}
