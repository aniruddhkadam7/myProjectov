package com.raybiztech.achievement.builder;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.achievement.business.AchievementType;
import com.raybiztech.achievement.dto.AchievementTypeDTO;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;

/**
 * @author Aprajita
 */

@Component("achievementTypeBuilder")
public class AchievementTypeBuilder {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(AchievementBuilder.class);

	public AchievementType convertAchievementDTOToEntity(
			AchievementTypeDTO achievementTypeDTO) {
		AchievementType achievementType = null;
		if (achievementTypeDTO != null) {
			achievementType = new AchievementType();
			achievementType
					.setAchievementType(achievementTypeDTO.getTypeName());
			achievementType.setStatus(achievementTypeDTO.getStatus());
			achievementType.setOrder(achievementTypeDTO.getOrder());
			achievementType.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			achievementType.setCreatedDate(new Second());
			achievementType.setTimePeriodRequired(achievementTypeDTO.getTimeperiodrequired());
			achievementType.setFromMonthToMontRequired(achievementTypeDTO.getDaterequired());
		}
		return achievementType;
	}

	public AchievementTypeDTO convertAchivementTypeToDTO(
			AchievementType achievementType) {

		Employee createdBy = null, updatedBy = null;
		createdBy = achievementType.getCreatedBy() != null ? dao.findBy(
				Employee.class, achievementType.getCreatedBy()) : null;
		updatedBy = achievementType.getUpdatedBy() != null ? dao.findBy(
				Employee.class, achievementType.getUpdatedBy()) : null;

		AchievementTypeDTO achievementTypeDTO = null;
		if (achievementType != null) {
			achievementTypeDTO = new AchievementTypeDTO();
			achievementTypeDTO.setId(achievementType.getId());
			achievementTypeDTO
					.setTypeName(achievementType.getAchievementType());
			achievementTypeDTO.setStatus(achievementType.getStatus());
			achievementTypeDTO.setOrder(achievementType.getOrder());
			achievementTypeDTO.setCreatedBy(createdBy != null ? createdBy
					.getEmployeeFullName() : "");
			achievementTypeDTO.setCreatedDate(achievementType.getCreatedDate()
					.toString("dd/MM/yyyy"));
			achievementTypeDTO.setUpdatedBy(updatedBy != null ? updatedBy
					.getEmployeeFullName() : "");
			achievementTypeDTO
					.setUpdatedDate(achievementType.getUpdatedDate() != null ? achievementType
							.getUpdatedDate().toString("dd/MM/yyyy") : null);
			achievementTypeDTO.setTimeperiodrequired(achievementType.getTimePeriodRequired());
			achievementTypeDTO.setDaterequired(achievementType.getFromMonthToMontRequired());
		}
		return achievementTypeDTO;
	}

	public List<AchievementTypeDTO> convertToDTOList(
			List<AchievementType> achievementTypes) {
		List<AchievementTypeDTO> achievementTypeDTOs = null;
		if (achievementTypes != null) {
			achievementTypeDTOs = new ArrayList<AchievementTypeDTO>();
			for (AchievementType achievementType : achievementTypes) {
				achievementTypeDTOs
						.add(convertAchivementTypeToDTO(achievementType));
			}
		}
		return achievementTypeDTOs;
	}

	public AchievementType toEditEntity(AchievementTypeDTO acTypeDTO) {
		Long loggedInEmpl = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		AchievementType achiType = null;
		if (acTypeDTO != null) {
			achiType = dao.findBy(AchievementType.class, acTypeDTO.getId());
			achiType.setAchievementType(acTypeDTO.getTypeName());
			achiType.setStatus(acTypeDTO.getStatus());
			if(acTypeDTO.getOrder()!= null) {
			achiType.setOrder(acTypeDTO.getOrder());
			}
			achiType.setUpdatedBy(loggedInEmpl);
			achiType.setUpdatedDate(new Second());
		}
		return achiType;
	}

}
