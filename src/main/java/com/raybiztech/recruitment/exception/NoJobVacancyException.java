package com.raybiztech.recruitment.exception;

public class NoJobVacancyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -810011077458959069L;

	public NoJobVacancyException() {
		super();
	}

	public NoJobVacancyException(String message) {
		super(message);
	}

}
