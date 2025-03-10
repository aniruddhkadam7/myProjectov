package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;

public class CustomizableJournals implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2519749020675803673L;
	
	private Long id;
	private Journals journal_id;
	private Long custom_field_id;
	private String value;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Journals getJournal_id() {
		return journal_id;
	}
	public void setJournal_id(Journals journal_id) {
		this.journal_id = journal_id;
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
