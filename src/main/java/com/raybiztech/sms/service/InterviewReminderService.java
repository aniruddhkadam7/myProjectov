package com.raybiztech.sms.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.sms.util.SendInterviewReminder;

@Service("interviewReminderService")
public class InterviewReminderService extends QuartzJobBean {

	SendInterviewReminder sendInterviewReminder;

	public SendInterviewReminder getSendInterviewReminder() {
		return sendInterviewReminder;
	}

	public void setSendInterviewReminder(
			SendInterviewReminder sendInterviewReminder) {
		this.sendInterviewReminder = sendInterviewReminder;
	}
	
	public SendInterviewReminder sendWhatsappNotificationbeforeDay(){
		return sendInterviewReminder;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		sendInterviewReminder.sendReminder();
	}

}
