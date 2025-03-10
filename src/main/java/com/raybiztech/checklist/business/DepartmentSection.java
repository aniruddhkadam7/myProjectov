package com.raybiztech.checklist.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class DepartmentSection implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private ChecklistSection section;
	private Employee employee;
	private Set<DepartmentChecklist> checklist;
	private Date checklistDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ChecklistSection getSection() {
		return section;
	}

	public void setSection(ChecklistSection section) {
		this.section = section;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getChecklistDate() {
		return checklistDate;
	}

	public void setChecklistDate(Date checklistDate) {
		this.checklistDate = checklistDate;
	}

	public Set<DepartmentChecklist> getChecklist() {
		return checklist;
	}

	public void setChecklist(Set<DepartmentChecklist> checklist) {
		this.checklist = checklist;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((checklist == null) ? 0 : checklist.hashCode());
		result = prime * result
				+ ((checklistDate == null) ? 0 : checklistDate.hashCode());
		result = prime * result
				+ ((employee == null) ? 0 : employee.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((section == null) ? 0 : section.hashCode());
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
		DepartmentSection other = (DepartmentSection) obj;
		if (checklist == null) {
			if (other.checklist != null)
				return false;
		} else if (!checklist.equals(other.checklist))
			return false;
		if (checklistDate == null) {
			if (other.checklistDate != null)
				return false;
		} else if (!checklistDate.equals(other.checklistDate))
			return false;
		if (employee == null) {
			if (other.employee != null)
				return false;
		} else if (!employee.equals(other.employee))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (section == null) {
			if (other.section != null)
				return false;
		} else if (!section.equals(other.section))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DepartmentSection [id=" + id + ", section=" + section
				+ ", employee=" + employee + ", checklist=" + checklist
				+ ", checklistDate=" + checklistDate + "]";
	}
	
}
