package com.raybiztech.mailtemplates.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.recruitment.business.Interview;
import com.raybiztech.recruitment.dto.CandidateScheduleDto;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.recruitment.utils.MailSenderUtility;

@Component("recuritmentMailConfiguration")
public class RecuritmentMailConfiguration {

	@Autowired
	PropBean propBean;

	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;

	@Autowired
	MailContentParser mailContentParser;

	@Autowired
	MailSenderUtility mailSenderUtility;

	Logger logger = Logger.getLogger(RecuritmentMailConfiguration.class);

	// Send Mail On scheduling interviews //
	public void sendScheduleInterviewMail(Candidate candidate,
			String interviewDate, String interviewTime, String round,
			Boolean sendMailToCandidate, Boolean sendMailToInterviewer)
			throws Exception {

		String bccemailId = (String) propBean.getPropData()
				.get("mail.hrMailId");

		String interviewType = String.valueOf(candidate.getInterview()
				.getInterviewType());
	
		
		String cvDetails = "<br><br>Please bring your updated CV at the time of interview. ";
		if (interviewType.equalsIgnoreCase("TELEPHONE")) {
			interviewType = "Telephonic";
			cvDetails = "";
		} else if (interviewType.equalsIgnoreCase("FACE_TO_FACE")) {
			interviewType = "In-person";
		}

		String interviewScheduleDate = null;
		String time = null;
		if (interviewDate.contains(" ")) {
			interviewScheduleDate = DateParser.toDateOtherFormat(interviewDate)
					.toString("dd MMM yyyy");
			time = DateParser.toDateOtherFormat(interviewDate).toString(
					"dd/MM/yyyy")
					+ "/" + mailSenderUtility.timeConverter(interviewTime);
		} else {
			interviewScheduleDate = DateParser.toDate(interviewDate).toString(
					"dd MMM yyyy");
			time = DateParser.toDate(interviewDate).toString("dd/MM/yyyy")
					+ "/" + mailSenderUtility.timeConverter(interviewTime);
		}

		String subjectForCandidate = "Interview at Ray Business Technologies";
		String mailContent = null;

		if (Integer.parseInt(round) > 1) {
			mailContent = mailTemplatesDaoImpl
					.getMailContent("Schedule Interview For Next Round");
		} else {
			mailContent = mailTemplatesDaoImpl
					.getMailContent("Schedule Interview");
		}

		String jd = (candidate.getAppliedFor().getDescription() != null) ? candidate
				.getAppliedFor().getDescription() : "";

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", subjectForCandidate);
		details.put("toName", candidate.getFullName());
		details.put("position", candidate.getAppliedForLookUp());
		details.put("jd", jd);
		details.put("date", interviewScheduleDate);
		details.put("time", interviewTime);
		details.put("type", interviewType);
		details.put("round", round);
		details.put("cvDetails", cvDetails);
		details.put("to", candidate.getEmail() != null ? candidate.getEmail()
				: "N/A");
		details.put("bcc", bccemailId);
		details.put("cc", "");
		if (mailContent != null) {

			if (sendMailToCandidate) {
				mailContentParser.parseAndSendMail(details, mailContent);
			}
			if (sendMailToInterviewer) {
				sendInterviewerMail(candidate, interviewScheduleDate,
						interviewTime, interviewType, round, bccemailId);
				Set<Employee> interviewers = candidate.getInterview()
						.getInterviewers();
				String bccInterviewersEmailId = "";
				for (Employee employee : interviewers) {
					bccInterviewersEmailId += employee.getEmail() + ",";
				}
				String agenda = interviewType + " interview scheduled - "
						+ candidate.getFullName();
				String meetingId = candidate.getMobile() + "_" + interviewDate
						+ "_" + interviewTime + "_" + round
						+ bccInterviewersEmailId;

				mailSenderUtility.send(agenda, time, bccInterviewersEmailId,
						meetingId);

			}
		} else {
			throw new MailCantSendException(
					"Schedule Interview For Next Round / Schedule Interview mail content not available");
		}

		// AFTER SENDING MAIL TO CANDIDATE SEND MAIL TO INTERVIEWR//

		// Sending meeting Invitation to Interviewer//
		/*if (sendMailToInterviewer) {
			Set<Employee> interviewers = candidate.getInterview()
					.getInterviewers();
			String bccInterviewersEmailId = "";
			for (Employee employee : interviewers) {
				bccInterviewersEmailId += employee.getEmail() + ",";
			}
			String agenda = interviewType + " interview scheduled - "
					+ candidate.getFullName();
			String meetingId = candidate.getMobile() + "_" + interviewDate
					+ "_" + interviewTime + "_" + round
					+ bccInterviewersEmailId;

			mailSenderUtility.send(agenda, time, bccInterviewersEmailId,
					meetingId);

		}*/

	}

