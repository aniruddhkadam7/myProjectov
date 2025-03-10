package com.raybiztech.appraisals.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class TimeSlot implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8785174731507224430L;

	private Long id;
	private String name;
	private String startTime;
	private String endTime;
	private String graceTime;
	private Long createdBy;
	private Long updatedBy;
	private Second createdDate;
	private Second updatedDate;
	
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
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String starttime) {
		this.startTime = starttime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getGraceTime() {
		return graceTime;
	}
	public void setGraceTime(String graceTime) {
		this.graceTime = graceTime;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Second getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}
	public Second getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}
	
}
