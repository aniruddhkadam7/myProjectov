/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeInOffice.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.TimeInOffice.business.TimeInOffice;
import com.raybiztech.TimeInOffice.dto.TimeInOfficeDTO;
import com.raybiztech.appraisals.business.Employee;

/**
 *
 * @author hari
 */
public interface TimeInOfficeDAO {


    Map<String, Object> getTimeInOfficeManagerReport(Employee employee,
            String date, Integer startIndex, Integer endIndex);

    TimeInOfficeDTO getTimeInOfficeEmployeeReport(String loggedInEmployeeId,
            String date);

    Map<String, Object> searchTimeInOffice(Employee employee, String date,
            Integer startIndex, Integer endIndex, String search);

    List<TimeInOffice> getweeklyTimeInOfficeList(String loggedInEmployeeId);

}
