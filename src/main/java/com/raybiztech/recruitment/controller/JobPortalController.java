package com.raybiztech.recruitment.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.raybiztech.appraisalmanagement.business.AppraisalForm;
import com.raybiztech.appraisalmanagement.business.Designation;
import com.raybiztech.appraisalmanagement.dto.AppraisalFormListDto;
import com.raybiztech.appraisalmanagement.dto.DesignationDto;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dto.CategoryDTO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.EmployeeSkillLookUpDTO;
import com.raybiztech.appraisals.dto.ReportiesHierarchyDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.appraisals.dto.SkillDTO;
import com.raybiztech.appraisals.dto.TimeSlotDTO;
import com.raybiztech.appraisals.exceptions.DuplicateActiveCycleException;
import com.raybiztech.appraisals.observation.dto.ObservationDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.dto.VendorDto;
import com.raybiztech.projectmanagement.invoice.dto.CountryLookUpDTO;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.recruitment.business.Technology;
import com.raybiztech.recruitment.chart.StatusChart;
import com.raybiztech.recruitment.dao.JobPortalDAO;
import com.raybiztech.recruitment.dto.CandidateDTO;
import com.raybiztech.recruitment.dto.CandidateInterviewCycleDTO;
import com.raybiztech.recruitment.dto.CandidateInterviewTimelineDTO;
import com.raybiztech.recruitment.dto.CandidateScheduleDto;
import com.raybiztech.recruitment.dto.CandidateStatus;
import com.raybiztech.recruitment.dto.CompaniesDTO;
import com.raybiztech.recruitment.dto.CompanyDTO;
import com.raybiztech.recruitment.dto.DepartmentDTO;
import com.raybiztech.recruitment.dto.JobVacancyDTO;
import com.raybiztech.recruitment.dto.NewJoineeDTO;
import com.raybiztech.recruitment.dto.PanelDTO;
import com.raybiztech.recruitment.dto.ScheduledCadidateDTO;
import com.raybiztech.recruitment.dto.SearchQueryParams;
import com.raybiztech.recruitment.dto.SkillLookUpDTO;
import com.raybiztech.recruitment.exception.CandidateCantBeDeletedException;
import com.raybiztech.recruitment.exception.DuplicateScheduleException;
import com.raybiztech.recruitment.exception.JobPortalException;
import com.raybiztech.recruitment.service.JobPortalService;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.recruitment.dto.EmploymentTypeDTO;
import com.raybiztech.recruitment.dto.GenderDTO;
import com.raybiztech.recruitment.dto.MaritalStatusDTO;
import com.raybiztech.recruitment.dto.BloodGroupDTO;
import com.raybiztech.recruitment.dto.QualificationCategoryDTO;
import com.raybiztech.recruitment.dto.RelationsDTO;
import com.raybiztech.recruitment.dto.JobTypeDTO;

@Controller
@RequestMapping("/jobapplicant")
public class JobPortalController {

	@Autowired
	JobPortalService jobPortalServiceImpl;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	JobPortalDAO jobPortalDAOImpl;

	Logger logger = Logger.getLogger(JobPortalController.class);

	@RequestMapping(value = "/jobAdmin/addNewCandidate", method = RequestMethod.POST)
	public @ResponseBody Long addNewCandidadate(
			@RequestBody CandidateScheduleDto candidateScheduleDto)
			throws JobPortalException {
		Long id = jobPortalServiceImpl.addNewCandidadate(candidateScheduleDto);

		jobPortalServiceImpl.sendScheduleMailToNewCandidate(
				candidateScheduleDto, id);

		return id;

	}

	@RequestMapping(value = "/getAllEmployeeData", method = RequestMethod.GET)
	public @ResponseBody List<EmployeeDTO> getAllEmployeeData()
			throws JobPortalException {
		return jobPortalServiceImpl.getAllEmployeeData();
	}

	@RequestMapping(value = "/getCandidate", method = RequestMethod.GET)
	public @ResponseBody List<CandidateDTO> getAllCandidate()
			throws JobPortalException {
		List<CandidateDTO> candidateDTOs = jobPortalServiceImpl
				.getAllCandidate();
		return candidateDTOs;
	}

