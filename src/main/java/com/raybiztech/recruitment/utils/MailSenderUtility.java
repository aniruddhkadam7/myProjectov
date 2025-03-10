/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import javax.jms.JMSException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.fortuna.ical4j.model.property.Organizer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.mail.sender.MessageSender;
import com.raybiztech.meetingrequest.service.MeetingRequestServiceImpl;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.recruitment.dto.CandidateScheduleDto;

/**
 *
 * @author hari
 */
@Component("mailSenderUtility")
public class MailSenderUtility {

	@Autowired
	MessageSender messageSender;
	@Autowired
	PropBean propBean;
	@Autowired
	MeetingRequestServiceImpl meetingRequestServiceImpl;
	static final Logger logger = Logger.getLogger(MailSenderUtility.class);

	public void sendMail(Candidate candidate, String interviewDate,
			String interviewTime, String round) {
		String candidateExp = candidate.getExperience();

		String toEmailId = candidate.getEmail();
		// String candidateName = candidate.getFirstName();
		String candidateName = candidate.getFullName();
		// String ccEmailId = "";
		String bccInterviewersEmailId = "";

		String interviewRound = round;

		String appliedFor = candidate.getAppliedForLookUp();

		String interviewType = String.valueOf(candidate.getInterview()
				.getInterviewType());

		String bringCV = "<br><br>Please bring your updated CV at the time of interview. ";
		if (interviewType.equalsIgnoreCase("TELEPHONE")) {
			interviewType = "Telephonic";
			bringCV = "";
		} else if (interviewType.equalsIgnoreCase("FACE_TO_FACE")) {
			interviewType = "In-person";
		}
		String onward = null;
		String jd = null;

		if (Integer.parseInt(interviewRound) > 1) {
			onward = "We are pleased to inform you that you have been shortlisted for the "
					+ "next level of interview. We request you to make yourself available on ";
			bringCV = "";
			jd = "";

		} else {
			onward = "Thank you for applying for the position of "
					+ appliedFor
					+ " with Ray Business Technologies, Hyderabad India. Your interview has been scheduled for ";
			jd = candidate.getAppliedFor().getDescription() != null ? "<br><br><b><u> Job description:</u></b> "
					+ candidate.getAppliedFor().getDescription()
					: "";

		}
		String interviewScheduleDate = null;
		String time = null;
		try {
			if (interviewDate.contains(" ")) {
				interviewScheduleDate = DateParser.toDateOtherFormat(
						interviewDate).toString("dd MMM yyyy");
				time = DateParser.toDateOtherFormat(interviewDate).toString(
						"dd/MM/yyyy")
						+ "/" + timeConverter(interviewTime);
				;
			} else {
				interviewScheduleDate = DateParser.toDate(interviewDate)
						.toString("dd MMM yyyy");
				time = DateParser.toDate(interviewDate).toString("dd/MM/yyyy")
						+ "/" + timeConverter(interviewTime);
				;
			}
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(
					MailSenderUtility.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		String interviewScheduleTime = interviewTime;
		// String positionVacant = candidate.getAppliedForLookUp();

		String bccemailId = (String) propBean.getPropData()
				.get("mail.hrMailId");// "hr@raybiztech.com";

		Set<Employee> interviewers = candidate.getInterview().getInterviewers();
		String employeeNames = null;
		// String managerCCMailId = null;

		for (Employee employee : interviewers) {

			employeeNames = employee.getFullName();

			// managerCCMailId = employee.getManager().getEmail();
			bccInterviewersEmailId += employee.getEmail() + ",";
		}
		if (interviewers.size() > 1) {
			employeeNames = "Associates";
		}

		String subjectForCandidate = "Interview at Ray Business Technologies";
		String subjectForInterviewer = "Reg: Interview schedule on "
				+ interviewScheduleDate + " at " + interviewScheduleTime;

		StringBuffer bodyOfCandidateMail = new StringBuffer(
				"<![CDATA[  Dear "
						+ candidateName
						+ ", <br><br> "
						+ onward
						+ "<a href=\"#\">"
						+ interviewScheduleDate
						+ ",</a>"
						+ " "
						+ interviewScheduleTime
						+ "."
						+ bringCV
						+ "<br><br> Mode of interview: "
						+ interviewType
						+ "<br><br> Round: "
						+ interviewRound
						+ "<br><br><b><u>Venue Details: </u></b>"
						+ "<br> <b>Ray Business Technologies Pvt Ltd</b>"
						+ "<br> Plot No: 204, Block B, Kavuri Hills,"
						+ "<br> Next to IGNOU, Madhapur, Hyderabad, Telagana India."
						+ "<br><br> <b><u>Contact Person:</u></b> HR"
						// +
						// "<br><br><b><u>Contact No:</u></b> 040-65690200 / 23118011 / 22, Ext: 101 / 102 / 103"
						+ "<br><br><b><u>Contact No:</u></b> +91 40 46400400 / 40027722 / 40208022 / 2311 8011 / 22"
						+ jd
						+ "<br><br> For further queries please feel free to contact HR department"
						+ "<br><br><br>Regards,"
						+ "<br>HR"
						+ "<br>Ray Business Technologies Private Limited"
						/*
						 * + "<br>T : +91 040 2311 8011/22 " +
						 * "<br>Visit: www.raybiztech.com"
						 */
						+ "<br><br>View us at <a href=\"http://www.facebook.com/raybiztech\">Facebook</a> | <a href=\"http://www.linkedin.com/company/ray-business-technologies\">Linkedin</a> | <a href=\"https://mobile.twitter.com/Raybiztech\">Twitter</a>"
						+ "<br>"
						+ "<br><b>Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of <a href=\"http://www.nasscom.in/ray-business-technologies-pvt-ltd/\">NASSCOM</a> and <a href=\"http://hysea.in/members/member-companies\">HYSEA</a> </b>"
						+ "<br>We are an equal opportunities employer. </b> ]]>");

		StringBuffer bodyOfInterviewrMail = new StringBuffer(
				interViewerMailBody(employeeNames, candidateName, appliedFor,
						candidate.getExperience(), toEmailId, interviewType,
						interviewRound, interviewScheduleDate,
						interviewScheduleTime));

		try {
			messageSender.sendMessage("<email><to>" + toEmailId + "</to>"
					+ "<bcc>" + bccemailId + "</bcc>" + "<subject>"
					+ subjectForCandidate + "</subject>" + "<body>"
					+ bodyOfCandidateMail + "</body></email>");

			messageSender.sendMessage("<email><to>" + bccInterviewersEmailId
					+ "</to>" + "<bcc>" + bccemailId + "</bcc>" + "<subject>"
					+ subjectForInterviewer + "</subject>" + "<body>"
					+ bodyOfInterviewrMail + "</body></email>");
			// Interview Invitation.
			String agenda = interviewType + " interview scheduled - "
					+ candidateName;
			String meetingId = candidate.getMobile() + "_" + interviewDate
					+ "_" + interviewTime + "_" + round
					+ bccInterviewersEmailId;
			send(agenda, time, bccInterviewersEmailId, meetingId);

		} catch (JMSException ex) {
			// null, ex);
		} catch (Exception ex) {
			// null, ex);
		}
	}

	public void sendRescheduleMail(Candidate candidate, String interviewDate,
			String interviewTime, String round, String interviewMode) {

		String toEmailId = candidate.getEmail();
		// String candidateName = candidate.getFirstName();
		String candidateName = candidate.getFullName();
		// String ccEmailId = " ";
		String bccInterviewersEmailId = "";

		String interviewRound = round;

		String interviewType = interviewMode;
		String bringCV = "<br><br>Please bring your updated CV at the time of interview. ";
		if (interviewType.equalsIgnoreCase("TELEPHONE")) {
			interviewType = "Telephonic";
			bringCV = "";
		} else if (interviewType.equalsIgnoreCase("FACE_TO_FACE")) {
			interviewType = "In-person";
		}
		if (Integer.parseInt(interviewRound) > 1) {
			bringCV = "";
		}

		String interviewScheduleDate = null;
		String time = null;
		try {
			if (interviewDate.contains(" ")) {
				interviewScheduleDate = DateParser.toDateOtherFormat(
						interviewDate).toString("dd MMM yyyy");
				time = DateParser.toDateOtherFormat(interviewDate).toString(
						"dd/MM/yyyy")
						+ "/" + timeConverter(interviewTime);
				;
			} else {
				interviewScheduleDate = DateParser.toDate(interviewDate)
						.toString("dd MMM yyyy");
				time = DateParser.toDate(interviewDate).toString("dd/MM/yyyy")
						+ "/" + timeConverter(interviewTime);
				;
			}
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(
					MailSenderUtility.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		String interviewScheduleTime = interviewTime;
		String appliedFor = candidate.getAppliedForLookUp();

		String bccemailId = (String) propBean.getPropData()
				.get("mail.hrMailId");// hr@raybiztech.com");
		Set<Employee> interviewers = candidate.getInterview().getInterviewers();
		String employeeNames = null;
		for (Employee employee : interviewers) {
			employeeNames = employee.getFullName();
			bccInterviewersEmailId += employee.getEmail() + ",";
		}
		if (interviewers.size() > 1) {
			employeeNames = "Associates";
		}
		String subjectForCandidate = "Interview at Ray Business Technologies";
		String subjectForInterviewer = "Reg: Interview schedule on "
				+ interviewScheduleDate + " at " + interviewScheduleTime + ".";

		StringBuffer bodyOfCandidateMail = new StringBuffer(candidateMailBody(
				candidateName, interviewScheduleDate, interviewScheduleTime,
				bringCV, interviewType, interviewRound));

		StringBuffer bodyOfInterviewrMail = new StringBuffer(
				interViewerMailBody(employeeNames, candidateName, appliedFor,
						candidate.getExperience(), toEmailId, interviewType,
						interviewRound, interviewScheduleDate,
						interviewScheduleTime));

		try {

			messageSender.sendMessage("<email><to>" + toEmailId + "</to>"
					+ "<bcc>" + bccemailId + "</bcc>" + "<subject>"
					+ subjectForCandidate + "</subject>" + "<body>"
					+ bodyOfCandidateMail + "</body></email>");

			messageSender.sendMessage("<email><to>" + bccInterviewersEmailId
					+ "</to>" + "<bcc>" + bccemailId + "</bcc>" + "<subject>"
					+ subjectForInterviewer + "</subject>" + "<body>"
					+ bodyOfInterviewrMail + "</body></email>");

			// Interview Invitation.
			String agenda = interviewType + " interview scheduled - "
					+ candidateName;
			String meetingId = candidate.getMobile() + "_" + interviewDate
					+ "_" + interviewTime + "_" + round
					+ bccInterviewersEmailId;
			send(agenda, time, bccInterviewersEmailId, meetingId);

		} catch (JMSException ex) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMailToEmployee(Employee employee)
			throws MalformedURLException {
		URL url = new URL("https://ovh.raybiztech.com/");
		StringBuffer subject = new StringBuffer("Reg: OVH");

		StringBuffer bodyOfMail = new StringBuffer(
				"<![CDATA[ <b> Dear "
						+ employee.getFullName()
						+ ", "
						+ "</b><br><br> Welcome to Ray Business Technologies Pvt. Ltd, please login to the below url,  "
						+ "<br>"
						+ url
						+ "  with your zimbra credentials, and update the necessary details in OVH (Contact number, Bank account number, Pan card number, EPF number, Emergency contact number and Address)."
						+ "<br> Feel free to contact HR desk for further queries."
						+ "<br><br>Kind Regards, <br> HR <br>Ray Business Technologies Private Limited ]]>");

		try {
			String cc = (String) propBean.getPropData().get("mail.hrMailId");

			String bcc = " ";
			String mail = employee.getEmail();
			messageSender.sendMessage("<email><to>" + mail + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + bodyOfMail
					+ "</body></email>");
		} catch (JMSException ex) {
			// Logger.getLogger(JobPortalServiceImpl.class.getName()).log(Level.SEVERE,
			// null, ex);
		}
	}

	public void send(String agenda, String time, String to, String meetingId)
			throws Exception {
		to = to.replace("null", "");
		Session session =mailSession();
		MimeMessage message = new MimeMessage(session);
		String from = (String) propBean.getPropData().get("mail.fromMailId");
		try {
			message.setFrom(new InternetAddress(from));
			message.setSubject(agenda);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					(String) propBean.getPropData().get("mail.hrMailId")));
			Multipart multipart = new MimeMultipart("alternative");
			BodyPart messageBodyPart = buildHtmlTextPart(agenda);
			multipart.addBodyPart(messageBodyPart);
			BodyPart calendarPart = buildCalendarPart(agenda, time, to,
					meetingId);
			multipart.addBodyPart(calendarPart);
			message.setContent(multipart);
			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			Thread.sleep(1000);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private BodyPart buildHtmlTextPart(String agenda) throws MessagingException {
		MimeBodyPart descriptionPart = new MimeBodyPart();

		String content = "<font >" + agenda + "</font>";
		descriptionPart.setContent(content, "text/html; charset=utf-8");
		return descriptionPart;
	}

	private SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat(
			"yyyyMMdd'T'HHmm'00'");

	private BodyPart buildCalendarPart(String agenda, String time, String to,
			String meetingId) throws Exception {
		to = to.replace("null", "");
		BodyPart calendarPart = new MimeBodyPart();
		java.util.Date start = meetingRequestServiceImpl.javaUtildate(time);
		java.util.Date end = meetingRequestServiceImpl
				.javaUtildate(endTime(time));
		String from = (String) propBean.getPropData().get("mail.fromMailId");
		String hr = (String) propBean.getPropData().get("mail.hrMailId");
		Organizer org1 = new Organizer(URI.create("MAILTO:" + from));

		String calendarContent = "BEGIN:VCALENDAR\n" + "METHOD:REQUEST\n"
				+ "PRODID:Zimbra-Calendar-Provider" + "VERSION:2.0\n"
				+ "BEGIN:VEVENT\n" + "DTSTAMP:"
				+ iCalendarDateFormat.format(start)
				+ "\n"
				+ "DTSTART:"
				+ iCalendarDateFormat.format(start)
				+ "\n"
				+ "DTEND:"
				+ iCalendarDateFormat.format(end)
				+ "\n"
				+ "SUMMARY:"
				+ agenda
				+ "\n"
				+ "UID:"
				// + id.toString()
				+ meetingId
				+ "\n"
				+ "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:"
				+ to
				+ "\n"
				+ "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:"
				+ hr
				+ "\n"
				+ org1/*
					 * "ORGANIZER:MAILTO:"+ propBean
					 * .getPropData().get("mail.hrMailId")
					 */
				+ "\n"
				+ "\n"
				+ "DESCRIPTION:"
				+ agenda
				+ "\n"
				+ "SEQUENCE:0\n"
				+ "PRIORITY:5\n"
				+ "CLASS:PUBLIC\n"
				+ "STATUS:CONFIRMED\n"
				+ "TRANSP:OPAQUE\n"
				+ "BEGIN:VALARM\n"
				+ "ACTION:DISPLAY\n"
				+ "DESCRIPTION:REMINDER\n"
				+ "TRIGGER;RELATED=START:-PT00H15M00S\n"
				+ "END:VALARM\n"
				+ "END:VEVENT\n" + "END:VCALENDAR";
		calendarPart.addHeader("Content-Class",
				"urn:content-classes:calendarmessage");
		calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");
		return calendarPart;

	}

	public String timeConverter(String interviewTime) {
		String maridian[] = interviewTime.split(" ");
		String hrs[] = maridian[0].split(":");

		if (maridian[1].equalsIgnoreCase("pm")
				&& (!hrs[0].equalsIgnoreCase("12"))) {
			hrs[0] = String.valueOf(Integer.parseInt(hrs[0]) + 12);
		} else if (maridian[1].equalsIgnoreCase("am")
				&& (hrs[0].equalsIgnoreCase("12"))) {
			hrs[0] = String.valueOf(Integer.parseInt(hrs[0]) - 12);
		}
		if (hrs[0].length() == 1) {
			hrs[0] = "0" + hrs[0];
		}
		return hrs[0] + "/" + hrs[1];

	}

	public String endTime(String startTime) {
		String maridian[] = startTime.split("/");
		maridian[4] = String.valueOf(Integer.parseInt(maridian[4]) + 30);
		if (Integer.parseInt(maridian[4]) > 60) {
			maridian[3] = String.valueOf(Integer.parseInt(maridian[3]) + 1);
			maridian[4] = String.valueOf(Integer.parseInt(maridian[4]) - 60);
		} else if (Integer.parseInt(maridian[4]) == 60) {
			maridian[3] = String.valueOf(Integer.parseInt(maridian[3]) + 1);
			maridian[4] = "00";
		}
		if (maridian[4].length() == 1) {
			maridian[4] = "0" + maridian[4];
		}
		return maridian[0] + "/" + maridian[1] + "/" + maridian[2] + "/"
				+ maridian[3] + "/" + maridian[4];
	}

	public void duplicateMethodForRescheduleInterview(Candidate candidate,
			CandidateScheduleDto candidateScheduleDto, Employee interviewr,
			CandidateInterviewCycle candidateInterviewCycle) {

		String toEmailId = candidate.getEmail();
		// String candidateName = candidate.getFirstName();
		String candidateName = candidate.getFullName();
		// String ccEmailId = " ";
		String bccInterviewersEmailId = "";

		String interviewRound = candidate.getInterview().getRound();

		String interviewType = candidateScheduleDto.getInterviewType();
		String bringCV = "<br><br>Please bring your updated CV at the time of interview. ";
		if (interviewType.equalsIgnoreCase("TELEPHONE")) {
			interviewType = "Telephonic";
			bringCV = "";
		} else if (interviewType.equalsIgnoreCase("FACE_TO_FACE")) {
			interviewType = "In-person";
		}
		if (Integer.parseInt(interviewRound) > 1) {
			bringCV = "";
		}

		String interviewScheduleDate = null;
		String time = null;
		try {
			if ((candidateScheduleDto.getScheduleDate()).contains(" ")) {
				interviewScheduleDate = DateParser.toDateOtherFormat(
						candidateScheduleDto.getScheduleDate()).toString(
						"dd MMM yyyy");
				time = DateParser.toDateOtherFormat(
						candidateScheduleDto.getScheduleDate()).toString(
						"dd/MM/yyyy")
						+ "/"
						+ timeConverter(candidateScheduleDto.getScheduleTime());
				;
			} else {
				interviewScheduleDate = DateParser.toDate(
						candidateScheduleDto.getScheduleDate()).toString(
						"dd MMM yyyy");
				time = DateParser
						.toDate(candidateScheduleDto.getScheduleDate())
						.toString("dd/MM/yyyy")
						+ "/"
						+ timeConverter(candidateScheduleDto.getScheduleTime());
				;
			}
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(
					MailSenderUtility.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		String interviewScheduleTime = candidateScheduleDto.getScheduleTime();
		String appliedFor = candidate.getAppliedForLookUp();

		String bccemailId = (String) propBean.getPropData()
				.get("mail.hrMailId");// hr@raybiztech.com");
		Set<Employee> interviewers = candidate.getInterview().getInterviewers();
		String employeeNames = null;
		// Here the below loop is not useful its only for future sake if
		// multiple interviewer
		for (Employee employee : interviewers) {
			employeeNames = employee.getFullName();
			bccInterviewersEmailId += employee.getEmail() + ",";
		}
		if (interviewers.size() > 1) {
			employeeNames = "Associates";
		}
		String subjectForCandidate = "Interview at Ray Business Technologies";
		String subjectForInterviewer = "Reg: Interview schedule on "
				+ interviewScheduleDate + " at " + interviewScheduleTime + ".";

		StringBuffer bodyOfCandidateMail = new StringBuffer(candidateMailBody(
				candidateName, interviewScheduleDate, interviewScheduleTime,
				bringCV, interviewType, interviewRound));

		StringBuffer bodyOfInterviewrMail = new StringBuffer(
				interViewerMailBody(interviewr.getFullName(), candidateName,
						appliedFor, candidate.getExperience(), toEmailId,
						interviewType, interviewRound, interviewScheduleDate,
						interviewScheduleTime));

		try {
			if (candidateInterviewCycle.getInterviewDate()
					.toString("dd/MM/yyyy")
					.equalsIgnoreCase(candidateScheduleDto.getScheduleDate())
					&& candidateInterviewCycle.getInterviewTime()
							.equalsIgnoreCase(
									candidateScheduleDto.getScheduleTime())
					&& candidateInterviewCycle.getInterviewers()
							.equalsIgnoreCase(interviewr.getFullName())) {
			} else if (candidateInterviewCycle.getInterviewDate()
					.toString("dd/MM/yyyy")
					.equalsIgnoreCase(candidateScheduleDto.getScheduleDate())
					&& candidateInterviewCycle.getInterviewTime()
							.equalsIgnoreCase(
									candidateScheduleDto.getScheduleTime())
					&& !candidateInterviewCycle.getInterviewers()
							.equalsIgnoreCase(interviewr.getFullName())) {
				messageSender.sendMessage("<email><to>"
						+ /* bccInterviewersEmailId */interviewr.getEmail()
						+ "</to>" + "<bcc>" + bccemailId + "</bcc>"
						+ "<subject>" + subjectForInterviewer + "</subject>"
						+ "<body>" + bodyOfInterviewrMail + "</body></email>");

				// Interview Invitation.
				String agenda = interviewType + " interview scheduled - "
						+ candidateName;
				String meetingId = candidate.getMobile() + "_"
						+ candidateScheduleDto.getScheduleDate() + "_"
						+ candidateScheduleDto.getScheduleTime() + "_"
						+ candidate.getInterview().getRound()
						+ /* bccInterviewersEmailId */interviewr.getEmail();
				send(agenda, time, /* bccInterviewersEmailId */
						interviewr.getEmail(), meetingId);
			} else {
				messageSender.sendMessage("<email><to>" + toEmailId + "</to>"
						+ "<bcc>" + bccemailId + "</bcc>" + "<subject>"
						+ subjectForCandidate + "</subject>" + "<body>"
						+ bodyOfCandidateMail + "</body></email>");

				messageSender.sendMessage("<email><to>"
						+ /* bccInterviewersEmailId */interviewr.getEmail()
						+ "</to>" + "<bcc>" + bccemailId + "</bcc>"
						+ "<subject>" + subjectForInterviewer + "</subject>"
						+ "<body>" + bodyOfInterviewrMail + "</body></email>");

				// Interview Invitation.
				String agenda = interviewType + " interview scheduled - "
						+ candidateName;
				String meetingId = candidate.getMobile() + "_"
						+ candidateScheduleDto.getScheduleDate() + "_"
						+ candidateScheduleDto.getScheduleTime() + "_"
						+ candidate.getInterview().getRound()
						+ /* bccInterviewersEmailId */interviewr.getEmail();
				send(agenda, time, /* bccInterviewersEmailId */
						interviewr.getEmail(), meetingId);
			}

		} catch (JMSException ex) {
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String candidateMailBody(String candidateName,
			String interviewScheduleDate, String interviewScheduleTime,
			String bringCV, String interviewType, String interviewRound) {
		String body = "<![CDATA[  Dear "
				+ candidateName
				+ ", <br><br> This is to inform you that your interview has been rescheduled for "
				+ interviewScheduleDate
				+ " , "
				+ interviewScheduleTime
				+ "."
				+ bringCV
				// + appliedFor
				// +
				// ". Your requested to make yourself available for an interview <b> rescheduled </b> on "

				+ " Request you to make yourself available at the scheduled time. "
				+ "<br><br> Mode of interview: "
				+ interviewType
				+ "<br><br> Round: "
				+ interviewRound
				+ "<br><br><b><u>Venue Details: </u></b>"
				+ "<br> <b>Ray Business Technologies Pvt Ltd</b>"
				+ "<br> Plot No: 204, Block B, Kavuri Hills,"
				+ "<br> Next to IGNOU, Madhapur, Hyderabad, Telagana India."
				+ "<br><br> <b><u>Contact Person:</u></b> HR"
				+ "<br><br><b><u>Contact No:</u></b> +91 40 46400400 / 40027722 / 40208022 / 2311 8011 / 22"
				+ "<br><br> For further queries please feel free to contact HR department"
				+ "<br><br><br><b>Regards, </b><br><b>HR</b>"
				+ "<br>Ray Business Technologies Private Limited"
				+ "<br><br>View us at <a href=\"http://www.facebook.com/raybiztech\">Facebook</a> | <a href=\"http://www.linkedin.com/company/ray-business-technologies\">Linkedin</a> | <a href=\"https://mobile.twitter.com/Raybiztech\">Twitter</a>"

				+ "<br><b>Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of <a href=\"http://www.nasscom.in/ray-business-technologies-pvt-ltd/\">NASSCOM</a> and <a href=\"http://hysea.in/members/member-companies\">HYSEA</a> </b>"
				+ "<br>We are an equal opportunities employer. </b> ]]>";
		return body;
	}

	public String interViewerMailBody(String employeeNames,
			String candidateName, String appliedFor, String experience,
			String toEmailId, String interviewType, String interviewRound,
			String interviewScheduleDate, String interviewScheduleTime) {
		String body = "<![CDATA[  Dear "
				+ employeeNames
				+ ", <br><br> We would like to invite you to take a interview of "
				+ candidateName
				+ " who had applied for "
				+ appliedFor
				+ " position. The details are mentioned below."
				// + interviewScheduleTime
				+ "<br><br> Exp: "
				+ /* candidate.getExperience() */experience
				+ "<br><br> Email ID: "
				+ toEmailId
				+ "<br><br> Mode of interview: "
				+ interviewType
				+ "<br><br> Round: "
				+ interviewRound
				+ "<br><br>Interview date and time: "
				+ interviewScheduleDate
				+ " , "
				+ interviewScheduleTime
				+ "<br><br>Please click the below link for further details"
				+ "<br><a href=\"https://ovh.raybiztech.com/\">ovh.raybiztech.com</a>."
				+ "<br><br> Request you to make yourself available at the scheduled date and time. "
				+ "<br><br> Feel free to contact HR department for further queries. "
				+ "<br><br><br>Regards, <br> HR <br>Ray Business Technologies Pvt Ltd. "
				+ "<br><br><b>Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of <a href=\"http://www.nasscom.in/ray-business-technologies-pvt-ltd/\">NASSCOM</a> and <a href=\"http://hysea.in/members/member-companies\">HYSEA</a> </b>"
				+ "<br>We are an equal opportunities employer. </b> ]]>";
		return body;

	}
	
	public Session mailSession(){
		String host = (String) propBean.getPropData().get("mail.host");
		String port = (String) propBean.getPropData().get("mail.port");
		String sslTrust = (String) propBean.getPropData().get("mail.sslTrust");
		
		Properties props = new Properties();// use java.util.properties
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		// props.put("mail.debug", "true");
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.socketFactory.port", port);
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.ssl.trust", sslTrust);
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
//		props.put("mail.smtp.starttls.enable", false);
		
		// WHEN WE R USING getDefaultInstance()
		// "Access to default session denied" COMING
		// Use javax.mail.session
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {

					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication((String) propBean
								.getPropData().get("mail.fromMailId"),
								(String) propBean.getPropData().get(
										"mail.mailIdPassword"));
					}
				});
		return session;

	}
}
