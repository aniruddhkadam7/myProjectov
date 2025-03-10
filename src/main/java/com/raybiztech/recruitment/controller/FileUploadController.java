/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.controller;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.raybiztech.SQAAudit.service.SQAAuditService;
import com.raybiztech.employeeprofile.service.EmployeeProfileServiceI;
import com.raybiztech.meetingrequest.service.MeetingRequestService;
import com.raybiztech.offerLetter.service.OfferLetterService;
import com.raybiztech.recruitment.dto.CandidateDTO;
import com.raybiztech.recruitment.service.JobPortalService;
import com.raybiztech.separation.service.SeparationService;
import com.raybiztech.supportmanagement.service.SupportManagementService;

/**
 *
 * @author hari
 */
@Controller
@RequestMapping("/fileUpload")
public class FileUploadController {

	@Autowired
	JobPortalService jobPortalServiceImpl;
	@Autowired
	SupportManagementService supportManagementServiceImpl;
	@Autowired
	SeparationService separationServiceImpl;
	@Autowired
	EmployeeProfileServiceI employeeProfileServiceImpl;
	
	@Autowired
	OfferLetterService offerLetterServiceImpl;
	
	@Autowired
	MeetingRequestService meetingRequestServiceImpl;
	
	@Autowired
	SQAAuditService sqaAuditServiceImpl;

	Logger logger = Logger.getLogger(FileUploadController.class);

	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadImage(MultipartHttpServletRequest request,
			HttpServletResponse response) {

		HttpHeaders headers = request.getRequestHeaders();

		Iterator<String> itr = request.getFileNames();

		if (itr.hasNext()) {
			String file = itr.next();

			MultipartFile mpf = request.getFile(file);

			// request.getParameter("empId")
			jobPortalServiceImpl
					.uploadImage(mpf, request.getParameter("empId"));
		}
	}

