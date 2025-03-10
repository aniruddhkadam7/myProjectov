package com.raybiztech.hiveworkpackages.dto;

import java.io.Serializable;

public class categoriesDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1054965980878618731L;
	
	private Long id;
	private Long project_id;
	private  String	name;
	private  Long assigned_to_id;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getProject_id() {
		return project_id;
	}
	public void setProject_id(Long project_id) {
		this.project_id = project_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getAssigned_to_id() {
		return assigned_to_id;
	}
	public void setAssigned_to_id(Long assigned_to_id) {
		this.assigned_to_id = assigned_to_id;
	}
	
	

}
