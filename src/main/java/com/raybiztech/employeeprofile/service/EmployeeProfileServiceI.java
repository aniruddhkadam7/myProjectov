/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.employeeprofile.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.business.VisaDetails;
import com.raybiztech.appraisals.dto.QualificationDTO;
import com.raybiztech.appraisals.dto.QualificationLookUpDTO;
import com.raybiztech.appraisals.dto.VisaDetailDTO;
import com.raybiztech.appraisals.dto.VisaLookUpDTO;
import com.raybiztech.employeeprofile.dto.EmployeeBankInformationDTO;
import com.raybiztech.employeeprofile.dto.EmployeeFamilyInformationDTO;
import com.raybiztech.employeeprofile.dto.FinanceDTO;
import com.raybiztech.lookup.business.BankNameLookup;
import com.raybiztech.projectmanagement.invoice.dto.CountryLookUpDTO;
import com.raybiztech.recruitment.controller.VisaImageDTO;
import com.raybiztech.recruitment.dto.AnniversariesDTO;
import com.raybiztech.recruitment.dto.CandidateInterviewTimelineDTO;
import com.raybiztech.recruitment.dto.HolidaysDTO;
import com.raybiztech.supportmanagement.dto.TicketsSubCategoryDTO;

/**
 *
 * @author naresh
 */
public interface EmployeeProfileServiceI {

	List<EmployeeFamilyInformationDTO> getEmployeeFamilyInformation(
			Long employeeId);

	void saveEmployeeFamilyDetails(
			EmployeeFamilyInformationDTO familyInformationDTO);

	EmployeeFamilyInformationDTO getFamilyInformation(long familyId);

	void updateEmployeeFamilyDetails(
			EmployeeFamilyInformationDTO familyInformationDTO);

	void deleteFamilymember(long familyId);

	List<HolidaysDTO> getUpcomingHolidays(String country);
	
	List<HolidaysDTO> getOnlyUpcomingHolidays();

	Map<String, Object> getUpcomingBirthdayAnniversaries(Integer startIndex,
			Integer endIndex);

	List<AnniversariesDTO> UpcomingMarriedAnniversaries();

	List<String> getAllDesignations();

	Boolean isemployeeUsernameexist(String userName);

	List<EmployeeBankInformationDTO> getEmployeeBankInformation(Long employeeId);

	void saveEmployeeBankDetails(EmployeeBankInformationDTO bankInformationDTO);

	EmployeeBankInformationDTO getBankInformation(Long bankId);

	void updateEmployeeBankDetails(EmployeeBankInformationDTO bankInformationDTO);

	void deleteBankAccount(Long bankId);

	void deleteHoliday(Long holidayId);

	void saveNewHoliday(HolidaysDTO holidaysDTO);

	void updateHoliday(HolidaysDTO holidaysDTO);

	HolidaysDTO getHoliday(Long holidayId);

	Map<String, Object> getUpcomingProvisionalPeriod(Long employeeId,
			Integer startIndex, Integer endIndex);

	FinanceDTO getFinanceInformation(Long employeeId);

	void updateFinanceInformation(FinanceDTO financeDTO);

	List<BankNameLookup> getBankNameLookups();

	CandidateInterviewTimelineDTO getEmployeeHistory(Long employeeId);

	void saveQualiCategory(QualificationLookUpDTO qualificationLookUpDTO);

	List<QualificationLookUpDTO> getQualiactionCategoryList();

	void deleteQualiCategory(Long id);

	void saveNewQualification(QualificationDTO qualificationDto);

	QualificationDTO getAllQualification(Long empID);

	Map<String, Object> multipleSelection();

	void updateQualification(QualificationDTO qualificationDTO);

	Map<String, Object> getEmployeeProfileHistory(Long employeeId);

	Map<String, Object> getFinanceDetailsList(Integer startIndex,
			Integer endIndexm, String employeeName);

	ByteArrayOutputStream exportFinanceData(String employeeNameSearch)
			throws IOException;

	void uploadEmployeeFinanceDetails(MultipartFile mpf, String financeId);

	void downloadFinanceFile(HttpServletResponse response, String fileName);

	ByteArrayOutputStream exportReporteeList(Long empID) throws IOException;
	
	Map<String, Object> getCountryLookUps();

	List<VisaLookUpDTO> getCountryChangeList(Long id);

	Long saveVisaDetails(VisaDetailDTO visaDetailsDTO);

	List<VisaDetailDTO> getEmployeeVisaDetailsList(Long loggedInEmpId);

	VisaDetailDTO getVisaDetails(Long id);

	void updateVisaDetailsToEmployee(VisaDetailDTO editVisaDetailsDTO);

	void deleteVisaDetail(Long visaID);

	
}
