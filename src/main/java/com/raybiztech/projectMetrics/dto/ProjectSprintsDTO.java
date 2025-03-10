package com.raybiztech.projectMetrics.dto;

import java.io.Serializable;
import java.util.Date;

public class ProjectSprintsDTO implements Serializable {

	/**
	 * .
	 */
	private static final long serialVersionUID = 1L;
	private Long Id;
	private String versionName;
	private Double actuallEffort;
	private String plannedEffort;
	private String actualStartDate;
	private String actualEndDate;
	private String baseLineStartDate;
	private String baseLineEndDate;
	private Double sheduleVariance;
	private String percentageOfCompletion;
	private String status;
	private String projectedStartDate;
	private String projectedEndDate;
	public String getPercentageOfCompletion() {
		return percentageOfCompletion;
	}

	public void setPercentageOfCompletion(String percentageOfCompletion) {
		this.percentageOfCompletion = percentageOfCompletion;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
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

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
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

	public Double getSheduleVariance() {
		return sheduleVariance;
	}

	public void setSheduleVariance(Double sheduleVariance) {
		this.sheduleVariance = sheduleVariance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProjectedStartDate() {
		return projectedStartDate;
	}

	public void setProjectedStartDate(String projectedStartDate) {
		this.projectedStartDate = projectedStartDate;
	}

	public String getProjectedEndDate() {
		return projectedEndDate;
	}

	public void setProjectedEndDate(String projectedEndDate) {
		this.projectedEndDate = projectedEndDate;
	}

	


}
