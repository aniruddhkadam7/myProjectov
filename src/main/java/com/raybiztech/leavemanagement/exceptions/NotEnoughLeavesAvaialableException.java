package com.raybiztech.leavemanagement.exceptions;

public class NotEnoughLeavesAvaialableException extends  RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotEnoughLeavesAvaialableException() {
		super();
	}

	public NotEnoughLeavesAvaialableException(String message) {
		super(message);
	}

}
