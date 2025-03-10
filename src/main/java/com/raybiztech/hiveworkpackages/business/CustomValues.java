package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;

public class CustomValues implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2463064139083909467L;
	
	
	private Long id;
	private String customizedType;
	private Long customizedId;
	private Long customFieldId;
	private String value;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCustomizedType() {
		return customizedType;
	}
	public void setCustomizedType(String customizedType) {
		this.customizedType = customizedType;
	}
	public Long getCustomizedId() {
		return customizedId;
	}
	public void setCustomizedId(Long customizedId) {
		this.customizedId = customizedId;
	}
	public Long getCustomFieldId() {
		return customFieldId;
	}
	public void setCustomFieldId(Long customFieldId) {
		this.customFieldId = customFieldId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	

}
