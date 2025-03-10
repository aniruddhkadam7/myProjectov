package com.raybiztech.checklist.business;

import java.io.Serializable;


public class DepartmentChecklist implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long deptChecklistId;
	private String name;
	private Boolean deptChecklistStatus;
	private String comments;
	
	public Long getDeptChecklistId() {
		return deptChecklistId;
	}
	public void setDeptChecklistId(Long deptChecklistId) {
		this.deptChecklistId = deptChecklistId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getDeptChecklistStatus() {
		return deptChecklistStatus;
	}
	public void setDeptChecklistStatus(Boolean deptChecklistStatus) {
		this.deptChecklistStatus = deptChecklistStatus;
	}
	
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((deptChecklistId == null) ? 0 : deptChecklistId.hashCode());
		result = prime
				* result
				+ ((deptChecklistStatus == null) ? 0 : deptChecklistStatus
						.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		DepartmentChecklist other = (DepartmentChecklist) obj;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (deptChecklistId == null) {
			if (other.deptChecklistId != null)
				return false;
		} else if (!deptChecklistId.equals(other.deptChecklistId))
			return false;
		if (deptChecklistStatus == null) {
			if (other.deptChecklistStatus != null)
				return false;
		} else if (!deptChecklistStatus.equals(other.deptChecklistStatus))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "DepartmentChecklist [deptChecklistId=" + deptChecklistId
				+ ", name=" + name + ", deptChecklistStatus="
				+ deptChecklistStatus + ", comments=" + comments + "]";
	}
	
	
	
	

}
