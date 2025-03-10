package com.raybiztech.projectmanagement.invoice.service;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
import com.raybiztech.projectmanagement.invoice.quartz.InvoiceStatusValidator;

@Service("invoiceStatusValidatorService")
public class InvoiceStatusValidatorService extends QuartzJobBean {

	InvoiceStatusValidator invoiceStatusValidator;

	public InvoiceStatusValidator getInvoiceStatusValidator() {
		return invoiceStatusValidator;
	}

	public void setInvoiceStatusValidator(
			InvoiceStatusValidator invoiceStatusValidator) {
		this.invoiceStatusValidator = invoiceStatusValidator;
	}

	Logger logger = Logger.getLogger(InvoiceStatusValidatorService.class);

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.warn("Running Invoice Status validator Scheduler");
		//below for loop is created in order to execute cron scheduler for all the tenants 
		//if only one tenant you can remove it
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			//tenant code ends here
		invoiceStatusValidator.updateInvoiceStatus();
		}

	}

}
