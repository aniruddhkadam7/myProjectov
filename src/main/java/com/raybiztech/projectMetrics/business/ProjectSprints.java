package com.raybiztech.projectMetrics.business;

import java.io.Serializable;
import com.raybiztech.date.Date;

public class ProjectSprints implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5945090771482919540L;

	private Long id;
	private Integer versionId;
	private String versionName;
	private Date startDate;
	private Date effectiveDate;
	private Double actuallEffort;
	private String plannedEffort;
	private Long projectId;
	private String projectName;
	private Date actualStartDate;
	private Date actualEndDate;
	private String status;
	private Date projectedStartDate;
	private Date projectedEndDate;
	private String percentageOfCompletion;
	private Date createdDate;
	private Date updatedDate;
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getVersionId() {
		return versionId;
	}
	
	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}
	
	public String getVersionName() {
		return versionName;
	}
	
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	public Double getActuallEffort() {
		return actuallEffort;
	}
	
	public void setActuallEffort(Double actuallEffort) {
		this.actuallEffort = actuallEffort;
	}
	
	public String getPlannedEffort() {
		return plannedEffort;
	}
	
	public void setPlannedEffort(String plannedEffort) {
		this.plannedEffort = plannedEffort;
	}
	
	public Long getProjectId() {
		return projectId;
	}
	
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	
	public String getProjectName() {
		return projectName;
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public Date getActualStartDate() {
		return actualStartDate;
	}
	
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	
	public Date getActualEndDate() {
		return actualEndDate;
	}
	
	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Date getProjectedStartDate() {
		return projectedStartDate;
	}
	
	public void setProjectedStartDate(Date projectedStartDate) {
		this.projectedStartDate = projectedStartDate;
	}
	
	public Date getProjectedEndDate() {
		return projectedEndDate;
	}
	
	public void setProjectedEndDate(Date projectedEndDate) {
		this.projectedEndDate = projectedEndDate;
	}
	
	public String getPercentageOfCompletion() {
		return percentageOfCompletion;
	}
	
	public void setPercentageOfCompletion(String percentageOfCompletion) {
		this.percentageOfCompletion = percentageOfCompletion;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public Date getUpdatedDate() {
		return updatedDate;
	}
	
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

}
