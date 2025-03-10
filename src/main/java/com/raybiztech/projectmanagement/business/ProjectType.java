package com.raybiztech.projectmanagement.business;

public enum ProjectType {

	FIXEDBID("FIXEDBID"), RETAINER("RETAINER"), TM("T&M"), SUPPORT("SUPPORT");

	private String type;

	private ProjectType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
