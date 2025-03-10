package com.raybiztech.leavemanagement.dto;

import java.io.Serializable;

public class LeaveApplicationDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String leaveCategoryName;
	private String fromDate;
	private String toDate;
	private String employeeComments;
	private Long employeeId;
	private String leaveAppliedOn;

	public String getLeaveCategoryName() {
		return leaveCategoryName;
	}

	public void setLeaveCategoryName(String leaveCategoryName) {
		this.leaveCategoryName = leaveCategoryName;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmployeeComments() {
		return employeeComments;
	}

	public void setEmployeeComments(String employeeComments) {
		this.employeeComments = employeeComments;
	}

	public Long getEmployeeId() {
		return employeeId;
	}


	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getLeaveAppliedOn() {
		return leaveAppliedOn;
	}

	public void setLeaveAppliedOn(String leaveAppliedOn) {
		this.leaveAppliedOn = leaveAppliedOn;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

}