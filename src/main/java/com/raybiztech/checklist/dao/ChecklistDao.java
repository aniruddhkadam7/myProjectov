package com.raybiztech.checklist.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.checklist.business.ChecklistSection;
import com.raybiztech.checklist.business.DepartmentSection;
import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.business.ProjectCheckList;

public interface ChecklistDao extends DAO {
	List<ChecklistSection> getSections(Long deptId);

	List<ProjectCheckList> getChecklistItems(Long departmentId);

	// void saveSection(ChecklistSectionDTO checklistSectionDTO);

	public Boolean isSectionExist(String sectionName, Long departmentId);

	public Boolean isAlreadyExist(String name, Long departmentId, Long sectionId);

	public Map<String, Object> getDepartmentSections(Long departmentId,
			Date fromDate, Date toDate, String multiSearch, Integer startindex,
			Integer endIndex);

	// public List<ProjectCheckList> checkLists(Long sectionsId);

	// for getting only status true checklist
	public List<ProjectCheckList> checkLists(Long sectionsId);

}
