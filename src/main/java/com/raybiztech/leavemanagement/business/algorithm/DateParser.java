package com.raybiztech.leavemanagement.business.algorithm;

import java.text.ParseException;

import com.raybiztech.date.Date;

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
