package com.raybiztech.compliance.quartz;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;


@Service("complianceMailService")
public class ComplianceMailService extends QuartzJobBean{
	
	ComplianceMail complianceMail;

	Logger logger = Logger.getLogger(ComplianceMailService.class);
			
	public ComplianceMail getComplianceMail() {
		return complianceMail;
	}

	public void setComplianceMail(ComplianceMail complianceMail) {
		this.complianceMail = complianceMail;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.warn("Executing Compliance Mail and Creating Tasks");
		
		//below for loop is created in order to execute cron scheduler for all the tenants 
		//if only one tenant you can remove it
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			//tenant code ends here
		complianceMail.sendComplianceMailAlert();
		}
	}
	
}
