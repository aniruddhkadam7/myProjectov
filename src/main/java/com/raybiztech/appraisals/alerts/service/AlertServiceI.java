/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.alerts.service;

import java.util.Map;

import com.raybiztech.appraisals.alerts.dto.AlertDTO;

/**
 *
 * @author naresh
 */
public interface AlertServiceI {

    void employeeHiveReport();

    Map<String, Object> getAlerts(Long employeeId);

    void getUpdateAllAlerts(Long employeeId);

    AlertDTO updateAlertDetails(Long employeeId, Long alertId);


     Map<String, Object> getAllAlerts(Long employeeId, Integer startIndex, Integer endIndex);
}
