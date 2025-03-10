package com.raybiztech.projectmanagement.business;

import java.io.Serializable;

public class ProjectNumbers implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4934495120664660726L;
	private Long id;
	private Project project;
	private ChangeRequest changeRequest;
	private String poNumber;
	private String projectAmount;
	private String currency;
	private String hourlyRate;
	private String description;
	private String saltKey;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
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

	public String getSaltKey() {
		return saltKey;
	}

	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}

	public ChangeRequest getChangeRequest() {
		return changeRequest;
	}

	public void setChangeRequest(ChangeRequest changeRequest) {
		this.changeRequest = changeRequest;
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

}
