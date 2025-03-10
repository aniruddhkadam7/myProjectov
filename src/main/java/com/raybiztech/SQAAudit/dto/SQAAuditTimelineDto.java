package com.raybiztech.SQAAudit.dto;

public class SQAAuditTimelineDto {

	private Long id;
	private Long auditId;
	private String auditType;
	private Long projectId;
	private String projectName;
	private String projectManager;
	private String auditors;
	private String auditees;
	private String auditDate;
	private String startTime;
	private String endTime;
	private String auditStatus;
	private String formStatus;
	private Boolean auditRescheduleStatus;
	private String pci;
	private String followUpDate;
	private String sqaComments;
	private String sqaFileName;
	private String sqaFilesPath;
	private String pmComments;
	private String pmFileName; 
	private String pmFilesPath;
	private String modifiedBy;
	private String modifiedDate;
	private String persistType;
	
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
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
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
	public String getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(String auditDate) {
		this.auditDate = auditDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
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
	public String getFollowUpDate() {
		return followUpDate;
	}
	public void setFollowUpDate(String followUpDate) {
		this.followUpDate = followUpDate;
	}
	public String getSqaComments() {
		return sqaComments;
	}
	public void setSqaComments(String sqaComments) {
		this.sqaComments = sqaComments;
	}
	public String getSqaFileName() {
		return sqaFileName;
	}
	public void setSqaFileName(String sqaFileName) {
		this.sqaFileName = sqaFileName;
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
	public String getPmFileName() {
		return pmFileName;
	}
	public void setPmFileName(String pmFileName) {
		this.pmFileName = pmFileName;
	}
	public String getPmFilesPath() {
		return pmFilesPath;
	}
	public void setPmFilesPath(String pmFilesPath) {
		this.pmFilesPath = pmFilesPath;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getPersistType() {
		return persistType;
	}
	public void setPersistType(String persistType) {
		this.persistType = persistType;
	}
	
	
}
