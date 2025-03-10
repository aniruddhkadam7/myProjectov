package com.raybiztech.ticketmanagement.DTO;

import java.io.Serializable;

public class MealLookUpDTO  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7310686770414700814L;
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
