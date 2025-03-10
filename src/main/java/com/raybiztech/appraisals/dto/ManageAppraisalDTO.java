package com.raybiztech.appraisals.dto;

import java.util.Date;

public class ManageAppraisalDTO {
	
	
	private Long id;
	private String name;
	private String description;
	private Date from;
	private Date to;
	private String done;
	private String status;
	
	public ManageAppraisalDTO() {
	}

	public ManageAppraisalDTO(String name, String description, Date from, Date to) {

		this.name = name;
		this.description = description;
		this.from = from;
		this.to = to;
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
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	public String getDone() {
		return done;
	}
	public void setDone(String done) {
		this.done = done;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
