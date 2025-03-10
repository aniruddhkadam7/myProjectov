package com.raybiztech.ticketmanagement.exceptions;

public class RequestNotFoundException extends RuntimeException{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9190848234262486187L;

	public RequestNotFoundException() {

	}

	public RequestNotFoundException(String msg) {
		super(msg);
	}
}
