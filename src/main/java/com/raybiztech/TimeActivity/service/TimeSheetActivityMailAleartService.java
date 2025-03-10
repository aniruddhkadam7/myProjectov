package com.raybiztech.TimeActivity.service;

import java.text.ParseException;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.SQAAudit.builder.SQAAuditBuilder;
import com.raybiztech.SQAAudit.dao.SQAAuditDAO;
import com.raybiztech.TimeActivity.quartz.TimeSheetAleart;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.mailtemplates.dao.MailTemplatesDao;
import com.raybiztech.mailtemplates.util.MailContentParser;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;


@Service("timeSheetActivityMailAleartService")
public class TimeSheetActivityMailAleartService extends QuartzJobBean {
	Logger logger = Logger.getLogger(TimeSheetActivityMailAleartService.class);
	
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
	
	private TimeSheetAleart timeSheetAleart;
	

	public TimeSheetAleart getTimeSheetAleart() {
		return timeSheetAleart;
	}


	public void setTimeSheetAleart(TimeSheetAleart timeSheetAleart) {
		this.timeSheetAleart = timeSheetAleart;
	}


	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			TenantTypes[] list = TenantTypes.class.getEnumConstants();
			for(TenantTypes type : list) {
				TenantContextHolder.setTenantType(type);
			logger.warn("In Service method");
			timeSheetAleart.sendTimeSheetAleart();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
