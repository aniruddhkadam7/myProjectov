package com.raybiztech.leavemanagement.business.algorithm;

public class StringValidator {

	 /**
     * 
     * @param s
     * @return
     */
    public boolean validate(String s) {
        return s != null && s.trim().length() != 0;
    }
}
