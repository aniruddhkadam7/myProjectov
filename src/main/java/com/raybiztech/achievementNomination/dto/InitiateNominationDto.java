package com.raybiztech.achievementNomination.dto;

import java.io.Serializable;
import java.util.Set;

public class InitiateNominationDto implements Serializable {

	private static final long serialVersionUID = 61378616004233303L;

	private Long id;
	private NominationCycleDto nominationCycleDto;
	private Set<NominationQuestionDto> nominationQuestionDto;
	private Boolean checkQuestion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NominationCycleDto getNominationCycleDto() {
		return nominationCycleDto;
	}

	public void setNominationCycleDto(NominationCycleDto nominationCycleDto) {
		this.nominationCycleDto = nominationCycleDto;
	}

	public Set<NominationQuestionDto> getNominationQuestionDto() {
		return nominationQuestionDto;
	}

	public void setNominationQuestionDto(
			Set<NominationQuestionDto> nominationQuestionDto) {
		this.nominationQuestionDto = nominationQuestionDto;
	}

	public Boolean getCheckQuestion() {
		return checkQuestion;
	}

	public void setCheckQuestion(Boolean checkQuestion) {
		this.checkQuestion = checkQuestion;
	}

}
