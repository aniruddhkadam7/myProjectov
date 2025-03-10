package com.raybiztech.leavemanagement.exceptions;

public class LeaveNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7261245632369046628L;

	/**
	 * 
	 */
	public LeaveNotFoundException() {

	}

	public LeaveNotFoundException(String msg) {
		super(msg);
	}

}