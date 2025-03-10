/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.alerts.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raybiztech.TimeInOffice.business.TimeInOffice;
import com.raybiztech.appraisals.alerts.business.Alert;
import com.raybiztech.appraisals.alerts.dao.AlertDAO;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.biometric.dao.BiometricDAO;
import com.raybiztech.date.Date;
import com.raybiztech.date.DayOfWeek;
import com.raybiztech.date.HourOfDay;
import com.raybiztech.date.MinuteOfHour;
import com.raybiztech.date.Second;
import com.raybiztech.date.SecondOfMinute;

/**
 *
 * @author naresh
 */
@Service("timeInofficeAlertService")
public class TimeInofficeAlertService {

    @Autowired
    AlertDAO alertDAO;
    @Autowired
    DAO dao;
    @Autowired
    BiometricDAO biometricDAO;
    private static final Logger logger = Logger
            .getLogger(AlertServiceImpl.class);

    public void employeeTimeInOfficeReport() {
        Date date = new Date().previous();
        logger.info("TimeInOfficeReportSTART");
        if (date.getDayofWeek() != DayOfWeek.SATURDAY && date.getDayofWeek() != DayOfWeek.SUNDAY) {
            List<Employee> employeeList = biometricDAO.getActiveEmployees();
            Boolean timeInOfficeQuartz = alertDAO.isTimeInOfficeQuartzUpdatedYesterday();
            if (timeInOfficeQuartz) {
                for (Employee employee : employeeList) {
                    Boolean attendance = alertDAO.isEmployeeActive(employee.getEmployeeId(), date);
                    if (attendance) {
                        TimeInOffice result = alertDAO.isTimeInOfficeUpdated(employee.getEmployeeId(), date);
                        String spentTime = result.getSpentHours();
                        String time = spentTime;
                        Integer hours = 0;
                        if (time.contains(":")) {
                            String[] value = time.split(":");
                            String hour = value[0];
                            hours = Integer.parseInt(hour.replace(" h", ""));
                        } else {
                            String hour = time;
                            hours = Integer.parseInt(hour.replace(" hr", ""));
                        }
                        if (hours < 8) {
                            Alert alert = new Alert();
                            alert.setAlertType("TimeInOffice");
                            alert.setEmployee(employee);
                            alert.setMsg("Your spent time is low");
                            alert.setMsgDate(new Second(new Date().previous().getYearOfEra(), new Date().previous().getMonthOfYear(), new Date().previous().getDayOfMonth(), HourOfDay.valueOf(0), MinuteOfHour.valueOf(0), SecondOfMinute.valueOf(0)));
                            alert.setAlertStatus(Boolean.FALSE);
                            alert.setLatestSatatus(Boolean.FALSE);
                            alert.setInsertOn(new Second());
                            dao.save(alert);
                        }
                    }
                }

            }
        }
        logger.info("TimeInOfficeReportEND");

    }
}
