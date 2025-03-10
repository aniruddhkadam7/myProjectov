package com.raybiztech.payslip.exceptions;

public class PayslipUnauthorizedException extends RuntimeException {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7747492648714629665L;

	public PayslipUnauthorizedException()
	{
		
	}
	
	public PayslipUnauthorizedException(String msg) {
		
		super(msg);
	}

}
