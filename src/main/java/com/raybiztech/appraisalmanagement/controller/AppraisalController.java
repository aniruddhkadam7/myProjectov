package com.raybiztech.appraisalmanagement.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisalmanagement.dto.AppraisalCycleDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalFormDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalKPIDataDto;
import com.raybiztech.appraisalmanagement.dto.KRADto;
import com.raybiztech.appraisalmanagement.dto.ReviewAuditDto;
import com.raybiztech.appraisalmanagement.dto.SearchQueryParamsInAppraisalForm;
import com.raybiztech.appraisalmanagement.service.AppraisalService;
import com.raybiztech.appraisals.exceptions.CycleNotAccessException;
import com.raybiztech.appraisals.exceptions.CycleNotActiveException;
import com.raybiztech.appraisals.exceptions.DesignationNotAssginedException;
import com.raybiztech.appraisals.exceptions.DuplicateActiveCycleException;
import com.raybiztech.appraisals.exceptions.DuplicateCycleNameException;
import com.raybiztech.appraisals.exceptions.InvalidCycleRangeException;
import com.raybiztech.appraisals.exceptions.InvalidRatingsException;

@Controller
@RequestMapping("/appraisal")
public class AppraisalController {

	Logger log = Logger.getLogger(AppraisalController.class);
	@Autowired
	AppraisalService appraisalService;

