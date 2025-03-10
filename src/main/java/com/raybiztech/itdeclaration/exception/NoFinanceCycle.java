package com.raybiztech.itdeclaration.exception;

public class NoFinanceCycle extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public NoFinanceCycle() {
		
	}
	public NoFinanceCycle(String msg) {
		super(msg);
	}

}
