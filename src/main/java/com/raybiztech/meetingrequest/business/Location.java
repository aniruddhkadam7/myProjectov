package com.raybiztech.meetingrequest.business;

import java.io.Serializable;

public class Location implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3248738460217990739L;

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
