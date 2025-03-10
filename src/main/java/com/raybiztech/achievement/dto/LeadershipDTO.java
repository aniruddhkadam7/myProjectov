package com.raybiztech.achievement.dto;

import java.io.Serializable;

import com.raybiztech.date.Date;

public class LeadershipDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -952738794806429572L;
	/**
	 * 
	 */
	 
		private Long id;
		private Long employeeId;
		private String employeeName;
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
		private String createdDate;
		private String updatedDate;
		private Boolean acceptance;
		private String designation;
		
		public Long getId() {
			return id;
		}
		public Long getEmployeeId() {
			return employeeId;
		}
		public String getEmployeeName() {
			return employeeName;
		}
		public Boolean getLeader() {
			return leader;
		}
		public Boolean getCommunicate() {
			return communicate;
		}
		public Boolean getInitiative() {
			return initiative;
		}
		public Boolean getTeamWorker() {
			return teamWorker;
		}
		public Boolean getConstructiveCriticism() {
			return constructiveCriticism;
		}
		public Boolean getHelper() {
			return helper;
		}
		public Boolean getDirectlyWorking() {
			return directlyWorking;
		}
		public Boolean getTravelOnsite() {
			return travelOnsite;
		}
		public Boolean getInnovationAndResearch() {
			return innovationAndResearch;
		}
		public String getReasonDetails() {
			return reasonDetails;
		}
		public String getExpectationsExample() {
			return expectationsExample;
		}
		public String getStatus() {
			return status;
		}
		public String getComments() {
			return comments;
		}
		public String getCreatedDate() {
			return createdDate;
		}
		public String getUpdatedDate() {
			return updatedDate;
		}
		public Boolean getAcceptance() {
			return acceptance;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public void setEmployeeId(Long employeeId) {
			this.employeeId = employeeId;
		}
		public void setEmployeeName(String employeeName) {
			this.employeeName = employeeName;
		}
		public void setLeader(Boolean leader) {
			this.leader = leader;
		}
		public void setCommunicate(Boolean communicate) {
			this.communicate = communicate;
		}
		public void setInitiative(Boolean initiative) {
			this.initiative = initiative;
		}
		public void setTeamWorker(Boolean teamWorker) {
			this.teamWorker = teamWorker;
		}
		public void setConstructiveCriticism(Boolean constructiveCriticism) {
			this.constructiveCriticism = constructiveCriticism;
		}
		public void setHelper(Boolean helper) {
			this.helper = helper;
		}
		public void setDirectlyWorking(Boolean directlyWorking) {
			this.directlyWorking = directlyWorking;
		}
		public void setTravelOnsite(Boolean travelOnsite) {
			this.travelOnsite = travelOnsite;
		}
		public void setInnovationAndResearch(Boolean innovationAndResearch) {
			this.innovationAndResearch = innovationAndResearch;
		}
		public void setReasonDetails(String reasonDetails) {
			this.reasonDetails = reasonDetails;
		}
		public void setExpectationsExample(String expectationsExample) {
			this.expectationsExample = expectationsExample;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
		public void setCreatedDate(String createdDate) {
			this.createdDate = createdDate;
		}
		public void setUpdatedDate(String updatedDate) {
			this.updatedDate = updatedDate;
		}
		public void setAcceptance(Boolean acceptance) {
			this.acceptance = acceptance;
		}
		public String getDesignation() {
			return designation;
		}
		public void setDesignation(String designation) {
			this.designation = designation;
		}

}
