package com.raybiztech.sms.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.date.Date;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.HourOfDay;
import com.raybiztech.date.MinuteOfHour;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.Second;
import com.raybiztech.date.SecondOfMinute;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.sms.dao.SMSDao;

@Component("sendInterviewReminder")
public class SendInterviewReminder {

	@Autowired
	SMSDao smsDao;

	@Autowired
	PropBean propBean;

	@Autowired
	SMSUtil smsUtil;

	Logger logger = Logger.getLogger(SendInterviewReminder.class);

	public void sendReminder() {

		List<CandidateInterviewCycle> candidateInterviewCycles = smsDao
				.getAllOfProperty(CandidateInterviewCycle.class,
						"interviewDate", new Date());

		if (!candidateInterviewCycles.isEmpty()) {

			String url = (String) propBean.getPropData().get("SMSAPI");

			String numbers = getNumbers(candidateInterviewCycles);

			String message = smsDao
					.getMeetingSMSAlertContent("Interview Alert");
			message = message.replace(" ", "%20");

			if (numbers != null && message != null && url != null) {

				url = url.replace("{number(s)}", numbers);
				url = url.replace("{message}", message);

				try {
					logger.warn("Sending Interview Reminder");
					smsUtil.sendMessage(url);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

	}

	public String getNumbers(
			List<CandidateInterviewCycle> candidateInterviewCycles) {

		String numbers = null;
		for (CandidateInterviewCycle cycle : candidateInterviewCycles) {
			if (checkInterviewIsInNextFifteenMin(cycle.getInterviewTime())) {
				String employeeName = cycle.getInterviewers();
				Employee employee2 = smsDao.findByUniqueProperty(
						Employee.class, "employeeFullName", employeeName);
				if (employee2.getPhone() != null) {
					numbers = (numbers != null) ? numbers + ","
							+ employee2.getPhone() : employee2.getPhone();
				}

			}

		}
		return numbers;

	}

	public Boolean checkInterviewIsInNextFifteenMin(String time) {

		Boolean flag;

		if (time != null) {

			String meridian = time.split(" ")[1];
			String stringHours = time.split(":")[0];
			String stringMinutes = time.split(":")[1];
			stringMinutes = stringMinutes.split(" ")[0];

			Integer hours = Integer.parseInt(stringHours);
			Integer minutes = Integer.parseInt(stringMinutes);

			if (meridian.equalsIgnoreCase("PM")
					&& !stringHours.equalsIgnoreCase("12")) {
				hours = hours + 12;
			}
			if (meridian.equalsIgnoreCase("AM")
					&& stringHours.equalsIgnoreCase("12")) {
				hours = 0;
			}

			Second currentTime = new Second();
			Second timeAfterFifteenMin = new Second(
					System.currentTimeMillis() + 15 * 60 * 1000);

			Second customTime = new Second(YearOfEra.valueOf(new Date()
					.getYearOfEra().getValue()), MonthOfYear.valueOf(new Date()
					.getMonthOfYear().getValue()), DayOfMonth.valueOf(1),
					HourOfDay.valueOf(hours), MinuteOfHour.valueOf(minutes),
					SecondOfMinute.valueOf(00));

			/*
			 * System.out.println("currentTime " + currentTime);
			 * System.out.println("timeAfterFifteenMin " + timeAfterFifteenMin);
			 * System.out.println("customTime " + customTime);
			 */

			if (currentTime.isBefore(customTime)
					&& timeAfterFifteenMin.isAfter(customTime)) {
				flag = Boolean.TRUE;

			} else {
				flag = Boolean.FALSE;
			}
		} else {
			flag = Boolean.FALSE;
		}

		return flag;
	}
}
