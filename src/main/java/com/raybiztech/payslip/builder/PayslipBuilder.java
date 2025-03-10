package com.raybiztech.payslip.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.EmployeeBankInformation;
import com.raybiztech.appraisals.business.Finance;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeAuditDTO;
import com.raybiztech.date.Second;
import com.raybiztech.employeeprofile.dao.EmployeeProfileDAOI;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.payslip.business.Payslip;
import com.raybiztech.payslip.business.SalaryStructure;
import com.raybiztech.payslip.dao.PayslipDao;
import com.raybiztech.payslip.dto.PayslipDto;
import com.raybiztech.payslip.dto.PayslipEmpViewDTO;
import com.raybiztech.payslip.dto.PayslipRetrieveDto;
import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.payslip.utility.SortByMonthComparator;
import com.raybiztech.projectmanagement.business.Audit;

@Component("payslipBuilder")
public class PayslipBuilder {
	@Autowired
	DAO dao;
	@Autowired
	EmployeeProfileDAOI employeeProfileDAOIMPL;

	@Autowired
	PayslipDao payslipDaoImpl;

	Logger logger = Logger.getLogger(PayslipBuilder.class);

	/* List<PayslipDto> errorList = new ArrayList<PayslipDto>(); */

	public PayslipRetrieveDto toDto(Payslip payslip) {

		AES256Encryption aes256Encryption = new AES256Encryption(payslip
				.getEmployee().getEmployeeId().toString(), payslip.getSalt());

		PayslipRetrieveDto dto = new PayslipRetrieveDto();

		Employee employee = payslip.getEmployee();

		SalaryStructure structure = payslip.getSalaryStructure();

		Finance finance = employeeProfileDAOIMPL
				.getEmplopyeeFinanceInfo(employee.getEmployeeId());
		Set<EmployeeBankInformation> bankInformation = employee
				.getBankInformations();
		for (EmployeeBankInformation information : bankInformation) {
			/*
			 * dto.setPanNumber(information.getPanCardAccountNumber());
			 * dto.setPfAccountNumber(information.getPfAccountNumber());
			 */

			// System.out.println("bank "+information.getBankName()+"#########"+information.getBankAccountNumber()+"structureeeeeeeeeeeee"+structure.getAccountNo());

			if (aes256Encryption.decrypt(structure.getAccountNo())
					.equalsIgnoreCase((information.getBankAccountNumber()))) {

				dto.setBankName(information.getBankName());
				break;
			} else {

				dto.setBankName("N/A");
			}

		}
		if (finance != null) {
			dto.setPanNumber(finance.getPanCardAccountNumber());

			dto.setPfAccountNumber(finance.getPfAccountNumber());

			dto.setUaNumber(finance.getUaNumber());

		}

		dto.setPaySlipId(payslip.getPaySlipId());
		dto.setEmployeeId(employee.getEmployeeId());
		dto.setDateOfBirth(DateParser.toString(employee.getDob()));

		dto.setName(employee.getFullName());
		//dto.setDesignation(employee.getDesignation());
		
		logger.warn("designation preview"+aes256Encryption.decrypt(structure.getDesignation()));
		
		String exitingDesignation = aes256Encryption.decrypt(structure.getDesignation());
		
		logger.warn("exitingDesignation"+exitingDesignation);
		
		logger.warn("specificdesignation"+structure.getSpecificDesignation());
		
		if(structure.getSpecificDesignation() != null){
			logger.warn("in specif");
		if(exitingDesignation.equalsIgnoreCase(structure.getSpecificDesignation())){
			logger.warn("condition true");
			dto.setDesignation(aes256Encryption.decrypt(structure.getDesignation()));
		}else{
			logger.warn("condition false");
			dto.setDesignation(structure.getSpecificDesignation());
		}
		}else{
			logger.warn("else");
			dto.setDesignation(aes256Encryption.decrypt(structure.getDesignation()));
		}
		
		
		//dto.setDesignation(aes256Encryption.decrypt(structure.getDesignation()));
		
		//dto.setSpecificDesignation(structure.getSpecificDesignation() != null ? structure.getSpecificDesignation() : null);
		

		dto.setJoiningDate(employee.getJoiningDate().toString());
		logger.warn("message" + dto.getJoiningDate());

		dto.setAccountNo(aes256Encryption.decrypt(structure.getAccountNo()));

		dto.setArrears(aes256Encryption.decrypt(structure.getArrears()));

		dto.setBasicSalary(aes256Encryption.decrypt(structure.getBasicSalary()));

		dto.setEpf(aes256Encryption.decrypt(structure.getEpf()));
		dto.setErc(aes256Encryption.decrypt(structure.getErc()));
		dto.setEsi(aes256Encryption.decrypt(structure.getEsi()));
		dto.setGratuity(structure.getGratuity() != null ? aes256Encryption
				.decrypt(structure.getGratuity()) : null);
		dto.setGrossSalary(aes256Encryption.decrypt(structure.getGrossSalary()));
		dto.setHouseRentAllowance(aes256Encryption.decrypt(structure
				.getHouseRentAllowance()));
		dto.setGrossSalAfterVariablepay(aes256Encryption.decrypt(structure
				.getGrossSalAfterVariablepay()));
		dto.setIncentive(aes256Encryption.decrypt(structure.getIncentive()));

		dto.setMedicliam(aes256Encryption.decrypt(structure.getMedicliam()));
		dto.setTransportAllowance(aes256Encryption.decrypt(structure
				.getTransportAllowance()));
		dto.setVariablePayPercentage(aes256Encryption.decrypt(structure
				.getVariablePayPercentage()));
		dto.setVariablePay(aes256Encryption.decrypt(structure.getVariablePay()));
		dto.setVpayable(aes256Encryption.decrypt(structure.getVpayable()));
		dto.setMonth(payslip.getMonth());
		dto.setYear(payslip.getYear());
		dto.setOtherAllowance(aes256Encryption.decrypt(structure
				.getOtherAllowance()));
		dto.setAbsent(aes256Encryption.decrypt(structure.getAbsent()));
		dto.setLossOfPay(aes256Encryption.decrypt(structure.getLossOfPay()));
		dto.setAdvArrears(aes256Encryption.decrypt(structure.getAdvArrears()));
		dto.setTaxDeductionScheme(aes256Encryption.decrypt(structure
				.getTaxDeductionScheme()));
		dto.setProfessionalTax(aes256Encryption.decrypt(structure
				.getProfessionalTax()));
		dto.setNetSalary(aes256Encryption.decrypt(structure.getNetSalary()));
		dto.setRemarks(structure.getRemarks());
		dto.setStatus(payslip.getStatus());

		dto.setSumOfLeaves(payslip.getSumOfLeaves(aes256Encryption));

		// dto.setPerDaySal(payslip.getPerDaySal(aes256Encryption));

		dto.setLeaveWithOutPay(payslip.getLeaveWithOutPay(aes256Encryption));

		dto.setTotalDeductions(payslip.getTotalDeductions(aes256Encryption));

		String month = payslip.getMonth();

		Date date = null;
		try {
			date = new SimpleDateFormat("MMMM").parse(month);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		Integer noOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		dto.setNoOfDaysInMonth(noOfDays);

		Integer absent = (dto.getAbsent() == null) ? 0 : Integer.parseInt(dto
				.getAbsent());

		dto.setTotalWorkingDays(noOfDays - absent);

		dto.setAllowences(payslip.getAllowences(aes256Encryption));
		dto.setMealsCard(aes256Encryption.decrypt(structure.getMealsCard()));
		dto.setDonation(aes256Encryption.decrypt(structure.getDonation()));

		return dto;
	}

	public Payslip toEntity(PayslipDto payslipDto, String month, String year,
			Payslip parameter) {

		Payslip payslip = parameter;

		String saltKey = KeyGenerators.string().generateKey();
		AES256Encryption aes256Encryption = new AES256Encryption(payslipDto
				.getEmployeeId().toString(), saltKey);
		payslip.setSalt(saltKey);

		SalaryStructure salaryStructure = new SalaryStructure();

		Employee employee = null;

		employee = dao.findBy(Employee.class, payslipDto.getEmployeeId());

		/* errorList.add(payslipDto); */

		salaryStructure.setSerialNo(payslipDto.getSerialNo());

		salaryStructure.setAccountNo(aes256Encryption.encrypt(payslipDto
				.getAccountNo()));

		/*
		 * salaryStructure.setJoiningDate(aes256Encryption.encrypt(payslipDto
		 * .getJoiningDate()));
		 */

		Date payslipUploadeDate = new Date();

		
		String latestDesignation = this.getLatestEmployeeDesignation(
				payslipDto.getEmployeeId(), payslip.getMonth(),
				payslip.getYear(), payslipUploadeDate);
		
		//logger.warn("latestDesignation"+latestDesignation);

		if(latestDesignation != null){
		salaryStructure.setDesignation(aes256Encryption
				.encrypt(latestDesignation));
		salaryStructure.setSpecificDesignation(latestDesignation);
		
		}else{
			salaryStructure.setDesignation(aes256Encryption.encrypt(employee.getDesignation()));
			salaryStructure.setSpecificDesignation(employee.getDesignation());
		}
		
	/*	salaryStructure.setDesignation(aes256Encryption.encrypt(employee.getDesignation()));
		salaryStructure.setSpecificDesignation(employee.getDesignation());*/
		
		

		salaryStructure.setName(payslipDto.getName());

		salaryStructure.setGrossSalary(aes256Encryption.encrypt(payslipDto
				.getGrossSalary() != null ? String.valueOf(payslipDto
				.getGrossSalary()) : "0"));

		salaryStructure.setVariablePayPercentage(aes256Encryption
				.encrypt(payslipDto.getVariablePayPercentage() != null ? String
						.valueOf(payslipDto.getVariablePayPercentage()) : "0"));

		salaryStructure.setVariablePay(aes256Encryption.encrypt(payslipDto
				.getVariablePay() != null ? String.valueOf(payslipDto
				.getVariablePay()) : "0"));

		salaryStructure
				.setGrossSalAfterVariablepay(aes256Encryption
						.encrypt(payslipDto.getGrossSalAfterVariablepay() != null ? String
								.valueOf(payslipDto
										.getGrossSalAfterVariablepay()) : "0"));

		salaryStructure.setBasicSalary(aes256Encryption.encrypt(payslipDto
				.getBasicSalary() != null ? String.valueOf(payslipDto
				.getBasicSalary()) : "0"));

		salaryStructure.setHouseRentAllowance(aes256Encryption
				.encrypt(payslipDto.getHouseRentAllowance() != null ? String
						.valueOf(payslipDto.getHouseRentAllowance()) : "0"));

		salaryStructure.setTransportAllowance(aes256Encryption
				.encrypt(payslipDto.getTransportAllowance() != null ? String
						.valueOf(payslipDto.getTransportAllowance()) : "0"));

		salaryStructure.setOtherAllowance(aes256Encryption.encrypt(payslipDto
				.getOtherAllowance() != null ? String.valueOf(payslipDto
				.getOtherAllowance()) : "0"));

		salaryStructure.setAbsent(aes256Encryption.encrypt(payslipDto
				.getAbsent() != null ? String.valueOf(payslipDto.getAbsent())
				: "0"));

		salaryStructure.setLossOfPay(aes256Encryption.encrypt(payslipDto
				.getLossOfPay() != null ? String.valueOf(payslipDto
				.getLossOfPay()) : "0"));

		salaryStructure.setMedicliam(aes256Encryption.encrypt(payslipDto
				.getMedicliam() != null ? String.valueOf(payslipDto
				.getMedicliam()) : "0"));

		salaryStructure
				.setEsi(aes256Encryption.encrypt(payslipDto.getEsi() != null ? String
						.valueOf(payslipDto.getEsi()) : "0"));

		salaryStructure
				.setEpf(aes256Encryption.encrypt(payslipDto.getEpf() != null ? String
						.valueOf(payslipDto.getEpf()) : "0"));

		salaryStructure.setGratuity(aes256Encryption.encrypt(payslipDto
				.getGratuity() != null ? String.valueOf(payslipDto
				.getGratuity()) : "0"));

		salaryStructure.setAdvArrears(aes256Encryption.encrypt(payslipDto
				.getAdvArrears() != null ? String.valueOf(payslipDto
				.getAdvArrears()) : "0"));

		salaryStructure
				.setErc(aes256Encryption.encrypt(payslipDto.getErc() != null ? String
						.valueOf(payslipDto.getErc()) : "0"));

		salaryStructure.setTaxDeductionScheme(aes256Encryption
				.encrypt(payslipDto.getTaxDeductionScheme() != null ? String
						.valueOf(payslipDto.getTaxDeductionScheme()) : "0"));

		salaryStructure.setProfessionalTax(aes256Encryption.encrypt(payslipDto
				.getProfessionalTax() != null ? String.valueOf(payslipDto
				.getProfessionalTax()) : "0"));

		salaryStructure.setArrears(aes256Encryption.encrypt(payslipDto
				.getArrears() != null ? String.valueOf(payslipDto.getArrears())
				: "0"));

		salaryStructure.setIncentive(aes256Encryption.encrypt(payslipDto
				.getIncentive() != null ? String.valueOf(payslipDto
				.getIncentive()) : "0"));

		salaryStructure.setVpayable(aes256Encryption.encrypt(payslipDto
				.getVpayable() != null ? String.valueOf(payslipDto
				.getVpayable()) : "0"));

		salaryStructure.setNetSalary(aes256Encryption.encrypt(payslipDto
				.getNetSalary() != null ? String.valueOf(payslipDto
				.getNetSalary()) : "0"));

		salaryStructure.setRemarks(payslipDto.getRemarks());
		salaryStructure.setMealsCard(aes256Encryption.encrypt(payslipDto
				.getMealsCard() != null ? String.valueOf(payslipDto
				.getMealsCard()) : "0"));
		salaryStructure.setDonation(aes256Encryption.encrypt(payslipDto
				.getDonation() != null ? String.valueOf(payslipDto
				.getDonation()) : "0"));

		payslip.setEmployee(employee);

		payslip.setSalaryStructure(salaryStructure);

		payslip.setStatus(Boolean.TRUE);
		payslip.setMonth(month);
		payslip.setYear(year);

		return payslip;

	}

	public Payslip toEditEntity(PayslipRetrieveDto payslipDto) {
		Payslip paySlip = dao.findBy(Payslip.class, payslipDto.getPaySlipId());

		AES256Encryption aes256Encryption = new AES256Encryption(payslipDto
				.getEmployeeId().toString(), paySlip.getSalt());
		
		Employee employee = null;

		employee = dao.findBy(Employee.class, payslipDto.getEmployeeId());


		SalaryStructure salaryStructure = paySlip.getSalaryStructure();
		salaryStructure.setAbsent(aes256Encryption.encrypt(payslipDto
				.getAbsent()));
		salaryStructure.setAccountNo(aes256Encryption.encrypt(payslipDto
				.getAccountNo()));
		
          Date payslipUploadeDate = new Date();

		
		String latestDesignation = this.getLatestEmployeeDesignation(
				payslipDto.getEmployeeId(), paySlip.getMonth(),
				paySlip.getYear(), payslipUploadeDate);

		if(latestDesignation != null){
		salaryStructure.setDesignation(aes256Encryption
				.encrypt(latestDesignation));
		}else{
			salaryStructure.setDesignation(aes256Encryption.encrypt(employee.getDesignation()));
		}
		
		salaryStructure.setSpecificDesignation(salaryStructure.getSpecificDesignation() != null ? salaryStructure.getSpecificDesignation() : null);
		
		/*salaryStructure.setDesignation(aes256Encryption.encrypt(payslipDto
				.getDesignation()));*/
		salaryStructure.setArrears(aes256Encryption.encrypt(payslipDto
				.getArrears()));
		salaryStructure.setBasicSalary(aes256Encryption.encrypt(payslipDto
				.getBasicSalary()));
		salaryStructure.setEpf(aes256Encryption.encrypt(payslipDto.getEpf()));
		salaryStructure.setErc(aes256Encryption.encrypt(payslipDto.getErc()));
		salaryStructure.setEsi(aes256Encryption.encrypt(payslipDto.getEsi()));
		salaryStructure.setGratuity(aes256Encryption.encrypt(payslipDto
				.getGratuity()));
		salaryStructure.setGrossSalAfterVariablepay(aes256Encryption
				.encrypt(payslipDto.getGrossSalAfterVariablepay()));
		salaryStructure.setGrossSalary(aes256Encryption.encrypt(payslipDto
				.getGrossSalary()));
		salaryStructure.setHouseRentAllowance(aes256Encryption
				.encrypt(payslipDto.getHouseRentAllowance()));
		salaryStructure.setIncentive(aes256Encryption.encrypt(payslipDto
				.getIncentive()));
		salaryStructure.setLossOfPay(aes256Encryption.encrypt(payslipDto
				.getLossOfPay()));
		salaryStructure.setMedicliam(aes256Encryption.encrypt(payslipDto
				.getMedicliam()));

		salaryStructure.setNetSalary(aes256Encryption.encrypt(payslipDto
				.getNetSalary()));

		salaryStructure.setVariablePay(aes256Encryption.encrypt(payslipDto
				.getVariablePay()));
		salaryStructure.setVariablePayPercentage(aes256Encryption
				.encrypt(payslipDto.getVariablePayPercentage()));
		salaryStructure.setVpayable(aes256Encryption.encrypt(payslipDto
				.getVpayable()));

		salaryStructure.setTransportAllowance(aes256Encryption
				.encrypt(payslipDto.getTransportAllowance()));
		salaryStructure.setOtherAllowance(aes256Encryption.encrypt(payslipDto
				.getOtherAllowance()));
		salaryStructure.setAdvArrears(aes256Encryption.encrypt(payslipDto
				.getAdvArrears()));
		salaryStructure.setTaxDeductionScheme(aes256Encryption
				.encrypt(payslipDto.getTaxDeductionScheme()));
		salaryStructure.setProfessionalTax(aes256Encryption.encrypt(payslipDto
				.getProfessionalTax()));
		salaryStructure.setRemarks(payslipDto.getRemarks());

		salaryStructure.setMealsCard(aes256Encryption.encrypt(payslipDto
				.getMealsCard() != null ? String.valueOf(payslipDto
				.getMealsCard()) : "0"));
		salaryStructure.setDonation(aes256Encryption.encrypt(payslipDto
				.getDonation() != null ? String.valueOf(payslipDto
				.getDonation()) : "0"));

		paySlip.setSalaryStructure(salaryStructure);
		return paySlip;

	}

	public List<PayslipRetrieveDto> getDTOList(List<Payslip> entitylist) {
		List<PayslipRetrieveDto> dtolist = new ArrayList<PayslipRetrieveDto>();
		for (Payslip payslip : entitylist) {
			dtolist.add(toDto(payslip));
		}
		return dtolist;
	}

	public List<PayslipEmpViewDTO> getEmpPayslipViewList(List<Object[]> list) {

		List<PayslipEmpViewDTO> payslipEmpViewdtoList = null;

		if (list != null) {
			payslipEmpViewdtoList = new ArrayList<PayslipEmpViewDTO>();
			for (Object[] row : list) {
				PayslipEmpViewDTO payslipdto = new PayslipEmpViewDTO();
				payslipdto.setEmpId((Long) row[0]);
				payslipdto.setMonth((String) row[1]);
				payslipdto.setYear((String) row[2]);
				payslipEmpViewdtoList.add(payslipdto);
			}
		}

		Collections.sort(payslipEmpViewdtoList, new SortByMonthComparator());
		return payslipEmpViewdtoList;

	}

	String getLatestEmployeeDesignation(Long employeeId, String month,
			String year, Date payslipUploadeDate) {

		Audit audits = payslipDaoImpl
				.getlatestDesignationForEmployee(employeeId);
		
		String latestdesignation = null;
		
		if(audits != null){

		logger.warn("employeeId" + employeeId);
		logger.warn("newvalue" + audits.getNewValue());
		logger.warn("oldvalue" + audits.getOldValue());

		logger.warn("audits.getModifiedDate()"+audits.getModifiedDate());

		String modifiedDate = audits.getModifiedDate().toString("dd/MM/yyyy");
		
		logger.warn("string modified date"+modifiedDate);
		
		Date modifydate = null;
		try {
			modifydate = new SimpleDateFormat("dd/MM/yyyy").parse(modifiedDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.warn("modified date" + modifydate);

		logger.warn("payslipUploadeDate" + payslipUploadeDate);

		if(modifydate.before(payslipUploadeDate)) {
			latestdesignation = audits.getNewValue();
		}
		}else{
			logger.warn("else no records in audit table");
			latestdesignation = null;
		}

		return latestdesignation;

	}

	/*
	 * // Now we are not using this method any where we have written this for //
	 * future // purpose if asked for number to currency format use this public
	 * String formatToIndianCurrency(String number) {
	 * 
	 * if (number != null) { // Formatting Number to Indian Curreny Using //
	 * com.ibm.icu.text.NumberFormat; Format format =
	 * NumberFormat.getCurrencyInstance(new Locale("en", "in")); String
	 * convertedNumber = format.format(new BigDecimal(number)); //
	 * convertedNumber will be in this ₹ 9,00,00,000.00 format String
	 * formattedNumber = convertedNumber.substring(2,
	 * convertedNumber.lastIndexOf(".")); // formattedNumber will be in this
	 * format 9,00,00,000
	 * 
	 * return formattedNumber; } return null;
	 * 
	 * }
	 */
	
}
