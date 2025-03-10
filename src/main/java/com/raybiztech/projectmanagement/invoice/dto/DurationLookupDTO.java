package com.raybiztech.projectmanagement.invoice.dto;

import java.io.Serializable;

public class DurationLookupDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5809026108002869407L;
	private Integer id;
	private String name;

	public DurationLookupDTO() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
