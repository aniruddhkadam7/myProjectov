package com.raybiztech.leavemanagement.exceptions;

public class LeaveCategoryAlreadyCreatedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8084243961370260085L;

	public LeaveCategoryAlreadyCreatedException() {

	}

	public LeaveCategoryAlreadyCreatedException(String message) {
		super(message);
	}
}
