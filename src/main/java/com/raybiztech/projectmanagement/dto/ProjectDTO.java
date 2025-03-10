package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;
import java.util.Set;

public class ProjectDTO implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = -9038694851473926910L;
	private Long id;
	private String projectName;
	private Long managerId;
	private String startdate;
	private String enddate;
	private String description;
	private String status;
	private String managerName;
	private Long count;
	private String Allocation;
	private boolean billable;
	private Boolean isAllocated;
	private Long employeeId;
	private String health;
	private String client;
	private String type;
	private Long clientId;
	private String projectStartdate;
	private String projectEndDate;
	private String requiredResources;
	private String newClient;
	private String requestedBy;
	private String statuEditFlag;
	private String technology;
	private String address;
	private String personName;
	private String email;
	private String country;
	private String organization;
	private Boolean intrnalOrNot;
	private String hiveProjectName;
	private String cc;
	private String bcc;
	private String deliveryManager;
	private Long projectRequestId;
	private String model;
	private Boolean checkListExist;
	private String projectCode;
	private String projectContactPerson;
	private String projectContactEmail;
	private String billingContactPerson;
	private String billingContactPersonEmail;
	private Set<ProjectRequestMilestoneDTO> projectRequestMilestoneDTO;
	private String platform;
	private String domain;
	private String clientName;
	//These two fields are used for creating project in hive at the time of project approval
	private Boolean hiveProjectFlag;
	
	

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public Long getProjectRequestId() {
		return projectRequestId;
	}

	public void setProjectRequestId(Long projectRequestId) {
		this.projectRequestId = projectRequestId;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getAllocation() {
		return Allocation;
	}

	public void setAllocation(String Allocation) {
		this.Allocation = Allocation;
	}

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
	}

	public Boolean getIsAllocated() {
		return isAllocated;
	}

	public void setIsAllocated(Boolean isAllocated) {
		this.isAllocated = isAllocated;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
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

	public String getProjectStartdate() {
		return projectStartdate;
	}

	public void setProjectStartdate(String projectStartdate) {
		this.projectStartdate = projectStartdate;
	}

	public String getProjectEndDate() {
		return projectEndDate;
	}

	public void setProjectEndDate(String projectEndDate) {
		this.projectEndDate = projectEndDate;
	}

	public String getRequiredResources() {
		return requiredResources;
	}

	public void setRequiredResources(String requiredResources) {
		this.requiredResources = requiredResources;
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

	public String getStatuEditFlag() {
		return statuEditFlag;
	}

	public void setStatuEditFlag(String statuEditFlag) {
		this.statuEditFlag = statuEditFlag;
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

	public String getHiveProjectName() {
		return hiveProjectName;
	}

	public void setHiveProjectName(String hiveProjectName) {
		this.hiveProjectName = hiveProjectName;
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

	public String getDeliveryManager() {
		return deliveryManager;
	}

	public void setDeliveryManager(String deliveryManager) {
		this.deliveryManager = deliveryManager;
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
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Boolean getHiveProjectFlag() {
		return hiveProjectFlag;
	}

	public void setHiveProjectFlag(Boolean hiveProjectFlag) {
		this.hiveProjectFlag = hiveProjectFlag;
	}
	
	
}
