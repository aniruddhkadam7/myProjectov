package com.raybiztech.compliance.quartz;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.compliance.builder.ComplianceBuilder;
import com.raybiztech.compliance.builder.ComplianceMailBuilder;
import com.raybiztech.compliance.business.Compliance;
import com.raybiztech.compliance.business.ComplianceTask;
import com.raybiztech.compliance.business.ComplianceTaskStatus;
import com.raybiztech.compliance.dao.ComplianceDao;
import com.raybiztech.date.Date;
import com.raybiztech.mailtemplates.util.ComplianceMailConfig;


@Component("complianceMail")
public class ComplianceMail {
	@Autowired
	PropBean propBean;

	@Autowired
	ComplianceDao complianceDaoImpl;

	@Autowired
	ComplianceMailConfig complianceMailConfig;
	
	@Autowired
	ComplianceBuilder complianceBuilder; 

	@Autowired
	ComplianceMailBuilder complianceMailBuilder;
	
	Logger logger = Logger.getLogger(ComplianceMail.class);

	//Actual cron method which gets execution 
	public void sendComplianceMailAlert() {
		//Creating ComplianceTasks first
		createComplianceTasks();
		//then Sending Mails of the Appropriate Compliance Tasks
		sendMails();
		
	}
	public void createComplianceTasks() {
		logger.warn("came to create tasks");
		
		//getting the last month , last week and last day created ComplianceTasks
		String previousMonthDate = complianceMailBuilder.lastMonth(new Date()).toString("yyyy-MM-dd");
		
		String previousWeek = complianceMailBuilder.lastWeek(new Date()).toString("yyyy-MM-dd");
		
		String yesterday = new Date().previous().toString("yyyy-MM-dd");
		
		List<ComplianceTask> complianceTaskList = complianceDaoImpl.getLastComplianceTask(previousMonthDate,previousWeek,yesterday);
		logger.warn("No of reocrds"+complianceTaskList.size());
		if(complianceTaskList!=null) {
			for(ComplianceTask complianceTask : complianceTaskList) {
				System.out.println("Comp Name: "+complianceTask.getComplianceName());
				Compliance compliance = complianceTask.getCompliance();
				ComplianceTask newComplianceTask= complianceBuilder.getComplainceTask(compliance);
				//Setting the Actual Compliance Date
				newComplianceTask.setComplianceDate(complianceBuilder.getComplianceDate(compliance));
				Long id = (Long) complianceDaoImpl.save(newComplianceTask);
				logger.warn("created Tasks id"+id);
			}
		}else {
			logger.warn("No Complaince Task Record Found");
		}
	}
	public void sendMails() {
		//Getting the Compliance Tasks for sending Mails
		List<ComplianceTask> mailComplainceTasks = complianceDaoImpl.getMailComplianceTasks();
		
		if(mailComplainceTasks!=null) {
			for(ComplianceTask complianceTask : mailComplainceTasks) {
				if(complianceTask.getComplianceTaskStatus().getComplianceTaskStatus().equals("In Progress")) {
					Compliance compliance = complianceTask.getCompliance();
					String escalationEmail = "";
					if(compliance.getEscalation()!=null) {
						Date today = new Date();
						Date escalationDate = complianceMailBuilder.toBack(complianceTask.getComplianceDate(),compliance.getEscalation());
						//Checking Whether today comes through escalation
						if(today.isAfter(escalationDate) || today.equals(escalationDate)) {
							escalationEmail = escalationEmail + compliance.getEscalationEmail();
						}
						escalationEmail = escalationEmail + compliance.getEscalationEmail();
					}
					
					complianceMailConfig.sendComplainceMail(compliance.getEmailTo(), escalationEmail);
				}
				
				
			}
		}else {
			logger.warn("No Complaince Mails to Send");
		}
	}
	
	
	
}
