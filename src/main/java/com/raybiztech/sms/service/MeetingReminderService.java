package com.raybiztech.sms.service;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.sms.util.SendMeetingReminder;

@Service("meetingReminderService")
public class MeetingReminderService extends QuartzJobBean {

	SendMeetingReminder sendMeetingReminder;

	public SendMeetingReminder getSendMeetingReminder() {
		return sendMeetingReminder;
	}

	public void setSendMeetingReminder(SendMeetingReminder sendMeetingReminder) {
		this.sendMeetingReminder = sendMeetingReminder;
	}

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		sendMeetingReminder.sendReminder();
	}

}
