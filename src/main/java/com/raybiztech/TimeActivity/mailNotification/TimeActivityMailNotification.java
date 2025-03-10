package com.raybiztech.TimeActivity.mailNotification;

import java.util.Calendar;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.mailtemplates.util.MailContentParser;

@Component("timeActivityMailNotification")
public class TimeActivityMailNotification {
	

	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;
	
	@Autowired
	MailContentParser mailContentParser;
	

	@Autowired
	PropBean propBean;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	SecurityUtils securityUtils;
	
	
	
	public void sendTimeSheetAlert(String emp, String pmMail){
		// For sending mail we need to set all the value which is required.
		String to = emp;
		String cc = pmMail;
		String content = null;
		Boolean flag = true;
		
		String regarding = "Time-sheets Reminder";
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[" + regarding + "]]>");
		details.put("to", to);
		details.put("cc", cc);
		details.put("bcc", "");
		
		content = mailTemplatesDaoImpl.getMailContent("Time-sheets Reminder");
		
		if(content != null) {
			mailContentParser.parseAndSendMail(details, content);
			System.out.println("Mail Send");
			
		}
		else {
			throw new MailCantSendException("Mail Content of 'Time Sheet Aleart' type is not available");
		}
	}



	public void sendMonthlyTimeSheetAleart(String pmMailId,String nameList,String pmName, String projName) {
		// For sending mail we need to set all the value which is required.
		
				// Getting Current Month name
				Calendar cal = Calendar.getInstance();
				Formatter fmt = new Formatter();
				String month = (fmt.format("%tB", cal)).toString();
				System.out.println("Current Month Name is = "+ month);
				
				String to = null;
				String cc = (String) propBean.getPropData().get("mail.sqaMailId");
				String content = null;
				Boolean flag = true;
				
				to = pmMailId;
				
				System.out.println("In sendind mail alert");
				System.out.println("PM Name = "+pmName);
				//cc = pmMail;
				String regarding = projName +"-"+month+" "+" Time-sheets Alert";
				Map<String, String> details = new HashMap<String, String>();
				details.put("regarding", "<![CDATA[" + regarding + "]]>");
				details.put("to", to);
				details.put("cc", cc);
				details.put("bcc", "");
				details.put("name", pmName);
				details.put("Comment", nameList);
				details.put("month", month);
				
				content = mailTemplatesDaoImpl.getMailContent("Monthly Time Sheet");
				
				if(content != null) {
					mailContentParser.parseAndSendMail(details, content);
					System.out.println("Mail Send");
				}
				else {
					throw new MailCantSendException("Mail Content of ' Monthly Hive Time Sheet Aleart' type is not available");
				}
		
	}

}