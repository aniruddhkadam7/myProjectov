package com.raybiztech.leavemanagement.exceptions;

public class UnderNoticeException extends RuntimeException{
    
    public static final long serialVersionUID=0111L;

    public UnderNoticeException() {
    }
    
   public UnderNoticeException(String msg){
        super("You are in UnderNotice so you can't apply leave");
    }
    
}
