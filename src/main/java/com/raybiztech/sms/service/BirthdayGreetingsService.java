package com.raybiztech.sms.service;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.sms.util.SendBirthdayWishes;

@Service("birthdayGreetingsService")
public class BirthdayGreetingsService extends QuartzJobBean {

	SendBirthdayWishes sendBirthdayWishes;

	public SendBirthdayWishes getSendBirthdayWishes() {
		return sendBirthdayWishes;
	}

	public void setSendBirthdayWishes(SendBirthdayWishes sendBirthdayWishes) {
		this.sendBirthdayWishes = sendBirthdayWishes;
	}

	Logger logger = Logger.getLogger(BirthdayGreetingsService.class);

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.warn("Sending Birthday Greetings");
		sendBirthdayWishes.sendSMS();
	}

}
