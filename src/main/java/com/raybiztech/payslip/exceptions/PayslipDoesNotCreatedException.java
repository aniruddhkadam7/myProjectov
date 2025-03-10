package com.raybiztech.payslip.exceptions;

public class PayslipDoesNotCreatedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4547168673868110581L;

	public PayslipDoesNotCreatedException() {

	}

	public PayslipDoesNotCreatedException(String msg)

	{
		super(msg);
	}
}
