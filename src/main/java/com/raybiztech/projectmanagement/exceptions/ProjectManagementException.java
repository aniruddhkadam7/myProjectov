package com.raybiztech.projectmanagement.exceptions;

public abstract class ProjectManagementException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6565887203541379047L;

	public ProjectManagementException() {

		super();
	}

	public ProjectManagementException(String message) {
		super(message);

	}

}
