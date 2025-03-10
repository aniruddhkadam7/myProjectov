/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.dto;

import java.io.Serializable;
import java.util.List;

import com.raybiztech.date.Date;

/**
 *
 * @author hari
 */
public class CandidateInterviewTimelineDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long personId;
	private String firstName;
	private String fullName;
	private String lastName;
	private String middleName;
	private String email;
	private String qualification;
	private String skill;
	private Integer pendingInterviewStatus;
	private String appliedFor;
	private String experience;
	private String candidateStatus;
	private String resumePath;
	private List<CandidateInterviewCycleDTO> cycleDTOs;
	private String statusComments;
	private String timelineStatus;
	private String joineeComments;
	private String modeOfInterview;
	private String recruiter;
	private String otherDocumentPath;
	private String initialComments;
	private String holdSubStatus;
	private String addedDate;
	private String reason;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getInitialComments() {
		return initialComments;
	}

	public void setInitialComments(String initialComments) {
		this.initialComments = initialComments;
	}

	public String getCandidateStatus() {
		return candidateStatus;
	}

	public void setCandidateStatus(String candidateStatus) {
		this.candidateStatus = candidateStatus;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public List<CandidateInterviewCycleDTO> getCycleDTOs() {
		return cycleDTOs;
	}

	public void setCycleDTOs(List<CandidateInterviewCycleDTO> cycleDTOs) {
		this.cycleDTOs = cycleDTOs;
	}

	public Integer getPendingInterviewStatus() {
		return pendingInterviewStatus;
	}

	public void setPendingInterviewStatus(Integer pendingInterviewStatus) {
		this.pendingInterviewStatus = pendingInterviewStatus;
	}

	public String getAppliedFor() {
		return appliedFor;
	}

	public void setAppliedFor(String appliedFor) {
		this.appliedFor = appliedFor;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getResumePath() {
		return resumePath;
	}

	public void setResumePath(String resumePath) {
		this.resumePath = resumePath;
	}

	public String getStatusComments() {
		return statusComments;
	}

	public void setStatusComments(String statusComments) {
		this.statusComments = statusComments;
	}

	public String getTimelineStatus() {
		return timelineStatus;
	}

	public void setTimelineStatus(String timelineStatus) {
		this.timelineStatus = timelineStatus;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getJoineeComments() {
		return joineeComments;
	}

	public void setJoineeComments(String joineeComments) {
		this.joineeComments = joineeComments;
	}

	public String getModeOfInterview() {
		return modeOfInterview;
	}

	public void setModeOfInterview(String modeOfInterview) {
		this.modeOfInterview = modeOfInterview;
	}

	public String getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(String recruiter) {
		this.recruiter = recruiter;
	}

	public String getOtherDocumentPath() {
		return otherDocumentPath;
	}

	public void setOtherDocumentPath(String otherDocumentPath) {
		this.otherDocumentPath = otherDocumentPath;
	}

	public String getHoldSubStatus() {
		return holdSubStatus;
	}

	public void setHoldSubStatus(String holdSubStatus) {
		this.holdSubStatus = holdSubStatus;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

}
