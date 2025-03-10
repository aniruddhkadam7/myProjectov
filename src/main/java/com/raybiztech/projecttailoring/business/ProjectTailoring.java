package com.raybiztech.projecttailoring.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.Project;

public class ProjectTailoring implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2150741247606776641L;
	
	private Long id;
	private Project project;
	private Employee createdBy;
	private Second createdDate;
	private Long updatedBy;
	private Second updatedDate;
	private Set<ProcessHeadData> processHeadData;
	private String tailoringStatus;
	private String rejectComments;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Set<ProcessHeadData> getProcessHeadData() {
		return processHeadData;
	}
	public void setProcessHeadData(Set<ProcessHeadData> processHeadData) {
		this.processHeadData = processHeadData;
	}
	public String getTailoringStatus() {
		return tailoringStatus;
	}
	public void setTailoringStatus(String tailoringStatus) {
		this.tailoringStatus = tailoringStatus;
	}
	public String getRejectComments() {
		return rejectComments;
	}
	public void setRejectComments(String rejectComments) {
		this.rejectComments = rejectComments;
	}

	

}
