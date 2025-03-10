package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class Journals implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3980508420605672891L;
	
	
	private Long id;
	private Long journable_id;
	private String journable_type;
	private users user_id;
	private String notes;
	private Second created_at;
	private Long version;
	private String activity_type;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getJournable_id() {
		return journable_id;
	}
	public void setJournable_id(Long journable_id) {
		this.journable_id = journable_id;
	}
	public String getJournable_type() {
		return journable_type;
	}
	public void setJournable_type(String journable_type) {
		this.journable_type = journable_type;
	}
	
	public users getUser_id() {
		return user_id;
	}
	public void setUser_id(users user_id) {
		this.user_id = user_id;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public Second getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Second created_at) {
		this.created_at = created_at;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getActivity_type() {
		return activity_type;
	}
	public void setActivity_type(String activity_type) {
		this.activity_type = activity_type;
	}
	
	

}
