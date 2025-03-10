package com.raybiztech.leavemanagement.utils;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.businesscalendar.BusinessCalendar;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.Duration;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.MonthYear;
import com.raybiztech.date.TimeUnit;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.business.LeaveSettingsLookup;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.leavemanagement.exceptions.NotWorkingDayException;
import com.raybiztech.recruitment.business.Department;
import com.raybiztech.separation.business.Separation;
import com.raybiztech.separation.dao.SeparationDao;

@Component("leaveManagementUtils")
public class LeaveManagementUtils {

	@Autowired
	LeaveDAO leaveDAO;
	
	@Autowired
	SeparationDao separationDaoImpl;

	public static Logger logger = Logger.getLogger(LeaveManagementUtils.class);

	public DateRange constructFinancicalYearPeriod(Integer financicalYear) {
		LeaveSettingsLookup leaveSettings = leaveDAO.getLeaveSettings();
		Date startDate = new Date(DayOfMonth.valueOf(1),
				MonthOfYear.valueOf(leaveSettings.getLeaveCycleMonth()
						.getMonthCode()), YearOfEra.valueOf(financicalYear));

		Date endDate = startDate.shift(new Duration(TimeUnit.MONTH, 12))
				.previous();

		DateRange period = new DateRange(startDate, endDate);

		return period;
	}

	public DateRange getCurrentFinancialPeriod() {

		Date currentDate = new Date();
		Date lastYearDate = currentDate.shift(new Duration(TimeUnit.YEAR, -1));
		DateRange period = null;

		LeaveSettingsLookup leaveSettings = leaveDAO.getLeaveSettings();

		if (currentDate.getMonthOfYear().getValue() < leaveSettings
				.getLeaveCycleMonth().getMonthCode()) {
			period = constructFinancicalYearPeriod(lastYearDate.getYearOfEra()
					.getValue());
		} else
			period = constructFinancicalYearPeriod(currentDate.getYearOfEra()
					.getValue());
		return period;

	}

	public DateRange getCurrentMonthPeriod() {
		Date firstDay = new Date(DayOfMonth.valueOf(25),
				MonthOfYear.valueOf(new Date().getMonthOfYear().getValue() - 1), new Date().getYearOfEra());
		Date lastDay = new Date(DayOfMonth.valueOf(24),
				new Date().getMonthOfYear(), new Date().getYearOfEra());
		logger.info("first day in utils is:" + firstDay + "last day is:"
				+ lastDay);
		DateRange dateRange = new DateRange(firstDay, lastDay);
		return dateRange;
	}

