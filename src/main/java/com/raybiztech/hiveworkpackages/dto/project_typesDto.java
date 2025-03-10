package com.raybiztech.hiveworkpackages.dto;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class project_typesDto  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9043583125810343743L;
	
	
	private Long id;
	private String name;
	private Integer allows_association;
	private Long position;
	private String created_at;
	private String updated_at;
	
	
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
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	
	
	

}
