package com.raybiztech.appraisals.observation.exceptions;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

public class DuplicateObservationException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7356909274609779424L;
	public DuplicateObservationException()
	{
		
	}
	public DuplicateObservationException(String message)
	{
		super(message);
	}
	
	
	

}

