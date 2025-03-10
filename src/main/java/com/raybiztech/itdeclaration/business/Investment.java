package com.raybiztech.itdeclaration.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class Investment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long investmentId;
	private String investmentName;
	private Long maxLimit;
	private String description;
	private String requiredDocs;
	private Long createdBy;
	private Long updatedBy;
	private Second createdDate;
	private Second updatedDate;
	private Section section;

	public Long getInvestmentId() {
		return investmentId;
	}

	public void setInvestmentId(Long investmentId) {
		this.investmentId = investmentId;
	}

	public String getInvestmentName() {
		return investmentName;
	}

	public void setInvestmentName(String investmentName) {
		this.investmentName = investmentName;
	}

	public Long getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(Long maxLimit) {
		this.maxLimit = maxLimit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getRequiredDocs() {
		return requiredDocs;
	}

	public void setRequiredDocs(String requiredDocs) {
		this.requiredDocs = requiredDocs;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((investmentId == null) ? 0 : investmentId.hashCode());
		result = prime * result
				+ ((investmentName == null) ? 0 : investmentName.hashCode());
		result = prime * result
				+ ((maxLimit == null) ? 0 : maxLimit.hashCode());
		result = prime * result
				+ ((requiredDocs == null) ? 0 : requiredDocs.hashCode());
		result = prime * result + ((section == null) ? 0 : section.hashCode());
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
		Investment other = (Investment) obj;
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
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (investmentId == null) {
			if (other.investmentId != null)
				return false;
		} else if (!investmentId.equals(other.investmentId))
			return false;
		if (investmentName == null) {
			if (other.investmentName != null)
				return false;
		} else if (!investmentName.equals(other.investmentName))
			return false;
		if (maxLimit == null) {
			if (other.maxLimit != null)
				return false;
		} else if (!maxLimit.equals(other.maxLimit))
			return false;
		if (requiredDocs == null) {
			if (other.requiredDocs != null)
				return false;
		} else if (!requiredDocs.equals(other.requiredDocs))
			return false;
		if (section == null) {
			if (other.section != null)
				return false;
		} else if (!section.equals(other.section))
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
		return "Investment [investmentId=" + investmentId + ", investmentName="
				+ investmentName + ", maxLimit=" + maxLimit + ", description="
				+ description + ", requiredDocs=" + requiredDocs
				+ ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + ", section=" + section + "]";
	}

	

	

}
