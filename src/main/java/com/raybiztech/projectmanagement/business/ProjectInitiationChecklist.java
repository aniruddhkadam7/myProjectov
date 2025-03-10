package com.raybiztech.projectmanagement.business;

import java.io.Serializable;

public class ProjectInitiationChecklist implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5807628598785920506L;
	
	private Long id;
	private ProjectCheckList checklist;
	private String answer;
	private String comments;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ProjectCheckList getChecklist() {
		return checklist;
	}
	public void setChecklist(ProjectCheckList checklist) {
		this.checklist = checklist;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
}
