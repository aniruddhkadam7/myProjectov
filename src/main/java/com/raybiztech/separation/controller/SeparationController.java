package com.raybiztech.separation.controller;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.separation.chart.SeparationChart;
import com.raybiztech.separation.dto.ClearanceCertificateDTO;
import com.raybiztech.separation.dto.ExitFeedBackDTO;
import com.raybiztech.separation.dto.SeparationDTO;
import com.raybiztech.separation.exception.CannotSubmitResignationException;
import com.raybiztech.separation.exception.InvalidTimeException;
import com.raybiztech.separation.exception.SeparationAlreadyExistException;
import com.raybiztech.separation.service.SeparationService;

@Controller
@RequestMapping("/separationController")
public class SeparationController {
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	SeparationService separationServiceImpl;

	// Employee Resignation Details
	@RequestMapping(value = "/SubmitResignation", method = RequestMethod.POST)
	public @ResponseBody void SubmitResignation(
			@RequestBody SeparationDTO separationDTO,
			HttpServletResponse response) {
		try {
			separationServiceImpl.saveResignationDetails(separationDTO,
					response);
		} catch (SeparationAlreadyExistException se) {
			response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
		} catch (UnauthorizedUserException exception) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		catch (InvalidTimeException exception) {
			response.setStatus(HttpServletResponse.SC_REQUEST_TIMEOUT);
		}
		catch(CannotSubmitResignationException exception) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	// This is for getting separation employee information
	@RequestMapping(value = "/getSeparationForm", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getEmployeeInfo() {
		return separationServiceImpl.getSeparationForm();
	}

	// This is for getting the separation Employee form
	/*@RequestMapping(value = "/getSeparation", params = { "separationId" }, method = RequestMethod.GET)
	public @ResponseBody SeparationDTO getSeparationId(
			@RequestParam Long separationId,HttpServletResponse res) {
		SeparationDTO dto=null;
		try{
			dto= separationServiceImpl.getSeparation(separationId,res);
		}catch (UnauthorizedUserException exception) {
			res.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}
		return dto;
	}*/
	
	@RequestMapping(value = "/getSeparation", params = { "separationId" }, method = RequestMethod.GET)
	public @ResponseBody SeparationDTO getSeparationId(
			@RequestParam Long separationId,HttpServletResponse httpServletResponse) {
		SeparationDTO separationDTO=null;
		try {
			separationDTO= separationServiceImpl.getSeparation(separationId);
		}
		catch(UnauthorizedUserException exception)
		{
			httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
			
		}
		return separationDTO;
	}


	// This is for updating the manager comments
	@RequestMapping(value = "/updateSeparation", method = RequestMethod.PUT)
	public @ResponseBody void updateSeparation(
			@RequestBody SeparationDTO separation) {
		separationServiceImpl.updateSeparation(separation);
	}

	// This is for revoking the resignation
	/*
	 * @RequestMapping(value = "/revokeResignation", params = { "separationId"
	 * }, method = RequestMethod.PUT) public @ResponseBody void
	 * revokeResignation(@RequestParam Long separationId) {
	 * separationServiceImpl.revokeResignation(separationId); }
	 */

	@RequestMapping(value = "/revokeResignation", method = RequestMethod.POST)
	public @ResponseBody void revokeResignation(
			@RequestBody SeparationDTO separation) {
		separationServiceImpl.revokeResignation(separation);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/resignationList", params = { "multiplesearch",
			"startIndex", "endIndex", "dateSelection", "from", "to", "status","empStatus" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> resignationList(
			@RequestParam String multiplesearch,
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String dateSelection, @RequestParam String from,
			@RequestParam String to, @RequestParam String status, @RequestParam String empStatus) {
		System.out.println("in controller");
		return separationServiceImpl.getResignationList(multiplesearch,
				startIndex, endIndex, dateSelection, from, to, status,empStatus);
	}

	@RequestMapping(value = "/clearanceCertificateComments", method = RequestMethod.POST)
	public @ResponseBody void clearanceCertificateComments(
			@RequestBody ClearanceCertificateDTO clearanceDTO) {
		separationServiceImpl.saveClearanceCertificateComments(clearanceDTO);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public @ResponseBody void update(@RequestBody SeparationDTO separation) {
		System.out.println(separation.getStatus());
		separationServiceImpl.update(separation);
	}

	@RequestMapping(value = "/IntitiateCC", params = { "separationId" }, method = RequestMethod.PUT)
	public @ResponseBody void IntitiateCC(@RequestParam Long separationId) {
		separationServiceImpl.IntitiateCC(separationId);
	}

	@RequestMapping(value = "/saveExitFeedBackForm", method = RequestMethod.POST)
	public @ResponseBody Long saveExitFeedBackForm(
			@RequestBody ExitFeedBackDTO exitFeedBackDTO) {
		return separationServiceImpl.saveExitFeedBackForm(exitFeedBackDTO);
	}

	@RequestMapping(value = "/getClearanceDetails", params = { "separationId",
			"submittedBy" }, method = RequestMethod.GET)
	public @ResponseBody List<ClearanceCertificateDTO> getClearanceDetails(
			@RequestParam Long separationId, @RequestParam String submittedBy) {
		return separationServiceImpl.getClearanceDetails(separationId,
				submittedBy);
	}

	@RequestMapping(value = "/getEmpDetails", params = { "separationId" }, method = RequestMethod.GET)
	public @ResponseBody SeparationDTO getEmpDetails(
			@RequestParam Long separationId) {
		return separationServiceImpl.getEmpDetails(separationId);
	}

	@RequestMapping(value = "/checkExitFeedBackForm", params = { "separationId" }, method = RequestMethod.GET)
	public @ResponseBody ExitFeedBackDTO checkExitFeedBackForm(
			@RequestParam Long separationId) {
		return separationServiceImpl.checkExitFeedBackForm(separationId);
	}

	@RequestMapping(value = "/getEmployeeSeparationForm", params = { "employeeId" }, method = RequestMethod.GET)
	public @ResponseBody SeparationDTO getEmployeeSeparationForm(
			@RequestParam Long employeeId) {
		return separationServiceImpl.getEmployeeSepartion(employeeId);
	}

	@RequestMapping(value = "/updateCC", method = RequestMethod.PUT)
	public @ResponseBody void updateCC(
			@RequestBody ClearanceCertificateDTO clearanceCertificateDTO) {
		separationServiceImpl.updateCC(clearanceCertificateDTO);
	}

	@RequestMapping(value = "/downloadFile", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadFile(HttpServletResponse response,
			String fileName) {
		separationServiceImpl.downloadFile(response, fileName);
	}

	@RequestMapping(value = "/getSeparationChart", params = { "dateSelection",
			"from", "to" }, method = RequestMethod.GET)
	public @ResponseBody SeparationChart getSeparationChart(
			@RequestParam String dateSelection, @RequestParam String from,
			@RequestParam String to) {
		return separationServiceImpl
				.getSeparationChart(dateSelection, from, to);
	}

	@RequestMapping(value = "/exportResignationList", params = { "status",
			"from", "to", "multiplesearch", "dateSelection" ,"empStatus"}, method = RequestMethod.GET)
	public @ResponseBody void exportResignationList(
			@RequestParam String status, @RequestParam String dateSelection,
			@RequestParam String from, @RequestParam String to,
			@RequestParam String multiplesearch, @RequestParam String empStatus,

			HttpServletResponse httpServletResponse) throws Exception {

		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"ResignationList.csv\"");

		ByteArrayOutputStream os = separationServiceImpl.exportResignationList(
				status, dateSelection, from, to, multiplesearch,empStatus);

		httpServletResponse.getOutputStream().write(os.toByteArray());
	}

	@RequestMapping(value = "/downloadForm", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadForm(HttpServletResponse response,
			String fileName) {
		separationServiceImpl.downloadForm(response, fileName);
	}

	@RequestMapping(value = "/getEmployeeResg", method = RequestMethod.GET)
	public @ResponseBody SeparationDTO getEmployeeResg() {
		return separationServiceImpl.getEmployeeResg();

	}
	
	/*@RequestMapping(value="/abscondedEmployee",method=RequestMethod.PUT)
	public @ResponseBody void abscondedEmployee(@RequestBody SeparationDTO separationDTO) throws ParseException
	{
		separationServiceImpl.abscondedEmployee(separationDTO);
	}*/
	
	//Below functionality is for sending mail to employee personal email
	@RequestMapping(value = "/sendMailToEmployeePersonalEmail", params = { "separationId" }, method = RequestMethod.PUT)
	public @ResponseBody void sendMailToEmployeePersonalEmail(@RequestParam Long separationId) {
		separationServiceImpl.sendMailToEmployeePersonalEmail(separationId);
	}
	

}
