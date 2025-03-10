package com.raybiztech.leavemanagement.business;

import java.io.Serializable;

public class LeaveSettingsLookup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2600859741252284557L;

	private Long id;
	private Integer probationPeriod;
	private Integer maxLeavesEarned;
	private Integer payrollCutoffDate;
	private LeaveCycleMonth leaveCycleMonth;
	private Double leavesPerYear;
	private Integer maxAccrualPerYear;

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

	public LeaveCycleMonth getLeaveCycleMonth() {
		return leaveCycleMonth;
	}

	public void setLeaveCycleMonth(LeaveCycleMonth leaveCycleMonth) {
		this.leaveCycleMonth = leaveCycleMonth;
	}

	public Integer getMaxAccrualPerYear() {
		return maxAccrualPerYear;
	}

	public void setMaxAccrualPerYear(Integer maxAccrualPerYear) {
		this.maxAccrualPerYear = maxAccrualPerYear;
	}

	public Double getLeavesPerYear() {
		return leavesPerYear;
	}

	public void setLeavesPerYear(Double leavesPerYear) {
		this.leavesPerYear = leavesPerYear;
	}
	
	

}
