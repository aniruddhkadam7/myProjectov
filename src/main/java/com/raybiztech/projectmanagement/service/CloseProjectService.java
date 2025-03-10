package com.raybiztech.projectmanagement.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
import com.raybiztech.projectmanagement.quartz.CloseProjectAlert;

@Service("closeProjecService")
public class CloseProjectService  extends QuartzJobBean{
	
	
	CloseProjectAlert closeProjectAlert;
	

	public CloseProjectAlert getCloseProjectAlert() {
		return closeProjectAlert;
	}


	public void setCloseProjectAlert(CloseProjectAlert closeProjectAlert) {
		this.closeProjectAlert = closeProjectAlert;
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			//tenant code ends here
			//System.out.println(type);
		closeProjectAlert.getAllInprogressProjects();
		}
		
	}

}
