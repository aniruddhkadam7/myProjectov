package com.raybiztech.appraisals.business;

import java.io.Serializable;

public class Finance implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2693112666192399008L;
	private Long financeId;
	private String pfAccountNumber;
	private String panCardAccountNumber;
	private String uaNumber;
	private Employee employee;
	private String aadharCardNumber;
	private String financeFilePath;
	

	public String getPfAccountNumber() {
		return pfAccountNumber;
	}

	public void setPfAccountNumber(String pfAccountNumber) {
		this.pfAccountNumber = pfAccountNumber;
	}

	public String getPanCardAccountNumber() {
		return panCardAccountNumber;
	}

	public void setPanCardAccountNumber(String panCardAccountNumber) {
		this.panCardAccountNumber = panCardAccountNumber;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Long getFinanceId() {
		return financeId;
	}

	public void setFinanceId(Long financeId) {
		this.financeId = financeId;
	}

	public String getUaNumber() {
		return uaNumber;
	}

	public void setUaNumber(String uaNumber) {
		this.uaNumber = uaNumber;
	}

	public String getAadharCardNumber() {
		return aadharCardNumber;
	}

	public void setAadharCardNumber(String aadharCardNumber) {
		this.aadharCardNumber = aadharCardNumber;
	}

	public String getFinanceFilePath() {
		return financeFilePath;
	}

	public void setFinanceFilePath(String financeFilePath) {
		this.financeFilePath = financeFilePath;
	}

	
}
