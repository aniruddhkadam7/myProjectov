package com.raybiztech.mailtemplates.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.icu.impl.duration.impl.DataRecord.EMilliSupport;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;

@Component("leaveManagementMailConfiguration")
public class LeaveManagementMailConfiguration implements Serializable {

	/**
	 * @author shashank
	 */

	@Autowired
	PropBean propBean;
	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;
	@Autowired
	MailContentParser mailContentParser;
	
	@Autowired
	LeaveDAO leaveDAO;

	Logger logger = Logger.getLogger(LeaveManagementMailConfiguration.class);

	private static final long serialVersionUID = 1L;

	// Leave Application//
	public void sendApplyMailNotification(LeaveDebit debit) {

		Map<String, String> details = getDetailsMap(debit);

		String content = mailTemplatesDaoImpl
				.getMailContent("Leave Application");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Leave Application' template Type");
		}

	}

	// Leave Cancellation//
	public void sendCancelLeaveMailNotification(LeaveDebit leaveDebits) {

		Map<String, String> details = getDetailsMap(leaveDebits);

		String content = mailTemplatesDaoImpl
				.getMailContent("Leave Cancellation");
		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Leave Cancellation' template Type");
		}

	}

	// Common Method//
	public Map<String, String> getDetailsMap(LeaveDebit debit) {

		String to = (String) propBean.getPropData().get("mail.hrMailId");
		if (debit.getEmployee().getManager() != null
				&& !debit.getEmployee().getManager().getEmployeeId()
						.equals(1001L)) {
			
			//projectManager = debit.getEmployee().getProjectManager();
			
			to = to + " ," + debit.getEmployee().getManager().getEmail() +","+
			(debit.getEmployee().getProjectManager() !=null ?
					debit.getEmployee().getProjectManager().getEmail():"");
			
		System.out.println("mail ids" + to );

		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[ Leave Application ]]>");
		details.put("name", debit.getEmployee().getFullName());
		details.put("fromDate", debit.getPeriod().getMinimum().toString());
		details.put("toDate", debit.getPeriod().getMaximum().toString());
		details.put("subject", debit.getEmployeeComments());
		details.put("cc", debit.getEmployee().getEmail());
		details.put("to", to);
		details.put("bcc", "");

		return details;
	}

	// Leave Rejection//
	public void sendLeaveRejectionMailNotification(LeaveDebit leaveDebits) {

		String to = (String) propBean.getPropData().get("mail.hrMailId");
		if (leaveDebits.getEmployee().getManager() != null
				&& !leaveDebits.getEmployee().getManager().getEmployeeId()
						.equals(1001L)) {
			to = to + " ," + leaveDebits.getEmployee().getManager().getEmail()+","+
					(leaveDebits.getEmployee().getProjectManager() !=null ?
							leaveDebits.getEmployee().getProjectManager().getEmail():"");

		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[ Leave Application ]]>");
		details.put("toName", leaveDebits.getEmployee().getFullName());
		details.put("fromDate", leaveDebits.getPeriod().getMinimum().toString());
		details.put("toDate", leaveDebits.getPeriod().getMaximum().toString());
		details.put("subject", leaveDebits.getEmployeeComments());
		details.put("cc", leaveDebits.getEmployee().getEmail());
		details.put("to", to);
		details.put("bcc", "");

		String content = mailTemplatesDaoImpl.getMailContent("Leave Rejection");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Leave Rejection' template Type");
		}

	}

	// Leave Approval//
	public void sendLeaveApproveAcknowledgement(LeaveDebit leaveDebits) {

		String to = (String) propBean.getPropData().get("mail.hrMailId");
		if (leaveDebits.getEmployee().getManager() != null
				&& !leaveDebits.getEmployee().getManager().getEmployeeId()
						.equals(1001L)) {
			to = to + " ," + leaveDebits.getEmployee().getManager().getEmail()+","+
					(leaveDebits.getEmployee().getProjectManager() !=null ?
							leaveDebits.getEmployee().getProjectManager().getEmail():"");

		}

		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[ Leave Application ]]>");
		details.put("toName", leaveDebits.getEmployee().getFullName());
		details.put("fromDate", leaveDebits.getPeriod().getMinimum().toString());
		details.put("toDate", leaveDebits.getPeriod().getMaximum().toString());
		details.put("cc", leaveDebits.getEmployee().getEmail());
		details.put("to", to);
		details.put("bcc", "");

		String content = mailTemplatesDaoImpl.getMailContent("Leave Approval");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Leave Approval' template Type");
		}
	}

	// Pending Leaves Approval Alert//
	public void sendLeavePendingApprovalAlert( String cc ,String to ,String toName) {
	
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[Pending Leave Approvals ]]>");
		details.put("toName", toName);
		details.put("cc", cc);
		details.put("to",to);
		details.put("bcc", "");
		
		String content = mailTemplatesDaoImpl
				.getMailContent("Pending leaves approval alert");

		if (content != null) {
			mailContentParser.parseAndSendMail(details, content);
		} else {
			throw new MailCantSendException(
					"Mail Content is not available for 'Pending leaves approval alert' template Type");
		}

	}
	
	
}


