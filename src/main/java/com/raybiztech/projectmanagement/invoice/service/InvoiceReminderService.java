package com.raybiztech.projectmanagement.invoice.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
import com.raybiztech.projectmanagement.invoice.quartz.InvoiceReminder;

public class InvoiceReminderService extends QuartzJobBean {

	private InvoiceReminder invoiceReminder;

	public InvoiceReminder getInvoiceReminder() {
		return invoiceReminder;
	}

	public void setInvoiceReminder(InvoiceReminder invoiceReminder) {
		this.invoiceReminder = invoiceReminder;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
		//below for loop is created in order to execute cron scheduler for all the tenants 
				//if only one tenant you can remove it
				TenantTypes[] list = TenantTypes.class.getEnumConstants();
				for(TenantTypes type : list) {
					TenantContextHolder.setTenantType(type);
					//tenant code ends here
					
					invoiceReminder.sendReminder();
				}
	}
}
