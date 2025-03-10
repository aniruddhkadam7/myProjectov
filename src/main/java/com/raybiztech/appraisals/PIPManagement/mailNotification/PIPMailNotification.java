package com.raybiztech.appraisals.PIPManagement.mailNotification;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.PIPManagement.dao.PIPDao;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.mail.sender.MessageSender;

@Component("PIPMailNotification")
public class PIPMailNotification {

	@Autowired
	PropBean propBean;

	@Autowired
	MessageSender messageSender;

	@Autowired
	PIPDao pipDao;

	public static Logger logger = Logger.getLogger(PIPMailNotification.class);

	public void sendPIPMailNotification(PIP pip) {
		

		Employee employee = pipDao.findBy(Employee.class, pip.getEmployee().getEmployeeId());
		Employee givenBy = pipDao.findBy(Employee.class, pip.getCreatedBy());

		String employeeName = employee.getEmployeeFullName();
		String givenByName = givenBy.getEmployeeFullName();
		String cc = (String) propBean.getPropData().get("mail.hrMailId");

		cc = cc + " ," + givenBy.getEmail();

		String to = "";
		String bcc = "";
		String subject = "<![CDATA[ PIP ]]>";
		StringBuffer buffer = new StringBuffer();

		to = employee.getEmail();
		buffer.append("<![CDATA[Dear " + employeeName)
				.append(", <br><br><br> You " 
						+ "  has been added into PIP  by Mr./Ms.  <b> " + givenByName
						+ "</b>.  To view the PIP details login to OVH.")
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
