package com.raybiztech.biometric.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import com.raybiztech.biometric.dto.BioAttendanceDTO;

public interface BiometricService {

	SortedSet<BioAttendanceDTO> getEmployeeAttendance(Long employeeId,
			String start, String end);

	Map<String, Object> getAllEmployeeAttendance(Long employeeId,
			Integer month, Integer year, Integer startIndex, Integer endIndex,
			String status, String shiftid, String search);

	Map<String, Object> searchEmployeesAttendance(Long employeeId,
			Integer month, Integer year, Integer startIndex, Integer endIndex,
			String search, String status,String shiftid);

	SortedSet<BioAttendanceDTO> getHolidayAttendance(String start, String end);
	
	SortedSet<BioAttendanceDTO> getAttendance(String start, String end,String country);

	SortedSet<BioAttendanceDTO> getEmployeeTimeInOffice(String employeeId,
			String start, String end);

	List<BioAttendanceDTO> getMonthlyHiveReportForEmployee(String employeeId,
			String start, String end);

	void updateLateReporting();

	ByteArrayOutputStream exportBioAttendance(Long employeeId, Integer month,
			Integer year, String status, String shiftid, String search)
			throws IOException;

	List<BioAttendanceDTO> myAttendence(String start,
			String end,Long employeeId);
	
	ByteArrayOutputStream exportAttendance2(Long employeeId, Integer month,
			Integer year, String status, String shiftid, String search)
			throws IOException;
}
