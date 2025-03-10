package com.raybiztech.appraisals.dto;


public class ApplicantDTO extends PersonDTO{
	Float experience;
	String currentDesignation;
	Float currentCTC;
	String reasonForChange;
	String skill;
	String dateApplied;
	EmployeeDTO manager;
	String source;
	String vacancy;
	public ApplicantDTO(){
		
	}
	public Float getExperience() {
		return experience;
	}
	public void setExperience(Float experience) {
		this.experience = experience;
	}
	public String getCurrentDesignation() {
		return currentDesignation;
	}
	public void setCurrentDesignation(String currentDesignation) {
		this.currentDesignation = currentDesignation;
	}
	public Float getCurrentCTC() {
		return currentCTC;
	}
	public void setCurrentCTC(Float currentCTC) {
		this.currentCTC = currentCTC;
	}
	public String getReasonForChange() {
		return reasonForChange;
	}
	public void setReasonForChange(String reasonForChange) {
		this.reasonForChange = reasonForChange;
	}
	
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public String getDateApplied() {
		return dateApplied;
	}
	public void setDateApplied(String dateApplied) {
		this.dateApplied = dateApplied;
	}
	public EmployeeDTO getManager() {
		return manager;
	}
	public void setManager(EmployeeDTO manager) {
		this.manager = manager;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getVacancy() {
		return vacancy;
	}
	public void setVacancy(String vacancy) {
		this.vacancy = vacancy;
	}
	
	
}
