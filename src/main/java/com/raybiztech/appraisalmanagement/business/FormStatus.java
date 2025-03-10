package com.raybiztech.appraisalmanagement.business;

public enum FormStatus {

	SAVE(0),SUBMIT(1),PENDING(2),OPENFORDISCUSSION(3),PENDINGAGREEMENT(4),COMPLETED(5),CLOSED(6);
	private final int  value;
	private FormStatus(int value){
		this.value=value;
	}
	public int getValue(){
		return value;
	}
}
