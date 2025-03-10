package com.raybiztech.TimeActivity.quartz;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.TimeActivity.business.EmpoloyeeHiveActivity;
import com.raybiztech.TimeActivity.dao.TimeActivityDAO;
import com.raybiztech.TimeActivity.mailNotification.TimeActivityMailNotification;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.recruitment.business.Holidays;

@Component("monthlyTimeSheetAleart")
public class MonthlyTimeSheetNotificationToManager {
	
	Logger logger = Logger.getLogger(MonthlyTimeSheetNotificationToManager.class);
	
	@Autowired
	TimeActivityDAO timeActivityDAOimpl;
	
	@Autowired
	TimeActivityMailNotification mailNotification;
	
	private List<Project> project;

	private List<AllocationDetails> activeAllocation;
	
	private List<Long> employeeId;
	
	
	// Method for converting Java Date to Java Calendar
	public static Calendar toCalendar(java.util.Date date){ 
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  return cal;
		} 
	
	public void sendTimeSheetAleart() throws ParseException {
		// Getting all the Project which is INPROGRESS
		project = timeActivityDAOimpl.getAllActiveProjectList() ;
		
		// To avoid duplicate project list
		HashSet<Project> runningProject = new HashSet<Project>();
		for(Project runningProject1 : project){
			
			runningProject.add(runningProject1);
		}
		
		
		// Taking one by one all the project and performing operation
		for(Project proj : runningProject){
			logger.warn("Project Name = "+proj.getProjectName());
			
			// Getting Project Manager object by calling getProjectManager() from Project object
			Employee pm = proj.getProjectManager();
			logger.warn("Project manager Name = "+pm.getFullName());

			// From Employee object we are taking his/her mail ID
			String pmMailId = pm.getEmail();
			String pmName = pm.getFirstName();
			String empMailId =null;
			logger.warn("PM Email Id = "+pmMailId);
		
			
			DateRange projPiriod = proj.getPeriod();
			// Getting Project start date and end Date.
			Date projectStartDate = projPiriod.getMinimum();
			Date projectEndDate = projPiriod.getMaximum();
			
			// Month start and end Date.
			Date monthFirstDate = new Date(DayOfMonth.valueOf(1),
					MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));
			logger.warn("First date = "+monthFirstDate);
			Date monthLastDate = new Date(DayOfMonth.valueOf(31),
					MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue())).previous();
			logger.warn("Last date = "+monthLastDate);
			Date start = null;
			Date end = null;
			
			// Here we are setting start and end date based on Project period.
			if(projectStartDate.isAfter(monthFirstDate)){
				start = projectStartDate;
			}
			
			else {
				start = monthFirstDate;
			}
			
			if(projectEndDate.isAfter(monthLastDate)){
				end = monthLastDate;
			}
			
			else {
				end = projectEndDate;
			}
			
		
			logger.warn("Start Date = "+start+"   End date ="+end);
			
			/*// Current months Holidays
			List<Holiday> holidays = timeActivityDAOimpl.getAllHolidayForCurrentMonth(start,end);
			
			int holidayCount = 0;
			for(Holiday h : holidays){
				holidayCount ++;
			}
			logger.warn("Holidays Count = "+holidayCount);
			
			Calendar startCal = Calendar.getInstance();
			startCal.setTime(start.getJavaDate());

			Calendar endCal = Calendar.getInstance();
			endCal.setTime(end.getJavaDate());
			
			// Here we are ignoring Saturdays and Sundays and calculating other days (working Days for current month)
			int workDays = 0;
			while (!startCal.after(endCal)) {
				logger.warn("employeeWorkingDay count is running");
				int day = startCal.get(Calendar.DAY_OF_WEEK);
				if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
					workDays++;
					System.out.println("--");
				}
				startCal.add(Calendar.DATE, 1);
			}
			logger.warn("Workdays befor holidays is "+workDays);
			workDays = workDays - holidayCount;
			
			logger.warn("Workdays is "+workDays);*/
		
			// Based on Project Id we are loading AllocationDetails date which have active Employee and Employee allocation is true. 
			List<AllocationDetails> details = timeActivityDAOimpl.getAllAllocationDetails(proj.getId());
			
