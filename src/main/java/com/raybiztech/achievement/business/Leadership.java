package com.raybiztech.achievement.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;

public class Leadership implements Serializable,Cloneable{
	
	/**
	 * @author Vishnu
	 */
	private static final long serialVersionUID = -2656017994030950869L;
	/**
	 * 
	 */
	private Long id;
	private Employee employee;
	private Boolean leader;
	private Boolean communicate;
	private Boolean initiative;
	private Boolean teamWorker;
	private Boolean constructiveCriticism;
	private Boolean helper;
	private Boolean directlyWorking;
	private Boolean travelOnsite;
	private Boolean innovationAndResearch;
	private String reasonDetails;
	private String expectationsExample;
	private String status;
	private String comments;
	private Date createdDate;
	private Date updatedDate;
	private Boolean acceptance;
	
	// Setters and Getters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean getLeader() {
		return leader;
	}
	public void setLeader(Boolean leader) {
		this.leader = leader;
	}
	public Boolean getCommunicate() {
		return communicate;
	}
	public void setCommunicate(Boolean communicate) {
		this.communicate = communicate;
	}
	public Boolean getInitiative() {
		return initiative;
	}
	public void setInitiative(Boolean initiative) {
		this.initiative = initiative;
	}
	public Boolean getTeamWorker() {
		return teamWorker;
	}
	public void setTeamWorker(Boolean teamWorker) {
		this.teamWorker = teamWorker;
	}
	public Boolean getConstructiveCriticism() {
		return constructiveCriticism;
	}
	public void setConstructiveCriticism(Boolean constructiveCriticism) {
		this.constructiveCriticism = constructiveCriticism;
	}
	public Boolean getHelper() {
		return helper;
	}
	public void setHelper(Boolean helper) {
		this.helper = helper;
	}
	public Boolean getDirectlyWorking() {
		return directlyWorking;
	}
	public void setDirectlyWorking(Boolean directlyWorking) {
		this.directlyWorking = directlyWorking;
	}
	public Boolean getTravelOnsite() {
		return travelOnsite;
	}
	public void setTravelOnsite(Boolean travelOnsite) {
		this.travelOnsite = travelOnsite;
	}
	public Boolean getInnovationAndResearch() {
		return innovationAndResearch;
	}
	public void setInnovationAndResearch(Boolean innovationAndResearch) {
		this.innovationAndResearch = innovationAndResearch;
	}
	public String getReasonDetails() {
		return reasonDetails;
	}
	public void setReasonDetails(String reasonDetails) {
		this.reasonDetails = reasonDetails;
	}
	public String getExpectationsExample() {
		return expectationsExample;
	}
	public void setExpectationsExample(String expectationsExample) {
		this.expectationsExample = expectationsExample;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Boolean getAcceptance() {
		return acceptance;
	}
	public void setAcceptance(Boolean acceptance) {
		this.acceptance = acceptance;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}


}
