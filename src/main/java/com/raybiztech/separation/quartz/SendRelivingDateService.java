package com.raybiztech.separation.quartz;


import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;

@Service("sendRelivingDateService")
public class SendRelivingDateService extends QuartzJobBean{
	
	SendRelivingDate sendRelivingDate;

	public SendRelivingDate getSendRelivingDate() {
		return sendRelivingDate;
	}

	public void setSendRelivingDate(SendRelivingDate sendRelivingDate) {
		this.sendRelivingDate = sendRelivingDate;
	}
	Logger logger=Logger.getLogger(SendRelivingDateService.class);

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//below for loop is created in order to execute cron scheduler for all the tenants 
				//if only one tenant you can remove it
				TenantTypes[] list = TenantTypes.class.getEnumConstants();
				for(TenantTypes type : list) {
					TenantContextHolder.setTenantType(type);
					//tenant code ends here
					sendRelivingDate.sendAlert();
				}
		
		
	}

}
