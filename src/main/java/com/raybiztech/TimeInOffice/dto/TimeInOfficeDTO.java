/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeInOffice.dto;

import java.util.List;

/**
 *
 * @author hari
 */
public class TimeInOfficeDTO {

    private String empID;
    private String empName;
    private String totalSpentHours;
    private String totalTimeInOfficeHours;
    private String date;
    private String month;
    private String week;
    private List dayList;
    
    private List<SpentHoursDTO> inOfficeDTOs;

   

    public String getTotalTimeInOfficeHours() {
        return totalTimeInOfficeHours;
    }

    public void setTotalTimeInOfficeHours(String totalTimeInOfficeHours) {
        this.totalTimeInOfficeHours = totalTimeInOfficeHours;
    }

    public String getEmpID() {
        return empID;
    }

    public String getTotalSpentHours() {
        return totalSpentHours;
    }

    public void setTotalSpentHours(String totalSpentHours) {
        this.totalSpentHours = totalSpentHours;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public List<SpentHoursDTO> getInOfficeDTOs() {
        return inOfficeDTOs;
    }

    public void setInOfficeDTOs(List<SpentHoursDTO> inOfficeDTOs) {
        this.inOfficeDTOs = inOfficeDTOs;
    }

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public List getDayList() {
		return dayList;
	}

	public void setDayList(List dayList) {
		this.dayList = dayList;
	}

}
