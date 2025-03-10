/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.business;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.invoice.business.Invoice;

/**
 *
 * @author naresh
 */
public class Milestone implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String title;
	private String milestoneNumber;
	private Date planedDate;
	private Date actualDate;
	private Boolean billable;
	private String comments;
	private Project project;
	private String milestonePercentage;
	private Date addOn;
	private Boolean closed;
	private Invoice invoice;
	private Boolean invoiceStatus;
	private Set<MilestonePeople> milestonePeople;
	private ChangeRequest changeRequest;
	private Long effort;
	private Boolean invoiceReopenFlag;
	private Boolean milestoneTypeFlag;

	public Milestone() {

	}

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

	public Date getPlanedDate() {
		return planedDate;
	}

	public void setPlanedDate(Date planedDate) {
		this.planedDate = planedDate;
	}

	public Date getActualDate() {
		return actualDate;
	}

	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}

	public Boolean isBillable() {
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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Date getAddOn() {
		return addOn;
	}

	public void setAddOn(Date addOn) {
		this.addOn = addOn;
	}

	public Boolean isClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Boolean getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Boolean invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getMilestonePercentage() {
		return milestonePercentage;
	}

	public void setMilestonePercentage(String milestonePercentage) {
		this.milestonePercentage = milestonePercentage;
	}

	public Set<MilestonePeople> getMilestonePeople() {
		return milestonePeople;
	}

	public void setMilestonePeople(Set<MilestonePeople> milestonePeople) {
		this.milestonePeople = milestonePeople;
	}

	public ChangeRequest getChangeRequest() {
		return changeRequest;
	}

	public void setChangeRequest(ChangeRequest changeRequest) {
		this.changeRequest = changeRequest;
	}

	public String getMilestoneNumber() {
		return milestoneNumber;
	}

	public void setMilestoneNumber(String milestoneNumber) {
		this.milestoneNumber = milestoneNumber;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1989, 11).append(this.getId()).hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Milestone) {
			final Milestone milestone = (Milestone) obj;
			return new EqualsBuilder().append(this.getId(), milestone.getId())
					.isEquals();
		}
		return false;
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


	public Boolean getMilestoneTypeFlag() {
		return milestoneTypeFlag;
	}

	public void setMilestoneTypeFlag(Boolean milestoneTypeFlag) {
		this.milestoneTypeFlag = milestoneTypeFlag;
	}

	
	
	

}