	// @RequestMapping(value = "/searchCadidates", params = {"skill",
	// "fromDate", "toDate"}, method = RequestMethod.GET)
	// public @ResponseBody
	// List<ScheduledCadidateDTO> searchCadidates(@RequestParam String skill,
	// @RequestParam String fromDate, @RequestParam String toDate) {
	// List<ScheduledCadidateDTO> scheduleDTO =
	// jobPortalServiceImpl.searchCadidates(skill, fromDate, toDate);
	// return scheduleDTO;
	//
	// }
	@RequestMapping(value = "/searchCadidates", params = { "skill", "fromDate",
			"toDate", "startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchCandidates(
			@RequestParam String skill, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex) {

		return jobPortalServiceImpl.searchCandidates(skill, fromDate, toDate,
				startIndex, endIndex);

	}

	@RequestMapping(value = "/searchScheduledCandidates", params = { "skill",
			"fromDate", "toDate", "search", "startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchScheduledCandidates(
			@RequestParam String skill, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam String search,
			@RequestParam Integer startIndex, @RequestParam Integer endIndex) {

		return jobPortalServiceImpl.searchScheduledCandidates(skill, fromDate,
				toDate, search, startIndex, endIndex);

	}

	@RequestMapping(value = "/getAllSkillsLookUp", method = RequestMethod.GET)
	@ResponseBody
	public List<SkillLookUpDTO> getAllSkillsLookUp() {
		return jobPortalServiceImpl.getAllSkillsLookUp();

	}

	@RequestMapping(value = "/jobAdmin/scheduleCandidateUpdate", method = RequestMethod.POST)
	public @ResponseBody void updateScheduleCandidadate(
			@RequestBody ScheduledCadidateDTO scheduledCadidateDTO)
			throws JobPortalException {
		jobPortalServiceImpl.updateScheduleCandidadate(scheduledCadidateDTO);
	}

	@RequestMapping(value = "/jobAdmin/allCadidates", params = { "startIndex",
			"endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllCadidates(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex)
			throws JobPortalException {
		return jobPortalServiceImpl.getAllCadidates(startIndex, endIndex);

	}

	@RequestMapping(value = "/jobAdmin/searchScheduledCandidate", params = {
			"startIndex", "endIndex", "searchStr" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchScheduledCandidate(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String searchStr) throws JobPortalException {
		return jobPortalServiceImpl.searchScheduledCandidate(startIndex,
				endIndex, searchStr);

	}

	@RequestMapping(value = "/jobAdmin/editCandidate", params = { "candidateId" }, method = RequestMethod.GET)
	public @ResponseBody CandidateScheduleDto editCandidate(
			@RequestParam String candidateId) {

		return jobPortalServiceImpl.editCandidate(candidateId);

	}

	@RequestMapping(value = "/jobAdmin/deleteCandidate", params = { "candidateId" }, method = RequestMethod.GET)
	public @ResponseBody void deleteCandidate(@RequestParam String candidateId) {

		jobPortalServiceImpl.deleteCandidate(candidateId);

	}

	@RequestMapping(value = "/jobAdmin/editNewCandidate", method = RequestMethod.POST)
	public @ResponseBody void updateCandidadate(
			@RequestBody CandidateScheduleDto candidateScheduleDto)
			throws JobPortalException {
		jobPortalServiceImpl.updateCandidadate(candidateScheduleDto);
	}

	@RequestMapping(value = "/jobAdmin/scheduleInterview", method = RequestMethod.POST)
	public @ResponseBody void scheduleInterview(
			@RequestBody CandidateScheduleDto candidateScheduleDto,
			HttpServletResponse httpServletResponse) throws JobPortalException {
		try {
			jobPortalServiceImpl.scheduleInterview(candidateScheduleDto);
		} catch (DuplicateScheduleException me) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "/jobAdmin/addEmployeeToPanel", params = {
			"employeeEmailId", "departmentId" }, method = RequestMethod.GET)
	public @ResponseBody void addEmployeeToPanel(
			@RequestParam String employeeEmailId, String departmentId) {
		jobPortalServiceImpl.addEmployeeToPanel(employeeEmailId, departmentId);

	}

	//
	// @RequestMapping(value = "/getAllEmployeePanelData", method =
	// RequestMethod.GET)
	// public @ResponseBody
	// List<PanelDTO> getAllEmployeePanelData() {
	// return jobPortalServiceImpl.getAllEmployeePanelData();
	//
	// }

	@RequestMapping(value = "/jobAdmin/deleteEmployeeToPanel", params = { "panelId" }, method = RequestMethod.GET)
	public @ResponseBody void deleteEmployeeToPanel(@RequestParam String panelId) {
		jobPortalServiceImpl.deleteEmployeeToPanel(panelId);
	}

	@RequestMapping(value = "/loggedInEmployee", params = { "loggedInEmpId" }, method = RequestMethod.GET)
	public @ResponseBody EmployeeDTO loggedInEmployeeData(
			@RequestParam String loggedInEmpId, HttpServletResponse value) {

		return jobPortalServiceImpl.getEmployee(Long.parseLong(loggedInEmpId));
	}

	@RequestMapping(value = "/loggedInEmployeeData", params = { "loggedInEmpId" }, method = RequestMethod.GET)
	public @ResponseBody EmployeeDTO loggedInEmployeeProfileData(
			@RequestParam String loggedInEmpId, HttpServletResponse res) {

		logger.warn("logged in employee" + loggedInEmpId);

		Long empId = Long.parseLong(loggedInEmpId);

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		/*
		 * Permission empdirectoryPermission = jobPortalDAOImpl
		 * .checkForPermission("Employee Directory", employee);
		 */

		Permission profileReporteesPermission = jobPortalDAOImpl
				.checkForPermission("My Profile-Reporties", employee);

		Permission allReportPermission = jobPortalDAOImpl.checkForPermission(
				"Reporties All", employee);

		if (profileReporteesPermission.getView()
				&& !allReportPermission.getView()) {

			logger.warn("in manger view");

			List<ReportiesHierarchyDTO> reportieesList = jobPortalServiceImpl
					.getReportiesUnderManager(employee.getEmployeeId());

			System.out.println("object" + reportieesList);

			List<Long> empIds = new ArrayList<Long>();

			for (ReportiesHierarchyDTO emp : reportieesList) {

				if (emp.getReporteeId().equals(empId)) {
					// System.out.println("reporteesList");
					empIds.add(empId);
				}

			}

			if (empIds.contains(empId)) {

				logger.warn("in reportees");

				return jobPortalServiceImpl.getEmployeeDataFormSearch(
						Long.parseLong(loggedInEmpId), res);
			} else {
				logger.warn("in unauthorized");
				res.setStatus(res.SC_FORBIDDEN);
				return null;
			}

		} else if (profileReporteesPermission.getView()
				&& allReportPermission.getView()) {
			logger.warn("in admin view");
			return jobPortalServiceImpl.getEmployeeDataFormSearch(
					Long.parseLong(loggedInEmpId), res);

		} else {
			res.setStatus(res.SC_FORBIDDEN);
			return null;
		}

		/*
		 * return jobPortalServiceImpl.getEmployeeDataFormSearch(
		 * Long.parseLong(loggedInEmpId), res);
		 */
	}

	@RequestMapping(value = "/getAllProfileEmployeesData", params = { "searchStr" }, method = RequestMethod.GET)
	public @ResponseBody List<SearchEmpDetailsDTO> getAllEmployeesData(
			@RequestParam String searchStr) {

		return jobPortalServiceImpl.getAllProfileEmployeesData(searchStr);
	}

	@RequestMapping(value = "/getAllEmployeeDetails", method = RequestMethod.GET)
	public @ResponseBody List<SearchEmpDetailsDTO> getAllEmployeeDetails() {

		return jobPortalServiceImpl.getAllProfileEmployeesData();
	}

	@RequestMapping(value = "/getAllReportingManagersData", method = RequestMethod.GET)
	public @ResponseBody List<EmployeeDTO> getAllReportingManagersData() {

		return jobPortalServiceImpl.getAllReportingManagersData();
	}

	@RequestMapping(value = "/jobAdmin/addNewEmployee", method = RequestMethod.POST)
	public @ResponseBody void addNewEmployee(
			@RequestBody EmployeeDTO employeeDTO,
			HttpServletResponse httpServletResponse) {
		jobPortalServiceImpl.addNewEmployee(employeeDTO);
	}

	@RequestMapping(value = "/deleteEmployee", params = { "employeeId" }, method = RequestMethod.GET)
	public @ResponseBody void deleteEmployee(@RequestParam String employeeId) {
		jobPortalServiceImpl.deleteEmployee(Long.parseLong(employeeId));
	}

	@RequestMapping(value = "/updateEmployeeDetails", method = RequestMethod.POST)
	public @ResponseBody void updateEmployeeDetails(
			@RequestBody EmployeeDTO employeeDTO,
			HttpServletResponse httpServletResponse) {

		jobPortalServiceImpl.updateEmployeeDetails(employeeDTO);
	}

	@RequestMapping(value = "/addSkillToEmployee", method = RequestMethod.POST)
	public @ResponseBody void addSkillToEmployee(
			@RequestBody SkillDTO skillDTO,
			HttpServletResponse httpServletResponse) {

		jobPortalServiceImpl.addSkillToEmployee(skillDTO);
	}

	@RequestMapping(value = "/updateEmployeeSkill", method = RequestMethod.POST)
	public @ResponseBody void updateEmployeeSkill(
			@RequestBody SkillDTO skillDTO, HttpServletResponse value) {
		jobPortalServiceImpl.updateEmployeeSkill(skillDTO);
	}

	@RequestMapping(value = "/deleteEmployeeSkill", params = { "skillId" }, method = RequestMethod.GET)
	public @ResponseBody void deleteEmployeeSkill(@RequestParam String skillId,
			HttpServletResponse value) {
		jobPortalServiceImpl.deleteEmployeeSkill(Long.parseLong(skillId));
	}

	@RequestMapping(value = "/editSkill", params = { "skillId" }, method = RequestMethod.GET)
	public @ResponseBody SkillDTO editSkill(@RequestParam String skillId,
			HttpServletResponse value) {
		return jobPortalServiceImpl.editSkill(Long.parseLong(skillId));
	}

	@RequestMapping(value = "/getCategorySkill", params = { "categoryId" }, method = RequestMethod.GET)
	public @ResponseBody List<EmployeeSkillLookUpDTO> getCategorySkill(
			@RequestParam String categoryId) {

		if (!categoryId.isEmpty()) {
			Long parsedCategoryID = Long.parseLong(categoryId);
			return jobPortalServiceImpl.getCategorySkill(parsedCategoryID);
		}
		return null;

	}

	@RequestMapping(value = "/getAllCategories", method = RequestMethod.GET)
	public @ResponseBody List<CategoryDTO> getAllCategories() {

		return jobPortalServiceImpl.getAllCategories();
	}

	@RequestMapping(value = "/addCategory", params = { "categoryName" }, method = RequestMethod.GET)
	public @ResponseBody List<CategoryDTO> addCategory(
			@RequestParam String categoryName) {
		return jobPortalServiceImpl.addCategory(categoryName);
	}

	@RequestMapping(value = "/deleteCategory", params = { "categoryId" }, method = RequestMethod.GET)
	public @ResponseBody List<CategoryDTO> deleteCategory(
			@RequestParam String categoryId) {
		return jobPortalServiceImpl.deleteCategory(Long.parseLong(categoryId));
	}

	@RequestMapping(value = "/addSkillToSpecificCategory", params = {
			"skillName", "categoryId" }, method = RequestMethod.GET)
	public @ResponseBody List<EmployeeSkillLookUpDTO> addSkillToSpecificCategory(
			@RequestParam String skillName, @RequestParam String categoryId) {
		return jobPortalServiceImpl.addSkillToSpecificCategory(skillName,
				Long.parseLong(categoryId));
	}

	@RequestMapping(value = "/deleteSkill", params = { "skillId" }, method = RequestMethod.GET)
	public @ResponseBody List<EmployeeSkillLookUpDTO> deleteSkill(
			@RequestParam String skillId) {
		return jobPortalServiceImpl.deleteSkill(Long.parseLong(skillId));
	}

	@RequestMapping(value = "/getEmployeeskillList", method = RequestMethod.GET)
	public @ResponseBody List<SkillDTO> getEmployeeskillList() {

		Long loggedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		return jobPortalServiceImpl.getEmployeeskillList(loggedInEmpId);
	}

	@RequestMapping(value = "/getEmployeeskills", params = { "empId" }, method = RequestMethod.GET)
	public @ResponseBody List<SkillDTO> getEmployeeskills(
			@RequestParam Long empId, HttpServletResponse httpServletResponse) {

		Long loggedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		return jobPortalServiceImpl.getEmployeeskillList(empId);
	}

	@RequestMapping(value = "/importFile", params = { "filePath" }, method = RequestMethod.POST)
	public @ResponseBody void importCandiadetFile(@RequestParam String filePath) {
		jobPortalServiceImpl.importCandiadetFile(filePath);
	}

	@RequestMapping(value = "/getAllUpcomingJoineeList", method = RequestMethod.GET)
	@ResponseBody
	public List<ScheduledCadidateDTO> getAllUpcomingJoineeList() {
		return jobPortalServiceImpl.getAllUpcomingJoineeList();

	}

	@RequestMapping(value = "/overwriteDuplicateCandidateData", method = RequestMethod.POST)
	public @ResponseBody void overwriteDuplicateCandidateData(
			@RequestBody CandidateDTO candidateDto) throws JobPortalException {
		jobPortalServiceImpl.overwriteDuplicateCandidateData(candidateDto);
	}

	@RequestMapping(value = "/jobAdmin/sendScheduleMailToCandidate", method = RequestMethod.POST)
	public @ResponseBody void sendScheduleMailToCandidate(
			@RequestBody CandidateScheduleDto candidateScheduleDto)
			throws JobPortalException {
		jobPortalServiceImpl.sendScheduleMailToCandidate(candidateScheduleDto);
	}

	@RequestMapping(value = "/jobAdmin/sendUpdateScheduleMailToCandidate", method = RequestMethod.POST)
	public @ResponseBody void sendUpdateScheduleMailToCandidate(
			@RequestBody CandidateScheduleDto candidateScheduleDto)
			throws JobPortalException {
		jobPortalServiceImpl
				.sendUpdateScheduleMailToCandidate(candidateScheduleDto);
	}

	// saving initial comments
	@RequestMapping(value = "/jobAdmin/saveInitialComments", params = {
			"initialComments", "personId" }, method = RequestMethod.PUT)
	public @ResponseBody void saveInitialComments(
			@RequestParam String initialComments, @RequestParam Long personId) {
		jobPortalServiceImpl.savingInitialComments(initialComments, personId);
	}

	// /viewScheduleCandidate

	@RequestMapping(value = "/timelinedetails", params = { "candidateId" }, method = RequestMethod.GET)
	public @ResponseBody CandidateInterviewTimelineDTO timeLineDetails(
			@RequestParam String candidateId,
			HttpServletResponse httpServletResponse) {
		return jobPortalServiceImpl.timeLineDetails(candidateId);
	}

	@RequestMapping(value = "/isCandidateMailExists", params = { "candidateEmail" }, method = RequestMethod.GET)
	@ResponseBody
	public Boolean isCandidateMailExists(@RequestParam String candidateEmail) {
		return jobPortalServiceImpl.isCandidateMailExists(candidateEmail);
	}

	@RequestMapping(value = "/isCandidateMobileNumberExists", params = { "candidateMobileNumber" }, method = RequestMethod.GET)
	@ResponseBody
	public Boolean isCandidateMobileNumberExists(
			@RequestParam String candidateMobileNumber) {
		return jobPortalServiceImpl
				.isCandidateMobileNumberExists(candidateMobileNumber);
	}

	@RequestMapping(value = "/isEditCandidateMailExists", params = {
			"candidateId", "mailId" }, method = RequestMethod.GET)
	@ResponseBody
	public Boolean isEditCandidateMailExists(@RequestParam String candidateId,
			@RequestParam String mailId) {
		return jobPortalServiceImpl.isEditCandidateMailExists(candidateId,
				mailId);
	}

	@RequestMapping(value = "/isEditCandidateMobileNumberExists", params = {
			"candidateId", "mobileNum" }, method = RequestMethod.GET)
	@ResponseBody
	public Boolean isEditCandidateMobileNumberExists(
			@RequestParam String candidateId, @RequestParam String mobileNum) {
		return jobPortalServiceImpl.isEditCandidateMobileNumberExists(
				candidateId, mobileNum);
	}

	// we are using StringBuffer because of we are using string param in job
	// portal controller aop
	@RequestMapping(value = "/EmployeesIndexData", params = { "startIndex",
			"endIndex", "selectionStatus", "searchStr" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getProfilePaginationEmployeesData(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String selectionStatus,
			@RequestParam StringBuffer searchStr,
			HttpServletResponse httpServletResponse) {
		return jobPortalServiceImpl.getProfilePaginationEmployeesData(
				startIndex, endIndex, selectionStatus, searchStr.toString());
	}
	
	@RequestMapping(value = "/EmployeesCategoryData", params = { "startIndex",
			"endIndex", "selectionDesignation" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> EmployeesCategoryData(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String selectionDesignation,
			HttpServletResponse httpServletResponse) {
		System.out.println("desg:" + selectionDesignation);
		return jobPortalServiceImpl.getEmployeesCategoryData(
				startIndex, endIndex, selectionDesignation);
	}
	@RequestMapping(value = "/getSelectedTypeEmployeeData", params = { "startIndex",
			"endIndex", "selectionStatus","selectionType","searchStr" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSelectedTypeEmployeeData(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String selectionStatus, @RequestParam String selectionType,
			@RequestParam StringBuffer searchStr,@RequestParam Integer country,
			HttpServletResponse httpServletResponse) {
		return jobPortalServiceImpl.getSelectedTypeEmployeeData(
				startIndex, endIndex, selectionStatus, selectionType, searchStr.toString(),country);
	}

	@RequestMapping(value = "/exportEmployeeData", params = {
			"selectionStatus", "searchStr" }, method = RequestMethod.GET)
	@ResponseBody
	public void exportEmployeeList(@RequestParam String selectionStatus,
			@RequestParam String searchStr,
			HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"EmployeeList.csv\"");
		ByteArrayOutputStream os = jobPortalServiceImpl.exportEmployeeData(
				selectionStatus, searchStr);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}
	@RequestMapping(value = "/exportEmployeeCategoryData", params = {
	"selectionDesignation"}, method = RequestMethod.GET)
      @ResponseBody
    public void exportEmployeeCategoryData(@RequestParam String selectionDesignation,
	   HttpServletResponse httpServletResponse) throws IOException {
       httpServletResponse.setContentType("text/csv");
         httpServletResponse.setHeader("Content-Disposition",
		   "attachment; filename=\"EmployeeDesignationList.csv\"");
         ByteArrayOutputStream os = jobPortalServiceImpl.exportEmployeeCategoryData(
		     selectionDesignation);
             httpServletResponse.getOutputStream().write(os.toByteArray());

      }

	@RequestMapping(value = "/jobAdmin/editEmployee", method = RequestMethod.POST)
	public @ResponseBody void editEmployee(
			@RequestBody EmployeeDTO employeeDto,
			HttpServletResponse httpServletResponse) {
		jobPortalServiceImpl.editEmployee(employeeDto);
	}

	@RequestMapping(value = "/jobAdmin/panelDepartmentEmployeeData", params = { "departmentId" }, method = RequestMethod.GET)
	@ResponseBody
	public List<PanelDTO> panelDepartmentEmployeeData(
			@RequestParam String departmentId) {
		return jobPortalServiceImpl.panelDepartmentEmployeeData(departmentId);
	}

	@RequestMapping(value = "/panelDepartment", params = { "departmentId" }, method = RequestMethod.GET)
	@ResponseBody
	public List<EmployeeDTO> panelEmployeeData(@RequestParam String departmentId) {
		return jobPortalServiceImpl.panelEmployeeData(departmentId);
	}

	@RequestMapping(value = "/getAllDepartmentList", method = RequestMethod.GET)
	@ResponseBody
	public List<DepartmentDTO> getAllDepartmentList() {
		return jobPortalServiceImpl.getAllDepartmentList();
	}

	@RequestMapping(value = "/jobAdmin/reSheduleInterviewForCandidate", params = { "candidateId" }, method = RequestMethod.GET)
	public @ResponseBody CandidateInterviewCycleDTO reSheduleInterviewForCandidate(
			@RequestParam String candidateId) {
		return jobPortalServiceImpl.reSheduleInterviewForCandidate(candidateId);
	}

	@RequestMapping(value = "/jobAdmin/reSheduleInterviewUpdate", method = RequestMethod.POST)
	public @ResponseBody void reSheduleInterviewUpdate(
			@RequestBody CandidateInterviewCycleDTO cycleDTO)
			throws JobPortalException {
		jobPortalServiceImpl.reSheduleInterviewUpdate(cycleDTO);
	}

	@RequestMapping(value = "/scheduledCandidatesForEmployee", params = {
			"employeeId", "startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getScheduledCandidatesForEmployee(
			@RequestParam Long employeeId, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex) {
		return jobPortalServiceImpl.getScheduledCandidatesForEmployee(
				employeeId, startIndex, endIndex);
	}

	@RequestMapping(value = "/empScheduleInterviewDetails", params = { "interviewCycleId" }, method = RequestMethod.GET)
	public @ResponseBody CandidateInterviewCycleDTO empScheduleInterviewDetailsByID(
			@RequestParam Long interviewCycleId, HttpServletResponse value) {
		return jobPortalServiceImpl
				.empScheduleInterviewDetailsByID(interviewCycleId);
	}

	@RequestMapping(value = "/jobAdmin/updateScheduleCandidatebyEmployee", method = RequestMethod.POST)
	public @ResponseBody void updateScheduleCandidatebyEmployee(
			@RequestBody CandidateInterviewCycleDTO cycleDTO)
			throws JobPortalException {
		jobPortalServiceImpl.updateScheduleCandidatebyEmployee(cycleDTO);
	}

	@RequestMapping(value = "/searchScheduledCandidatesForEmployee", params = {
			"status", "fromDate", "toDate", "startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getSearchScheduledCandidatesForEmployee(
			@RequestParam String status, @RequestParam String fromDate,
			@RequestParam String toDate, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex) {

		Long employeeId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		return jobPortalServiceImpl.getSearchScheduledCandidatesForEmployee(
				employeeId, status, fromDate, toDate, startIndex, endIndex);
	}

	@RequestMapping(value = "/jobAdmin/getViewCandiadetForAdmin", params = { "candidateId" }, method = RequestMethod.GET)
	public @ResponseBody CandidateInterviewCycleDTO getViewCandiadetForAdmin(
			@RequestParam Long candidateId) {
		return jobPortalServiceImpl.getViewCandiadetForAdmin(candidateId);
	}

	@RequestMapping(value = "/jobAdmin/isJobCodeExist", params = { "jobCode" }, method = RequestMethod.GET)
	public @ResponseBody Boolean isJobCodeExist(@RequestParam String jobCode) {
		return jobPortalServiceImpl.isJobCodeExist(jobCode);
	}

	@RequestMapping(value = "/downloadFile", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadFile(HttpServletResponse response,
			String fileName) {

		jobPortalServiceImpl.downloadFile(response, fileName);
	}

	/*
	 * @RequestMapping(value = "/downloadScheduleCandidates", params = { "skill",
	 * "fromDate", "toDate" }, method = RequestMethod.GET) public @ResponseBody void
	 * downloadScheduleCandidatesFile( HttpServletResponse response, @RequestParam
	 * String skill,
	 * 
	 * @RequestParam String fromDate, @RequestParam String toDate) {
	 * 
	 * jobPortalServiceImpl.downloadScheduleCandidatesFile(response, skill,
	 * fromDate, toDate);
	 * 
	 * } 
	 */

	@RequestMapping(value = "/addNewJoinee", method = RequestMethod.POST)
	public @ResponseBody void addNewJoinee(@RequestBody NewJoineeDTO JoineeDTO) {
		jobPortalServiceImpl.addNewJoinee(JoineeDTO);
	}

	@RequestMapping(value = "/jobAdmin/updateInterview", method = RequestMethod.POST)
	public @ResponseBody void updateInterview(
			@RequestBody CandidateInterviewCycleDTO cicdto)
			throws JobPortalException {
		jobPortalServiceImpl.updateInterview(cicdto);
	}

	@RequestMapping(value = "/interviewRoundCount", params = { "candidateId" }, method = RequestMethod.GET)
	public @ResponseBody Integer countofInterviewRoundList(
			@RequestParam String candidateId) {
		return jobPortalServiceImpl.countofInterviewRoundList(candidateId);
	}

	@RequestMapping(value = "/getUpcomingJoineeList", params = { "startIndex",
			"endIndex", "searchName" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getUpcomingJoineeList(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String searchName) {
		return jobPortalServiceImpl.getUpcomingJoineeList(startIndex, endIndex,
				searchName);
	}

	@RequestMapping(value = "/getJoineeById", params = { "joineeId" }, method = RequestMethod.GET)
	public @ResponseBody NewJoineeDTO getJoineeById(@RequestParam Long joineeId) {
		return jobPortalServiceImpl.getJoineeById(joineeId);
	}

	@RequestMapping(value = "/updateNewJoinee", method = RequestMethod.PUT)
	public @ResponseBody void editUpcomingJoinee(
			@RequestBody NewJoineeDTO joineeDTO) {
		jobPortalServiceImpl.updateNewJoinee(joineeDTO);
	}

	@RequestMapping(value = "/jobAdmin/reScheduleInterview", method = RequestMethod.POST)
	public @ResponseBody void reScheduleInterview(
			@RequestBody CandidateScheduleDto cicdto) throws JobPortalException {
		jobPortalServiceImpl.reScheduleInterview(cicdto);
	}

	@RequestMapping(value = "/jobAdmin/interviewStatusReport", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> interviewStatusReport(
			@RequestBody SearchQueryParams queryParams)
			throws JobPortalException {
		return jobPortalServiceImpl.interviewStatusReport(queryParams);
	}
	@RequestMapping(value = "/jobAdmin/getCountryWiseCandidatesList", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCountryWiseCandidatesList(@RequestBody SearchQueryParams queryParams)
			throws JobPortalException {
		System.out.println("in cont" + queryParams.getStartIndex() + queryParams.getEndIndex());
		return jobPortalServiceImpl.getCountryWiseCandidatesList(queryParams);
	}
	@RequestMapping(value = "/jobAdmin/updateCandidateInterviewStatus", method = RequestMethod.POST)
	public @ResponseBody void updateCandidateInterviewStatus(
			@RequestBody CandidateStatus candidateStatus) {
		jobPortalServiceImpl.updateCandidateInterviewStatus(candidateStatus);
	}

	@RequestMapping(value = "/jobAdmin/isCandidateMappedWithJob", params = { "jobCode" }, method = RequestMethod.GET)
	public @ResponseBody Boolean isCandidateMappedWithJob(
			@RequestParam String jobCode) {
		return jobPortalServiceImpl.isCandidateMappedWithJob(jobCode);
	}

	@RequestMapping(value = "/jobAdmin/deleteJoinee", params = { "joineeId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteJoinee(@RequestParam Long joineeId) {
		jobPortalServiceImpl.deleteJoinee(joineeId);
	}

	@RequestMapping(value = "/getJobOpeningById", method = RequestMethod.GET, params = { "jobVacancyId" })
	public @ResponseBody JobVacancyDTO getJobOpeningById(
			@RequestParam Long jobVacancyId) {
		return jobPortalServiceImpl.getJobOpeningById(jobVacancyId);
	}

	@RequestMapping(value = "/checkeamil", params = { "email" }, method = RequestMethod.GET)
	@ResponseBody
	public Boolean checkemail(@RequestParam String email) {
		return jobPortalServiceImpl.checkemail(email);
	}

	@RequestMapping(value = "/addtechnology", params = { "technology" }, method = RequestMethod.POST)
	@ResponseBody
	public void addTechnology(@RequestParam String technology) {
		jobPortalServiceImpl.addTechnology(technology);
	}

	@RequestMapping(value = "/getAllTechnology", method = RequestMethod.GET)
	@ResponseBody
	public List<Technology> getAllTechnology() {
		return jobPortalServiceImpl.viewTechnology();
	}
	@RequestMapping(value = "/getAllDesignations", method = RequestMethod.GET)
	@ResponseBody
	public List<Designation> getAllDesignations() {
		return jobPortalServiceImpl.viewDesignation();
	}

	@RequestMapping(value = "/deleteTechnology", params = { "technologyId" }, method = RequestMethod.GET)
	public @ResponseBody void deleteTechnology(@RequestParam Long technologyId) {
		jobPortalServiceImpl.deleteTechnology(technologyId);
	}

	@RequestMapping(value = "/defaultPic", params = { "gender" }, method = RequestMethod.POST)
	public @ResponseBody void defaultPic(@RequestParam String gender) {
		jobPortalServiceImpl.defaultPic(gender);
	}

	@RequestMapping(value = "/getpersontechnology", params = { "id" }, method = RequestMethod.GET)
	public @ResponseBody String gettech(@RequestParam Long id) {
		return jobPortalServiceImpl.getpersontechnology(id);
	}

	@RequestMapping(value = "/getReportiesUnderManager", params = { "empID" }, method = RequestMethod.GET)
	public @ResponseBody List<ReportiesHierarchyDTO> getReportiesUnderManager(
			@RequestParam Long empID) {

		/*
		 * Long empID = securityUtils
		 * .getLoggedEmployeeIdforSecurityContextHolder();
		 */
		return jobPortalServiceImpl.getReportiesUnderManager(empID);
	}

	// add the designation
	@RequestMapping(value = "/addDesignation", method = RequestMethod.POST)
	public @ResponseBody void addDesignation(
			@RequestBody DesignationDto designationDto,
			HttpServletResponse response) {
		try {
			jobPortalServiceImpl.addDesignation(designationDto);
		} catch (DuplicateActiveCycleException e) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	// delete the designation
	@RequestMapping(value = "/deleteDesignation", params = { "designationId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteDesignation(
			@RequestParam Long designationId, HttpServletResponse response) {
		try {
			jobPortalServiceImpl.deleteDesignation(designationId);
		} catch (CandidateCantBeDeletedException e) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
	}

	/* To Download CV format */
	@RequestMapping(value = "/downloadCVFormateFile", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadCVFormateFile(
			HttpServletResponse response, String fileName) {

		jobPortalServiceImpl.downloadFile(response, fileName);
	}

	/* To Download existing Rbt cv of Employee */
	@RequestMapping(value = "/downloadRBTCv", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadRBTCv(HttpServletResponse response,
			String fileName) {

		jobPortalServiceImpl.downloadRBTCv(response, fileName);
	}

	@RequestMapping(value = "/getEmployeeMyProfileObservations", method = RequestMethod.GET)
	public @ResponseBody List<ObservationDTO> getEmployeeMyProfileObservations() {

		Long loggedInEmpID = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		return jobPortalServiceImpl
				.getEmployeeMyProfileObservations(loggedInEmpID);
	}

	@RequestMapping(value = "/addTimeSlot", method = RequestMethod.POST)
	public @ResponseBody void addTimeSlot(@RequestBody TimeSlotDTO timeSlotDTO,
			HttpServletResponse response) {
		try {
			jobPortalServiceImpl.addTimeSlot(timeSlotDTO);
		} catch (DuplicateActiveCycleException e) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "/getAllShifting", method = RequestMethod.GET)
	public @ResponseBody List<TimeSlotDTO> getAllShifting() {
		return jobPortalServiceImpl.getAllShifting();
	}

	// To Update shift details in data base
	@RequestMapping(value = "/updateShiftDetail", method = RequestMethod.PUT)
	public @ResponseBody void updateShiftDetail(
			@RequestBody TimeSlotDTO timeSlotDTO) {
		jobPortalServiceImpl.updateShiftDetail(timeSlotDTO);
	}

	// To delete shift details
	@RequestMapping(value = "/deleteShiftDetail", params = { "shifId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteShiftDetail(
			@RequestParam("shifId") Long shifId) {
		jobPortalServiceImpl.deleteShiftDetail(shifId);

	}

	@RequestMapping(value = "/uploadFileForNewJoinee", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody void uploadFileForNewJoinee(
			MultipartHttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		HttpHeaders headers = httpServletRequest.getRequestHeaders();

		Iterator<String> itr = httpServletRequest.getFileNames();

		if (itr.hasNext()) {
			String file = itr.next();

			MultipartFile mpf = httpServletRequest.getFile(file);

			// request.getParameter("empId")
			jobPortalServiceImpl.uploadFileForNewJoinee(mpf,
					httpServletRequest.getParameter("candidateId"));
		}
		// jobPortalServiceImpl.uploadFileForNewJoinee(JoineeDTO);
	}

	@RequestMapping(value = "/downloadAttachment", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadAttachment(HttpServletResponse response,
			String fileName) {
		System.out.println("downloaded ..........");
		jobPortalServiceImpl.downloadAttachment(response, fileName);
	}

	// uploading candidates excel sheet
	@RequestMapping(value = "/uploadBulkOfCandidates", method = RequestMethod.POST)
	@Transactional
	public @ResponseBody List<Map<String, String>> uploadBulkOfCandidates(
			MultipartHttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		HttpHeaders headers = httpServletRequest.getRequestHeaders();
		MultipartFile mpf = httpServletRequest.getFile("file");
		return jobPortalServiceImpl.uploadBulkOfCandidates(mpf);
	}

	// here is the HR Employees LookUp
	@RequestMapping(value = "/getAllHrData", method = RequestMethod.GET)
	public @ResponseBody List<EmployeeDTO> getAllHrData() {

		return jobPortalServiceImpl.getAllHrData();
	}

	// counting number of candidates status
	@RequestMapping(value = "/getCandidatesListCount", params = {
			"selectionStatus", "fromDate", "toDate" }, method = RequestMethod.GET)
	public @ResponseBody List<Object> getCandidatesListCount(
			@RequestParam String selectionStatus,
			@RequestParam String fromDate, @RequestParam String toDate) {

		return jobPortalServiceImpl.getCandidatesListCount(selectionStatus,
				fromDate, toDate);
	}

	// gettting employee reviews
	@RequestMapping(value = "/getLoggedInEmployeeReviews", params = { "employeeId" }, method = RequestMethod.GET)
	public @ResponseBody List<AppraisalFormListDto> getLoggedInEmployeereviews(
			@RequestParam Long employeeId) {
		return jobPortalServiceImpl.getLoggedInEmployeeReviews(employeeId);
	}

	@RequestMapping(value = "/exportInterviewStatusReport", params = {
			"candidateStatus", "fromDate", "searchByCandidateName",
			"searchByMultipleFlag", "searchByRecruiterName",
			"searchBySourceName", "selectionStatus", "selectionTechnology",
			"toDate" }, method = RequestMethod.GET)
	public @ResponseBody void exportInterviewStatusReport(
			@RequestParam String candidateStatus,
			@RequestParam String fromDate,
			@RequestParam String searchByCandidateName,
			@RequestParam String searchByMultipleFlag,
			@RequestParam String searchByRecruiterName,
			@RequestParam String searchBySourceName,
			@RequestParam String selectionStatus,
			@RequestParam String selectionTechnology,
			@RequestParam String toDate, HttpServletResponse response)
			throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"InterviewStatusReport.csv\"");
		ByteArrayOutputStream os = jobPortalServiceImpl
				.exportInterviewStatusReport(candidateStatus, fromDate,
						searchByCandidateName, searchByMultipleFlag,
						searchByRecruiterName, searchBySourceName,
						selectionStatus, selectionTechnology, toDate);
		if(response!=null) {
		response.getOutputStream().write(os.toByteArray());
		}

	}

	@RequestMapping(value = "/getAllCompanies", params = { "startIndex",
			"endIndex", "searchCompany", "selectionTechnology" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllCompanies(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String searchCompany,
			@RequestParam String selectionTechnology) {
		return jobPortalServiceImpl.getAllCompanies(startIndex, endIndex,
				searchCompany, selectionTechnology);
	}

	@RequestMapping(value = "/getAllCompaniesData", method = RequestMethod.GET)
	@ResponseBody
	public List<CompanyDTO> getAllCompaniesData() {
		return jobPortalServiceImpl.getAllCompaniesData();
	}

	@RequestMapping(value = "/getAllCandidatesInfo", params = { "startIndex",
			"endIndex","companyName"}, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object>  getAllCandidatesInfo(@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String companyName, HttpServletResponse httpServletResponse) {
		return jobPortalServiceImpl.getAllCandidatesInfo(startIndex, endIndex,companyName);
	}

	@RequestMapping(value = "/getAllEmployeesInfo", params = { "startIndex",
			"endIndex","companyName"}, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllEmployeesInfo(@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String companyName,HttpServletResponse httpServletResponse) {
		return jobPortalServiceImpl.getAllEmployeesInfo(startIndex, endIndex,companyName);
	}

	@RequestMapping(value = "/exportCompaniesList", params = { "companySearch",
			"selectionTechnology" }, method = RequestMethod.GET)
	public @ResponseBody void exportCompaniesList(
			@RequestParam String companySearch,
			@RequestParam String selectionTechnology,
			HttpServletResponse httpServletResponse) throws Exception {
		
		//logger.warn("in controller");
		
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"CompaniesList.csv\"");
		
		ByteArrayOutputStream os = jobPortalServiceImpl.exportCompaniesList(
				companySearch, selectionTechnology);
		
		httpServletResponse.getOutputStream().write(os.toByteArray());
		

	}

	
	@RequestMapping(value ="/exportInterviewerDetails", params = {
			"fromDate","toDate","selectionStatus"  } ,method =RequestMethod.GET)
	public @ResponseBody void exportInterviewerDetails(
			 @RequestParam String fromDate ,
			 @RequestParam String toDate ,
			 @RequestParam String selectionStatus ,
			
			 HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment;filename=\"InterviewerDetails.csv\"");
		
		   ByteArrayOutputStream os = jobPortalServiceImpl.
				   exportInterviewerDetails(fromDate,toDate,selectionStatus);
		if(response!=null) {
		response.getOutputStream().write(os.toByteArray());
			
		}
	}
	
	@RequestMapping(value = "/downloadScheduleCandidates", params = {
			"fromDate", "toDate","skill"}, method = RequestMethod.GET)
	public @ResponseBody void downloadScheduleCandidatesFile(
			 @RequestParam String skill,
			@RequestParam String fromDate, @RequestParam String toDate,HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment;filename=\"ScheduledCandidates.csv\"");
		System.out.println("tech :" + skill);
		ByteArrayOutputStream os = jobPortalServiceImpl.downloadScheduleCandidatesFile(
				fromDate, toDate,skill);
		if(response !=null) {
			response.getOutputStream().write(os.toByteArray());
		}

	}
	 
	
	@RequestMapping(value="/getStatusChart",params = { 
			"dateSelection" ,"from","to" } ,method= RequestMethod.GET)
	  public @ResponseBody StatusChart getStatusChart(
			  @RequestParam String dateSelection,
			  @RequestParam String from,
			  @RequestParam String to ) {
			  return jobPortalServiceImpl.getStatusChart(
					  dateSelection, from,to);
					  
	}
	@RequestMapping(value = "/getEmpCountries", method = RequestMethod.GET)
	public @ResponseBody List<CountryLookUpDTO> getEmpCountries() {

		return jobPortalServiceImpl.getEmpCountries();
	}
	@RequestMapping(value = "/getVendorDetails", method = RequestMethod.GET)
	public @ResponseBody List<VendorDto> getVendorDetails() {

		return jobPortalServiceImpl.getVendorDetails();
	}
	
	@RequestMapping(value = "/sendRejectedMessagetoCandidate", method = RequestMethod.POST)
	public @ResponseBody void sendRejectedMessagetoCandidate(
			@RequestParam Long candidateId)
	 {

		jobPortalServiceImpl.sendRejectedMessagetoCandidate(candidateId);

		

	}
	@RequestMapping(value = "/getGenders", method = RequestMethod.GET)
 	public @ResponseBody List<GenderDTO> getGenders() {

		return jobPortalServiceImpl.getGenders();
	}
	@RequestMapping(value = "/getBloodgroups", method = RequestMethod.GET)
 	public @ResponseBody List<BloodGroupDTO> getBloodgroups() {

		return jobPortalServiceImpl.getBloodgroups();
	}
    @RequestMapping(value = "/getMaritalStatus", method = RequestMethod.GET)
 	public @ResponseBody List<MaritalStatusDTO> getMaritalStatus() {

		return jobPortalServiceImpl.getMaritalStatus();
	}
    @RequestMapping(value = "/getRelations", method = RequestMethod.GET)
 	public @ResponseBody List<RelationsDTO> getRelations() {

		return jobPortalServiceImpl.getRelations();
	}
    @RequestMapping(value = "/getQualificationCatergory", method = RequestMethod.GET)
 	public @ResponseBody List<QualificationCategoryDTO> getQualificationCatergory() {

		return jobPortalServiceImpl.getQualificationCatergory();
	}
    @RequestMapping(value = "/getEmploymentType", method = RequestMethod.GET)
 	public @ResponseBody List<EmploymentTypeDTO> getEmploymentType() {

		return jobPortalServiceImpl.getEmploymentType();
	}
    @RequestMapping(value = "/getJobType", method = RequestMethod.GET)
 	public @ResponseBody List<JobTypeDTO> getJobType() {

		return jobPortalServiceImpl.getJobType();
	}

}
