/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.projectmanagement.business;

/**
 *
 * @author user
 */
import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;

public class MilestonePeople implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3475753803249051300L;
	private Long id;
	private Employee employee;
	private Boolean isBillable;
	private Date startDate;
	private Date endDate;
	private String count;
	private String monthWorkingDays;
	private String holidays;
	private String leaves;
	private String totalDays;
	private String hours;
	private String totalValue;
	private String comments;
	private String role;

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Boolean getIsBillable() {
		return isBillable;
	}

	public void setIsBillable(Boolean isBillable) {
		this.isBillable = isBillable;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
