package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;
import java.util.Set;

public class MileStoneAuditDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3219030963819659151L;

	private Long id;

	private String title;
	private String planedDate;
	private String actualDate;
	private Boolean billable;
	private String comments;
	private String modifiedDate;
	private String modifiedBy;
	private Long milestoneId;
	private String persistType;
	private String projectName;
	private String projectManager;
	private String projectType;
	private String projectStatus;
	private String milestonePercentage;
	private Set<MilestonePeopleDTO> peopleDTOs;
	private String milestoneAmount;
	private Boolean milestoneTypeFlag;
	

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

	public String getPlanedDate() {
		return planedDate;
	}

	public void setPlanedDate(String planedDate) {
		this.planedDate = planedDate;
	}

	public String getActualDate() {
		return actualDate;
	}

	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getMilestoneId() {
		return milestoneId;
	}

	public void setMilestoneId(Long milestoneId) {
		this.milestoneId = milestoneId;
	}

	public String getPersistType() {
		return persistType;
	}

	public void setPersistType(String persistType) {
		this.persistType = persistType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public Set<MilestonePeopleDTO> getPeopleDTOs() {
		return peopleDTOs;
	}

	public void setPeopleDTOs(Set<MilestonePeopleDTO> peopleDTOs) {
		this.peopleDTOs = peopleDTOs;
	}

	public String getMilestonePercentage() {
		return milestonePercentage;
	}

	public void setMilestonePercentage(String milestonePercentage) {
		this.milestonePercentage = milestonePercentage;
	}

	public String getMilestoneAmount() {
		return milestoneAmount;
	}

	public void setMilestoneAmount(String milestoneAmount) {
		this.milestoneAmount = milestoneAmount;
	}

	public Boolean getMilestoneTypeFlag() {
		return milestoneTypeFlag;
	}

	public void setMilestoneTypeFlag(Boolean milestoneTypeFlag) {
		this.milestoneTypeFlag = milestoneTypeFlag;
	}
	


}