	@RequestMapping(value = "/cycle", method = RequestMethod.POST)
	@ResponseBody
	public void addCycle(@RequestBody AppraisalCycleDto appraisalCycleDto,
			HttpServletResponse httpServletResponse) {
		try {
			appraisalService.addCycle(appraisalCycleDto);
		} catch (InvalidCycleRangeException e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);

		} catch (DuplicateActiveCycleException e) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
		} catch (DuplicateCycleNameException e) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
	}

	@RequestMapping(value = "/cycle", method = RequestMethod.GET)
	@ResponseBody
	public List<AppraisalCycleDto> getAllAppraisalCycle(
			HttpServletResponse httpServletResponse) {
		return appraisalService.getAllAppraisalCycle();
	}

	@RequestMapping(value = "/AppraisalForm", params = { "employeeid" }, method = RequestMethod.GET)
	@ResponseBody
	public AppraisalFormDto getAppraisalForm(@RequestParam Long employeeid,
			HttpServletResponse htppResponse) {

		AppraisalFormDto appraisalFormDto = null;
		try {
			appraisalFormDto = appraisalService.getAppraisalForm(employeeid);
		} catch (CycleNotActiveException cnae) {

			htppResponse.setStatus(HttpServletResponse.SC_GONE);

		} catch (CycleNotAccessException cycleNotAccessException) {
			htppResponse.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);

		} catch (DesignationNotAssginedException designationNotAssginedException) {

			htppResponse
					.setStatus(HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED);
		}

		return appraisalFormDto;

	}

	@RequestMapping(value = "/EmployeeAppraisalForm", method = RequestMethod.POST)
	@ResponseBody
	public void employeeAppraisalForm(
			@RequestBody AppraisalFormDto appraisalFormDto,
			HttpServletResponse httpServletResponse) {
		try {
			appraisalService.employeeAppraisalForm(appraisalFormDto);
		} catch (InvalidRatingsException invalidRatingsException) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		}
	}

	@RequestMapping(value = "/ManagerAppraisalForm", method = RequestMethod.POST)
	@ResponseBody
	public void managerAppraisalForm(
			@RequestBody AppraisalFormDto appraisalFormDto) {
		appraisalService.managerAppraisalForm(appraisalFormDto);
	}

	@RequestMapping(value = "/getCurrentForms", params = { "startIndex",
			"endIndex", "employeeID", "cycleId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllAppraisalForm(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam Long employeeID, @RequestParam Long cycleId) {
		return appraisalService.getCurrentForms(startIndex, endIndex,
				employeeID, cycleId);
	}

	@RequestMapping(value = "/getCycle", params = { "cycleId" }, method = RequestMethod.GET)
	public @ResponseBody AppraisalCycleDto getAllAppraisalForm(
			@RequestParam Long cycleId) {
		return appraisalService.getCycle(cycleId);
	}

	@RequestMapping(value = "/updateCycle", method = RequestMethod.PUT)
	@ResponseBody
	public void updateCycle(@RequestBody AppraisalCycleDto appraisalCycleDto,
			HttpServletResponse httpServletResponse) {

		try {
			appraisalService.updateCycle(appraisalCycleDto);
		} catch (InvalidCycleRangeException e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);

		} catch (DuplicateActiveCycleException e) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
		}
	}

	@RequestMapping(value = "/activeCycle", method = RequestMethod.GET)
	public @ResponseBody AppraisalCycleDto activeCycle() {
		return appraisalService.getActiveCycleData();
	}

	@RequestMapping(value = "/underKras/{cycleId}/{designationId}", method = RequestMethod.GET)
	public @ResponseBody List<KRADto> getAllKrasUnderDesignation(
			@PathVariable("cycleId") Long cycleId,
			@PathVariable("designationId") Long designationId) {
		return appraisalService.getAllKrasUnderDesignation(cycleId,
				designationId);
	}

	@RequestMapping(value = "/validateCycle", method = RequestMethod.PUT)
	@ResponseBody
	public void validateCycle(@RequestBody AppraisalCycleDto appraisalCycleDto,
			HttpServletResponse httpServletResponse) {

		try {
			appraisalService.validateCycle(appraisalCycleDto);
		} catch (DuplicateCycleNameException e) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);

		}
	}

	@RequestMapping(value = "/validateCycleRange", method = RequestMethod.PUT)
	@ResponseBody
	public void validateCycleRange(
			@RequestBody AppraisalCycleDto appraisalCycleDto,
			HttpServletResponse httpServletResponse) {

		try {
			appraisalService.validateCycleRange(appraisalCycleDto);
		} catch (InvalidCycleRangeException e) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);
		}
	}

	@RequestMapping(value = "/getSearchResult", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> searchEmployee(
			@RequestBody SearchQueryParamsInAppraisalForm paramsInAppraisalForm) {

		return appraisalService.searchEmployee(paramsInAppraisalForm);
	}

	/*
	 * @RequestMapping(value = "/getSearchResultByManager", params = {
	 * "startIndex", "endIndex", "employeeID", "cycleId","searchString" },
	 * method = RequestMethod.GET) public @ResponseBody Map<String, Object>
	 * searchByManagerName(
	 * 
	 * @RequestParam Integer startIndex, @RequestParam Integer endIndex,
	 * 
	 * @RequestParam Long employeeID, @RequestParam Long cycleId,
	 * 
	 * @RequestParam String searchString) { return
	 * appraisalService.searchByManagerName(startIndex, endIndex, employeeID,
	 * cycleId,searchString); }
	 */

	// get the designations under the cycle
	@RequestMapping(value = "/getDesignationsUnderCycle", params = {
			"startIndex", "endIndex", "cycleId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getDesignationsUnderCycle(
			@RequestParam("startIndex") Integer startIndex,
			@RequestParam("endIndex") Integer endIndex,
			@RequestParam("cycleId") Long cycleId,
			HttpServletResponse httpServletResponse) {
		return appraisalService.getDesignationsUnderCycle(startIndex, endIndex,
				cycleId);
	}

	// when manager clicking the submit then average of form rating should
	// display
	@RequestMapping(value = "/employeeAppraisalFormForRating", method = RequestMethod.POST)
	@ResponseBody
	public Double employeeAppraisalFormForRating(
			@RequestBody AppraisalFormDto appraisalFormDto,
			HttpServletResponse httpServletResponse) {
		return appraisalService
				.employeeAppraisalFormForRating(appraisalFormDto);
	}

	// this method used only completed appraisal forms
	@RequestMapping(value = "/existingAppraisalForm", params = { "appraisalFormId" }, method = RequestMethod.GET)
	@ResponseBody
	public AppraisalFormDto getExistingAppraisalForm(
			@RequestParam Long appraisalFormId, HttpServletResponse htppResponse) {
		return appraisalService.getExistingAppraisalForm(appraisalFormId);
	}

	// close appraisal form
	@RequestMapping(value = "/closeAppraisalForm", method = RequestMethod.PUT)
	public @ResponseBody void closeAppraisalForm(
			@RequestBody AppraisalFormDto appraisalFormDto,
			HttpServletResponse httpServletResponse) {
		appraisalService.closeAppraisalForm(appraisalFormDto);
	}

	// Appraisal confirmation (I Agree)
	@RequestMapping(value = "/appraisalConfirmation", method = RequestMethod.PUT)
	@ResponseBody
	public void appraisalConfirmation(
			@RequestBody AppraisalFormDto appraisalFormDto) {
		appraisalService.appraisalConfirmation(appraisalFormDto);
	}

	@RequestMapping(value = "/copyTheCycleData", method = RequestMethod.POST)
	@ResponseBody
	public void copyTheCycleData(@RequestParam Long oldCycleId,
			@RequestParam Long newCycleId) {

		appraisalService.copyTheCycleData(oldCycleId, newCycleId);

	}

	@RequestMapping(value = "/isAlreadyExist", params = { "newCycleId" }, method = RequestMethod.GET)
	@ResponseBody
	public Boolean isAlreadyExist(@RequestParam Long newCycleId) {
		return appraisalService.isAlreadyExist(newCycleId);
	}

	@RequestMapping(value = "/requestDiscussion", params = { "appraisalFormId" }, method = RequestMethod.PUT)
	@ResponseBody
	public void requestDiscussion(@RequestParam Long appraisalFormId) {
		appraisalService.requestDiscussion(appraisalFormId);
	}

	@RequestMapping(value = "/saveReviewComments", params = { "comments",
			"appraisalFormId" }, method = RequestMethod.POST)
	@ResponseBody
	public void saveReviewComments(@RequestParam String comments,
			@RequestParam Long appraisalFormId) {
		appraisalService.saveReviewComments(comments, appraisalFormId);
	}

	@RequestMapping(value = "/getReviewComments", params = { "appraisalFormId" }, method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getReviewComments(
			@RequestParam Long appraisalFormId) {

		return appraisalService.getReviewComments(appraisalFormId);
	}

	@RequestMapping(value = "/exportAppraisalList", params = { "activecycleId",
			"departmentName", "designationName", "appraisalFormStatus",
			"status", "search", "ratings", "fromDate", "toDate" }, method = RequestMethod.GET)
	@ResponseBody
	public void exportAppraisalList(@RequestParam Long activecycleId,
			@RequestParam String empStatus,
			@RequestParam String departmentName,
			@RequestParam String designationName,
			@RequestParam String appraisalFormStatus,
			@RequestParam String status, @RequestParam String search,
			@RequestParam List<Double> ratings, @RequestParam String fromDate,
			@RequestParam String toDate, HttpServletResponse httpServletResponse)
			throws IOException {
		SearchQueryParamsInAppraisalForm appraisalForm = new SearchQueryParamsInAppraisalForm();
		appraisalForm.setCycleId(activecycleId);
		appraisalForm.setEmpStatus(empStatus);
		appraisalForm.setDepartmentName(departmentName);
		appraisalForm.setDesignationName(designationName);
		appraisalForm.setAppraisalFormStatus(appraisalFormStatus);
		appraisalForm.setSearchString(search);
		appraisalForm.setRole(status);
		appraisalForm.setRatings(ratings);
		appraisalForm.setFromDate(fromDate);
		appraisalForm.setToDate(toDate);
		appraisalForm.setStartIndex(null);
		appraisalForm.setEndIndex(null);
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"AppraisalList.csv\"");
		ByteArrayOutputStream os = appraisalService
				.exportAppraisalList(appraisalForm);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = "/changedKPISRating", method = RequestMethod.POST)
	@ResponseBody
	public void changedKPISRating(@RequestBody AppraisalFormDto kpis) {
		appraisalService.changedKPISRating(kpis);
	}

}
