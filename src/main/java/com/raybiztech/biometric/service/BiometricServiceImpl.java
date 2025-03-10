package com.raybiztech.biometric.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityTime;
import com.raybiztech.TimeInOffice.business.TimeInOffice;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.manager.dao.ManagerAppraisalDAO;
import com.raybiztech.biometric.builder.BioAttendanceBuilder;
import com.raybiztech.biometric.builder.HolidayAttendanceBuilder;
import com.raybiztech.biometric.business.AttendanceStatus;
import com.raybiztech.biometric.business.BioAttendance;
import com.raybiztech.biometric.dao.BiometricDAO;
import com.raybiztech.biometric.dto.BioAttendanceDTO;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.MonthYear;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.employeeprofile.dao.EmployeeProfileDAOI;
import com.raybiztech.leavemanagement.builder.LeaveCategoryBuilder;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.dto.LeaveDTO;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.recruitment.business.Holidays;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.spentHours.dao.SpentHourDAO;


@Service("biometricService")
public class BiometricServiceImpl implements BiometricService {

	@Autowired
	LeaveManagementUtils leaveManagementUtils;
	@Autowired
	BiometricDAO biometricDAO;
	@Autowired
	BioAttendanceBuilder bioAttendanceBuilder;
	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	EmployeeProfileDAOI employeeProfileDAOIMPL;
	@Autowired
	HolidayAttendanceBuilder holidayAttendanceBuilder;
	@Autowired
	LeaveCategoryBuilder leaveCategoryBuilder;
	@Autowired
	SpentHourDAO spentHourDAO;
	@Autowired
	ManagerAppraisalDAO managerAppraisalDao;
	@Autowired
	DAO dao;
	@Autowired
	ProjectService projectService;
	
	int[] days;
	int [] daysList;
	public static Logger logger = Logger.getLogger(BiometricServiceImpl.class);

	@Override
	@Transactional
	public SortedSet<BioAttendanceDTO> getEmployeeAttendance(Long employeeId,
			String start, String end) {

		DateRange givenDateRange = null;
		DateRange monthPeriod = null;
		try {
			Date fromDate = Date.parse(start, "yyyy-MM-dd");
			Date toDate = Date.parse(end, "yyyy-MM-dd");
			givenDateRange = new DateRange(fromDate, toDate);

		} catch (ParseException e) {
			logger.error(e.getMessage());
		}

		Iterator<Date> itr = givenDateRange.iterator();
		while (itr.hasNext()) {
			Date element = itr.next();
			if (element.getDayOfMonth().getValue() == 17) {
				Date pointDay = element;
				monthPeriod = leaveManagementUtils.constructMonthPeriod(
						pointDay.getMonthOfYear().getValue(), pointDay
								.getYearOfEra().getValue());

			}
		}
		Set<Holidays> holidays = employeeProfileDAOIMPL
				.getHolidays(monthPeriod);

		Set<BioAttendance> bioAttendances = biometricDAO.getAttendances(
				employeeId, monthPeriod);

		return bioAttendanceBuilder.getEmployeeAttendanceDTOSet(bioAttendances,
				holidays);

	}

	@Override
	@Transactional
	public Map<String, Object> getAllEmployeeAttendance(Long employeeId,
			Integer month, Integer year, Integer startIndex, Integer endIndex,
			String status, String shiftid, String search) {
		MonthOfYear priviousMonthOfYear = MonthOfYear.valueOf(month-1);
		MonthOfYear monthOfYear = MonthOfYear.valueOf(month);
		YearOfEra yearOfEra = YearOfEra.valueOf(year);
		
		// Here we are calculating Date from 25 to 24
		DateRange monthPeriod = leaveManagementUtils
				.getMonthPeriod(new MonthYear(monthOfYear, yearOfEra));

		List<LeaveDebit> leaveDebits = biometricDAO
				.getAllLeaveDebites(monthPeriod);

		Map<String, Object> allEmpAttendanceMap = new HashMap<String, Object>();

		Employee employee = biometricDAO.findBy(Employee.class, employeeId);
		Permission attendanceReport = dao.checkForPermission(
				"Attendance Report", employee);
		Permission attendanceReportAll = dao.checkForPermission(
				"Attendance Report All", employee);

		// if ("admin".equalsIgnoreCase(employee.getRole())
		// || "Finance".equalsIgnoreCase(employee.getRole())
		// || "HR".equalsIgnoreCase(employee.getRole())) {
		if (attendanceReport.getView() && attendanceReportAll.getView()) {
			allEmpAttendanceMap = biometricDAO.getStatusPaginatedEmployees(
					startIndex, endIndex, status, shiftid, search);
		} else {
			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());
			allEmpAttendanceMap = biometricDAO
					.getPaginatedReporteesForAttendance(managerIds, startIndex,
							endIndex, status, shiftid, search);

		}

