package com.raybiztech.leavemanagement.exceptions;

public class MailCantSendException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9193141881514393510L;

	public MailCantSendException() {

	}

	public MailCantSendException(String msg) {
		super(msg);
	}

}
