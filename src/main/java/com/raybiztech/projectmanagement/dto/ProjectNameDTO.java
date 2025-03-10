package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;

public class ProjectNameDTO implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private Long id;
	
private String projectName;

private  String  startdate;

private String enddate;

public String getStartdate() {
	return startdate;
}

public void setStartdate(String startdate) {
	this.startdate = startdate;
}

public String getEnddate() {
	return enddate;
}

public void setEnddate(String enddate) {
	this.enddate = enddate;
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getProjectName() {
	return projectName;
}

public void setProjectName(String projectName) {
	this.projectName = projectName;
}
}
