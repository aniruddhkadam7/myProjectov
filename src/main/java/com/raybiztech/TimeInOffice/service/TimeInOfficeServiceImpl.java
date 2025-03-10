/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeInOffice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

import com.raybiztech.TimeInOffice.builder.TimeInOfficeBuilder;
import com.raybiztech.TimeInOffice.business.TimeInOffice;
import com.raybiztech.TimeInOffice.dao.TimeInOfficeDAO;
import com.raybiztech.TimeInOffice.dto.SpentHoursDTO;
import com.raybiztech.TimeInOffice.dto.TimeInOfficeDTO;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.manager.dao.ManagerAppraisalDAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.biometric.dao.BiometricDAO;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.MonthYear;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;

/**
 * 
 * @author hari
 */
@Service("timeInOfficeService")
public class TimeInOfficeServiceImpl implements TimeInOfficeService {

	@Autowired
	TimeInOfficeDAO timeInOfficeDAO;
	@Autowired
	BiometricDAO biometricDAO;
	@Autowired
	TimeInOfficeBuilder timeInOfficeBuilder;
	@Autowired
	ManagerAppraisalDAO managerAppraisalDao;

	@Autowired
	SecurityUtils utils;
	
	@Autowired
	LeaveManagementUtils leaveManagementUtils;

	Logger logger = Logger.getLogger(TimeInOfficeServiceImpl.class);

	@Override
	public TimeInOfficeDTO getTimeInOfficeEmployeeReport(String loggedInEmployeeId, String date) {
		return timeInOfficeDAO.getTimeInOfficeEmployeeReport(loggedInEmployeeId, date);
	}

	@Override
	public Map<String, Object> searchTimeInOffice(String loggedInEmployeeId, String date, Integer startIndex,
			Integer endIndex, String search) {
		Map<String, Object> map = new HashMap<String, Object>();

		Employee employee = biometricDAO.findBy(Employee.class, Long.valueOf(loggedInEmployeeId));
		Map<String, Object> timeInOfcMap = timeInOfficeDAO.searchTimeInOffice(employee, date, startIndex, endIndex,
				search);
		List<TimeInOfficeDTO> timeInOfficeDTOs = (List) timeInOfcMap.get("list");

		Integer totalRecords = (Integer) timeInOfcMap.get("size");
		map.put("list", timeInOfficeDTOs);
		map.put("size", totalRecords);
		return map;
	}

	@Cacheable("timeInOfficeCache")
	@Override
	public Map<String, Object> getTimeInOfficeManagerReport(String loggedInEmployeeId, String date, Integer startIndex,
			Integer endIndex) {
		logger.info("GetTimeInOfficeManagerReport method called.........");
		Map<String, Object> map = new HashMap<String, Object>();
		Employee employee = biometricDAO.findBy(Employee.class, Long.valueOf(loggedInEmployeeId));

		Map<String, Object> tInOfcMap = timeInOfficeDAO.getTimeInOfficeManagerReport(employee, date, startIndex,
				endIndex);
		
		List<TimeInOfficeDTO> timeInOfficeDTOs = (List) tInOfcMap.get("list");
		ArrayList<Integer> daysList = (ArrayList<Integer>) tInOfcMap.get("daysList");
		Integer totalRecords = (Integer) tInOfcMap.get("size");
		map.put("list", timeInOfficeDTOs);
		map.put("size", totalRecords);
		map.put("dayList", daysList);
		return map;
	}

	@Transactional
	@Override
	public List<TimeInOfficeDTO> getweeklyTimeInOfficeReport(String loggedInEmployeeId) {
		// TODO Auto-generated method stub

		List<TimeInOffice> timeInOfficeList = timeInOfficeDAO.getweeklyTimeInOfficeList(loggedInEmployeeId);

		Collections.sort(timeInOfficeList);

		List<TimeInOfficeDTO> TimeInOfficeDTOList = timeInOfficeBuilder.convertEntityListToDTOList(timeInOfficeList);

		return TimeInOfficeDTOList;
	}

