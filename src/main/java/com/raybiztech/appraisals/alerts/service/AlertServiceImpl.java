/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.alerts.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raybiztech.appraisals.alerts.business.Alert;
import com.raybiztech.appraisals.alerts.dao.AlertDAO;
import com.raybiztech.appraisals.alerts.dto.AlertDTO;
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
@Service("alertService")
public class AlertServiceImpl implements AlertServiceI {

    @Autowired
    AlertDAO alertDAO;
    @Autowired
    DAO dao;
    @Autowired
    BiometricDAO biometricDAO;
    private static final Logger logger = Logger
            .getLogger(AlertServiceImpl.class);

    @Override
    public void employeeHiveReport() {

        Date date = new Date().previous();
        if (date.getDayofWeek() != DayOfWeek.SATURDAY && date.getDayofWeek() != DayOfWeek.SUNDAY) {
            List<Employee> employeeList = biometricDAO.getActiveEmployees();
            Boolean hiveQuarz = alertDAO.isHiveQuartzUpdatedYesterday();
            if (hiveQuarz) {
                for (Employee employee : employeeList) {
                    Long employeeId = employee.getEmployeeId();
                    Boolean attendance = alertDAO.isEmployeeActive(employeeId, date);
                    if (attendance) {
                        Boolean result = alertDAO.isHiveUpdated(employee.getEmail(), date);
                        if (result) {
                            Alert alert = new Alert();
                            alert.setAlertType("Hive");
                            alert.setEmployee(employee);
                            alert.setMsg("Please update your hive timing");
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

    }

    @Override
    public Map<String, Object> getAlerts(Long employeeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> alerts = alertDAO.getAlerts(employeeId);
        Integer size = (Integer) alerts.get("size");
        List<Alert> alertsList = (List<Alert>) alerts.get("alertsList");
        List<AlertDTO> alertDTOList = new ArrayList<AlertDTO>();
        for (Alert alert : alertsList) {
            AlertDTO adto = new AlertDTO();
            adto.setId(alert.getId());
            adto.setMsg(alert.getMsg());
            adto.setMsgDate(alert.getMsgDate().toString("yyyy-MM-dd"));
            adto.setAlertStatus(alert.getAlertStatus());
            adto.setLatestSatatus(alert.getLatestSatatus());
            adto.setAlertType(alert.getAlertType());
            adto.setEmpId(alert.getEmployee().getEmployeeId().toString());
            alertDTOList.add(adto);
        }
        map.put("alertsSize", size);
        map.put("alertsList", alertDTOList);
        return map;
    }

    @Override
    public void getUpdateAllAlerts(Long employeeId) {
        alertDAO.getUpdateAllAlerts(employeeId);
    }

    @Override
    public AlertDTO updateAlertDetails(Long employeeId, Long alertId) {
        Alert alert = alertDAO.updateAlertDetails(employeeId, alertId);
        alert.setAlertStatus(Boolean.TRUE);
        dao.saveOrUpdate(alert);
        Alert alertstatus = alertDAO.updateAlertDetails(employeeId, alertId);
        AlertDTO alertDTO = new AlertDTO();
        alertDTO.setId(alertstatus.getId());
        alertDTO.setMsg(alertstatus.getMsg());
        alertDTO.setMsgDate(alertstatus.getMsgDate().toString());
        alertDTO.setAlertType(alertstatus.getAlertType());
        alertDTO.setAlertStatus(alertstatus.getAlertStatus());
        alertDTO.setLatestSatatus(alertstatus.getLatestSatatus());
        return alertDTO;
    }

    @Override
    public Map<String, Object> getAllAlerts(Long employeeId, Integer startIndex, Integer endIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> allAlerts = alertDAO.getAllAlerts(employeeId, startIndex, endIndex);
        Integer size = (Integer) allAlerts.get("size");
        List<Alert> alertsList = (List<Alert>) allAlerts.get("alertsList");
        List<AlertDTO> alertDTOList = new ArrayList<AlertDTO>();
        for (Alert alert : alertsList) {
            AlertDTO adto = new AlertDTO();
            adto.setId(alert.getId());
            adto.setMsg(alert.getMsg());
            adto.setMsgDate(alert.getMsgDate().toString("yyyy-MM-dd"));
            adto.setAlertStatus(alert.getAlertStatus());
            adto.setLatestSatatus(alert.getLatestSatatus());
            adto.setAlertType(alert.getAlertType());
            adto.setEmpId(alert.getEmployee().getEmployeeId().toString());
            alertDTOList.add(adto);
        }
        map.put("alertsSize", size);
        map.put("alertsList", alertDTOList);
        return map;

    }
}
