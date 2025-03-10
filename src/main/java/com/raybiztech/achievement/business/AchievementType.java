package com.raybiztech.achievement.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

/**
 * @author Aprajita
 */

public class AchievementType implements Serializable{
	
	private static final long serialVersionUID = -292733858315324L;
	
	private Long id ;
	private String achievementType;
	private Long createdBy;
	private Second createdDate;
	private Long updatedBy;
	private Second updatedDate;
	private Boolean status;
	private Long order;
	private Boolean timePeriodRequired;
	private Boolean fromMonthToMontRequired;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAchievementType() {
		return achievementType;
	}
	public void setAchievementType(String achievementType) {
		this.achievementType = achievementType;
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
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Long getOrder() {
		return order;
	}
	public void setOrder(Long order) {
		this.order = order;
	}
	public Boolean getTimePeriodRequired() {
		return timePeriodRequired;
	}
	public void setTimePeriodRequired(Boolean timePeriodRequired) {
		this.timePeriodRequired = timePeriodRequired;
	}
	public Boolean getFromMonthToMontRequired() {
		return fromMonthToMontRequired;
	}
	public void setFromMonthToMontRequired(Boolean fromMonthToMontRequired) {
		this.fromMonthToMontRequired = fromMonthToMontRequired;
	}
	
	@Override
	public String toString() {
		return "AchievementType [id=" + id + ", achievementType=" + achievementType + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate
				+ ", status=" + status + ", order=" + order + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((achievementType == null) ? 0 : achievementType.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
		result = prime * result + ((updatedDate == null) ? 0 : updatedDate.hashCode());
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
		AchievementType other = (AchievementType) obj;
		if (achievementType == null) {
			if (other.achievementType != null)
				return false;
		} else if (!achievementType.equals(other.achievementType))
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
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
	
}
