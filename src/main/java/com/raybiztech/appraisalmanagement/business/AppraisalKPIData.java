package com.raybiztech.appraisalmanagement.business;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class AppraisalKPIData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3738068578486534317L;
	private Long id;
	private String name;
	private String description;
	private String feedback;
	private Integer rating;
	private String frequency;
	private String target;
	// private String managerFeedback;
	// private Integer managerRating;
	private AppraisalKRAData appraisalKRAData;
	private Long empId;
	private Integer level;

	private Set<AppraisalKPIData> managersFeedback;

	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public AppraisalKRAData getAppraisalKRAData() {
		return appraisalKRAData;
	}

	public void setAppraisalKRAData(AppraisalKRAData appraisalKRAData) {
		this.appraisalKRAData = appraisalKRAData;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Set<AppraisalKPIData> getManagersFeedback() {
		return managersFeedback;
	}

	public void setManagersFeedback(Set<AppraisalKPIData> managersFeedback) {
		this.managersFeedback = managersFeedback;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 97 * hash + Objects.hashCode(this.id);
		hash = 97 * hash + Objects.hashCode(this.name);
		hash = 97 * hash + Objects.hashCode(this.description);
		hash = 97 * hash + Objects.hashCode(this.feedback);
		hash = 97 * hash + Objects.hashCode(this.rating);
		hash = 97 * hash + Objects.hashCode(this.appraisalKRAData);
		hash = 97 * hash + Objects.hashCode(this.empId);
		hash = 97 * hash + Objects.hashCode(this.level);
		hash = 97 * hash + Objects.hashCode(this.managersFeedback);
		hash = 97 * hash + Objects.hashCode(this.status);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AppraisalKPIData other = (AppraisalKPIData) obj;
		if (!Objects.equals(this.name, other.name)) {
			return false;
		}
		if (!Objects.equals(this.description, other.description)) {
			return false;
		}
		if (!Objects.equals(this.feedback, other.feedback)) {
			return false;
		}
		if (!Objects.equals(this.status, other.status)) {
			return false;
		}
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (!Objects.equals(this.rating, other.rating)) {
			return false;
		}
		if (!Objects.equals(this.appraisalKRAData, other.appraisalKRAData)) {
			return false;
		}
		if (!Objects.equals(this.empId, other.empId)) {
			return false;
		}
		if (!Objects.equals(this.level, other.level)) {
			return false;
		}
		if (!Objects.equals(this.managersFeedback, other.managersFeedback)) {
			return false;
		}
		return true;
	}

	// @Override
	// public int hashCode() {
	// return new HashCodeBuilder(1989, 77).append(name).hashCode();
	// }
	//
	// @Override
	// public boolean equals(Object obj) {
	// if (obj == null) {
	// return false;
	// }
	// if (getClass() != obj.getClass()) {
	// return false;
	// }
	// final AppraisalKPIData other = (AppraisalKPIData) obj;
	// return new EqualsBuilder().append(name, other.getName()).isEquals();
	// }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