	@RequestMapping(value = "/uploadCroppedImage", params = { "base64data",
			"empID" }, method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadCroppedImage(String base64data, Long empID) {
		int dot1 = base64data.indexOf(",");
		String substr = base64data.substring(dot1 + 1, base64data.length());
		jobPortalServiceImpl.uploadBase64Image(substr, empID);
	}

	@RequestMapping(value = "/importExcelFile", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public List<CandidateDTO> importExcelFile(
			MultipartHttpServletRequest request, HttpServletResponse response) {

		HttpHeaders headers = request.getRequestHeaders();

		Iterator<String> itr = request.getFileNames();
		List<CandidateDTO> candidateDTOs = null;
		if (itr.hasNext()) {
			String file = itr.next();

			MultipartFile mpf = request.getFile(file);

			// request.getParameter("empId")
			candidateDTOs = jobPortalServiceImpl.importExcelFile(mpf,
					request.getParameter("empId"));
		}
		return candidateDTOs;
	}

	@RequestMapping(value = "/uploadCandidateResume", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadCandidateResume(MultipartHttpServletRequest request,
			HttpServletResponse response) {

		HttpHeaders headers = request.getRequestHeaders();

		Iterator<String> itr = request.getFileNames();

		if (itr.hasNext()) {
			String file = itr.next();

			MultipartFile mpf = request.getFile(file);

			// request.getParameter("empId")
			jobPortalServiceImpl.uploadCandidateResume(mpf,
					request.getParameter("personId"));
		}
	}

	@RequestMapping(value = "/uploadCandidateObservation", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadFileInObservation(MultipartHttpServletRequest request,
			HttpServletResponse response) {

		Iterator<String> fileNames = request.getFileNames();

		if (fileNames != null) {

			if (fileNames.hasNext()) {

				String fileString = fileNames.next();
				MultipartFile mpf = request.getFile(fileString);

				jobPortalServiceImpl.uploadFileInObservation(mpf,
						request.getParameter("personId"));

			}

		}

	}

	// uploadSupport Tickets Documents
	@RequestMapping(value = "/uploadSupportTicketsDocuments", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadSupportTicketsDocuments(
			MultipartHttpServletRequest request, HttpServletResponse response) {
		HttpHeaders headers = request.getRequestHeaders();
		Iterator<String> itr = request.getFileNames();
		if (itr.hasNext()) {
			String file = itr.next();

			MultipartFile mpf = request.getFile(file);
			// System.out.println("upload support ticket doc's"+mpf.getName());
			supportManagementServiceImpl.uploadSupportTicketsDocuments(mpf,
					request.getParameter("ticketId"));
		}
	}

	@RequestMapping(value = "/uploadRBTResume", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadRBTResume(MultipartHttpServletRequest request,
			HttpServletResponse response) {

		HttpHeaders headers = request.getRequestHeaders();

		Iterator<String> itr = request.getFileNames();

		if (itr.hasNext()) {
			String file = itr.next();

			MultipartFile mpf = request.getFile(file);

			Long employeeId = Long.valueOf(request.getParameter("personId"));

			// request.getParameter("empId")
			jobPortalServiceImpl.uploadRBTResume(mpf, employeeId);
		}
	}

	// upload relieving letter
	@RequestMapping(value = "/uploadrelievingletter", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadrelievingletter(MultipartHttpServletRequest request,
			HttpServletResponse response) {

		HttpHeaders headers = request.getRequestHeaders();

		Iterator<String> itr = request.getFileNames();

		if (itr.hasNext()) {
			String file = itr.next();

			MultipartFile mpf = request.getFile(file);

			Long exitFeedbakformId = Long.valueOf(request
					.getParameter("exitfeddbackformId"));

			separationServiceImpl.uploadrelievingletter(mpf, exitFeedbakformId);

			// request.getParameter("empId")

		}

	}

	@RequestMapping(value = "/uploadExitfeedBackFile", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadExitfeedBackFile(MultipartHttpServletRequest request,
			HttpServletResponse response) {

		HttpHeaders headers = request.getRequestHeaders();

		Iterator<String> itr = request.getFileNames();

		if (itr.hasNext()) {
			String file = itr.next();

			MultipartFile mpf = request.getFile(file);

			Long exitFeedbakformId = Long.valueOf(request
					.getParameter("exitfeddbackformId"));

			separationServiceImpl
					.uploadExitfeedBackFile(mpf, exitFeedbakformId);

			// request.getParameter("empId")

		}

	}

	@RequestMapping(value = "/uploadEmployeeFinanceDetails", method = RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadEmployeeFinanceDetails(
			MultipartHttpServletRequest request, HttpServletResponse response) {

		Iterator<String> itr = request.getFileNames();

		if (itr.hasNext()) {
			String file = itr.next();

			MultipartFile mpf = request.getFile(file);

			String financeId = request.getParameter("financeId");

			employeeProfileServiceImpl.uploadEmployeeFinanceDetails(mpf,
					financeId);
		}
	}
	
	//Upload Passport Front Copy
	@RequestMapping(value ="/uploadPassPortFrontImage",method =RequestMethod.POST)
	@Transactional
	@ResponseBody
	public void uploadPassportFrontCopy(MultipartHttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
		
		HttpHeaders headers = httpServletRequest.getRequestHeaders();

		Iterator<String> itr = httpServletRequest.getFileNames();
		
		
		while (itr.hasNext()) {
			String file = itr.next();
			MultipartFile mpf = httpServletRequest.getFile(file);
			if(file.equals("file1")) {
				jobPortalServiceImpl.uploadPassportFrontCopy(mpf,httpServletRequest.getParameter("empId"));
			}
			else {
				jobPortalServiceImpl.uploadPassportBackCopy(mpf,httpServletRequest.getParameter("empId"));
			}
			
			
		}
		
		/*if (itr.hasNext()) {
			String file = itr.next();
			MultipartFile mpf = httpServletRequest.getFile(file);
			jobPortalServiceImpl.uploadPassportFrontCopy(mpf,httpServletRequest.getParameter("empId"));
	}*/
	}
	//Upload Passport Back Copy
		@RequestMapping(value ="/uploadPassPortBackImage",method =RequestMethod.POST)
		@Transactional
		@ResponseBody
		public void uploadPassportBackCopy(MultipartHttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
			HttpHeaders headers = httpServletRequest.getRequestHeaders();

			Iterator<String> itr = httpServletRequest.getFileNames();

			if (itr.hasNext()) {
				String file = itr.next();

				MultipartFile mpf = httpServletRequest.getFile(file);
				jobPortalServiceImpl.uploadPassportBackCopy(mpf,httpServletRequest.getParameter("empId"));
		}
		}
	//upload visa details copy
		@RequestMapping(value ="/uploadVisaImage", method =RequestMethod.POST)
		@Transactional
		@ResponseBody
		public void uploadVisaDetailsCopy(MultipartHttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse){
			HttpHeaders headers = httpServletRequest.getRequestHeaders();
			Iterator<String> itr = httpServletRequest.getFileNames();

			if (itr.hasNext()) {
				String file = itr.next();
				MultipartFile mpf = httpServletRequest.getFile(file);
				jobPortalServiceImpl.uploadVisaDetailsCopy(mpf,httpServletRequest.getParameter("visaId"));
		}
		}
		
		@RequestMapping(value = "/uploadOfferLetter", method = RequestMethod.POST)
		@Transactional
		@ResponseBody
		public void uploadOfferLetter(MultipartHttpServletRequest request,
				HttpServletResponse response) {

			HttpHeaders headers = request.getRequestHeaders();

			Iterator<String> itr = request.getFileNames();

			if (itr.hasNext()) {
				String file = itr.next();

				MultipartFile mpf = request.getFile(file);

				
					offerLetterServiceImpl.uploadOfferLetterDocument(mpf);

				// request.getParameter("empId")

			}

		}
		
		@RequestMapping(value = "/uploadMeetingRequestFeedbackForm", method = RequestMethod.POST)
		@Transactional
		@ResponseBody
		public void uploadMeetingRequestFeedbackForm(MultipartHttpServletRequest request,
				HttpServletResponse response) {

			HttpHeaders headers = request.getRequestHeaders();

			Iterator<String> itr = request.getFileNames();

			if (itr.hasNext()) {
				String file = itr.next();

				MultipartFile mpf = request.getFile(file);

				meetingRequestServiceImpl.uploadFeedbackForm(mpf,
						request.getParameter("eventId"));
			}
		}
	
		@RequestMapping(value = "/uploadSQAAuditFiles", method = RequestMethod.POST)
		@Transactional
		@ResponseBody
		public void uploadSQAAuditFiles(MultipartHttpServletRequest request,
				HttpServletResponse reponse) {
			HttpHeaders headers = request.getRequestHeaders();
			
			Iterator<String> itr = request.getFileNames();
			if(itr.hasNext()) {
				String file = itr.next();
				
				MultipartFile mpf = request.getFile(file);
				
				sqaAuditServiceImpl.uploadSQAAuditFiles(mpf, request.getParameter("auditId")
						);
				
			}
		}
}
