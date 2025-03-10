package com.raybiztech.projectmanagement.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.exceptions.InvalidCycleRangeException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.itdeclaration.business.FinanceCycle;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.projectMetrics.dto.EffortVarianceDTO;
import com.raybiztech.projectMetrics.dto.ProjectSprintsDTO;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.Domain;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Platform;
import com.raybiztech.projectmanagement.business.ProjectCheckList;
import com.raybiztech.projectmanagement.business.ProjectRequest;
import com.raybiztech.projectmanagement.dto.AllocationEffortDto;
import com.raybiztech.projectmanagement.dto.ChangeRequestDTO;
import com.raybiztech.projectmanagement.dto.ClientDTO;
import com.raybiztech.projectmanagement.dto.CountryDTO;
import com.raybiztech.projectmanagement.dto.ManagerDTO;
import com.raybiztech.projectmanagement.dto.MilestoneDTO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.dto.ProjectInitiationChecklistDTO;
import com.raybiztech.projectmanagement.dto.ProjectNameDTO;
import com.raybiztech.projectmanagement.dto.ProjectNumbersDTO;
import com.raybiztech.projectmanagement.dto.ProjectProposalsDTO;
import com.raybiztech.projectmanagement.dto.ProjectRequestAuditDTO;
import com.raybiztech.projectmanagement.dto.ProjectRequestDTO;
import com.raybiztech.projectmanagement.dto.ProjectRequestMailDTO;
import com.raybiztech.projectmanagement.dto.ReportDTO;
import com.raybiztech.projectmanagement.dto.StatusReportDTO;
import com.raybiztech.projectmanagement.exception.NoCheckList;
import com.raybiztech.projectmanagement.exception.ProjectRequestIdAlreadyExistsException;
import com.raybiztech.projectmanagement.exceptions.DuplicateClientCodeException;
import com.raybiztech.projectmanagement.exceptions.DuplicateClientException;
import com.raybiztech.projectmanagement.exceptions.DuplicateClientOrganizationException;
import com.raybiztech.projectmanagement.exceptions.DuplicateProjectException;
import com.raybiztech.projectmanagement.exceptions.HiveProjectNameNotExistException;
import com.raybiztech.projectmanagement.exceptions.ProjectNameNotExistException;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceQueryDTO;
import com.raybiztech.projectmanagement.invoice.lookup.Currency;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.separation.builder.SeparationBuilder;

@Controller
@RequestMapping("/project-mgmt")
public class ProjectController {

	@Autowired
	ProjectService projectService;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	SeparationBuilder separationBuilder;

	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public @ResponseBody List<ProjectDTO> projects() {
		return projectService.getAllProjects();
	}

