package com.raybiztech.appraisals.dto;


public class TimeSlotDTO {
	private Long id;
	private String name;
	private String startTimeHour;
	private String startTimeMinutes;
	private String endTimeHour;
	private String endTimeMinutes;
	private String graceTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartTimeHour() {
		return startTimeHour;
	}
	public void setStartTimeHour(String startTimeHour) {
		this.startTimeHour = startTimeHour;
	}
	public String getStartTimeMinutes() {
		return startTimeMinutes;
	}
	public void setStartTimeMinutes(String startTimeMinutes) {
		this.startTimeMinutes = startTimeMinutes;
	}
	public String getEndTimeHour() {
		return endTimeHour;
	}
	public void setEndTimeHour(String endTimeHour) {
		this.endTimeHour = endTimeHour;
	}
	public String getEndTimeMinutes() {
		return endTimeMinutes;
	}
	public void setEndTimeMinutes(String endTimeMinutes) {
		this.endTimeMinutes = endTimeMinutes;
	}
	public String getGraceTime() {
		return graceTime;
	}
	public void setGraceTime(String graceTime) {
		this.graceTime = graceTime;
	}
	
}
