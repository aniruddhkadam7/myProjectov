package com.raybiztech.recruitment.exception;

public class DuplicateScheduleException extends RuntimeException{
	public DuplicateScheduleException(){
		super();
	}
	public DuplicateScheduleException(String msg){
		super(msg);
	}

}
