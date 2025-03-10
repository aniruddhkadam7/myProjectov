package com.raybiztech.projectMetrics.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.Project;

public class ScheduleVariance implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1768102829322727101L;

	private Long id;
	private Project project;
	private Date baselineStartdate;
	private Date baselineEnddate;
	private Date actualStartdate;
	private Date actualEnddate;
	private Double scheduleVariance;
	private String comments;
	private Employee createdBy;
	private Second createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBaselineStartdate() {
		return baselineStartdate;
	}

	public void setBaselineStartdate(Date baselineStartdate) {
		this.baselineStartdate = baselineStartdate;
	}

	public Date getBaselineEnddate() {
		return baselineEnddate;
	}

	public void setBaselineEnddate(Date baselineEnddate) {
		this.baselineEnddate = baselineEnddate;
	}

	public Date getActualStartdate() {
		return actualStartdate;
	}

	public void setActualStartdate(Date actualStartdate) {
		this.actualStartdate = actualStartdate;
	}

	public Date getActualEnddate() {
		return actualEnddate;
	}

	public void setActualEnddate(Date actualEnddate) {
		this.actualEnddate = actualEnddate;
	}

	public Double getScheduleVariance() {
		return scheduleVariance;
	}

	public void setScheduleVariance(Double scheduleVariance) {
		this.scheduleVariance = scheduleVariance;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Employee getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Employee createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualEnddate == null) ? 0 : actualEnddate.hashCode());
		result = prime * result
				+ ((actualStartdate == null) ? 0 : actualStartdate.hashCode());
		result = prime * result
				+ ((baselineEnddate == null) ? 0 : baselineEnddate.hashCode());
		result = prime
				* result
				+ ((baselineStartdate == null) ? 0 : baselineStartdate
						.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime
				* result
				+ ((scheduleVariance == null) ? 0 : scheduleVariance.hashCode());
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
		ScheduleVariance other = (ScheduleVariance) obj;
		if (actualEnddate == null) {
			if (other.actualEnddate != null)
				return false;
		} else if (!actualEnddate.equals(other.actualEnddate))
			return false;
		if (actualStartdate == null) {
			if (other.actualStartdate != null)
				return false;
		} else if (!actualStartdate.equals(other.actualStartdate))
			return false;
		if (baselineEnddate == null) {
			if (other.baselineEnddate != null)
				return false;
		} else if (!baselineEnddate.equals(other.baselineEnddate))
			return false;
		if (baselineStartdate == null) {
			if (other.baselineStartdate != null)
				return false;
		} else if (!baselineStartdate.equals(other.baselineStartdate))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
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
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (scheduleVariance == null) {
			if (other.scheduleVariance != null)
				return false;
		} else if (!scheduleVariance.equals(other.scheduleVariance))
			return false;
		return true;
	}

}
