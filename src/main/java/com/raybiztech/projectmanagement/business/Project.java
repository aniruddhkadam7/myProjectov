package com.raybiztech.projectmanagement.business;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.projectnewsfeed.business.ProjectFeedPost;

public class Project implements Serializable, Cloneable {

	/**
     *
     */

	Logger logger = Logger.getLogger(Project.class);
	private static final long serialVersionUID = 1L;
	private Long id;
	public String projectName;
	public Employee projectManager;
	public DateRange period;
	public String description;
	public ProjectStatus status;
	private Set<AllocationDetails> allocationDetails;
	private Set<Milestone> milestones;
	private Set<StatusReport> statusReports;
	public String health;
	private Set<ProjectFeedPost> feedPost;
	public Client client;
	public ProjectType type;
	private Date createdDate;
	private Boolean internalOrNot;
	private String hiveProjectName;
	private ProjectRequest projectRequest;
	private ProjectModel model;
	private String projectCode;
	private String projectContactPerson;
	private String projectContactEmail;
	private String billingContactPerson;
	private String billingContactPersonEmail;
	private String platform;
	private String domain;
	//These two fields are used for creating project in hive at the time of project approval
	private Boolean hiveProjectFlag;
	private Boolean proformaInvoiceFlag = false;
	
	public Boolean getProformaInvoiceFlag() {
		return proformaInvoiceFlag;
	}

	public void setProformaInvoiceFlag(Boolean proformaInvoiceFlag) {
		this.proformaInvoiceFlag = proformaInvoiceFlag;
	}

	public ProjectRequest getProjectRequest() {
		return projectRequest;
	}

	public void setProjectRequest(ProjectRequest projectRequest) {
		this.projectRequest = projectRequest;
	}

	public ProjectType getType() {
		return type;
	}

	public void setType(ProjectType type) {
		this.type = type;
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

	public DateRange getPeriod() {
		return period;
	}

	public void setPeriod(DateRange period) {
		this.period = period;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	public Employee getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(Employee projectManager) {
		this.projectManager = projectManager;
	}

	public Set<AllocationDetails> getAllocationDetails() {
		return allocationDetails;
	}

	public void setAllocationDetails(Set<AllocationDetails> allocationDetails) {
		this.allocationDetails = allocationDetails;
	}

	public Set<Milestone> getMilestones() {
		return milestones;
	}

	public void setMilestones(Set<Milestone> milestones) {
		this.milestones = milestones;
	}

	public Set<StatusReport> getStatusReports() {
		return statusReports;
	}

	public void setStatusReports(Set<StatusReport> statusReports) {
		this.statusReports = statusReports;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public Set<ProjectFeedPost> getFeedPost() {
		return feedPost;
	}

	public void setFeedPost(Set<ProjectFeedPost> feedPost) {
		this.feedPost = feedPost;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public ProjectModel getModel() {
		return model;
	}

	public void setModel(ProjectModel model) {
		this.model = model;
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

	public Set<Milestone> getClosedBillableMileStones() {

		Set<Milestone> milestoneSet = null;
		if (this.getMilestones() != null) {

			milestoneSet = new HashSet<Milestone>();
			for (Milestone milestone : this.getMilestones()) {

				if (milestone.isBillable() && milestone.isClosed()
						&& !milestone.getInvoiceStatus()) {

					milestoneSet.add(milestone);
				}
			}

		}

		return Collections.unmodifiableSet(milestoneSet);

	}

	// this method is get all milestone dtos while editing Invoice
	public Set<Milestone> getClosedBillableMileStonesForEdit() {

		Set<Milestone> milestoneSet = null;
		if (this.getMilestones() != null) {

			milestoneSet = new HashSet<Milestone>();
			for (Milestone milestone : this.getMilestones()) {

				if (milestone.isBillable() && milestone.isClosed()) {

					milestoneSet.add(milestone);
				}
			}

		}

		return Collections.unmodifiableSet(milestoneSet);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Project other = (Project) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Project [projectName=" + projectName + ", period=" + period
				+ ", description=" + description + ", status=" + status
				+ ", allocationDetails=" + allocationDetails + ", milestones="
				+ milestones + ", statusReports=" + statusReports + ", health="
				+ health + "]";
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Boolean getInternalOrNot() {
		return internalOrNot;
	}

	public void setInternalOrNot(Boolean internalOrNot) {
		this.internalOrNot = internalOrNot;
	}

	public String getHiveProjectName() {
		return hiveProjectName;
	}

	public void setHiveProjectName(String hiveProjectName) {
		this.hiveProjectName = hiveProjectName;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public Boolean getHiveProjectFlag() {
		return hiveProjectFlag;
	}

	public void setHiveProjectFlag(Boolean hiveProjectFlag) {
		this.hiveProjectFlag = hiveProjectFlag;
	}
	
	
}
