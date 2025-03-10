package com.raybiztech.appraisals.exceptions;

public class DuplicateActiveCycleException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7900883032331875822L;

	public DuplicateActiveCycleException() {

	}

	public DuplicateActiveCycleException(String msg) {
		super(msg);
	}
}