	@Override
	public ByteArrayOutputStream exportAttendanceData(String hiveDate, String search) throws IOException {
		logger.warn("at line no:124" + search);
		Employee loggedEmployee = (Employee) utils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		List<TimeInOfficeDTO> timeInOfficeDTOs;
		List<Integer> daylist;
		

		Map<String, Object> map = new HashMap<String, Object>();

		if (search == null) {
			Map<String, Object> tInOfcMap = timeInOfficeDAO.getTimeInOfficeManagerReport(loggedEmployee, hiveDate, null,
					null);

			timeInOfficeDTOs = (List) tInOfcMap.get("list");
			daylist = (List) tInOfcMap.get("daysList");
		} else {

			Map<String, Object> timeInOfcMap = timeInOfficeDAO.searchTimeInOffice(loggedEmployee, hiveDate, null, null,
					search);

			timeInOfficeDTOs = (List) timeInOfcMap.get("list");

		}
		
		String h[] = hiveDate.split("/");
		MonthOfYear priviousMonthOfYear = MonthOfYear.valueOf((Integer.parseInt(h[0])-1));
		MonthOfYear monthOfYear = MonthOfYear.valueOf(Integer.parseInt(h[0]));
		YearOfEra yearOfEra = YearOfEra.valueOf(Integer.parseInt(h[1]));
 
		DateRange monthPeriod = leaveManagementUtils
				.getMonthPeriod(new MonthYear(monthOfYear, yearOfEra));
		
		// Privious Month Date range.
		MonthYear mon = new MonthYear(priviousMonthOfYear, yearOfEra);
		
		int priMin = monthPeriod.getMinimum().getDayOfMonth().getValue();
		int priLast = mon.getLastDay().getDayOfMonth().getValue();
		
		logger.warn(" last month Minimum Date range is = "+priMin+"  Last Day = "+priLast);
		
		// Current Month Date range.
		MonthYear monCurrent = new MonthYear(priviousMonthOfYear, yearOfEra);
				
		int currentMin =1;
		int currentLast = monthPeriod.getMaximum().getDayOfMonth().getValue();
		

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

		/*for (Integer i = 2; i <= 32; i++) {
			row1.createCell(i).setCellValue(i - 1);
			row1.getCell(i).setCellStyle(style);
		}*/
		
		Integer j = 2;
		for(Integer i = priMin; i<= priLast; i++){
			row1.createCell(j).setCellValue(i);
			row1.getCell(j).setCellStyle(style);
			j++;
		}
		for(Integer i = currentMin; i<= currentLast; i++){
			row1.createCell(j).setCellValue(i);
			row1.getCell(j).setCellStyle(style);
			j++;
		}

		Cell cell34 = row1.createCell(34);
		cell34.setCellValue("Total");
		cell34.setCellStyle(style);

		for (TimeInOfficeDTO dto : timeInOfficeDTOs) {
			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getEmpID());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getEmpName());
/*
			if (dto.getInOfficeDTOs() != null) {

				Collections.sort(dto.getInOfficeDTOs(), new Comparator<SpentHoursDTO>() {
					@Override
					public int compare(SpentHoursDTO o1, SpentHoursDTO o2) {

						return o1.getDayOfMonth().compareTo(o2.getDayOfMonth());
					}
				});

			}*/

			int i = 2;
			for (SpentHoursDTO list : dto.getInOfficeDTOs()) {
				row.createCell(i).setCellValue(list.getSpentHours());
				i = i + 1;
			}
			Cell cel34 = row.createCell(34);
			cel34.setCellValue(dto.getTotalSpentHours());

		}

		sheet.autoSizeColumn(1);

		workbook.write(bos);
		bos.flush();
		bos.close();

		return bos;
	}
}
