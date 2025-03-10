package com.raybiztech.payslip.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.hrm.security.rest.dao.UserDao;
import com.raybiztech.mailtemplates.util.PayslipMailConfiguration;
import com.raybiztech.payslip.builder.PayslipBuilder;
import com.raybiztech.payslip.business.Payslip;
import com.raybiztech.payslip.dao.PayslipDao;
import com.raybiztech.payslip.dto.PayslipDto;
import com.raybiztech.payslip.dto.PayslipEmpViewDTO;
import com.raybiztech.payslip.dto.PayslipRetrieveDto;
import com.raybiztech.payslip.exceptions.PayslipAlreadyGeneratedException;
import com.raybiztech.payslip.exceptions.PayslipDoesNotCreatedException;
import com.raybiztech.payslip.exceptions.PayslipDoesNotExistException;
import com.raybiztech.payslip.mailNotification.PayslipGeneratedMailNotification;
import com.raybiztech.payslip.utility.ClearDirectory;
import com.raybiztech.payslip.utility.ExcelFileReader;
import com.raybiztech.payslip.utility.FileUploader;
import com.raybiztech.payslip.utility.Jasper;

@Service("payslipServiceImpl")
public class PayslipServiceImpl implements PayslipService {

	@Autowired
	ExcelFileReader excelFileReader;
	@Autowired
	PropBean propBean;
	@Autowired
	PayslipBuilder payslipBuilder;
	@Autowired
	PayslipDao payslipDaoImpl;
	@Autowired
	ClearDirectory clearDirectory;
	@Autowired
	DAO dao;
	@Autowired
	UserDao userDao;
	@Autowired
	Jasper jasper;
	@Autowired
	PayslipMailConfiguration payslipMailConfiguration;

	String path;

	Logger logger = Logger.getLogger(PayslipServiceImpl.class);

	@Transactional
	@Override
	public List<PayslipDto> getExcelDetails(MultipartFile multipartFile)
			throws IOException {

		clearDirectory.clearPayslipDirectory();

		FileUploader fileUploader = new FileUploader();
		path = fileUploader.uploadFile(multipartFile, propBean);

		List<PayslipDto> listAfterReadingExcelFile = null;
		listAfterReadingExcelFile = excelFileReader.readExcelFile(path);

		return listAfterReadingExcelFile;

	}

	@SuppressWarnings("unused")
	@Transactional
	@Override
	@CacheEvict(value = "payslips", allEntries = true)
	public List<PayslipDto> saveExcelFile(final String month, final String year)
			throws IOException {

		/*
		 * Boolean dataExists = payslipDaoImpl.checkDataExistsForMonthAndYear(
		 * month, year);
		 */

		/* if (!dataExists) { */

		List<PayslipDto> errorList = new ArrayList<PayslipDto>();

		List<PayslipDto> paySlipDtoList = null;
		paySlipDtoList = excelFileReader.readExcelFile(path);

		for (PayslipDto payslipDto : paySlipDtoList) {

			Employee employeeData = dao.findBy(Employee.class,
					payslipDto.getEmployeeId());

			if (employeeData == null) {
				errorList.add(payslipDto);
			}

		}

		if (errorList.isEmpty()) {

			for (final PayslipDto payslipDto : paySlipDtoList) {

				Payslip ExistingPayslip = payslipDaoImpl
						.checkDataExistForMonthYearAndEmployeeId(month, year,
								payslipDto.getEmployeeId());

				// if payslip object already exist update it or else insert
				// new
				// payslip object

				if (ExistingPayslip != null) {
					Payslip payslip = payslipBuilder.toEntity(payslipDto,
							month, year, ExistingPayslip);
					dao.update(payslip);

				} else {

					Payslip payslip = payslipBuilder.toEntity(payslipDto,
							month, year, new Payslip());
					dao.save(payslip);

				}
				// here Sending mail for employee saying mail send for
				// paticular
				// employee

				if (payslipDto != null) {

					payslipMailConfiguration
							.sendPayslipGeneratedMailNotificationToEmployee(
									payslipDto, month, year);

					/*
					 * MailNotificationForPayslip
					 * .sendPayslipGeneratedMailNotificationToEmployee(
					 * payslipDto, month, year);
					 */

				}
			}
		}

		return errorList;

		/*
		 * } else { throw new MonthAndYearAlreadyExistsException(); }
		 */
	}

