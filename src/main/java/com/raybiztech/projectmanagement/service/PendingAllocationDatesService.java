package com.raybiztech.projectmanagement.service;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
import com.raybiztech.projectmanagement.quartz.PendingResourceAllocationMailAlert;

@Service("pendingAllocationDatesService")
public class PendingAllocationDatesService extends QuartzJobBean {

	Logger logger = Logger.getLogger(PendingAllocationDatesService.class);

	private PendingResourceAllocationMailAlert pendingResourceAllocationMailAlert;

	public void setPendingResourceAllocationMailAlert(
			PendingResourceAllocationMailAlert pendingResourceAllocationMailAlert) {
		this.pendingResourceAllocationMailAlert = pendingResourceAllocationMailAlert;
	}

	public PendingResourceAllocationMailAlert getPendingResourceAllocationMailAlert() {
		return pendingResourceAllocationMailAlert;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
		//below for loop is created in order to execute cron scheduler for all the tenants 
		//if only one tenant you can remove it
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			//tenant code ends here
		try {
			logger.warn("Sending Resource Allocation dates alert");
			pendingResourceAllocationMailAlert
					.alertpendingresourceallocations();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}

}
