package com.raybiztech.appraisals.dto;

import java.io.Serializable;

public class CycleDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3821126864439913804L;
	private Long id;
	private String name;
	private String description;
	private String fromDate;
	private String toDate;

	private Integer percentage_Done;
	private String status;

	public Integer getPercentage_Done() {
		return percentage_Done;
	}

	public void setPercentage_Done(Integer percentage_Done) {
		this.percentage_Done = percentage_Done;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	@Override
	public String toString() {
		return "name :" + name + " description: " + description
				+ " from date: " + fromDate + " to date: " + toDate;
	}

}
