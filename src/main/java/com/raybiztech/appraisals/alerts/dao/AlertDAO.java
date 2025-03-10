/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.alerts.dao;

import java.util.Map;

import com.raybiztech.TimeInOffice.business.TimeInOffice;
import com.raybiztech.appraisals.alerts.business.Alert;
import com.raybiztech.date.Date;

/**
 *
 * @author naresh
 */
public interface AlertDAO {

    Boolean isHiveUpdated(String empEmail, Date date);

    Map<String, Object> getAlerts(Long employeeId);

    void getUpdateAllAlerts(Long employeeId);

    Alert updateAlertDetails(Long employeeId, Long alertId);

    Boolean isEmployeeActive(Long employeeId, Date date);

    TimeInOffice isTimeInOfficeUpdated(Long employeeId, Date date);

    Map<String, Object> getAllAlerts(Long employeeId, Integer startIndex, Integer endIndex);

    Boolean isHiveQuartzUpdatedYesterday();

    Boolean isTimeInOfficeQuartzUpdatedYesterday();
}
