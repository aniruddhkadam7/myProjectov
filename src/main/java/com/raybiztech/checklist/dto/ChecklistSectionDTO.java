package com.raybiztech.checklist.dto;

public class ChecklistSectionDTO {
	private Long checklistsectionId;
	private String sectionName;
	private Long departmentId;
	private String departmentName;

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getChecklistsectionId() {
		return checklistsectionId;
	}

	public void setChecklistsectionId(Long checklistsectionId) {
		this.checklistsectionId = checklistsectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

}
