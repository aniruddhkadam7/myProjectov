package com.raybiztech.mailtemplates.util;

import java.util.Map;
import java.util.Map.Entry;

import javax.jms.JMSException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.mail.sender.MessageSender;

@Component("mailContentParser")
public class MailContentParser {

	Logger logger = Logger.getLogger(MailContentParser.class);

	@Autowired
	MessageSender messageSender;

	public void parseAndSendMail(Map<String, String> details, String mailContent) {

		mailContent = "<![CDATA[" + mailContent + "]]>";
		mailContent = mailContent.replace("<div>", "<br>")
				.replace("</div>", "").replace("<br />", "");

		for (Entry<String, String> entry : details.entrySet()) {
			if (entry.getValue() != null) {

				switch (entry.getKey()) {

				case "name":
					mailContent = mailContent.replace("[$$Name$$]",
							details.get("name"));
					break;

				case "toName":
					mailContent = mailContent.replace("[$$ToName$$]",
							details.get("toName"));
					break;

				case "fromDate":
					mailContent = mailContent.replace("[$$From$$]",
							details.get("fromDate"));
					break;

				case "toDate":
					mailContent = mailContent.replace("[$$To$$]",
							details.get("toDate"));
					break;

				case "subject":
					mailContent = mailContent.replace("[$$Subject$$]",
							details.get("subject"));
					break;

				case "month":
					mailContent = mailContent.replace("[$$Month$$]",
							details.get("month"));
					break;

				case "year":
					mailContent = mailContent.replace("[$$Year$$]",
							details.get("year"));
					break;

				case "projectName":
					mailContent = mailContent.replace("[$$ProjectName$$]",
							details.get("projectName"));
					break;

				case "position":
					mailContent = mailContent.replace("[$$Position$$]",
							details.get("position"));
					break;

				case "jd":
					mailContent = mailContent.replace("[$$JD$$]",
							details.get("jd"));
					break;

				case "date":
					mailContent = mailContent.replace("[$$Date$$]",
							details.get("date"));
					break;

				case "time":
					mailContent = mailContent.replace("[$$Time$$]",
							details.get("time"));
					break;

				case "type":
					mailContent = mailContent.replace("[$$Type$$]",
							details.get("type"));
					break;

				case "round":
					mailContent = mailContent.replace("[$$Round$$]",
							details.get("round"));
					break;

				case "cvDetails":
					mailContent = mailContent.replace("[$$CvDetails$$]",
							details.get("cvDetails"));
					break;

				case "mail":
					mailContent = mailContent.replace("[$$Email$$]",
							details.get("mail"));
					break;

				case "exp":
					mailContent = mailContent.replace("[$$Experience$$]",
							details.get("exp"));
					break;

				case "location":
					mailContent = mailContent.replace("[$$Location$$]",
							details.get("location"));
					break;

				case "room":
					mailContent = mailContent.replace("[$$Room$$]",
							details.get("room"));
					break;
				case "Comment":
					mailContent = mailContent.replace("[$$Comment$$]",
							details.get("Comment"));
					break;	
					
				case "projectCode":
					mailContent = mailContent.replace("[$$ProjectCode$$]", 
							details.get("projectCode"));
					break;
					
				case "table":
					mailContent = mailContent.replace("[$$Table$$]", 
							details.get("table"));
					break;

				default:
					break;

				}

			}
		}

		try {
			/*
			 * logger.warn("BCC is " + details.get("bcc") + " to is " +
			 * details.get("to")); logger.warn("mailContent "+mailContent);
			 */

			String path = details.get("path") != null ? "<path>"
					+ details.get("path") + "</path>" : "";

			messageSender.sendMessage("<email><to>" + details.get("to")
					+ "</to>" + "<cc>" + details.get("cc") + "</cc>" + "<bcc>"
					+ details.get("bcc") + "</bcc>" + "<subject>"
					+ details.get("regarding") + "</subject>" + path + "<body>"
					+ mailContent + "</body></email>");
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}
}
