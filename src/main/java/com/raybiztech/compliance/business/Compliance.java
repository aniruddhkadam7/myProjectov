package com.raybiztech.compliance.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class Compliance implements Serializable{

	/**
	 * @author  venkatesh urlana 
	 */
	private static final long serialVersionUID = -2176324967201639045L;
	
	private Long complianceId;
	private EmpDepartment department;
	private String emailTo;
	private String complianceName;
	private Date complianceDate;
	private Byte beforeNotification;
	private Recurring recurring;
	private Byte escalation;
	private String escalationEmail;
	private Set<ComplianceTask> complianceTasks;
	private String description;
	private Priority priority;
	private Long createdBy;
	private Second createdDate;
	private Long updatedBy;
	private Second updatedDate;
	
	public Long getComplianceId() {
		return complianceId;
	}
	public void setComplianceId(Long complianceId) {
		this.complianceId = complianceId;
	}
	public EmpDepartment getDepartment() {
		return department;
	}
	public void setDepartment(EmpDepartment department) {
		this.department = department;
	}
	public String getComplianceName() {
		return complianceName;
	}
	public void setComplianceName(String complianceName) {
		this.complianceName = complianceName;
	}
	public Date getComplianceDate() {
		return complianceDate;
	}
	public void setComplianceDate(Date complianceDate) {
		this.complianceDate = complianceDate;
	}
	public Byte getBeforeNotification() {
		return beforeNotification;
	}
	public void setBeforeNotification(Byte beforeNotification) {
		this.beforeNotification = beforeNotification;
	}
	public Recurring getRecurring() {
		return recurring;
	}
	public void setRecurring(Recurring recurring) {
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
	public Set<ComplianceTask> getComplianceTasks() {
		return complianceTasks;
	}
	public void setComplianceTasks(Set<ComplianceTask> complianceTasks) {
		this.complianceTasks = complianceTasks;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getEmailTo() {
		return emailTo;
	}
	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}
	public Priority getPriority() {
		return priority;
	}
	public void setPriority(Priority priority) {
		this.priority = priority;
	}
	
}
