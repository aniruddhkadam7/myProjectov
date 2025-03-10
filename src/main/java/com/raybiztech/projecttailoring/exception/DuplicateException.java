package com.raybiztech.projecttailoring.exception;

public class DuplicateException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3340196491062173134L;

	public DuplicateException() {
		super();
	}

	public DuplicateException(String message) {
		super(message);
	}
}
