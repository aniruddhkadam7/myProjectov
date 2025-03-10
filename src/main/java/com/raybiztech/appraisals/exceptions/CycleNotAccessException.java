/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.exceptions;

/**
 *
 * @author hari
 */
public class CycleNotAccessException extends RuntimeException{

    public CycleNotAccessException(String string) {
        super(string);
    }
    
    public CycleNotAccessException()
    {
        super();
    }
   
    
}
