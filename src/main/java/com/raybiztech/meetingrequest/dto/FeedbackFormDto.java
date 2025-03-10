package com.raybiztech.meetingrequest.dto;

public class FeedbackFormDto {

	private Long id;
	private Long eventId;
	private String feedbackFormPath;
	private Long employeeId;
	private String createdBy;
	private String createdDate;
	private String feedBackFormName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEventId() {
		return eventId;
	}
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	public String getFeedbackFormPath() {
		return feedbackFormPath;
	}
	public void setFeedbackFormPath(String feedbackFormPath) {
		this.feedbackFormPath = feedbackFormPath;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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
	public String getFeedBackFormName() {
		return feedBackFormName;
	}
	public void setFeedBackFormName(String feedBackFormName) {
		this.feedBackFormName = feedBackFormName;
	}
	
	
	
}
