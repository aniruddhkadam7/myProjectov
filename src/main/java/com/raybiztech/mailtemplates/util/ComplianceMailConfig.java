package com.raybiztech.mailtemplates.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;

@Component("complianceMailConfig")
public class ComplianceMailConfig {
	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;
	 
	@Autowired
	MailContentParser mailContentParser;
	
	@Autowired
	PropBean propBean;
	
	@Autowired
	SecurityUtils securityUtils;
	
	public void sendComplainceMail(String toAddress,String cc){
		
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[ComplianceMail]]>");
		
		//details.put("name", loggedEmployee.getFullName());
		details.put("to", toAddress);
		details.put("cc", cc);
		details.put("bcc", "");
		String content = mailTemplatesDaoImpl.getMailContent("ComplianceMail");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Compliance Mail' template Type");
		
		
		
	}
	}
}
