/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author sravani
 */
public class MilestonePeopleDTO implements Serializable {

	private static final long serialVersionUID = 3471432747749051300L;
	private Long milestonePeopleId;
	private Long milestoneId;
	private List<Long> employeeIds;
	private Boolean isBillable;
	private String fromDate;
	private String endDate;
	private Long employeeId;
	private String employeeName;
	private String employeeDesignation;
	private String employeeStatus;
	private String duration;
	private String count;
	private String rate;
	private String role;
	private String amount;
	private Boolean status;
	private String comments;
	private Long noOfdays;
	private String monthWorkingDays;
	private String holidays;
	private String leaves;
	private String totalDays;
	private String hours;
	private String totalValue;
	private String billableDays;
	

	public String getBillableDays() {
		return billableDays;
	}

	public void setBillableDays(String billableDays) {
		this.billableDays = billableDays;
	}

	public String getHours() {
		return hours;
	}

	public void setHours(String hours) {
		this.hours = hours;
	}

	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	public Long getMilestonePeopleId() {
		return milestonePeopleId;
	}

	public void setMilestonePeopleId(Long milestonePeopleId) {
		this.milestonePeopleId = milestonePeopleId;
	}

	public Long getMilestoneId() {
		return milestoneId;
	}

	public void setMilestoneId(Long milestoneId) {
		this.milestoneId = milestoneId;
	}

	public List<Long> getEmployeeIds() {
		return employeeIds;
	}

	public void setEmployeeIds(List<Long> employeeIds) {
		this.employeeIds = employeeIds;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Boolean getIsBillable() {
		return isBillable;
	}

	public void setIsBillable(Boolean isBillable) {
		this.isBillable = isBillable;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeDesignation() {
		return employeeDesignation;
	}

	public void setEmployeeDesignation(String employeeDesignation) {
		this.employeeDesignation = employeeDesignation;
	}

	public String getEmployeeStatus() {
		return employeeStatus;
	}

	public Long getNoOfdays() {
		return noOfdays;
	}

	public void setNoOfdays(Long noOfdays) {
		this.noOfdays = noOfdays;
	}

	public void setEmployeeStatus(String employeeStatus) {
		this.employeeStatus = employeeStatus;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getMonthWorkingDays() {
		return monthWorkingDays;
	}

	public void setMonthWorkingDays(String monthWorkingDays) {
		this.monthWorkingDays = monthWorkingDays;
	}

	public String getHolidays() {
		return holidays;
	}

	public void setHolidays(String holidays) {
		this.holidays = holidays;
	}

	public String getLeaves() {
		return leaves;
	}

	public void setLeaves(String leaves) {
		this.leaves = leaves;
	}

	public String getTotalDays() {
		return totalDays;
	}

	public void setTotalDays(String totalDays) {
		this.totalDays = totalDays;
	}

}
