package com.raybiztech.supportmanagement.service;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
import com.raybiztech.supportmanagement.quartz.AccessTicketEndDateAlert;

@Service("accessTicketEndDateAlertService")
public class AccessTicketEndDateAlertService extends QuartzJobBean {

	AccessTicketEndDateAlert accessTicketEndDateAlert;
	
	Logger logger = Logger.getLogger(AccessTicketEndDateAlert.class);

	public AccessTicketEndDateAlert getAccessTicketEndDateAlert() {
		return accessTicketEndDateAlert;
	}

	public void setAccessTicketEndDateAlert(
			AccessTicketEndDateAlert accessTicketEndDateAlert) {
		this.accessTicketEndDateAlert = accessTicketEndDateAlert;
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
			accessTicketEndDateAlert.accessTicketDetails();
		} catch (ParseException pe) {
			pe.printStackTrace();
		}

	}
	}

}
