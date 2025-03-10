package com.raybiztech.SQAAudit.dto;

import java.util.Set;


public class SQAAuditFormDto {
	
	private Long id;
	private String auditType;
	private String projectType;
	private Long projectId;
	private String projectManager;
	private Set<Long> auditorIds;
	private Set<Long> auditeeIds;
	private Set<SQAAuditorsDto> auditors;
	private Set<SQAAuditeesDto> auditees;
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
	private String createdBy;
	private String createdDate;
	private String updatedBy;
	private String updatedDate;
	private Boolean disableEditButton;
	private String projectName;
	private Long projectManagerId;
	private Boolean showEditButton;
	private String comments;
	private Boolean  containsFile;
	private Boolean isSQA = false;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}
	public Set<Long> getAuditorIds() {
		return auditorIds;
	}
	public void setAuditorIds(Set<Long> auditorIds) {
		this.auditorIds = auditorIds;
	}
	public Set<Long> getAuditeeIds() {
		return auditeeIds;
	}
	public void setAuditeeIds(Set<Long> auditeeIds) {
		this.auditeeIds = auditeeIds;
	}
	public Set<SQAAuditorsDto> getAuditors() {
		return auditors;
	}
	public void setAuditors(Set<SQAAuditorsDto> auditors) {
		this.auditors = auditors;
	}
	public Set<SQAAuditeesDto> getAuditees() {
		return auditees;
	}
	public void setAuditees(Set<SQAAuditeesDto> auditees) {
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
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Boolean getDisableEditButton() {
		return disableEditButton;
	}
	public void setDisableEditButton(Boolean disableEditButton) {
		this.disableEditButton = disableEditButton;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public Long getProjectManagerId() {
		return projectManagerId;
	}
	public void setProjectManagerId(Long projectManagerId) {
		this.projectManagerId = projectManagerId;
	}
	public Boolean getShowEditButton() {
		return showEditButton;
	}
	public void setShowEditButton(Boolean showEditButton) {
		this.showEditButton = showEditButton;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Boolean getContainsFile() {
		return containsFile;
	}
	public void setContainsFile(Boolean containsFile) {
		this.containsFile = containsFile;
	}
	public Boolean getIsSQA() {
		return isSQA;
	}
	public void setIsSQA(Boolean isSQA) {
		this.isSQA = isSQA;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	
	
}
