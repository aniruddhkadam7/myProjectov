package com.raybiztech.employeeprofile.dto;

public class FinanceDTO {
	private Long financeId;

	private String pfAccountNumber;
	private String panCardAccountNumber;
	private String uaNumber;
	private Long employeeId;
	private String aadharCardNumber;
	private String financeFilePath;
	private String financeFileName;
	

	public Long getFinanceId() {
		return financeId;
	}

	public void setFinanceId(Long financeId) {
		this.financeId = financeId;
	}

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

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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

	public String getFinanceFileName() {
		return financeFileName;
	}

	public void setFinanceFileName(String financeFileName) {
		this.financeFileName = financeFileName;
	}

	

}
