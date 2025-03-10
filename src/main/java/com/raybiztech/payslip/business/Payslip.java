package com.raybiztech.payslip.business;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.payslip.utility.AES256Encryption;

public class Payslip implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4277869989630544011L;

	private Long paySlipId;
	private Employee employee;
	private SalaryStructure salaryStructure;
	private String month;
	private String year;
	private Boolean status;
	private String salt;
	private String payslipFileName;

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public SalaryStructure getSalaryStructure() {
		return salaryStructure;
	}

	public void setSalaryStructure(SalaryStructure salaryStructure) {
		this.salaryStructure = salaryStructure;
	}

	public Long getPaySlipId() {
		return paySlipId;
	}

	public void setPaySlipId(Long paySlipId) {
		this.paySlipId = paySlipId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getPayslipFileName() {
		return payslipFileName;
	}

	public void setPayslipFileName(String payslipFileName) {
		this.payslipFileName = payslipFileName;
	}

	public Integer getLop(AES256Encryption aes256Encryption) {
		String lop = getSalaryStructure().getLossOfPay();
		return lop != null ? Integer.parseInt(aes256Encryption.decrypt(lop))
				: 0;
	}

	public Integer getGrosSal(AES256Encryption aes256Encryption) {

		String grossSalary = getSalaryStructure().getGrossSalary();
		return grossSalary != null ? Integer.parseInt(aes256Encryption
				.decrypt(grossSalary)) : 0;
	}

	public Integer getSumOfLeaves(AES256Encryption aes256Encryption) {
		String absent = getSalaryStructure().getAbsent();
		Integer absent2 = absent != null ? Integer.parseInt(aes256Encryption
				.decrypt(absent)) : 0;
		return absent2 + getLop(aes256Encryption);
	}

	public Integer getPerDaySal(AES256Encryption aes256Encryption) {

		return (int) Math.round((double) getGrosSal(aes256Encryption) / 30);
	}

	public Integer getLeaveWithOutPay(AES256Encryption aes256Encryption) {

		return getPerDaySal(aes256Encryption) * getLop(aes256Encryption);
	}

	public Integer getTotalDeductions(AES256Encryption aes256Encryption) {

		String taxDeductionScheme = getSalaryStructure()
				.getTaxDeductionScheme();
		Integer tds = taxDeductionScheme != null ? Integer
				.parseInt(aes256Encryption.decrypt(taxDeductionScheme)) : 0;

		String professionalTax = getSalaryStructure().getProfessionalTax();
		Integer pta = professionalTax != null ? Integer
				.parseInt(aes256Encryption.decrypt(professionalTax)) : 0;

		String variablePay = getSalaryStructure().getVariablePay();
		Integer vpay = variablePay != null ? Integer.parseInt(aes256Encryption
				.decrypt(variablePay)) : 0;

		String epf = getSalaryStructure().getEpf();
		Integer epf2 = epf != null ? Integer.parseInt(aes256Encryption
				.decrypt(epf)) : 0;

		String esi = getSalaryStructure().getEsi();
		Integer esi2 = esi != null ? Integer.parseInt(aes256Encryption
				.decrypt(esi)) : 0;

		String medicliam = getSalaryStructure().getMedicliam();
		Integer medicliam2 = medicliam != null ? Integer
				.parseInt(aes256Encryption.decrypt(medicliam)) : 0;

		String erc = getSalaryStructure().getErc();
		Integer erc2 = erc != null ? Integer.parseInt(aes256Encryption
				.decrypt(erc)) : 0;

		String advArrears = getSalaryStructure().getAdvArrears();
		Integer advances = advArrears != null ? Integer
				.parseInt(aes256Encryption.decrypt(advArrears)) : 0;

		/*
		 * Integer lwp = getLeaveWithOutPay(aes256Encryption) != null ?
		 * getLeaveWithOutPay(aes256Encryption) : 0;
		 */

		Integer lop = getLop(aes256Encryption) != null ? getLop(aes256Encryption)
				: 0;

		String gratuityString = getSalaryStructure().getGratuity();
		Integer gratuity = (gratuityString != null) ? Integer
				.parseInt(aes256Encryption.decrypt(gratuityString)) : 0;

		String mealsCard = getSalaryStructure().getMealsCard();
		Integer mealsCard1 = mealsCard != null ? Integer.parseInt(aes256Encryption
				.decrypt(mealsCard)) : 0;
		

		String donation = getSalaryStructure().getDonation();
		Integer donation1 = mealsCard != null ? Integer.parseInt(aes256Encryption
				.decrypt(donation)) : 0;

		return tds + pta + vpay + epf2 + esi2 + medicliam2 + erc2 + advances
				+ lop + gratuity + mealsCard1 + donation1;
	}

	public Integer getAllowences(AES256Encryption aes256Encryption) {

		String arrears = getSalaryStructure().getArrears();
		Integer arrears2 = arrears != null ? Integer.parseInt(aes256Encryption
				.decrypt(arrears)) : 0;

		String incentive = getSalaryStructure().getIncentive();
		Integer incentive2 = incentive != null ? Integer
				.parseInt(aes256Encryption.decrypt(incentive)) : 0;

		String vpayable = getSalaryStructure().getVpayable();
		Integer vpay2 = vpayable != null ? Integer.parseInt(aes256Encryption
				.decrypt(vpayable)) : 0;

		return arrears2 + incentive2 + vpay2;

	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(paySlipId)

		.toHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Payslip) {
			final Payslip other = (Payslip) obj;
			return new EqualsBuilder().append(paySlipId, other.paySlipId)

			.isEquals();
		} else {
			return false;
		}
	}

}
