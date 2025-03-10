package com.raybiztech.payslip.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.payslip.dto.PayslipDto;
import com.raybiztech.payslip.dto.PayslipEmpViewDTO;
import com.raybiztech.payslip.dto.PayslipRetrieveDto;
import com.raybiztech.payslip.exceptions.MonthAndYearAlreadyExistsException;
import com.raybiztech.payslip.exceptions.PayslipAlreadyGeneratedException;
import com.raybiztech.payslip.exceptions.PayslipDoesNotCreatedException;
import com.raybiztech.payslip.exceptions.PayslipDoesNotExistException;
import com.raybiztech.payslip.service.PayslipService;
import com.raybiztech.payslip.utility.ClearDirectory;
import com.raybiztech.recruitment.service.JobPortalService;

@Controller
@RequestMapping("/payslip")
public class PayslipController {

	@Autowired
	PayslipService payslipServiceImpl;
	@Autowired
	ClearDirectory clearDirectory;
	@Autowired
	JobPortalService jobPortalServiceImpl;
	
	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(PayslipController.class);

	@RequestMapping(value = "/payrolladmin/readExcelFile", method = RequestMethod.POST)
	public @ResponseBody List<PayslipDto> readExcelFile(
			MultipartHttpServletRequest request, HttpServletResponse response)
			throws IOException {

		List<PayslipDto> payslipDtos = null;

		Iterator<String> iterator = request.getFileNames();

		while (iterator.hasNext()) {
			String file = iterator.next();
			MultipartFile multipartFile = request.getFile(file);

			payslipDtos = payslipServiceImpl.getExcelDetails(multipartFile);

		}

		return payslipDtos;

	}

	@RequestMapping(value = "/payrolladmin/saveExcelFile", params = { "month",
			"year" }, method = RequestMethod.POST)
	public @ResponseBody List<PayslipDto> saveExcelFile(String month,
			String year, HttpServletResponse httpServletResponse)
			throws Exception {
		Map<String,Object> loggedEmp=securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee e=(Employee) loggedEmp.get("employee");
		
		
		  if(e.getRole().equalsIgnoreCase("admin") || e.getRole().equalsIgnoreCase("Finance")
		            || e.getRole().equalsIgnoreCase("Finance Manager"))
		  {
		List<PayslipDto> errorList = new ArrayList<PayslipDto>();
		try {
			errorList = payslipServiceImpl.saveExcelFile(month, year);
		} catch (MonthAndYearAlreadyExistsException alreadyExists) {
			httpServletResponse.setStatus(HttpServletResponse.SC_SEE_OTHER);

		}
		return errorList;
		  }
		  else
		  {
			  httpServletResponse.setStatus(httpServletResponse.SC_UNAUTHORIZED);
	          return null;
		  }
	}

	@RequestMapping(value = "/payrolladmin/clearDirectory", method = RequestMethod.POST)
	public @ResponseBody void clearDirectory() throws IOException {
		clearDirectory.clearPayslipDirectory();

	}

	@RequestMapping(value = "/payrolladmin/downloadExcelFile", params = { "fileName" }, method = RequestMethod.GET)
	public @ResponseBody void downloadFile(HttpServletResponse response,
			String fileName) {

		jobPortalServiceImpl.downloadFile(response, fileName);
	}

	// generate payslip

	@RequestMapping(value = "/payrolladmin/getCurrentPayslip", method = RequestMethod.GET, params = {
			"month", "year", "startIndex", "endIndex" })
	public @ResponseBody Map<String, Object> getCurrentPayslip(
			@RequestParam String month, String year, int startIndex,
			int endIndex,HttpServletResponse httpServletResponse) {

		Map<String,Object> loggedEmp=securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee e=(Employee) loggedEmp.get("employee");
		
		
		  if(e.getRole().equalsIgnoreCase("admin") || e.getRole().equalsIgnoreCase("Finance")
		            || e.getRole().equalsIgnoreCase("Finance Manager"))
		  {
			  return payslipServiceImpl.getAllPayslipByMonthOfYear(month, year,
		                startIndex, endIndex);
		           
		  }
		  else{
			  httpServletResponse.setStatus(httpServletResponse.SC_UNAUTHORIZED);
	          return null;
		  }
		       
	}

