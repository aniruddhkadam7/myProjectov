package com.raybiztech.achievementNomination.builder;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.raybiztech.achievementNomination.business.NominationQuestionsData;
import com.raybiztech.achievementNomination.dto.NominationQuestionDataDto;

@Component("nominationQuestionsDataBuilder")
public class NominationQuestionsDataBuilder {

	public NominationQuestionsData toEntity(NominationQuestionDataDto dataDto) {
		NominationQuestionsData nominationQuestionsData = null;
		if (dataDto != null) {
			nominationQuestionsData = new NominationQuestionsData();
			nominationQuestionsData.setQuestions(dataDto.getQuestions());
			nominationQuestionsData.setFeedBack(dataDto.getFeedBack());
		}
		return nominationQuestionsData;
	}

	public NominationQuestionDataDto toDto(NominationQuestionsData data) {
		NominationQuestionDataDto nominationQuestionDataDto = null;
		if (data != null) {
			nominationQuestionDataDto = new NominationQuestionDataDto();
			nominationQuestionDataDto.setQuestions(data.getQuestions());
			nominationQuestionDataDto.setFeedBack(data.getFeedBack());
		}
		return nominationQuestionDataDto;
	}

	public Set<NominationQuestionsData> toEntityList(
			Set<NominationQuestionDataDto> questionDataDtos) {
		Set<NominationQuestionsData> nominationQuestionsData = null;
		if (questionDataDtos != null) {
			nominationQuestionsData = new HashSet<NominationQuestionsData>();
			for (NominationQuestionDataDto dataDto : questionDataDtos) {
				nominationQuestionsData.add(toEntity(dataDto));
			}
		}
		return nominationQuestionsData;
	}

	public Set<NominationQuestionDataDto> toDtoList(
			Set<NominationQuestionsData> nominationQuestionsDatas) {

		Set<NominationQuestionDataDto> dataDtos = null;
		if (nominationQuestionsDatas != null) {
			dataDtos = new HashSet<NominationQuestionDataDto>();
			for (NominationQuestionsData data : nominationQuestionsDatas) {
				dataDtos.add(toDto(data));
			}
		}
		return dataDtos;
	}

}
