package com.raybiztech.checklist.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.checklist.business.ChecklistSection;
import com.raybiztech.checklist.business.DepartmentChecklist;
import com.raybiztech.checklist.business.DepartmentSection;
import com.raybiztech.checklist.dto.ChecklistSectionDTO;
import com.raybiztech.checklist.dto.DepartmentChecklistDTO;
import com.raybiztech.checklist.dto.DepartmentSectionDto;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

@Component("checklistSectionBuilder")
public class ChecklistSectionBuilder {
	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DepartmentChecklistBuilder departmentChecklistBuilder;

	public ChecklistSection toEntity(ChecklistSectionDTO checklistSectionDTO) {
		// converting checklistsectionDTO to business
		ChecklistSection checklistSection = null;
		if (checklistSectionDTO != null) {
			checklistSection = new ChecklistSection();
			checklistSection.setSectionName(checklistSectionDTO
					.getSectionName());
			checklistSection.setDepartment(dao.findBy(EmpDepartment.class,
					checklistSectionDTO.getDepartmentId()));
			checklistSection.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			checklistSection.setCreatedDate(new Second());
		}

		return checklistSection;
	}

	public ChecklistSectionDTO toDto(ChecklistSection checklistSections) {
		// converting checklistSectionbusiness to DTO
		ChecklistSectionDTO checklistSectionDTO = null;
		checklistSectionDTO = new ChecklistSectionDTO();
		checklistSectionDTO.setChecklistsectionId(checklistSections
				.getChecklistsectionId());
		checklistSectionDTO.setSectionName(checklistSections.getSectionName());

		return checklistSectionDTO;
	}

	// editing checklist section
	public ChecklistSection toEditEntity(ChecklistSectionDTO checklistSectionDTO) {
		ChecklistSection checklistSection = dao.findBy(ChecklistSection.class,
				checklistSectionDTO.getChecklistsectionId());
		checklistSection.setChecklistsectionId(checklistSectionDTO
				.getChecklistsectionId());
		checklistSection.setSectionName(checklistSectionDTO.getSectionName());
		checklistSection.setDepartment(dao.findBy(EmpDepartment.class,
				checklistSectionDTO.getDepartmentId()));
		checklistSection.setUpdatedBy(securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder());
		checklistSection.setUpdatedDate(new Second());
		return checklistSection;

	}

	public DepartmentSection toEntity(DepartmentSectionDto departmentSectionDto) {
		DepartmentSection departmentSection = null;

		departmentSection = new DepartmentSection();
		// departmentSection.setChecklist(departmentChecklistBuilder.toEntity(departmentSectionDto.getChecklistDTOs()));
		departmentSection.setChecklistDate(new Date());
		departmentSection.setEmployee(dao.findBy(Employee.class,
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder()));
		departmentSection.setSection(dao.findBy(ChecklistSection.class,
				departmentSectionDto.getSectionsId()));
		departmentSection.setChecklist(departmentChecklistBuilder
				.toEntityChecklists(departmentSectionDto.getChecklistDTOs()));

		return departmentSection;

	}

	public DepartmentSectionDto toSectionDTO(DepartmentSection departmentSection) {
		DepartmentSectionDto departmentSectionDto = null;
		int checklistCount = 0;
		if (departmentSection != null) {
			departmentSectionDto = new DepartmentSectionDto();
			
			departmentSectionDto.setDepartmentSectionId(departmentSection.getId());

			departmentSectionDto.setSectionsId(departmentSection.getSection()
					.getChecklistsectionId());
			departmentSectionDto.setChecklistDate(departmentSection
					.getChecklistDate().toString("dd/MM/yyyy"));

			departmentSectionDto.setSectionName(departmentSection.getSection()
					.getSectionName());
			Employee employee = departmentSection.getEmployee();
			departmentSectionDto
					.setEmployeeName(employee.getEmployeeFullName());
			// departmentSectionDto.setChecklistDTOs(departmentChecklistBuilder.toDTO(departmentSection.getChecklist()));
			departmentSectionDto.setChecklistDTOs(departmentChecklistBuilder
					.toDTOChecklistDTOs(departmentSection.getChecklist()));
			departmentSectionDto.setChecklistDTOs(departmentChecklistBuilder
					.toDTOSET(departmentSection.getChecklist()));
			for (DepartmentChecklist deptChk : departmentSection.getChecklist()) {
				if (!deptChk.getDeptChecklistStatus()) {
					checklistCount = checklistCount + 1;
				}
			}

			departmentSectionDto.setCompliance(checklistCount + "/"
					+ departmentSection.getChecklist().size());
		}

		return departmentSectionDto;

	}

	public List<DepartmentSectionDto> todtolist(
			List<DepartmentSection> sectionList) {
		List<DepartmentSectionDto> sectionDtoList = new ArrayList<DepartmentSectionDto>();
		for (DepartmentSection section : sectionList) {
			sectionDtoList.add(toSectionDTO(section));
		}

		return sectionDtoList;

	}

}
