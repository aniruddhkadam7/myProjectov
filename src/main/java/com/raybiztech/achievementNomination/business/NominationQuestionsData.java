package com.raybiztech.achievementNomination.business;

import java.io.Serializable;

/**
 * @author Aprajita
 */
public class NominationQuestionsData implements Serializable {

	private static final long serialVersionUID = -2067898939260780985L;

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
