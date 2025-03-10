package com.raybiztech.itdeclaration.business;

import java.io.Serializable;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class FinanceCycle implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long cycleId;
	private String cycleName;
	private Date startDate;
	private Date endDate;
	private Boolean active;
	private Long createdBy;
	private Second createdDate;
	private Second updatedDate;
	
	public Long getCycleId() {
		return cycleId;
	}
	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
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
	public Second getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getCycleName() {
		return cycleName;
	}
	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}
	
	@Override
	public String toString() {
		return "FinanceCycle [cycleId=" + cycleId + ", cycleName=" + cycleName
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", active=" + active + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + ", getCycleId()=" + getCycleId()
				+ ", getStartDate()=" + getStartDate() + ", getEndDate()="
				+ getEndDate() + ", getActive()=" + getActive()
				+ ", getCreatedBy()=" + getCreatedBy() + ", getCreatedDate()="
				+ getCreatedDate() + ", getUpdatedDate()=" + getUpdatedDate()
				+ ", getCycleName()=" + getCycleName() + ", hashCode()="
				+ hashCode() + ", getClass()=" + getClass() + ", toString()="
				+ super.toString() + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((active == null) ? 0 : active.hashCode());
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((cycleId == null) ? 0 : cycleId.hashCode());
		result = prime * result
				+ ((cycleName == null) ? 0 : cycleName.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
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
		FinanceCycle other = (FinanceCycle) obj;
		if (active == null) {
			if (other.active != null)
				return false;
		} else if (!active.equals(other.active))
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
		if (cycleId == null) {
			if (other.cycleId != null)
				return false;
		} else if (!cycleId.equals(other.cycleId))
			return false;
		if (cycleName == null) {
			if (other.cycleName != null)
				return false;
		} else if (!cycleName.equals(other.cycleName))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (updatedDate == null) {
			if (other.updatedDate != null)
				return false;
		} else if (!updatedDate.equals(other.updatedDate))
			return false;
		return true;
	}
	
	
}
