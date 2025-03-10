/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeActivity.dao;

import java.util.List;
import java.util.Map;

import com.ibm.icu.util.Holiday;
import com.raybiztech.TimeActivity.business.EmpoloyeeHiveActivity;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityReport;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.recruitment.business.Holidays;

/**
 *
 * @author naresh
 */
public interface TimeActivityDAO extends DAO {

    List<EmpoloyeeHiveActivity> getEmployeeHiveTimeActivities(String userName);


    List<EmpoloyeeHiveActivity> getEmployeeDayHiveSheet(String projectDate,
            String userName);

    Map<String, Object> getMonthlyHiveReportForManager(
            String hiveDate, Employee employee, Integer startIndex, Integer endIndex);

    Map<String, Object> searchHiveTime(String hiveDate,
            Employee employee, Integer startIndex, Integer endIndex, String search);

    EmpoloyeeHiveActivityReport getMonthlyHiveReportForEmployee(
            String hiveDate, String loggedInEmpId);
    
    Map<String,Object> projectTimeSheet(String hiveDate,List<Long> empIds,String hiveProjectName);
    
    List<Project> getAllActiveProjectList();
    
    List<AllocationDetails> getAllAllocationDetails(Long id);


	List<EmpoloyeeHiveActivity> getAllHiveActivityForEmployee(Long empId,Date start, Date end);


	List<Holidays> getAllHolidayForCurrentMonth(Date start, Date end);


	List<LeaveDebit> getAllApprovedLeave(Long empId,Date from, Date to);
}
