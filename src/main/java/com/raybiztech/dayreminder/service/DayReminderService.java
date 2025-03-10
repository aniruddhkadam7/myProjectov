package com.raybiztech.dayreminder.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import com.raybiztech.dayreminder.util.DayReminderUtil;;

@Service("DayReminderService")
public class DayReminderService extends QuartzJobBean {

	DayReminderUtil DayReminderUtil;

    public DayReminderUtil getDayReminderUtil() {
		return DayReminderUtil;
	}

	public void setDayReminderUtil(DayReminderUtil dayReminderUtil) {
		DayReminderUtil = dayReminderUtil;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("in execute internal");
		try{
			DayReminderUtil.sendTemplateReminder();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
