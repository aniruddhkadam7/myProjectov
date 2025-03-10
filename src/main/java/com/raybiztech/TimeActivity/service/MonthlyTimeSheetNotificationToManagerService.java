package com.raybiztech.TimeActivity.service;

import java.text.ParseException;

import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.SQAAudit.builder.SQAAuditBuilder;
import com.raybiztech.SQAAudit.dao.SQAAuditDAO;
import com.raybiztech.TimeActivity.quartz.MonthlyTimeSheetNotificationToManager;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.mailtemplates.util.MailContentParser;


@Service("monthlyTimeSheetActivityMailAleartService")
public class MonthlyTimeSheetNotificationToManagerService extends QuartzJobBean {
	
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
	
	private MonthlyTimeSheetNotificationToManager monthlyTimeSheetNotificationToManager;
	
	

	public MonthlyTimeSheetNotificationToManager getMonthlyTimeSheetNotificationToManager() {
		return monthlyTimeSheetNotificationToManager;
	}



	public void setMonthlyTimeSheetNotificationToManager(
			MonthlyTimeSheetNotificationToManager monthlyTimeSheetNotificationToManager) {
		this.monthlyTimeSheetNotificationToManager = monthlyTimeSheetNotificationToManager;
	}



	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			monthlyTimeSheetNotificationToManager.sendTimeSheetAleart();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
