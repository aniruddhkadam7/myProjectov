package com.raybiztech.hiveProject.business;

import java.io.Serializable;

public class HiveDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5047479640396645247L;
	/**
	 * 
	 */
	

	private Long id;
	private String location;
	private String key;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@Override
	public String toString() {
		return "HiveDetails [id=" + id + ", location=" + location + ", key=" + key + "]";
	}
	
	
	
	
}
