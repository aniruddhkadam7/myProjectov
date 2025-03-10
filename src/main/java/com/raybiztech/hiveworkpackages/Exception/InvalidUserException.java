package com.raybiztech.hiveworkpackages.Exception;

public class InvalidUserException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5217317996485695932L;
	
	public InvalidUserException() {
		
	}
	public InvalidUserException(String msg) {
		super(msg);
	}
	

}
