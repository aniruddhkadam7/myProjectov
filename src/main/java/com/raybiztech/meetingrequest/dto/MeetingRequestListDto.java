package com.raybiztech.meetingrequest.dto;

import java.util.List;

public class MeetingRequestListDto {

	public Long id;
	public String locationName;
	public String roomName;
	public List<BookedTimingsDto> bookedTimings;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public List<BookedTimingsDto> getBookedTimings() {
		return bookedTimings;
	}

	public void setBookedTimings(List<BookedTimingsDto> bookedTimings) {
		this.bookedTimings = bookedTimings;
	}

}
