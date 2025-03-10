package com.raybiztech.appraisalmanagement.dto;

import java.util.Map;
import java.util.Set;

import com.raybiztech.appraisals.dto.EmployeeDTO;

public class AppraisalFormDto {

	private Long id;
	private AppraisalCycleDto appraisalCycle;
	private Set<AppraisalKRADataDto> kra;
	private EmployeeDTO employee;
	private String formStatus;
	private Integer formStatusvalue;
	private Map<String, Float> formRating;
	private String appraisalFormStatus;
	private String adjustedAvgRating;
	private String finalFeedback;
	private Set<AppraisalFormAvgRatingsDto> avgRatingsDtos;
	private Double overallAvgRating;
	private String overallAvgRatingName;
	private Long finalRating;
	private String finalRatingName;
	private String discussionOn;
	private String discussionSummary;
	private String openForDiscussionFlag;
	private Boolean iAgreeFlag;
	private String closedSummary;
	private String closedOn;
	private String pendingWith;
	private String closedStatus;
	private String closedBy;
	private String empDepartmentName;
	private String empDesignationName;
	private Double empAvgRating;
	private String empAvgRatingName;
	private String manager1Name;
	private Boolean requestDiscussion;
	private Set<AppraisalKPIDataDto> kpis;

	// private String firstMangerName;
	// private String firstManagerDeptName;
	// private String firstManagerDesigName;
	// private String secondMangerName;
	// private String secondManagerDeptName;
	// private String secondManagerDesigName;

	public String getFormStatus() {
		return formStatus;
	}

	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AppraisalCycleDto getAppraisalCycle() {
		return appraisalCycle;
	}

	public void setAppraisalCycle(AppraisalCycleDto appraisalCycle) {
		this.appraisalCycle = appraisalCycle;
	}

	public Set<AppraisalKRADataDto> getKra() {
		return kra;
	}

	public void setKra(Set<AppraisalKRADataDto> kra) {
		this.kra = kra;
	}

	public EmployeeDTO getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeDTO employee) {
		this.employee = employee;
	}

	public Integer getFormStatusvalue() {
		return formStatusvalue;
	}

	public void setFormStatusvalue(Integer formStatusvalue) {
		this.formStatusvalue = formStatusvalue;
	}

	public Map<String, Float> getFormRating() {
		return formRating;
	}

	public void setFormRating(Map<String, Float> formRating) {
		this.formRating = formRating;
	}

	public String getAppraisalFormStatus() {
		return appraisalFormStatus;
	}

	public void setAppraisalFormStatus(String appraisalFormStatus) {
		this.appraisalFormStatus = appraisalFormStatus;
	}

	public String getAdjustedAvgRating() {
		return adjustedAvgRating;
	}

	public void setAdjustedAvgRating(String adjustedAvgRating) {
		this.adjustedAvgRating = adjustedAvgRating;
	}

	public String getFinalFeedback() {
		return finalFeedback;
	}

	public void setFinalFeedback(String finalFeedback) {
		this.finalFeedback = finalFeedback;
	}

	public Set<AppraisalFormAvgRatingsDto> getAvgRatingsDtos() {
		return avgRatingsDtos;
	}

	public void setAvgRatingsDtos(Set<AppraisalFormAvgRatingsDto> avgRatingsDtos) {
		this.avgRatingsDtos = avgRatingsDtos;
	}

	public Double getOverallAvgRating() {
		return overallAvgRating;
	}

	public void setOverallAvgRating(Double overallAvgRating) {
		this.overallAvgRating = overallAvgRating;
	}

	public Long getFinalRating() {
		return finalRating;
	}

	public void setFinalRating(Long finalRating) {
		this.finalRating = finalRating;
	}

	public String getDiscussionOn() {
		return discussionOn;
	}

	public void setDiscussionOn(String discussionOn) {
		this.discussionOn = discussionOn;
	}

	public String getDiscussionSummary() {
		return discussionSummary;
	}

	public void setDiscussionSummary(String discussionSummary) {
		this.discussionSummary = discussionSummary;
	}

	public String getOverallAvgRatingName() {
		return overallAvgRatingName;
	}

	public void setOverallAvgRatingName(String overallAvgRatingName) {
		this.overallAvgRatingName = overallAvgRatingName;
	}

	public String getFinalRatingName() {
		return finalRatingName;
	}

	public void setFinalRatingName(String finalRatingName) {
		this.finalRatingName = finalRatingName;
	}

	public String getOpenForDiscussionFlag() {
		return openForDiscussionFlag;
	}

	public void setOpenForDiscussionFlag(String openForDiscussionFlag) {
		this.openForDiscussionFlag = openForDiscussionFlag;
	}

	public Boolean getiAgreeFlag() {
		return iAgreeFlag;
	}

	public void setiAgreeFlag(Boolean iAgreeFlag) {
		this.iAgreeFlag = iAgreeFlag;
	}

	public String getClosedSummary() {
		return closedSummary;
	}

	public void setClosedSummary(String closedSummary) {
		this.closedSummary = closedSummary;
	}

	public String getClosedOn() {
		return closedOn;
	}

	public void setClosedOn(String closedOn) {
		this.closedOn = closedOn;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public String getClosedStatus() {
		return closedStatus;
	}

	public void setClosedStatus(String closedStatus) {
		this.closedStatus = closedStatus;
	}

	public String getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}

	public String getEmpDepartmentName() {
		return empDepartmentName;
	}

	public void setEmpDepartmentName(String empDepartmentName) {
		this.empDepartmentName = empDepartmentName;
	}

	public String getEmpDesignationName() {
		return empDesignationName;
	}

	public void setEmpDesignationName(String empDesignationName) {
		this.empDesignationName = empDesignationName;
	}

	public String getManager1Name() {
		return manager1Name;
	}

	public void setManager1Name(String manager1Name) {
		this.manager1Name = manager1Name;
	}

	public Double getEmpAvgRating() {
		return empAvgRating;
	}

	public void setEmpAvgRating(Double empAvgRating) {
		this.empAvgRating = empAvgRating;
	}

	public String getEmpAvgRatingName() {
		return empAvgRatingName;
	}

	public void setEmpAvgRatingName(String empAvgRatingName) {
		this.empAvgRatingName = empAvgRatingName;
	}

	public Boolean getRequestDiscussion() {
		return requestDiscussion;
	}

	public void setRequestDiscussion(Boolean requestDiscussion) {
		this.requestDiscussion = requestDiscussion;
	}

	public Set<AppraisalKPIDataDto> getKpis() {
		return kpis;
	}

	public void setKpis(Set<AppraisalKPIDataDto> kpis) {
		this.kpis = kpis;
	}

}
