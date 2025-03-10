package com.raybiztech.projectmanagement.exception;

public class ProjectRequestIdAlreadyExistsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7902318488979290191L;
	public ProjectRequestIdAlreadyExistsException(){
		
	}
	public ProjectRequestIdAlreadyExistsException(String msg){
		super(msg);
	}

}
