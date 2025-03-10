package com.raybiztech.checklist.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.checklist.dto.ChecklistConfigurationDto;
import com.raybiztech.checklist.dto.ChecklistDTO;
import com.raybiztech.checklist.dto.ChecklistSectionDTO;
import com.raybiztech.checklist.dto.DepartmentSectionDto;
import com.raybiztech.checklist.dto.DepartmentChecklistDTO;
import com.raybiztech.projectmanagement.business.ProjectCheckList;

public interface ChecklistService {
	public List<EmpDepartment> getDeparments();

	public void saveChecklistItem(ChecklistConfigurationDto configurationDto);

	public List<ChecklistConfigurationDto> getChecklistItems(Long departmentId);

	public List<ChecklistSectionDTO> getSections(Long deptId);

	public void saveSection(ChecklistSectionDTO checklistSectionDTO);

	public void editSection(ChecklistSectionDTO checklistSectionDTO);

	public Boolean isSectionExist(String sectionName, Long departmentId);

	public void editChecklistSection(ChecklistConfigurationDto configurationDto);

	public Boolean isAlreadyExist(String name, Long departmentId, Long sectionId);

	public Map<String, Object> getDepartmentSections(Long departmentId,
			Integer startIndex, Integer endIndex, String dateSelection,
			String from, String to, String multiSearch);

	public List<ChecklistSectionDTO> getSectionsByDeptId();

	public List<ChecklistDTO> getChecklistBySecId(Long sectionsId);

	public void addComments(DepartmentSectionDto departmentSectionDto);

}
