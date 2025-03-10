package com.raybiztech.recruitment.dto;

import java.io.Serializable;

public class NewJoineeDTO implements Serializable {

    private static final long serialVersionUID = -9027238927677469585L;
    private Long id;
    private String appliedForLookUp;
    private String candidateName;
    private String designation;
    private String dateOfJoining;
    private String currentCTC;
    private String employmentType;
    private String jobType;
    private String status;
    private String comments;
    private String attachedDocumentPath;
    private String experience;
    private String candidateEmail;
    private String dateOfBirth;
    private Long candidateId;
    private String technology;
    private String candidateInterviewStatus;
    private String departmentName;
    private String mobile;
    private Boolean sendOfferMessagetoCandidate;
    
    
    public Long getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppliedForLookUp() {
        return appliedForLookUp;
    }

    public void setAppliedForLookUp(String appliedForLookUp) {
        this.appliedForLookUp = appliedForLookUp;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(String dateOfJoining) {
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

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getCandidateEmail() {
        return candidateEmail;
    }

    public void setCandidateEmail(String candidateEmail) {
        this.candidateEmail = candidateEmail;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Boolean getSendOfferMessagetoCandidate() {
		return sendOfferMessagetoCandidate;
	}

	public void setSendOfferMessagetoCandidate(
			Boolean sendOfferMessagetoCandidate) {
		this.sendOfferMessagetoCandidate = sendOfferMessagetoCandidate;
	}
    
        
}
