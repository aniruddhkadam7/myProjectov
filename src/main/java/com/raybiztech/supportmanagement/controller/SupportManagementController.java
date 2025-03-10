package com.raybiztech.supportmanagement.controller;

import com.raybiztech.appraisalmanagement.dto.SearchQueryParamsInAppraisalForm;
import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.dto.EmpDepartmentDTO;
import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.recruitment.dto.DepartmentDTO;
import com.raybiztech.supportmanagement.dto.SupportTicketsDTO;
import com.raybiztech.supportmanagement.dto.TicketsCategoryDTO;
import com.raybiztech.supportmanagement.dto.TicketsSubCategoryDTO;
import com.raybiztech.supportmanagement.dto.TrackerDto;
import com.raybiztech.supportmanagement.service.SupportManagementService;
import com.raybiztech.supportmanagement.utility.EntityInUseException;
import com.raybiztech.ticketmanagement.exceptions.DateException;
import com.raybiztech.ticketmanagement.exceptions.DateTimeException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("supportManagementController")
@RequestMapping("/supportManagement")
public class SupportManagementController {

	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(SupportManagementController.class);

	@Autowired
	SupportManagementService supportManagementServiceImpl;

	// To get the category look up based on department selected
	@RequestMapping(value = "/departmentCategoryList", params = { "deptId" }, method = RequestMethod.GET)
	public @ResponseBody List<TicketsCategoryDTO> departmentCategoryList(@RequestParam("deptId") Long deptId) {
		return supportManagementServiceImpl.departmentCategoryList(deptId);
	}

	// To get the list of sub categories
	@RequestMapping(value = "/subCategoryList", params = { "categoryId" }, method = RequestMethod.GET)
	public @ResponseBody List<TicketsSubCategoryDTO> subCategoryList(@RequestParam("categoryId") Long categoryId) {
		return supportManagementServiceImpl.subCategoryList(categoryId);
	}

	// To store the newly created ticket
	@RequestMapping(value = "/createTickets", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> createTickets(@RequestBody SupportTicketsDTO supportTicketsDTO,
			HttpServletResponse httpServletResponse) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = supportManagementServiceImpl.createTickets(supportTicketsDTO);
		} catch (DateException de) {
			httpServletResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (DateTimeException dateTimeException) {
			httpServletResponse.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
		}
		return map;
	}

	// To add the categories to data base
	@RequestMapping(value = "/addCategory", method = RequestMethod.POST)
	public @ResponseBody void addCategory(@RequestBody TicketsCategoryDTO ticketsCategoryDTO) {
		supportManagementServiceImpl.addCategory(ticketsCategoryDTO);

	}

	// To get all Category List
	@RequestMapping(value = "/getAllCategoryList", method = RequestMethod.GET)
	public @ResponseBody List<TicketsCategoryDTO> getAllCategoryList() {
		return supportManagementServiceImpl.getAllCategoryList();
	}

	// To add the sub categories to data base
	@RequestMapping(value = "/addSubCategory", method = RequestMethod.POST)
	public @ResponseBody void addSubCategories(@RequestBody TicketsSubCategoryDTO ticketsSubCategoryDTO) {
		supportManagementServiceImpl.addSubCategory(ticketsSubCategoryDTO);

	}

	// To get the list of sub categories
	@RequestMapping(value = "/getSubCategoriesList", method = RequestMethod.GET)
	public @ResponseBody List<TicketsSubCategoryDTO> getAllSubCategories() {
		return supportManagementServiceImpl.getAllSubCategories();
	}

