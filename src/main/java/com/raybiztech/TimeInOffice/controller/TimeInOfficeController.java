/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeInOffice.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.TimeInOffice.dto.TimeInOfficeDTO;
import com.raybiztech.TimeInOffice.service.TimeInOfficeService;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;

/**
 * 
 * @author hari
 */
@Controller
@RequestMapping("/timeInOffice")
public class TimeInOfficeController {

	@Autowired
	TimeInOfficeService timeInOfficeService;

	@Autowired
	DAO dao;

	@Autowired
	SecurityUtils securityUtils;

	@RequestMapping(value = "/getTimeInOfficeManagerReport", params = { "loggedInEmployeeId", "date", "startIndex",
			"endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getTimeInOfficeManagerReport(@RequestParam String loggedInEmployeeId,
			@RequestParam String date, @RequestParam Integer startIndex, @RequestParam Integer endIndex,
			HttpServletResponse httpServletResponse) {

		// here for aop purpose making controller id param to Long
		String loggedEmployeeId = String.valueOf(loggedInEmployeeId);

		return timeInOfficeService.getTimeInOfficeManagerReport(loggedEmployeeId, date, startIndex, endIndex);
	}

	@RequestMapping(value = "/getTimeInOfficeEmployeeReport", params = { "loggedInEmployeeId",
			"date" }, method = RequestMethod.GET)
	public @ResponseBody TimeInOfficeDTO getTimeInOfficeEmployeeReport(@RequestParam Long loggedInEmployeeId,
			@RequestParam String date, HttpServletResponse httpServletResponse) {

		// here for aop purpose making controller id param to Long
		String loggedEmployeeId = String.valueOf(loggedInEmployeeId);

		return timeInOfficeService.getTimeInOfficeEmployeeReport(loggedEmployeeId, date);
	}

	@RequestMapping(value = "/searchTimeInOffice", params = { "loggedInEmployeeId", "date", "startIndex", "endIndex",
			"search" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchTimeInOffice(@RequestParam String loggedInEmployeeId,
			@RequestParam String date, @RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String search) {
		return timeInOfficeService.searchTimeInOffice(loggedInEmployeeId, date, startIndex, endIndex, search);
	}

	@RequestMapping(value = "/weeklyTimeInOfficeReport", method = RequestMethod.GET)
	public @ResponseBody List<TimeInOfficeDTO> weeklyTimeInOfficeReport() {

		return timeInOfficeService.getweeklyTimeInOfficeReport(
				String.valueOf(securityUtils.getLoggedEmployeeIdforSecurityContextHolder()));
	}

	@RequestMapping(value = "/exportAttendanceReport", params = { "hiveDate", "search" }, method = RequestMethod.GET)
	@ResponseBody
	public void exportAttendanceReport(@RequestParam String hiveDate, @RequestParam String search,
			HttpServletResponse httpServletResponse) throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"Report.csv\"");
		ByteArrayOutputStream os = timeInOfficeService.exportAttendanceData(hiveDate, search);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}
}
