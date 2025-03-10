/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeActivity.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.TimeActivity.builder.HiveTimeActivityBuilder;
import com.raybiztech.TimeActivity.business.EmpoloyeeHiveActivity;
import com.raybiztech.TimeActivity.dao.TimeActivityDAO;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityDTO;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityReport;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityTime;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.manager.dao.ManagerAppraisalDAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.biometric.dao.BiometricDAO;
import com.raybiztech.biometric.dto.BioAttendanceDTO;
import com.raybiztech.date.Date;

/**
 * 
 * @author naresh
 */
@Service("timeActivityService")
@Transactional
public class TimeActivityServiceImpl implements TimeActivityService {

	private Logger logger = Logger.getLogger(TimeActivityServiceImpl.class);
	@Autowired
	TimeActivityDAO timeActivityDAO;
	@Autowired
	ManagerAppraisalDAO managerAppraisalDao;
	@Autowired
	BiometricDAO biometricDAO;
	@Autowired
	DAO dao;
	@Autowired
	HiveTimeActivityBuilder hiveTimeActivityBuilder;
	@Autowired
	SecurityUtils utils;

	@Override
	public List<EmpoloyeeHiveActivityDTO> getEmployeeHiveTimeActivities(
			String userName) {
		List<EmpoloyeeHiveActivity> activityDTOList = timeActivityDAO
				.getEmployeeHiveTimeActivities(userName);
		return hiveTimeActivityBuilder.convertEntityToDTO(activityDTOList);
	}

	@Override
	public List<EmpoloyeeHiveActivityDTO> getEmployeeDayHiveSheet(
			String projectDate, String userName) {
		List<EmpoloyeeHiveActivity> activityDTOList = timeActivityDAO
				.getEmployeeDayHiveSheet(projectDate, userName);
		return hiveTimeActivityBuilder.convertEntityToDTO(activityDTOList);
	}

	@Cacheable("hiveCache")
	@Override
	public Map<String, Object> getMonthlyHiveReportForManager(String hiveDate,
			String loggedInEmpId, Integer startIndex, Integer endIndex) {
		logger.info("hive activity method called.");
		Map<String, Object> map = new HashMap<String, Object>();
		Employee employee = biometricDAO.findBy(Employee.class,
				Long.valueOf(loggedInEmpId));
		Map<String, Object> hiveReportMap = timeActivityDAO
				.getMonthlyHiveReportForManager(hiveDate, employee, startIndex,
						endIndex);
		List<EmpoloyeeHiveActivityReport> activityDTOList = (List) hiveReportMap
				.get("list");
		Integer noOfRecords = (Integer) hiveReportMap.get("size");
		map.put("list", activityDTOList);
		map.put("size", noOfRecords);

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ByteArrayOutputStream exportBioAttendance(String hiveDate)
			throws IOException {

		Employee loggedEmployee = (Employee) utils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Map<String, Object> hiveReportMap = timeActivityDAO
				.getMonthlyHiveReportForManager(hiveDate, loggedEmployee, null,
						null);

		List<EmpoloyeeHiveActivityReport> activityDTOList = (List) hiveReportMap
				.get("list");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int rowIndex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Employee ID");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Name");
		cell1.setCellStyle(style);

		for (Integer i = 2; i <= 32; i++) {
			row1.createCell(i).setCellValue(i - 1);
			row1.getCell(i).setCellStyle(style);
		}

		Cell cell34 = row1.createCell(34);
		cell34.setCellValue("Total");
		cell34.setCellStyle(style);

		for (EmpoloyeeHiveActivityReport dto : activityDTOList) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getId());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getFirstName() + " " + dto.getLastName());

			List<EmpoloyeeHiveActivityTime> activityTimes = dto
					.getActivityTimes();

			if (activityTimes != null) {

				Collections.sort(activityTimes,
						new Comparator<EmpoloyeeHiveActivityTime>() {

							@Override
							public int compare(EmpoloyeeHiveActivityTime o1,
									EmpoloyeeHiveActivityTime o2) {
								return o1.getDayofMonth().compareTo(
										o2.getDayofMonth());
							}
						});

			}

			int i = 2;
			for (EmpoloyeeHiveActivityTime hivedto : activityTimes) {
				row.createCell(i).setCellValue(hivedto.getHours());
				i = i + 1;
			}

			Cell cel34 = row.createCell(34);
			cel34.setCellValue(dto.getTotalHiveTime());

		}

		sheet.autoSizeColumn(1);

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;

	}

	@Override
	public EmpoloyeeHiveActivityReport getMonthlyHiveReportForEmployee(
			String hiveDate, String loggedInEmpId) {
		EmpoloyeeHiveActivityReport activityDTOList = timeActivityDAO
				.getMonthlyHiveReportForEmployee(hiveDate, loggedInEmpId);
		return activityDTOList;
	}

	@Override
	public Map<String, Object> searchHiveTime(String loggedInEmployeeId,
			String date, Integer startIndex, Integer endIndex, String search) {

		Map<String, Object> map = new HashMap<String, Object>();
		Employee employee = biometricDAO.findBy(Employee.class,
				Long.valueOf(loggedInEmployeeId));

		Map<String, Object> searchHiveMap = timeActivityDAO.searchHiveTime(
				date, employee, startIndex, endIndex, search);
		List<EmpoloyeeHiveActivityReport> activityDTOList = (List) searchHiveMap
				.get("list");
		Integer noOfSearhedEmp = (Integer) searchHiveMap.get("size");
		map.put("list", activityDTOList);
		map.put("size", noOfSearhedEmp);

		return map;
	}

}
