package com.raybiztech.recruitment.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.date.Date;

public class NewJoinee implements Serializable {

    private static final long serialVersionUID = 1938911130367838921L;

    private Long id;
    private String position;
    private String joineeName;
    private String designation;

    private Date dateOfJoining;
    private Date dateOfBirth;
    private String currentCTC;
    private String employmentType;
    private String jobType;
    private String status;
    private String comments;
    private String attachedDocumentPath;

    private String experience;

    private String email;
    private Long candidateId;
    private String technology;
    private String candidateInterviewStatus;
    private String departmentName;

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getJoineeName() {
        return joineeName;
    }

    public void setJoineeName(String joineeName) {
        this.joineeName = joineeName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Date getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(Date dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getCurrentCTC() {
        return currentCTC;
    }

    public void setCurrentCTC(String currentCTC) {
        this.currentCTC = currentCTC;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAttachedDocumentPath() {
        return attachedDocumentPath;
    }

    public void setAttachedDocumentPath(String attachedDocumentPath) {
        this.attachedDocumentPath = attachedDocumentPath;
    }

    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }
    

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1989, 55).append(id).hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof NewJoinee) {
            final NewJoinee other = (NewJoinee) obj;
            return new EqualsBuilder().append(id, other.getId())
                    .isEquals();
        } else {
            return false;
        }
    }

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getCandidateInterviewStatus() {
		return candidateInterviewStatus;
	}

	public void setCandidateInterviewStatus(String candidateInterviewStatus) {
		this.candidateInterviewStatus = candidateInterviewStatus;
	}

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

        
}
