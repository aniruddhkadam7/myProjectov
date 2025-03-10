package com.raybiztech.leavemanagement.service;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.leavemanagement.quartz.AutoApprovalOfPendingLeaves;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;

@Service("autoApprovalOfPendingLeavesService")
public class AutoApprovalOfPendingLeavesService extends QuartzJobBean {

	AutoApprovalOfPendingLeaves autoApprovalOfPendingLeaves;

	public AutoApprovalOfPendingLeaves getAutoApprovalOfPendingLeaves() {
		return autoApprovalOfPendingLeaves;
	}

	public void setAutoApprovalOfPendingLeaves(
			AutoApprovalOfPendingLeaves autoApprovalOfPendingLeaves) {
		this.autoApprovalOfPendingLeaves = autoApprovalOfPendingLeaves;
	}

	Logger logger = Logger.getLogger(AutoApprovalOfPendingLeavesService.class);

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		//logger.warn("In autoApprovalOfPendingLeavesService ");
		//below for loop is created in order to execute cron scheduler for all the tenants 
		//if only one tenant you can remove it
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			//tenant code ends here
		autoApprovalOfPendingLeaves.autoApproveAllPendingLeaves();
		}

	}
}
