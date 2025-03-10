package com.raybiztech.achievement.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

/**
 * @author Aprajita
 */

public class Achievement implements Serializable,Cloneable {

	private static final long serialVersionUID = -292733858313589L;

	private Long id;
	public AchievementType achievementType;
	public Employee employee;
	public String timePeriod;
	public Date startDate;
	public Date endDate;
	public String description;
	public String profilePicture;
	public String thumbPicture;
	public Boolean showOnDashBoard;
	public Long createdBy;
	public Second createdDate;
	public Long updatedBy;
	public Second updatedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AchievementType getAchievementType() {
		return achievementType;
	}

	public void setAchievementType(AchievementType achievementType) {
		this.achievementType = achievementType;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod(String timePeriod) {
		this.timePeriod = timePeriod;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public String getThumbPicture() {
		return thumbPicture;
	}

	public void setThumbPicture(String thumbPicture) {
		this.thumbPicture = thumbPicture;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getShowOnDashBoard() {
		return showOnDashBoard;
	}

	public void setShowOnDashBoard(Boolean showOnDashBoard) {
		this.showOnDashBoard = showOnDashBoard;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Second getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}
	

	 @Override
	    public Object clone() throws CloneNotSupportedException {
	        return super.clone();
	    }

}
