package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;

public class LineItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2823333510560500750L;
	/**
	 * 
	 */

	private Long id;
	private Object item;
	// private String item;
	// private Employee employee;
	private String description;
	private Duration duration;
	private String monthWorkingDays;
	private String holidays;
	private String leaves;
	private String count;
	private String hours;
	private String totalValue;
	private String billableDays;
	private String rate;
	private String amount;
	private Date fromDate;
	private Date endDate;
	private String status;
	private String comments;
	private String lineItemAmount;
	// private String role;
	private String saltkey;

	public LineItem() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Object getItem() {
		return item;
	}

	public void setItem(Object item) {
		this.item = item;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getSaltkey() {
		return saltkey;
	}

	public void setSaltkey(String saltkey) {
		this.saltkey = saltkey;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	// public Employee getEmployee() {
	// return employee;
	// }
	//
	// public void setEmployee(Employee employee) {
	// this.employee = employee;
	// }
	//
	// public String getRole() {
	// return role;
	// }
	//
	// public void setRole(String role) {
	// this.role = role;
	// }

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public String getBillableDays() {
		return billableDays;
	}

	public void setBillableDays(String billableDays) {
		this.billableDays = billableDays;
	}

	public String getLineItemAmount() {
		return lineItemAmount;
	}

	public void setLineItemAmount(String lineItemAmount) {
		this.lineItemAmount = lineItemAmount;
	}
	

}
