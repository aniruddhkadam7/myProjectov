package com.raybiztech.compliance.business;

public enum Recurring {
	WEEKLY("Weekly"),MONTHLY("Monthly"),DAILY("Daily");
	
	private String recurring;

	private Recurring(String recurring) {
		this.recurring = recurring;
	}

	public String getRecurring() {
		return recurring;
	}
}
