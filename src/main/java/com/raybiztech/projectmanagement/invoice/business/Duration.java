package com.raybiztech.projectmanagement.invoice.business;

public enum Duration {
	HOURS("HOURS"), DAYS("DAYS"), MONTHS("MONTHS");
	private String duration;
	private Duration(String duration) {
		this.duration = duration;
	}

	public String getDuration() {
		return duration;
	}

}
