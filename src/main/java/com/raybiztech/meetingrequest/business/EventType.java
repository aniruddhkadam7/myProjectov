package com.raybiztech.meetingrequest.business;

import java.io.Serializable;

public class EventType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6743518136000763576L;
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
