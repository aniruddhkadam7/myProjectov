package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;

import com.raybiztech.date.Second;


public class projects implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7855043952422181541L;
	
	private Long id;
	private String name;
	private String description ;
	private Integer is_public;
	private Long parent_id;
	private Second created_on;
	private Second updated_on;
	private String identifier;
	private Long status;
	private Long lft;
	private Long rgt;
	private project_types project_type_id;
	
	//we don't require these fields as no user exists with user id -1
	/*private users responsible_id;
	private Long work_packages_responsible_id;*/
	
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getIs_public() {
		return is_public;
	}
	public void setIs_public(Integer is_public) {
		this.is_public = is_public;
	}
	public Long getParent_id() {
		return parent_id;
	}
	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}
	public Second getCreated_on() {
		return created_on;
	}
	public void setCreated_on(Second created_on) {
		this.created_on = created_on;
	}
	public Second getUpdated_on() {
		return updated_on;
	}
	public void setUpdated_on(Second updated_on) {
		this.updated_on = updated_on;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public Long getLft() {
		return lft;
	}
	public void setLft(Long lft) {
		this.lft = lft;
	}
	public Long getRgt() {
		return rgt;
	}
	public void setRgt(Long rgt) {
		this.rgt = rgt;
	}
	public project_types getProject_type_id() {
		return project_type_id;
	}
	public void setProject_type_id(project_types project_type_id) {
		this.project_type_id = project_type_id;
	}
	/*public users getResponsible_id() {
		return responsible_id;
	}
	public void setResponsible_id(users responsible_id) {
		this.responsible_id = responsible_id;
	}
	public Long getWork_packages_responsible_id() {
		return work_packages_responsible_id;
	}
	public void setWork_packages_responsible_id(Long work_packages_responsible_id) {
		this.work_packages_responsible_id = work_packages_responsible_id;
	}*/
	
	
	
	
	
	
	
	

}
