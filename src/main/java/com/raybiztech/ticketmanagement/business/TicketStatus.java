package com.raybiztech.ticketmanagement.business;

public enum TicketStatus {
	Accepted("Accepted"), Cancelled("Cancelled"), Rejected("Rejected"),InProcess("In-Process");
	private	String  ticketStatus;
	private TicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

}
