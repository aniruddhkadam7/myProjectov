package com.raybiztech.payslip.exceptions;

public class PayslipDoesNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8971033447718009128L;

	public PayslipDoesNotExistException() {

	}

	public PayslipDoesNotExistException(String msg) {
		super(msg);
	}

}
