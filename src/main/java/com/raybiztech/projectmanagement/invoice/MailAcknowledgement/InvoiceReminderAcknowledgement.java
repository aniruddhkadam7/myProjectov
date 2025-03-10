package com.raybiztech.projectmanagement.invoice.MailAcknowledgement;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.mail.sender.MessageSender;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceReminderLogDTO;

@Component("invoiceReminderAcknowledgement")
public class InvoiceReminderAcknowledgement {

	@Autowired
	MessageSender messageSender;

	@Autowired
	PropBean propBean;

	Logger logger = Logger.getLogger(InvoiceReminderAcknowledgement.class);

	public void sendInvoiceReminder(InvoiceReminderLogDTO invoiceReminderLogDto,HttpServletRequest request) {

		String to = invoiceReminderLogDto.getTo();
		String cc = invoiceReminderLogDto.getCc() != null ? invoiceReminderLogDto
				.getCc() : "";
		String bcc = invoiceReminderLogDto.getBcc() != null ? invoiceReminderLogDto
				.getBcc() : "";
		String invoiceSubject = invoiceReminderLogDto.getSubject();
		String description = invoiceReminderLogDto.getDescription();
		String detail = invoiceReminderLogDto.getDetails();

		String subject = "<![CDATA[ " + invoiceSubject + " ]]>";
		StringBuffer buffer = new StringBuffer();
		
		/*if(tenantkey.equalsIgnoreCase("RAYBIZTECH")){
			 headerPath = filePath + "header.html";
			 footerPath = filePath + "footer.html";
			}else if(tenantkey.equalsIgnoreCase("AIBRIDGEML")){
				 headerPath = filePath + "header1.html";
				 footerPath = filePath + "footer1.html";
			}*/
		String tenantkey =request.getHeader("tenantkey");
		if(tenantkey.equalsIgnoreCase("RAYBIZTECH")){
		buffer.append("<![CDATA[")
				.append(" <br> " + description + "<br><br> " + detail)
				.append(" <br><br><br> Regards,<br> Raybiztech")
				.append(" <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA."
						+ "]]>");
		}else if(tenantkey.equalsIgnoreCase("AIBRIDGEML")){
			buffer.append("<![CDATA[")
			.append(" <br> " + description + "<br><br> " + detail)
			.append(" <br><br><br> Regards,<br> AIBridgeML")
			.append(" <br><br> AIBridgeML"
					+ "]]>");
		}
		String body = buffer.toString() + "unique for Account mail only";

		try {
			messageSender.sendMessage("<email><to>" + to + "</to>" + "<cc>"
					+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>"
					+ subject + "</subject>" + "<body>" + body
					+ "</body></email>");

		} catch (JMSException je) {
			logger.error(je.getMessage());
		}
	}
}
