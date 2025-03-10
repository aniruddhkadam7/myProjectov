package com.raybiztech.SQAAudit.Exception;

public class SQAAuditAlreadyExistsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3220445272415671499L;
	
	public SQAAuditAlreadyExistsException() {
		
	}
	
	public SQAAuditAlreadyExistsException(String msg) {
		super(msg);
	}

}
