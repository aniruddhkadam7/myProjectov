package com.raybiztech.projectmanagement.invoice.utility;

import java.io.File;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.date.Date;
import com.raybiztech.mail.sender.MessageSender;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.business.InvoiceStatus;

@Component
public class ProjectNotification {

	@Autowired
	MessageSender messageSender;

	@Autowired
	PropBean propBean;

	public static Logger logger = Logger.getLogger(ProjectNotification.class);

	public void remindMilestoneDelay(Project project, String cc,
			String milestone, String status) {
		String bcc = " ";
		String subject = "<![CDATA[ Milestone Delay ]]>";
		String content = (String) propBean.getPropData().get(status);
		String manager = project.getProjectManager().getFullName();

		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Hi ")
				.append(manager + ",<br><br>")
				.append(project.getProjectName() + "'s " + milestone + " ")
				.append(content)
				.append(" <br><br><br> Best Regards,<br>Ray Business Technologies Pvt Ltd<br>")
				.append(" ]]>");

		String body = buffer.toString();

		logger.info("to is " + project.getProjectManager().getEmail()
				+ " cc is " + cc + " bcc is " + bcc + " subject is " + subject
				+ " body is " + body);

		try {
			messageSender.sendMessage("<email><to>"
					+ project.getProjectManager().getEmail() + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}
	}

	public void alertMilestoneClosing(Project project, String milestone,
			String status) {

		String to = project.getProjectManager().getEmail();
		String cc = (String) propBean.getPropData().get("mail.hrMailId") + ","
				+ propBean.getPropData().get("mail.financeMailId");

		String bcc = " ";
		String subject = "<![CDATA[ Milestone Closed ]]>";
		StringBuffer buffer = new StringBuffer();

		buffer.append(
				"<![CDATA[ Dear " + project.getProjectManager().getFullName())
				.append(",<br><br> We would like to inform you that the ")
				.append("<b>" + milestone + "</b>" + " milestone of project ")
				.append("<b>" + project.getProjectName() + "</b>"
						+ " was closed ")
				.append(".<br> <br>For further queries, feel free to contact concern department ")
				.append(". <br><br><br> Regards,<br>")
				.append("HR <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA.]]>");

		String body = buffer.toString();
		logger.warn(" Sending mail for close milestone........");
		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");
			logger.warn("milestone mail sent successfully");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}

	public void emailInoviceToClient(Invoice invoice, String cc,
			String getPDFPath, String month, String year) {

		invoice.setInvoiceStatus("SENT");
		invoice.setInvoiceAmountSentDate(new Date());

		File file = new File(getPDFPath);

		Client client = invoice.getMilsestone().getProject().getClient();

		String bcc = " ";
		String subject = "<![CDATA[ Invoice ]]>";
		String content = (String) propBean.getPropData().get("raiseInvoice");

		StringBuffer buffer = new StringBuffer();

		buffer.append("<![CDATA[Hi ")
				.append(client.getPersonName() + ",<br><br>")
				.append(content)
				.append(month)
				.append(" Month")
				/* .append(invoice.getMilsestone().getTitle()) */
				.append(" of ")
				.append(year + ".")
				.append("<br><br>Feel free to contact Finance department for further queries..")
				.append(" <br><br><br> Best Regards,<br>Ray Business Technologies Pvt Ltd<br>Visit: www.raybiztech.com<br>Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA")
				.append(" ]]>");

		String body = buffer.toString();

		logger.warn(body);

		try {
			messageSender.sendMessage("<email><to>" + client.getEmail()
					+ "</to>" + "<cc>" + cc + "</cc>" + "<bcc>" + bcc
					+ "</bcc>" + "<subject>" + subject + "</subject>"
					+ "<body>" + body + "</body><path>" + getPDFPath
					+ "</path></email>");
			logger.warn("message sent to queue");
			// file.delete();

		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}
}
