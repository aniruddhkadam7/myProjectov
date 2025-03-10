/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.alerts.quartzScheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import com.raybiztech.appraisals.alerts.service.AlertServiceI;

/**
 *
 * @author naresh
 */
@Service("hiveQuartzScheduler")
public class HiveQuartzScheduler extends QuartzJobBean {

    @Autowired
    AlertServiceI alertService;

    public void setAlertService(AlertServiceI alertService) {
        this.alertService = alertService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        alertService.employeeHiveReport();
    }
}
