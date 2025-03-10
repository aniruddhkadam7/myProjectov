package com.raybiztech.checklist.dto;

import java.io.Serializable;

public class DepartmentChecklistDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long deptChecklistId;
	private String name;
	private Boolean deptChecklistStatus;
	private String comments;
	public Long getDeptChecklistId() {
		return deptChecklistId;
	}
	public void setDeptChecklistId(Long deptChecklistId) {
		this.deptChecklistId = deptChecklistId;
	}
	
	public Boolean getDeptChecklistStatus() {
		return deptChecklistStatus;
	}
	public void setDeptChecklistStatus(Boolean deptChecklistStatus) {
		this.deptChecklistStatus = deptChecklistStatus;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
