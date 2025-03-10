/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author naresh
 */
public class MilestoneDTO implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String title;
	private String milestoneNumber;
	private String planedDate;
	private String actualDate;
	private Boolean billable;
	private String comments;
	private String project;
	private String client;
	private Long projectId;
	private Boolean isClosed;
	private String milestonePercentage;
	private List<MilestonePeopleDTO> milestonePeopleDTO;
	private List<ReportDTO> allocatedMilestonePeople;
	private Long crId;
	private String crName;
	private String crDuration;
	private Boolean invoiceStatus;
	private String projectType;
	private Long effort;
	private Boolean invoiceReopenFlag;
	private Boolean enableReopenFlag;
	private Boolean invoiceExits;
	private Boolean milestoneTypeFlag;
	private String milestoneAmount;
	private String raisedInvoicePercentage;
	private String remainingPercentage;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMilestoneNumber() {
		return milestoneNumber;
	}
	public void setMilestoneNumber(String milestoneNumber) {
		this.milestoneNumber = milestoneNumber;
	}
	public String getPlanedDate() {
		return planedDate;
	}
	public void setPlanedDate(String planedDate) {
		this.planedDate = planedDate;
	}
	public String getActualDate() {
		return actualDate;
	}
	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}
	public Boolean getBillable() {
		return billable;
	}
	public void setBillable(Boolean billable) {
		this.billable = billable;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Boolean getIsClosed() {
		return isClosed;
	}
	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}
	public String getMilestonePercentage() {
		return milestonePercentage;
	}
	public void setMilestonePercentage(String milestonePercentage) {
		this.milestonePercentage = milestonePercentage;
	}
	public List<MilestonePeopleDTO> getMilestonePeopleDTO() {
		return milestonePeopleDTO;
	}
	public void setMilestonePeopleDTO(List<MilestonePeopleDTO> milestonePeopleDTO) {
		this.milestonePeopleDTO = milestonePeopleDTO;
	}
	public List<ReportDTO> getAllocatedMilestonePeople() {
		return allocatedMilestonePeople;
	}
	public void setAllocatedMilestonePeople(List<ReportDTO> allocatedMilestonePeople) {
		this.allocatedMilestonePeople = allocatedMilestonePeople;
	}
	public Long getCrId() {
		return crId;
	}
	public void setCrId(Long crId) {
		this.crId = crId;
	}
	public String getCrName() {
		return crName;
	}
	public void setCrName(String crName) {
		this.crName = crName;
	}
	public String getCrDuration() {
		return crDuration;
	}
	public void setCrDuration(String crDuration) {
		this.crDuration = crDuration;
	}
	public Boolean getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(Boolean invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public Long getEffort() {
		return effort;
	}
	public void setEffort(Long effort) {
		this.effort = effort;
	}
	public Boolean getInvoiceReopenFlag() {
		return invoiceReopenFlag;
	}
	public void setInvoiceReopenFlag(Boolean invoiceReopenFlag) {
		this.invoiceReopenFlag = invoiceReopenFlag;
	}
	public Boolean getEnableReopenFlag() {
		return enableReopenFlag;
	}
	public void setEnableReopenFlag(Boolean enableReopenFlag) {
		this.enableReopenFlag = enableReopenFlag;
	}
	public Boolean getInvoiceExits() {
		return invoiceExits;
	}
	public void setInvoiceExits(Boolean invoiceExits) {
		this.invoiceExits = invoiceExits;
	}
	public Boolean getMilestoneTypeFlag() {
		return milestoneTypeFlag;
	}
	public void setMilestoneTypeFlag(Boolean milestoneTypeFlag) {
		this.milestoneTypeFlag = milestoneTypeFlag;
	}
	public String getMilestoneAmount() {
		return milestoneAmount;
	}
	public void setMilestoneAmount(String milestoneAmount) {
		this.milestoneAmount = milestoneAmount;
	}
	public String getRaisedInvoicePercentage() {
		return raisedInvoicePercentage;
	}
	public void setRaisedInvoicePercentage(String raisedInvoicePercentage) {
		this.raisedInvoicePercentage = raisedInvoicePercentage;
	}
	public String getRemainingPercentage() {
		return remainingPercentage;
	}
	public void setRemainingPercentage(String remainingPercentage) {
		this.remainingPercentage = remainingPercentage;
	}
	
	

	
	
	
	
	
	
	
	

}
