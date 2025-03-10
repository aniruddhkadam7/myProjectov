package com.raybiztech.projectmanagement.dto;

public class ChangeRequestDTO {

	private Long id;
	private String name;
	private String descripition;
	private String duration;
	private Long projectId;
	private Boolean numbersStatus;

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

	public String getDescripition() {
		return descripition;
	}

	public void setDescripition(String descripition) {
		this.descripition = descripition;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public Boolean getNumbersStatus() {
		return numbersStatus;
	}

	public void setNumbersStatus(Boolean numbersStatus) {
		this.numbersStatus = numbersStatus;
	} 
}
