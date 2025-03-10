/*
 * To change this license header, choose License Headers in Project Properties. 
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.employeeprofile.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.business.VisaDetails;
import com.raybiztech.appraisals.certification.Dto.CertificationDto;
import com.raybiztech.appraisals.certification.service.CertificationService;
import com.raybiztech.appraisals.dto.QualificationDTO;
import com.raybiztech.appraisals.dto.QualificationLookUpDTO;
import com.raybiztech.appraisals.dto.VisaDetailDTO;
import com.raybiztech.appraisals.dto.VisaLookUpDTO;
import com.raybiztech.appraisals.observation.exceptions.DuplicateObservationException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.dto.AssetDto;
import com.raybiztech.assetmanagement.service.AssetManagementService;
import com.raybiztech.employeeprofile.Exception.HolidayAlredyExistException;
import com.raybiztech.employeeprofile.dto.EmployeeBankInformationDTO;
import com.raybiztech.employeeprofile.dto.EmployeeFamilyInformationDTO;
import com.raybiztech.employeeprofile.dto.FinanceDTO;
import com.raybiztech.employeeprofile.service.EmployeeProfileServiceI;
import com.raybiztech.lookup.business.BankNameLookup;
import com.raybiztech.recruitment.dto.AnniversariesDTO;
import com.raybiztech.recruitment.dto.CandidateInterviewTimelineDTO;
import com.raybiztech.recruitment.dto.HolidaysDTO;
import com.raybiztech.supportmanagement.dto.TicketsSubCategoryDTO;

/**
 * 
 * @author naresh
 */
@Controller
@RequestMapping("/Employee")
public class EmployeeProfileController {

	@Autowired
	EmployeeProfileServiceI employeeProfileServiceImpl;
	@Autowired
	CertificationService certificationService;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	AssetManagementService assetManagementServiceImpl;

	Logger logger = Logger.getLogger(EmployeeProfileController.class);

	/*
	 * @RequestMapping(value = "/familyInformation", method = RequestMethod.GET)
	 * public @ResponseBody List<EmployeeFamilyInformationDTO>
	 * getEmployeeFamilyInformation() {
	 * 
	 * return employeeProfileServiceImpl .getEmployeeFamilyInformation(securityUtils
	 * .getLoggedEmployeeIdforSecurityContextHolder()); }
	 */

	@RequestMapping(value = "/familyInformation", params = { "loggedInEmpId" }, method = RequestMethod.GET)
	public @ResponseBody List<EmployeeFamilyInformationDTO> getEmployeeFamilyInformation(
			@RequestParam Long loggedInEmpId, HttpServletResponse httpServletResponse) {
		return employeeProfileServiceImpl.getEmployeeFamilyInformation(loggedInEmpId);
	}

	@RequestMapping(value = "/saveFamilyInformation", method = RequestMethod.POST)
	public @ResponseBody void saveEmployeeFamilyDetails(@RequestBody EmployeeFamilyInformationDTO familyInformationDTO,
			HttpServletResponse value) {
		employeeProfileServiceImpl.saveEmployeeFamilyDetails(familyInformationDTO);
	}

	@RequestMapping(value = "/getFamilyInformation", params = { "familyId" }, method = RequestMethod.GET)
	public @ResponseBody EmployeeFamilyInformationDTO getFamilyInformation(@RequestParam Long familyId,
			HttpServletResponse httpServletResponse) {
		return employeeProfileServiceImpl.getFamilyInformation(familyId);
	}

	@RequestMapping(value = "/updateFamilyInformation", method = RequestMethod.POST)
	public @ResponseBody void updateEmployeeFamilyDetails(
			@RequestBody EmployeeFamilyInformationDTO familyInformationDTO, HttpServletResponse value) {
		employeeProfileServiceImpl.updateEmployeeFamilyDetails(familyInformationDTO);
	}

