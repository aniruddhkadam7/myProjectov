package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;

public class categories  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 807877904338219799L;
	
	private Long id;
	private projects project_id;
	private  String	name;
	private  users assigned_to_id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public projects getProject_id() {
		return project_id;
	}
	public void setProject_id(projects project_id) {
		this.project_id = project_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public users getAssigned_to_id() {
		return assigned_to_id;
	}
	public void setAssigned_to_id(users assigned_to_id) {
		this.assigned_to_id = assigned_to_id;
	}
	
	
	
	
}
