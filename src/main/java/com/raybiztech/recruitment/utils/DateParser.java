/*
 * To change this template,  choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.utils;


import java.text.ParseException;

import com.raybiztech.date.Date;

/**
 * 
 * @author ramesh
 */
public class DateParser {

    private DateParser() {

    }

    /**
     * 
     * @param date
     * @return
     */
    public static String toString(Date date) {
        StringValidator validator = new StringValidator();

        return date == null || !validator.validate(date.toString()) ? null
                : date.toString();
    }

    /**
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date toDate(String date) throws ParseException {
        StringValidator validator = new StringValidator();

        return (validator.validate(date)) ? Date.parse(date, "dd/MM/yyyy")
                : null;
    }
       public static Date toDateOtherFormat(String date) throws ParseException {
        StringValidator validator = new StringValidator();

        return (validator.validate(date)) ? Date.parse(date, "dd MMM yyyy")
                : null;
    }
    }
