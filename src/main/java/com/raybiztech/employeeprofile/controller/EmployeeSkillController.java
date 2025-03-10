package com.raybiztech.employeeprofile.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.certification.Dto.CertificateTypeDto;
import com.raybiztech.appraisals.certification.Dto.CertificationDto;
import com.raybiztech.appraisals.dto.VisaDetailDTO;
import com.raybiztech.appraisals.dto.VisaLookUpDTO;
import com.raybiztech.employeeprofile.service.EmployeeSkillService;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

@Controller
@RequestMapping("/EmployeeSkill")
public class EmployeeSkillController {

	@Autowired
	EmployeeSkillService employeeSkillServiceImpl;

	@RequestMapping(value = "/employeeSkillReport", params = { "startIndex",
			"endIndex", "categoryId", "skillId", "searchstr", "competency",
			"experience" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> EmployeeSkillReport(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String categoryId, @RequestParam String skillId,
			@RequestParam String searchstr, @RequestParam String competency,
			@RequestParam String experience,
			HttpServletResponse httpServletResponse) {
		return employeeSkillServiceImpl.EmployeeSkillReport(startIndex,
				endIndex, categoryId, skillId, searchstr, competency,
				experience);
	}

	@RequestMapping(value = "/getAllEmployeecertificates", params = {
			"startIndex", "endIndex", "selectionTechnology", "selectedCertificate", "multipleSearch" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> EmployeeCertificatesList(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String selectionTechnology, @RequestParam String selectedCertificate,
			@RequestParam String multipleSearch) {
		return employeeSkillServiceImpl.getAllEmployeecertificates(startIndex,
				endIndex, selectionTechnology, selectedCertificate, multipleSearch);
	}

	@RequestMapping(value = "/getCertificationForEmployee", params = { "empId",
			"selectionTechnology", "selectedCertificate", "multipleSearch" }, method = RequestMethod.GET)
	public @ResponseBody List<CertificationDto> EmployeeCertificates(
			@RequestParam Long empId, @RequestParam String selectionTechnology,@RequestParam String selectedCertificate,
			@RequestParam String multipleSearch) {
		return employeeSkillServiceImpl.EmployeeCertificates(empId,
				selectionTechnology,selectedCertificate, multipleSearch);
	}

	@RequestMapping(value = "/getCountries", method = RequestMethod.GET)
	public @ResponseBody List<CountryLookUp> getCountries() {
		return employeeSkillServiceImpl.getCountries();
	}

	@RequestMapping(value = "/getVisaTypeList", method = RequestMethod.GET)
	public @ResponseBody List<VisaLookUpDTO> getVisaTypeList() {
		return employeeSkillServiceImpl.getVisaTypeList();
	}

	@RequestMapping(value = "/getAllVisaDetails", params = { "startIndex",
			"endIndex", "countryId", "visaTypeId", "multipleSearch" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getEmployeeVisaDetailsReport(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam Integer countryId, @RequestParam Long visaTypeId,
			@RequestParam String multipleSearch) {
		return employeeSkillServiceImpl.getEmployeeVisaDetailsReport(
				startIndex, endIndex, countryId, visaTypeId, multipleSearch);
	}

	@RequestMapping(value = "/getEmployeeVisaDetailsList", params = { "empId",
			"countryId", "visaTypeId", "multipleSearch" }, method = RequestMethod.GET)
	public @ResponseBody List<VisaDetailDTO> getEmployeeVisaDetailsList(
			@RequestParam Long empId, @RequestParam Integer countryId,
			@RequestParam Long visaTypeId, @RequestParam String multipleSearch) {
		return employeeSkillServiceImpl.getEmployeeVisaDetailsList(empId,
				countryId, visaTypeId, multipleSearch);
	}

	@RequestMapping(value = "/exportCertificatesList", params = { "selectionTechnology", "selectedCertificate",
			"multipleSearch" }, method = RequestMethod.GET)
	public @ResponseBody void exportCertificatesList(
			@RequestParam String selectionTechnology,@RequestParam String selectedCertificate,
			@RequestParam String multipleSearch,
			HttpServletResponse httpServletResponse) throws Exception {

		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"CertificatesList.csv\"");

		ByteArrayOutputStream os = employeeSkillServiceImpl.exportCertificatesList(
				selectionTechnology,selectedCertificate, multipleSearch);

		httpServletResponse.getOutputStream().write(os.toByteArray());

	}
	
	@RequestMapping(value = "/exportEmployeeVisaList", params = {"countryId", "visaTypeId","multipleSearch",}, method = RequestMethod.GET)
	public @ResponseBody void exportExpenseList(@RequestParam Integer countryId, @RequestParam Long visaTypeId, @RequestParam String multipleSearch,
			HttpServletResponse httpServletResponse) throws Exception{
		
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"EmployeeVisaList.csv\"");
		
		ByteArrayOutputStream os = employeeSkillServiceImpl.exportEmployeeVisaList(countryId,visaTypeId,multipleSearch);
		
		httpServletResponse.getOutputStream().write(os.toByteArray());
		
	}
	
	@RequestMapping(value = "/addCertificateType", method = RequestMethod.POST)
	public @ResponseBody void addCertificateType(@RequestBody CertificateTypeDto dto) {
		employeeSkillServiceImpl.addCertificateType(dto);
	}
	
	@RequestMapping(value = "/getCertificateTypeList", method = RequestMethod.GET)
	public @ResponseBody List<CertificateTypeDto> getCertificateTypeList(){
		return employeeSkillServiceImpl.getCertificateTypeList();
	}
	
	@RequestMapping(value = "/getCertificateByTechnology",params = {"technologyName"}, method = RequestMethod.GET)
	public @ResponseBody List<CertificateTypeDto> getCertificateByTechnology(@RequestParam String technologyName){
		return employeeSkillServiceImpl.getCertificateByTechnology(technologyName);
	}
	
	@RequestMapping(value = "/checkForDuplicateCertificate", params = {"technologyId","certificateType"}, method = RequestMethod.GET)
	public @ResponseBody Boolean checkForDuplicateCertificate(@RequestParam Long technologyId,@RequestParam String certificateType) {
		return employeeSkillServiceImpl.checkForDuplicateCertificate(technologyId,certificateType);
	}
	
	@RequestMapping(value = "/editCertificateType",params = {"certificateId"}, method = RequestMethod.GET)
	public @ResponseBody CertificateTypeDto getCertificateTypeDetails(@RequestParam Long certificateId){
		return employeeSkillServiceImpl.getCertificateTypeDetails(certificateId);
	}
	
	@RequestMapping(value = "/updateCertificatetype", method = RequestMethod.PUT)
	public @ResponseBody void updateCertificatetype(@RequestBody CertificateTypeDto dto) {
		employeeSkillServiceImpl.updateCertificatetype(dto);
	}
	
	@RequestMapping(value = "/deleteCertificateType", params = {"certificateId"}, method = RequestMethod.DELETE)
	public @ResponseBody void deleteCertificateType(@RequestParam Long certificateId) {
		employeeSkillServiceImpl.deleteCertificateType(certificateId);
	}
}
