package com.raybiztech.mailtemplates.util;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.achievementNomination.business.Nomination;
import com.raybiztech.achievementNomination.business.NominationCycle;
import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.observation.dto.ObservationDTO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.date.Date;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;

@Component("performanceManagementMailConfiguration")
public class PerformanceManagementMailConfiguration {

	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;
	@Autowired
	MailContentParser mailContentParser;
	@Autowired
	PropBean propBean;

	public void sendObservationMailNotification(ObservationDTO observationDTO) {

		Employee employee = mailTemplatesDaoImpl.findBy(Employee.class,
				observationDTO.getEmployee().getPersonId());
		Employee addedBy = mailTemplatesDaoImpl.findBy(Employee.class,
				observationDTO.getAddedBy().getPersonId());
		String observationMonth = observationDTO.getObservationMonth();
		Date date = null;
		try {
			date = Date.parse(observationMonth, "MM/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String temp[] = date.toString().split(" ");
		String finalMonth = temp[1] + " " + temp[2];
		String regarding = "<![CDATA[ Observation for ]]>" + finalMonth;

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", regarding);
		details.put("name", addedBy.getEmployeeFullName());
		details.put("toName", employee.getFullName());
		details.put("month", finalMonth);
		details.put("cc", addedBy.getEmail());
		details.put("to", employee.getEmail());
		details.put("bcc", "");

		String content = mailTemplatesDaoImpl.getMailContent("Observation");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Observation' template Type");
		}

	}

	public void sendPIPMailNotification(PIP pip) {

		Employee employee = mailTemplatesDaoImpl.findBy(Employee.class, pip
				.getEmployee().getEmployeeId());
		Employee givenBy = mailTemplatesDaoImpl.findBy(Employee.class,
				pip.getCreatedBy());

		String cc = (String) propBean.getPropData().get("mail.hrMailId");
		cc = cc + " ," + givenBy.getEmail();

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[ PIP ]]>");
		details.put("name", givenBy.getEmployeeFullName());
		details.put("toName", employee.getFullName());
		details.put("cc", cc);
		details.put("to", employee.getEmail());
		details.put("bcc", "");

		String content = mailTemplatesDaoImpl.getMailContent("PIP");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Observation' template Type");
		}

	}

	public void sendNominationMailNotification(Nomination nomination) {

		Employee employee = nomination.getEmployee();
		Employee givenBy = mailTemplatesDaoImpl.findBy(Employee.class,
				nomination.getCreatedBy());
		NominationCycle cycle = nomination.getNominationCycleId();
		String startingMonth = cycle.getFromMonth().toString("MM/yyyy");
		String endingMonth = cycle.getToMonth().toString("MM/yyyy");

		String to = (String) propBean.getPropData().get("mail.hrMailId");
		String cc = givenBy.getEmail();

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[ Nomination for ]]>" + startingMonth
				+ "-" + endingMonth);
		details.put("subject", employee.getFullName());
		details.put("type", nomination.getAchievementType()
				.getAchievementType());
		details.put("name", givenBy.getEmployeeFullName());
		details.put("fromDate", startingMonth);
		details.put("toDate", endingMonth);
		details.put("cc", cc);
		details.put("to", to);
		details.put("bcc", "");
		
		String content = mailTemplatesDaoImpl.getMailContent("Nomination");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Nomination' template Type");
		}

	}
}
