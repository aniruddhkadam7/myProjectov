package com.raybiztech.mailtemplates.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.meetingrequest.builder.MeetingRequestBuilder;
import com.raybiztech.meetingrequest.business.MeetingRequest;
import com.raybiztech.meetingrequest.business.Room;
import com.raybiztech.meetingrequest.dto.MeetingRequestDto;

@Component("conferenceRoomMailConfiguration")
public class ConferenceRoomMailConfiguration {

	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;

	@Autowired
	MailContentParser mailContentParser;

	@Autowired
	MeetingRequestBuilder meetingRequestBuilder;

	public void sendMeetingConfirmationMail(MeetingRequestDto meetingRequestDto) {

		Map<String, String> meetingDetails = getMeetingDetails(meetingRequestDto);

		String mailContent = mailTemplatesDaoImpl.getMailContent("New Meeting");

		if (mailContent != null) {
			mailContentParser.parseAndSendMail(meetingDetails, mailContent);
		} else {
			throw new MailCantSendException("New Meeting content not available");
		}

	}

	public void sendEventConfirmationMail(MeetingRequestDto meetingRequestDto) {

		Map<String, String> meetingDetails = getMeetingDetails(meetingRequestDto);

		String mailContent = mailTemplatesDaoImpl.getMailContent("New Event");

		if (mailContent != null) {
			mailContentParser.parseAndSendMail(meetingDetails, mailContent);
		} else {
			throw new MailCantSendException("New Event content not available");
		}

	}

	public void sendMeetingUpdationMail(MeetingRequestDto meetingRequestDto) {

		Map<String, String> meetingDetails = getMeetingDetails(meetingRequestDto);

		String mailContent = mailTemplatesDaoImpl
				.getMailContent("Meeting Update");

		if (mailContent != null) {
			mailContentParser.parseAndSendMail(meetingDetails, mailContent);
		} else {
			throw new MailCantSendException(
					"Meeting Update content not available");
		}

	}

	public void sendCancelMeetingMail(Long meetingId) {

		MeetingRequest meetingRequest = mailTemplatesDaoImpl.findBy(
				MeetingRequest.class, meetingId);
		Employee employee = mailTemplatesDaoImpl.findBy(Employee.class,
				Long.parseLong(meetingRequest.getAuthorName()));
		String regarding = null;
		if (meetingRequest.getEventType() != null) {
			regarding = "<![CDATA[ Event Cancelled for ]]>"
					+ meetingRequest.getAgenda();
		} else {
			regarding = "<![CDATA[ Meeting Cancelled for ]]>"
					+ meetingRequest.getAgenda();
		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", regarding);
		details.put("toName", employee.getFullName());
		details.put("subject", meetingRequest.getAgenda());
		details.put("to", employee.getEmail());
		details.put("bcc", "");
		details.put("cc", "");

		String mailContent = mailTemplatesDaoImpl
				.getMailContent("Meeting Cancellation");

		if (mailContent != null) {
			mailContentParser.parseAndSendMail(details, mailContent);
		} else {
			throw new MailCantSendException(
					"Meeting Cancellation content not available");
		}

	}

	public Map<String, String> getMeetingDetails(
			MeetingRequestDto meetingRequestDto) {

		Employee reservedBy = mailTemplatesDaoImpl.findBy(Employee.class,
				meetingRequestDto.getAuthorName().getId());

		String startTime = meetingRequestDto.getStartTime();
		String endTime = meetingRequestDto.getEndTime();
		String[] stime = startTime.split("/");
		String startTimeFormate = stime[3] + ":" + stime[4];
		String[] endTimeFormate = endTime.split("/");
		String s2 = endTimeFormate[3] + ":" + endTimeFormate[4];
		String endHour = meetingRequestBuilder.twelveHoursFormate(s2);
		String startHour = meetingRequestBuilder
				.twelveHoursFormate(startTimeFormate);

		Room room = mailTemplatesDaoImpl.findBy(Room.class,
				meetingRequestDto.getRoomId());

		String regarding = null;
		if (meetingRequestDto.getEventTypeId() != null) {
			regarding = "<![CDATA[ Event for ]]>"
					+ meetingRequestDto.getAgenda();
		} else {
			regarding = "<![CDATA[ Meeting Request for ]]>"
					+ meetingRequestDto.getAgenda();
		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", regarding);
		details.put("toName", reservedBy.getFullName());
		details.put("subject", meetingRequestDto.getAgenda());
		details.put("location", room.getLocation().getLocationName());
		details.put("room", room.getRoomName());
		details.put("date", meetingRequestDto.getFromDate());
		details.put("fromDate", meetingRequestDto.getFromDate());
		details.put("toDate", meetingRequestDto.getToDate());
		details.put("time", startHour + " to " + endHour);
		details.put("to", reservedBy.getEmail());
		details.put("bcc", "");
		details.put("cc", "");

		return details;

	}
}
