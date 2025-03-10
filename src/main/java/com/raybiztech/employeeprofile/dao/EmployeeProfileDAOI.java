/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.employeeprofile.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.EmployeeBankInformation;
import com.raybiztech.appraisals.business.Finance;
import com.raybiztech.appraisals.business.Qualification;
import com.raybiztech.appraisals.business.QualificationLookUp;
import com.raybiztech.appraisals.business.VisaDetails;
import com.raybiztech.appraisals.business.VisaLookUp;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.QualificationDTO;
import com.raybiztech.appraisals.dto.QualificationLookUpDTO;
import com.raybiztech.appraisals.dto.VisaDetailDTO;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.lookup.business.BankNameLookup;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.recruitment.business.Holidays;
import com.raybiztech.sms.business.SmsTemplates;

/**
 * 
 * @author naresh
 */
public interface EmployeeProfileDAOI extends DAO {

	List<Holidays> getUpcomingHolidays(String country);
	
	List<Holidays> getOnlyUpcomingHolidays(String country);

	Map<String, Object> getUpcomingBirthdayAnniversaries(Integer startIndex,
			Integer endIndex);

	List<Employee> getUpcomingMarriedAnniversaries();

	Boolean isemployeeUsernameexist(String userName);

	Set<Holidays> getHolidays(DateRange month);

	Map<String, Object> getUpcomingProvisionalPeriod(Employee employee,
			Integer probationaryPeriod, Integer startIndex, Integer endIndex);

	Finance getEmplopyeeFinanceInfo(Long empId);

	void updateEmplopyeeFinanceInfo(Long empId);

	List<BankNameLookup> getBankNameList();

	Boolean isHolidayDateExist(Date holidaydate, String country);

	Boolean isHolidayDateExistUpdate(Date holidaydate,String country);

	List<QualificationLookUp> getQualiactionCategoryList();

	Qualification getAllQualification(Long empId);

	Boolean checkForDuplicate(QualificationLookUpDTO qualificationLookUpDTO);

	Boolean checkForUse(Long id);

	List<Employee> getBirthdayEmployees();

	String getSMSTemplate();
	
	Map<String, List<Audit>> getEmployeeDetailAudit(Long employeeId);
	
	Map<String, Object> getFinanceDetailsList(Integer startIndex,
			Integer endIndex, String employeeName);
	
	Map<String, Object> getCountryLookUps();

	List<VisaLookUp> getCountryChangeList(Long id);
	
	List<VisaDetails> getEmployeeVisaDetailsList(Long loggedInEmpId);
	
	Boolean checkForDuplicateVisa(VisaDetailDTO visaDetailDTO, DateRange dateRange);

	Boolean checkForDuplicateVisaForEdit(VisaDetailDTO editVisaDetailsDTO, DateRange dateRange);
}
