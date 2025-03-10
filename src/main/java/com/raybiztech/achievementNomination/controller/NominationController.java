package com.raybiztech.achievementNomination.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.achievementNomination.dto.InitiateNominationDto;
import com.raybiztech.achievementNomination.dto.NominationCycleDto;
import com.raybiztech.achievementNomination.dto.NominationDto;
import com.raybiztech.achievementNomination.dto.NominationQuestionDto;
import com.raybiztech.achievementNomination.service.NominationService;
import com.raybiztech.achievementNomination.util.NomineeAlreadyAddedException;
import com.raybiztech.appraisals.exceptions.CycleNotActiveException;
import com.raybiztech.appraisals.exceptions.DuplicateCycleNameException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;

/**
 *
 * @author Aprajita
 */
@Controller
@RequestMapping("/nominationController")
public class NominationController {

	Logger logger = Logger.getLogger(NominationController.class);

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	NominationService nominationServiceImpl;

	/* To add nomination question */
	@RequestMapping(value = "/addQuestion", method = RequestMethod.POST)
	public @ResponseBody void addQuestion(
			@RequestBody NominationQuestionDto nominationQuestionDto) {
		nominationServiceImpl.addQuestion(nominationQuestionDto);
	}

	/* To get all question List */
	@RequestMapping(value = "/getAllQuestions", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllQuestions() {
		return nominationServiceImpl.getAllQuestions();
	}

	/* To delete question */
	@RequestMapping(value = "/deleteQuestion", params = { "questionId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteQuestion(
			@RequestParam("questionId") Long questionId,HttpServletResponse httpServletResponse) {
		try{
		nominationServiceImpl.deleteQuestion(questionId);
		}catch(Exception exception){
			httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
	}

	/* To add nomination cycle */
	@RequestMapping(value = "/addCycle", method = RequestMethod.POST)
	public @ResponseBody void addCycle(
			@RequestBody NominationCycleDto nominationCycleDto,HttpServletResponse httpServletResponse) {
		try{
			nominationServiceImpl.addCycle(nominationCycleDto);
		}
		catch (DuplicateCycleNameException e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	/* To get all cycle List */
	@RequestMapping(value = "/getallcycles", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getallcycles() {
		return nominationServiceImpl.getAllCycles();
	}

	/* To delete question */
	@RequestMapping(value = "/deleteCycle", params = { "cycleId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteCycle(@RequestParam("cycleId") Long cycleId) {
		nominationServiceImpl.deleteCycle(cycleId);
	}

	/* To get active cycle */
	@RequestMapping(value = "/getActiveCycleData", method = RequestMethod.GET)
	public @ResponseBody InitiateNominationDto getActiveCycleData(HttpServletResponse response) {
		InitiateNominationDto initiateNominationDto=null;
		try {
			initiateNominationDto= nominationServiceImpl.getActiveCycleData();	
		} catch (CycleNotActiveException e) {
			response.setStatus(HttpServletResponse.SC_GONE);
		}
		return initiateNominationDto;
	}

	/* To initiate cycle */
	@RequestMapping(value = "/initiateCycle", method = RequestMethod.POST)
	public @ResponseBody void initiateCycle(
			@RequestBody InitiateNominationDto initiateCycleDto) {
		nominationServiceImpl.initiateCycle(initiateCycleDto);
	}

	/* To get particular cycle for edit */
	@RequestMapping(value = "/editCycle", params = { "cycleId" }, method = RequestMethod.GET)
	public @ResponseBody NominationCycleDto editCycle(
			@RequestParam("cycleId") Long cycleId) {
		return nominationServiceImpl.editCycle(cycleId);
	}

	/* update cycle */
	@RequestMapping(value = "/updateCycle", method = RequestMethod.PUT)
	public @ResponseBody void updateCycle(
			@RequestBody NominationCycleDto editcycledetails,HttpServletResponse httpServletResponse) {
	try{
		nominationServiceImpl.updateCycle(editcycledetails);
	}
	catch (DuplicateCycleNameException e) {
		httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
	}
	}

	/* To Get Nomination Form Details */
	@RequestMapping(value = "/nominationFormDetails", method = RequestMethod.GET)
	public @ResponseBody NominationDto getNominationFormDetails() {
		return nominationServiceImpl.getFormDetails();
	}

	/* To Add nominee */

	/* To add nomination */
	@RequestMapping(value = "/addNominee", method = RequestMethod.POST)
	public @ResponseBody void addNominee(
			@RequestBody NominationDto nominationDto,
			HttpServletResponse httpServletResponse) {

		try {
			nominationServiceImpl.addNominee(nominationDto);
		} catch (NomineeAlreadyAddedException e) {
			httpServletResponse
					.setStatus(httpServletResponse.SC_NOT_ACCEPTABLE);
		}

	}

	/* To get Nominations of cycle */
	@RequestMapping(value = "/getNominations", params = { "cycleId" }, method = RequestMethod.GET)
	public @ResponseBody List<NominationDto> getNominations(
			@RequestParam("cycleId") Long cycleId) {
		return nominationServiceImpl.getNominations(cycleId);
	}

	/* To get Individual Nominations */
	@RequestMapping(value = "/getNominationDetails", params = { "nominationId" }, method = RequestMethod.GET)
	public @ResponseBody NominationDto getNominationDetails(
			@RequestParam("nominationId") Long nominationId) {
		return nominationServiceImpl.getNominationDetails(nominationId);
	}

	/* To Update nomination */
	@RequestMapping(value = "/reviewNominee", method = RequestMethod.PUT)
	public @ResponseBody void reviewNominee(
			@RequestBody NominationDto nominationDto) {
		nominationServiceImpl.updateNominee(nominationDto);
	}

	@RequestMapping(value = "/exportNomineesList",params={"cycle"},method = RequestMethod.GET)
	public @ResponseBody void exportNomineesList(@RequestParam Long cycle,
			HttpServletResponse httpServletResponse)throws IOException{
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"NomineesList.csv\"");
		ByteArrayOutputStream os = nominationServiceImpl.exportNomineesListData(cycle);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}
	
}