	@RequestMapping(value = "/payrolladmin/updatePayslip", method = RequestMethod.POST)
	public @ResponseBody void updatePayslip(
			@RequestBody PayslipRetrieveDto payslipRetrieveDto) {
		payslipServiceImpl.updatePayslip(payslipRetrieveDto);
	}

	@RequestMapping(value = "/payrolladmin/deletePayslip", method = RequestMethod.DELETE, params = { "paySlipId" })
	public @ResponseBody void deletePayslip(@RequestParam Long paySlipId) {
		payslipServiceImpl.deletePayslip(paySlipId);
	}

	@RequestMapping(value = "/payrolladmin/deleteCheckedPayslips", method = RequestMethod.DELETE, params = { "paySlipId" })
	public @ResponseBody void deleteCheckedPayslips(
			@RequestParam List<Long> paySlipId) {
		payslipServiceImpl.deleteCheckedPayslips(paySlipId);
	}

	@RequestMapping(value = "/payrolladmin/generatePayslip", method = RequestMethod.GET, params = {
			"month", "year" })
	public @ResponseBody void generatePayslip(@RequestParam String month,
			String year, HttpServletResponse httpServletResponse)
			throws FileNotFoundException {

		try {
			payslipServiceImpl
					.generatePayslip(httpServletResponse, month, year);
		} catch (PayslipAlreadyGeneratedException alreadyGenerated) {
			httpServletResponse.setStatus(HttpServletResponse.SC_FOUND);

		} catch (PayslipDoesNotCreatedException doesNotCreatedException) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		} catch (JRException e) {

			e.printStackTrace();
		}

	}

	@RequestMapping(value = "/checkpayslipExists", params = { "month", "year",
			"empId" }, method = RequestMethod.GET)
	public @ResponseBody boolean checkPayslipExists(
			HttpServletResponse response, String month, String year,
			String empId)

	{
		return payslipServiceImpl.checkPayslipExists(response, empId, month,
				year);
	}

	@RequestMapping(value = "/downloadPayslip", params = { "month", "year",
			"empId" }, method = RequestMethod.GET)
	public @ResponseBody void downloadPayslip(HttpServletResponse response,
			String month, String year, Long empId) throws IOException {

		try {
			payslipServiceImpl.downloadPayslip(response, String.valueOf(empId),
					month, year);
		} catch (PayslipDoesNotExistException doesNotExist) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@RequestMapping(value = "/getEmployeePayslipsForSelectedYear", params = {
			"year", "empId" }, method = RequestMethod.GET)
	public @ResponseBody List<PayslipEmpViewDTO> getEmployeePayslipsForSelectedYear(
			String year, Long empId, HttpServletResponse httpServletResponse) {

		// here above in parameters making empid of Long Type because using that
		// param in aop method

		return payslipServiceImpl.getEmployeePayslipsForSelectedYear(
				String.valueOf(empId), year);

	}

	@RequestMapping(value = "/getPayslipDataForView", params = { "month",
			"year", "empId" }, method = RequestMethod.GET)
	public @ResponseBody List<PayslipRetrieveDto> getPayslipDataForViewToEmployee(
			String month, String year, Long empId, HttpServletResponse value) {

		// here above in parameters making empid of Long Type because using that
		// param in aop method

		return payslipServiceImpl.getPayslipDataForViewToEmployee(
				String.valueOf(empId), month, year);

	}

	@RequestMapping(value = "/payrolladmin/searchEmployee", params = {
			"searchStringCand", "startIndex", "endIndex", "month", "year" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchEmployee(
			String searchStringCand, String month, String year,
			Integer startIndex, Integer endIndex) {

		return payslipServiceImpl.getSearchEmployees(searchStringCand, month,
				year, startIndex, endIndex);

	}

	@RequestMapping(value = "/payrolladmin/generatePayslipAndDownloadPayslip", method = RequestMethod.GET, params = {
			"month", "year", "empId" })
	public @ResponseBody void generatePayslipandDownloadPayslip(
			@RequestParam String month, String year, Long empId,
			HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest)
			throws FileNotFoundException {

		// here above in parameters making empid of Long Type because using that
		// param in aop method

		try {
			payslipServiceImpl.generatePayslipandDownload(httpServletResponse,
					month, year, String.valueOf(empId),httpServletRequest);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}