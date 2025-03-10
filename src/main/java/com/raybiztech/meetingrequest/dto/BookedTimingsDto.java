package com.raybiztech.meetingrequest.dto;

import java.util.List;

public class BookedTimingsDto {

	public String bookedTimings;
	public String authorName;
	public String agenda;
	public List<String> attendees;

	public String getBookedTimings() {
		return bookedTimings;
	}

	public void setBookedTimings(String bookedTimings) {
		this.bookedTimings = bookedTimings;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	public List<String> getAttendees() {
		return attendees;
	}

	public void setAttendees(List<String> attendees) {
		this.attendees = attendees;
	}

}
