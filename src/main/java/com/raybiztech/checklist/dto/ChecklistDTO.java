package com.raybiztech.checklist.dto;

import java.io.Serializable;


public class ChecklistDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long Id;
	private String name;
	private ChecklistSectionDTO checklistSectionDTO;
	private String type;
	private Boolean status;

	

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

	public ChecklistSectionDTO getChecklistSectionDTO() {
		return checklistSectionDTO;
	}

	public void setChecklistSectionDTO(ChecklistSectionDTO checklistSectionDTO) {
		this.checklistSectionDTO = checklistSectionDTO;
	}

}
