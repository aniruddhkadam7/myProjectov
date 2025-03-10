package com.raybiztech.mailtemplates.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisalmanagement.business.AppraisalForm;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;

@Component("appraisalManagementMailConfiguration")
public class AppraisalManagementMailConfiguration {

	/***
	 * @author shashank
	 * **/

	@Autowired
	PropBean propBean;
	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;
	@Autowired
	MailContentParser mailContentParser;
	@Autowired
	SecurityUtils securityUtils;

	// Sending mail on successfully submission of Employee//

	public void sendEmployeeReviewFormSubmisson(AppraisalForm appraisalForm) {

		String to = "";
		String cc = (String) propBean.getPropData().get("mail.hrMailId");

		Employee employee = appraisalForm.getEmployee();

		if (employee.getEmployeeId().equals(
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder())) {
			to = employee.getManager().getEmail();
			cc = cc + " ," + employee.getEmail();
		}

		String startDate = appraisalForm.getAppraisalCycle()
				.getAppraisalPeriod().getMinimum().toString();
		
		String[] startDate2 = startDate.split(" ");
		
		String endDate = appraisalForm.getAppraisalCycle().getAppraisalPeriod()
				.getMaximum().toString();
		
		String[] endDate2 = endDate.split(" ");

		String reviewType = appraisalForm.getAppraisalCycle().getAppraisalType();
		
		String regarding = "<![CDATA[ " + reviewType + " Review - ]]>"; 
		
		if(startDate2[1].equalsIgnoreCase(endDate2[1])){
			regarding += startDate2[1] + " " + startDate2[2];
		}
		else {
			regarding += "(" + startDate2[1] + "-" + endDate2[1] + ")" + " " + startDate2[2];
		}
		
		
		// + " " + "to" + " "+ endDate2[1] + " " + endDate2[2];

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", regarding);
		details.put("name", employee.getFullName());
		details.put("toName", employee.getManager().getFullName());
		details.put("cc", cc);
		details.put("to", to);
		details.put("bcc", "");

		String content = mailTemplatesDaoImpl
				.getMailContent("Review Form Submission");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Review Form Submission' template Type");
		}

	}

	// Sending mail on manager feedback for Employee Review form //
	public void managerReviewFeedBack(AppraisalForm appraisalForm) {

		String cc = (String) propBean.getPropData().get("mail.hrMailId");

		String empIds[] = appraisalForm.getManagesList().split(",");
		Employee empManager = mailTemplatesDaoImpl.findBy(Employee.class,
				Long.parseLong(empIds[0]));
		String to = appraisalForm.getEmployee().getEmail();
		cc = cc + " ," + empManager.getEmail();

		String startDate = appraisalForm.getAppraisalCycle()
				.getAppraisalPeriod().getMinimum().toString();
		
		String[] startDate2 = startDate.split(" ");
		String endDate = appraisalForm.getAppraisalCycle().getAppraisalPeriod()
				.getMaximum().toString();
		
		String[] endDate2 = endDate.split(" ");
		String reviewType = appraisalForm.getAppraisalCycle().getAppraisalType();
		
		String regarding = "<![CDATA[ " + reviewType + " Review - ]]>"; 
		
		if(startDate2[1].equalsIgnoreCase(endDate2[1])){
			regarding += startDate2[1] + " " + startDate2[2];
		}
		else {
			regarding += "(" + startDate2[1] + "-" + endDate2[1] + ")" + " " + startDate2[2];
		}
		// + " " + "to" + " "+ endDate2[1] + " " + endDate2[2];

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", regarding);
		// details.put("subject", empManager.getFullName());
		details.put("toName", appraisalForm.getEmployee().getFullName());
		details.put("name", empManager.getFullName());
		details.put("cc", cc);
		details.put("to", to);
		details.put("bcc", "");

		String content = mailTemplatesDaoImpl
				.getMailContent("Manager FeedBack Form");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Manager FeedBack Form' template Type");
		}

	}

	// send mail on review acknowledgement//

	public void appraisalFormreviewAcknowledgement(AppraisalForm appraisalForm) {
		String to = "";
		String cc = (String) propBean.getPropData().get("mail.hrMailId");
		Employee employee = appraisalForm.getEmployee();
		String empIds[] = appraisalForm.getManagesList().split(",");
		Employee empManager = mailTemplatesDaoImpl.findBy(Employee.class,
				Long.parseLong(empIds[0]));
		to = empManager.getEmail();
		if (employee.getEmployeeId().equals(
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder())) {
			cc = cc + " ," + employee.getEmail();
		}
		String startDate = appraisalForm.getAppraisalCycle()
				.getAppraisalPeriod().getMinimum().toString();
		
		String[] startDate2 = startDate.split(" ");
		String endDate = appraisalForm.getAppraisalCycle().getAppraisalPeriod()
				.getMaximum().toString();
		
		String[] endDate2 = endDate.split(" ");
		String reviewType = appraisalForm.getAppraisalCycle().getAppraisalType();
		
		String regarding = "<![CDATA[ " + reviewType + " Review - ]]>"; 
		
		if(startDate2[1].equalsIgnoreCase(endDate2[1])){
			regarding += startDate2[1] + " " + startDate2[2];
		}
		else {
			regarding += "(" + startDate2[1] + "-" + endDate2[1] + ")" + " " + startDate2[2];
		}
		// + " " + "to" + " "+ endDate2[1] + " " + endDate2[2];
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", regarding);
		details.put("name", employee.getFullName());
		details.put("toName", employee.getManager().getFullName());
		details.put("cc", cc);
		details.put("to", to);
		details.put("bcc", "");
		String content = mailTemplatesDaoImpl
				.getMailContent("Review Acknowledgement");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Review Acknowledgement' template Type");
		}

	}

	// Sending mail on Request for discussion //

	public void requestForDiscussion(AppraisalForm appraisalForm) {
		String to = "";
		String cc = (String) propBean.getPropData().get("mail.hrMailId");
		Employee employee = appraisalForm.getEmployee();
		String empIds[] = appraisalForm.getManagesList().split(",");
		Employee empManager = mailTemplatesDaoImpl.findBy(Employee.class,
				Long.parseLong(empIds[0]));
		to = empManager.getEmail();
		if (employee.getEmployeeId().equals(
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder())) {
			cc = cc + " ," + employee.getEmail();
		}
		String startDate = appraisalForm.getAppraisalCycle()
				.getAppraisalPeriod().getMinimum().toString();
		
		String[] startDate2 = startDate.split(" ");
		String endDate = appraisalForm.getAppraisalCycle().getAppraisalPeriod()
				.getMaximum().toString();
		
		String[] endDate2 = endDate.split(" ");
		String reviewType = appraisalForm.getAppraisalCycle().getAppraisalType();
		
		String regarding = "<![CDATA[ " + reviewType + " Review - ]]>"; 
		
		if(startDate2[1].equalsIgnoreCase(endDate2[1])){
			regarding += startDate2[1] + " " + startDate2[2];
		}
		else {
			regarding += "(" + startDate2[1] + "-" + endDate2[1] + ")" + " " + startDate2[2];
		}
		// + " " + "to" + " "+ endDate2[1] + " " + endDate2[2];
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", regarding);
		details.put("name", employee.getFullName());
		details.put("toName", employee.getManager().getFullName());
		details.put("cc", cc);
		details.put("to", to);
		details.put("bcc", "");
		String content = mailTemplatesDaoImpl
				.getMailContent("Request For Discussion");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Request For Discussion' template Type");
		}

	}

}
