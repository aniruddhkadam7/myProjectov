package com.raybiztech.checklist.dto;

import java.util.Set;

public class DepartmentSectionDto {
	private Long departmentSectionId;
	private Long sectionsId;
	private Long employeeId;
	private String sectionName;
	private String employeeName;
	private String checklistDate;
	private String compliance;

	private Set<DepartmentChecklistDTO> checklistDTOs;

	/*
	 * private String deptChecklistName; private Boolean deptChecklistStatus;
	 * private String Comments;
	 */
	public String getCompliance() {
		return compliance;
	}

	public void setCompliance(String compliance) {
		this.compliance = compliance;
	}

	public Set<DepartmentChecklistDTO> getChecklistDTOs() {
		return checklistDTOs;
	}

	public void setChecklistDTOs(Set<DepartmentChecklistDTO> checklistDTOs) {
		this.checklistDTOs = checklistDTOs;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getDepartmentSectionId() {
		return departmentSectionId;
	}

	public void setDepartmentSectionId(Long departmentSectionId) {
		this.departmentSectionId = departmentSectionId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getChecklistDate() {
		return checklistDate;
	}

	public void setChecklistDate(String checklistDate) {
		this.checklistDate = checklistDate;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public Long getSectionsId() {
		return sectionsId;
	}

	public void setSectionsId(Long sectionsId) {
		this.sectionsId = sectionsId;
	}

}
