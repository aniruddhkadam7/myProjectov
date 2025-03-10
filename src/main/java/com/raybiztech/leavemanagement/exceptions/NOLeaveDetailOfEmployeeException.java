package com.raybiztech.leavemanagement.exceptions;

public class NOLeaveDetailOfEmployeeException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
 
	
	public NOLeaveDetailOfEmployeeException(){
		
	}
	public NOLeaveDetailOfEmployeeException(String msg){
		super(msg);
	}
}
