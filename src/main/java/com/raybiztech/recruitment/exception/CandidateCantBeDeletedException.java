/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.recruitment.exception;

/**
 *
 * @author hari
 */
public class CandidateCantBeDeletedException extends RuntimeException{
    
    public  CandidateCantBeDeletedException()
    {
        super();
    }
    public  CandidateCantBeDeletedException(String msg)
    {
        super(msg);
    }
   
    
}
