package com.raybiztech.projectMetrics.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.Project;

public class EffortVariance implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8686550757348497119L;
	
	private Long id;
	private Project project;
	private String baselineEffort;
	private String actualEffort;
	private String percentageOfCompletion;
	private String status;
	private Double effortVariance;
	private Double effortsConsumed;
	private String comments;
	private Employee createdBy;
	private Second createdDate;
	
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Employee getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Employee createdBy) {
		this.createdBy = createdBy;
	}
	public Second getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	
}