	public void sendRescheduleMail(Candidate candidate, String interviewDate,
			String interviewTime, String round, String interviewMode)
			throws Exception {

		String bccemailId = (String) propBean.getPropData()
				.get("mail.hrMailId");

		String interviewType = interviewMode;

		String cvDetails = "<br><br>Please bring your updated CV at the time of interview. ";
		if (interviewType.equalsIgnoreCase("TELEPHONE")) {
			interviewType = "Telephonic";
			cvDetails = "";
		} else if (interviewType.equalsIgnoreCase("FACE_TO_FACE")) {
			interviewType = "In-person";
		}
		if (Integer.parseInt(round) > 1) {
			cvDetails = "";
		}

		String interviewScheduleDate = null;
		String time = null;
		if (interviewDate.contains(" ")) {
			interviewScheduleDate = DateParser.toDateOtherFormat(interviewDate)
					.toString("dd MMM yyyy");
			time = DateParser.toDateOtherFormat(interviewDate).toString(
					"dd/MM/yyyy")
					+ "/" + mailSenderUtility.timeConverter(interviewTime);
		} else {
			interviewScheduleDate = DateParser.toDate(interviewDate).toString(
					"dd MMM yyyy");
			time = DateParser.toDate(interviewDate).toString("dd/MM/yyyy")
					+ "/" + mailSenderUtility.timeConverter(interviewTime);
		}

		String subjectForCandidate = "Interview at Ray Business Technologies";
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", subjectForCandidate);
		details.put("toName", candidate.getFullName());
		details.put("date", interviewScheduleDate);
		details.put("time", interviewTime);
		details.put("type", interviewType);
		details.put("round", round);
		details.put("cvDetails", cvDetails);
		details.put("to", candidate.getEmail());
		details.put("bcc", bccemailId);
		details.put("cc", "");

		String mailContent = mailTemplatesDaoImpl
				.getMailContent("Rescheduled Interview");

		if (mailContent != null) {
			mailContentParser.parseAndSendMail(details, mailContent);
		} else {
			throw new MailCantSendException(
					"Rescheduled Interview not available");
		}

		// Send Interviewer mail after rescheduling interview//
		sendInterviewerMail(candidate, interviewScheduleDate, interviewTime,
				interviewMode, round, bccemailId);

		// Sending meeting Invitation to Interviewer//

		Set<Employee> interviewers = candidate.getInterview().getInterviewers();
		String bccInterviewersEmailId = null;
		for (Employee employee : interviewers) {
			bccInterviewersEmailId += employee.getEmail() + ",";
		}

		String agenda = interviewType + " interview scheduled - "
				+ candidate.getFullName();
		String meetingId = candidate.getMobile() + "_" + interviewDate + "_"
				+ interviewTime + "_" + round + bccInterviewersEmailId;

		// We are using mailSenderUtility because we are sending ICS calender to
		// interviewer //
		mailSenderUtility.send(agenda, time, bccInterviewersEmailId, meetingId);

	}

