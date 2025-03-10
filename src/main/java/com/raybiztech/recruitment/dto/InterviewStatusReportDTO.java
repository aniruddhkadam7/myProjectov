/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.dto;

import java.io.Serializable;

/**
 *
 * @author naresh
 */
public class InterviewStatusReportDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String interviewDate;
	private String candidateName;
	private String contactNumber;
	private String emailId;
	private String technology;
	private String experiance;
	private String status;
	private Long candidateId;
	private String timeLineStatus;
	private String recruiter;
	private String source; 
	private String interviewResultStatus;
	private String interviewerName;
	private Long interviewRound;
	
	

	public String getInterviewerName() {
		return interviewerName;
	}

	public void setInterviewerName(String interviewerName) {
		this.interviewerName = interviewerName;
	}

	public String getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(String recruiter) {
		this.recruiter = recruiter;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public String getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(String interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getExperiance() {
		return experiance;
	}

	public void setExperiance(String experiance) {
		this.experiance = experiance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTimeLineStatus() {
		return timeLineStatus;
	}

	public void setTimeLineStatus(String timeLineStatus) {
		this.timeLineStatus = timeLineStatus;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getInterviewResultStatus() {
		return interviewResultStatus;
	}

	public void setInterviewResultStatus(String interviewResultStatus) {
		this.interviewResultStatus = interviewResultStatus;
	}

	public Long getInterviewRound() {
		return interviewRound;
	}

	public void setInterviewRound(Long interviewRound) {
		this.interviewRound = interviewRound;
	}

	
	
	
}
