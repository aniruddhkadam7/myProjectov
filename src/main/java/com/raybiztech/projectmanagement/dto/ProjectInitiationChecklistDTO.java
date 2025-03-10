package com.raybiztech.projectmanagement.dto;

public class ProjectInitiationChecklistDTO {
	private Long id;
	private String name;
	private String answer;
	private String answer1;
	private String answer2;
	private String answer3;
	private String comments;
	private Long checklistId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Long getChecklistId() {
		return checklistId;
	}
	public void setChecklistId(Long checklistId) {
		this.checklistId = checklistId;
	}
	public String getAnswer1() {
		return answer1;
	}
	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}
	public String getAnswer2() {
		return answer2;
	}
	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}
	public String getAnswer3() {
		return answer3;
	}
	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

}
