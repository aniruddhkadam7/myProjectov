package com.raybiztech.meetingrequest.business;

import java.io.Serializable;

public class Room implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2249606719886081080L;
	private Long id;
	private String roomName;
	private Location location;
	private Boolean roomStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Location getLocation() {
		return location;
    }

	public void setLocation(Location location) {
		this.location = location;
    }

	public Boolean getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(Boolean roomStatus) {
		this.roomStatus = roomStatus;
	}
	
}
