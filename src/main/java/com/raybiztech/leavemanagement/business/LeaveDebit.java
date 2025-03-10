package com.raybiztech.leavemanagement.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;

public class LeaveDebit implements Serializable, Comparable<LeaveDebit> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1876925055476470102L;

	private Long id;
	private LeaveCategory leaveCategory;
	private DateRange period;
	private Double numberOfDays;
	private String employeeComments;
	private String managerComments;
	private Employee employee;
	private LeaveStatus status;
	private Date leaveAppliedOn;
	private int version;

	// Here we are putting approvedBy string because there is scheduler
	// which runs on last day of month and auto approves all pending
	// leaves in such case we need to set ApprovedBy "Auto Approved"
	private String approvedBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LeaveCategory getLeaveCategory() {
		return leaveCategory;
	}

	public void setLeaveCategory(LeaveCategory leaveCategory) {
		this.leaveCategory = leaveCategory;
	}

	public DateRange getPeriod() {
		return period;
	}

	public void setPeriod(DateRange period) {
		this.period = period;
	}

	public Double getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Double numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public String getEmployeeComments() {
		return employeeComments;
	}

	public void setEmployeeComments(String employeeComments) {
		this.employeeComments = employeeComments;
	}

	public String getManagerComments() {
		return managerComments;
	}

	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public LeaveStatus getStatus() {
		return status;
	}

	public void setStatus(LeaveStatus status) {
		this.status = status;
	}

	public Date getLeaveAppliedOn() {
		return leaveAppliedOn;
	}

	public void setLeaveAppliedOn(Date leaveAppliedOn) {
		this.leaveAppliedOn = leaveAppliedOn;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1989, 55).append(id).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final LeaveDebit other = (LeaveDebit) obj;
		return new EqualsBuilder().append(id, other.id).isEquals();
	}

	@Override
	public String toString() {
		return "LeaveDebits [id=" + id + ", leaveCategory="
				+ leaveCategory.getName() + employeeComments
				+ ", managerComments=" + managerComments + ", employee="
				+ employee.getFullName() + ", leaveAppliedOn=" + leaveAppliedOn
				+ "]";
	}

	@Override
	public int compareTo(LeaveDebit o) {
		// TODO Auto-generated method stub
		return (int) (this.id - o.id);
	}

}
