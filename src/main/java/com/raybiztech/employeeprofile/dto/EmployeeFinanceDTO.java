package com.raybiztech.employeeprofile.dto;

import java.util.List;

public class EmployeeFinanceDTO {

	private Long id;
	private Long employeeId;
	private String employeeName;
	private FinanceDTO financeDetails;
	private List<EmployeeBankInformationDTO> bankDetails;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public FinanceDTO getFinanceDetails() {
		return financeDetails;
	}

	public void setFinanceDetails(FinanceDTO financeDetails) {
		this.financeDetails = financeDetails;
	}

	public List<EmployeeBankInformationDTO> getBankDetails() {
		return bankDetails;
	}

	public void setBankDetails(List<EmployeeBankInformationDTO> bankDetails) {
		this.bankDetails = bankDetails;
	}

}
