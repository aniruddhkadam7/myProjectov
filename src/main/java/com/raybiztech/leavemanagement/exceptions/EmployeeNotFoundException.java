package com.raybiztech.leavemanagement.exceptions;

public class EmployeeNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8383424238128663666L;

	public EmployeeNotFoundException() {

	}

	public EmployeeNotFoundException(String msg) {
		super(msg);

	}

}
