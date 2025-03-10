package com.raybiztech.payslip.dto;

import java.io.Serializable;

public class PayslipDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7217869940888508110L;
	private Double serialNo;
	private Long paySlipId;
	private Long employeeId;
	private String designation;
	private String joiningDate;
	private String name;
	private String accountNo;
	private Long grossSalary;
	private Long variablePayPercentage;
	private Long variablePay;

	private Long grossSalAfterVariablepay;
	private Long basicSalary;
	private Long houseRentAllowance;
	private Long transportAllowance;
	private Long otherAllowance;
	private Integer absent;
	private Integer lossOfPay;
	private Long medicliam;
	private Long esi;
	private Long epf;
	private Long gratuity;

	private Long erc;
	private Long taxDeductionScheme;
	private Long professionalTax;
	private Long arrears;
	private Long advArrears;
	private Long incentive;
	private Long vpayable;
	private Long netSalary;
	private String month;
	private String year;
	private String remarks;
	private Boolean status;
	private Long mealsCard;
	private Long donation;
	private String specificDesignation;

	public Double getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(Double serialNo) {
		this.serialNo = serialNo;
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

	public Long getGrossSalary() {
		return grossSalary;
	}

	public void setGrossSalary(Long grossSalary) {
		this.grossSalary = grossSalary;
	}

	public Long getVariablePayPercentage() {
		return variablePayPercentage;
	}

	public void setVariablePayPercentage(Long variablePayPercentage) {
		this.variablePayPercentage = variablePayPercentage;
	}

	public Long getVariablePay() {
		return variablePay;
	}

	public void setVariablePay(Long variablePay) {
		this.variablePay = variablePay;
	}

	public Long getGrossSalAfterVariablepay() {
		return grossSalAfterVariablepay;
	}

	public void setGrossSalAfterVariablepay(Long grossSalAfterVariablepay) {
		this.grossSalAfterVariablepay = grossSalAfterVariablepay;
	}

	public Long getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Long basicSalary) {
		this.basicSalary = basicSalary;
	}

	public Long getHouseRentAllowance() {
		return houseRentAllowance;
	}

	public void setHouseRentAllowance(Long houseRentAllowance) {
		this.houseRentAllowance = houseRentAllowance;
	}

	public Long getTransportAllowance() {
		return transportAllowance;
	}

	public void setTransportAllowance(Long transportAllowance) {
		this.transportAllowance = transportAllowance;
	}

	public Long getOtherAllowance() {
		return otherAllowance;
	}

	public void setOtherAllowance(Long otherAllowance) {
		this.otherAllowance = otherAllowance;
	}

	public Integer getAbsent() {
		return absent;
	}

	public void setAbsent(Integer absent) {
		this.absent = absent;
	}

	public Integer getLossOfPay() {
		return lossOfPay;
	}

	public void setLossOfPay(Integer lossOfPay) {
		this.lossOfPay = lossOfPay;
	}

	public Long getMedicliam() {
		return medicliam;
	}

	public void setMedicliam(Long medicliam) {
		this.medicliam = medicliam;
	}

	public Long getEsi() {
		return esi;
	}

	public void setEsi(Long esi) {
		this.esi = esi;
	}

	public Long getEpf() {
		return epf;
	}

	public void setEpf(Long epf) {
		this.epf = epf;
	}

	public Long getErc() {
		return erc;
	}

	public void setErc(Long erc) {
		this.erc = erc;
	}

	public Long getTaxDeductionScheme() {
		return taxDeductionScheme;
	}

	public void setTaxDeductionScheme(Long taxDeductionScheme) {
		this.taxDeductionScheme = taxDeductionScheme;
	}

	public Long getProfessionalTax() {
		return professionalTax;
	}

	public void setProfessionalTax(Long professionalTax) {
		this.professionalTax = professionalTax;
	}

	public Long getArrears() {
		return arrears;
	}

	public void setArrears(Long arrears) {
		this.arrears = arrears;
	}

	public Long getAdvArrears() {
		return advArrears;
	}

	public void setAdvArrears(Long advArrears) {
		this.advArrears = advArrears;
	}

	public Long getIncentive() {
		return incentive;
	}

	public void setIncentive(Long incentive) {
		this.incentive = incentive;
	}

	public Long getVpayable() {
		return vpayable;
	}

	public void setVpayable(Long vpayable) {
		this.vpayable = vpayable;
	}

	public Long getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(Long netSalary) {
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

	public Long getGratuity() {
		return gratuity;
	}

	public void setGratuity(Long gratuity) {
		this.gratuity = gratuity;
	}

	public Long getMealsCard() {
		return mealsCard;
	}

	public void setMealsCard(Long mealsCard) {
		this.mealsCard = mealsCard;
	}

	public Long getDonation() {
		return donation;
	}

	public void setDonation(Long donation) {
		this.donation = donation;
	}


	public String getSpecificDesignation() {
		return specificDesignation;
	}

	public void setSpecificDesignation(String specificDesignation) {
		this.specificDesignation = specificDesignation;
	}
	

	

}
