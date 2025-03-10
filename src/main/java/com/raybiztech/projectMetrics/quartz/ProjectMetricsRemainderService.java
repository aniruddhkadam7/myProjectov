package com.raybiztech.projectmetrics.quartz;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;



@Service("ProjectMetricsRemainderService")
public class ProjectMetricsRemainderService extends QuartzJobBean {
	
Logger logger = Logger.getLogger(ProjectMetricsRemainderService.class);	

	ProjectMetricsRemainder sendProjectMetricsRemainder;
	
	
	public ProjectMetricsRemainder getSendProjectMetricsRemainder() {
		return sendProjectMetricsRemainder;
	}

	public void setSendProjectMetricsRemainder(ProjectMetricsRemainder sendProjectMetricsRemainder) {
		this.sendProjectMetricsRemainder = sendProjectMetricsRemainder;
	}




	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//logger.warn("before method calling");
		
		sendProjectMetricsRemainder.sendProjectMetricsAlert();
		
		//logger.warn("after method calling");
		
	}

	
}
