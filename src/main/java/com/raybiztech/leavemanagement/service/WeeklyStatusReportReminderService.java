package com.raybiztech.leavemanagement.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.leavemanagement.quartz.WeeklyStatusReportReminder;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;

@Service("weeklyStatusReportReminderService")
public class WeeklyStatusReportReminderService extends QuartzJobBean {

	WeeklyStatusReportReminder weeklyStatusReportReminder;

	public WeeklyStatusReportReminder getWeeklyStatusReportReminder() {
		return weeklyStatusReportReminder;
	}

	public void setWeeklyStatusReportReminder(
			WeeklyStatusReportReminder weeklyStatusReportReminder) {
		this.weeklyStatusReportReminder = weeklyStatusReportReminder;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
		//below for loop is created in order to execute cron scheduler for all the tenants 
		//if only one tenant you can remove it
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			//tenant code ends here

		weeklyStatusReportReminder.sendStatusMailReminder();
		
		}
	}

}
