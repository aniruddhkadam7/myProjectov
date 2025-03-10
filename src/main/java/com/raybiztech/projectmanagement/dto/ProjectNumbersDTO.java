package com.raybiztech.projectmanagement.dto;

public class ProjectNumbersDTO {

	private Long id;
	private Long projectId;
	private String projectname;
	private String projectAmount;
	private String currency;
	private Long changeRequestId;
	private String crName;
	private String hourlyRate;
	private String description;
	private String crhours;
	private String poNumber;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectAmount() {
		return projectAmount;
	}

	public void setProjectAmount(String projectAmount) {
		this.projectAmount = projectAmount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public Long getChangeRequestId() {
		return changeRequestId;
	}

	public void setChangeRequestId(Long changeRequestId) {
		this.changeRequestId = changeRequestId;
	}

	public String getCrName() {
		return crName;
	}

	public void setCrName(String crName) {
		this.crName = crName;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getHourlyRate() {
		return hourlyRate;
	}

	public void setHourlyRate(String hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCrhours() {
		return crhours;
	}

	public void setCrhours(String crhours) {
		this.crhours = crhours;
	}

}
