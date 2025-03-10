package com.raybiztech.separation.exception;

public class CannotSubmitResignationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -207405812075663199L;
	
	public CannotSubmitResignationException() {
		
	}
	
	public CannotSubmitResignationException(String msg) {
		super(msg);
	}
	
}
