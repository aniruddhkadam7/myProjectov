package com.raybiztech.projectMetrics.dto;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;

public class EffortVarianceDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3697092102648742797L;
	private Long id;
	private Long projectId;
	private String baselineEffort;
	private String actualEffort;
	private String percentageOfCompletion;
	private String status;
	private Double effortVariance;
	private Double effortsConsumed;
	private String comments;
	private String createdBy;
	private String createdDate;
	private String createdTime;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getBaselineEffort() {
		return baselineEffort;
	}

	public void setBaselineEffort(String baselineEffort) {
		this.baselineEffort = baselineEffort;
	}

	public String getActualEffort() {
		return actualEffort;
	}

	public void setActualEffort(String actualEffort) {
		this.actualEffort = actualEffort;
	}

	public String getPercentageOfCompletion() {
		return percentageOfCompletion;
	}

	public void setPercentageOfCompletion(String percentageOfCompletion) {
		this.percentageOfCompletion = percentageOfCompletion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getEffortVariance() {
		return effortVariance;
	}

	public void setEffortVariance(Double effortVariance) {
		this.effortVariance = effortVariance;
	}

	public Double getEffortsConsumed() {
		return effortsConsumed;
	}

	public void setEffortsConsumed(Double effortsConsumed) {
		this.effortsConsumed = effortsConsumed;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

}