			//Project days = timeActivityDAOimpl.getProjectDays(proj.getId());
			String nameList = "";
			// From AllocationDetails Record one by one we are loading AllocationDetails object and from this we are getting employee object and record.
			for(AllocationDetails list : details){
				// Getting employee object
				Employee employee = list.getEmployee();
				//Based on employee allocation period we are getting first and last working day of an employee in project.
				Date empStartDate = list.getPeriod().getMinimum();
				Date empEndDate = list.getPeriod().getMaximum();
				logger.warn("Employee allocatin periond == Start Date is "+empStartDate+"   End Date is  "+empEndDate);
				Date to,from;
				
				// Calculating employee working day in current month
				if(empStartDate.isAfter(start)){
					from = empStartDate;
				}
				
				else {
					from = start;
				}
				
				if(empEndDate.isAfter(end)){
					to = end;
				}
				
				else {
					to = empEndDate;
				}
				logger.warn("Employee allocatin periond in current month == Start Date is "+from+"   End Date is  "+to);
				// Getting holidays list in employee working days in current month
				List<Holidays> empHolidays = timeActivityDAOimpl.getAllHolidayForCurrentMonth(from,to);
				// Getting Employee leave for current month.
				Long employeeId = employee.getEmployeeId();
				List<LeaveDebit> leaveDebit = timeActivityDAOimpl.getAllApprovedLeave(employeeId,from,to);
				Double noOfDays =0.0;
				for(LeaveDebit leave : leaveDebit){
					noOfDays = leave.getNumberOfDays();
				}
				logger.warn("Debit Leave is = "+noOfDays);
				
				// Counting Holidays.
				int empHolidayCount = 0;
				for(Holidays h : empHolidays){
					Calendar cal = toCalendar(h.getDate().getJavaDate());
					int day = cal.get(Calendar.DAY_OF_WEEK);
					if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
					empHolidayCount ++;
					}
				}
				
				logger.warn("For Employee Holidays count is = "+empHolidayCount);
				//Converting Date into Calendar by using toCalendar method. and calculating working day for employee if employee allocate for this
				// project in middle of current month.
				Calendar empStartCal =toCalendar(from.getJavaDate());
				Calendar empEndCal =toCalendar(to.getJavaDate());
				logger.warn("empStartCal = "+empStartCal); 
				logger.warn("empEndCal = "+empEndCal);
				int empWorkDays = 0;
				while (!empStartCal.after(empEndCal)) {
					logger.warn("Employee Holidays count is running who allocated  in middle");
					int day = empStartCal.get(Calendar.DAY_OF_WEEK);
					if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
						empWorkDays++;
					}
					empStartCal.add(Calendar.DATE, 1);
				}
				System.out.println("Employee Workdays befor holidays is "+empWorkDays);
				empWorkDays = empWorkDays - empHolidayCount;
				System.out.println("Employee Workdays after holidays is "+empWorkDays);
				empWorkDays = (int) (empWorkDays-noOfDays);
				System.out.println("Employee Workdays after leave is "+empWorkDays);
				
				if(!employee.getRole().contains("Manager") && !employee.getRole().contains("admin")){
					Long empId = employee.getEmployeeId();
					//Fetching all hive record for Employee for current Month
					logger.warn("Start time for Hive = "+from);
					logger.warn("End time for Hive = "+to);
					List<EmpoloyeeHiveActivity> hiveActivity = timeActivityDAOimpl.getAllHiveActivityForEmployee(empId,from,to);
					//if(proj.get)
					int count =0;
					HashSet<Date> hiveDate = new HashSet<Date>();
					for(EmpoloyeeHiveActivity hivelist : hiveActivity){
						Date date = hivelist.getDate();
						//System.out.println("Last run date is = "+date);
						hiveDate.add(date);
					}
					count = hiveDate.size();
					logger.warn("Time sheet count is "+count);
					System.out.println("Employee Name = "+employee.getEmployeeFullName());
					//if(from.isAfter(start)){
						if(count < empWorkDays-3){
							String name = employee.getEmployeeFullName();
							nameList = nameList+name+", ";
						}
					//}
				/*	else if(from.isBefore(start)){
						if(count < empWorkDays-3){
							String name = employee.getEmployeeFullName();
							nameList = nameList+name+", ";
						}
					}
					else if(to.isAfter(end)){
						if(count < empWorkDays-3){
							String name = employee.getEmployeeFullName();
							nameList = nameList+name+", ";
						}
					}
					else if(to.isBefore(end)){
						if(count < empWorkDays-3){
							String name = employee.getEmployeeFullName();
							nameList = nameList+name+", ";
						}
					}*/
					
					
					
				}
			}
			String projName = proj.getProjectName();
			logger.warn("name list is = "+nameList);
			if(nameList != ""){
				mailNotification.sendMonthlyTimeSheetAleart(pmMailId,nameList,pmName,projName);
			}

		}
		
	}
}
