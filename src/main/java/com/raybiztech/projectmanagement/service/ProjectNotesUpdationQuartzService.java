package com.raybiztech.projectmanagement.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
import com.raybiztech.projectmanagement.quartz.ProjectNotesUpdationAlert;

@Service("ProjectNotesUpdationQuartzService")
public class ProjectNotesUpdationQuartzService extends QuartzJobBean {

	ProjectNotesUpdationAlert projectNotesUpdationAlert;

	public ProjectNotesUpdationAlert getProjectNotesUpdationAlert() {
		return projectNotesUpdationAlert;
	}

	public void setProjectNotesUpdationAlert(
			ProjectNotesUpdationAlert projectNotesUpdationAlert) {
		this.projectNotesUpdationAlert = projectNotesUpdationAlert;
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
		projectNotesUpdationAlert.sendProjectNotesAlerts();
	}
	}
}
