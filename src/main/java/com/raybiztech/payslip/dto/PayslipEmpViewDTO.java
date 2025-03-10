package com.raybiztech.payslip.dto;

import java.io.Serializable;

public class PayslipEmpViewDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1718566686167863658L;

	private Long empId;
	private String year;
	private String month;

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

}
