package com.raybiztech.leavemanagement.dto;

import java.io.Serializable;

import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.LeaveStatus;

public class LeaveDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7818349667949195880L;

	private Long id;
	private LeaveCategoryDTO leaveCategoryDTO;
	private DateRange period;
	private String employeeComments;
	private String managerComments;
	private EmployeeDTO employeeDTO;
	private LeaveStatus status;
	@SuppressWarnings("unused")
	private String from;
	@SuppressWarnings("unused")
	private String to;
	private Date leaveDate;
	private Date leaveAppliedOn;
	private Double numberOfDays;
	private String appliedDate;
	private Boolean canBeCancelledAfterApproval;
	private String approvedBy;

	public Long getId() {
		return id;
	}

	public String getAppliedDate() {
		return appliedDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LeaveCategoryDTO getLeaveCategoryDTO() {
		return leaveCategoryDTO;
	}

	public void setLeaveCategoryDTO(LeaveCategoryDTO leaveCategoryDTO) {
		this.leaveCategoryDTO = leaveCategoryDTO;
	}

	public DateRange getPeriod() {
		return period;
	}

	public void setPeriod(DateRange period) {
		this.period = period;
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

	public EmployeeDTO getEmployeeDTO() {
		return employeeDTO;
	}

	public void setEmployeeDTO(EmployeeDTO employeeDTO) {
		this.employeeDTO = employeeDTO;
	}

	public LeaveStatus getStatus() {
		return status;
	}

	public void setStatus(LeaveStatus status) {
		this.status = status;
	}

	public String getFrom() {
		return this.period.getMinimum().toString("dd MMM yyyy");
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return this.period.getMaximum().toString("dd MMM yyyy");
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getLeaveAppliedOn() {
		return leaveAppliedOn;
	}

	public void setLeaveAppliedOn(Date leaveAppliedOn) {
		this.leaveAppliedOn = leaveAppliedOn;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public Double getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(Double numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public void setAppliedDate(String appliedDate) {
		this.appliedDate = appliedDate;
	}

	public Boolean getCanBeCancelledAfterApproval() {
		return canBeCancelledAfterApproval;
	}

	public void setCanBeCancelledAfterApproval(
			Boolean canBeCancelledAfterApproval) {
		this.canBeCancelledAfterApproval = canBeCancelledAfterApproval;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

}