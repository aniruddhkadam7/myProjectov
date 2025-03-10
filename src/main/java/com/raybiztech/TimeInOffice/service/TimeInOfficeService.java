/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeInOffice.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.raybiztech.TimeInOffice.dto.TimeInOfficeDTO;

/**
 *
 * @author hari
 */
public interface TimeInOfficeService {

    TimeInOfficeDTO getTimeInOfficeEmployeeReport(String loggedInEmployeeId,
            String date);

    Map<String, Object> searchTimeInOffice(String loggedInEmployeeId,
            String date, Integer startIndex, Integer endIndex, String search);

    Map<String, Object> getTimeInOfficeManagerReport(String loggedInEmployeeId,
            String date, Integer startIndex, Integer endindex);

    List<TimeInOfficeDTO> getweeklyTimeInOfficeReport(String loggedInEmployeeId);
    
    ByteArrayOutputStream exportAttendanceData(String hiveDate,String search)
			throws IOException;
    
}
