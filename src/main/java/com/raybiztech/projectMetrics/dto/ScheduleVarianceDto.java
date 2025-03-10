package com.raybiztech.projectMetrics.dto;

public class ScheduleVarianceDto {

	private Long id;
	private Long projectId;
	private String baseLineStartDate;
	private String baseLineEndDate;
	private String actualStartDate;
	private String actualEndDate;
	private String overAllSheduleVariance;
	private String comments;
	private Long createdBy;
	private String employeeName;
	private String createdDate;
	private String createdTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getBaseLineStartDate() {
		return baseLineStartDate;
	}

	public void setBaseLineStartDate(String baseLineStartDate) {
		this.baseLineStartDate = baseLineStartDate;
	}

	public String getBaseLineEndDate() {
		return baseLineEndDate;
	}

	public void setBaseLineEndDate(String baseLineEndDate) {
		this.baseLineEndDate = baseLineEndDate;
	}

	public String getActualStartDate() {
		return actualStartDate;
	}

	public void setActualStartDate(String actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	public String getActualEndDate() {
		return actualEndDate;
	}

	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	public String getOverAllSheduleVariance() {
		return overAllSheduleVariance;
	}

	public void setOverAllSheduleVariance(String overAllSheduleVariance) {
		this.overAllSheduleVariance = overAllSheduleVariance;
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

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

}
