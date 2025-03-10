/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.supportmanagement.dto;

import java.io.Serializable;
import java.util.List;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

/**
 *
 * @author anil
 */
public class SupportTicketsDTO implements Serializable {

	private static final long serialVersionUID = 61378616004233305L;

	private Long id;
	private Long departmentId;
	private String departmentName;
	private Long categoryId;
	private String categoryName;
	private Long subCategoryId;
	private String subCategoryName;
	private String subject;
	private String description;
	private String status;
	private String priority;
	private String startDate;
	private String endDate;
	private Long assigneeId;
	private String employeeName;
	private int percentageDone;
	private String actualTime;
	private String authorName;
	private String assigneeName;
	private String approvalStatus;
	private String filePath;
	private String estimatedTime;

	private List<Long> watcherIds;
	private List<String> watcherNames;
	private Boolean disableApprove;
	private Boolean disableCancel;
	private Long tracker;
	private String trackerName;
	private String accessStartDate;
	private String accessEndDate;
	private String createdDate;
	private String approvedBy;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Long getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
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

	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	public int getPercentageDone() {
		return percentageDone;
	}

	public void setPercentageDone(int percentageDone) {
		this.percentageDone = percentageDone;
	}

	public String getActualTime() {
		return actualTime;
	}

	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(String estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public List<Long> getWatcherIds() {
		return watcherIds;
	}

	public void setWatcherIds(List<Long> watcherIds) {
		this.watcherIds = watcherIds;
	}

	public List<String> getWatcherNames() {
		return watcherNames;
	}

	public void setWatcherNames(List<String> watcherNames) {
		this.watcherNames = watcherNames;
	}

	public Boolean getDisableApprove() {
		return disableApprove;
	}

	public void setDisableApprove(Boolean disableApprove) {
		this.disableApprove = disableApprove;
	}

	public Boolean getDisableCancel() {
		return disableCancel;
	}

	public void setDisableCancel(Boolean disableCancel) {
		this.disableCancel = disableCancel;
	}

	public Long getTracker() {
		return tracker;
	}

	public void setTracker(Long tracker) {
		this.tracker = tracker;
	}

	public String getTrackerName() {
		return trackerName;
	}

	public void setTrackerName(String trackerName) {
		this.trackerName = trackerName;
	}

	public String getAccessStartDate() {
		return accessStartDate;
	}

	public void setAccessStartDate(String accessStartDate) {
		this.accessStartDate = accessStartDate;
	}

	public String getAccessEndDate() {
		return accessEndDate;
	}

	public void setAccessEndDate(String accessEndDate) {
		this.accessEndDate = accessEndDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	
	

}
