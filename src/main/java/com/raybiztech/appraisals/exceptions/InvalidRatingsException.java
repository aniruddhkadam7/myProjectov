/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.exceptions;

/**
 *
 * @author anil
 */
public class InvalidRatingsException extends RuntimeException{

    public InvalidRatingsException() {
    }

    public InvalidRatingsException(String string) {
        super(string);
    }
    
    
    
}
