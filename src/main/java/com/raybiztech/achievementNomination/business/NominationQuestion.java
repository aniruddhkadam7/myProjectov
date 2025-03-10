package com.raybiztech.achievementNomination.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

/**
 * @author Aprajita
 */
public class NominationQuestion implements Serializable {

	private static final long serialVersionUID = -292733858311232L;

	private Long id;
	private String question;
	private Long createdBy;
	private Second createdDate;

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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	

}
