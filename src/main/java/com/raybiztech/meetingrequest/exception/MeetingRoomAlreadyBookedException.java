/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.meetingrequest.exception;

/**
 *
 * @author sravani
 */
public class MeetingRoomAlreadyBookedException extends RuntimeException{
    
    private static final long serialVersionUID = 1764235894698383506L;
    
    public MeetingRoomAlreadyBookedException()
    {
    
    }
    public  MeetingRoomAlreadyBookedException(String msg){
        super(msg);
    
    }
    
}
