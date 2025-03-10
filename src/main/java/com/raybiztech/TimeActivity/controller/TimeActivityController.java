/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeActivity.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityDTO;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityReport;
import com.raybiztech.TimeActivity.service.TimeActivityService;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;

/**
 *
 * @author naresh
 */
@Controller
@RequestMapping("/timeActivity")
public class TimeActivityController {

	@Autowired
	TimeActivityService timeActivityService;
	@Autowired
	DAO dao;

	@RequestMapping(value = "/hiveTimeActivities", params = { "userName" }, method = RequestMethod.GET)
	public @ResponseBody List<EmpoloyeeHiveActivityDTO> getEmployeeHiveTimeActivities(
			@RequestParam String userName) {
		return timeActivityService.getEmployeeHiveTimeActivities(userName);
	}

	@RequestMapping(value = "/employeeDayHiveSheet", params = { "projectDate",
			"userName" }, method = RequestMethod.GET)
	public @ResponseBody List<EmpoloyeeHiveActivityDTO> getEmployeeDayHiveSheet(
			@RequestParam String projectDate, @RequestParam String userName) {
		return timeActivityService.getEmployeeDayHiveSheet(projectDate,
				userName);
	}

	@RequestMapping(value = "/managerHiveReports", params = { "hiveDate",
			"startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getMonthlyHiveReportForManager(
			@RequestParam String hiveDate, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex) {

		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		Employee loggedInEmp = dao.findByUniqueProperty(Employee.class,
				"userName", userName);

		String loggedInEmpId = String.valueOf(loggedInEmp.getEmployeeId());

		return timeActivityService.getMonthlyHiveReportForManager(hiveDate,
				loggedInEmpId, startIndex, endIndex);
	}

	@RequestMapping(value = "/exportHiveReport", params = { "hiveDate" }, method = RequestMethod.GET)
	@ResponseBody
	public void exportHiveReport(@RequestParam String hiveDate,
			HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"HiveReport.csv\"");
		ByteArrayOutputStream os = timeActivityService
				.exportBioAttendance(hiveDate);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = "/employeeHiveReports", params = { "hiveDate" }, method = RequestMethod.GET)
	public @ResponseBody EmpoloyeeHiveActivityReport getMonthlyHiveReportForEmployee(
			@RequestParam String hiveDate) {

		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		Employee loggedInEmp = dao.findByUniqueProperty(Employee.class,
				"userName", userName);

		String loggedInEmpId = String.valueOf(loggedInEmp.getEmployeeId());

		return timeActivityService.getMonthlyHiveReportForEmployee(hiveDate,
				loggedInEmpId);
	}

	@RequestMapping(value = "/searchHiveTime", params = { "loggedInEmployeeId",
			"hiveDate", "startIndex", "endIndex", "search" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchTimeInOffice(
			@RequestParam String loggedInEmployeeId,
			@RequestParam String hiveDate, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex, @RequestParam String search) {
		return timeActivityService.searchHiveTime(loggedInEmployeeId, hiveDate,
				startIndex, endIndex, search);
	}
}
