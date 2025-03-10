package com.raybiztech.leavemanagement.quartz;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.biometric.dao.BiometricDAO;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Duration;
import com.raybiztech.date.TimeUnit;
import com.raybiztech.leavemanagement.business.CarryForwardLeave;
import com.raybiztech.leavemanagement.business.LeaveSettingsLookup;
import com.raybiztech.leavemanagement.business.LeaveType;
import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.leavemanagement.service.LeavesCarryForwardService;
import com.raybiztech.leavemanagement.utils.EmployeeUtils;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;

@Component("leavesCarryForwardJob")
public class LeavesCarryForwardJob {

	@Autowired
	LeaveDAO leaveDAO;
	@Autowired
	LeaveManagementUtils leaveManagementUtils;
	@Autowired
	BiometricDAO biometricDAO;
	@Autowired
	EmployeeUtils employeeUtils;

	public static Logger logger = Logger
			.getLogger(LeavesCarryForwardService.class);

	public void carryForwardLeaves() {
		
		//below for loop is created in order to execute cron scheduler for all the tenants 
		//if only one tenant you can remove it
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			//tenant code ends here

		logger.info("hello dynamic quartz.........");

		LeaveSettingsLookup leaveSettings = leaveDAO.getLeaveSettings();

		Double maxAccrural = new Double(leaveSettings.getMaxAccrualPerYear());
		DateRange previousFinancialPeriod = leaveManagementUtils
				.getPreviousFinancialPeriod();

		for (Employee employee : biometricDAO.getActiveEmployees()) {

			Double creditingLeaves = 0.0;

			Double creditedLeaves = leaveManagementUtils.getCreditedLeaves(
					employee.getEmployeeId(), previousFinancialPeriod);

			Double carryForwardedLeaves = leaveDAO
					.getEmployeeCarryForwardedLeaves(employee,
							previousFinancialPeriod);

			if (carryForwardedLeaves == null) {
				carryForwardedLeaves = 0.0;
			}
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
			
			
			if(joinProbRange.contains(previousFinancialPeriod.getMinimum())){
				carryForwardedLeaves = 0.0;
			}*/
			
			Double maxLeavesEarned = new Double(leaveDAO.getLeaveSettings()
					.getMaxLeavesEarned());;
			if (creditedLeaves > maxLeavesEarned) {
				creditedLeaves = maxLeavesEarned;
			}
			
			

			logger.info("last year creditedLeaves :"
					+ (carryForwardedLeaves + creditedLeaves));
			Double debitedLeaves = leaveDAO.getAllDebitedLeaves(employee,
					previousFinancialPeriod);

			if (debitedLeaves == null) {
				debitedLeaves = 0.0;
			}
			logger.info("last year debitedLeaves  :" + debitedLeaves);

			Double availableLeaves = (carryForwardedLeaves + creditedLeaves)
					- debitedLeaves;
			logger.info("available leaves in last year is:" + availableLeaves);

			Double leaveDaysInThisYear = leaveManagementUtils.leaveDaysInYear(
					employee, leaveManagementUtils.getCurrentFinancialPeriod());

			logger.info("leaveDaysInThisYear is:" + leaveDaysInThisYear);

			CarryForwardLeave leaveCredit = new CarryForwardLeave();
			leaveCredit.setEmployee(employee);
			leaveCredit.setComments("Carry forwarded leaves");
			leaveCredit.setLeaveCreditedOn(new Date());
			leaveCredit.setLeaveType(LeaveType.EARNED);

			if ((availableLeaves <= maxAccrural)) {
				creditingLeaves = availableLeaves;
			} else
				creditingLeaves = maxAccrural;

			if (leaveDaysInThisYear != 0.0) {
				Double availNextYearAccural = maxAccrural - leaveDaysInThisYear;
				if (availableLeaves >= availNextYearAccural)
					creditingLeaves = availNextYearAccural;
				else
					creditingLeaves = availableLeaves;

			}
			//with the new requirement any  person within probation gets 0 leaves carry forward . 
			
			// removed below condition as employees who are in probation period
	        // should get leaves monthly.
	        // Therir is no probation period condition we removed below condition . it is applied from jan2019.
	        // if we need probation condition then uncomment below code.
			
			/*if (employeeUtils.isInProbationPeriod(employee, leaveSettings)) {

				creditingLeaves = 0d;
			}*/
			leaveCredit.setDaysCredited(creditingLeaves);
			leaveDAO.save(leaveCredit);
			logger.info(leaveCredit.getDaysCredited()
					+ " Leaves are carry forwarded for " + employee);

		}

	}

}
}
