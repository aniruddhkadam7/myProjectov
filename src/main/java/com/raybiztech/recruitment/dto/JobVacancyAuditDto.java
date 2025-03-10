package com.raybiztech.recruitment.dto;

import java.io.Serializable;

public class JobVacancyAuditDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private String jobCode;
    private String positionVacant;//title
    private String minimumExperience;
    private String description;
    private String opendDate;
    private String expiryDate;
    private String noOfRequirements;
    private String offered;
    private String remaining;
    private String status;
    
    private String  oldjobCode;
    private String  oldpositionVacant;//title
    private String  oldminimumExperience;
    private String  olddescription;
    private String  oldopendDate;
    private String  oldexpiryDate;
    private String oldnoOfRequirements;
    private String oldoffered;
    private String oldremaining;
    
    
    private String modifiedDate;
	private String modifiedBy;
	private String persistType;
	private String columnName;
	private String oldStatus; 
	
	public String getOldStatus() {
		return oldStatus;
	}
	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public String getPersistType() {
		return persistType;
	}
	public String getColumnName() {
		return columnName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public void setPersistType(String persistType) {
		this.persistType = persistType;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Long getId() {
		return id;
	}
	public String getJobCode() {
		return jobCode;
	}
	public String getPositionVacant() {
		return positionVacant;
	}
	public String getMinimumExperience() {
		return minimumExperience;
	}
	public String getDescription() {
		return description;
	}
	public String getOpendDate() {
		return opendDate;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	
	public String getOldjobCode() {
		return oldjobCode;
	}
	public String getOldpositionVacant() {
		return oldpositionVacant;
	}
	public String getOldminimumExperience() {
		return oldminimumExperience;
	}
	public String getOlddescription() {
		return olddescription;
	}
	public String getOldopendDate() {
		return oldopendDate;
	}
	public String getOldexpiryDate() {
		return oldexpiryDate;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	public void setPositionVacant(String positionVacant) {
		this.positionVacant = positionVacant;
	}
	public void setMinimumExperience(String minimumExperience) {
		this.minimumExperience = minimumExperience;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setOpendDate(String opendDate) {
		this.opendDate = opendDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public void setOldjobCode(String oldjobCode) {
		this.oldjobCode = oldjobCode;
	}
	public void setOldpositionVacant(String oldpositionVacant) {
		this.oldpositionVacant = oldpositionVacant;
	}
	public void setOldminimumExperience(String oldminimumExperience) {
		this.oldminimumExperience = oldminimumExperience;
	}
	public void setOlddescription(String olddescription) {
		this.olddescription = olddescription;
	}
	public void setOldopendDate(String oldopendDate) {
		this.oldopendDate = oldopendDate;
	}
	public void setOldexpiryDate(String oldexpiryDate) {
		this.oldexpiryDate = oldexpiryDate;
	}
	public String getNoOfRequirements() {
		return noOfRequirements;
	}
	public String getOffered() {
		return offered;
	}
	public String getRemaining() {
		return remaining;
	}
	public String getOldnoOfRequirements() {
		return oldnoOfRequirements;
	}
	public String getOldoffered() {
		return oldoffered;
	}
	public String getOldremaining() {
		return oldremaining;
	}
	public void setNoOfRequirements(String noOfRequirements) {
		this.noOfRequirements = noOfRequirements;
	}
	public void setOffered(String offered) {
		this.offered = offered;
	}
	public void setRemaining(String remaining) {
		this.remaining = remaining;
	}
	public void setOldnoOfRequirements(String oldnoOfRequirements) {
		this.oldnoOfRequirements = oldnoOfRequirements;
	}
	public void setOldoffered(String oldoffered) {
		this.oldoffered = oldoffered;
	}
	public void setOldremaining(String oldremaining) {
		this.oldremaining = oldremaining;
	}
	
    
}
