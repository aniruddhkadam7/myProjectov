package com.raybiztech.payslip.exceptions;

public class MonthAndYearAlreadyExistsException extends RuntimeException

{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1764234353698383506L;

	public MonthAndYearAlreadyExistsException() {
	}

	public MonthAndYearAlreadyExistsException(String msg) {
		super(msg);
	}

}
