/*package com.raybiztech.leavemanagement.businesstest;

import java.util.HashSet;
import java.util.Set;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.business.LeaveDebits;
import com.raybiztech.leavemanagement.business.LeaveCategory;
import com.raybiztech.leavemanagement.business.LeaveCategorySummary;
import com.raybiztech.leavemanagement.business.LeaveSummary;

public class LeaveData {
public LeaveDebits getLeaveData(){

	LeaveDebits leave = new LeaveDebits();
	leave.setPeriod(new DateRange(new Date(DayOfMonth.valueOf(20),
			MonthOfYear.valueOf(9), YearOfEra.valueOf(2014)), new Date(
			DayOfMonth.valueOf(23), MonthOfYear.valueOf(9), YearOfEra
					.valueOf(2014))));

	LeaveSummary leaveSummary = new LeaveSummary();
	Employee employee = new Employee();
	employee.setEmployeeId(1273L);
	LeaveCategory category = new LeaveCategory();
	category.setName("CASUAL");

	LeaveCategorySummary le = new LeaveCategorySummary();
	le.setLeaveCategory(category);
	le.setAvailableLeaves(5.0);
	le.setId(1l);

	Set<LeaveCategorySummary> s = new HashSet<LeaveCategorySummary>();
	s.add(le);

	leave.setCategory(category);
	leave.setEmployee(employee);
	leaveSummary.setLeaveCategorySummaries(s);
	return leave;
}
}
*/