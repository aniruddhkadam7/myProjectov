package com.raybiztech.leavemanagement.exceptions;

public class NotWorkingDayException extends RuntimeException {

	private static final long serialVersionUID = 3406613113622946945L;

	/**
	 * 
	 */

	public NotWorkingDayException() {
		super();
	}

	public NotWorkingDayException(String message) {
		super(message);
	}

}
