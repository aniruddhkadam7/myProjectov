package com.raybiztech.projectmanagement.invoice.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
import com.raybiztech.projectmanagement.invoice.quartz.MilestoneReminder;

public class MilestoneReminderService extends QuartzJobBean{
	
	private MilestoneReminder milestoneReminder;
	
	public MilestoneReminder getMilestoneReminder() {
		return milestoneReminder;
	}

	public void setMilestoneReminder(MilestoneReminder milestoneReminder) {
		this.milestoneReminder = milestoneReminder;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//below for loop is created in order to execute cron scheduler for all the tenants 
		//if only one tenant you can remove it
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			//tenant code ends here
		milestoneReminder.sendReminder();
		}
	}

}
