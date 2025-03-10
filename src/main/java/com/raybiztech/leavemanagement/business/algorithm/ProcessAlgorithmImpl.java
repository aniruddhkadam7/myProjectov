package com.raybiztech.leavemanagement.business.algorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.business.LeaveType;
import com.raybiztech.leavemanagement.exceptions.LeaveCannotProcessException;
import com.raybiztech.leavemanagement.exceptions.NotEnoughLeavesAvaialableException;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;

@Component("processAlgorithm")
public class ProcessAlgorithmImpl implements ProcessAlgorithm {

	@Autowired
	LeaveManagementUtils leaveManagementUtils;

	@Override
	public LeaveDebit processLeave(LeaveDebit leaveDebit,
			Double avialableLeaves, Integer maxAccuraral) {
		// TODO Auto-generated method stub

		DateRange nextFinancialRange = leaveManagementUtils
				.getNextFinancialPeriod();
		
		// user applied period
		if (leaveDebit.getPeriod().getMinimum()
				.isAfter(leaveDebit.getPeriod().getMaximum())) {
			throw new LeaveCannotProcessException(
					"To date should be greater than From Date");
		}

		if (leaveDebit.getLeaveCategory().getLeaveType()
				.equals(LeaveType.EARNED)) {
			if (leaveDebit.getNumberOfDays() > avialableLeaves) {
				throw new NotEnoughLeavesAvaialableException(
						"You don't have enough leave balance to apply");
			}
			if (nextFinancialRange
					.contains(leaveDebit.getPeriod().getMaximum())) {

				Double debitedLeavesInNextFinancialYear = leaveManagementUtils
						.leaveDaysInYear(leaveDebit.getEmployee(),
								nextFinancialRange);
				if(debitedLeavesInNextFinancialYear==null){
					debitedLeavesInNextFinancialYear=0.0;
				}

				if (debitedLeavesInNextFinancialYear
						+ leaveDebit.getNumberOfDays() > maxAccuraral) {
					throw new NotEnoughLeavesAvaialableException(
							"You don't have enough leave balance to apply");

				}

			}
		}

		return leaveDebit;
	}

}
