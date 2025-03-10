package com.raybiztech.appraisals.observation.mailNotification;

import java.text.ParseException;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.observation.dao.ObservationDAO;
import com.raybiztech.appraisals.observation.dto.ObservationDTO;
import com.raybiztech.date.Date;
import com.raybiztech.mail.sender.MessageSender;

@Component("observationMailNotification")
public class ObservationMailNotification {

	@Autowired
	MessageSender messageSender;

	@Autowired
	ObservationDAO observationDAO;

	public static Logger logger = Logger
			.getLogger(ObservationMailNotification.class);

	public void sendObservationMailNotification(ObservationDTO observationDTO) {

		Employee employee = observationDAO.findBy(Employee.class,
				observationDTO.getEmployee().getPersonId());
		Employee addedBy = observationDAO.findBy(Employee.class, observationDTO
				.getAddedBy().getPersonId());
		String observationMonth = observationDTO.getObservationMonth();
		Date date = null;
		try {
			date = Date.parse(observationMonth, "MM/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String temp[] = date.toString().split(" ");
		String finalMonth = temp[1] + " " + temp[2];
		String givenByName = addedBy.getEmployeeFullName();
		String employeeName = employee.getFullName();
		String cc = addedBy.getEmail();

		String to = "";
		String bcc = "";
		String subject = "<![CDATA[ Observation for ]]>" + finalMonth;
		StringBuffer buffer = new StringBuffer();

		to = employee.getEmail();
		buffer.append("<![CDATA[Dear " + employeeName)
				.append(", <br><br><br> Your Observation for <b>" + finalMonth
						+ "</b> month has been added by Mr./Ms. " + givenByName
						+ ".  To view the Observation login to OVH.")
				.append(" <br><br><br> Regards,<br>")
				.append(givenByName
						+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."
						+ "]]>");
		String body = buffer.toString();

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");

		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}
}
