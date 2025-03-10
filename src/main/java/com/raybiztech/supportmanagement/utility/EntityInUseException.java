package com.raybiztech.supportmanagement.utility;

public class EntityInUseException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3026927151361134844L;
	public EntityInUseException() {

	}

	public EntityInUseException(String msg) {
		super(msg);
	}

}
