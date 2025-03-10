package com.raybiztech.checklist.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.ProjectCheckList;

public class ChecklistSection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long checklistsectionId;
	private String sectionName;
	private EmpDepartment department;
	private Long createdBy;
	private Second createdDate;
	private Long updatedBy;
	private Second updatedDate;
	private Set<ProjectCheckList> checkList;
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
	public EmpDepartment getDepartment() {
		return department;
	}
	public void setDepartment(EmpDepartment department) {
		this.department = department;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public Second getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}
	public Long getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Second getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Set<ProjectCheckList> getCheckList() {
		return checkList;
	}
	public void setCheckList(Set<ProjectCheckList> checkList) {
		this.checkList = checkList;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((checkList == null) ? 0 : checkList.hashCode());
		result = prime
				* result
				+ ((checklistsectionId == null) ? 0 : checklistsectionId
						.hashCode());
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((department == null) ? 0 : department.hashCode());
		result = prime * result
				+ ((sectionName == null) ? 0 : sectionName.hashCode());
		result = prime * result
				+ ((updatedBy == null) ? 0 : updatedBy.hashCode());
		result = prime * result
				+ ((updatedDate == null) ? 0 : updatedDate.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChecklistSection other = (ChecklistSection) obj;
		if (checkList == null) {
			if (other.checkList != null)
				return false;
		} else if (!checkList.equals(other.checkList))
			return false;
		if (checklistsectionId == null) {
			if (other.checklistsectionId != null)
				return false;
		} else if (!checklistsectionId.equals(other.checklistsectionId))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (department == null) {
			if (other.department != null)
				return false;
		} else if (!department.equals(other.department))
			return false;
		if (sectionName == null) {
			if (other.sectionName != null)
				return false;
		} else if (!sectionName.equals(other.sectionName))
			return false;
		if (updatedBy == null) {
			if (other.updatedBy != null)
				return false;
		} else if (!updatedBy.equals(other.updatedBy))
			return false;
		if (updatedDate == null) {
			if (other.updatedDate != null)
				return false;
		} else if (!updatedDate.equals(other.updatedDate))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ChecklistSection [checklistsectionId=" + checklistsectionId
				+ ", sectionName=" + sectionName + ", department=" + department
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate
				+ ", checkList=" + checkList + "]";
	}
	
	}
