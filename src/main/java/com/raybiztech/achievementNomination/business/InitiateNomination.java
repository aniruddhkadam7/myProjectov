package com.raybiztech.achievementNomination.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.date.Second;

/**
 * @author Aprajita
 */
public class InitiateNomination implements Serializable {

	private static final long serialVersionUID = -292733858313742L;

	private Long id;
	private NominationCycle nominationCycle;
	private Long createdBy;
	private Second createdDate;
	private Set<NominationQuestion> nominationQuestions;
	private Boolean checkQuestion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NominationCycle getNominationCycle() {
		return nominationCycle;
	}

	public void setNominationCycle(NominationCycle nominationCycle) {
		this.nominationCycle = nominationCycle;
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

	

	

	public Set<NominationQuestion> getNominationQuestions() {
		return nominationQuestions;
	}

	public void setNominationQuestions(Set<NominationQuestion> nominationQuestions) {
		this.nominationQuestions = nominationQuestions;
	}

	public Boolean getCheckQuestion() {
		return checkQuestion;
	}

	public void setCheckQuestion(Boolean checkQuestion) {
		this.checkQuestion = checkQuestion;
	}
	
	

}
