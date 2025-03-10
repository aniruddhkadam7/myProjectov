package com.raybiztech.meetingrequest.dto;

import java.util.List;

public class ReservedTimingsForEvent {

	public String date;
	public List<String> timings;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<String> getTimings() {
		return timings;
	}

	public void setTimings(List<String> timings) {
		this.timings = timings;
	}

}
