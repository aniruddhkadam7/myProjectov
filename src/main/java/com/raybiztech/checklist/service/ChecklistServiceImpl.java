package com.raybiztech.checklist.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.checklist.builder.ChecklistBuilder;
import com.raybiztech.checklist.builder.ChecklistSectionBuilder;
import com.raybiztech.checklist.builder.DepartmentChecklistBuilder;
import com.raybiztech.checklist.business.ChecklistSection;
import com.raybiztech.checklist.business.DepartmentSection;
import com.raybiztech.checklist.dao.ChecklistDao;
import com.raybiztech.checklist.dto.ChecklistConfigurationDto;
import com.raybiztech.checklist.dto.ChecklistDTO;
import com.raybiztech.checklist.dto.ChecklistSectionDTO;
import com.raybiztech.checklist.dto.DepartmentSectionDto;
import com.raybiztech.checklist.service.ChecklistService;
import com.raybiztech.date.Date;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.projectmanagement.business.ProjectCheckList;
import com.raybiztech.rolefeature.business.Permission;

@Service("ChecklistService")
@Transactional
public class ChecklistServiceImpl implements ChecklistService {

	@Autowired
	ChecklistDao checklistDao;
	@Autowired
	ChecklistBuilder checklistBuilder;
	@Autowired
	ChecklistSectionBuilder checklistSectionBuilder;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	DepartmentChecklistBuilder departmentChecklistBuilder;

	@Override
	public List<EmpDepartment> getDeparments() {
		
		return checklistDao.get(EmpDepartment.class);
		
	}

	@Override
	public List<ChecklistSectionDTO> getSections(Long deptId) {
		List<ChecklistSection> list = checklistDao.getSections(deptId);

		return checklistBuilder.todtolist(list);
	}

	@Override
	public void saveChecklistItem(ChecklistConfigurationDto configurationDto) {

		checklistDao.save(checklistBuilder.toEntity(configurationDto));

	}

	@Override
	public List<ChecklistConfigurationDto> getChecklistItems(Long departmentId) {
		List<ProjectCheckList> list = checklistDao
				.getChecklistItems(departmentId);

		return checklistBuilder.toDtoList(list);
	}

	public void saveSection(ChecklistSectionDTO checklistSectionDTO) {
		checklistDao
				.save(checklistSectionBuilder.toEntity(checklistSectionDTO));
	}

	@Override
	public void editSection(ChecklistSectionDTO checklistSectionDTO) {
		checklistDao.saveOrUpdate(checklistSectionBuilder
				.toEditEntity(checklistSectionDTO));
	}

	@Override
	public void editChecklistSection(ChecklistConfigurationDto configurationDto) {
		checklistDao.saveOrUpdate(checklistBuilder
				.toEditEntity(configurationDto));
	}

	@Override
	public Boolean isSectionExist(String sectionName, Long departmentId) {
		return checklistDao.isSectionExist(sectionName, departmentId);
	}

	@Override
	public Boolean isAlreadyExist(String name, Long departmentId, Long sectionId) {

		return checklistDao.isAlreadyExist(name, departmentId, sectionId);
	}

	public List<ChecklistSectionDTO> getSectionsByDeptId() {
		Employee employee = checklistDao.findBy(Employee.class,
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
		EmpDepartment empDepartment = checklistDao.findByUniqueProperty(
				EmpDepartment.class, "departmentName",
				employee.getDepartmentName());

		return getSections(empDepartment.getDepartmentId());
	}

	/*
	 * @Override public List<ChecklistDTO> getChecklistBySecId(Long sectionsId)
	 * {
	 * 
	 * return
	 * checklistBuilder.toListCheckListDTO(checklistDao.checkLists(sectionsId));
	 * }
	 */

	@Override
	public void addComments(DepartmentSectionDto departmentSectionDto) {
		checklistDao.save(checklistSectionBuilder
				.toEntity(departmentSectionDto));
	}

	@Override
	public List<ChecklistDTO> getChecklistBySecId(Long sectionsId) {
		ChecklistSection chkSec = checklistDao.findBy(ChecklistSection.class,
				sectionsId);
		Set<ProjectCheckList> set = chkSec.getCheckList();

		return checklistBuilder.toListCheckListDTO(checklistDao
				.checkLists(sectionsId));
	}

	@Override
	public Map<String, Object> getDepartmentSections(Long departmentId,
			Integer startIndex, Integer endIndex, String dateSelection,
			String from, String to, String multiSearch) {

		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		} else {

			Map<String, Date> dateMap = checklistDao
					.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Permission adminChecklist = checklistDao.checkForPermission(
				"Total Checklist", employee);

		Permission deptChecklist = checklistDao.checkForPermission(
				"Department Wise Checklist", employee);

		Map<String, Object> detailsMap = null;

		if (adminChecklist.getView() && !deptChecklist.getView()) {
			detailsMap = checklistDao.getDepartmentSections(departmentId,
					fromDate, toDate, multiSearch, startIndex, endIndex);

		} else if (adminChecklist.getView() && deptChecklist.getView()) {

			EmpDepartment department = checklistDao.findByUniqueProperty(
					EmpDepartment.class, "departmentName",
					employee.getDepartmentName());

			detailsMap = checklistDao.getDepartmentSections(department.getDepartmentId(),
					fromDate, toDate, multiSearch, startIndex, endIndex);

		}

		Map<String, Object> map = new HashMap<String, Object>();
		List<DepartmentSectionDto> dtoList = checklistSectionBuilder
				.todtolist((List) detailsMap.get("list"));

		map.put("list", dtoList);
		map.put("size", detailsMap.get("size"));

		return map;

	}
}
