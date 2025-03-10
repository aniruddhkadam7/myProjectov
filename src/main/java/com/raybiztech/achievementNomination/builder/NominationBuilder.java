package com.raybiztech.achievementNomination.builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.achievement.business.AchievementType;
import com.raybiztech.achievementNomination.business.Nomination;
import com.raybiztech.achievementNomination.business.NominationCycle;
import com.raybiztech.achievementNomination.dto.NominationDto;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;

@Component("nominationBuilder")
public class NominationBuilder {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	NominationQuestionsDataBuilder dataBuilder;

	public Nomination toEntity(NominationDto nominationDto) {
		Nomination nomination = null;
		if (nominationDto != null) {
			nomination = new Nomination();
			nomination.setAchievementType(dao.findBy(AchievementType.class,
					nominationDto.getAchievementTypeId()));
			nomination.setEmployee(dao.findBy(Employee.class,
					nominationDto.getEmployeeId()));
			nomination.setNominationCycleId(dao.findBy(NominationCycle.class,
					nominationDto.getCycleID()));
			nomination.setNominationQuestionsData(dataBuilder
					.toEntityList(nominationDto
							.getNominationQuestionDataDtosId()));
			nomination.setRating(nominationDto.getRating());
			nomination.setFinalComments(nominationDto.getFinalComments());
			nomination.setNominationStatus(nominationDto.getNominationStatus());
			nomination.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			nomination.setCreatedDate(new Second());

		}
		return nomination;
	}

	public NominationDto toDto(Nomination nomination) {

		NominationDto nominationDto = null;
		if (nomination != null) {
			nominationDto = new NominationDto();
			AchievementType achievementType = nomination.getAchievementType();
			NominationCycle cycle = nomination.getNominationCycleId();
			Employee nominee = nomination.getEmployee();
			nominationDto.setAchievementTypeId(achievementType.getId());
			nominationDto.setAchievementType(achievementType
					.getAchievementType());
			nominationDto.setCycleName(cycle.getCycleName());
			nominationDto.setEmployeeName(nominee.getFullName());
			nominationDto.setEmployeeId(nominee.getEmployeeId());
			nominationDto
					.setFromMonth(cycle.getFromMonth().toString("MM/yyyy"));
			nominationDto.setToMonth(cycle.getToMonth().toString("MM/yyyy"));
			nominationDto.setId(nomination.getId());
			nominationDto.setFinalComments(nomination.getFinalComments());
			nominationDto.setNominationStatus(nomination.getNominationStatus());
			nominationDto.setRating(nomination.getRating());

			nominationDto.setNominationQuestionDataDtosId(dataBuilder
					.toDtoList(nomination.getNominationQuestionsData()));
		}
		return nominationDto;

	}

}
