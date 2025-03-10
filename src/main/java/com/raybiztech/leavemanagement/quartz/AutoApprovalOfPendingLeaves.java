package com.raybiztech.leavemanagement.quartz;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.business.LeaveStatus;
import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;

@Transactional
@Component("autoApprovalOfPendingLeaves")
public class AutoApprovalOfPendingLeaves {

	@Autowired
	LeaveDAO leaveDAO;

	@Autowired
	LeaveManagementUtils leaveManagementUtils;

	Logger logger = Logger.getLogger(AutoApprovalOfPendingLeaves.class);

	// This Method will approve all Pending leaves automatically
	public void autoApproveAllPendingLeaves() {

		/*
		 * List<LeaveDebit> leaves = leaveDAO
		 * .getAllEmployeeMonthlyPendingLeaves(leaveManagementUtils
		 * .getCurrentMonthPeriod());
		 */

		DateRange dateRange = leaveManagementUtils.getPreviousMonthPeriod();

		logger.warn("Scheduler approving all PENDING LEAVES between "
				+ dateRange.getMinimum() + " ---" + dateRange.getMaximum());

		List<LeaveDebit> leaves = leaveDAO
				.getAllEmployeeMonthlyPendingLeaves(dateRange);

		if (!leaves.isEmpty()) {
			for (LeaveDebit leave : leaves) {
				if (!(leave.getVersion() >= 1)) {
					leave.setStatus(LeaveStatus.Approved);
					leave.setApprovedBy("Auto Approved");
					leaveDAO.saveOrUpdate(leave);
				}
			}
		}

	}
}
