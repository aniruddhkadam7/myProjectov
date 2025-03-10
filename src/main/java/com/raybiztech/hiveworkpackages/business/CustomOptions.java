package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;

public class CustomOptions implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4001007347343575756L;
	
	private Long id;
	private Long custom_field_id;
	private String value;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustom_field_id() {
		return custom_field_id;
	}
	public void setCustom_field_id(Long custom_field_id) {
		this.custom_field_id = custom_field_id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	
	

}
