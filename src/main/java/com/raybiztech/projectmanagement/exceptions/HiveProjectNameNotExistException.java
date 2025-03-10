package com.raybiztech.projectmanagement.exceptions;

public class HiveProjectNameNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HiveProjectNameNotExistException() {

	}

	public HiveProjectNameNotExistException(String msg) {
		super(msg);
	}

}