	@RequestMapping(value = "/deleteFamilymember", params = { "familyId" }, method = RequestMethod.GET)
	public @ResponseBody void deleteFamilymember(@RequestParam Long familyId, HttpServletResponse httpServletResponse) {
		employeeProfileServiceImpl.deleteFamilymember(familyId);
	}

	@RequestMapping(value = "/upcomingHolidays", params={"country"}, method = RequestMethod.GET)
	public @ResponseBody List<HolidaysDTO> getUpcomingHolidays(@RequestParam String country) {
		return employeeProfileServiceImpl.getUpcomingHolidays(country);
	}
	@RequestMapping(value = "/onlyUpcomingHolidays", method = RequestMethod.GET)
	public @ResponseBody List<HolidaysDTO> getOnlyUpcomingHolidays() {
		return employeeProfileServiceImpl.getOnlyUpcomingHolidays();
	}
	@RequestMapping(value = "/upcomingBirthdayAnniversaries", params = { "startIndex",
			"endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getUpcomingBirthdayAnniversaries(@RequestParam Integer startIndex,
			@RequestParam Integer endIndex) {
		return employeeProfileServiceImpl.getUpcomingBirthdayAnniversaries(startIndex, endIndex);
	}

	@RequestMapping(value = "/upcomingMarriedAnniversaries", method = RequestMethod.GET)
	public @ResponseBody List<AnniversariesDTO> UpcomingMarriedAnniversaries() {
		return employeeProfileServiceImpl.UpcomingMarriedAnniversaries();
	}

	@RequestMapping(value = "/allDesignations", method = RequestMethod.GET)
	public @ResponseBody List<String> getAllDesignations() {
		// TODO Auto-generated method stub
		return employeeProfileServiceImpl.getAllDesignations();
	}

	@RequestMapping(value = "/isemployeeUsernameexist", params = { "userName" }, method = RequestMethod.GET)
	@ResponseBody
	public Boolean isemployeeUsernameexist(@RequestParam String userName) {
		return employeeProfileServiceImpl.isemployeeUsernameexist(userName);
	}

	/*
	 * @RequestMapping(value = "/bankInformation", method = RequestMethod.GET)
	 * public @ResponseBody Map<String, Object> getEmployeeBankInformation() {
	 * 
	 * String loggedInEmpId = String.valueOf(securityUtils
	 * .getLoggedEmployeeIdforSecurityContextHolder());
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>();
	 * 
	 * FinanceDTO financedto = employeeProfileServiceImpl
	 * .getFinanceInformation(Long.parseLong(loggedInEmpId)); map.put("finance",
	 * financedto); List<EmployeeBankInformationDTO> list = new
	 * ArrayList<EmployeeBankInformationDTO>(); list =
	 * employeeProfileServiceImpl.getEmployeeBankInformation(Long
	 * .parseLong(loggedInEmpId)); map.put("bankinfo", list);
	 * 
	 * return map; }
	 */

	@RequestMapping(value = "/bankInformation", params = { "loggedInEmpId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getEmployeeBankInformation(@RequestParam Long loggedInEmpId,
			HttpServletResponse httpServletResponse) {
		Map<String, Object> map = new HashMap<String, Object>();

		FinanceDTO financedto = employeeProfileServiceImpl.getFinanceInformation(loggedInEmpId);
		map.put("finance", financedto);
		List<EmployeeBankInformationDTO> list = new ArrayList<EmployeeBankInformationDTO>();
		list = employeeProfileServiceImpl.getEmployeeBankInformation(loggedInEmpId);
		map.put("bankinfo", list);

		return map;
	}

	@RequestMapping(value = "/saveBankInformation", method = RequestMethod.POST)
	public @ResponseBody void saveEmployeeBankDetails(@RequestBody EmployeeBankInformationDTO bankInformationDTO,
			HttpServletResponse httpServletResponse) {
		employeeProfileServiceImpl.saveEmployeeBankDetails(bankInformationDTO);
	}

	@RequestMapping(value = "/bankInformation", params = { "bankId" }, method = RequestMethod.GET)
	public @ResponseBody EmployeeBankInformationDTO getBankInformation(@RequestParam Long bankId,
			HttpServletResponse httpServletResponse) {
		return employeeProfileServiceImpl.getBankInformation(bankId);
	}

	@RequestMapping(value = "/updateBankInformation", method = RequestMethod.POST)
	public @ResponseBody void updateEmployeeBankDetails(@RequestBody EmployeeBankInformationDTO bankInformationDTO,
			HttpServletResponse value) {
		employeeProfileServiceImpl.updateEmployeeBankDetails(bankInformationDTO);
	}

	@RequestMapping(value = "/deleteBankAccount", params = { "bankId" }, method = RequestMethod.GET)
	public @ResponseBody void deleteBankAccount(@RequestParam Long bankId, HttpServletResponse value) {
		employeeProfileServiceImpl.deleteBankAccount(bankId);
	}

	@RequestMapping(value = "/deleteHoliday", params = { "holidayId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteHoliday(@RequestParam Long holidayId) {
		employeeProfileServiceImpl.deleteHoliday(holidayId);
	}

	@RequestMapping(value = "/saveHoliday", method = RequestMethod.POST)
	public @ResponseBody void saveNewHoliday(@RequestBody HolidaysDTO holidaysDTO,
			HttpServletResponse httpServletResponse) {
		try {
			employeeProfileServiceImpl.saveNewHoliday(holidaysDTO);
		} catch (HolidayAlredyExistException e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "/editHoliday", method = RequestMethod.PUT)
	public @ResponseBody void editHoliday(@RequestBody HolidaysDTO holidaysDTO,
			HttpServletResponse httpServletResponse) {
		try {
			employeeProfileServiceImpl.updateHoliday(holidaysDTO);
		} catch (HolidayAlredyExistException e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "/holiday", params = { "holidayId" }, method = RequestMethod.GET)
	public @ResponseBody HolidaysDTO getHoliday(@RequestParam Long holidayId) {
		return employeeProfileServiceImpl.getHoliday(holidayId);
	}

	@RequestMapping(value = "/provisionPeriod", params = { "employeeId", "startIndex",
			"endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getUpcomingProvisionalPeriod(Long employeeId, Integer startIndex,
			Integer endIndex, HttpServletResponse httpServletResponse) {
		return employeeProfileServiceImpl.getUpcomingProvisionalPeriod(employeeId, startIndex, endIndex);
	}

	@RequestMapping(value = "/certification", method = RequestMethod.POST)
	public @ResponseBody void certification(@RequestBody CertificationDto certificationDto, HttpServletResponse value) {
		certificationService.addCertificate(certificationDto);
	}

	@RequestMapping(value = "/certification", method = RequestMethod.PUT)
	public @ResponseBody void updateCertification(@RequestBody CertificationDto certificationDto,
			HttpServletResponse httpServletResponse) {
		certificationService.updateCertificateForEmployee(certificationDto);
	}

	@RequestMapping(value = "/certification", params = { "certificationId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteCertification(@RequestParam Long certificationId, HttpServletResponse value) {
		certificationService.deleteCertification(certificationId);
	}

	@RequestMapping(value = "/certification", method = RequestMethod.GET)
	public @ResponseBody List<CertificationDto> getCertifications() {

		return certificationService
				.getCertificatesForEmployee(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
	}

	@RequestMapping(value = "/employeeCertification", params = { "empId" }, method = RequestMethod.GET)
	public @ResponseBody List<CertificationDto> getEmployeeCertifications(@RequestParam Long empId,
			HttpServletResponse httpServletResponse) {

		return certificationService.getCertificatesForEmployee(empId);
	}

	@RequestMapping(value = "/getCertification/{certificationId}", method = RequestMethod.GET)
	public @ResponseBody CertificationDto getCertificate(@PathVariable Long certificationId,
			HttpServletResponse value) {
		return certificationService.getCertificate(certificationId);
	}

	@RequestMapping(value = "/updateFinanceInformation", method = RequestMethod.POST)
	public @ResponseBody void updateFinanceInformation(@RequestBody FinanceDTO financeDTO, HttpServletResponse value) {
		employeeProfileServiceImpl.updateFinanceInformation(financeDTO);
	}

	@RequestMapping(value = "/financeInformation", params = { "loggedInEmpId" }, method = RequestMethod.GET)
	public @ResponseBody FinanceDTO getFinanceInformation(@RequestParam Long loggedInEmpId) {
		return employeeProfileServiceImpl.getFinanceInformation(loggedInEmpId);
	}

	@RequestMapping(value = "/getBankNameLookup", method = RequestMethod.GET)
	public @ResponseBody List<BankNameLookup> getBankLookup() {
		return employeeProfileServiceImpl.getBankNameLookups();
	}

	@RequestMapping(value = "/getEmployeeHistory", params = { "loggedInEmpId" }, method = RequestMethod.GET)
	public @ResponseBody CandidateInterviewTimelineDTO getEmployeeHistory(@RequestParam Long loggedInEmpId,
			HttpServletResponse httpServletResponse) {
		return employeeProfileServiceImpl.getEmployeeHistory(loggedInEmpId);
	}

	@RequestMapping(value = "/saveQualiCategory", method = RequestMethod.POST)
	public @ResponseBody void saveQualiCategory(@RequestBody QualificationLookUpDTO qualificationLookUpDTO,
			HttpServletResponse httpServletResponse) {
		employeeProfileServiceImpl.saveQualiCategory(qualificationLookUpDTO);

	}

	@RequestMapping(value = "/getQualiactionCategoryList", method = RequestMethod.GET)
	public @ResponseBody List<QualificationLookUpDTO> getQualiactionCategoryList() {

		return employeeProfileServiceImpl.getQualiactionCategoryList();
	}

	@RequestMapping(value = "/deleteQualiCategory", params = { "id" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteQualiCategory(@RequestParam("id") Long id) {
		employeeProfileServiceImpl.deleteQualiCategory(id);
	}

	@RequestMapping(value = "/saveNewQualification", method = RequestMethod.POST)
	public @ResponseBody void saveNewQualification(@RequestBody QualificationDTO qualificationDto,
			HttpServletResponse value) {
		try {
			employeeProfileServiceImpl.saveNewQualification(qualificationDto);
		} catch (AccessDeniedException e) {
			value.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}

	}

	@RequestMapping(value = "/getEmployeeQualification", params = { "empID" }, method = RequestMethod.GET)
	public @ResponseBody QualificationDTO getEmployeeQualification(Long empID) {
		return employeeProfileServiceImpl.getAllQualification(empID);
	}

	@RequestMapping(value = "/multipleSelection", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> multipleSelection() {
		return employeeProfileServiceImpl.multipleSelection();
	}

	@RequestMapping(value = "/updateQualification", method = RequestMethod.PUT)
	public @ResponseBody void updateQualification(@RequestBody QualificationDTO qualificationDto,
			HttpServletResponse value) {
		try {
			employeeProfileServiceImpl.updateQualification(qualificationDto);
		} catch (AccessDeniedException e) {
			value.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}

	}

	@RequestMapping(value = "/getEmployeeAssets", params = { "employeeId" }, method = RequestMethod.GET)
	public @ResponseBody List<AssetDto> getEmployeeAssets(@RequestParam Long employeeId,
			HttpServletResponse httpServletResponse) {
		return assetManagementServiceImpl.getEmployeeAssets(employeeId);

	}
	@RequestMapping(value = "/getEmployeeProjectslist", params = { "employeeId" }, method = RequestMethod.GET)
	public @ResponseBody List<AssetDto> getEmployeeProjectslist(@RequestParam Long employeeId,
			HttpServletResponse httpServletResponse) {
		return assetManagementServiceImpl.getEmployeeAssets(employeeId);

	}
	

	@RequestMapping(value = "/getEmployeeProfileHistory", params = { "employeeId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getEmployeeProfileHistory(@RequestParam Long employeeId,
			HttpServletResponse httpServletResponse) {
		return employeeProfileServiceImpl.getEmployeeProfileHistory(employeeId);
	}
	
	@RequestMapping(value = "/financeDetails", params = { "startIndex", "endIndex",
			"employeeName" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFinanceDetailsList(@RequestParam Integer startIndex,
			@RequestParam Integer endIndex, @RequestParam String employeeName,
			HttpServletResponse httpServletResponse) {
		return employeeProfileServiceImpl.getFinanceDetailsList(startIndex, endIndex, employeeName);

	}

	@RequestMapping(value = "/exportFinanceList", params = { "employeeNameSearch" }, method = RequestMethod.GET)
	public @ResponseBody void exportFinanceList(@RequestParam String employeeNameSearch,
			HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"FinanceList.csv\"");
		ByteArrayOutputStream os = employeeProfileServiceImpl.exportFinanceData(employeeNameSearch);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	// download files
	@RequestMapping(value = "/downloadFinanceFile", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadFinanceFile(HttpServletResponse response, String fileName) {
		employeeProfileServiceImpl.downloadFinanceFile(response, fileName);
	}

	@RequestMapping(value = "/exportReporteeList", method = RequestMethod.GET)
	public @ResponseBody void exportReporteeList(HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"ReporteeList.csv\"");
		Long empID = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		ByteArrayOutputStream os = employeeProfileServiceImpl.exportReporteeList(empID);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = "/getCountryLookUps", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getCountryLookUps() {

		return employeeProfileServiceImpl.getCountryLookUps();
	}

	@RequestMapping(value = "/getCountryChangeList", params = { "id" }, method = RequestMethod.GET)
	public @ResponseBody List<VisaLookUpDTO> getCountryChangeList(@RequestParam Long id) {
		return employeeProfileServiceImpl.getCountryChangeList(id);
	}

	@RequestMapping(value = "/saveVisaDetails", method = RequestMethod.POST)
	public @ResponseBody Long saveVisaDetails(@RequestBody VisaDetailDTO VisaDetailsDTO, HttpServletResponse value) {
		Long id =null;
		try {
			id=employeeProfileServiceImpl.saveVisaDetails(VisaDetailsDTO);
			
		} catch (DuplicateObservationException ex) {
			value.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		return id;
	}

	@RequestMapping(value = "/getEmployeeVisaDetailsList", params = { "loggedInEmpId" }, method = RequestMethod.GET)
	public @ResponseBody List<VisaDetailDTO> getEmployeeVisaDetailsList(@RequestParam Long loggedInEmpId,
			HttpServletResponse httpServletResponse) {
		return employeeProfileServiceImpl.getEmployeeVisaDetailsList(loggedInEmpId);
	}

	@RequestMapping(value = "/getVisaDetails", params = { "id" }, method = RequestMethod.GET)
	public @ResponseBody VisaDetailDTO getVisaDetails(@RequestParam Long id, HttpServletResponse httpServletResponse) {
		return employeeProfileServiceImpl.getVisaDetails(id);
	}

	@RequestMapping(value = "/updateVisaDetailsToEmployee", method = RequestMethod.PUT)
	public @ResponseBody void updateVisaDetailsToEmployee(@RequestBody VisaDetailDTO editVisaDetailsDTO,
			HttpServletResponse value) {
		try {
			employeeProfileServiceImpl.updateVisaDetailsToEmployee(editVisaDetailsDTO);
		} catch (DuplicateObservationException ex) {
			value.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "/deleteVisaDetail", params = { "visaID" }, method = RequestMethod.GET)
	public @ResponseBody void deleteVisaDetail(@RequestParam Long visaID, HttpServletResponse httpServletResponse) {
		employeeProfileServiceImpl.deleteVisaDetail(visaID);
	}
}
