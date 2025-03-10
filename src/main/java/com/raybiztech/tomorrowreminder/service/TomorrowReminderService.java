package com.raybiztech.tomorrowreminder.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.tomorrowreminder.util.TomorrowReminderUtil;

@Service("TomorrowReminderService")
public class TomorrowReminderService extends QuartzJobBean {

	TomorrowReminderUtil TomorrowReminderUtil;
	
	
    
	public TomorrowReminderUtil getTomorrowReminderUtil() {
		return TomorrowReminderUtil;
	}


	public void setTomorrowReminderUtil(TomorrowReminderUtil tomorrowReminderUtil) {
		TomorrowReminderUtil = tomorrowReminderUtil;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("in execute tomorrow");
		try{
			TomorrowReminderUtil.sendTemplateReminderforTomorrow();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
