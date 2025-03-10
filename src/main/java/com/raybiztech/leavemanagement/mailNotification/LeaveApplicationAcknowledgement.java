package com.raybiztech.leavemanagement.mailNotification;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.mail.sender.MessageSender;

@Component("leaveApplicationAcknowledgement")
public class LeaveApplicationAcknowledgement {

	@Autowired
	MessageSender messageSender;

	@Autowired
	PropBean propBean;
        
	public PropBean getPropBean() {
		return propBean;
	}

	public void setPropBean(PropBean propBean) {
		this.propBean = propBean;
	}

	public static Logger logger = Logger
			.getLogger(LeaveApplicationAcknowledgement.class);

	public void sendLeaveApplicationAcknowledgement(LeaveDebit leaveDebits) {

		String to = (String) propBean.getPropData().get("mail.hrMailId");

		String cc = leaveDebits.getEmployee().getEmail();
		if (leaveDebits.getEmployee().getManager() != null
				&& !leaveDebits.getEmployee().getManager().getEmployeeId()
						.equals(1001L)) {
			to = to + " ," + leaveDebits.getEmployee().getManager().getEmail();

		}

		String bcc = " ";
		String reason = "";
		if (leaveDebits.getEmployeeComments() != null) {
			reason = ", because of " + leaveDebits.getEmployeeComments();
		}
		String subject = "<![CDATA[ Leave Application ]]>";
		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Hi,<br><br> I am ")
				.append(leaveDebits.getEmployee().getFullName())
				.append(", applied leave from ")
				.append(leaveDebits.getPeriod().getMinimum().toString())
				.append(" to ")
				.append(leaveDebits.getPeriod().getMaximum().toString())
				.append(reason)
				.append(". So, please grant me leave. <br><br><br> Best Regards,<br>")
				.append(leaveDebits.getEmployee().getFullName() + "<br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."+"]]>");

		String body = buffer.toString();

		logger.info("Sending message ..... ");

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}

	public void sendLeaveCancellationAcknowledgement(LeaveDebit leaveDebits) {

		String to = (String) propBean.getPropData().get("mail.hrMailId");
		String cc = leaveDebits.getEmployee().getEmail();
		if (leaveDebits.getEmployee().getManager() != null
				&& !leaveDebits.getEmployee().getManager().getEmployeeId()
						.equals(1001L)) {
			to = to + " ," + leaveDebits.getEmployee().getManager().getEmail()+","+
					(leaveDebits.getEmployee().getProjectManager() !=null ?
							leaveDebits.getEmployee().getProjectManager().getEmail():"");
		}
		String bcc = " ";
		String subject = "<![CDATA[ Leave Application ]]>";

		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Hi,<br><br> I am ")
				.append(leaveDebits.getEmployee().getFullName())
				.append(", applied leave from ")
				.append(leaveDebits.getPeriod().getMinimum().toString())
				.append(" to ")
				.append(leaveDebits.getPeriod().getMaximum().toString())
				.append(", I want to cancel my leave. <br><br><br> Best Regards,<br>")
				.append(leaveDebits.getEmployee().getFullName() + "<br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."+"]]>");

		String body = buffer.toString();

		logger.info("Sending message ..... ");

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}

	public void sendLeaveRejectionAcknowledgement(LeaveDebit leaveDebits) {

		String to = leaveDebits.getEmployee().getEmail();
		String cc = (String) propBean.getPropData().get("mail.hrMailId");
		if (leaveDebits.getEmployee().getManager() != null
				&& !leaveDebits.getEmployee().getManager().getEmployeeId()
						.equals(1001L)) {
			cc = cc + "," + leaveDebits.getEmployee().getManager().getEmail();
		}
		String bcc = " ";
		String subject = "<![CDATA[ Leave Application ]]>";
		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Hi ")
				.append(leaveDebits.getEmployee().getFullName())
				.append(", your applied leave from ")
				.append(leaveDebits.getPeriod().getMinimum().toString())
				.append(" to ")
				.append(leaveDebits.getPeriod().getMaximum().toString())
				.append(" has been Rejected. <br><br><br> Best Regards,<br><b>")
				.append("Ray Business Technologies Pvt Ltd <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA." + "]]>");

		String body = buffer.toString();

		logger.info("Sending message ..... ");

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}

	public void sendLeaveApproveAcknowledgement(LeaveDebit leaveDebits) {

		String to = leaveDebits.getEmployee().getEmail();
		String cc = (String) propBean.getPropData().get("mail.hrMailId");
		if (leaveDebits.getEmployee().getManager() != null
				&& !leaveDebits.getEmployee().getManager().getEmployeeId()
						.equals(1001L)) {
			cc = cc + "," + leaveDebits.getEmployee().getManager().getEmail();
		}
		String bcc = " ";
		String subject = "<![CDATA[ Leave Application ]]>";

		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Hi ")
				.append(leaveDebits.getEmployee().getFullName())
				.append(", your applied leave from ")
				.append(leaveDebits.getPeriod().getMinimum().toString())
				.append(" to ")
				.append(leaveDebits.getPeriod().getMaximum().toString())
				.append(" has been Approved. <br><br><br> Best Regards,<br><b>")
				.append("Ray Business Technologies Pvt Ltd <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA." + "]]>");

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

	public void sendLeavePendingApprovalAlert(Employee employee, String cc) {

		String to = employee.getEmail();

		String subject = "<![CDATA[Pending Leave Approvals ]]>";
		String bcc = " ";
		// cc=" ";

		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Hi ")
				.append(employee.getFullName())
				.append(", please Approve all pending leaves.")
				.append(" <br><br><br> Best Regards,<br>HR<br>Ray Business Technologies Pvt Ltd<br>")
				.append(" ]]>");

		String body = buffer.toString();

		logger.info("Sending message ..... ");

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}
        
	public void weeklyStatusReportReminderMail(String leads) {

		String to = leads;

		String cc = (String) propBean.getPropData().get("mail.hrMailId");
		String subject = "<![CDATA[Weekly status report - Reminder ]]>";
		String bcc = " ";

		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Dear Associates,")
				.append("<br><br>Please update your weekly status report of all the projects in Hive today on or before 6:00 PM (without fail). Please mark Mr. Gupta and Mr. Chait as watchers.<br><br>Feel free to contact HR desk for further queries.")
				.append(" <br><br><br> Best Regards,<br>HR<br>Ray Business Technologies Pvt Ltd<br><br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA.")
				.append(" ]]>");

		String body = buffer.toString();

		logger.info("Sending message ..... ");

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
