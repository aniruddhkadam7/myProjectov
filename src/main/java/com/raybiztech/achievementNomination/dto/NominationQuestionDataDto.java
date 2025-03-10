package com.raybiztech.achievementNomination.dto;

import java.io.Serializable;

public class NominationQuestionDataDto implements Serializable{

	private static final long serialVersionUID = 3388983733912439403L;
	
	private Long id;
	private String questions;
	private String feedBack;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQuestions() {
		return questions;
	}
	public void setQuestions(String questions) {
		this.questions = questions;
	}
	public String getFeedBack() {
		return feedBack;
	}
	public void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}
	
	
	
	
	

}
