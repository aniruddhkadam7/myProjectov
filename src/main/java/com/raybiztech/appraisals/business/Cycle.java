package com.raybiztech.appraisals.business;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Cycle implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long cycleId;
	private String name;
	private String description;
	private String fromDate;
	private String toDate;
	private Integer percentage_Done;
	private String status;
	private Set<Appraisal> appraisals = new HashSet<Appraisal>();

	public Cycle() {

	}

	public Long getCycleId() {
		return cycleId;
	}



	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
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

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public Integer getPercentage_Done() {
		return percentage_Done;
	}

	public void setPercentage_Done(Integer percentage_Done) {
		this.percentage_Done = percentage_Done;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<Appraisal> getAppraisals() {
		return appraisals;
	}

	public void setAppraisals(Set<Appraisal> appraisals) {
		this.appraisals = appraisals;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1977, 53).append(cycleId).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Cycle other = (Cycle) obj;
		return new EqualsBuilder().append(cycleId, other.cycleId).isEquals();
	}

	@Override
	public String toString() {
		return "name :" + name + " description: " + description
				+ " from date: " + fromDate + " to date: " + toDate
				+ " percentage done: " + percentage_Done + " Status: " + status;
	}

}
