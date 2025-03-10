package com.raybiztech.achievementNomination.util;

public class NomineeAlreadyAddedException extends RuntimeException {

	/**
	 * @author shashank
	 */
	private static final long serialVersionUID = -8080419184790729374L;

	public NomineeAlreadyAddedException() {
	}

	public NomineeAlreadyAddedException(String msg) {
		super(msg);
	}

}
