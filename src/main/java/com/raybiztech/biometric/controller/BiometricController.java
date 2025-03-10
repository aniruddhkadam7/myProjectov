package com.raybiztech.biometric.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.biometric.dto.BioAttendanceDTO;
import com.raybiztech.biometric.service.BiometricService;

@Controller
@RequestMapping("/biometric")
public class BiometricController {

	@Autowired
	BiometricService biometricService;

	@RequestMapping(value = "/attendance", params = { "employeeId", "start",
			"end" }, method = RequestMethod.GET)
	public @ResponseBody SortedSet<BioAttendanceDTO> attendance(
			Long employeeId, String start, String end,
			HttpServletResponse httpServletResponse) {

		return biometricService.getEmployeeAttendance(employeeId, start, end);
	}

	@RequestMapping(value = "/bioAdminManager/attendance", params = {
			"employeeId", "month", "year", "startIndex", "endIndex", "status",
			"search" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> attendanceForAdminManager(
			Long employeeId, Integer month, Integer year, Integer startIndex,
			Integer endIndex, String status, HttpServletResponse value,
			String shiftid, String search) {
		return biometricService.getAllEmployeeAttendance(employeeId, month,
				year, startIndex, endIndex, status, shiftid, search);

	}

	@RequestMapping(value = "/exportAttendance", params = { "employeeId",
			"month", "year", "status", "search" }, method = RequestMethod.GET)
	@ResponseBody
	public void exportAttendance(@RequestParam Long employeeId,
			@RequestParam Integer month, @RequestParam Integer year,
			@RequestParam String status, @RequestParam String shiftid,
			@RequestParam String search, HttpServletResponse httpServletResponse)
			throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"Attendance.csv\"");
		ByteArrayOutputStream os = biometricService.exportBioAttendance(
				employeeId, month, year, status, shiftid, search);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

	@RequestMapping(value = "/holidayAttendance", params = { "start", "end" }, method = RequestMethod.GET)
	public @ResponseBody SortedSet<BioAttendanceDTO> holidayAttendance(
			String start, String end) {

		return biometricService.getHolidayAttendance(start, end);

	}

	@RequestMapping(value = "/timeInOffice", params = { "employeeId", "start",
			"end" }, method = RequestMethod.GET)
	public @ResponseBody Set<BioAttendanceDTO> getEmployeeTimeInOffice(
			Long employeeId, String start, String end, HttpServletResponse value) {

		// here for aop purpose getting employee in Long format and as per code
		// converting it to string and sending to futher flow
		String empid = String.valueOf(employeeId);
		return biometricService.getEmployeeTimeInOffice(empid, start, end);
	}

	@RequestMapping(value = "/hiveTimeInCalender", params = { "employeeId",
			"start", "end" }, method = RequestMethod.GET)
	public @ResponseBody List<BioAttendanceDTO> getMonthlyHiveReportForEmployee(
			Long employeeId, String start, String end,
			HttpServletResponse httpServletResponse) {

		// here for aop purpose getting employee in Long format and as per code
		// converting it to string and sending to futher flow
		String empid = String.valueOf(employeeId);

		return biometricService.getMonthlyHiveReportForEmployee(empid, start,
				end);
	}

	@RequestMapping(value = "/bioAdminManager/searchAttendance", params = {
			"employeeId", "month", "year", "startIndex", "endIndex", "search",
			"status","shiftid" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> searchAttendance(Long employeeId,
			Integer month, Integer year, Integer startIndex, Integer endIndex,
			String search, String status,String shiftid) {
		return biometricService.searchEmployeesAttendance(employeeId, month,
				year, startIndex, endIndex, search, status,shiftid);

	}

	@RequestMapping(value = "/bioAdminManager/updateLateReporting", method = RequestMethod.PUT)
	public @ResponseBody void updateLateReporting() {
		biometricService.updateLateReporting();
	}
	
	@RequestMapping(value="/myAttendence", params = { "start", "end","employeeId" },method=RequestMethod.GET)
	public @ResponseBody List<BioAttendanceDTO> myAttendence(String start,String end,Long employeeId)
	{
		return biometricService.myAttendence(start,end,employeeId);
	}
	
	@RequestMapping(value = "/exportAttendance2", params = { "employeeId",
			"month", "year", "status", "search" }, method = RequestMethod.GET)
	@ResponseBody
	public void exportAttendance2(@RequestParam Long employeeId,
			@RequestParam Integer month, @RequestParam Integer year,
			@RequestParam String status, @RequestParam String shiftid,
			@RequestParam String search, HttpServletResponse httpServletResponse)
			throws IOException {
		httpServletResponse.setContentType("text/csv");
		httpServletResponse.setHeader("Content-Disposition",
				"attachment; filename=\"Attendance.csv\"");
		ByteArrayOutputStream os = biometricService.exportAttendance2(
				employeeId, month, year, status, shiftid, search);
		httpServletResponse.getOutputStream().write(os.toByteArray());

	}

}
