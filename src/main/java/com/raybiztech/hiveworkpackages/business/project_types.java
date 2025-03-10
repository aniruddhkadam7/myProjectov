package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class project_types implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8919072325129792094L;

	private Long id;
	private String name;
	private Integer allows_association;
	private Long position;
	private Second created_at;
	private Second updated_at;
	
	
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
	public Integer getAllows_association() {
		return allows_association;
	}
	public void setAllows_association(Integer allows_association) {
		this.allows_association = allows_association;
	}
	public Long getPosition() {
		return position;
	}
	public void setPosition(Long position) {
		this.position = position;
	}
	public Second getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Second created_at) {
		this.created_at = created_at;
	}
	public Second getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Second updated_at) {
		this.updated_at = updated_at;
	}
	
	

}
