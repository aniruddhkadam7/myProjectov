package com.raybiztech.projectmanagement.dao;

import java.io.Serializable;

import com.raybiztech.projectmanagement.business.Project;

public class AllocationDetailsDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Project project;
	private Long count;
	

	public Project getProject() {
		return project;
	}

	public Long getCount() {
		return count;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	
	
	

}
