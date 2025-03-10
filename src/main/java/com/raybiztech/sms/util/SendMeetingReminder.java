package com.raybiztech.sms.util;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.meetingrequest.business.MeetingAttendees;
import com.raybiztech.meetingrequest.business.MeetingRequest;
import com.raybiztech.sms.dao.SMSDao;

@Component("sendMeetingReminder")
public class SendMeetingReminder {

	@Autowired
	SMSDao smsDao;

	@Autowired
	PropBean propBean;

	@Autowired
	SMSUtil smsUtil;

	Logger logger = Logger.getLogger(SendMeetingReminder.class);

	public void sendReminder() {

		List<MeetingRequest> meetingRequests = smsDao
				.getMeetingsWhichStartsInNextFifteenMinutes();

		if (meetingRequests != null) {

			String numbers = getAttendeeNumbers(meetingRequests);

			String message = smsDao.getMeetingSMSAlertContent("Meeting Alert");
			message = message.replace(" ", "%20");

			String url = (String) propBean.getPropData().get("SMSAPI");

			if (numbers != null && message != null && url != null) {

				url = url.replace("{number(s)}", numbers);
				url = url.replace("{message}", message);

				try {
					logger.warn("Sending Meeting Reminder");
					smsUtil.sendMessage(url);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
	}

	public String getAttendeeNumbers(List<MeetingRequest> meetingRequests) {

		String numbers = null;
		for (MeetingRequest meetingRequest : meetingRequests) {
			List<MeetingAttendees> attendees = smsDao.getAllOfProperty(
					MeetingAttendees.class, "meetingRequest", meetingRequest);
			if (!attendees.isEmpty()) {
				for (MeetingAttendees attendee : attendees) {
					Employee employee = attendee.getEmployee();
					if (employee.getPhone() != null) {
						numbers = (numbers != null) ? numbers + ","
								+ employee.getPhone() : employee.getPhone();
					}

				}
			}
		}
		return numbers;
	}
}
