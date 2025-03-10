package com.raybiztech.achievementNomination.dto;

import java.io.Serializable;
import java.util.Set;

public class NominationDto implements Serializable {

	private static final long serialVersionUID = 6737145471585375922L;

	private Long id;
	private Long employeeId;
	private String employeeName;
	private Long achievementTypeId;
	private String achievementType;
	private Set<NominationQuestionDataDto> nominationQuestionDataDtosId;
	private Long cycleID;
	private String cycleName;
	private String fromMonth;
	private String toMonth;
	private Long rating;
	private String finalComments;
	private String nominationStatus;
	private String activateFlag;
	private String createdBy;
	private String createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getAchievementTypeId() {
		return achievementTypeId;
	}

	public void setAchievementTypeId(Long achievementTypeId) {
		this.achievementTypeId = achievementTypeId;
	}

	public String getAchievementType() {
		return achievementType;
	}

	public void setAchievementType(String achievementType) {
		this.achievementType = achievementType;
	}

	public Set<NominationQuestionDataDto> getNominationQuestionDataDtosId() {
		return nominationQuestionDataDtosId;
	}

	public void setNominationQuestionDataDtosId(
			Set<NominationQuestionDataDto> nominationQuestionDataDtosId) {
		this.nominationQuestionDataDtosId = nominationQuestionDataDtosId;
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

	public Long getCycleID() {
		return cycleID;
	}

	public void setCycleID(Long cycleID) {
		this.cycleID = cycleID;
	}

	public String getCycleName() {
		return cycleName;
	}

	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}

	public String getFromMonth() {
		return fromMonth;
	}

	public void setFromMonth(String fromMonth) {
		this.fromMonth = fromMonth;
	}

	public String getToMonth() {
		return toMonth;
	}

	public void setToMonth(String toMonth) {
		this.toMonth = toMonth;
	}

	public String getActivateFlag() {
		return activateFlag;
	}

	public void setActivateFlag(String activateFlag) {
		this.activateFlag = activateFlag;
	}

	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public String getFinalComments() {
		return finalComments;
	}

	public void setFinalComments(String finalComments) {
		this.finalComments = finalComments;
	}

	public String getNominationStatus() {
		return nominationStatus;
	}

	public void setNominationStatus(String nominationStatus) {
		this.nominationStatus = nominationStatus;
	}

}