		List<Employee> employees = (List) allEmpAttendanceMap
				.get("employeeList");
		Integer totalNumberOfRecords = (Integer) allEmpAttendanceMap
				.get("size");

		List<EmployeeDTO> employeeDTOs = employeeBuilder
				.leaveReportEmployeeDTOList(employees);

		logger.info("total number of leaves in this months are :"
				+ leaveDebits.size());

		List<LeaveDTO> customLeaveDTOs = new ArrayList<LeaveDTO>();

		for (LeaveDebit debits : leaveDebits) {

			Iterator<Date> itr = debits.getPeriod().iterator();
			while (itr.hasNext()) {
				Date date = itr.next();
				LeaveDTO leaveDTO = new LeaveDTO();
				leaveDTO.setEmployeeDTO(employeeBuilder
						.leaveReportEmployeeDTO(debits.getEmployee()));
				leaveDTO.setLeaveCategoryDTO(leaveCategoryBuilder
						.createLeaveCategoryDTO(debits.getLeaveCategory()));
				leaveDTO.setLeaveDate(date);
				leaveDTO.setStatus(debits.getStatus());
				customLeaveDTOs.add(leaveDTO);

			}

		}

		Set<Holidays> holidays = employeeProfileDAOIMPL
				.getHolidays(monthPeriod);
		List<Date> holidayDates = new ArrayList<Date>();
		for(Holidays holiday: holidays){
			holidayDates.add(holiday.getDate());
		}

		List<EmployeeDTO> allAttendanceDTOList = bioAttendanceBuilder
				.getAllAttendanceDTOSet(
						biometricDAO.getAllEmployeeAttendances(monthPeriod),
						employeeDTOs, customLeaveDTOs, holidays,
						monthPeriod);
		
		
		// Privious Month Date range.
				MonthYear mon = new MonthYear(priviousMonthOfYear, yearOfEra);
				
				int priMin = monthPeriod.getMinimum().getDayOfMonth().getValue();
				int priLast = mon.getLastDay().getDayOfMonth().getValue();
				
				logger.warn(" last month Minimum Date range is = "+priMin+"  Last Day = "+priLast);
				
				// Current Month Date range.
				MonthYear monCurrent = new MonthYear(priviousMonthOfYear, yearOfEra);
						
				int currentMin =1;
				int currentLast = monthPeriod.getMaximum().getDayOfMonth().getValue();
				
				//List<Integer> daysList;
				ArrayList<Integer> daysList1 = new ArrayList<Integer>();
				
				int j =0;
				for(int i = priMin; i<= priLast; i++){
					Integer temp = (Integer)i;
					//daysList[j]=i;
					daysList1.add(temp);
					j++;
				}
				
