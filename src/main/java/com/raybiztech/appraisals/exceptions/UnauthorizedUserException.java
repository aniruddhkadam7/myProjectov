package com.raybiztech.appraisals.exceptions;

public class UnauthorizedUserException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1812777702062785585L;
	public UnauthorizedUserException()
	{
		
	}
	public UnauthorizedUserException(String message)
	{
		super(message);
	}

}
