package com.raybiztech.meetingrequest.exception;

public class CrossingTimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 483158416964661798L;
	public CrossingTimeException()
    {
    
    }
    public  CrossingTimeException(String msg){
        super(msg);
    
    }
}
