package com.raybiztech.achievementNomination.builder;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.achievementNomination.business.InitiateNomination;
import com.raybiztech.achievementNomination.business.NominationCycle;
import com.raybiztech.achievementNomination.business.NominationQuestion;
import com.raybiztech.achievementNomination.dto.InitiateNominationDto;
import com.raybiztech.achievementNomination.dto.NominationDto;
import com.raybiztech.achievementNomination.dto.NominationQuestionDataDto;
import com.raybiztech.achievementNomination.dto.NominationQuestionDto;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;

@Component("initiateNominationBuilder")
public class InitiateNominationBuilder {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DAO dao;

	@Autowired
	NominationCycleBuilder nominationCycleBuilder;
	@Autowired
	NominationQuestionBuilder nominationQuestionBuilder;

	Logger logger = Logger.getLogger(InitiateNominationBuilder.class);

	public InitiateNomination toEntity(InitiateNominationDto initiateNomineeDto) {

		InitiateNomination initiateNominee = new InitiateNomination();
		if (initiateNomineeDto != null) {
			Set<NominationQuestion> nominationQuestionsSet = new HashSet<NominationQuestion>();
			for (NominationQuestionDto nominationQuestionsDto : initiateNomineeDto
					.getNominationQuestionDto()) {
				nominationQuestionsSet.add(dao.findBy(NominationQuestion.class,
						nominationQuestionsDto.getId()));
			}
			initiateNominee.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			initiateNominee.setCreatedDate(new Second());
			initiateNominee.setNominationCycle(dao.findBy(

			NominationCycle.class, initiateNomineeDto.getNominationCycleDto()
					.getId()));
			initiateNominee.setNominationQuestions(nominationQuestionsSet);
		}
		return initiateNominee;
	}

	public InitiateNomination convertDTOtoEntity(InitiateNominationDto dto) {

		InitiateNomination initiateNomination = null;
		if (dto != null) {
			initiateNomination = new InitiateNomination();
			initiateNomination.setCheckQuestion(dto.getCheckQuestion());
			initiateNomination.setId(dto.getId());
			initiateNomination.setNominationCycle(nominationCycleBuilder
					.convertNominationCycleDtoToEntity(dto
							.getNominationCycleDto()));
			initiateNomination.setNominationQuestions(nominationQuestionBuilder
					.convertToEntitySet(dto.getNominationQuestionDto()));
		}
		return initiateNomination;
	}

	public InitiateNominationDto concertEntityToDto(
			InitiateNomination initiateNomination) {
		InitiateNominationDto dto = null;
		if (initiateNomination != null) {
			dto = new InitiateNominationDto();
			//dto.setCheckQuestion(initiateNomination.getCheckQuestion());
			dto.setId(initiateNomination.getId());

			dto.setNominationCycleDto(nominationCycleBuilder
					.convertNominationCycleToDto(initiateNomination
							.getNominationCycle()));

			dto.setNominationQuestionDto(nominationQuestionBuilder
					.convetTODtoList(initiateNomination
							.getNominationQuestions()));
		}

		return dto;
	}

	public NominationDto buildNominationForm(
			InitiateNomination nominationDetails) {

		NominationDto dto = null;
		if (nominationDetails != null) {
			dto = new NominationDto();

			NominationCycle cycle = nominationDetails.getNominationCycle();
			dto.setCycleID(cycle.getId());
			dto.setCycleName(cycle.getCycleName());
			dto.setFromMonth(cycle.getFromMonth().toString("MM/yyyy"));
			dto.setToMonth(cycle.getToMonth().toString("MM/yyyy"));
			//dto.setAchievementTypeId(1L);

			Set<NominationQuestionDataDto> dataDtoSet = new HashSet<NominationQuestionDataDto>();
			for (NominationQuestion nominationQuestion : nominationDetails
					.getNominationQuestions()) {
				NominationQuestionDataDto dataDto = new NominationQuestionDataDto();
				dataDto.setQuestions(nominationQuestion.getQuestion());
				dataDtoSet.add(dataDto);
			}
			dto.setNominationQuestionDataDtosId(dataDtoSet);

		}
		return dto;

	}
}
