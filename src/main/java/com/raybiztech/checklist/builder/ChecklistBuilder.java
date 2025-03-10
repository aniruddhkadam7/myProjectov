package com.raybiztech.checklist.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.checklist.business.ChecklistSection;
import com.raybiztech.checklist.dto.ChecklistConfigurationDto;
import com.raybiztech.checklist.dto.ChecklistDTO;
import com.raybiztech.checklist.dto.ChecklistSectionDTO;
import com.raybiztech.projectmanagement.business.ProjectCheckList;
import com.raybiztech.projectmanagement.business.ProjectInitiationChecklist;

@Component("checklistBuilder")
public class ChecklistBuilder {
	@Autowired
	DAO dao;
	@Autowired
	ChecklistSectionBuilder checklistSectionBuilder;

	public ChecklistSectionDTO todto(ChecklistSection section) {
		ChecklistSectionDTO sectiondto = null;

		if (section != null) {
			sectiondto = new ChecklistSectionDTO();
			sectiondto.setChecklistsectionId(section.getChecklistsectionId());
			sectiondto.setDepartmentId(section.getDepartment()
					.getDepartmentId());
			sectiondto.setDepartmentName(section.getDepartment()
					.getDepartmentName());
			sectiondto.setSectionName(section.getSectionName());

		}

		return sectiondto;

	}

	public ChecklistConfigurationDto DTO(ProjectCheckList checklist) {
		ChecklistConfigurationDto configurationDto = null;

		if (checklist != null) {
			configurationDto = new ChecklistConfigurationDto();
			configurationDto.setId(checklist.getId());
			configurationDto.setName(checklist.getName());
			// /System.out.println("sectioname"+checklist.getChecklistSection().getSectionName());
			if (checklist.getChecklistSection() != null) {
				configurationDto.setSectionName(checklist.getChecklistSection()
						.getSectionName());

				configurationDto.setSectionId(checklist.getChecklistSection()
						.getChecklistsectionId());
			}

			configurationDto.setStatus(checklist.getStatus());
		}

		return configurationDto;

	}

	public ProjectCheckList toEntity(ChecklistConfigurationDto configurationDto) {
		ProjectCheckList checklist = null;
		if (configurationDto != null) {
			checklist = new ProjectCheckList();
			checklist.setName(configurationDto.getName());
			checklist.setChecklistSection(dao.findBy(ChecklistSection.class,
					configurationDto.getSectionId()));
			checklist.setType("Checklist");
			checklist.setStatus(configurationDto.getStatus());
		}
		return checklist;

	}

	public ProjectCheckList toEditEntity(
			ChecklistConfigurationDto configurationDto) {
		ProjectCheckList checklist = dao.findBy(ProjectCheckList.class,
				configurationDto.getId());
		checklist.setName(configurationDto.getName());
		checklist.setChecklistSection(dao.findBy(ChecklistSection.class,
				configurationDto.getSectionId()));
		checklist.setType("Checklist");
		checklist.setStatus(configurationDto.getStatus());
		return checklist;
	}

	public List<ChecklistSectionDTO> todtolist(
			List<ChecklistSection> sectionList) {
		List<ChecklistSectionDTO> sectionDtoList = new ArrayList<ChecklistSectionDTO>();
		for (ChecklistSection section : sectionList) {
			sectionDtoList.add(todto(section));
		}

		return sectionDtoList;

	}

	public List<ChecklistConfigurationDto> toDtoList(
			List<ProjectCheckList> checklist) {
		List<ChecklistConfigurationDto> configurationDtoList = new ArrayList<ChecklistConfigurationDto>();
		for (ProjectCheckList check : checklist) {
			configurationDtoList.add(DTO(check));
		}

		return configurationDtoList;

	}
	
	public List<ChecklistDTO> toListCheckListDTO(List<ProjectCheckList> set) {
		List<ChecklistDTO> list = new ArrayList<ChecklistDTO>();
		if(set!=null){
			for(ProjectCheckList projChk: set){
				ChecklistDTO dto = new ChecklistDTO();
				dto.setId(projChk.getId());
				dto.setName(projChk.getName());
				dto.setStatus(projChk.getStatus());
				dto.setType(projChk.getType());
				dto.setChecklistSectionDTO(checklistSectionBuilder.toDto(projChk.getChecklistSection()));
				//dto.setChecklistSection(projChk.getChecklistSection());
				list.add(dto);
			}
			
			
		}
		return list;
	}
	public ProjectCheckList toEntity(ChecklistDTO checklistDTO){
		ProjectCheckList checkList = null;
		checkList.setName(checklistDTO.getName());
		checkList.setStatus(checklistDTO.getStatus());
		checkList.setType(checklistDTO.getType());
		checkList.setChecklistSection(checklistSectionBuilder.toEntity(checklistDTO.getChecklistSectionDTO()));
		return checkList;
		
	}
	public ChecklistDTO checklistDTO(ProjectCheckList checkList){
		
		ChecklistDTO checklistDTO = null;
		checklistDTO.setId(checkList.getId());
		checklistDTO.setName(checkList.getName());
		checklistDTO.setStatus(checkList.getStatus());
		checklistDTO.setType(checkList.getType());
		checklistDTO.setChecklistSectionDTO(checklistSectionBuilder.toDto(checkList.getChecklistSection()));
		return checklistDTO;
		
	}

}
