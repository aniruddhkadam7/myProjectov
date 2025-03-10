package com.raybiztech.assetmanagement.service;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.assetmanagement.quartz.AssetWarrantyEndDateAlert;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;

@Service("assetWarrantyEndDatesService")
public class AssetWarrantyEndDatesService extends QuartzJobBean {

	Logger logger = Logger.getLogger(AssetWarrantyEndDatesService.class);

	private AssetWarrantyEndDateAlert assetWarrantyEndDateAlert;
	
	

	public AssetWarrantyEndDateAlert getAssetWarrantyEndDateAlert() {
		return assetWarrantyEndDateAlert;
	}



	public void setAssetWarrantyEndDateAlert(
			AssetWarrantyEndDateAlert assetWarrantyEndDateAlert) {
		this.assetWarrantyEndDateAlert = assetWarrantyEndDateAlert;
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
			assetWarrantyEndDateAlert
					.AssetWarrantyEndDateAlertForNextFifteenDays();
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
	}
	}
}
