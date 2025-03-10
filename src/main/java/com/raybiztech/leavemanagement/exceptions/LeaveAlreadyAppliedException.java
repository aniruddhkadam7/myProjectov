package com.raybiztech.leavemanagement.exceptions;

public class LeaveAlreadyAppliedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LeaveAlreadyAppliedException(){
		
	}
	public LeaveAlreadyAppliedException(String msg){
		super("leave already applied");
	}

}
