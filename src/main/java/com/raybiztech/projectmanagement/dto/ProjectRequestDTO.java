/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.projectmanagement.dto;

import java.util.Set;

import com.raybiztech.projectmanagement.business.ProjectModel;

/**
 *
 * @author anil
 */
public class ProjectRequestDTO {

	private Long id;
	private String projectName;
	private Long managerId;
	private String startdate;
	private String enddate;
	private String description;
	private String requiredResources;
	private String status;
	private String managerName;
	private String client;
	private String type;
	private Long clientId;
	private String newClient;
	private String requestedBy;
	private String technology;
	private String address;
	private String personName;
	private String email;
	private String country;
	private String organization;
	private Boolean intrnalOrNot;
	private String cc;
	private String bcc;
	private Set<ProjectInitiationChecklistDTO> chelist;
	private String model;
	private Boolean checkListExist;
	private String projectContactPerson;
	private String projectContactEmail;
	private String billingContactPerson;
	private String billingContactPersonEmail;
	private Set<ProjectRequestMilestoneDTO> projectRequestMilestoneDTO;
	private String platform;
	private String Domain;
	private boolean isAccess = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequiredResources() {
		return requiredResources;
	}

	public void setRequiredResources(String requiredResources) {
		this.requiredResources = requiredResources;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public String getNewClient() {
		return newClient;
	}

	public void setNewClient(String newClient) {
		this.newClient = newClient;
	}

	public String getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(String requestedBy) {
		this.requestedBy = requestedBy;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public Boolean getIntrnalOrNot() {
		return intrnalOrNot;
	}

	public void setIntrnalOrNot(Boolean intrnalOrNot) {
		this.intrnalOrNot = intrnalOrNot;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public Set<ProjectInitiationChecklistDTO> getChelist() {
		return chelist;
	}

	public void setChelist(Set<ProjectInitiationChecklistDTO> chelist) {
		this.chelist = chelist;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Boolean getCheckListExist() {
		return checkListExist;
	}

	public void setCheckListExist(Boolean checkListExist) {
		this.checkListExist = checkListExist;
	}


	public String getProjectContactPerson() {
		return projectContactPerson;
	}

	public void setProjectContactPerson(String projectContactPerson) {
		this.projectContactPerson = projectContactPerson;
	}

	public String getProjectContactEmail() {
		return projectContactEmail;
	}

	public void setProjectContactEmail(String projectContactEmail) {
		this.projectContactEmail = projectContactEmail;
	}

	public String getBillingContactPerson() {
		return billingContactPerson;
	}

	public void setBillingContactPerson(String billingContactPerson) {
		this.billingContactPerson = billingContactPerson;
	}

	public String getBillingContactPersonEmail() {
		return billingContactPersonEmail;
	}

	public void setBillingContactPersonEmail(String billingContactPersonEmail) {
		this.billingContactPersonEmail = billingContactPersonEmail;
	}

	public Set<ProjectRequestMilestoneDTO> getProjectRequestMilestoneDTO() {
		return projectRequestMilestoneDTO;
	}

	public void setProjectRequestMilestoneDTO(
			Set<ProjectRequestMilestoneDTO> projectRequestMilestoneDTO) {
		this.projectRequestMilestoneDTO = projectRequestMilestoneDTO;

	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getDomain() {
		return Domain;
	}

	public void setDomain(String domain) {
		Domain = domain;
	}

	public boolean isAccess() {
		return isAccess;
	}

	public void setAccess(boolean isAccess) {
		this.isAccess = isAccess;
	}
	
	

}
