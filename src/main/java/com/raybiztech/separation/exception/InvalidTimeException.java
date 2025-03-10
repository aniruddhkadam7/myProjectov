package com.raybiztech.separation.exception;

public class InvalidTimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3507105832976471382L;
	
	public InvalidTimeException() {

	}

	public InvalidTimeException(String msg) {
		super(msg);
	}

}
