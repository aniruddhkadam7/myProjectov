package com.raybiztech.leavemanagement.exceptions;

public class LeaveCannotProcessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2142835682401741473L;

	public LeaveCannotProcessException() {

	}

	public LeaveCannotProcessException(String msg) {
		super(msg);
	}
}
