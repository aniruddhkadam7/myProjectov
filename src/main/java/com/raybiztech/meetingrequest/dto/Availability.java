package com.raybiztech.meetingrequest.dto;

import java.io.Serializable;

public class Availability implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8102624943980707518L;
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAvailability() {
		return availability;
	}
	public void setAvailability(String availability) {
		this.availability = availability;
	}
	private String availability;
	
	
}
