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

import com.raybiztech.appraisals.alerts.service.TimeInofficeAlertService;

/**
 *
 * @author naresh
 */
@Service("timeQuartzScheduler")
public class TimeQuartzScheduler extends QuartzJobBean {

    @Autowired
    TimeInofficeAlertService timeInofficeAlertService;

    public void setTimeInofficeAlertService(TimeInofficeAlertService timeInofficeAlertService) {
        this.timeInofficeAlertService = timeInofficeAlertService;
    }

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        timeInofficeAlertService.employeeTimeInOfficeReport();
    }
}