	// generate payslips

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	@Cacheable("payslips")
	public Map<String, Object> getAllPayslipByMonthOfYear(String month,
			String year, int startIndex, int endIndex) {

		Map<String, Object> listofPayslipByMonthAndYear = payslipDaoImpl
				.getAllPayslipByMonthOfYear(month, year, startIndex, endIndex);

		List<PayslipRetrieveDto> payslipRetrieveDtos = new ArrayList<PayslipRetrieveDto>();

		List<Payslip> payslipEntity = new ArrayList<Payslip>();

		payslipEntity = (List<Payslip>) listofPayslipByMonthAndYear.get("list");
		int size = (Integer) listofPayslipByMonthAndYear.get("size");

		Map<String, Object> map = new HashMap<String, Object>();

		for (Payslip payslip : payslipEntity) {
			PayslipRetrieveDto payslipRetrieveDto = payslipBuilder
					.toDto(payslip);
			payslipRetrieveDtos.add(payslipRetrieveDto);
		}
		map.put("size", size);
		map.put("list", payslipRetrieveDtos);

		return map;

	}

	@Transactional
	@Override
	@CacheEvict(value = "payslips", allEntries = true)
	public void updatePayslip(PayslipRetrieveDto payslipRetrieveDto) {

		Payslip payslip = payslipBuilder.toEditEntity(payslipRetrieveDto);

		dao.update(payslip);
	}

	@Transactional
	@Override
	@CacheEvict(value = "payslips", allEntries = true)
	public void deletePayslip(Long id) {

		Payslip payslip = dao.findBy(Payslip.class, id);
		dao.delete(payslip);

	}

	@Transactional
	@Override
	@CacheEvict(value = "payslips", allEntries = true)
	public void deleteCheckedPayslips(List<Long> id) {

		payslipDaoImpl.deleteCheckedPayslips(id);
	}

	@Transactional
	@Override
	@CacheEvict(value = "payslips", allEntries = true)
	public void generatePayslip(HttpServletResponse httpServletResponse,
			String month, String year) {

		List<Payslip> allpayslipsList = payslipDaoImpl.getallPayslip(month,
				year);

		for (final Payslip payslip : allpayslipsList) {

			Boolean status = payslip.getStatus();
			final PayslipRetrieveDto payslipRetrieveDto = payslipBuilder
					.toDto(payslip);

			if (status.equals(true)) {
				throw new PayslipAlreadyGeneratedException();
			}

			else {
				String path = (String) propBean.getPropData().get(
						"jasperReports");
				try {
					final InputStream inputStream = new FileInputStream(path
							+ "Salary.jrxml");
					try {
						Jasper.genratepdf(payslipRetrieveDto, inputStream,
								propBean);

					} catch (JRException e) {

						e.printStackTrace();
					}

					payslip.setPayslipFileName(payslipRetrieveDto
							.getEmployeeId()
							+ "_"
							+ payslipRetrieveDto.getMonth()
							+ "_"
							+ payslipRetrieveDto.getYear() + ".pdf");

					dao.update(payslip);

				} catch (FileNotFoundException exce) {
					throw new PayslipDoesNotCreatedException(
							"payslip does not created");
				}
			}

		}

	}

	@Transactional
	@Override
	public void downloadPayslip(HttpServletResponse response, String empid,
			String month, String year) throws IOException {

		String filePath = (String) propBean.getPropData().get("payslips");
		String fileName = empid + "_" + month + "_" + year + ".pdf";

		String fileNameWithPath = filePath + fileName;

		InputStream fileInputStream = null;
		PrintWriter printWriter = null;
		try {

			response.setHeader("Content-Disposition", "attachment;filename="
					+ "\"" + fileName + "\"");
			response.setContentType("application/pdf");
			File downloadFile = new File(fileNameWithPath);

			if (downloadFile.exists()) {
				fileInputStream = new FileInputStream(downloadFile);
				OutputStream outputStream = null;

				outputStream = response.getOutputStream();
				IOUtils.copy(fileInputStream, outputStream);

				// this is to delete payslips once it is downloaded
				downloadFile.delete();
			} else {
				throw new UnauthorizedUserException("Unauthorised Attempt");
			}

		} catch (FileNotFoundException exception) {

		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
			if (printWriter != null) {
				printWriter.flush();
				printWriter.close();
			}
		}
	}

