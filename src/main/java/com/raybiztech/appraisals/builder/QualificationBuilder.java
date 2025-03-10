package com.raybiztech.appraisals.builder;

import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Qualification;
import com.raybiztech.appraisals.business.QualificationLookUp;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.MultipleSelectionDTO;
import com.raybiztech.appraisals.dto.QualificationDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;

@Component("qualificationBuilder")
public class QualificationBuilder {

	@Autowired
	DAO dao;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	QualificationLookUpBuilder qualificationLookUpBuilder;

	Logger logger = Logger.getLogger(QualificationBuilder.class);

	public Qualification toEntity(QualificationDTO qualificationDTO) {
		Long loggedInEmp = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		Qualification qualification = null;
		String pg = null;
		String ug = null;
		String hsc = null;

		if (qualificationDTO != null) {
			qualification = new Qualification();
			qualification.setEmployee(dao.findBy(Employee.class, loggedInEmp));
			for (MultipleSelectionDTO multipleSelectionDTOList : qualificationDTO
					.getPgLookUp()) {
				pg = pg + "," + multipleSelectionDTOList.getId();
			}
			for (MultipleSelectionDTO multipleSelectionDTOList : qualificationDTO
					.getGraduationLookUp()) {
				ug = ug + "," + multipleSelectionDTOList.getId();
			}
			/*for (MultipleSelectionDTO multipleSelectionDTOList : qualificationDTO
					.getHscLookUp()) {
				hsc = hsc + "," + multipleSelectionDTOList.getId();
			}*/
			String str = pg + ug + hsc;
			qualification.setQualificationDetails(str.replace("null", ""));

			qualification.setSscName(qualificationDTO.getSscName());
			qualification.setHscName(qualificationDTO.getHscName());
			qualification.setCreatedBy(loggedInEmp);
			qualification.setCreatedDate(new Second());
			qualification.setOthers(qualificationDTO.getOthers());

		}
		return qualification;
	}

	public QualificationDTO toDto(Qualification qualification) {

		Employee createdBy = null, updatedBy = null;
		/*createdBy = qualification.getCreatedBy() != null ? dao.findBy(
				Employee.class, qualification.getCreatedBy()) : null;
		updatedBy = qualification.getUpdatedBy() != null ? dao.findBy(
				Employee.class, qualification.getUpdatedBy()) : null;
*/
		QualificationDTO qualificationDTO = null;
		if (qualification != null) {
			qualificationDTO = new QualificationDTO();
			List<MultipleSelectionDTO> pgMultipleList = new ArrayList<MultipleSelectionDTO>();
			List<MultipleSelectionDTO> ugMultipleList = new ArrayList<MultipleSelectionDTO>();
			//List<MultipleSelectionDTO> hscMultipleList = new ArrayList<MultipleSelectionDTO>();

			Employee employee = qualification.getEmployee();
			qualificationDTO.setEmpId(employee.getEmployeeId());
			qualificationDTO.setEmpName(employee.getEmployeeFullName());
			qualificationDTO.setId(qualification.getId());
			qualificationDTO.setSscName(qualification.getSscName());
			qualificationDTO.setHscName(qualification.getHscName());
			String[] qualificationdetails = qualification
					.getQualificationDetails().split(",");
			if (qualificationdetails != null) {
				for (String string : qualificationdetails) {
					if (!string.equalsIgnoreCase("") && string != null) {
						QualificationLookUp qualificationLookUp = dao
								.findBy(QualificationLookUp.class,
										Long.valueOf(string));
						if (qualificationLookUp.getQualificationCategory()
								.equalsIgnoreCase("Post Graduation")) {
							MultipleSelectionDTO pgMultipleDto = new MultipleSelectionDTO();
							pgMultipleDto.setId(qualificationLookUp.getId()
									.toString());
							pgMultipleDto.setLabel(qualificationLookUp
									.getQualificationName());
							pgMultipleList.add(pgMultipleDto);
						} else if (qualificationLookUp
								.getQualificationCategory().equalsIgnoreCase(
										"Graduation")) {
							MultipleSelectionDTO ugMultipleDto = new MultipleSelectionDTO();
							ugMultipleDto.setId(qualificationLookUp.getId()
									.toString());
							ugMultipleDto.setLabel(qualificationLookUp
									.getQualificationName());
							ugMultipleList.add(ugMultipleDto);
						} /*else if (qualificationLookUp
								.getQualificationCategory().equalsIgnoreCase(
										"Higher Secondary Certificate")) {
							MultipleSelectionDTO hscMultipleDto = new MultipleSelectionDTO();
							hscMultipleDto.setId(qualificationLookUp.getId()
									.toString());
							hscMultipleDto.setLabel(qualificationLookUp
									.getQualificationName());
							hscMultipleList.add(hscMultipleDto);
						}*/
					}
				}
				qualificationDTO.setPgLookUp(pgMultipleList);
				qualificationDTO.setGraduationLookUp(ugMultipleList);
				//qualificationDTO.setHscLookUp(hscMultipleList);
			}
			qualificationDTO.setCreatedBy(createdBy != null ? createdBy
					.getFullName() : "");
			qualificationDTO.setUpdatedBy(updatedBy != null ? updatedBy
					.getFullName() : "");
			qualificationDTO
					.setCreatedDate(qualification.getCreatedDate() != null ? qualification
							.getCreatedDate().toString("dd/MM/yyyy") : "");
			qualificationDTO
					.setUpdatedDate(qualification.getUpdatedDate() != null ? qualification
							.getUpdatedDate().toString("dd/MM/yyyy") : "");
			qualificationDTO.setOthers(qualification.getOthers());
		}
		return qualificationDTO;

	}

	public List<QualificationDTO> toDtoList(List<Qualification> qualifications) {

		List<QualificationDTO> qualificationDTOs = new ArrayList<QualificationDTO>();
		if (qualificationDTOs != null) {
			for (Qualification qualification : qualifications) {
				qualificationDTOs.add(toDto(qualification));
			}
		}

		return qualificationDTOs;
	}

}
