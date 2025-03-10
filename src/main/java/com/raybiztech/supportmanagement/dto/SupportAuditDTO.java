package com.raybiztech.supportmanagement.dto;

public class SupportAuditDTO {
	private Long id;
	public String ticketsSubCategoryName;
	public String subject;
	public String description;
	public String status;
	public String priority;
	public String startDate;
	public String endDate;
	public String assignee;
	public String percentageDone;
	public String actualTime;
	public String documentsPath;
	public String approvalStatus;

	public String oldticketsSubCategoryName;
	public String oldsubject;
	public String olddescription;
	public String oldstatus;
	public String oldpriority;
	public String oldstartDate;
	public String oldendDate;
	public String oldassignee;
	public String oldpercentageDone;
	public String oldactualTime;
	public String olddocumentsPath;
	public String oldapprovalStatus;

	private String modifiedDate;
	private String modifiedBy;
	private String persistType;
	private String columnName;
	private String additionalInfo;

	private String subCategoryName;
	private String estimatedTime;
	private String workFlow;
	private String oldsubCategoryName;
	private String oldestimatedTime;
	private String oldworkFlow;
	private String approvedByManager;
	private String OldapprovedByManager;
	private String levelOfHierarchy;
	private String oldlevelOfHierarchy;
	private String tracker;
	private String oldtracker;
	private String accessStartDate;
	private String oldAccessStartDate;
	private String accessEndDate;
	private String OldAccessEndDate;

	public String getTracker() {
		return tracker;
	}

	public void setTracker(String tracker) {
		this.tracker = tracker;
	}

	public String getOldtracker() {
		return oldtracker;
	}

	public void setOldtracker(String oldtracker) {
		this.oldtracker = oldtracker;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public String getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(String estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public String getWorkFlow() {
		return workFlow;
	}

	public void setWorkFlow(String workFlow) {
		this.workFlow = workFlow;
	}

	public String getOldsubCategoryName() {
		return oldsubCategoryName;
	}

	public void setOldsubCategoryName(String oldsubCategoryName) {
		this.oldsubCategoryName = oldsubCategoryName;
	}

	public String getOldestimatedTime() {
		return oldestimatedTime;
	}

	public void setOldestimatedTime(String oldestimatedTime) {
		this.oldestimatedTime = oldestimatedTime;
	}

	public String getOldworkFlow() {
		return oldworkFlow;
	}

	public void setOldworkFlow(String oldworkFlow) {
		this.oldworkFlow = oldworkFlow;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTicketsSubCategoryName() {
		return ticketsSubCategoryName;
	}

	public void setTicketsSubCategoryName(String ticketsSubCategoryName) {
		this.ticketsSubCategoryName = ticketsSubCategoryName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getPercentageDone() {
		return percentageDone;
	}

	public void setPercentageDone(String percentageDone) {
		this.percentageDone = percentageDone;
	}

	public String getActualTime() {
		return actualTime;
	}

	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}

	public String getDocumentsPath() {
		return documentsPath;
	}

	public void setDocumentsPath(String documentsPath) {
		this.documentsPath = documentsPath;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getOldticketsSubCategoryName() {
		return oldticketsSubCategoryName;
	}

	public void setOldticketsSubCategoryName(String oldticketsSubCategoryName) {
		this.oldticketsSubCategoryName = oldticketsSubCategoryName;
	}

	public String getOldsubject() {
		return oldsubject;
	}

	public void setOldsubject(String oldsubject) {
		this.oldsubject = oldsubject;
	}

	public String getOlddescription() {
		return olddescription;
	}

	public void setOlddescription(String olddescription) {
		this.olddescription = olddescription;
	}

	public String getOldstatus() {
		return oldstatus;
	}

	public void setOldstatus(String oldstatus) {
		this.oldstatus = oldstatus;
	}

	public String getOldpriority() {
		return oldpriority;
	}

	public void setOldpriority(String oldpriority) {
		this.oldpriority = oldpriority;
	}

	public String getOldstartDate() {
		return oldstartDate;
	}

	public void setOldstartDate(String oldstartDate) {
		this.oldstartDate = oldstartDate;
	}

	public String getOldendDate() {
		return oldendDate;
	}

	public void setOldendDate(String oldendDate) {
		this.oldendDate = oldendDate;
	}

	public String getOldassignee() {
		return oldassignee;
	}

	public void setOldassignee(String oldassignee) {
		this.oldassignee = oldassignee;
	}

	public String getOldpercentageDone() {
		return oldpercentageDone;
	}

	public void setOldpercentageDone(String oldpercentageDone) {
		this.oldpercentageDone = oldpercentageDone;
	}

	public String getOldactualTime() {
		return oldactualTime;
	}

	public void setOldactualTime(String oldactualTime) {
		this.oldactualTime = oldactualTime;
	}

	public String getOlddocumentsPath() {
		return olddocumentsPath;
	}

	public void setOlddocumentsPath(String olddocumentsPath) {
		this.olddocumentsPath = olddocumentsPath;
	}

	public String getOldapprovalStatus() {
		return oldapprovalStatus;
	}

	public void setOldapprovalStatus(String oldapprovalStatus) {
		this.oldapprovalStatus = oldapprovalStatus;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPersistType() {
		return persistType;
	}

	public void setPersistType(String persistType) {
		this.persistType = persistType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getApprovedByManager() {
		return approvedByManager;
	}

	public void setApprovedByManager(String approvedByManager) {
		this.approvedByManager = approvedByManager;
	}

	public String getOldapprovedByManager() {
		return OldapprovedByManager;
	}

	public void setOldapprovedByManager(String oldapprovedByManager) {
		OldapprovedByManager = oldapprovedByManager;
	}

	public String getLevelOfHierarchy() {
		return levelOfHierarchy;
	}

	public void setLevelOfHierarchy(String levelOfHierarchy) {
		this.levelOfHierarchy = levelOfHierarchy;
	}

	public String getOldlevelOfHierarchy() {
		return oldlevelOfHierarchy;
	}

	public void setOldlevelOfHierarchy(String oldlevelOfHierarchy) {
		this.oldlevelOfHierarchy = oldlevelOfHierarchy;
	}

	public String getAccessStartDate() {
		return accessStartDate;
	}

	public void setAccessStartDate(String accessStartDate) {
		this.accessStartDate = accessStartDate;
	}

	public String getOldAccessStartDate() {
		return oldAccessStartDate;
	}

	public void setOldAccessStartDate(String oldAccessStartDate) {
		this.oldAccessStartDate = oldAccessStartDate;
	}

	public String getAccessEndDate() {
		return accessEndDate;
	}

	public void setAccessEndDate(String accessEndDate) {
		this.accessEndDate = accessEndDate;
	}

	public String getOldAccessEndDate() {
		return OldAccessEndDate;
	}

	public void setOldAccessEndDate(String oldAccessEndDate) {
		OldAccessEndDate = oldAccessEndDate;
	}

	
}
