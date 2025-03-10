package com.raybiztech.payslip.dto;

import java.io.Serializable;

public class PayslipRetrieveDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2634891169056255211L;
	private Long paySlipId;
	private Long employeeId;
	private String designation;
	private String joiningDate;
	private String name;
	private String accountNo;
	private String grossSalary;
	private String variablePayPercentage;
	private String variablePay;

	private String grossSalAfterVariablepay;
	private String basicSalary;
	private String houseRentAllowance;
	private String transportAllowance;
	private String otherAllowance;
	private String absent;
	private String lossOfPay;
	private String medicliam;
	private String esi;
	private String epf;
	private String gratuity;

	private String erc;
	private String taxDeductionScheme;
	private String professionalTax;
	private String arrears;
	private String advArrears;
	private String incentive;
	private String vpayable;
	private String netSalary;
	private String month;
	private Integer sumOfLeaves;
	private Integer totalDeductions;
	private Integer perDaySal;
	private Integer leaveWithOutPay;
	private Integer noOfDaysInMonth;
	private Integer totalWorkingDays;
	private Integer allowences;
	private String year;
	private String remarks;
	private Boolean status;
	private String pfAccountNumber;
	private String panNumber;
	private String bankName;
	private String dateOfBirth;

	private String uaNumber;
	private String mealsCard;
	private String donation;
	private String specificDesignation;

	public Integer getNoOfDaysInMonth() {
		return noOfDaysInMonth;
	}

	public void setNoOfDaysInMonth(Integer noOfDaysInMonth) {
		this.noOfDaysInMonth = noOfDaysInMonth;
	}

	public Long getPaySlipId() {
		return paySlipId;
	}

	public void setPaySlipId(Long paySlipId) {
		this.paySlipId = paySlipId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(String grossSalary) {
		this.grossSalary = grossSalary;
	}

	public String getVariablePayPercentage() {
		return variablePayPercentage;
	}

	public void setVariablePayPercentage(String variablePayPercentage) {
		this.variablePayPercentage = variablePayPercentage;
	}

	public String getVariablePay() {
		return variablePay;
	}

	public void setVariablePay(String variablePay) {
		this.variablePay = variablePay;
	}

	public String getGrossSalAfterVariablepay() {
		return grossSalAfterVariablepay;
	}

	public void setGrossSalAfterVariablepay(String grossSalAfterVariablepay) {
		this.grossSalAfterVariablepay = grossSalAfterVariablepay;
	}

	public String getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(String basicSalary) {
		this.basicSalary = basicSalary;
	}

	public String getHouseRentAllowance() {
		return houseRentAllowance;
	}

	public void setHouseRentAllowance(String houseRentAllowance) {
		this.houseRentAllowance = houseRentAllowance;
	}

	public String getTransportAllowance() {
		return transportAllowance;
	}

	public void setTransportAllowance(String transportAllowance) {
		this.transportAllowance = transportAllowance;
	}

	public String getOtherAllowance() {
		return otherAllowance;
	}

	public void setOtherAllowance(String otherAllowance) {
		this.otherAllowance = otherAllowance;
	}

	public String getAbsent() {
		return absent;
	}

	public void setAbsent(String absent) {
		this.absent = absent;
	}

	public String getLossOfPay() {
		return lossOfPay;
	}

	public void setLossOfPay(String lossOfPay) {
		this.lossOfPay = lossOfPay;
	}

	public String getMedicliam() {
		return medicliam;
	}

	public void setMedicliam(String medicliam) {
		this.medicliam = medicliam;
	}

	public String getEsi() {
		return esi;
	}

	public void setEsi(String esi) {
		this.esi = esi;
	}

	public String getEpf() {
		return epf;
	}

	public void setEpf(String epf) {
		this.epf = epf;
	}

	public String getErc() {
		return erc;
	}

	public void setErc(String erc) {
		this.erc = erc;
	}

	public String getTaxDeductionScheme() {
		return taxDeductionScheme;
	}

	public void setTaxDeductionScheme(String taxDeductionScheme) {
		this.taxDeductionScheme = taxDeductionScheme;
	}

	public String getProfessionalTax() {
		return professionalTax;
	}

	public void setProfessionalTax(String professionalTax) {
		this.professionalTax = professionalTax;
	}

	public String getArrears() {
		return arrears;
	}

	public void setArrears(String arrears) {
		this.arrears = arrears;
	}

	public String getAdvArrears() {
		return advArrears;
	}

	public void setAdvArrears(String advArrears) {
		this.advArrears = advArrears;
	}

	public String getIncentive() {
		return incentive;
	}

	public void setIncentive(String incentive) {
		this.incentive = incentive;
	}

	public String getVpayable() {
		return vpayable;
	}

	public void setVpayable(String vpayable) {
		this.vpayable = vpayable;
	}

	public String getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(String netSalary) {
		this.netSalary = netSalary;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getSumOfLeaves() {
		return sumOfLeaves;
	}

	public void setSumOfLeaves(Integer sumOfLeaves) {
		this.sumOfLeaves = sumOfLeaves;
	}

	public Integer getTotalDeductions() {
		return totalDeductions;
	}

	public void setTotalDeductions(Integer totalDeductions) {
		this.totalDeductions = totalDeductions;
	}

	public Integer getPerDaySal() {
		return perDaySal;
	}

	public void setPerDaySal(Integer perDaySal) {
		this.perDaySal = perDaySal;
	}

	public Integer getLeaveWithOutPay() {
		return leaveWithOutPay;
	}

	public void setLeaveWithOutPay(Integer leaveWithOutPay) {
		this.leaveWithOutPay = leaveWithOutPay;
	}

	public Integer getTotalWorkingDays() {
		return totalWorkingDays;
	}

	public void setTotalWorkingDays(Integer totalWorkingDays) {
		this.totalWorkingDays = totalWorkingDays;
	}

	public Integer getAllowences() {
		return allowences;
	}

	public void setAllowences(Integer allowences) {
		this.allowences = allowences;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getPfAccountNumber() {
		return pfAccountNumber;
	}

	public void setPfAccountNumber(String pfAccountNumber) {
		this.pfAccountNumber = pfAccountNumber;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getUaNumber() {
		return uaNumber;
	}

	public void setUaNumber(String uaNumber) {
		this.uaNumber = uaNumber;
	}

	public String getGratuity() {
		return gratuity;
	}

	public void setGratuity(String gratuity) {
		this.gratuity = gratuity;
	}

	public String getMealsCard() {
		return mealsCard;
	}

	public void setMealsCard(String mealsCard) {
		this.mealsCard = mealsCard;
	}

	public String getDonation() {
		return donation;
	}

	public void setDonation(String donation) {
		this.donation = donation;
	}


	public String getSpecificDesignation() {
		return specificDesignation;
	}

	public void setSpecificDesignation(String specificDesignation) {
		this.specificDesignation = specificDesignation;
	}
	
	

	
}
