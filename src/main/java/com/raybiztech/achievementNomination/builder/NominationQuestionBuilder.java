package com.raybiztech.achievementNomination.builder;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.achievementNomination.business.NominationQuestion;
import com.raybiztech.achievementNomination.dto.NominationQuestionDto;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;

/**
 * @author Aprajita
 */
@Component("nominationQuestionBuilder")
public class NominationQuestionBuilder implements Serializable {

	private static final long serialVersionUID = -1200653113492831942L;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DAO dao;

	public NominationQuestion convertNominationQuestionDtoToEntity(
			NominationQuestionDto nominationQuestionDto) {

		NominationQuestion nominationQuestion = null;
		if (nominationQuestionDto != null) {
			nominationQuestion = new NominationQuestion();
			nominationQuestion.setQuestion(nominationQuestionDto.getQuestion());
			nominationQuestion.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			nominationQuestion.setCreatedDate(new Second());
		}

		return nominationQuestion;
	}

	public NominationQuestionDto convertNominationQuestionEntityToDto(
			NominationQuestion nominationQuestion) {
		Employee createdBy = null;
		createdBy = nominationQuestion.getCreatedBy() != null ? dao.findBy(
				Employee.class, nominationQuestion.getCreatedBy()) : null;

		NominationQuestionDto nominationQuestionDto = null;
		if (nominationQuestion != null) {
			nominationQuestionDto = new NominationQuestionDto();
			nominationQuestionDto.setId(nominationQuestion.getId());
			nominationQuestionDto.setQuestion(nominationQuestion.getQuestion());
		}

		return nominationQuestionDto;
	}

	public Set<NominationQuestionDto> convetTODtoList(
			Set<NominationQuestion> nominationQuestions) {

		Set<NominationQuestionDto> nominationQuestionDtos = null;
		if (nominationQuestions != null) {
			nominationQuestionDtos = new HashSet<NominationQuestionDto>();
			for (NominationQuestion nominationQuestion : nominationQuestions) {
				nominationQuestionDtos
						.add(convertNominationQuestionEntityToDto(nominationQuestion));
			}

		}
		return nominationQuestionDtos;
	}

	public Set<NominationQuestion> convertToEntitySet(
			Set<NominationQuestionDto> nominationQuestionDtos) {

		Set<NominationQuestion> nominationQuestions = null;
		if (nominationQuestionDtos != null) {
			nominationQuestions = new HashSet<NominationQuestion>();
			for (NominationQuestionDto dto : nominationQuestionDtos) {
				NominationQuestion nominationQuestion = new NominationQuestion();
				nominationQuestion.setId(dto.getId());
				nominationQuestion.setQuestion(dto.getQuestion());
				nominationQuestions.add(nominationQuestion);
			}

		}
		return nominationQuestions;

	}

}
