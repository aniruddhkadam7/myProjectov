package com.raybiztech.supportmanagement.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class Tracker implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2606297203860040971L;

	private Long id;
	private String name;
	private Boolean permission;
	private Long createdBy;
	private Second createdDate;

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

	public Boolean getPermission() {
		return permission;
	}

	public void setPermission(Boolean permission) {
		this.permission = permission;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

}
