package com.raybiztech.separation.exception;

public class SeparationAlreadyExistException extends  RuntimeException{
	
	private static final long serialVersionUID = 1700285821549052995L;
	public SeparationAlreadyExistException(){
		
	}
public SeparationAlreadyExistException(String msg){
	super(msg);
}
}
