package com.raybiztech.checklist.builder;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.checklist.business.DepartmentChecklist;
import com.raybiztech.checklist.dto.DepartmentChecklistDTO;

@Component("departmentChecklistBuilder")
public class DepartmentChecklistBuilder {
	@Autowired
	ChecklistBuilder checklistBuilder;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	ChecklistSectionBuilder checklistSectionBuilder;

	public DepartmentChecklist toEntity(DepartmentChecklistDTO checklistDTO) {
		DepartmentChecklist departmentChecklist = null;

		departmentChecklist = new DepartmentChecklist();
		departmentChecklist.setComments(checklistDTO.getComments());
		departmentChecklist.setName(checklistDTO.getName());
		departmentChecklist.setDeptChecklistStatus(checklistDTO
				.getDeptChecklistStatus());
		// departmentChecklist.setChecklistSection(checklistSectionBuilder.toEntity(checklistDTO.getChecklistSectionDTO()));

		return departmentChecklist;

	}

	/*
	 * public DepartmentChecklist toEntity(DepartmentChecklistDTO checklistDTO){
	 * DepartmentChecklist departmentChecklist = null;
	 * if(departmentChecklist!=null){ departmentChecklist = new
	 * DepartmentChecklist();
	 * departmentChecklist.setDeptChecklistStatus(checklistDTO
	 * .getDeptChecklistStatus());
	 * departmentChecklist.setComments(checklistDTO.getComments()); }
	 * 
	 * 
	 * 
	 * return departmentChecklist;
	 * 
	 * }
	 */
	public DepartmentChecklistDTO toDTO(DepartmentChecklist departmentChecklist) {
		DepartmentChecklistDTO checklistDTO = null;
		if (departmentChecklist != null) {
			checklistDTO = new DepartmentChecklistDTO();
			checklistDTO.setDeptChecklistId(departmentChecklist.getDeptChecklistId());
			checklistDTO.setName(departmentChecklist.getName());
			checklistDTO.setDeptChecklistStatus(departmentChecklist
					.getDeptChecklistStatus());
			checklistDTO.setComments(departmentChecklist.getComments());

		}
		return checklistDTO;
	}

	public Set<DepartmentChecklistDTO> toDTOSET(
			Set<DepartmentChecklist> departmentChecklist) {
		Set<DepartmentChecklistDTO> checklistDtoList = null;
		if (departmentChecklist != null) {
			checklistDtoList = new HashSet<DepartmentChecklistDTO>();
			for (DepartmentChecklist checklist : departmentChecklist) {
				checklistDtoList.add(toDTO(checklist));
			}
		}

		return checklistDtoList;
	}

	/*
	 * public DepartmentChecklistDTO toDTO(DepartmentChecklist
	 * departmentChecklist){ DepartmentChecklistDTO checklistDTO = new
	 * DepartmentChecklistDTO();
	 * checklistDTO.setDeptChecklistId(departmentChecklist
	 * .getDeptChecklistId());
	 * checklistDTO.setComments(departmentChecklist.getComments());
	 * checklistDTO.setName(departmentChecklist.getName());
	 * checklistDTO.setDeptChecklistStatus
	 * (departmentChecklist.getDeptChecklistStatus());
	 * //checklistDTO.setChecklistSectionDTO
	 * (checklistSectionBuilder.toDto(departmentChecklist
	 * .getChecklistSection())); return checklistDTO;
	 * 
	 * }
	 */

	public Set<DepartmentChecklist> toEntityChecklists(
			Set<DepartmentChecklistDTO> checklistDTOs) {
		Set<DepartmentChecklist> setChecklists = new HashSet<DepartmentChecklist>();
		for (DepartmentChecklistDTO checklistDTO : checklistDTOs) {
			DepartmentChecklist checklist = new DepartmentChecklist();
			checklist.setName(checklistDTO.getName());
			checklist.setDeptChecklistStatus(checklistDTO
					.getDeptChecklistStatus());
			checklist.setComments(checklistDTO.getComments());
			setChecklists.add(checklist);
		}

		return setChecklists;

	}

	public Set<DepartmentChecklistDTO> toDTOChecklistDTOs(
			Set<DepartmentChecklist> checklists) {
		Set<DepartmentChecklistDTO> dtos = new HashSet<DepartmentChecklistDTO>();
		for (DepartmentChecklist checklist : checklists) {
			DepartmentChecklistDTO checklistDTO = new DepartmentChecklistDTO();
			checklistDTO.setDeptChecklistId(checklist.getDeptChecklistId());
			checklistDTO.setName(checklist.getName());
			checklistDTO.setDeptChecklistStatus(checklist
					.getDeptChecklistStatus());
			checklistDTO.setComments(checklist.getComments());
			dtos.add(checklistDTO);

		}
		return dtos;

	}

}