	// To delete Category
	@RequestMapping(value = "/deleteCategory", params = { "categoryId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteCategory(@RequestParam("categoryId") Long categoryId) {
		supportManagementServiceImpl.deleteCategory(categoryId);
	}

	// To Update Category in data base
	@RequestMapping(value = "/updateCategory", method = RequestMethod.PUT)
	public @ResponseBody void updateCategory(@RequestBody TicketsCategoryDTO ticketsCategoryDTO) {
		supportManagementServiceImpl.updateCategory(ticketsCategoryDTO);
	}

	// To Update Sub-Category in data base
	@RequestMapping(value = "/updateSubCategory", method = RequestMethod.PUT)
	public @ResponseBody void updateSubCategory(@RequestBody TicketsSubCategoryDTO ticketsSubCategoryDTO) {
		supportManagementServiceImpl.updateSubCategory(ticketsSubCategoryDTO);
	}

	// To get the tickets for approval
	@RequestMapping(value = "/getAllTicketsForApproval", params = { "ticketStatus", "categoryId", "subCategoryId",
			"startIndex", "endIndex", "progressStatus", "multiSearch", "searchByEmpName", "searchByAssigneeName",
			"from", "to", "dateSelection", "departmentId", "trackerID" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllTicketsForApproval(@RequestParam String ticketStatus,
			@RequestParam Long categoryId, @RequestParam Long subCategoryId, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex, @RequestParam String progressStatus, @RequestParam String multiSearch,
			@RequestParam String searchByEmpName, @RequestParam String searchByAssigneeName, @RequestParam String from,
			@RequestParam String to, @RequestParam String dateSelection, @RequestParam Long departmentId,
			@RequestParam Long trackerID) {

		return supportManagementServiceImpl.getAllTicketsForApproval(ticketStatus, categoryId, subCategoryId,
				startIndex, endIndex, progressStatus, multiSearch, searchByEmpName, searchByAssigneeName, from, to,
				dateSelection, departmentId, trackerID);

	}

	// To delete SubCategory
	@RequestMapping(value = "/deleteSubCategory", params = { "subCategoryId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteSubCategory(@RequestParam("subCategoryId") Long subCategoryId) {
		supportManagementServiceImpl.deleteSubCategory(subCategoryId);
	}

	// To get the individual tickets of employee
	@RequestMapping(value = "/getIndividualTickets", method = RequestMethod.GET)
	public @ResponseBody List<SupportTicketsDTO> getIndividualTickets() {
		/*
		 * Date parsedDate = null; try { if(date.equalsIgnoreCase("today")){ parsedDate
		 * = new Date(); }else if(date.equalsIgnoreCase("tomorrow")){ parsedDate = new
		 * Date().next(); }else { parsedDate = DateParser.toDate(date); } } catch
		 * (ParseException ex) { ex.printStackTrace(); }
		 */
		return supportManagementServiceImpl.getIndividualTickets();
	}

	@RequestMapping(value = "/getTicket", params = { "ticketId" }, method = RequestMethod.GET)
	public @ResponseBody SupportTicketsDTO getTiceket(Long ticketId, HttpServletResponse httpServletResponse)
			throws IOException {
		SupportTicketsDTO dto = supportManagementServiceImpl.getTicket(ticketId);
		if (dto != null) {
			return dto;
		} else {
			httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
			throw new UnauthorizedUserException();
		}
	}

	// To edit the individual tickets
	@RequestMapping(value = "/editIndividualTickets", method = RequestMethod.PUT)
	public @ResponseBody void editIndividualTickets(@RequestBody SupportTicketsDTO supportTicketsDTO,
			HttpServletResponse httpServletResponse) {
		supportManagementServiceImpl.editIndividualTickets(supportTicketsDTO);
	}

	// To get the Search Sub Category List
	@RequestMapping(value = "/getSearchSubCategoryList", params = { "startIndex",
			"endIndex" }, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getSearchSubCategoryList(
			@RequestBody TicketsSubCategoryDTO paramsOfSubCategory, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex) {
		return supportManagementServiceImpl.getSearchSubCategoryList(paramsOfSubCategory, startIndex, endIndex);
	}

	// To cancel raised ticket
	@RequestMapping(value = "/cancelTicketRequest", params = { "requestId" }, method = RequestMethod.PUT)
	public @ResponseBody void cancelTicketRequest(@RequestParam Long requestId,
			HttpServletResponse httpServletResponse) {
		supportManagementServiceImpl.cancelTicketRequest(requestId);
	}

	// download files
	@RequestMapping(value = "/downloadFile", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadFile(HttpServletResponse response, String fileName) {
		supportManagementServiceImpl.downloadFile(response, fileName);
	}

	// To get all the look ups for ticket approvals page
	@RequestMapping(value = "/getAllLookups", method = RequestMethod.GET)
	public @ResponseBody List<TicketsCategoryDTO> getAllLookups() {
		return supportManagementServiceImpl.getAllLookups();

	}

	@RequestMapping(value = "/searchTicketData", params = { "startIndex", "endIndex",
			"multiSearch" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchTicketData(@RequestParam Integer startIndex,
			@RequestParam Integer endIndex, @RequestParam String multiSearch) {
		return supportManagementServiceImpl.searchTicketData(startIndex, endIndex, multiSearch);
	}

	@RequestMapping(value = "/exportRaisedTickets", params = { "multiSearch" }, method = RequestMethod.GET)
	public @ResponseBody void exportRaisedTickets(@RequestParam String multiSearch, HttpServletResponse response)
			throws IOException {

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"TicketList.csv\"");
		ByteArrayOutputStream os = supportManagementServiceImpl.exportRaisedTickets(multiSearch);
		response.getOutputStream().write(os.toByteArray());

	}

	// To approve ticket by manager
	@RequestMapping(value = "/approveByManagerTicket", params = { "ticketId" }, method = RequestMethod.PUT)
	public @ResponseBody void approveByManagerTicket(@RequestParam Long ticketId,
			HttpServletResponse httpServletResponse) {
		try {
			supportManagementServiceImpl.approveByManagerTicket(ticketId);
		} catch (DateException de) {
			httpServletResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (DateTimeException dateTimeException) {
			httpServletResponse.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
		}
	}

	// To reject ticket by manager
	@RequestMapping(value = "/rejectManagerTicket", params = { "tktId" }, method = RequestMethod.PUT)
	public @ResponseBody void rejectManagerTicket(@RequestParam Long tktId) {
		supportManagementServiceImpl.rejectManagerTicket(tktId);
	}

	@RequestMapping(value = "/getAudit", params = { "id", "filterName" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAudit(@RequestParam Long id, @RequestParam String filterName,
			HttpServletResponse httpServletResponse) {
		return supportManagementServiceImpl.getAudit(id, filterName);
	}

	@RequestMapping(value = "/departmentWiseList", method = RequestMethod.GET)
	public @ResponseBody List<EmpDepartment> departmentWiseList() {
		return supportManagementServiceImpl.departmentWiseList();
	}

	@RequestMapping(value = "/exportTicketStatusList", params = { "ticketStatus", "categoryId", "subCategoryId",
			"startIndex", "endIndex", "progressStatus", "multiSearch", "searchByEmpName", "from", "to", "dateSelection",
			"searchByAssigneeName", "departmentId", "trackerID" }, method = RequestMethod.GET)
	public @ResponseBody void exportTicketStatusList(@RequestParam String ticketStatus, @RequestParam Long categoryId,
			@RequestParam Long subCategoryId, @RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String progressStatus, @RequestParam String multiSearch, @RequestParam String searchByEmpName,
			String searchByAssigneeName, @RequestParam String from, @RequestParam String to,
			@RequestParam String dateSelection, HttpServletResponse response, @RequestParam Long departmentId,
			@RequestParam Long trackerID) throws IOException {

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"TicketList.csv\"");
		ByteArrayOutputStream os = supportManagementServiceImpl.exportTicketStatusList(
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder(), ticketStatus, categoryId, subCategoryId,
				startIndex, endIndex, progressStatus, multiSearch, searchByEmpName, searchByAssigneeName, from, to,
				dateSelection, response, departmentId, trackerID);
		response.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = "/updateHierarchyReportingManager", params = { "oldmanager", "employee",
			"newmanager" }, method = RequestMethod.PUT)
	public @ResponseBody void updateHierarchyReportingManager(@RequestParam String oldmanager,
			@RequestParam String employee, @RequestParam String newmanager) {
		supportManagementServiceImpl.updateHierarchyReportingManager(oldmanager, employee, newmanager);
	}

	@RequestMapping(value = "/getDepartmentNameList", method = RequestMethod.GET)
	public @ResponseBody List<EmpDepartmentDTO> getDepartmentNameList() {

		return supportManagementServiceImpl.getDepartmentNameList();
	}

	@RequestMapping(value = "/getDepartmentListForRaise", method = RequestMethod.GET)
	public @ResponseBody List<EmpDepartmentDTO> getDepartmentListForRaise() {

		return supportManagementServiceImpl.getDepartmentListForRaise();
	}

	@RequestMapping(value = "/addTracker", method = RequestMethod.POST)
	public @ResponseBody List<TrackerDto> addTracker(@RequestBody TrackerDto trackerDto) {
		return supportManagementServiceImpl.addTracker(trackerDto);

	}

	@RequestMapping(value = "/deleteTracker", params = { "id" }, method = RequestMethod.DELETE)
	public @ResponseBody List<TrackerDto> deleteTracker(@RequestParam("id") Long id,
			HttpServletResponse httpServletResponse) throws IOException {
		List<TrackerDto> returnresult = new ArrayList<TrackerDto>();
		try {
			returnresult = supportManagementServiceImpl.deleteTracker(id);
		} catch (EntityInUseException exception) {
			httpServletResponse.sendError(httpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		return returnresult;
	}

	@RequestMapping(value = "/getAllTracker", method = RequestMethod.GET)
	public @ResponseBody List<TrackerDto> getAllTracker() {
		return supportManagementServiceImpl.getAllTracker();
	}

	@RequestMapping(value = "/exportTicketApprovalList", params = { "ticketStatus", "categoryId", "subCategoryId",
			"startIndex", "endIndex", "progressStatus", "multiSearch", "searchByEmpName", "from", "to", "dateSelection",
			"searchByAssigneeName", "departmentId", "trackerID" }, method = RequestMethod.GET)
	public @ResponseBody void exportTicketApprovalList(@RequestParam String ticketStatus, @RequestParam Long categoryId,
			@RequestParam Long subCategoryId, @RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String progressStatus, @RequestParam String multiSearch, @RequestParam String searchByEmpName,
			String searchByAssigneeName, @RequestParam String from, @RequestParam String to,
			@RequestParam String dateSelection, HttpServletResponse response, @RequestParam Long departmentId,
			@RequestParam Long trackerID) throws IOException {

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"TicketList.csv\"");
		ByteArrayOutputStream os = supportManagementServiceImpl.exportTicketApprovalList(
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder(), ticketStatus, categoryId, subCategoryId,
				startIndex, endIndex, progressStatus, multiSearch, searchByEmpName, searchByAssigneeName, from, to,
				dateSelection, response, departmentId, trackerID);
		response.getOutputStream().write(os.toByteArray());

	}

	// To get the tickets report
	@RequestMapping(value = "/getTicketsReport", params = { "ticketStatus", "from", "to", "dateSelection",
			"departmentId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getTicketsReport(@RequestParam String ticketStatus,
			@RequestParam String from, @RequestParam String to, @RequestParam String dateSelection,
			@RequestParam Long departmentId) {

		return supportManagementServiceImpl.getTicketsReport(ticketStatus, from, to, dateSelection, departmentId);

	}

	// To get the tickets list according to status

	@RequestMapping(value = "/getTicketsDetails", params = { "filter", "trackerId", "startIndex", "endIndex",
			"categoryId", "subCategoryId", "from", "to", "dateSelection", "departmentId",
			"ticketStatus" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getTicketsDetails(@RequestParam String filter,
			@RequestParam Long trackerId, @RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam Long categoryId, @RequestParam Long subCategoryId, @RequestParam String from,
			@RequestParam String to, @RequestParam String dateSelection, @RequestParam Long departmentId,
			@RequestParam String ticketStatus) {

		return supportManagementServiceImpl.getTicketsDetails(filter, trackerId, startIndex, endIndex, categoryId,
				subCategoryId, from, to, dateSelection, departmentId, ticketStatus);

	}

	@RequestMapping(value = "/exportReportList", params = { "departmentId", "dateSelection", "from",
			"to" }, method = RequestMethod.GET)
	@ResponseBody
	public void exportReportList(@RequestParam Long departmentId, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex, @RequestParam String from, @RequestParam String to,
			@RequestParam String dateSelection,

			HttpServletResponse httpServletResponse) throws IOException {

		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"SupportReport.csv\"");
		ByteArrayOutputStream os = supportManagementServiceImpl.exportReportList(startIndex, endIndex, from, to,
				dateSelection, departmentId);

		httpServletResponse.getOutputStream().write(os.toByteArray());

	}
}
