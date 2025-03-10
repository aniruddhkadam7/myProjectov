/*package com.raybiztech.leavemanagement.businesstest;

import java.util.HashSet;
import java.util.Set;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.leavemanagement.business.LeaveCategory;
import com.raybiztech.leavemanagement.business.LeaveCategorySummary;
import com.raybiztech.leavemanagement.business.LeaveSummary;

public class LeaveSummaryData {
public LeaveSummary getLeaveSummaryData(){
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
	leaveSummary.setLeaveCategorySummaries(s);
	
	leaveSummary.setAllAvailableLeaves(10.0);
	return leaveSummary;
}
}
*/