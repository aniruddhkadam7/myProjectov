package com.raybiztech.projectmanagement.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.commons.Percentage;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;

public class AllocationEffort implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4629012243962287670L;

	/**
	 * 
	 */

	/**
	 * 
	 */

	private Long id;

	public Percentage percentage;

	public DateRange period;

	public String comments;

	public Boolean billable;
	public Employee employee;
	private Project project;
	public Boolean isAllocated;
	private Second createdDate;
	private Second updatedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Percentage getPercentage() {
		return percentage;
	}

	public void setPercentage(Percentage percentage) {
		this.percentage = percentage;
	}

	public DateRange getPeriod() {
		return period;
	}

	public void setPeriod(DateRange period) {
		this.period = period;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Boolean getIsAllocated() {
		return isAllocated;
	}

	public void setIsAllocated(Boolean isAllocated) {
		this.isAllocated = isAllocated;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}
	

	public Second getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

}
