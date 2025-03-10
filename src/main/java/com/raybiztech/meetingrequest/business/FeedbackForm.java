package com.raybiztech.meetingrequest.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;

public class FeedbackForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private MeetingRequest event;
	private String feedbackFormPath;
	private String feedBackFormName;
	private Employee createdBy;
	private Date createdDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public MeetingRequest getEvent() {
		return event;
	}
	public void setEvent(MeetingRequest event) {
		this.event = event;
	}
	public String getFeedbackFormPath() {
		return feedbackFormPath;
	}
	public void setFeedbackFormPath(String feedbackFormPath) {
		this.feedbackFormPath = feedbackFormPath;
	}
	public String getFeedBackFormName() {
		return feedBackFormName;
	}
	public void setFeedBackFormName(String feedBackFormName) {
		this.feedBackFormName = feedBackFormName;
	}
	public Employee getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Employee createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		return "FeedbackForm [id=" + id + ", event=" + event + ", feedbackFormPath=" + feedbackFormPath
				+ ", feedBackFormName=" + feedBackFormName + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ "]";
	}
	
	
	
}
