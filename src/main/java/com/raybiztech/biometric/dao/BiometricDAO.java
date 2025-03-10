package com.raybiztech.biometric.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityTime;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.biometric.business.BioAttendance;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.dao.LeaveDAO;

public interface BiometricDAO extends LeaveDAO {

	Set<BioAttendance> getAttendances(Long employeeId, DateRange monthPeriod);

	SortedSet<BioAttendance> getAllEmployeeAttendances(DateRange monthPeriod);

	List<Employee> getActiveEmployees();

	List<Employee> getInActiveEmployees();

	List<EmpoloyeeHiveActivityTime> getMonthlyHiveReportForEmployee(
			String employeeId, DateRange monthPeriod);

	Map<String, Object> getStatusPaginatedEmployees(Integer startIndex,
			Integer endIndex, String status, String shiftid, String search);

	// List<Employee> getPaginatedActiveEmployeesForAttendance(Integer
	// startIndex,Integer endIndex);
	Map<String, Object> getPaginatedInActiveEmployees(Integer startIndex,
			Integer endIndex);

	Integer getTotalActiveEmployees();

	Integer getTotalInActiveEmployees();

	Map<String, Object> searchEmployees(String search, Integer startIndex,
			Integer endIndex, String status,String shiftid);

	// List<Employee> searchReportees(Long employeeId, String search,
	// Integer index, String status);
	Map<String, Object> searchReportees(List<Long> employeeId, String search,
			Integer startIndex, Integer endIndex, String status,String shiftid);

	Integer totalSearchEmployees(String search, String status);

	Integer totalSearchReportees(Long employeeId, String search, String status);

	Integer getTotalActiveReportees(Long employeeId);

	Integer getTotalInActiveReportees(Long employeeId);

	List<Employee> getPaginatedReportees(Long employeeId, Integer index,
			String status);

	Map<String, Object> getPaginatedReporteesForAttendance(
			List<Long> employeeId, Integer startIndex, Integer endIndex,
			String status, String shiftid, String search);

	List<BioAttendance> updateLateReporting();
	
	List<BioAttendance> getLateComingAttendance(DateRange monthPeriod,List<Date> holidays);
	
}
