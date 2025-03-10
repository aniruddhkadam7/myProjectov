/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.leavemanagement.dto;

import java.io.Serializable;

/**
 *
 * @author anil
 */
public class EmpLeaveDTO implements Serializable {

	private static final long serialVersionUID = 00070L;
	private Long empId;
	private Double remainingDays;
	private Double totalDays;
	private Double pendingLeaves;
	private String empName;
	private Double approvedLeaves;
	private Double cancelAfterApprovalLeaves;

	public Double getApprovedLeaves() {
		return approvedLeaves;
	}

	public void setApprovedLeaves(Double approvedLeaves) {
		this.approvedLeaves = approvedLeaves;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Double getRemainingDays() {
		return remainingDays;
	}

	public void setRemainingDays(Double remainingDays) {
		this.remainingDays = remainingDays;
	}

	public Double getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(Double totalDays) {
		this.totalDays = totalDays;
	}

	public Double getPendingLeaves() {
		return pendingLeaves;
	}

	public void setPendingLeaves(Double pendingLeaves) {
		this.pendingLeaves = pendingLeaves;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Double getCancelAfterApprovalLeaves() {
		return cancelAfterApprovalLeaves;
	}

	public void setCancelAfterApprovalLeaves(Double cancelAfterApprovalLeaves) {
		this.cancelAfterApprovalLeaves = cancelAfterApprovalLeaves;
	}

}
