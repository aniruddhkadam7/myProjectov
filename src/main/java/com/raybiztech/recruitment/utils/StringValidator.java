/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.utils;

/**
 * 
 * @author ramesh
 */
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