	// Sending Mail to Interviewer on scheduling interview//
	public void sendInterviewerMail(Candidate candidate,
			String interviewScheduleDate, String interviewTime,
			String interviewType, String interviewRound, String bccemailId) {

		String subjectForInterviewer = "Reg: Interview schedule on "
				+ interviewScheduleDate + " at " + interviewTime;

		String employeeNames = "";
		String to = "";

		Set<Employee> interviewers = candidate.getInterview().getInterviewers();
		for (Employee employee : interviewers) {
			employeeNames = employee.getFullName();
			to += employee.getEmail() + ",";
		}
		if (interviewers.size() > 1) {
			employeeNames = "Associates";
		}

		String mailContent = mailTemplatesDaoImpl
				.getMailContent("Interviewer Mail");

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", subjectForInterviewer);
		details.put("toName", employeeNames);
		details.put("name", candidate.getFullName());
		details.put("position", candidate.getAppliedForLookUp());
		details.put("exp", candidate.getExperience());
		details.put("mail",
				(candidate.getEmail() != null) ? candidate.getEmail() : "N/A");
		details.put("date", interviewScheduleDate);
		details.put("time", interviewTime);
		details.put("type", interviewType);
		details.put("round", interviewRound);
		details.put("to", to);
		details.put("bcc", bccemailId);
		details.put("cc", "");

		if (mailContent != null) {
			mailContentParser.parseAndSendMail(details, mailContent);
		} else {
			throw new MailCantSendException(
					"Interviewer mail content not available");
		}

	}

	public void sendMailToNewEmployee(Employee employee) {

		String cc = (String) propBean.getPropData().get("mail.hrMailId");

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "Reg: OVH");
		details.put("toName", employee.getFullName());
		details.put("to", employee.getEmail());
		details.put("bcc", "");
		details.put("cc", cc);

		String mailContent = mailTemplatesDaoImpl
				.getMailContent("New Employee");

