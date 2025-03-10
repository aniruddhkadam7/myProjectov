package com.raybiztech.leavemanagement.dto;

import java.io.Serializable;

public class LeaveSettingsDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3240876627801938473L;
	private Long id;
	private Integer probationPeriod;
	private Integer maxLeavesEarned;
	private String leaveCycleMonth;
	private Integer payrollCutoffDate;
	private Double leavesPerYear;
	private Integer maxAccrualPerYear;
	
	public String getLeaveCycleMonth() {
		return leaveCycleMonth;
	}
	public void setLeaveCycleMonth(String leaveCycleMonth) {
		this.leaveCycleMonth = leaveCycleMonth;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getProbationPeriod() {
		return probationPeriod;
	}
	public void setProbationPeriod(Integer probationPeriod) {
		this.probationPeriod = probationPeriod;
	}
	public Integer getMaxLeavesEarned() {
		return maxLeavesEarned;
	}
	public void setMaxLeavesEarned(Integer maxLeavesEarned) {
		this.maxLeavesEarned = maxLeavesEarned;
	}
	public Integer getPayrollCutoffDate() {
		return payrollCutoffDate;
	}
	public void setPayrollCutoffDate(Integer payrollCutoffDate) {
		this.payrollCutoffDate = payrollCutoffDate;
	}

	public Double getLeavesPerYear() {
		return leavesPerYear;
	}
	public void setLeavesPerYear(Double leavesPerYear) {
		this.leavesPerYear = leavesPerYear;
	}
	public Integer getMaxAccrualPerYear() {
		return maxAccrualPerYear;
	}
	public void setMaxAccrualPerYear(Integer maxAccrualPerYear) {
		this.maxAccrualPerYear = maxAccrualPerYear;
	}
	

}