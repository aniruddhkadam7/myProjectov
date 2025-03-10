package com.raybiztech.leavemanagement.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.CarryForwardLeave;
import com.raybiztech.leavemanagement.business.LeaveCategory;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.business.LeaveSettingsLookup;
import com.raybiztech.leavemanagement.business.PayrollCutOffDates;
import com.raybiztech.leavemanagement.business.ProbationPeriod;
import com.raybiztech.leavemanagement.dto.SearchCriteriaDTO;

public interface LeaveDAO extends DAO {

	Map<String, Object> getAllLeaves(Integer startIndex, Integer endIndex,
			Long employeeId);

	SortedSet<LeaveCategory> getAllLeaveCategories();

	<T extends Serializable> T getLeaveCategoryByName(Serializable name);

	List<LeaveDebit> numberOfTimesApplied(LeaveDebit leave);

	Map<String, Object> getReporteePendingLeaves(Integer startIndex,
			Integer endIndex, List<Long> managerEmployeeId, DateRange period);

	
	/*Map<String, Object> getResourcePendingLeaves(Integer startIndex,
			Integer endIndex, List<Long> employeeList, DateRange period);*/
	
	Map<String,Object> getResorces(Integer startIndex,
			Integer endIndex,  Long projectMangerId, DateRange period);
	
	
	// List<LeaveDebit> searchEmployees(SearchCriteriaDTO searchCriteriaDTO);
	Map<String, Object> searchEmployees(Integer startIndex, Integer endIndex,
			List<Long> managerIds, SearchCriteriaDTO searchCriteriaDTO);
	
	Map<String,Object> getResorcesList(Integer startIndex, Integer endIndex,
			Long projectMangerId, SearchCriteriaDTO searchCriteriaDTO);

	LeaveSettingsLookup getLeaveSettings();

	Employee getEmployeeByUserName(String userName);

	Double getDebitedLeaves(Employee employee, LeaveCategory leaveCategory,
			DateRange financialPeriod);

	List<LeaveDebit> getLeaveDebitedSet(Employee employee, DateRange period);

	List<CarryForwardLeave> getAllEmployeeCarryForwardedLeaves(DateRange period);

	List<LeaveDebit> getAllEmployeeLeaveDebits(DateRange period);

	List<Integer> getAllCreditedYears();

	Double getEmployeeCarryForwardedLeaves(Employee employee,
			DateRange financialPeriod);

	Double getAllDebitedLeaves(Employee employee, DateRange financialPeriod);

	Map<String, Object> getAllEmployeePendingLeaves(Integer startIndex,
			Integer endIndex, DateRange period);

	List<LeaveDebit> getAllEmployeeMonthlyPendingLeaves(DateRange monthPeriod);

	List<LeaveDebit> getAllLeaveDebites(DateRange period);

	Double getDebitedLeavesInNextYear(Employee employee,
			DateRange nextFinancialPeriod);

	// List<LeaveDebit> searchEmployeesAsAdmin(SearchCriteriaDTO
	// searchCriteriaDTO);
	Map<String, Object> searchEmployeesAsAdmin(Integer startIndex,
			Integer endIndex, SearchCriteriaDTO searchCriteriaDTO);

	Set<LeaveCategory> getEarnedCategories();

	List<LeaveDebit> getNextYearLeaveDebits(Employee employee, DateRange period);

	SortedSet<LeaveCategory> getlopCategories();

	List<LeaveDebit> getReporteesPendingLeavesOfManager(Employee manager,
			DateRange monthPeriod);
	
	CarryForwardLeave getCarryForwardLeave(Employee employee,Date creditedOn);

	List<ProbationPeriod> getProbationMonths();

	List<PayrollCutOffDates> getCuttOffDates();
	
	

}
