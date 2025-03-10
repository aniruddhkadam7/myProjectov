package com.raybiztech.projectmanagement.business;

public enum ProjectModel {
	DEVELOPMENT("DEVELOPMENT"),SUPPORT("SUPPORT");
	
	private String model;

	private ProjectModel(String model) {
		this.model = model;
	}

	public String getModel() {
		return model;
	}

}
