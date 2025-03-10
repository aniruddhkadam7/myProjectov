package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;

import com.raybiztech.date.Date;

public class LineItemDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2125240537648904031L;

	private Long id;
	private String item;
	private Long empId;
	private String description;
	private String duration;
	private String monthWorkingDays;
	private String holidays;
	private String leaves;
	private String count;
	private String rate;
	private String amount;
	private String empName;
	private String role;
	private Boolean flag;
	private String fromDate;
	private String endDate;
	private String comments;
	private String hours;
	private String totalValue;
	private String billableDays;
	private String lineItemAmount;

	public String getLineItemAmount() {
		return lineItemAmount;
	}

	public void setLineItemAmount(String lineItemAmount) {
		this.lineItemAmount = lineItemAmount;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
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

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
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

	public String getBillableDays() {
		return billableDays;
	}

	public void setBillableDays(String billableDays) {
		this.billableDays = billableDays;
	}

}