				for(int i = currentMin; i<= currentLast; i++){
					//daysList[j]=i;
					Integer temp = (Integer)i;
					daysList1.add(temp);
					j++;
				}
						

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("size", totalNumberOfRecords);
		map.put("list", allAttendanceDTOList);
		map.put("days", daysList1);
		return map;

	}

 @Override
	public SortedSet<BioAttendanceDTO> getHolidayAttendance(String start,
			String end) {
		DateRange dateRange = null;
		DateRange monthPeriod = null;
		try {
			Date fromDate = Date.parse(start, "yyyy-MM-dd");
			Date toDate = Date.parse(end, "yyyy-MM-dd");
			dateRange = new DateRange(fromDate, toDate);
		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		Iterator<Date> itr = dateRange.iterator();
		while (itr.hasNext()) {
			Date element = itr.next();
			if (element.getDayOfMonth().getValue() == 17) {
				Date pointDay = element;
				logger.info("point day is :" + pointDay.toString());
				monthPeriod = leaveManagementUtils.constructMonthPeriod(
						pointDay.getMonthOfYear().getValue(), pointDay
								.getYearOfEra().getValue());

			}
		}

		return holidayAttendanceBuilder
				.createHolidayAttendanceDTOSet(employeeProfileDAOIMPL
						.getHolidays(monthPeriod));
	}
	@Override
	public SortedSet<BioAttendanceDTO> getAttendance(String start,
			String end,String country) {
		DateRange dateRange = null;
		DateRange monthPeriod = null;
		try {
			Date fromDate = Date.parse(start, "yyyy-MM-dd");
			Date toDate = Date.parse(end, "yyyy-MM-dd");
			dateRange = new DateRange(fromDate, toDate);
		} catch (ParseException e) {
			logger.info(e.getMessage());
		}
		Iterator<Date> itr = dateRange.iterator();
		while (itr.hasNext()) {
			Date element = itr.next();
			if (element.getDayOfMonth().getValue() == 17) {
				Date pointDay = element;
				logger.info("point day is :" + pointDay.toString());
				monthPeriod = leaveManagementUtils.constructMonthPeriod(
						pointDay.getMonthOfYear().getValue(), pointDay
								.getYearOfEra().getValue());

			}
		}

		return holidayAttendanceBuilder
				.createHolidayAttendanceDTOSET(employeeProfileDAOIMPL
						.getHolidays(monthPeriod),country);
	}

	@Override
	@Transactional
	public SortedSet<BioAttendanceDTO> getEmployeeTimeInOffice(
			String employeeId, String start, String end) {

		DateRange givenDateRange = null;
		DateRange monthPeriod = null;
		try {
			Date fromDate = Date.parse(start, "yyyy-MM-dd");
			Date toDate = Date.parse(end, "yyyy-MM-dd");
			givenDateRange = new DateRange(fromDate, toDate);

		} catch (ParseException e) {
			logger.error(e.getMessage());
		}

		Iterator<Date> itr = givenDateRange.iterator();
		while (itr.hasNext()) {
			Date element = itr.next();
			if (element.getDayOfMonth().getValue() == 17) {
				Date pointDay = element;
				monthPeriod = leaveManagementUtils.constructMonthPeriod(
						pointDay.getMonthOfYear().getValue(), pointDay
								.getYearOfEra().getValue());

			}
		}
		List<TimeInOffice> spentTimeHours = spentHourDAO.getEmployeeSpentHours(
				employeeId, monthPeriod);

		Set<Holidays> holidays = employeeProfileDAOIMPL
				.getHolidays(monthPeriod);
		return bioAttendanceBuilder.getEmployeeTimeInOfficeDTOSet(
				spentTimeHours, holidays);
	}

	@Override
	@Transactional
	public List<BioAttendanceDTO> getMonthlyHiveReportForEmployee(
			String employeeId, String start, String end) {

		DateRange givenDateRange = null;
		DateRange monthPeriod = null;
		try {
			Date fromDate = Date.parse(start, "yyyy-MM-dd");
			Date toDate = Date.parse(end, "yyyy-MM-dd");
			givenDateRange = new DateRange(fromDate, toDate);

		} catch (ParseException e) {
			logger.error(e.getMessage());
		}

		Iterator<Date> itr = givenDateRange.iterator();
		while (itr.hasNext()) {
			Date element = itr.next();
			if (element.getDayOfMonth().getValue() == 17) {
				Date pointDay = element;
				monthPeriod = leaveManagementUtils.constructMonthPeriod(
						pointDay.getMonthOfYear().getValue(), pointDay
								.getYearOfEra().getValue());

			}
		}
		List<EmpoloyeeHiveActivityTime> spentHiveHours = biometricDAO
				.getMonthlyHiveReportForEmployee(employeeId, monthPeriod);
		Set<Holidays> holidays = employeeProfileDAOIMPL
				.getHolidays(monthPeriod);

		return bioAttendanceBuilder.getEmployeeHiveTimeDTOSet(spentHiveHours,
				holidays);
	}

	@Override
	@Transactional
	public Map<String, Object> searchEmployeesAttendance(Long employeeId,
			Integer month, Integer year, Integer startIndex, Integer endIndex,
			String search, String status,String shiftid) {
		MonthOfYear monthOfYear = MonthOfYear.valueOf(month);
		YearOfEra yearOfEra = YearOfEra.valueOf(year);

		DateRange monthPeriod = leaveManagementUtils
				.getMonthPeriod(new MonthYear(monthOfYear, yearOfEra));

		List<LeaveDebit> leaveDebits = biometricDAO
				.getAllLeaveDebites(monthPeriod);

		Map<String, Object> searchEmpMap = null;

		Employee employee = biometricDAO.findBy(Employee.class, employeeId);

		Permission attendanceReport = dao.checkForPermission(
				"Attendance Report", employee);
		Permission attendanceReportAll = dao.checkForPermission(
				"Attendance Report All", employee);

		if (attendanceReport.getView() && attendanceReportAll.getView()) {

			searchEmpMap = biometricDAO.searchEmployees(search, startIndex,
					endIndex, status,shiftid);
		} else {
			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());
			searchEmpMap = biometricDAO.searchReportees(managerIds, search,
					startIndex, endIndex, status,shiftid);

		}

		List<Employee> employees = (List) searchEmpMap.get("searchEmpList");
		Integer noOfSearchedEmp = (Integer) searchEmpMap.get("size");

		List<EmployeeDTO> employeeDTOs = employeeBuilder
				.leaveReportEmployeeDTOList(employees);

		List<LeaveDTO> customLeaveDTOs = new ArrayList<LeaveDTO>();

		for (LeaveDebit debits : leaveDebits) {

			Iterator<Date> itr = debits.getPeriod().iterator();
			while (itr.hasNext()) {
				Date date = itr.next();
				LeaveDTO leaveDTO = new LeaveDTO();
				leaveDTO.setEmployeeDTO(employeeBuilder
						.leaveReportEmployeeDTO(debits.getEmployee()));
				leaveDTO.setLeaveCategoryDTO(leaveCategoryBuilder
						.createLeaveCategoryDTO(debits.getLeaveCategory()));
				leaveDTO.setLeaveDate(date);
				leaveDTO.setStatus(debits.getStatus());
				customLeaveDTOs.add(leaveDTO);

			}

		}

		Set<Holidays> holidays = employeeProfileDAOIMPL
				.getHolidays(monthPeriod);
		List<Date> holidayDates = new ArrayList<Date>();

		for (Holidays holiday : holidays) {
			holidayDates.add(holiday.getDate());

		}
		
		

		List<EmployeeDTO> allAttendanceDTOList = bioAttendanceBuilder
				.getAllAttendanceDTOSet(
						biometricDAO.getAllEmployeeAttendances(monthPeriod),
						employeeDTOs, customLeaveDTOs, holidays,
						monthPeriod);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("size", noOfSearchedEmp);
		map.put("list", allAttendanceDTOList);
		return map;
	}

	@Override
	public void updateLateReporting() {
		List<BioAttendance> attendances = biometricDAO.updateLateReporting();
		logger.warn("List size " + attendances.size());
		for (BioAttendance attendance : attendances) {

			String inTime = attendance.getInTime().toString(
					"yyyy-MM-dd HH:mm:ss");
			java.sql.Timestamp InTime = java.sql.Timestamp.valueOf(inTime);
			String[] s1 = inTime.split(" ");
			String actualtime = timeWithGrace(s1[0], attendance.getEmployee()
					.getTimeSlot().getStartTime(), attendance.getEmployee()
					.getTimeSlot().getGraceTime());
			java.sql.Timestamp actualTime = java.sql.Timestamp
					.valueOf(actualtime);
			if (InTime.after(actualTime)) {

				attendance.setLateReport(true);
			} else {
				attendance.setLateReport(false);
			}
			biometricDAO.update(attendance);
			;
		}
	}

	// This is to check time including grace Period
	public String timeWithGrace(String date, String slot, String graceTime) {
		String[] seprt = slot.split(":");
		int minWithGrace = Integer.valueOf(graceTime)
				+ Integer.parseInt(seprt[1]);
		int exacthr = minWithGrace / 60;
		int exactMin = minWithGrace % 60;
		int hr = Integer.parseInt(seprt[0]) + exacthr;
		hr = hr < 24 ? hr : 24 - hr;
		String hour = String.valueOf(hr).length() < 2 ? "0"
				+ String.valueOf(hr) : String.valueOf(hr);
		String minutes = String.valueOf(exactMin).length() < 2 ? "0"
				+ String.valueOf(exactMin) : String.valueOf(exactMin);

		slot = hour + ":" + minutes;
		String finalTime = date + " " + slot + ":59";

		return finalTime;
	}

	public ByteArrayOutputStream exportBioAttendance(Long employeeId,
			Integer month, Integer year, String status, String shiftid,
			String search) throws IOException {

		Map<String, Object> map = this.getAllEmployeeAttendance(employeeId,
				month, year, null, null, status, shiftid, search);

		List<EmployeeDTO> dtos = (List<EmployeeDTO>) map.get("list");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		// Setting Date for Excell sheet
		MonthOfYear priviousMonthOfYear = MonthOfYear.valueOf(month-1); // Privious month
		MonthOfYear monthOfYear = MonthOfYear.valueOf(month); // Current MOnth
		YearOfEra yearOfEra = YearOfEra.valueOf(year);
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
				
				logger.warn(" Current month Minimum Date range is = "+currentMin+"  Last Day = "+currentLast);
		
		
		
		//for(int i=min;i<=monthPeriod)
		
		

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
			row1.createCell(i).setCellValue(monthPeriod.getMinimum().toString());
			//row1.createCell(i).setCellValue(i - 1);
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
		cell34.setCellValue("Absent");
		cell34.setCellStyle(style);
		
		Cell cell35 = row1.createCell(35);
		cell35.setCellValue("LateComing");
		cell35.setCellStyle(style);
		
		Cell cell36 = row1.createCell(36);
		cell36.setCellValue("Casual");
		cell36.setCellStyle(style);
		
		Cell cell37 = row1.createCell(37);
		cell37.setCellValue("LOP");
		cell37.setCellStyle(style);
		
		Cell cell38 = row1.createCell(38);
		cell38.setCellValue("Holiday");
		cell38.setCellStyle(style);
		
		for (EmployeeDTO dto : dtos) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getId());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getFullName());

			int i = 2;
			for (BioAttendanceDTO attendanceDTO : dto.getBioAttendanceDtoSet()) {
				
				if(attendanceDTO.getAttendanceStatus() != null){
				/*if(attendanceDTO.getAttendanceStatus().equals(AttendanceStatus.A)){
					row.createCell(i).setCellValue("A");
				}*/
					
					/*else if(attendanceDTO.getAttendanceStatus().equals(AttendanceStatus.P)){
					row.createCell(i).setCellValue("P");
				}*/
				if(attendanceDTO.getTitle().contains("-")){
					row.createCell(i).setCellValue("P");
				}
				else if(attendanceDTO.getTitle().equalsIgnoreCase("Absent")){
					row.createCell(i).setCellValue("A");
				}
				else if(attendanceDTO.getTitle().equalsIgnoreCase("C")){
					row.createCell(i).setCellValue("C");
				}
				else if(attendanceDTO.getTitle().equalsIgnoreCase("P")){
					row.createCell(i).setCellValue("C");
				}
				else if(attendanceDTO.getTitle().equalsIgnoreCase("H")){
					row.createCell(i).setCellValue("H");
				}
				else if(attendanceDTO.getTitle().equalsIgnoreCase("L")){
					row.createCell(i).setCellValue("L");
				}
				else{
					row.createCell(i).setCellValue("-");
				}
				}
				else{
					row.createCell(i).setCellValue("-");
				}
				/*row.createCell(i).row.createCell(i).setCellValue(
						attendanceDTO.getTitle() != null ? attendanceDTO
								.getTitle() : "-");(
						attendanceDTO.getTitle() != null ? attendanceDTO
								.getTitle() : "-");*/
				i = i + 1;
			}

			Cell cel34 = row.createCell(34);
			cel34.setCellValue(dto.getAbsentCount());
			
			Cell cel35 = row.createCell(35);
			if(dto.getLateComingCount()!=null) {
			cel35.setCellValue(dto.getLateComingCount());
			}
			else {
				cel35.setCellValue(0);
			}
			
			Cell cel36 = row.createCell(36);
			if(dto.getCasualLeaveCount() != null) {
				cel36.setCellValue(dto.getCasualLeaveCount());
			}
			else {
				cel36.setCellValue(0);
			}
			
			Cell cel37 = row.createCell(37);
			if(dto.getLopLeaveCount() != null) {
				cel37.setCellValue(dto.getLopLeaveCount());
			}
			else {
				cel37.setCellValue(0);
			}
			
			Cell cel38 = row.createCell(38);
			if(dto.getHolidaysCount() != null) {
				cel38.setCellValue(dto.getHolidaysCount());
			}
			else {
				cel38.setCellValue(0);
			}
			
		}

		for (int i = 0; i <= 38; i++) {
			sheet.autoSizeColumn(i);
		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;
	}
	
	@Override
	public List<BioAttendanceDTO> myAttendence(String start,
			String end,Long employeeId) {
		Employee employee=dao.findBy(Employee.class, employeeId);

		List<BioAttendanceDTO> myAttendenceReport = new ArrayList<BioAttendanceDTO>();

		Set<BioAttendanceDTO> empAttendence = this.getEmployeeAttendance(employeeId, start, end);
		
		Set<BioAttendanceDTO> hoildays = this.getAttendance(start, end,employee.getCountry());
		
		Set<BioAttendanceDTO> employeTimeInOffice = this.getEmployeeTimeInOffice(String.valueOf(employeeId), start, end);
	
		List<BioAttendanceDTO> employeeAttendenceHiveReport = this.getMonthlyHiveReportForEmployee(String.valueOf(employeeId), start,end);
		
		
		myAttendenceReport.addAll(empAttendence);
		myAttendenceReport.addAll(hoildays);
		myAttendenceReport.addAll(employeTimeInOffice);
		myAttendenceReport.addAll(employeeAttendenceHiveReport);
		
		
		
		return myAttendenceReport;
	}
	
	public ByteArrayOutputStream exportAttendance2(Long employeeId,
			Integer month, Integer year, String status, String shiftid,
			String search) throws IOException {

		Map<String, Object> map = this.getAllEmployeeAttendance(employeeId,
				month, year, null, null, status, shiftid, search);

		List<EmployeeDTO> dtos = (List<EmployeeDTO>) map.get("list");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		
		// Setting Date for Excell sheet
				MonthOfYear priviousMonthOfYear = MonthOfYear.valueOf(month-1); // Privious month
				MonthOfYear monthOfYear = MonthOfYear.valueOf(month); // Current MOnth
				YearOfEra yearOfEra = YearOfEra.valueOf(year);
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
						
						logger.warn(" Current month Minimum Date range is = "+currentMin+"  Last Day = "+currentLast);
				

		int rowIndex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font blackfont = workbook.createFont();
		blackfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		blackfont.setFontHeightInPoints((short) 10);
		CellStyle style1 = workbook.createCellStyle();
		style1.setAlignment(CellStyle.ALIGN_CENTER);
		style1.setFont(blackfont);
		
		CellStyle style2 = workbook.createCellStyle();
		Font redFont = workbook.createFont();
		redFont.setColor(HSSFColor.RED.index);
		style2.setFont(redFont);
		style2.setAlignment(CellStyle.ALIGN_CENTER);
		
		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Employee ID");
		cell0.setCellStyle(style1);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Name");
		cell1.setCellStyle(style1);

		/*for (Integer i = 2; i <= 32; i++) {
			row1.createCell(i).setCellValue(i - 1);
			row1.getCell(i).setCellStyle(style1);
		}*/
		
		Integer j = 2;
		for(Integer i = priMin; i<= priLast; i++){
			row1.createCell(j).setCellValue(i);
			row1.getCell(j).setCellStyle(style1);
			j++;
		}
		for(Integer i = currentMin; i<= currentLast; i++){
			row1.createCell(j).setCellValue(i);
			row1.getCell(j).setCellStyle(style1);
			j++;
		}


		Cell cell34 = row1.createCell(34);
		cell34.setCellValue("Absent");
		cell34.setCellStyle(style1);
		
		Cell cell35 = row1.createCell(35);
		cell35.setCellValue("LateComing");
		cell35.setCellStyle(style1);
		
		Cell cell36 = row1.createCell(36);
		cell36.setCellValue("Casual/Paid");
		cell36.setCellStyle(style1);
		
		Cell cell37 = row1.createCell(37);
		cell37.setCellValue("LOP");
		cell37.setCellStyle(style1);
		
		Cell cell38 = row1.createCell(38);
		cell38.setCellValue("Holiday");
		cell38.setCellStyle(style1);
		
		for (EmployeeDTO dto : dtos) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getId());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getFullName());

			int i = 2;
			for (BioAttendanceDTO attendanceDTO : dto.getBioAttendanceDtoSet()) {
				
				if(attendanceDTO.getAttendanceStatus() != null){
					if(attendanceDTO.getTitle().equals("Absent")) {
						row.createCell(i).setCellValue("A");
					}
					else {
					row.createCell(i).setCellValue(attendanceDTO.getTitle());
					row.getCell(i).setCellStyle(style1);
					}
					
					if(attendanceDTO.getTitle().equals("Absent") || 
		(attendanceDTO.getLateReport()!= null && attendanceDTO.getLateReport().equals(Boolean.TRUE))){
						row.getCell(i).setCellStyle(style2);
					}
				}
				else{
					row.createCell(i).setCellValue("-");
					row.getCell(i).setCellStyle(style1);
				}
				
				
				i = i + 1;
			}

			Cell cel34 = row.createCell(34);
			cel34.setCellValue(dto.getAbsentCount());
			
			Cell cel35 = row.createCell(35);
			if(dto.getLateComingCount()!=null) {
			cel35.setCellValue(dto.getLateComingCount());
			}
			else {
				cel35.setCellValue(0);
			}
			
			Cell cel36 = row.createCell(36);
			if(dto.getCasualLeaveCount() != null) {
				cel36.setCellValue(dto.getCasualLeaveCount());
			}
			else {
				cel36.setCellValue(0);
			}
			
			Cell cel37 = row.createCell(37);
			if(dto.getLopLeaveCount() != null) {
				cel37.setCellValue(dto.getLopLeaveCount());
			}
			else {
				cel37.setCellValue(0);
			}
			
			Cell cel38 = row.createCell(38);
			if(dto.getHolidaysCount() != null) {
				cel38.setCellValue(dto.getHolidaysCount());
			}
			else {
				cel38.setCellValue(0);
			}
			
		}

		for (int i = 0; i <= 38; i++) {
			sheet.autoSizeColumn(i);
		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;
	}
	
}
