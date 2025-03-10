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


@Component("itDeclarationMailConfiguration")
public class ITDeclarationMailConfiguration {
	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;
	 
	@Autowired
	MailContentParser mailContentParser;
	
	@Autowired
	PropBean propBean;
	
	@Autowired
	SecurityUtils securityUtils;
	
	public void sendITFormSubmitMail(Long itDeclarationFormId){
		Employee loggedEmployee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		String to=(String) propBean.getPropData().get("mail.financeMailId");

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[IT Declaration Form]]>");
		
		details.put("name", loggedEmployee.getFullName());
		details.put("to", to);
		details.put("cc", "");
		details.put("bcc", "");
		String content = mailTemplatesDaoImpl.getMailContent("ITDeclarationFormMail");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'ITDeclarationFormMail' template Type");
		
		
		
	}
	}
	
	

}
