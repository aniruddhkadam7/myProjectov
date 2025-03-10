package com.raybiztech.SQAAudit.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.commons.Percentage;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.Project;

public class SQAAuditForm implements Serializable,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 170660924072970799L;
	
	private Long id;
	public String auditType;
	public Boolean projectType;
	public Project project;
	public Set<SQAAuditors> auditors;
	public Set<SQAAuditees> auditees;
	public Date auditDate;
	public Second startTime;
	public Second endTime;
	public String auditStatus;
	public String formStatus;
	public Boolean auditRescheduleStatus;
	public String pci;
	public Date followUpDate;
	public String sqaComments;
	public String sqaFilesPath;
	public String pmComments;
	public String pmFilesPath;
	public Employee createdBy;
	public Date createdDate;
	public Employee updatedBy;
	public Date updatedDate;
	public String projectName;
	public Employee projectManager;
	public Boolean containsFile;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAuditType() {
		return auditType;
	}
	public void setAuditType(String auditType) {
		this.auditType = auditType;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Set<SQAAuditors> getAuditors() {
		return auditors;
	}
	public void setAuditors(Set<SQAAuditors> auditors) {
		this.auditors = auditors;
	}
	public Set<SQAAuditees> getAuditees() {
		return auditees;
	}
	public void setAuditees(Set<SQAAuditees> auditees) {
		this.auditees = auditees;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public Second getStartTime() {
		return startTime;
	}
	public void setStartTime(Second startTime) {
		this.startTime = startTime;
	}
	public Second getEndTime() {
		return endTime;
	}
	public void setEndTime(Second endTime) {
		this.endTime = endTime;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getFormStatus() {
		return formStatus;
	}
	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}
	public Boolean getAuditRescheduleStatus() {
		return auditRescheduleStatus;
	}
	public void setAuditRescheduleStatus(Boolean auditRescheduleStatus) {
		this.auditRescheduleStatus = auditRescheduleStatus;
	}
	public String getPci() {
		return pci;
	}
	public void setPci(String pci) {
		this.pci = pci;
	}
	public Date getFollowUpDate() {
		return followUpDate;
	}
	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}
	public String getSqaComments() {
		return sqaComments;
	}
	public void setSqaComments(String sqaComments) {
		this.sqaComments = sqaComments;
	}
	public String getSqaFilesPath() {
		return sqaFilesPath;
	}
	public void setSqaFilesPath(String sqaFilesPath) {
		this.sqaFilesPath = sqaFilesPath;
	}
	public String getPmComments() {
		return pmComments;
	}
	public void setPmComments(String pmComments) {
		this.pmComments = pmComments;
	}
	public String getPmFilesPath() {
		return pmFilesPath;
	}
	public void setPmFilesPath(String pmFilesPath) {
		this.pmFilesPath = pmFilesPath;
	}
	public Employee getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Employee createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Employee getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Employee updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public Employee getProjectManager() {
		return projectManager;
	}
	public void setProjectManager(Employee projectManager) {
		this.projectManager = projectManager;
	}
	public Boolean getContainsFile() {
		return containsFile;
	}
	public void setContainsFile(Boolean containsFile) {
		this.containsFile = containsFile;
	}
	
	public Boolean getProjectType() {
		return projectType;
	}
	public void setProjectType(Boolean projectType) {
		this.projectType = projectType;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((auditDate == null) ? 0 : auditDate.hashCode());
		result = prime
				* result
				+ ((auditRescheduleStatus == null) ? 0 : auditRescheduleStatus
						.hashCode());
		result = prime * result
				+ ((auditStatus == null) ? 0 : auditStatus.hashCode());
		result = prime * result
				+ ((auditType == null) ? 0 : auditType.hashCode());
		result = prime * result
				+ ((auditees == null) ? 0 : auditees.hashCode());
		result = prime * result
				+ ((auditors == null) ? 0 : auditors.hashCode());
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result
				+ ((followUpDate == null) ? 0 : followUpDate.hashCode());
		result = prime * result
				+ ((formStatus == null) ? 0 : formStatus.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((pci == null) ? 0 : pci.hashCode());
		result = prime * result
				+ ((pmComments == null) ? 0 : pmComments.hashCode());
		result = prime * result
				+ ((pmFilesPath == null) ? 0 : pmFilesPath.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result
				+ ((projectManager == null) ? 0 : projectManager.hashCode());
		result = prime * result
				+ ((projectName == null) ? 0 : projectName.hashCode());
		result = prime * result
				+ ((sqaComments == null) ? 0 : sqaComments.hashCode());
		result = prime * result
				+ ((sqaFilesPath == null) ? 0 : sqaFilesPath.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
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
		SQAAuditForm other = (SQAAuditForm) obj;
		if (auditDate == null) {
			if (other.auditDate != null)
				return false;
		} else if (!auditDate.equals(other.auditDate))
			return false;
		if (auditRescheduleStatus == null) {
			if (other.auditRescheduleStatus != null)
				return false;
		} else if (!auditRescheduleStatus.equals(other.auditRescheduleStatus))
			return false;
		if (auditStatus == null) {
			if (other.auditStatus != null)
				return false;
		} else if (!auditStatus.equals(other.auditStatus))
			return false;
		if (auditType == null) {
			if (other.auditType != null)
				return false;
		} else if (!auditType.equals(other.auditType))
			return false;
		if (auditees == null) {
			if (other.auditees != null)
				return false;
		} else if (!auditees.equals(other.auditees))
			return false;
		if (auditors == null) {
			if (other.auditors != null)
				return false;
		} else if (!auditors.equals(other.auditors))
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
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (followUpDate == null) {
			if (other.followUpDate != null)
				return false;
		} else if (!followUpDate.equals(other.followUpDate))
			return false;
		if (formStatus == null) {
			if (other.formStatus != null)
				return false;
		} else if (!formStatus.equals(other.formStatus))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pci == null) {
			if (other.pci != null)
				return false;
		} else if (!pci.equals(other.pci))
			return false;
		if (pmComments == null) {
			if (other.pmComments != null)
				return false;
		} else if (!pmComments.equals(other.pmComments))
			return false;
		if (pmFilesPath == null) {
			if (other.pmFilesPath != null)
				return false;
		} else if (!pmFilesPath.equals(other.pmFilesPath))
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (projectManager == null) {
			if (other.projectManager != null)
				return false;
		} else if (!projectManager.equals(other.projectManager))
			return false;
		if (projectName == null) {
			if (other.projectName != null)
				return false;
		} else if (!projectName.equals(other.projectName))
			return false;
		if (sqaComments == null) {
			if (other.sqaComments != null)
				return false;
		} else if (!sqaComments.equals(other.sqaComments))
			return false;
		if (sqaFilesPath == null) {
			if (other.sqaFilesPath != null)
				return false;
		} else if (!sqaFilesPath.equals(other.sqaFilesPath))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
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
		StringBuilder builder = new StringBuilder();
		builder.append("SQAAuditForm [id=").append(id).append(", auditType=")
				.append(auditType).append(", project=").append(project)
				.append(", auditors=").append(auditors).append(", auditees=")
				.append(auditees).append(", auditDate=").append(auditDate)
				.append(", startTime=").append(startTime).append(", endTime=")
				.append(endTime).append(", auditStatus=").append(auditStatus)
				.append(", formStatus=").append(formStatus)
				.append(", auditRescheduleStatus=")
				.append(auditRescheduleStatus).append(", pci=").append(pci)
				.append(", followUpDate=").append(followUpDate)
				.append(", sqaComments=").append(sqaComments)
				.append(", sqaFilesPath=").append(sqaFilesPath)
				.append(", pmComments=").append(pmComments)
				.append(", pmFilesPath=").append(pmFilesPath)
				.append(", createdBy=").append(createdBy)
				.append(", createdDate=").append(createdDate)
				.append(", updatedBy=").append(updatedBy)
				.append(", updatedDate=").append(updatedDate)
				.append(", projectName=").append(projectName)
				.append(", projectManager=").append(projectManager).append("]");
		return builder.toString();
	}
	
	
	
	
	

	
	
	
}
