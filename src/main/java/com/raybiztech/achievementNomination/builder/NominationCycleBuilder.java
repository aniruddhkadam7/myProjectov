package com.raybiztech.achievementNomination.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.achievementNomination.business.NominationCycle;
import com.raybiztech.achievementNomination.dto.NominationCycleDto;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;

/**
 * @author Aprajita
 */

@Component("nominationCycleBuilder")
public class NominationCycleBuilder {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(NominationCycleBuilder.class);

	public NominationCycle convertNominationCycleDtoToEntity(
			NominationCycleDto nominationCycleDto) {

		NominationCycle nominationCycle = null;
		if (nominationCycleDto != null) {
			nominationCycle = new NominationCycle();
			nominationCycle.setCycleName(nominationCycleDto.getCycleName());
			try {
				nominationCycle.setFromMonth(Date.parse(
						nominationCycleDto.getFromMonth(), "MM/yyyy"));
				nominationCycle.setToMonth(Date.parse(
						nominationCycleDto.getToMonth(), "MM/yyyy"));
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
			nominationCycle.setActivateFlag(nominationCycleDto
					.getActivateFlag());
			nominationCycle.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			nominationCycle.setCreatedDate(new Second());
			try {
				nominationCycle.setStartDate(Date.parse(
						nominationCycleDto.getStartDate(), "dd/MM/yyyy"));
				nominationCycle.setEndDate(Date.parse(
						nominationCycleDto.getEndDate(), "dd/MM/yyyy"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return nominationCycle;
	}

	public NominationCycleDto convertNominationCycleToDto(
			NominationCycle nominationCycle) {

		NominationCycleDto nominationCycleDto = null;
		if (nominationCycle != null) {
			nominationCycleDto = new NominationCycleDto();
			nominationCycleDto.setId(nominationCycle.getId());
			nominationCycleDto.setCycleName(nominationCycle.getCycleName());
			nominationCycleDto
					.setFromMonth(nominationCycle.getFromMonth() != null ? nominationCycle
							.getFromMonth().toString("MM/yyyy") : null);
			nominationCycleDto
					.setToMonth(nominationCycle.getToMonth() != null ? nominationCycle
							.getToMonth().toString("MM/yyyy") : null);
			nominationCycleDto.setActivateFlag(nominationCycle
					.getActivateFlag());
			// nominationCycleDto.setCreatedBy(createdBy != null ? createdBy
			// .getEmployeeFullName() : "");
			nominationCycleDto
					.setStartDate(nominationCycle.getStartDate() != null ? nominationCycle
							.getStartDate().toString("dd/MM/yyyy") : null);
			nominationCycleDto
					.setEndDate(nominationCycle.getEndDate() != null ? nominationCycle
							.getEndDate().toString("dd/MM/yyyy") : null);

		}

		return nominationCycleDto;
	}

	public List<NominationCycleDto> convertToDtoList(
			List<NominationCycle> nominationCycles) {

		List<NominationCycleDto> nominationCycleDtos = null;
		if (nominationCycles != null) {
			nominationCycleDtos = new ArrayList<NominationCycleDto>();
			for (NominationCycle nominationCycle : nominationCycles) {
				nominationCycleDtos
						.add(convertNominationCycleToDto(nominationCycle));

			}
		}

		return nominationCycleDtos;
	}

	public NominationCycle convertdtoToEditEntity(
			NominationCycleDto nominationCycleDto) {

		NominationCycle nominationCycle = null;
		if (nominationCycleDto != null) {
			nominationCycle = dao.findBy(NominationCycle.class,
					nominationCycleDto.getId());
			nominationCycle.setCycleName(nominationCycleDto.getCycleName());
			try {
				/*
				 * nominationCycle
				 * .setFromMonth(nominationCycleDto.getFromMonth() != null ?
				 * Date.parse(nominationCycleDto.getFromMonth(), "MM/yyyy") :
				 * null);
				 */
				nominationCycle.setFromMonth(Date.parse(
						nominationCycleDto.getFromMonth(), "MM/yyyy"));
				nominationCycle
						.setToMonth(nominationCycleDto.getToMonth() != null ? Date
								.parse(nominationCycleDto.getToMonth(),
										"MM/yyyy") : null);
			} catch (ParseException pe) {
				pe.printStackTrace();
			}
			nominationCycle.setActivateFlag(nominationCycleDto
					.getActivateFlag());
			nominationCycle.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			nominationCycle.setCreatedDate(new Second());
			try {
				nominationCycle
						.setStartDate(nominationCycleDto.getStartDate() != null ? DateParser
								.toDate(nominationCycleDto.getStartDate())
								: null);
				nominationCycle
						.setEndDate(nominationCycleDto.getEndDate() != null ? DateParser
								.toDate(nominationCycleDto.getEndDate()) : null);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return nominationCycle;
	}

}
