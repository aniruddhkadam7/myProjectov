package com.raybiztech.projectmanagement.dto;

public class ProjectRequestMilestoneDTO {
	private Long id;
	private String title;
	private String effort;
	private String fromDate;
	private String toDate;
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	private String comments;
	private Boolean billable;
	private String milestonePercentage;
	
	public Boolean getBillable() {
		return billable;
	}
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
	public String getMilestonePercentage() {
		return milestonePercentage;
	}
	public void setMilestonePercentage(String milestonePercentage) {
		this.milestonePercentage = milestonePercentage;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEffort() {
		return effort;
	}
	public void setEffort(String effort) {
		this.effort = effort;
	}


	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	

}
