package com.raybiztech.appraisals.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.QualificationLookUp;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.MultipleSelectionDTO;
import com.raybiztech.appraisals.dto.QualificationLookUpDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;

@Component("QualificationLookUp")
public class QualificationLookUpBuilder {

	@Autowired
	DAO dao;

	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(QualificationLookUpBuilder.class);

	public QualificationLookUp toEntity(
			QualificationLookUpDTO qualificationLookUpDTO) {
		Long loggedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);

		QualificationLookUp qualificationLookUp = null;
		if (qualificationLookUpDTO != null) {
			qualificationLookUp = new QualificationLookUp();
			qualificationLookUp.setQualificationCategory(qualificationLookUpDTO
					.getQualificationCategory());
			qualificationLookUp.setQualificationName(qualificationLookUpDTO
					.getQualificationName());
			qualificationLookUp.setCreatedBy(employee.getEmployeeId());
			qualificationLookUp.setCreatedDate(new Second());
		}

		return qualificationLookUp;

	}

	public QualificationLookUpDTO toDTO(QualificationLookUp qualificationLookUp) {

		Employee createdBy = null, updatedBy = null;
		createdBy = qualificationLookUp.getCreatedBy() != null ? dao.findBy(
				Employee.class, qualificationLookUp.getCreatedBy()) : null;
		updatedBy = qualificationLookUp.getUpdatedBy() != null ? dao.findBy(
				Employee.class, qualificationLookUp.getUpdatedBy()) : null;
		QualificationLookUpDTO dto = null;
		if (qualificationLookUp != null) {
			dto = new QualificationLookUpDTO();
			dto.setId(qualificationLookUp.getId());
			dto.setQualificationCategory(qualificationLookUp
					.getQualificationCategory());
			dto.setQualificationName(qualificationLookUp.getQualificationName());
			dto.setCreatedBy(createdBy != null ? createdBy.getFullName() : "");
			dto.setUpdatedBy(updatedBy != null ? updatedBy.getFullName() : "");
			dto.setCreatedDate(qualificationLookUp.getCreatedDate() != null ? qualificationLookUp
					.getCreatedDate().toString("dd/MM/yyyy") : "");
			dto.setUpdatedDate(qualificationLookUp.getUpdatedDate() != null ? qualificationLookUp
					.getUpdatedDate().toString("dd/MM/yyyy") : "");
		}
		return dto;

	}

	public List<QualificationLookUpDTO> toDTOList(
			List<QualificationLookUp> qualificationLookUps) {
		List<QualificationLookUpDTO> qualificationDtos = new ArrayList<QualificationLookUpDTO>();
		if (qualificationLookUps != null) {
			for (QualificationLookUp qualificationLookUp : qualificationLookUps) {
				qualificationDtos.add(toDTO(qualificationLookUp));
			}
		}
		return qualificationDtos;

	}

	public Set<QualificationLookUp> toEntityList(
			Set<QualificationLookUpDTO> qualificationLookUps) {
		Set<QualificationLookUp> qualificationDtos = null;
		if (qualificationLookUps != null) {
			qualificationDtos = new HashSet<QualificationLookUp>();
			for (QualificationLookUpDTO qualificationLookUp : qualificationLookUps) {
				qualificationDtos.add(toEntity(qualificationLookUp));
			}
		}
		return qualificationDtos;
	}

	public Set<QualificationLookUpDTO> toDtoList(
			Set<QualificationLookUp> qualificationLookUps) {
		Set<QualificationLookUpDTO> qualificationDtos = null;
		if (qualificationLookUps != null) {
			qualificationDtos = new HashSet<QualificationLookUpDTO>();
			for (QualificationLookUp qualificationLookUp : qualificationLookUps) {
				qualificationDtos.add(toDTO(qualificationLookUp));
			}
		}
		return qualificationDtos;
	}

	public Map<String, Object> qualificationLookUpList(
			List<QualificationLookUp> qualificationLookUps) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<MultipleSelectionDTO> pgSelectionDTOs = new ArrayList<MultipleSelectionDTO>();
		List<MultipleSelectionDTO> graduationDTOs = new ArrayList<MultipleSelectionDTO>();
		//List<MultipleSelectionDTO> hscDTOs = new ArrayList<MultipleSelectionDTO>();

		if (qualificationLookUps != null) {
			for (QualificationLookUp qualificationLookUp : qualificationLookUps) {
				MultipleSelectionDTO dto = new MultipleSelectionDTO();
				dto.setId(qualificationLookUp.getId().toString());
				dto.setLabel(qualificationLookUp.getQualificationName());
				if (qualificationLookUp.getQualificationCategory()
						.equalsIgnoreCase("Post Graduation")) {
					pgSelectionDTOs.add(dto);
				} else if (qualificationLookUp.getQualificationCategory()
						.equalsIgnoreCase("Graduation")) {
					graduationDTOs.add(dto);
				} /*else if (qualificationLookUp.getQualificationCategory()
						.equalsIgnoreCase("Higher Secondary Certificate")) {
					hscDTOs.add(dto);
				}*/
			}
		}
		map.put("pgDetails", pgSelectionDTOs);
		map.put("graduationDetails", graduationDTOs);
		//map.put("hscDetail", hscDTOs);
		return map;
	}

}