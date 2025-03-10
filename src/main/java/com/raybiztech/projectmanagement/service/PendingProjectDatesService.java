package com.raybiztech.projectmanagement.service;

import java.text.ParseException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
import com.raybiztech.projectmanagement.quartz.PendingProjectClosedDateAlert;

@Service("pendingProjectDatesService")
public class PendingProjectDatesService extends QuartzJobBean{
	
private PendingProjectClosedDateAlert pendingProjectClosedDateAlert;

public void setPendingProjectClosedDateAlert(
		PendingProjectClosedDateAlert pendingProjectClosedDateAlert) {
	this.pendingProjectClosedDateAlert = pendingProjectClosedDateAlert;
}

public PendingProjectClosedDateAlert getPendingProjectClosedDateAlert() {
	return pendingProjectClosedDateAlert;
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
	pendingProjectClosedDateAlert.projectDetails();
} catch (ParseException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}
}

}
