/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeInOffice.dto;

/**
 *
 * @author hari
 */
public class SpentHoursDTO {

    private String spentHours;
    private Integer dayOfMonth;
    private String colorForTime;

    public String getColorForTime() {
        return colorForTime;
    }

    public void setColorForTime(String colorForTime) {
        this.colorForTime = colorForTime;
    }

    public String getSpentHours() {
        return spentHours;
    }

    public void setSpentHours(String spentHours) {
        this.spentHours = spentHours;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
