package com.raybiztech.biometric.service;

import java.text.ParseException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.raybiztech.biometric.quartz.LateReportingTimeAlert;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;

@Component("lateReportingAlertService")
public class LateReportingAlertService extends QuartzJobBean {

	LateReportingTimeAlert lateReportingTimeAlert;

	public LateReportingTimeAlert getLateReportingTimeAlert() {
		return lateReportingTimeAlert;
	}

	public void setLateReportingTimeAlert(
			LateReportingTimeAlert lateReportingTimeAlert) {
		this.lateReportingTimeAlert = lateReportingTimeAlert;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		//below for loop is created in order to execute cron scheduler for all the tenants 
		//if only one tenant you can remove it
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			//tenant code ends here
		try {
			lateReportingTimeAlert.lateReportingTime();
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
	}
	}
}
