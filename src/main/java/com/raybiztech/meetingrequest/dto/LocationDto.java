package com.raybiztech.meetingrequest.dto;

public class LocationDto {

	private Long id;
	private String locationName;
	private Boolean locationStatus;

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

	public Boolean getLocationStatus() {
		return locationStatus;
	}

	public void setLocationStatus(Boolean locationStatus) {
		this.locationStatus = locationStatus;
	}
	

}
