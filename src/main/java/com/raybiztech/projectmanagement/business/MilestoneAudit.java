package com.raybiztech.projectmanagement.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class MilestoneAudit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5013536959419684215L;
	private Long id;
	private String title;
	private Date planedDate;
	private Date actualDate;
	private Boolean billable;
	private String comments;
	private Second modifiedDate;
	private String modifiedBy;
	private Long milestoneId;
	private String persistType;
	private Set<MilestonePeopleAudit> milestonePeopleAudit;
	private String milestonePercentage;
	private Boolean milestoneTypeFlag;

	public MilestoneAudit() {
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

	public Date getPlanedDate() {
		return planedDate;
	}

	public void setPlanedDate(Date planedDate) {
		this.planedDate = planedDate;
	}

	public Date getActualDate() {
		return actualDate;
	}

	public void setActualDate(Date actualDate) {
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

	public Second getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Second modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getMilestoneId() {
		return milestoneId;
	}

	public void setMilestoneId(Long milestoneId) {
		this.milestoneId = milestoneId;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getPersistType() {
		return persistType;
	}

	public void setPersistType(String persistType) {
		this.persistType = persistType;
	}

	public Set<MilestonePeopleAudit> getMilestonePeopleAudit() {
		return milestonePeopleAudit;
	}

	public void setMilestonePeopleAudit(Set<MilestonePeopleAudit> milestonePeopleAudit) {
		this.milestonePeopleAudit = milestonePeopleAudit;
	}

	public String getMilestonePercentage() {
		return milestonePercentage;
	}

	public void setMilestonePercentage(String milestonePercentage) {
		this.milestonePercentage = milestonePercentage;
	}

	
	public Boolean getMilestoneTypeFlag() {
		return milestoneTypeFlag;
	}

	public void setMilestoneTypeFlag(Boolean milestoneTypeFlag) {
		this.milestoneTypeFlag = milestoneTypeFlag;
	}
	
	

	
}
