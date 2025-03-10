package com.raybiztech.mailtemplates.business;

import java.io.Serializable;

import com.raybiztech.date.Date;

public class AccountEmail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1735328341474945520L;
	private Long id;
	private String email;
	private String password;
	private Date createdDate;
	private String saltKey;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSaltKey() {
		return saltKey;
	}
	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}

	
	
}
