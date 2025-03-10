package com.raybiztech.projectmanagement.business;

import java.io.Serializable;

import com.raybiztech.checklist.business.ChecklistSection;

public class ProjectCheckList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8870684026087951321L;
	private Long Id;
	private String name;
	private ChecklistSection checklistSection;
	private String type;
	private Boolean status;

	public ChecklistSection getChecklistSection() {
		return checklistSection;
	}

	public void setChecklistSection(ChecklistSection checklistSection) {
		this.checklistSection = checklistSection;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
