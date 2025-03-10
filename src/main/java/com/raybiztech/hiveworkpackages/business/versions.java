package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class versions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 48771584998916083L;
	
	
	private Long id;
	private projects project_id;
	private String name;
	private String description ;
	private Date effective_date;
	private Second created_on;
	private Second updated_on;
	private String wiki_page_title ;
	private String  	status ;
	private  String sharing;
	private Date start_date;
	
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getEffective_date() {
		return effective_date;
	}
	public void setEffective_date(Date effective_date) {
		this.effective_date = effective_date;
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
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	
	
	
	
	
	
	

}
