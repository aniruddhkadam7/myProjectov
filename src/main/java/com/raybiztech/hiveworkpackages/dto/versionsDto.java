package com.raybiztech.hiveworkpackages.dto;

import java.io.Serializable;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.hiveworkpackages.business.projects;

public class versionsDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5215713209077574079L;
	
	
	private Long id;
	private Long project_id;
	private String name;
	private String description ;
	private String effective_date;
	private String created_on;
	private String updated_on;
	private String wiki_page_title ;
	private String  status ;
	private String sharing;
	private String start_date;
	
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getEffective_date() {
		return effective_date;
	}
	public void setEffective_date(String effective_date) {
		this.effective_date = effective_date;
	}
	public String getCreated_on() {
		return created_on;
	}
	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}
	public String getUpdated_on() {
		return updated_on;
	}
	public void setUpdated_on(String updated_on) {
		this.updated_on = updated_on;
	}
	public String getWiki_page_title() {
		return wiki_page_title;
	}
	public void setWiki_page_title(String wiki_page_title) {
		this.wiki_page_title = wiki_page_title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSharing() {
		return sharing;
	}
	public void setSharing(String sharing) {
		this.sharing = sharing;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	
	

}
