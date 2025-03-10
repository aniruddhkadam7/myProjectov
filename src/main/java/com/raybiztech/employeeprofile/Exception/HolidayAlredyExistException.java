package com.raybiztech.employeeprofile.Exception;

public class HolidayAlredyExistException  extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 188898240331574102L;
	
	public HolidayAlredyExistException()
	{
		
	}
	public HolidayAlredyExistException(String msg)
	{
		super(msg);
	}

}