	@Transactional
	@Override
	public boolean checkPayslipExists(HttpServletResponse response,
			String empid, String month, String year) {

		try {
			return payslipDaoImpl.checkPayslipExist(month, year, empid);
		} catch (PayslipDoesNotExistException doesNotExist) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return false;

	}

	@Transactional
	@Override
	public List<PayslipEmpViewDTO> getEmployeePayslipsForSelectedYear(
			String empid, String year) {

		List<Object[]> payslipMonthList = payslipDaoImpl
				.getEmployeePayslipsForSelectedYear(empid, year);

		return payslipBuilder.getEmpPayslipViewList(payslipMonthList);
	}

	@Transactional
	@Override
	public List<PayslipRetrieveDto> getPayslipDataForViewToEmployee(
			String empid, String month, String year) {
		List<Payslip> employeeViewList = null;
		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		Employee emplo = dao.findByEmployeeName(Employee.class, userName);

		employeeViewList = payslipDaoImpl.getPayslipDataForViewToEmployee(
				empid, month, year);

		return payslipBuilder.getDTOList(employeeViewList);

	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public Map<String, Object> getSearchEmployees(String searchStringCand,
			String month, String year, Integer startIndex, Integer endIndex) {

		Map<String, Object> payslipList = payslipDaoImpl.searchEmployeeDetails(
				searchStringCand, month, year, startIndex, endIndex);

		Integer totalRecords = (Integer) payslipList.get("size");

		List<Payslip> payslipEntity = new ArrayList<Payslip>();
		payslipEntity = (List<Payslip>) payslipList.get("list");

		List<PayslipRetrieveDto> payslipRetrieveDtos = new ArrayList<PayslipRetrieveDto>();

		for (Payslip payslip : payslipEntity) {
			PayslipRetrieveDto payslipRetrieveDto = payslipBuilder
					.toDto(payslip);
			payslipRetrieveDtos.add(payslipRetrieveDto);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("size", totalRecords);
		map.put("list", payslipRetrieveDtos);

		return map;
	}

	@Transactional
	@Override
	public void generatePayslipandDownload(
			HttpServletResponse httpServletResponse, String month, String year,
			String empid, HttpServletRequest httpServletRequest) throws JRException {

		String tenant = httpServletRequest.getHeader("tenantKey");
		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		Employee emplo = dao.findByEmployeeName(Employee.class, userName);

		Payslip ExistingPayslip = payslipDaoImpl
				.checkDataExistForMonthYearAndEmployeeId(month, year,
						Long.parseLong(empid));

		PayslipRetrieveDto payslipRetrieveDto = payslipBuilder
				.toDto(ExistingPayslip);

		String path = (String) propBean.getPropData().get("jasperReports");
		try {
			 InputStream inputStream = null;
			 if(tenant.equalsIgnoreCase("RAYBIZTECH")){
				 inputStream =  new FileInputStream(path
							+ "Salary.jrxml");
			 }
			 else if(tenant.equalsIgnoreCase("AIBRIDGEML")){
				 inputStream =  new FileInputStream(path
							+ "AISalary.jrxml");
			 }

			try {

				Jasper.genratepdf(payslipRetrieveDto, inputStream, propBean);

			} catch (JRException e) {

				e.printStackTrace();
			}

			ExistingPayslip.setPayslipFileName(payslipRetrieveDto
					.getEmployeeId()
					+ "_"
					+ payslipRetrieveDto.getMonth()
					+ "_" + payslipRetrieveDto.getYear() + ".pdf");

			dao.update(ExistingPayslip);

		} catch (FileNotFoundException exce) {
			throw new PayslipDoesNotCreatedException("payslip does not created");
		}

	}

}
