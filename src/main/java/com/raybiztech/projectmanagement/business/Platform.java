package com.raybiztech.projectmanagement.business;

import java.io.Serializable;

public class Platform  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7115654498490438074L;
	
	private Long id;
	private String name;
	
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
	

}
