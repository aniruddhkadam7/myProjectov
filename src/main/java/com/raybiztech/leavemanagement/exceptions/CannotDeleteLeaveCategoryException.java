package com.raybiztech.leavemanagement.exceptions;

public class CannotDeleteLeaveCategoryException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8699789804461759750L;

	public CannotDeleteLeaveCategoryException() {

	}

	public CannotDeleteLeaveCategoryException(String message) {
		super("Leave Catrgory already been using ");
	}

}