	public DateRange getPreviousMonthPeriod() {

		Date firstDay = new Date(
				DayOfMonth.valueOf(25),
				MonthOfYear.valueOf(new Date().getMonthOfYear().getValue() - 1),
				YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

		Date lastDay = new Date(DayOfMonth.valueOf(24),
				MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
				YearOfEra.valueOf(new Date().getYearOfEra().getValue()));
		DateRange dateRange = new DateRange(firstDay, lastDay);
		return dateRange;
		
	}
	
	public DateRange getConstructMonthPeriod(MonthYear month) {
		Date firstDay = new Date(DayOfMonth.valueOf(1),
				MonthOfYear.valueOf(month.getMonthOfYear().getValue()),
				YearOfEra.valueOf(month.getYearOfEra().getValue()));
		Date lastDay = month.getLastDay();

		DateRange dateRange = new DateRange(firstDay, lastDay);
		logger.warn("FD = "+firstDay+" , LD = "+lastDay);
		return dateRange;
	}
	
	public DateRange getMonthPeriod(MonthYear month) {
		Date firstDay = new Date(DayOfMonth.valueOf(25),
				MonthOfYear.valueOf(month.getMonthOfYear().getValue()-1),
				YearOfEra.valueOf(month.getYearOfEra().getValue()));
		Date lastDay = new Date(DayOfMonth.valueOf(24),
				MonthOfYear.valueOf(month.getMonthOfYear().getValue()),
				YearOfEra.valueOf(month.getYearOfEra().getValue()));

		DateRange dateRange = new DateRange(firstDay, lastDay);
		logger.warn("FD = "+firstDay+" , LD = "+lastDay);
		return dateRange;
	}


	public DateRange getPreviousFinancialPeriod() {

		DateRange currentFinancialYearPeriod = getCurrentFinancialPeriod();

		Date endDate = currentFinancialYearPeriod.getMinimum().previous();
		Date startDate = endDate.shift(new Duration(TimeUnit.MONTH, -12))
				.next();

		return new DateRange(startDate, endDate);

	}

	public DateRange getNextFinancialPeriod() {

		DateRange currentFinancialYearPeriod = getCurrentFinancialPeriod();

		Date startDate = currentFinancialYearPeriod.getMaximum().next();
		Date endDate = currentFinancialYearPeriod.getMaximum().shift(
				new Duration(TimeUnit.MONTH, 12));

		return new DateRange(startDate, endDate);

	}

	public DateRange constructMonthPeriod(Integer month, Integer year) {

		MonthOfYear monthOfYear = MonthOfYear.valueOf(month);
		YearOfEra yearOfEra = YearOfEra.valueOf(year);

		return getConstructMonthPeriod(new MonthYear(monthOfYear, yearOfEra));
	}

	public Double getCreditedLeaves(Long employeeId, DateRange finanicalPeriod) {
		
		System.out.println("fin min:" + finanicalPeriod.getMinimum().toString());
		System.out.println("fin max:" + finanicalPeriod.getMaximum().toString());

		Employee employee = leaveDAO.findBy(Employee.class, employeeId);

		Double value = 0.0;

		DateRange currentCreditedRange = new DateRange(finanicalPeriod
				.getMinimum().next(), finanicalPeriod.getMaximum().next());
		
		logger.warn("minimum"+finanicalPeriod.getMinimum().next().toString());
		logger.warn("maximum"+finanicalPeriod.getMaximum().next().toString());

		
		System.out.println(" employee joining date" + employee.getJoiningDate());
		
		// For a employee in probation during the financial year cron his / her leaves are calculated
		// as below.
		Date joiningDate = employee.getJoiningDate();
		
		// removed below condition as employees who are in probation period
        // should get leaves monthly.
        // Therir is no probation period condition we removed below condition . it is applied from jan2019.
        // if we need probation condition then uncomment below code.
		
		/*Date probationPeriodEndDate = joiningDate.shift(
				new Duration(TimeUnit.MONTH, leaveDAO.getLeaveSettings()
						.getProbationPeriod()));
		DateRange joinProbRange = new DateRange(joiningDate, probationPeriodEndDate);
		
		
		if(joinProbRange.contains(finanicalPeriod.getMinimum())){
			DateRange creditedRange = new DateRange(joiningDate, new Date().next());
			Iterator<Date> itr = creditedRange.iterator();
			
			while (itr.hasNext()) {

				Date date = itr.next();
				if (date.getDayOfMonth().getValue() == 1) {
					value = value + 1;
				}

			}
			
		}
		else*/
		
		// If we want leave will credited on every month last days then uncomment below code.
		/*
		 if (employee.getJoiningDate().isBefore(finanicalPeriod.getMinimum())) {
				
				logger.warn("in before financial period");

				Iterator<Date> itr = currentCreditedRange.iterator();

				while (itr.hasNext()) {

					Date date = itr.next();
					if (date.getDayOfMonth().getValue() == 1) {
						value = value + 1;
					}

				}

			} else {
				currentCreditedRange = new DateRange(employee.getJoiningDate()
						.next(), finanicalPeriod.getMaximum().next());
				Iterator<Date> itr = currentCreditedRange.iterator();
				
				logger.warn("in after financial period");

				while (itr.hasNext()) {

					Date date = itr.next();
					if (date.getDayOfMonth().getValue() == 1) {
						value = value + 1;
					}

				}
				
				

			}*/
		
		// If we want leave will credited on every month on 24th then uncomment below code.
		if (employee.getJoiningDate().isBefore(finanicalPeriod.getMinimum())) {
			
			logger.warn("in before financial period");

			Iterator<Date> itr = currentCreditedRange.iterator();

			while (itr.hasNext()) {

				Date date = itr.next();
				if (date.getDayOfMonth().getValue() == 25) {
					value = value + 1;
				}

			}

		} else {
			currentCreditedRange = new DateRange(employee.getJoiningDate()
					.next(), finanicalPeriod.getMaximum().next());
			Iterator<Date> itr = currentCreditedRange.iterator();
			
			logger.warn("in after financial period");

			while (itr.hasNext()) {

				Date date = itr.next();
				if (date.getDayOfMonth().getValue() == 25) {
					value = value + 1;
				}

			}
			
			if(employee.getJoiningDate().getDayOfMonth().getValue() >=24){
				value = value+1;
			}

		}
		
		Double leaveValue = 0.0;

		Double leavePerMonth = Math.round((leaveDAO.getLeaveSettings()
				.getLeavesPerYear() / 12) * 100.0) / 100.0;
		
		Double creditedValue = value * leavePerMonth;
		
		logger.warn("in credited leaves"+creditedValue);
		
		
		
		// New functionality for employees who are in probation period.
		// here we fixed the date as 11-08-2018,this is used for
		// if employee joins after 10th day of month then leave should not be
		// added for the following month.

		//Double totalCreditedLeaves = 0d;

		Date augDate = new Date(DayOfMonth.valueOf(11),
				MonthOfYear.valueOf(7), YearOfEra.valueOf(2018));

		logger.warn("joining date" + employee.getJoiningDate());
		logger.warn("condition for getting date"
				+ (employee.getJoiningDate().equals(augDate) || employee
						.getJoiningDate().isAfter(augDate)));
		
		if (!employee.getJoiningDate().isBefore(finanicalPeriod.getMinimum())){

		if (employee.getJoiningDate().equals(augDate)
				|| employee.getJoiningDate().isAfter(augDate)) {
			logger.warn("day condition"
					+ (employee.getJoiningDate().getDayOfMonth().getValue() > 10));
			if (employee.getJoiningDate().getDayOfMonth().getValue() > 10) {
				logger.warn("leaves condition"
						+ (creditedValue >= 1.25));
				if (creditedValue >= 1.25) {
					creditedValue = creditedValue - 1.25;
				}
			}
		}
		}
		
		// below functionality is used for if employee is in notice period then leaves shouls not be added and if employee revoke the  
		//his/her resignation  leaves should be added.the period between resignation and revoke  dates leaves will not added for employee.
		
		Boolean separationExitsForEmployee = separationDaoImpl.isSepartionExistsForEmployee(employee);
		
		logger.warn("flagggg" +separationExitsForEmployee);	
		
		if(separationExitsForEmployee == Boolean.TRUE ){
			
			logger.warn("in separation" +separationExitsForEmployee);
			
			Separation separation = separationDaoImpl.getEmployeeSepartionForLeave(employeeId);
			
			logger.warn("revokecondition"+ (separation.getIsRevoked() == Boolean.FALSE));
			System.out.println("separation date:" + separation.getResignationDate().toString());
			
			if(separation.getIsRevoked() == Boolean.FALSE){
				if(finanicalPeriod.contains(separation.getResignationDate())){
				logger.warn("in if condition in submit resignation");
				logger.warn("status"+separation.getStatus());
				logger.warn("separation date"+separation.getResignationDate());
				logger.warn("today date"+ new Date());
				DateRange underNoticeRange = new DateRange(separation.getResignationDate(), new Date());
				
				logger.warn("underNoticeRange"+underNoticeRange.getMinimum().toString());
				
				Iterator<Date> itr = underNoticeRange.iterator();
				// From every month 24th onwards we are calculating leave for those employee who is serving Notice period.
				while (itr.hasNext()) {
					Date date = itr.next();
					if (date.getDayOfMonth().getValue() == 25) {
						leaveValue = leaveValue + 1;
					}
				}
				logger.warn("underNoticeValue"+leaveValue);
				Double underNoticeLeave = leaveValue * leavePerMonth;
				
				// We are not crediting any leave for those employee who is serving Notice Period.
				logger.warn("before substration"+creditedValue);
				creditedValue = creditedValue - underNoticeLeave;
				logger.warn("after substration"+creditedValue);
				}
				
			}else if(separation.getIsRevoked() == Boolean.TRUE){
				if(finanicalPeriod.contains(separation.getResignationDate())){
				
				logger.warn("in else of revoke condition");
				String revokeddate = separation.getCreatedDate().toString("dd/MM/yyyy");
				
				Date revokedDate = null;
				try {
					 revokedDate = DateParser.toDate(revokeddate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				DateRange revokeRange = new DateRange(separation.getResignationDate(), revokedDate);
				
				Iterator<Date> itr = revokeRange.iterator();
				// If employee revoke his/her notice period then also we are not adding any leave for those month
				// in which employee in notice period.
				while (itr.hasNext()) {
					Date date = itr.next();
					if (date.getDayOfMonth().getValue() == 25) {
						leaveValue = leaveValue + 1;
					}
				}
				logger.warn("revokeValue"+leaveValue);
				Double underNoticeLeave = leaveValue * leavePerMonth;
				logger.warn("beforererevoke substration"+creditedValue);
				creditedValue = creditedValue - underNoticeLeave;
				logger.warn("afterrevoke substration"+creditedValue);
				}
				
			}
				
		}
		
		return creditedValue;
	}

	public Double appliedDays(LeaveDebit leaveDebit,
			BusinessCalendar businessCalendar) {

		Double days = (double) businessCalendar.getWorkingDays(
				leaveDebit.getPeriod()).size();
		logger.warn("Business cal = "+ businessCalendar.getWorkingDays(leaveDebit.getPeriod()));
		logger.info("appliedDays is " + days);
		if (days == 0.0) {
			throw new NotWorkingDayException(
					"Leave cannot be applied  on non working days");
		}

		return days;
	}

	public Double leaveDaysInYear(Employee employee, DateRange dateRange) {
		List<LeaveDebit> nextYearLeavesList = leaveDAO.getNextYearLeaveDebits(
				employee, dateRange);
		logger.info("nextYearLeavesList size is :" + nextYearLeavesList.size());
		BusinessCalendar employeeBusinessCalendar = leaveDAO.findByName(
				Department.class, employee.getDepartmentName())
				.getBusinessCalendar();

		Set<Date> leaveDates = new HashSet<Date>();
		for (LeaveDebit leaveDebit2 : nextYearLeavesList) {

			Iterator<Date> itr = leaveDebit2.getPeriod().iterator();
			while (itr.hasNext()) {
				Date date = itr.next();
				if (dateRange.contains(date)
						&& employeeBusinessCalendar.isWorkingDay(date))
					leaveDates.add(date);
			}

		}
		Double debitedLeaves = (double) leaveDates.size();

		logger.info("DebitedLeavesInNextFinancialYear is :" + debitedLeaves);
		return debitedLeaves;
	}
	
	//check whether the  leave's date of apply before the financial cycle start
	public Boolean checkPreviousCycleLeave(LeaveDebit leaveDebit) {
		
		DateRange currentFinanceYear = getCurrentFinancialPeriod();
		Date startDate = currentFinanceYear.getMinimum();
		
		Date appliedDate = leaveDebit.getLeaveAppliedOn();
		
		if(appliedDate.isBefore(startDate)){
			return true;
		}
		return false;
	}


}
