package com.raybiztech.projectmanagement.business;

import java.io.Serializable;

import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;

public class ProjectRequestMilestone implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1863215493243532659L;
	
	private Long id;
	private String milestoneTitle;
	private String effort;
	private DateRange period;
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

	private String comments;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getMilestoneTitle() {
		return milestoneTitle;
	}
	public void setMilestoneTitle(String milestoneTitle) {
		this.milestoneTitle = milestoneTitle;
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
	public DateRange getPeriod() {
		return period;
	}
	public void setPeriod(DateRange period) {
		this.period = period;
	}
	
	
	

}