		if (mailContent != null) {
			mailContentParser.parseAndSendMail(details, mailContent);
		} else {
			throw new MailCantSendException(
					"New Employee mail Template  not available");
		}

	}

	// THIS METHOD VALIDATES ON RESCHEDULING INTERVIEW THE MAIL SHOULD GO TO
	// ONLY INTERVIEWER OR CANDIDATE ALSO//
	public void sendRescheduleMailOnCondition(Candidate candidate,
			CandidateScheduleDto candidateScheduleDto, Employee interviewr,
			CandidateInterviewCycle candidateInterviewCycle) throws Exception {

		String bccemailId = (String) propBean.getPropData()
				.get("mail.hrMailId");

		String interviewType = candidateScheduleDto.getInterviewType();
		String interviewDate = candidateScheduleDto.getScheduleDate();
		String interviewTime = candidateScheduleDto.getScheduleTime();
		String round = candidate.getInterview().getRound();
		Boolean sendMailToCandidate = candidateScheduleDto
				.getSendMailToCandidate();
		Boolean sendMailToInterviewer = candidateScheduleDto
				.getSendMailToInterviewer();

		String cvDetails = "<br><br>Please bring your updated CV at the time of interview. ";
		if (interviewType.equalsIgnoreCase("TELEPHONE")) {
			interviewType = "Telephonic";
			cvDetails = "";
		} else if (interviewType.equalsIgnoreCase("FACE_TO_FACE")) {
			interviewType = "In-person";

		}
		if (Integer.parseInt(round) > 1) {
			cvDetails = "";
		}

		String interviewScheduleDate = null;
		String time = null;
		if (interviewDate.contains(" ")) {
			interviewScheduleDate = DateParser.toDateOtherFormat(interviewDate)
					.toString("dd MMM yyyy");
			time = DateParser.toDateOtherFormat(interviewDate).toString(
					"dd/MM/yyyy")
					+ "/" + mailSenderUtility.timeConverter(interviewTime);
		} else {
			interviewScheduleDate = DateParser.toDate(interviewDate).toString(
					"dd MMM yyyy");
			time = DateParser.toDate(interviewDate).toString("dd/MM/yyyy")
					+ "/" + mailSenderUtility.timeConverter(interviewTime);
		}

		Set<Employee> interviewers = candidate.getInterview().getInterviewers();
		String bccInterviewersEmailId = null;
		for (Employee employee : interviewers) {
			bccInterviewersEmailId += employee.getEmail() + ",";
		}

		String subjectForCandidate = "Interview at Ray Business Technologies";

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", subjectForCandidate);
		details.put("toName", candidate.getFullName());
		details.put("date", interviewScheduleDate);
		details.put("time", interviewTime);
		details.put("type", interviewType);
		details.put("round", round);
		details.put("cvDetails", cvDetails);
		details.put("to", candidate.getEmail());
		details.put("bcc", bccemailId);
		details.put("cc", "");

		// ONLY SENDING MAIL FOR INTERVIEWER//
		if (candidateInterviewCycle.getInterviewDate().toString("dd/MM/yyyy")
				.equalsIgnoreCase(candidateScheduleDto.getScheduleDate())
				&& candidateInterviewCycle.getInterviewTime().equalsIgnoreCase(
						candidateScheduleDto.getScheduleTime())
				&& !candidateInterviewCycle.getInterviewers().equalsIgnoreCase(
						interviewr.getFullName())) {

			// Send Interviewer mail after rescheduling interview//
			sendInterviewerMail(candidate, interviewScheduleDate,
					interviewTime, interviewType, round, bccemailId);

			// Sending meeting Invitation to Interviewer//

			String agenda = interviewType + " interview scheduled - "
					+ candidate.getFullName();
			String meetingId = candidate.getMobile() + "_" + interviewDate
					+ "_" + interviewTime + "_" + round
					+ bccInterviewersEmailId;

			// We are using mailSenderUtility because we are sending ICS
			// calender to
			// interviewer //
			mailSenderUtility.send(agenda, time, bccInterviewersEmailId,
					meetingId);

		} else {

			// SENDING MAIL TO CANDIDATE AS WELL AS INTERVIEWER
			String mailContent = mailTemplatesDaoImpl
					.getMailContent("Rescheduled Interview");

			if (mailContent != null) {

				if (sendMailToCandidate) {

					mailContentParser.parseAndSendMail(details, mailContent);
				}
				if (sendMailToInterviewer) {
					sendInterviewerMail(candidate, interviewScheduleDate,
							interviewTime, interviewType, round, bccemailId);

					// Sending meeting Invitation to Interviewer//

					String agenda = interviewType + " interview scheduled - "
							+ candidate.getFullName();
					String meetingId = candidate.getMobile() + "_" + interviewDate
							+ "_" + interviewTime + "_" + round
							+ bccInterviewersEmailId;

					// We are using mailSenderUtility because we are sending ICS
					// calender to
					// interviewer //
					mailSenderUtility.send(agenda, time, bccInterviewersEmailId,
							meetingId);
				}
			} else {
				throw new MailCantSendException(
						"Rescheduled Interview not available");
			}

			// Send Interviewer mail after rescheduling interview//
			/*if (sendMailToInterviewer) {
				sendInterviewerMail(candidate, interviewScheduleDate,
						interviewTime, interviewType, round, bccemailId);

				// Sending meeting Invitation to Interviewer//

				String agenda = interviewType + " interview scheduled - "
						+ candidate.getFullName();
				String meetingId = candidate.getMobile() + "_" + interviewDate
						+ "_" + interviewTime + "_" + round
						+ bccInterviewersEmailId;

				// We are using mailSenderUtility because we are sending ICS
				// calender to
				// interviewer //
				mailSenderUtility.send(agenda, time, bccInterviewersEmailId,
						meetingId);
			}*/

		}

	}
	
}
