package com.raybiztech.appraisals.exceptions;

public class CycleAlreadyExistedException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public CycleAlreadyExistedException(){
		
	}
	public CycleAlreadyExistedException(String msg)
	{
		super(msg);
	}

}
