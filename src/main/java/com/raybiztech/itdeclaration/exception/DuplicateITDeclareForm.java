package com.raybiztech.itdeclaration.exception;

public class DuplicateITDeclareForm extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DuplicateITDeclareForm(){
		
	}
	 public DuplicateITDeclareForm(String msg){
		super(msg);
	}
}
