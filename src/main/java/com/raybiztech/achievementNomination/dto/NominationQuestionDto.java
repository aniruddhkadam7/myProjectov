package com.raybiztech.achievementNomination.dto;

import java.io.Serializable;

public class NominationQuestionDto implements Serializable{
	
	private static final long serialVersionUID = -6918503994017192862L;
	
	private Long id;
	private String question;
	private Boolean checkQuestion;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public Boolean getCheckQuestion() {
		return checkQuestion;
	}
	public void setCheckQuestion(Boolean checkQuestion) {
		this.checkQuestion = checkQuestion;
	}
	
	
	
}
