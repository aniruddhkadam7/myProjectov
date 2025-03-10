package com.raybiztech.projectmanagement.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;

public class ProjectProposals implements Serializable {

	/**
	 * Shashank
	 */

	private static final long serialVersionUID = -1626509879891264709L;
	private Long id;
	private String post;
	private Employee postedBy;
	private Second postedOn;
	private Project project;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public Employee getPostedBy() {
		return postedBy;
	}

	public void setPostedBy(Employee postedBy) {
		this.postedBy = postedBy;
	}

	public Second getPostedOn() {
		return postedOn;
	}

	public void setPostedOn(Second postedOn) {
		this.postedOn = postedOn;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
