/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisalmanagement.dto;

/**
 *
 * @author anil
 */
public class AppraisalFormAvgRatingsDto {

	private Long id;
	private String employeeName;
	private Long employeeId;
	private Integer level;
	private Double defaultAvgRating;
	private Double adjustedAvgRating;
	private String finalFeedback;
	private String defaultAvgRatingName;
	private String adjustedAvgRatingName;
	private Boolean iAgree;
	private String departmentName;
	private String designationName;
	public String discussionSummary;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Double getDefaultAvgRating() {
		return defaultAvgRating;
	}

	public void setDefaultAvgRating(Double defaultAvgRating) {
		this.defaultAvgRating = defaultAvgRating;
	}

	public Double getAdjustedAvgRating() {
		return adjustedAvgRating;
	}

	public void setAdjustedAvgRating(Double adjustedAvgRating) {
		this.adjustedAvgRating = adjustedAvgRating;
	}

	public String getFinalFeedback() {
		return finalFeedback;
	}

	public void setFinalFeedback(String finalFeedback) {
		this.finalFeedback = finalFeedback;
	}

	public String getDefaultAvgRatingName() {
		return defaultAvgRatingName;
	}

	public void setDefaultAvgRatingName(String defaultAvgRatingName) {
		this.defaultAvgRatingName = defaultAvgRatingName;
	}

	public String getAdjustedAvgRatingName() {
		return adjustedAvgRatingName;
	}

	public void setAdjustedAvgRatingName(String adjustedAvgRatingName) {
		this.adjustedAvgRatingName = adjustedAvgRatingName;
	}

	public Boolean getiAgree() {
		return iAgree;
	}

	public void setiAgree(Boolean iAgree) {
		this.iAgree = iAgree;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getDesignationName() {
		return designationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public String getDiscussionSummary() {
		return discussionSummary;
	}

	public void setDiscussionSummary(String discussionSummary) {
		this.discussionSummary = discussionSummary;
	}

}
