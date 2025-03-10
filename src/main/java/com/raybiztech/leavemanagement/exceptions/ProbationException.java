package com.raybiztech.leavemanagement.exceptions;

public class ProbationException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4184260577448371377L;

	public ProbationException(){
	}
	
	public ProbationException(String msg){
		 super("You are in probation period so you can apply leave after 15 January 2019");
	}

}
