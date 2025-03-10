package com.raybiztech.separation.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;

public class ExitFeedBack implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3055712154737679315L;
		
	private Long id;
	private Separation separation;
	private Employee employee;
	private PrimaryReason primaryReason;
	private String otherCommnets;
	private String salary;
	private String opportunityForGrowth;
	private String recognitionOfwork;
	private String promotion;
	private String educationalBackground;
	private String personelPolicies;
	private String organisationCulture;
	private String roleClarity;
	private String superiorGuidance;
	private String expectations;
	private String expectationsFulfilled;
	private String likeAboutCompany;
	private String dislikeAboutCompany;
	private String joinLater;
	private String relievingLetter;
	private String exitFeedbackForm;
	private Boolean isPIP;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Separation getSeparation() {
		return separation;
	}
	public void setSeparation(Separation separation) {
		this.separation = separation;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public PrimaryReason getPrimaryReason() {
		return primaryReason;
	}
	public void setPrimaryReason(PrimaryReason primaryReason) {
		this.primaryReason = primaryReason;
	}
	public String getOtherCommnets() {
		return otherCommnets;
	}
	public void setOtherCommnets(String otherCommnets) {
		this.otherCommnets = otherCommnets;
	}
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getOpportunityForGrowth() {
		return opportunityForGrowth;
	}
	public void setOpportunityForGrowth(String opportunityForGrowth) {
		this.opportunityForGrowth = opportunityForGrowth;
	}
	public String getRecognitionOfwork() {
		return recognitionOfwork;
	}
	public void setRecognitionOfwork(String recognitionOfwork) {
		this.recognitionOfwork = recognitionOfwork;
	}
	public String getPromotion() {
		return promotion;
	}
	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}
	public String getEducationalBackground() {
		return educationalBackground;
	}
	public void setEducationalBackground(String educationalBackground) {
		this.educationalBackground = educationalBackground;
	}
	public String getPersonelPolicies() {
		return personelPolicies;
	}
	public void setPersonelPolicies(String personelPolicies) {
		this.personelPolicies = personelPolicies;
	}
	public String getOrganisationCulture() {
		return organisationCulture;
	}
	public void setOrganisationCulture(String organisationCulture) {
		this.organisationCulture = organisationCulture;
	}
	public String getRoleClarity() {
		return roleClarity;
	}
	public void setRoleClarity(String roleClarity) {
		this.roleClarity = roleClarity;
	}
	public String getSuperiorGuidance() {
		return superiorGuidance;
	}
	public void setSuperiorGuidance(String superiorGuidance) {
		this.superiorGuidance = superiorGuidance;
	}
	public String getExpectations() {
		return expectations;
	}
	public void setExpectations(String expectations) {
		this.expectations = expectations;
	}
	public String getExpectationsFulfilled() {
		return expectationsFulfilled;
	}
	public void setExpectationsFulfilled(String expectationsFulfilled) {
		this.expectationsFulfilled = expectationsFulfilled;
	}
	public String getLikeAboutCompany() {
		return likeAboutCompany;
	}
	public void setLikeAboutCompany(String likeAboutCompany) {
		this.likeAboutCompany = likeAboutCompany;
	}
	public String getDislikeAboutCompany() {
		return dislikeAboutCompany;
	}
	public void setDislikeAboutCompany(String dislikeAboutCompany) {
		this.dislikeAboutCompany = dislikeAboutCompany;
	}
	public String getJoinLater() {
		return joinLater;
	}
	public void setJoinLater(String joinLater) {
		this.joinLater = joinLater;
	}
	public String getRelievingLetter() {
		return relievingLetter;
	}
	public void setRelievingLetter(String relievingLetter) {
		this.relievingLetter = relievingLetter;
	}
	public String getExitFeedbackForm() {
		return exitFeedbackForm;
	}
	public void setExitFeedbackForm(String exitFeedbackForm) {
		this.exitFeedbackForm = exitFeedbackForm;
	}
	public Boolean getIsPIP() {
		return isPIP;
	}
	public void setIsPIP(Boolean isPIP) {
		this.isPIP = isPIP;
	}
	
	
}
