/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeInOffice.builder;

import java.lang.reflect.Constructor;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.stereotype.Component;
import com.raybiztech.TimeInOffice.business.TimeInOffice;
import com.raybiztech.TimeInOffice.dto.TimeInOfficeDTO;

/**
 *
 * @author hari
 */
@Component("timeInOfficeBuilder")
public class TimeInOfficeBuilder {
	public List<TimeInOfficeDTO> convertEntityListToDTOList(
			List<TimeInOffice> timeInOfficeList) {
		List<TimeInOfficeDTO> timeInOfficeDTOList = null;
		if (!timeInOfficeList.isEmpty()) {
			timeInOfficeDTOList = new ArrayList<TimeInOfficeDTO>();

			for (TimeInOffice timeInOffice : timeInOfficeList) {
				TimeInOfficeDTO timeInOfficeDTO = new TimeInOfficeDTO();
				timeInOfficeDTO.setEmpID(timeInOffice.getEmpID());
				timeInOfficeDTO.setEmpName(timeInOffice.getEmpName());
				String tt = String.valueOf(timeInOffice.getSpentHours());
				tt = tt.replaceAll("[h, ,m,r]", "");
				String timeSplit[] = tt.split(":");
				if (!tt.equals("0")) {
					tt = tt.format("%02d:%02d", Integer.valueOf(timeSplit[0]),
							Integer.valueOf(timeSplit[1]));
				} else
					tt = "00:00";
				timeInOfficeDTO.setTotalSpentHours(tt);
				try {
					timeInOfficeDTO.setWeek(com.raybiztech.date.Date
							.parse(timeInOffice.getDt().toString(),
									"dd MMM yyyy").getDayofWeek().toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				timeInOfficeDTO.setDate(timeInOffice.getDt().toString());

				timeInOfficeDTOList.add(timeInOfficeDTO);
			}

		}
		return timeInOfficeDTOList;
	}
}
