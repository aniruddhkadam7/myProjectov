package com.raybiztech.appraisals.employee.controller;

import java.util.Iterator;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.raybiztech.appraisals.dto.AppraisalDTO;
import com.raybiztech.appraisals.dto.KPIRatingDTO;
import com.raybiztech.appraisals.dto.MyDTO;
import com.raybiztech.appraisals.employee.service.EmployeeAppraisalService;


@Controller
@RequestMapping("/employeeAppraisalController")
public class EmployeeAppraisalController {
	@Autowired
	private EmployeeAppraisalService employeeAppraisalService;
	private Logger logger = Logger.getLogger(EmployeeAppraisalController.class);

	@RequestMapping(value = "/getEmployeeAppraisalData", params = { "employeeId" }, method = RequestMethod.GET)
	public @ResponseBody
	AppraisalDTO getEmployeeAppraisalData(Long employeeId) {
		return employeeAppraisalService.getEmployeeAppraisalData(employeeId);
	}

	@RequestMapping(value = "/saveKpiRatingToKpis", method = RequestMethod.POST)
	public @ResponseBody
	void saveKPIRating(MyDTO myDto, MultipartHttpServletRequest request) {
		logger.info("ratingDTO object : " + myDto.getName());
		MultipartFile mpf = null;
		if (request != null) {
			Iterator<String> itr = request.getFileNames();
			try {
				while (itr.hasNext()) {
					String file = itr.next();
					mpf = request.getFile(file);
					if (mpf != null) {
						logger.info("file size and original name"
								+ mpf.getSize() + ":"
								+ mpf.getOriginalFilename());
						String fileName = employeeAppraisalService
								.uploadFile(mpf);
						logger.info("dummy file name is "+fileName);
						System.out.println("dummy file name is "+fileName);

						myDto.setEmployeeDummyFileName(fileName);
						myDto.setEmployeeFileName(mpf.getOriginalFilename());
					}
				}
			} catch (Exception e) {
				logger.error(
						"Exception in VisitorMessageController while upload file in Multipart"
								+ e.getMessage(), e);
			}
		}

		employeeAppraisalService.saveKPIRating(myDto);
		logger.info("final dto object for emp is :...."+myDto.getEmployeeFileName()+"-- "+myDto.getEmployeeDummyFileName()+"-- "+myDto.getManagerFileName()+"-- "+myDto.getManagerDummyFileName());
	}

	@RequestMapping(value = "/saveKPIRatingByManager", method = RequestMethod.POST)
	public @ResponseBody
	void saveKPIRatingByManager(MyDTO myDto, MultipartHttpServletRequest request) {
		logger.info("ratingDTO object in manager is: " + myDto.getName());
		logger.info("emp file name at start: "+myDto.getEmployeeFileName());
		logger.info("emp dummy name at start: "+myDto.getEmployeeDummyFileName());
		MultipartFile mpf = null;
		if (request != null) {
			Iterator<String> itr = request.getFileNames();
			try {
				while (itr.hasNext()) {
					String file = itr.next();
					mpf = request.getFile(file);
					if (mpf != null) {
						logger.info("file name and original name"
								+ mpf.getSize() + ":"
								+ mpf.getOriginalFilename());
						
						String fileName = employeeAppraisalService
								.uploadFile(mpf);
						logger.info("dummy file name is "+fileName);
						System.out.println("dummy file name is "+fileName);

						myDto.setManagerDummyFileName(fileName);
						myDto.setManagerFileName(mpf.getOriginalFilename());
						
						
						//employeeAppraisalService.uploadFile(mpf);
						//myDto.setManagerFileName(mpf.getOriginalFilename());
					}
				}
			} catch (Exception e) {
				logger.error(
						"Exception in VisitorMessageController while upload file in Multipart"
								+ e.getMessage(), e);
			}
		}
		logger.info("emp file name : "+myDto.getEmployeeFileName());
		logger.info("emp dummy name : "+myDto.getEmployeeDummyFileName());
		logger.info("*************************************");
		logger.info("mgr file name : "+myDto.getManagerFileName());
		logger.info("mgr dummy file name : "+myDto.getManagerDummyFileName());
		


		employeeAppraisalService.saveKPIRating(myDto);
	}

	@RequestMapping(value = "/downloadFile", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody
	void downloadFile(HttpServletResponse response, String fileName) {
		logger.info("inside download file method in controller...");
		employeeAppraisalService.downloadFile(response, fileName);
	}

	@RequestMapping(value = "/deleteKpiRating", method = RequestMethod.POST)
	public @ResponseBody
	void deleteKpiRating(@RequestBody KPIRatingDTO ratingDTO) {
		employeeAppraisalService.deleteKpiRating(ratingDTO);
	}

	@RequestMapping(value = "/submitEmployeeAppraisal", params = { "employeeId" }, method = RequestMethod.POST)
	public @ResponseBody
	void submitEmployeeAppraisal(Long employeeId) {
		employeeAppraisalService.submitEmployeeAppraisal(employeeId);
	}

	@RequestMapping(value = "/acknowledgeAppraisal", params = { "employeeId" }, method = RequestMethod.POST)
	public @ResponseBody
	void acknowledgeAppraisal(Long employeeId) {
		employeeAppraisalService.acknowledgeAppraisal(employeeId);
	}
}
