/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeActivity.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityDTO;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityReport;

/**
 *
 * @author naresh
 */
public interface TimeActivityService {

	List<EmpoloyeeHiveActivityDTO> getEmployeeHiveTimeActivities(String userName);

	List<EmpoloyeeHiveActivityDTO> getEmployeeDayHiveSheet(String projectDate,
			String userName);

	Map<String, Object> getMonthlyHiveReportForManager(String hiveDate,
			String loggedInEmpId, Integer startIndex, Integer endIndex);

	Map<String, Object> searchHiveTime(String loggedInEmployeeId, String date,
			Integer startIndex, Integer endIndex, String search);

	EmpoloyeeHiveActivityReport getMonthlyHiveReportForEmployee(
			String hiveDate, String loggedInEmpId);

	ByteArrayOutputStream exportBioAttendance(String hiveDate)
			throws IOException;
	
}
