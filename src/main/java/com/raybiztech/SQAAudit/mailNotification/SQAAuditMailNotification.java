package com.raybiztech.SQAAudit.mailNotification;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.SQAAudit.builder.SQAAuditBuilder;
import com.raybiztech.SQAAudit.business.SQAAuditForm;
import com.raybiztech.SQAAudit.business.SQAAuditees;
import com.raybiztech.SQAAudit.business.SQAAuditors;
import com.raybiztech.SQAAudit.dao.SQAAuditDAO;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.leavemanagement.exceptions.MailCantSendException;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.mailtemplates.util.MailContentParser;
import com.raybiztech.projectmanagement.business.Project;

@Component("sqaAuditMailNotification")
public class SQAAuditMailNotification {
	
	
	@Autowired
	SQAAuditDAO sqaDaoImpl;
	
	@Autowired
	PropBean propBean;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	MailTemplatesDao mailTemplatesDaoImpl;

	@Autowired
	MailContentParser mailContentParser;
	
	@Autowired
	SecurityUtils securityUtils;
	
	@Autowired
	SQAAuditBuilder sqaAuditbuilder;

	public void sendSQAAuditScheduleOrRescheduleMail(Long auditId) {
		SQAAuditForm audit = sqaDaoImpl.findBy(SQAAuditForm.class, auditId);
		Employee pm = audit.getProjectManager();
		String to = pm.getEmail();
		String content = null;
		Boolean flag = true;
		for(SQAAuditors auditor : audit.getAuditors()) {
			if(flag) {
				to = to + " ,"+auditor.getAuditor().getEmail();
				//System.out.println("to:"+to);
			flag = false;
			}
			else {
				to = to + " ," + auditor.getAuditor().getEmail();
				//System.out.println("to:"+to);
			}
		}
		
		for(SQAAuditees auditee : audit.getAuditees()) {
			to = to + " ," + auditee.getAuditee().getEmail();
			//System.out.println("to:"+to);
		}
		
		String cc = audit.getCreatedBy().getManager().getEmail();
		//System.out.println("cc:"+cc);
		
		String projectName = audit.getProject() != null ? audit.getProject().getProjectName() : audit.getProjectName();
		
		String regarding = null;
		
		String auditDate = audit.getAuditDate().toString("dd/MM/yyyy");
		String startTime = sqaAuditbuilder.twelveHoursFormate(audit.getStartTime().getHourOfDay().getValue()
					+ ":"
					+ audit.getStartTime().getMinuteOfHour().getValue());
		String endTime = sqaAuditbuilder.twelveHoursFormate(audit.getEndTime().getHourOfDay().getValue()
					+ ":"
					+ audit.getEndTime().getMinuteOfHour().getValue());
		String date  = auditDate + " " + startTime + "-" + endTime;
		
		if(!audit.getAuditRescheduleStatus()) {
			content = mailTemplatesDaoImpl.getMailContent("SQA Audit Schedule");
			 regarding = projectName + "_" +audit.getAuditType() + "Audit Schedule" ;
			}
			else {
				content = mailTemplatesDaoImpl.getMailContent("SQA Audit Reschedule");
				 regarding = projectName + "_" +audit.getAuditType() + "Audit Reschedule" ;
			}
		
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[" + regarding + "]]>");
		details.put("to", to);
		details.put("cc", cc);
		details.put("bcc", "");
		details.put("type", audit.getAuditType());
		details.put("projectName",projectName);
		details.put("date", date);
		
		
		if(content != null) {
			mailContentParser.parseAndSendMail(details, content);
		}
		else {
			throw new MailCantSendException("Mail Content of 'SQA Audit Schedule' type is not available");
		}
		
	}

	public void sendAuditReportToManager(Long auditId) {
		SQAAuditForm audit = sqaDaoImpl.findBy(SQAAuditForm.class, auditId);
		Employee emp = audit.getProjectManager();
		String to = emp.getEmail();
		Boolean flag = true;
		for(SQAAuditees auditee : audit.getAuditees()) {
			if(flag) {
				to = to+","+ auditee.getAuditee().getEmail();
				//System.out.println("to:"+to);
			flag = false;
			}
			else {
				to = to + " ," + auditee.getAuditee().getEmail();
				//System.out.println("to:"+to);
			}
		}
		String cc = audit.getCreatedBy().getManager().getEmail();
		//System.out.println("cc:"+cc);
		
		for(SQAAuditors auditor : audit.getAuditors()) {
			cc = cc + " ," + auditor.getAuditor().getEmail();
			//System.out.println("cc:"+cc);
		}
		
		String projectName = audit.getProject() != null ? audit.getProject().getProjectName() : audit.getProjectName();
		
		String regarding = projectName + "_" +audit.getAuditType() + "Audit Report";
		
		
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[" + regarding + "]]>");
		details.put("to", to);
		details.put("cc", cc);
		details.put("bcc", "");
		details.put("projectName",projectName);
		if(audit.getSqaFilesPath() != null && audit.getContainsFile()) {
		File file = new File(audit.getSqaFilesPath());
		details.put("path", file.toString());
		}
		if(audit.getFollowUpDate() != null){
		details.put("date", audit.getFollowUpDate().toString("dd/MM/yyyy"));
		}
		details.put("projectCode", audit.getPci());
		if(audit.getSqaComments()!=null){
			details.put("Comment", audit.getSqaComments());
		}
		else{
			details.put("Comment","");
		}
		
		String content ="";
		
		if(audit.getFollowUpDate() != null){
		 content = mailTemplatesDaoImpl.getMailContent("SQA Audit Report");
		}else{
			content =mailTemplatesDaoImpl.getMailContent("SQA Audit Report With Out Followup Audit Date");
		}
		
		if(content != null) {
			mailContentParser.parseAndSendMail(details, content);
		}
		else {
			throw new MailCantSendException("Mail Content of 'SQA Audit Report' type is not available");
		}
	}

	public void sendFollowupAuditReportToSQATeam(Long auditId) {
		SQAAuditForm audit = sqaDaoImpl.findBy(SQAAuditForm.class, auditId);
		//String to = (String) propBean.getPropData().get("mail.sqaMailId");
		String to = null;
		Boolean flag = true;
		for(SQAAuditors auditor : audit.getAuditors()) {
			
			if(flag) {
				to = auditor.getAuditor().getEmail();
				//System.out.println("to:"+to);
			flag = false;
			}
			else {
				to = to + " ," + auditor.getAuditor().getEmail();
				//System.out.println("to:"+to);
			}
		}
		// SQAManagerEmail
		String cc = audit.getCreatedBy().getManager().getEmail();
		
		for(SQAAuditees auditee : audit.getAuditees()) {
			cc = cc + " ," + auditee.getAuditee().getEmail();
			//System.out.println("cc:"+cc);
		}
		String projectName = audit.getProject() != null ? audit.getProject().getProjectName() : audit.getProjectName();
		String regarding = projectName + "_" +audit.getAuditType() + " " + "Findings Closure Report";
		
		
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[" + regarding + "]]>");
		details.put("to", to);
		details.put("cc", cc);
		details.put("bcc", "");
		details.put("projectName",projectName);
		if(audit.getPmFilesPath() != null && audit.getContainsFile()) {
		File file = new File(audit.getPmFilesPath());
		details.put("path", file.toString());
		}
		if(audit.getPmComments()!=null){
			details.put("Comment", audit.getPmComments());
		}
		else{
			details.put("Comment","");
		}
		details.put("name", audit.getProjectManager().getFullName());
		
		String content = mailTemplatesDaoImpl.getMailContent("SQA Followup Audit Report");
		
		if(content != null) {
			mailContentParser.parseAndSendMail(details, content);
		}
		else {
			throw new MailCantSendException("Mail Content of 'SQA Followup Audit Report' type is not available");
		}
	}
	
	public void sendAuditClosureMail(Long auditId) {
		SQAAuditForm audit = sqaDaoImpl.findBy(SQAAuditForm.class, auditId);
		String to = null;
		Boolean flag = true;
		for(SQAAuditees auditee : audit.getAuditees()) {
			
			if(flag) {
				to = auditee.getAuditee().getEmail();
				//System.out.println("to:"+to);
			flag = false;
			}
			else {
				to = to + " ," + auditee.getAuditee().getEmail();
				//System.out.println("to:"+to);
			}
		}
		String cc = audit.getCreatedBy().getManager().getEmail();
		//System.out.println(cc);
		for(SQAAuditors auditor : audit.getAuditors()) {
			cc = cc + " ," + auditor.getAuditor().getEmail();
			//System.out.println("cc:"+cc);
		}
		
		String projectName = audit.getProject() != null ? audit.getProject().getProjectName() : audit.getProjectName();
		String regarding = projectName + "_" +audit.getAuditType() + "Audit Closure Report";
		
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[" + regarding + "]]>");
		details.put("to", to);
		details.put("cc", cc);
		details.put("bcc", "");
		details.put("projectName",projectName);
		details.put("type", audit.getAuditType());
		
		String content = mailTemplatesDaoImpl.getMailContent("SQA Audit Closure");
		
		if(content != null) {
			mailContentParser.parseAndSendMail(details, content);
		}
		else {
			throw new MailCantSendException("Mail Content of 'SQA Audit Closure' type is not available");
		}
	}
	
	// Follow-up End date Alert
	 public void sendFollowUpEndDateAlert(SQAAuditForm form){
		 SQAAuditForm audit = sqaDaoImpl.findBy(SQAAuditForm.class, form.getId());
			String to = null;
			String cc = (String) propBean.getPropData().get("mail.sqaMailId");
			Boolean flag = true;
			System.out.println("Manager Mail : "+audit.getProjectManager().getEmail());
			 to = audit.getProjectManager().getEmail();
			//System.out.println(cc);
			 boolean value = false;
			for(SQAAuditors auditor : audit.getAuditors()) {
				if(value==false){
					if((auditor.getAuditor().getRole()).contains("SQA")){
						cc = cc +","+ auditor.getAuditor().getManager().getEmail();
						System.out.print(cc);
						value=true;
					}
				}
				//System.out.println("cc:"+cc);
			}
			
			String projectName = audit.getProject() != null ? audit.getProject().getProjectName() : audit.getProjectName();
			String regarding = projectName + "_" +audit.getAuditType() + " followup audit for findings closure";
			String name =audit.getProjectManager().getFullName();
			
			Map<String, String> details = new HashMap<String, String>();
			details.put("regarding", "<![CDATA[" + regarding + "]]>");
			details.put("to", to);
			details.put("cc", cc);
			details.put("bcc", "");
			details.put("projectName",projectName);
			details.put("type", audit.getAuditType());
			details.put("name", name);
			
			String content = mailTemplatesDaoImpl.getMailContent("Follow-Up Date Aleart");
			
			if(content != null) {
				mailContentParser.parseAndSendMail(details, content);
				System.out.println("Mail Send");
			}
			else {
				throw new MailCantSendException("Mail Content of 'SQA Audit Closure' type is not available");
			}

	 }

	public void sendFollowUpAuditStatusReportToManager(Long id) {
		
		SQAAuditForm form = sqaDaoImpl.findBy(SQAAuditForm.class, id);
		String to = null;
		String cc = (String) propBean.getPropData().get("mail.sqaMailId");
		Boolean flag = true;
		System.out.println("Manager Mail : "+form.getProjectManager().getEmail());
		 to = form.getProjectManager().getEmail();
		//System.out.println(cc);
		 boolean value = false;
		for(SQAAuditors auditor : form.getAuditors()) {
			if(value==false){
				if((auditor.getAuditor().getRole()).contains("SQA")){
					cc = cc +","+ auditor.getAuditor().getManager().getEmail();
					System.out.print(cc);
					value=true;
				}
			}
			//System.out.println("cc:"+cc);
		}
		
		String projectName = form.getProject() != null ? form.getProject().getProjectName() : form.getProjectName();
		String regarding = projectName + "_" +form.getAuditType() + " Followup Audit Status Report";
		String name =form.getProjectManager().getFullName();
		
		Map<String, String> details = new HashMap<String, String>();
		details.put("regarding", "<![CDATA[" + regarding + "]]>");
		details.put("to", to);
		details.put("cc", cc);
		details.put("bcc", "");
		details.put("name", name);
		if(form.getSqaComments() != null){
			details.put("Comment", form.getSqaComments());
		}
		else{
			details.put("Comment", "");
		}
		
		if(form.getSqaFilesPath() != null && form.getContainsFile()) {
			File file = new File(form.getSqaFilesPath());
			details.put("path", file.toString());
			}
		
		String content = mailTemplatesDaoImpl.getMailContent("Followup audit status report");
		
		if(content != null) {
			mailContentParser.parseAndSendMail(details, content);
			System.out.println("Mail Send");
		}
		else {
			throw new MailCantSendException("Mail Content of 'Followup audit status report' type is not available");
		}

		
		
	}
	 
	 
	 
}
