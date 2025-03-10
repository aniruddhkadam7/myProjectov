package com.raybiztech.leavemanagement.service;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.leavemanagement.quartz.LeavesCarryForwardJob;

@Service("leavesCarryForwardService")
public class LeavesCarryForwardService {

	@Autowired
	LeavesCarryForwardJob leavesCarryForwardJob;
	@Autowired
	LeaveDAO leaveDAO;

	private Scheduler scheduler;

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public LeavesCarryForwardJob getLeavesCarryForwardJob() {
		return leavesCarryForwardJob;
	}

	public void setLeavesCarryForwardJob(
			LeavesCarryForwardJob leavesCarryForwardJob) {
		this.leavesCarryForwardJob = leavesCarryForwardJob;
	}

	public static Logger logger = Logger
			.getLogger(LeavesCarryForwardService.class);

	@Transactional
	public void carryForward() {

		logger.info("carryForward method called .........");

		try {
			// create JOB

			logger.info("LeaveCarryForward Scheduler is " + scheduler);
			Integer yearValue = leaveDAO.getLeaveSettings()
					.getLeaveCycleMonth().getMonthCode();

			logger.info("financialYear  value  is " + yearValue);
			
			Integer	financialYear=yearValue+1;

			MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
			jobDetail.setTargetObject(leavesCarryForwardJob);
			jobDetail.setTargetMethod("carryForwardLeaves");
			jobDetail.setName("LeaveCarryForward Job");
			jobDetail.setConcurrent(false);
			jobDetail.afterPropertiesSet();

			// create CRON Trigger
			CronTriggerBean cronTrigger = new CronTriggerBean();
			cronTrigger.setBeanName("LeaveCarryForward Trigger");

		 	
			// Execute at every financial year begining
			//String expression = "0 0 0 1 " + financialYear+ " ? *";
			String expression = "0 0 0 1 " + financialYear+ " ? *";
			
			//for testing purpose
			
			//String expression = "0 06 0 * * ? ";
			
			//String expression = "0 1 0 1 4 ? *";
			logger.info("cron expression for LeaveCarryForward Job is :"+expression);
			cronTrigger.setCronExpression(expression);
			cronTrigger.afterPropertiesSet();

			scheduler.scheduleJob((JobDetail) jobDetail.getObject(),
					cronTrigger);

			// Start Scheduler
			scheduler.start();

		} catch (Exception e) {
			logger.error(" Leaves Cannot be Carry forward ");
		}

	}
}
