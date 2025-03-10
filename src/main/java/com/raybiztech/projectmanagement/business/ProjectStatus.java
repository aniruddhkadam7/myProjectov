package com.raybiztech.projectmanagement.business;

public enum ProjectStatus {
	NEW("New"), CLOSED("Closed"), ONHOLD("On Hold"), INPROGRESS("In Progress");
	private String projectStatus;

	public String getProjectStatus() {
		return projectStatus;
	}

	private ProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

}
