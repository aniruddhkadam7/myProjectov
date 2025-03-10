package com.raybiztech.employee.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.employee.quartz.EmployeeExperience;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;

@Service("employeeExperienceService")
public class EmployeeExperienceService extends QuartzJobBean {
	
	EmployeeExperience employeeExperience;

	public EmployeeExperience getEmployeeExperience() {
		return employeeExperience;
	}


	public void setEmployeeExperience(EmployeeExperience employeeExperience) {
		this.employeeExperience = employeeExperience;
	}


	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			//tenant code ends here
			employeeExperience.updateEmployeeExperience();
		}
		
	}

}

