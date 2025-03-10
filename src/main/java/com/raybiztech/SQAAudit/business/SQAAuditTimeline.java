package com.raybiztech.SQAAudit.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.Project;

public class SQAAuditTimeline implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7629211288666991374L;
	
	private Long id;
	private Long auditId;
	private String auditType;
	private Project project;
	private String auditors;
	private String auditees;
	private Date auditDate;
	private Second startTime;
	private Second endTime;
	private String auditStatus;
	private String formStatus;
	private Boolean auditRescheduleStatus;
	private String pci;
	private Date followUpDate;
	private String sqaComments;
	private String sqaFilesPath;
	private String pmComments;
	private String pmFilesPath;
	private Employee modifiedBy;
	private Second modifiedDate;
	private String persistType;
	private String projectName;
	private String projectManager;
	
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAuditId() {
		return auditId;
	}
	public void setAuditId(Long auditId) {
		this.auditId = auditId;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public String getAuditors() {
		return auditors;
	}
	public void setAuditors(String auditors) {
		this.auditors = auditors;
	}
	public String getAuditees() {
		return auditees;
	}
	public void setAuditees(String auditees) {
		this.auditees = auditees;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public Second getStartTime() {
		return startTime;
	}
	public void setStartTime(Second startTime) {
		this.startTime = startTime;
	}
	public Second getEndTime() {
		return endTime;
	}
	public void setEndTime(Second endTime) {
		this.endTime = endTime;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getFormStatus() {
		return formStatus;
	}
	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}
	public Boolean getAuditRescheduleStatus() {
		return auditRescheduleStatus;
	}
	public void setAuditRescheduleStatus(Boolean auditRescheduleStatus) {
		this.auditRescheduleStatus = auditRescheduleStatus;
	}
	public String getPci() {
		return pci;
	}
	public void setPci(String pci) {
		this.pci = pci;
	}
	public Date getFollowUpDate() {
		return followUpDate;
	}
	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}
	public String getSqaComments() {
		return sqaComments;
	}
	public void setSqaComments(String sqaComments) {
		this.sqaComments = sqaComments;
	}
	public String getSqaFilesPath() {
		return sqaFilesPath;
	}
	public void setSqaFilesPath(String sqaFilesPath) {
		this.sqaFilesPath = sqaFilesPath;
	}
	public String getPmComments() {
		return pmComments;
	}
	public void setPmComments(String pmComments) {
		this.pmComments = pmComments;
	}
	public String getPmFilesPath() {
		return pmFilesPath;
	}
	public void setPmFilesPath(String pmFilesPath) {
		this.pmFilesPath = pmFilesPath;
	}
	public Employee getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(Employee modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Second getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Second modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getPersistType() {
		return persistType;
	}
	public void setPersistType(String persistType) {
		this.persistType = persistType;
	}
	@Override
	public String toString() {
		return "SQAAuditTimeline [id=" + id + ", auditId=" + auditId + ", auditType=" + auditType + ", project="
				+ project + ", auditors=" + auditors + ", auditees=" + auditees + ", auditDate=" + auditDate
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", auditStatus=" + auditStatus + ", formStatus="
				+ formStatus + ", auditRescheduleStatus=" + auditRescheduleStatus + ", pci=" + pci + ", followUpDate="
				+ followUpDate + ", sqaComments=" + sqaComments + ", sqaFilesPath=" + sqaFilesPath + ", pmComments="
				+ pmComments + ", pmFilesPath=" + pmFilesPath + ", modifiedBy=" + modifiedBy + ", modifiedDate="
				+ modifiedDate + ", persistType=" + persistType + "]";
	}
	
	
	
}
