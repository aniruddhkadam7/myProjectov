package com.raybiztech.SQAAudit.service;

import java.text.ParseException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.SQAAudit.quartz.PendingAuditFollowUpDateAlert;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
@Service("pendingAuditFollowUpDateService")
public class PendingFollowUpDateService extends QuartzJobBean {
	
	private PendingAuditFollowUpDateAlert pendingAuditFollowUpDateAlert;

	public PendingAuditFollowUpDateAlert getPendingAuditFollowUpDateAlert() {
		return pendingAuditFollowUpDateAlert;
	}

	public void setPendingAuditFollowUpDateAlert(
			PendingAuditFollowUpDateAlert pendingAuditFollowUpDateAlert) {
		this.pendingAuditFollowUpDateAlert = pendingAuditFollowUpDateAlert;
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
				pendingAuditFollowUpDateAlert.auditDetails();
			} 
			catch (ParseException e) {
				e.printStackTrace();
			}
		}

	}

}
