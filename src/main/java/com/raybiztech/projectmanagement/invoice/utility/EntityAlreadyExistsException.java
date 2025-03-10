package com.raybiztech.projectmanagement.invoice.utility;

public class EntityAlreadyExistsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1863251889604929188L;

	public EntityAlreadyExistsException() {

	}

	public EntityAlreadyExistsException(String msg) {
		super(msg);
	}

}