	@RequestMapping(value = "/project", method = RequestMethod.POST)
	public @ResponseBody void addProject(@RequestBody ProjectDTO projectDTO,
			HttpServletResponse httpServletResponse) {
		try {
			projectService.addProject(projectDTO);
		} catch (DuplicateProjectException e) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);

		}
	}

	@RequestMapping(value = "/projectRequest", method = RequestMethod.POST)
	public @ResponseBody void projectRequest(
			@RequestBody ProjectRequestDTO projectRequestDTO,
			HttpServletResponse httpServletResponse) {

		try {
			projectService.addProjectRequest(projectRequestDTO);
		} catch (DuplicateProjectException e) {
			httpServletResponse
					.setStatus(httpServletResponse.SC_SERVICE_UNAVAILABLE);
		}

	}

	/*
	 * @RequestMapping(value = "/getAllProjectRequestList", method =
	 * RequestMethod.GET) public @ResponseBody List<ProjectRequestDTO>
	 * getAllProjectRequestList() { return
	 * projectService.getAllProjectRequestList(); }
	 */

	@RequestMapping(value = "/getAllProjectRequestList", params = {
			"firstIndex", "endIndex", "multiSearch" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllProjectRequestList(
			@RequestParam Integer firstIndex, @RequestParam Integer endIndex,
			@RequestParam String multiSearch) {
		return projectService.getAllProjectRequestList(firstIndex, endIndex,
				multiSearch);
	}

	// @RequestMapping(value = "/getMangerRequestProjects", method =
	// RequestMethod.GET)
	// public @ResponseBody List<ProjectRequestDTO> getMangerRequestProjects() {
	// Long loggdinEmpId = securityUtils
	// .getLoggedEmployeeIdforSecurityContextHolder();
	// return projectService.getManagerProjectRequests(loggdinEmpId);
	//
	// }

	// get project initiation checklist
	@RequestMapping(value = "/getCheckList", method = RequestMethod.GET)
	public @ResponseBody List<ProjectInitiationChecklistDTO> getCheckList() {
		return projectService.getInitiationCheckList();
	}

	// checking client organization is unique or not

	@RequestMapping(value = "/clientOrg", params = { "organization" }, method = RequestMethod.GET)
	public @ResponseBody Boolean checkClientOrg(
			@RequestParam String organization) {
		return projectService.checkClientOrg(organization);
	}

	@RequestMapping(value = "/rejectProjectRequest", params = { "requestId",
			"comment" }, method = RequestMethod.PUT)
	public @ResponseBody void rejectProjectRequest(
			@RequestParam Long requestId, @RequestParam String comment) {
		projectService.rejectProjectRequest(requestId, comment);
	}

	@RequestMapping(value = "/project", method = RequestMethod.PUT)
	public @ResponseBody ProjectDTO updateProject(
			@RequestBody ProjectDTO projectDTO) {
		return projectService.updateProject(projectDTO);
	}

	@RequestMapping(value = "/projectDelete", params = { "projectid" }, method = RequestMethod.GET)
	public @ResponseBody void deleteProject(@RequestParam Long projectid) {
		projectService.deleteProject(projectid);
	}

	@RequestMapping(value = "/deleteProjectRequest", params = { "id" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteProjectRequest(@RequestParam Long id,
			HttpServletResponse response) {
		try {
			projectService.deleteProjectRequest(id, response);
		} catch (ProjectRequestIdAlreadyExistsException pe) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
	}

	@RequestMapping(value = "/activeProjects", method = RequestMethod.GET)
	public @ResponseBody List<ProjectDTO> activeProjects() {
		return projectService.activeProjects();
	}

	@RequestMapping(value = "/deAllocateProject", method = RequestMethod.POST)
	public @ResponseBody void deAllocateProjectToEmployee(
			@RequestBody ReportDTO reportDTO) {
		projectService.deAllocateProjectToEmployee(reportDTO);
	}

	@RequestMapping(value = "/updateAllocateProject", method = RequestMethod.POST)
	public @ResponseBody void updateAllocateProject(@RequestBody ReportDTO dto) {
		projectService.updateAllocateProject(dto);
	}

	@RequestMapping(value = "/project", params = { "projectid" }, method = RequestMethod.GET)
	public @ResponseBody ProjectDTO getproject(Long projectid,
			HttpServletResponse httpServletResponse) {
		return projectService.getproject(projectid);
	}

	@RequestMapping(value = "/getProjectRequest", params = { "id" }, method = RequestMethod.GET)
	public @ResponseBody ProjectRequestDTO getProjectRequest(Long id) {
		return projectService.getProjectRequest(id);

	}

	@RequestMapping(value = "/getAllocateProject/{employeid}/{projectid}", method = RequestMethod.GET)
	public @ResponseBody ReportDTO getAllocateProject(
			@PathVariable("employeid") Long employeeid,
			@PathVariable("projectid") Long projectid) {
		return projectService.getAllocateProject(employeeid, projectid);
	}

	@RequestMapping(value = "/getAllManagers", method = RequestMethod.GET)
	public @ResponseBody List<ManagerDTO> getAllManagers() {
		return projectService.getAllManagers();
	}

	@RequestMapping(value = "/activeProjectsbyname", method = RequestMethod.GET)
	public @ResponseBody List<ProjectNameDTO> activeProjectsByProjectName() {
		return projectService.activeProjectsByProjectName();
	}

	@RequestMapping(value = "/activeProjectsForEmployee", params = {
			"projectStatus", "type", "health", "firstIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> activeProjectsForEmployee(
			@RequestParam String projectStatus, @RequestParam String type,
			@RequestParam String health, @RequestParam Integer firstIndex,
			@RequestParam Integer endIndex) {

		return projectService.activeProjectsForEmployee(
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder(),
				projectStatus, type, health, firstIndex, endIndex);

	}
	
	@RequestMapping(value ="/getEmployeeProjectslist", params ={
			"employeeid","projectStatus", "type", "firstIndex", "endIndex" 
	},method =RequestMethod.GET)
	public @ResponseBody Map<String, Object> getEmployeeProjectslist(@RequestParam Long employeeid, 
			@RequestParam String projectStatus, @RequestParam String type,
			 @RequestParam Integer firstIndex,
			@RequestParam Integer endIndex){
		return projectService.getEmployeeProjectslist(employeeid,
				projectStatus, type, firstIndex, endIndex);
	}

	@RequestMapping(value = "/projectsForMyProfile", params = {
			"projectStatus", "type", "firstIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> projectsForMyProfile(

	@RequestParam String projectStatus, @RequestParam String type,
			@RequestParam Integer firstIndex, @RequestParam Integer endIndex) {
		Long employeeid = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		return projectService.projectsForMyProfile(employeeid, projectStatus,
				type, firstIndex, endIndex);

	}

	@RequestMapping(value = "/projectUnderEmployees/", params = { "employeeid",
			"isBillale", "isAllocated", "startdate", "enddate" }, method = RequestMethod.GET)
	public @ResponseBody List<ProjectDTO> getAllProjects_UnderEmployee(
			@RequestParam Long employeeid, @RequestParam String isBillale,
			@RequestParam String isAllocated, @RequestParam String startdate,
			@RequestParam String enddate, @RequestParam String dateSelection) {
		// String startdate="01/01/2012";
		// String enddate="21/11/2015";

		return projectService.getAllProjects_UnderEmployee(employeeid,
				isBillale, isAllocated, startdate, enddate, dateSelection);

	}

	@RequestMapping(value = "/searchByEmployeeName", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchByEmployeeName(
			@RequestParam Integer firstIndex, @RequestParam Integer endIndex,
			@RequestParam String employeeName,
			@RequestParam String Billingtype,
			@RequestParam String EmployeeStatus,
			@RequestParam String technology, @RequestParam String startdate,
			@RequestParam String enddate,
			@RequestParam List<String> departmentNames,
			@RequestParam String dateSelection) {

		Long loggedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		return projectService.searchByEmployeeName(loggedInEmpId, firstIndex,
				endIndex, employeeName, technology, Billingtype,
				EmployeeStatus, startdate, enddate, departmentNames,
				dateSelection);

	}

	@RequestMapping(value = "/searchByEmployeeId/{employeeId}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchByEmployeeId(
			@PathVariable("employeeId") Long employeeId) {
		return projectService.searchByEmployeeId(employeeId);
	}

	@RequestMapping(value = "/EmployeesIndexData", params = { "startIndex",
			"endIndex", "selectionStatus" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getProfilePaginationEmployeesData(
			@RequestParam int startIndex, @RequestParam int endIndex,
			@RequestParam String selectionStatus) {

		Long loggedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		return projectService.getProfilePaginationEmployeesData(loggedInEmpId,
				startIndex, endIndex, selectionStatus);
	}

	@RequestMapping(value = "/projects", params = { "projectStatus" }, method = RequestMethod.GET)
	public @ResponseBody List<ProjectDTO> projects(
			@RequestParam String projectStatus) {
		return projectService.getAllProjects(projectStatus);
	}

	@RequestMapping(value = "/searchAllocationReport", params = { "employeeId",
			"projectStatus", "health", "startdate", "enddate", "multiSearch",
			"firstIndex", "endIndex", "projectDatePeriod", "intrnalOrNot" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchAllocationReportData(
			@RequestParam Long employeeId, @RequestParam String projectStatus,
			@RequestParam String type, @RequestParam String health,
			@RequestParam String startdate, @RequestParam String enddate,
			@RequestParam String multiSearch, @RequestParam Integer firstIndex,
			@RequestParam Integer endIndex,
			@RequestParam String projectDatePeriod, Boolean intrnalOrNot) {
		return projectService.searchAllocationReportData(employeeId,
				projectStatus, type, health, firstIndex, endIndex, startdate,
				enddate, multiSearch, projectDatePeriod, intrnalOrNot);
	}

	@RequestMapping(value = "/exportProjectList", params = { "employeeId",
			"projectStatus", "health", "startdate", "enddate", "multiSearch",
			"projectDatePeriod", "intrnalOrNot" }, method = RequestMethod.GET)
	public @ResponseBody void exportProjectList(@RequestParam Long employeeId,
			@RequestParam String projectStatus, @RequestParam String type,
			@RequestParam String health, @RequestParam String startdate,
			@RequestParam String enddate, @RequestParam String multiSearch,
			@RequestParam String projectDatePeriod, Boolean intrnalOrNot,
			HttpServletResponse httpServletResponse) throws Exception {

		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"ProjectList.csv\"");

		ByteArrayOutputStream os = projectService.exportProjectList(employeeId,
				projectStatus, type, health, startdate, enddate, multiSearch,
				projectDatePeriod, intrnalOrNot);

		httpServletResponse.getOutputStream().write(os.toByteArray());
	}

	@RequestMapping(value = "/updateEmployeeAllocateProject", method = RequestMethod.POST)
	public @ResponseBody void updateEmployeeAllocateProject(
			@RequestBody ProjectDTO dto) {
		projectService.updateEmployeeAllocateProject(dto);
	}

	@RequestMapping(value = "/mileStones", method = RequestMethod.POST)
	public @ResponseBody void addMileStone(@RequestBody MilestoneDTO dto,
			HttpServletResponse httpServletResponse) {
		projectService.addMileStone(dto);
	}

	// changed type of getAllMilestonePercentageOFProject method from Integer to Double due to
		// milestone percentage field allows decimal values.
		
	@RequestMapping(value = "/mileStones", method = RequestMethod.GET)
	public @ResponseBody Double getAllMilestonePercentageOFProject(
			@RequestParam Long projectId) {
		return projectService.getAllMilestonePercentageCount(projectId);
	}
	
	// changed type of getRaisedCRPercentage method from Integer to Double due to
			// milestone percentage field allows decimal values.
			

	@RequestMapping(value = "/getRaisedCRPercentage", method = RequestMethod.GET)
	public @ResponseBody Double getRaisedCRPercentage(@RequestParam Long crId) {
		return projectService.getRaisedCRPercentage(crId);
	}

	@RequestMapping(value = "/mileStonesList", params = { "projectId",
			"firstIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllMilestones_UnderProject(
			@RequestParam Long projectId, @RequestParam Integer firstIndex,
			@RequestParam Integer endIndex,
			HttpServletResponse httpServletResponse) {
		return projectService.getAllMilestones_UnderProject(projectId,
				firstIndex, endIndex);
	}

	@RequestMapping(value = "/getMilestone", params = { "milestoneId" }, method = RequestMethod.GET)
	public @ResponseBody MilestoneDTO getMilestone(
			@RequestParam Long milestoneId,
			HttpServletResponse httpServletResponse) {
		return projectService.getMilestone(milestoneId);
	}

	@RequestMapping(value = "/getMilestoneNumber", params = { "projectId" }, method = RequestMethod.GET)
	public @ResponseBody String getMilestoneNumber(@RequestParam Long projectId) {
		return projectService.getNextMilestoneNumber(projectId);
	}

	@RequestMapping(value = "/updateMileStone", method = RequestMethod.POST)
	public @ResponseBody void updateMileStone(@RequestBody MilestoneDTO dto,
			HttpServletResponse httpServletResponse) {
		projectService.updateMileStone(dto);
	}

	@RequestMapping(value = "/milestoneDelete", params = { "milestoneId" }, method = RequestMethod.GET)
	public @ResponseBody void deleteMilestone(@RequestParam Long milestoneId,
			HttpServletResponse httpServletResponse) {
		projectService.deleteMilestone(milestoneId);
	}

	@RequestMapping(value = "/statusReports", method = RequestMethod.POST)
	public @ResponseBody void addStatusReport(@RequestBody StatusReportDTO dto,
			HttpServletResponse httpServletResponse) {
		projectService.addStatusReport(dto);
	}

	@RequestMapping(value = "/updateStatusreport", method = RequestMethod.POST)
	public @ResponseBody void updateStatusReport(
			@RequestBody StatusReportDTO dto,
			HttpServletResponse httpServletResponse) {
		projectService.updateStatusReport(dto);
	}

	@RequestMapping(value = "/statusReportDelete", params = { "statusReportId" }, method = RequestMethod.GET)
	public @ResponseBody void deleteStatusReport(
			@RequestParam Long statusReportId,
			HttpServletResponse httpServletResponse) {
		projectService.deleteStatusReport(statusReportId);
	}

	@RequestMapping(value = "/statusReportList", params = { "projectId",
			"firstIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllStatusReports_UnderProject(
			@RequestParam Long projectId, @RequestParam Integer firstIndex,
			@RequestParam Integer endIndex) {
		return projectService.getAllStatusReports_UnderProject(projectId,
				firstIndex, endIndex);
	}

	@RequestMapping(value = "/client", method = RequestMethod.POST)
	public @ResponseBody void client(@RequestBody ClientDTO clientDTO,
			HttpServletResponse httpServletResponse) {
		try {
			projectService.addClient(clientDTO);
		} catch (DuplicateClientException duplicateClientException) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);

		} catch (DuplicateClientOrganizationException duplicateClientException) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);

		} catch (DuplicateClientCodeException duplicateClientCodeException) {
			httpServletResponse
					.setStatus((HttpServletResponse.SC_NOT_ACCEPTABLE));
		}

	}

	@RequestMapping(value = "/client", params = { "startIndex", "endIndex","selectionStatus"}, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> client(@RequestParam Integer startIndex,
			@RequestParam Integer endIndex,@RequestParam String selectionStatus) {
		return projectService.getAllClients(startIndex, endIndex,selectionStatus);
	}

	@RequestMapping(value = "/getClients", method = RequestMethod.GET)
	@ResponseBody
	public List<ClientDTO> getClients() {
		return projectService.getclients();
	}

	@RequestMapping(value = "/deleteClient", params = { "clientId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteClient(@RequestParam Long clientId) {
		projectService.deleteClient(clientId);
	}

	@RequestMapping(value = "/client/{clientId}", method = RequestMethod.GET)
	public @ResponseBody ClientDTO getClient(@PathVariable Long clientId) {
		return projectService.getclient(clientId);
	}

	@RequestMapping(value = "/country", method = RequestMethod.GET)
	@ResponseBody
	public List<CountryDTO> countries() {
		return projectService.getCountries();
	}

	@RequestMapping(value = "/updateClient", method = RequestMethod.PUT)
	public @ResponseBody void updateClient(@RequestBody ClientDTO clientDTO) {
		projectService.updateClient(clientDTO);
	}

	@RequestMapping(value = "/searchClients", params = { "search",
			"startIndex", "endIndex","selectionStatus"}, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchClients(
			@RequestParam String search, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex,@RequestParam String selectionStatus) {
		return projectService.searchClients(search, startIndex, endIndex,selectionStatus);
	}

	@RequestMapping(value = "/closeMilestone", params = { "milestoneId" }, method = RequestMethod.GET)
	@ResponseBody
	public void closeMilestone(@RequestParam Long milestoneId,
			HttpServletResponse httpServletResponse) {
		projectService.closeMilestone(milestoneId);
	}

	// ================================================================================================
	@RequestMapping(value = "/billingReport", params = { "startIndex",
			"endIndex", "status" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getBillingReports(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String status) {
		return projectService.getBillingReports(startIndex, endIndex, status);
	}

	// ====================================================================================================

	@RequestMapping(value = "/billingReportonSearch", params = { "startIndex",
			"endIndex", "status", "from", "to", "search", "client",
			"dateSelection" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getBillingReportsOnSearch(
			@RequestParam Integer startIndex, Integer endIndex, String status,
			String from, String to, String search, String client,
			String dateSelection) {

		return projectService.getBillingReportsOnSearch(startIndex, endIndex,
				status, from, to, search, client, dateSelection);
	}

	@RequestMapping(value = "/projectsOfClients", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> projectsOfClients(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String client, @RequestParam String type) {
		return projectService.getProjectsOfClients(startIndex, endIndex,
				client, type);
	}

	@RequestMapping(value = "/closeProject", params = { "projectId" }, method = RequestMethod.GET)
	@ResponseBody
	public void closeProject(@RequestParam Long projectId) {
		projectService.closeProject(projectId);
	}

	@RequestMapping(value = "/exportFile", params = { "id", "startIndex",
			"endIndex", "empName", "technology", "isbillable", "isAllocated",
			"startdate", "lastdate", "departmentNames", "dateSelection" }, method = RequestMethod.GET)
	@Transactional
	public @ResponseBody void exportCsv(@RequestParam long id,
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String empName, @RequestParam String technology,
			@RequestParam String isbillable, @RequestParam String isAllocated,
			@RequestParam String startdate, @RequestParam String lastdate,
			@RequestParam List<String> departmentNames,
			@RequestParam String dateSelection, HttpServletResponse response)
			throws IOException

	{
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"AllocationList.csv\"");
		ByteArrayOutputStream os = projectService.exportCsv(id, response,
				startIndex, endIndex, empName, technology, isbillable,
				isAllocated, startdate, lastdate, departmentNames,
				dateSelection);
		response.getOutputStream().write(os.toByteArray());
	}

	@RequestMapping(value = "/getClientInfo/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ClientDTO getClientInfo(@PathVariable Long id) {

		return projectService.getClientInfo(id);
	}

	@RequestMapping(value = "/getCurrencyList", method = RequestMethod.GET)
	public @ResponseBody List<Currency> getCurrencyList() {
		return projectService.getCurrencyLookUp();
	}

	/*
	 * @RequestMapping(value = "/getProjectNumbers", params = { "projectid" },
	 * method = RequestMethod.GET) public @ResponseBody ProjectNumbersDTO
	 * getProjectNumbers(Long projectid) {
	 * 
	 * return projectService.getProjectNumbers(projectid); }
	 */

	@RequestMapping(value = "/getProjectNumbers", params = { "projectid" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getProjectNumbers(Long projectid) {
		return projectService.getProjectNumbers(projectid);
	}

	@RequestMapping(value = "/projectNumbers", method = RequestMethod.POST)
	public @ResponseBody void addProjectNumbers(
			@RequestBody ProjectNumbersDTO projectNumbersDTO) {
		projectService.addProjectNumbers(projectNumbersDTO);
	}

	@RequestMapping(value = "/updateProjectNumbers", method = RequestMethod.PUT)
	public @ResponseBody void updateProjectNumbers(
			@RequestBody ProjectNumbersDTO projectNumbersDTO,
			HttpServletResponse httpServletResponse) {
		try {
			projectService.updateProjectNumbers(projectNumbersDTO);
		} catch (Exception e) {
			httpServletResponse
					.setStatus(httpServletResponse.SC_NOT_ACCEPTABLE);
		}

	}

	@RequestMapping(value = "/updateCRNumbers", method = RequestMethod.PUT)
	public @ResponseBody void updateCRNumbers(
			@RequestBody ProjectNumbersDTO projectNumbersDTO,
			HttpServletResponse httpServletResponse) {

		try {
			projectService.updateCRNumbers(projectNumbersDTO);
		} catch (Exception e) {
			httpServletResponse
					.setStatus(httpServletResponse.SC_NOT_ACCEPTABLE);
		}

	}

	@RequestMapping(value = "/changeRequest", method = RequestMethod.POST)
	public @ResponseBody void AddCR(
			@RequestBody ChangeRequestDTO changeRequestDTO,
			HttpServletResponse httpServletResponse) {
		projectService.addCR(changeRequestDTO);
	}

	@RequestMapping(value = "/getCRList", params = { "projectid", "firstIndex",
			"endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getchangeRequestDTOList(
			@RequestParam Long projectid, @RequestParam Integer firstIndex,
			@RequestParam Integer endIndex,
			HttpServletResponse httpServletResponse) {

		return projectService.getChangeRequestDTOList(projectid, firstIndex,
				endIndex);
	}

	@RequestMapping(value = "/getCRLookupforNumbers", params = { "projectid" }, method = RequestMethod.GET)
	public @ResponseBody List<ChangeRequestDTO> getCRLookupforNumbers(
			Long projectid) {

		return projectService.getCRlookupForProjectNumbers(projectid);
	}

	@RequestMapping(value = "/deleteCR", params = { "crId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteCR(Long crId,
			HttpServletResponse httpServletResponse) {
		projectService.deleteCR(crId);
	}

	@RequestMapping(value = "/getCRListForMilestone", params = { "projectid" }, method = RequestMethod.GET)
	public @ResponseBody List<ChangeRequestDTO> getCRDtoListForMilestone(
			Long projectid) {
		return projectService.getCRListForMilestone(projectid);
	}

	@RequestMapping(value = "/updateChangeRequest", method = RequestMethod.PUT)
	public @ResponseBody void updateChangeRequest(
			@RequestBody ChangeRequestDTO changeRequestDTO,
			HttpServletResponse httpServletResponse) {
		projectService.updateChangeRequest(changeRequestDTO);

	}

	@RequestMapping(value = "/getProjectHistory", params = { "projectId" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getProjectHistory(@RequestParam Long projectId,
			HttpServletResponse httpServletResponse) {
		return projectService.getProjectHistory(projectId);
	}

	@RequestMapping(value = "/getWorkDetails", params = { "fromdate", "todate",
			"empId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getWorkDetails(String fromdate,
			String todate, Long empId) {

		return projectService.getWorkingDaysOfEmployeeForDetails(fromdate,
				todate, empId);
	}

	@RequestMapping(value = "/projectProposal", method = RequestMethod.POST)
	public @ResponseBody void addProjectProposal(
			@RequestBody ProjectProposalsDTO dto) {
		projectService.addProjectProposal(dto);
	}

	@RequestMapping(value = "/projectProposal", method = RequestMethod.GET)
	public @ResponseBody List<ProjectProposalsDTO> getProjectProposals(
			Long projectId) {
		return projectService.getProjectProposals(projectId);
	}

	@RequestMapping(value = "/reOpenMilestone", params = { "milestoneId" }, method = RequestMethod.GET)
	@ResponseBody
	public void reOpenMilestone(@RequestParam Long milestoneId) {
		projectService.reOpenMilestone(milestoneId);
	}

	@RequestMapping(value = "/projectRequestMailId", params = { "cc", "bcc" }, method = RequestMethod.POST)
	public @ResponseBody void projectRequestMailId(@RequestParam String cc,
			@RequestParam String bcc, HttpServletResponse httpServletResponse) {
		projectService.projectRequestMailId(cc, bcc);

	}

	@RequestMapping(value = "/getProjectRequestMailIds", method = RequestMethod.GET)
	@ResponseBody
	public ProjectRequestMailDTO getProjectRequestMailIds() {
		return projectService.getProjectRequestMailIds();
	}

	@RequestMapping(value = "/downloadInitationCheckList", params = { "projectRequestId" }, method = RequestMethod.GET)
	@ResponseBody
	public void downloadInitationCheckList(@RequestParam Long projectRequestId,
			HttpServletResponse httpServletResponse) throws IOException {
		try {
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=\"ProjectInitiationCheckList"
							+ projectRequestId + ".csv\"");

			httpServletResponse.setContentType("text/csv");

			ByteArrayOutputStream byteArrayOutputStream = projectService
					.downloadInitationCheckList(projectRequestId);

			httpServletResponse.getOutputStream().write(
					byteArrayOutputStream.toByteArray());
		} catch (NoCheckList checkList) {
			httpServletResponse.setStatus(httpServletResponse.SC_NO_CONTENT);
		}

	}

	@RequestMapping(value = "/getAuditForProjectRequest", params = { "projectRequestId" }, method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectRequestAuditDTO> getAuditForProjectRequest(
			@RequestParam Long projectRequestId) {

		return projectService.getAuditForProjectRequest(projectRequestId);

	}

	@RequestMapping(value = "/getAllPlatforms", method = RequestMethod.GET)
	@ResponseBody
	public List<Platform> getAllPlatforms() {
		return projectService.getAllPlatforms();
	}

	@RequestMapping(value = "/getAllDomains", method = RequestMethod.GET)
	@ResponseBody
	public List<Domain> getAllDomains() {
		return projectService.getAllDomains();
	}

	@RequestMapping(value = "/exportEmployeeAllocation", params = { "id",
			"startIndex", "endIndex", "empName", "technology", "isbillable",
			"isAllocated", "startdate", "lastdate", "departmentNames",
			"dateSelection" }, method = RequestMethod.GET)
	public @ResponseBody void exportEmployeeAllocation(@RequestParam long id,
			@RequestParam int startIndex, @RequestParam int endIndex,
			@RequestParam String empName, @RequestParam String technology,
			@RequestParam String isbillable, @RequestParam String isAllocated,
			@RequestParam String startdate, @RequestParam String lastdate,
			@RequestParam List<String> departmentNames,
			@RequestParam String dateSelection, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"EmployeeAllocationList.csv\"");

		ByteArrayOutputStream os = projectService.exportEmployeeAllocation(id,
				startIndex, endIndex, empName, technology, isbillable,
				isAllocated, startdate, lastdate, departmentNames,
				dateSelection);

		response.getOutputStream().write(os.toByteArray());
	}
	
	@RequestMapping(value = "/getNumbersAndPercentage", params = { "milestoneid","id"},method=RequestMethod.GET)
	public @ResponseBody Map<String ,Object> 
	getNumbersAndPercentage(@RequestParam Long milestoneid,@RequestParam String id) {
		//here we calling Service method to get the Numbers and percentage
		return projectService.getNumbersAndPercentage(milestoneid,id);
	}
	
	@RequestMapping(value ="/getNonClosedMilestones",params={"fromIndex","toIndex"},method=RequestMethod.GET)
	public @ResponseBody Map<String ,Object> getNonClosedMilestones(@RequestParam Integer fromIndex,@RequestParam Integer toIndex){
		return projectService.getNonClosedMilestones(fromIndex, toIndex);
		
	}
	
	
	
	@RequestMapping(value ="/projectsUnderClient", params ={"clientId"}, method = RequestMethod.GET)
	public @ResponseBody List<ProjectDTO> getAllActiveProjects_UnderClient(@RequestParam Long clientId){
	
		return projectService.getAllActiveProjects_UnderClient(clientId);
	}
	
	@RequestMapping(value = "/reOpenRaisedInvoiceMilestone", params = { "milestoneId" }, method = RequestMethod.GET)
	@ResponseBody
	public void reOpenRaisedInvoiceMilestone(@RequestParam Long milestoneId) {
		projectService.reOpenRaisedInvoiceMilestone(milestoneId);
	}
		
	@RequestMapping(value ="/getRaisedInvoices", params ={"milestoneId"}, method = RequestMethod.GET)
	public @ResponseBody List<InvoiceDTO> getRaisedInvoices(@RequestParam Long milestoneId){
	
		return projectService.getRaisedInvoices(milestoneId);
	}
	
	//getting milestones and cr List for project invoices
	@RequestMapping(value = "/getClosedMilestonesandCRs",params = { "projectId"},method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getClosedMilestonesandCRs(@RequestParam Long projectId)
	{
		return projectService.getClosedMilestonesandCRs(projectId);
	}
	
	
}
