package com.raybiztech.leavemanagement.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.dto.LeaveApplicationDTO;
import com.raybiztech.leavemanagement.dto.LeaveSummaryDTO;
import com.raybiztech.leavemanagement.dto.SearchCriteriaDTO;
import com.raybiztech.leavemanagement.exceptions.LeaveAlreadyAppliedException;
import com.raybiztech.leavemanagement.exceptions.LeaveNotFoundException;
import com.raybiztech.leavemanagement.exceptions.NotEnoughLeavesAvaialableException;
import com.raybiztech.leavemanagement.exceptions.NotWorkingDayException;
import com.raybiztech.leavemanagement.exceptions.ProbationException;
import com.raybiztech.leavemanagement.exceptions.UnderNoticeException;
import com.raybiztech.leavemanagement.service.LeaveService;

@Controller
@RequestMapping("/leave")
public class LeaveController {

	@Autowired
	LeaveService leaveService;
	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(LeaveController.class);

	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public @ResponseBody void apply(@RequestBody LeaveApplicationDTO leaveDTO,
			HttpServletResponse httpServletResponse) {
		try {
			leaveService.applyLeave(leaveDTO);
		} catch (LeaveAlreadyAppliedException ex) {
			logger.info("leave already applied on mentioned period");
			httpServletResponse.setStatus(HttpServletResponse.SC_FOUND);
		} catch (NotWorkingDayException ex) {
			logger.info("leave is applied on non working day");
			httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} catch (NotEnoughLeavesAvaialableException ex) {
			logger.info("NotEnoughLeavesAvaialableException");
			httpServletResponse
					.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
		} catch (UnderNoticeException exception) {
			logger.info("You are in UnderNotice so you can't apply leave");
			httpServletResponse
					.setStatus(httpServletResponse.SC_NOT_ACCEPTABLE);
		}catch (ProbationException exception){
			logger.info("You are in probation period so you can apply leave after 15 January 2019");
			httpServletResponse
			.setStatus(httpServletResponse.SC_EXPECTATION_FAILED);
		}

	}

	@RequestMapping(value = "/leaves", params = { "startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> leaves(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			HttpServletResponse httpServletResponse) {

		Long employeeId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		return leaveService.getEmployeeLeaves(startIndex, endIndex, employeeId);
	}

	@RequestMapping(value = "/leaveSummary", method = RequestMethod.GET)
	public @ResponseBody LeaveSummaryDTO leaveSummary() {

		// here getting employee id from security context in securityutils class
		return leaveService.getLeaveSummary(securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder());
	}

	@RequestMapping(value = "/cancel", params = { "leaveId" }, method = RequestMethod.PUT)
	public @ResponseBody void cancel(@RequestParam Long leaveId,
			HttpServletResponse httpServletResponse) throws Exception {
		leaveService.cancelLeave(leaveId);
	}

	@RequestMapping(value = "/bioAdminManager/reject", params = { "leaveId" }, method = RequestMethod.PUT)
	public @ResponseBody void reject(@RequestParam Long leaveId ,HttpServletResponse httpServletResponse)
			throws Exception {
		leaveService.rejectlLeave(leaveId);
	}

	@RequestMapping(value = "/isInProbation", params = { "employeeId" }, method = RequestMethod.GET)
	public @ResponseBody boolean isInProbation(@RequestParam Long employeeId) {
		return leaveService.isInProbation(employeeId);
	}

	@RequestMapping(value = "/bioAdminManager/employeeLeaves", params = {
			"startIndex", "endIndex", "managerId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> employeeLeaves(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam Long managerId, HttpServletResponse value) {
		return leaveService.getAllEmployeeLeaves(startIndex, endIndex,
				managerId);
	}

	@RequestMapping(value = "/bioAdminManager/approve", params = { "leaveId",
			"adminComments" }, method = RequestMethod.PUT)
	public @ResponseBody void approve(@RequestParam Long leaveId,
			@RequestParam String adminComments,
			HttpServletResponse httpServletResponse) {
		leaveService.approveLeave(leaveId, adminComments);
	}

	@RequestMapping(value = "/bioAdminManager/searchEmployees", params = {
			"startIndex", "endIndex" }, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> searchEmployees(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestBody SearchCriteriaDTO searchCriteriaDTO,
			HttpServletResponse httpServletResponse) {
		return leaveService.searchEmployees(startIndex, endIndex,
				searchCriteriaDTO);
	}

	@RequestMapping(value = "/datePeiod", method = RequestMethod.GET)
	public @ResponseBody DateRange monthPeiod() {
		return leaveService.getMonthPeiod();

	}

	// For employee for cancel after approval of leave
	@RequestMapping(value = "/cancelAfterApproval", params = { "leaveId" }, method = RequestMethod.PUT)
	public @ResponseBody void cancelAfterApproval(@RequestParam Long leaveId,
			HttpServletResponse httpServletResponse) throws Exception {
		leaveService.cancelAfterApproval(leaveId);
	}
	
	@RequestMapping(value ="/checkProjectManagerExits", params ={"leaveid"}, method = RequestMethod.GET)
	public @ResponseBody boolean  checkProjectManagerExits(@RequestParam Long leaveid){
		return leaveService.checkprojectManagerexits(leaveid);
	}

	@RequestMapping(value = "/probationPeriod", method = RequestMethod.GET)
	public @ResponseBody List<Integer> probationPeriod() {
		return leaveService.probationPeriod();
	}
	@RequestMapping(value = "/getCuttOffDates", method = RequestMethod.GET)
	public @ResponseBody List<Integer> getCuttOffDates() {
		return leaveService.getCuttOffDates();
	}

}
