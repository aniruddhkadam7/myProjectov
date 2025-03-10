package com.raybiztech.achievement.dto;

import java.io.Serializable;

/**
 * @author Aprajita
 */

public class AchievementDTO implements Serializable {

	private static final long serialVersionUID = 61378616004243303L;

	private Long id;
	private Long achievementTypeId;
	private String achievementType;
	private Long employeeId;
	private String employeeName;
	private String timePeriod;
	private String startDate;
	private String endDate;
	private String description;
	private String profilePicture;
	private String thumbPicture;
	private Boolean showOnDashBoard;
	private String createdBy;
	private String createdDate;
	private String updatedBy;
	private String updatedDate;
	private String croppedImageData;
	private Boolean timePeriodRequired;
	private Boolean dateRequired;
	


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getThumbPicture() {
		return thumbPicture;
	}

	public void setThumbPicture(String thumbPicture) {
		this.thumbPicture = thumbPicture;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public Boolean getShowOnDashBoard() {
		return showOnDashBoard;
	}

	public void setShowOnDashBoard(Boolean showOnDashBoard) {
		this.showOnDashBoard = showOnDashBoard;
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

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCroppedImageData() {
		return croppedImageData;
	}

	public void setCroppedImageData(String croppedImageData) {
		this.croppedImageData = croppedImageData;
	}


	public Boolean getTimePeriodRequired() {
		return timePeriodRequired;
	}

	public void setTimePeriodRequired(Boolean timePeriodRequired) {
		this.timePeriodRequired = timePeriodRequired;
	}

	public Boolean getDateRequired() {
		return dateRequired;
	}

	public void setDateRequired(Boolean dateRequired) {
		this.dateRequired = dateRequired;
	}




}
