package com.raybiztech.payslip.exceptions;

public class PayslipAlreadyGeneratedException extends RuntimeException 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2338249279668783093L;

	public PayslipAlreadyGeneratedException()
	{
		
	}
	
	public PayslipAlreadyGeneratedException(String msg) {
		
		super(msg);
	}

	
}
