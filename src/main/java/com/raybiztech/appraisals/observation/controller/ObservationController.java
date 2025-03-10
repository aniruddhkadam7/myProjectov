package com.raybiztech.appraisals.observation.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.exceptions.InvalidRatingsException;
import com.raybiztech.appraisals.observation.dto.ObservationDTO;
import com.raybiztech.appraisals.observation.dto.PerformanceRatingsDTO;
import com.raybiztech.appraisals.observation.dto.SearchObservationDTO;
import com.raybiztech.appraisals.observation.exceptions.DuplicateObservationException;
import com.raybiztech.appraisals.observation.service.ObservationService;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.projectmanagement.dto.ReportDTO;

@Controller
@RequestMapping("/observation-mgnt")
public class ObservationController {

	@Autowired
	ObservationService observationService;
	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(ObservationController.class);

	@RequestMapping(value = "/observation", method = RequestMethod.POST)
	public @ResponseBody Long addObservation(
			@RequestBody ObservationDTO observationDTO,
			HttpServletResponse httpServletResponse) {
		Long id = null;
		try {
			id = observationService.addObservation(observationDTO);
		} catch (InvalidRatingsException invalidRatingsException) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (DuplicateObservationException exception) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		return id;

	}

	@RequestMapping(value = "/observation", method = RequestMethod.PUT)
	public @ResponseBody void updateObservation(
			@RequestBody ObservationDTO observationDTO) {
		observationService.updateObservation(observationDTO);
	}

	@RequestMapping(value = "/observation/{id}", method = RequestMethod.DELETE)
	public @ResponseBody void deleteObservation(@PathVariable("id") Long id) {
		observationService.deleteObservation(id);
	}

	@RequestMapping(value = "/observation/", params = { "startIndex",
			"endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> ObservationOfEmployee(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex) {

		Long id = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		return observationService.ObservationOfEmployee(id, startIndex,
				endIndex);
	}

	@RequestMapping(value = "/activeEmployee/", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportDTO> getActiveEmployees() {

		Long id = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();

		return observationService.getActiveEmployees(id);
	}

	@RequestMapping(value = "/observationreport/", params = { "startIndex",
			"endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> ObservationOfReport(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex) {

		Long empId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		return observationService.ObservationOfReport(empId, startIndex,
				endIndex);
	}

	@RequestMapping(value = "/searchObservation", params = { "startIndex",
			"endIndex" }, method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> searchObservation(
			@RequestBody SearchObservationDTO observationDTO,
			@RequestParam Integer startIndex, @RequestParam Integer endIndex) {

		Long empId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		return observationService.searchObservation(observationDTO, empId,
				startIndex, endIndex);
	}

	@RequestMapping(value = "/downloadFile", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadFile(HttpServletResponse response,
			String fileName) {

		observationService.downloadFile(response, fileName);
	}

	// Observation Graph
	/*
	 * @RequestMapping(value = "/getSelection", params = { "status", "personId",
	 * "fromMonth", "fromYear", "toMonth", "toYear" }, method =
	 * RequestMethod.GET) public @ResponseBody ObservationGraphsDTO
	 * getSelection(
	 * 
	 * @RequestParam String status, @RequestParam Long personId,
	 * 
	 * @RequestParam Integer fromMonth, @RequestParam Integer fromYear,
	 * 
	 * @RequestParam Integer toMonth, @RequestParam Integer toYear) {
	 * 
	 * return observationService.getSelectionObservatGraphs(personId, status,
	 * fromMonth, fromYear, toMonth, toYear); }
	 */

	@RequestMapping(value = "/getObservationHistory", params = { "dateStatus",
			"startIndex", "endIndex", "personId", "fromMonth", "fromYear",
			"toMonth", "toYear" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getObservationHistory(
			@RequestParam String dateStatus, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex, @RequestParam Long personId,
			@RequestParam Integer fromMonth, @RequestParam Integer fromYear,
			@RequestParam Integer toMonth, @RequestParam Integer toYear) {

		return observationService.getObservationHistory(dateStatus, startIndex,
				endIndex, personId, fromMonth, fromYear, toMonth, toYear);
	}

	@RequestMapping(value = "/getPerformanceRatings", method = RequestMethod.GET)
	public @ResponseBody List<PerformanceRatingsDTO> getPerformanceRatings() {
		return observationService.getAllPerformanceRatings();
	}

}
