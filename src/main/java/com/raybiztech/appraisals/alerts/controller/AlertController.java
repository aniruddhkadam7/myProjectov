/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.alerts.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.alerts.dto.AlertDTO;
import com.raybiztech.appraisals.alerts.service.AlertServiceI;

/**
 *
 * @author naresh
 */
@Controller
@RequestMapping("/alert")
public class AlertController {

	private static final Logger logger = Logger
			.getLogger(AlertController.class);
	@Autowired
	AlertServiceI alertService;

	@RequestMapping(value = "/empAlert", params = { "employeeId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAlerts(
			@RequestParam Long employeeId,
			HttpServletResponse httpServletResponse) {
		return alertService.getAlerts(employeeId);
	}

	@RequestMapping(value = "/clickAlerts", params = { "employeeId" }, method = RequestMethod.GET)
	public @ResponseBody void getUpdateAllAlerts(@RequestParam Long employeeId,
			HttpServletResponse httpServletResponse) {
		alertService.getUpdateAllAlerts(employeeId);
	}

	@RequestMapping(value = "/updateAlert", params = { "employeeId", "alertId" }, method = RequestMethod.GET)
	public @ResponseBody AlertDTO updateAlertDetails(
			@RequestParam Long employeeId, @RequestParam String alertId,
			HttpServletResponse httpServletResponse) {

		// here (in params )we are making alert id to string because of aop

		Long convertedAlertID = Long.parseLong(alertId);
		return alertService.updateAlertDetails(employeeId, convertedAlertID);
	}

	@RequestMapping(value = "/allAlerts", params = { "employeeId",
			"startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllAlerts(
			@RequestParam Long employeeId, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex,
			HttpServletResponse httpServletResponse) {
		return alertService.getAllAlerts(employeeId, startIndex, endIndex);
	}
}
