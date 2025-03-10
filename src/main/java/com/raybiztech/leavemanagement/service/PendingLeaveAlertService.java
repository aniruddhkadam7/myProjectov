package com.raybiztech.leavemanagement.service;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.leavemanagement.quartz.PendingApprovalMailAlert;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;

@Service("pendingLeaveAlertService")
public class PendingLeaveAlertService extends QuartzJobBean {

	public static Logger logger = Logger
			.getLogger(PendingLeaveAlertService.class);

	private PendingApprovalMailAlert pendingApprovalMailAlert;
	@Autowired
	LeaveDAO leaveDAO;
	@Autowired
	LeaveManagementUtils leaveManagementUtils;

	public LeaveManagementUtils getLeaveManagementUtils() {
		return leaveManagementUtils;
	}

	public void setLeaveManagementUtils(LeaveManagementUtils leaveManagementUtils) {
		this.leaveManagementUtils = leaveManagementUtils;
	}

	public PendingApprovalMailAlert getPendingApprovalMailAlert() {
		return pendingApprovalMailAlert;
	}

	public void setPendingApprovalMailAlert(
			PendingApprovalMailAlert pendingApprovalMailAlert) {
		this.pendingApprovalMailAlert = pendingApprovalMailAlert;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		DateRange dateRange = leaveManagementUtils.getCurrentMonthPeriod();
		logger.info("month range min is :" + dateRange.getMinimum().toString()
				+ " max is :" + dateRange.getMaximum().toString());
		logger.info("pendingApprovalMailAlert is " + pendingApprovalMailAlert);
		
		//below for loop is created in order to execute cron scheduler for all the tenants 
				//if only one tenant you can remove it
				TenantTypes[] list = TenantTypes.class.getEnumConstants();
				for(TenantTypes type : list) {
					TenantContextHolder.setTenantType(type);
					//tenant code ends here
					
					pendingApprovalMailAlert.alertPendingApprovals(dateRange);
				}
	}

}
