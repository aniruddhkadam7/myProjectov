/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.utils;

import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.HourOfDay;
import com.raybiztech.date.MinuteOfHour;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.Second;
import com.raybiztech.date.SecondOfMinute;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.recruitment.utils.StringValidator;

/**
 *
 * @author user
 */
public class SecondParser {

     public static String toString(Second date) {
        StringValidator validator = new StringValidator();

        return date == null || !validator.validate(date.toString()) ? null
                : date.toString("dd/MM/yyyy HH:mm:ss");
    }

    /**
     * 
     * @param date
     * @return
     */
    public static Second toSecond(String date) {
        StringValidator validator = new StringValidator();
        if(date==null || "null".equalsIgnoreCase(date)){
          return null;
        }
        String[] dates = date.split("/");
        int seconds=0;
        if(dates.length>5){
            seconds=Integer.parseInt(dates[IntegerConstants.CONSTANT5.getConstantValue()]);
        }
        return validator.validate(date) ? new Second(YearOfEra.valueOf(Integer
                .parseInt(dates[2])), MonthOfYear.valueOf(Integer
                .parseInt(dates[1]) - 1), DayOfMonth.valueOf(Integer
                .parseInt(dates[0])),
                HourOfDay.valueOf(Integer
                        .parseInt(dates[IntegerConstants.CONSTANT3
                                .getConstantValue()])),
                MinuteOfHour.valueOf(Integer
                        .parseInt(dates[IntegerConstants.CONSTANT4
                                .getConstantValue()])),
                SecondOfMinute.valueOf(seconds)) : null;
    }
    
    
    
}
