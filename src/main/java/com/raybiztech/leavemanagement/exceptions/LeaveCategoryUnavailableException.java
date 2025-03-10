package com.raybiztech.leavemanagement.exceptions;

public class LeaveCategoryUnavailableException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1241565469755746880L;
	
	public LeaveCategoryUnavailableException(){
		
	}

	public LeaveCategoryUnavailableException(String msg){
		super(msg);
	}
}
