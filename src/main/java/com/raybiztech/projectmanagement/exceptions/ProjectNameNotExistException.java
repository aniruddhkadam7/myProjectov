package com.raybiztech.projectmanagement.exceptions;

public class ProjectNameNotExistException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProjectNameNotExistException() {

	}

	public ProjectNameNotExistException(String msg) {
		super(msg);
	}

}
