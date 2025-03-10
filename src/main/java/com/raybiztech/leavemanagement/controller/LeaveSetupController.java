package com.raybiztech.leavemanagement.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.leavemanagement.dto.LeaveCategoryDTO;
import com.raybiztech.leavemanagement.dto.LeaveSettingsDTO;
import com.raybiztech.leavemanagement.exceptions.CannotDeleteLeaveCategoryException;
import com.raybiztech.leavemanagement.exceptions.LeaveCategoryAlreadyCreatedException;
import com.raybiztech.leavemanagement.service.LeaveSetupService;

@Controller
@RequestMapping("/leaveSetup")
public class LeaveSetupController {

	@Autowired
	LeaveSetupService leaveSetupService;

	Logger logger = Logger.getLogger(LeaveSetupController.class);

	@RequestMapping(value = "/leaveCategoriesForAdmin", method = RequestMethod.GET)
	public @ResponseBody SortedSet<LeaveCategoryDTO> leaveCategories(
			HttpServletResponse value) {

		return leaveSetupService.getAllLeaveCategories();

	}

	@RequestMapping(value = "/lopCategories", method = RequestMethod.GET)
	public @ResponseBody SortedSet<LeaveCategoryDTO> lopCategories() {

		return leaveSetupService.getlopCategories();

	}

	@RequestMapping(value = "/bioAdmin/leaveSummaries", params = {
			"financialYear", "startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> leaveSummaries(
			@RequestParam Integer financialYear,
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			HttpServletResponse value) {

		return leaveSetupService.getAllEmployeesLeaveSummary(financialYear,
				startIndex, endIndex);

	}

	@RequestMapping(value = "/bioAdmin/leaveCategory", method = RequestMethod.POST)
	public @ResponseBody void leaveCategory(
			@RequestBody LeaveCategoryDTO leaveCategoryDTO)
			throws LeaveCategoryAlreadyCreatedException {
		leaveSetupService.addLeaveCategory(leaveCategoryDTO);
	}

	@RequestMapping(value = "/bioAdmin/leaveCalendarSettings", method = RequestMethod.POST)
	public @ResponseBody void leaveCalendarSettings(
			@RequestBody LeaveSettingsDTO leaveSettingsDTO) {
		leaveSetupService.updateLeaveCalendarSettings(leaveSettingsDTO);
	}

	@RequestMapping(value = "/bioAdmin/leaveCategory", params = { "leaveCategoryId" }, method = RequestMethod.DELETE)
	public @ResponseBody void leaveCategory(@RequestParam Long leaveCategoryId)
			throws CannotDeleteLeaveCategoryException {
		leaveSetupService.deleteLeaveCategory(leaveCategoryId);
	}

	@RequestMapping(value = "/bioAdmin/leaveCategory", method = RequestMethod.PUT)
	public @ResponseBody void editLeaveCategory(
			@RequestBody LeaveCategoryDTO leaveCategoryDTO) {
		leaveSetupService.editLeaveCategory(leaveCategoryDTO);
	}

	@RequestMapping(value = "/bioAdminManager/leaveCalendarSettings", method = RequestMethod.GET)
	public @ResponseBody LeaveSettingsDTO leaveCalendarSettings(
			HttpServletResponse value) {
		return leaveSetupService.getLeaveCalendarSetting();
	}

	@RequestMapping(value = "/creditedYears", method = RequestMethod.GET)
	public @ResponseBody List<Integer> creditedYears() {

		return leaveSetupService.getAllCreditedYears();

	}

	@RequestMapping(value = "/employees", method = RequestMethod.GET)
	public @ResponseBody Set<EmployeeDTO> employees() {

		return leaveSetupService.getAllEmployeesOfCompany();

	}

	@RequestMapping(value = "/getLeaveCategory", params = { "leaveCategoryId" }, method = RequestMethod.GET)
	public @ResponseBody LeaveCategoryDTO getLeaveCategory(
			@RequestParam Long leaveCategoryId) {

		return leaveSetupService.getleaveCategory(leaveCategoryId);

	}

	@RequestMapping(value = "/financialYear", method = RequestMethod.GET)
	public @ResponseBody Integer financialYear() {

		return leaveSetupService.getCurrentFinancialYear();

	}

	@RequestMapping(value = "/searchLeaveSummaries", params = {
			"financialYear", "startIndex", "endIndex", "search" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchLeaveSummaries(
			@RequestParam Integer financialYear,
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String search) {

		return leaveSetupService.searchLeaveSummaries(financialYear, search,
				startIndex, endIndex);

	}

	@RequestMapping(value = "/leaveCategories", params = { "employeeId" }, method = RequestMethod.GET)
	public @ResponseBody SortedSet<LeaveCategoryDTO> leaveCategories(
			@RequestParam Long employeeId,HttpServletResponse httpServletResponse) {
		return leaveSetupService.getEligibleLeaveCategories(employeeId);

	}
	
	@RequestMapping(value = "/exportLeaveReport", params = {
			"financialYear", "startIndex", "endIndex", "search" }, method = RequestMethod.GET)
	public @ResponseBody void exportLeavereport(@RequestParam Integer financialYear,
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String search,HttpServletResponse response)throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"LeaveReport.csv\"");
		ByteArrayOutputStream os = leaveSetupService.exportLeavereport(financialYear,startIndex,endIndex,
				search, response);
		response.getOutputStream().write(os.toByteArray());
		
	}

}
