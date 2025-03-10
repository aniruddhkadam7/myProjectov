/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.dto;

import java.io.Serializable;
import java.util.List;

import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

/**
 *
 * @author hari
 */
public class CandidateScheduleDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String candidateName;
	private String candidateFirstName;
	private String candidateLastName;
	private String candidateEmail;
	private String experience;
	private String skills;
	private String mobile;
	private JobVacancyDTO appliedFor;
	private String sourceType;
	private String sourceName;
	private String uploadResume;
	private String otherDocs;
	private String status;
	private Long candidateId;
	private String appliedForLookUp;
	private Long vacancyId;
	// Schedule Related
	private String scheduleDate;
	private String scheduleTime;
	private String interviewerEmailid;
	private String receipentEmailId;
	private String description;
	private String scheduleFlag;
	private String interviewRound;
	private List<EmployeeDTO> interviewersDTOList;
	private Long interviewerId;
	private String interviewType;
	private String interviewStatus;
	private String dob;
	private String timelineStatus;
	private String technology;
	private String recruiter;
	private String ctc;
	private String ectc;
	private String np;
	private String reason;
	private String skypeId;
	private String currentEmployer;
	private String currentLocation;
	private Boolean sendMailToCandidate;
	private Boolean sendMailToInterviewer;
	private Boolean sendMessageToCandidate;
	private Boolean sendMessageToInterviewer;
	private String jobTypeName;
	private String pan;
	private String adhar;
	private String linkedin;
	private Integer countryId;
	private CountryLookUp country;
	private String notifications;
	private String contactDetails;
	private Integer countryCode;
	
	
	
	
    public Boolean getSendMessageToCandidate() {
		return sendMessageToCandidate;
	}

	public void setSendMessageToCandidate(Boolean sendMessageToCandidate) {
		this.sendMessageToCandidate = sendMessageToCandidate;
	}

	public Boolean getSendMessageToInterviewer() {
		return sendMessageToInterviewer;
	}

	public void setSendMessageToInterviewer(Boolean sendMessageToInterviewer) {
		this.sendMessageToInterviewer = sendMessageToInterviewer;
	}

	public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

	public String getNotifications() {
		return notifications;
	}

	public void setNotifications(String notifications) {
		this.notifications = notifications;
	}

	public CountryLookUp getCountry() {
		return country;
	}

	public void setCountry(CountryLookUp country) {
		this.country = country;
	}

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getCurrentEmployer() {
		return currentEmployer;
	}

	public void setCurrentEmployer(String currentEmployer) {
		this.currentEmployer = currentEmployer;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getCtc() {
		return ctc;
	}

	public void setCtc(String ctc) {
		this.ctc = ctc;
	}

	public String getEctc() {
		return ectc;
	}

	public void setEctc(String ectc) {
		this.ectc = ectc;
	}

	public String getNp() {
		return np;
	}

	public void setNp(String np) {
		this.np = np;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public Long getVacancyId() {
		return vacancyId;
	}

	public void setVacancyId(Long vacancyId) {
		this.vacancyId = vacancyId;
	}

	public Long getInterviewerId() {
		return interviewerId;
	}

	public void setInterviewerId(Long interviewerId) {
		this.interviewerId = interviewerId;
	}

	public String getInterviewStatus() {
		return interviewStatus;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setInterviewStatus(String interviewStatus) {
		this.interviewStatus = interviewStatus;
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public String getCandidateEmail() {
		return candidateEmail;
	}

	public void setCandidateEmail(String candidateEmail) {
		this.candidateEmail = candidateEmail;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public JobVacancyDTO getAppliedFor() {
		return appliedFor;
	}

	public void setAppliedFor(JobVacancyDTO appliedFor) {
		this.appliedFor = appliedFor;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getUploadResume() {
		return uploadResume;
	}

	public void setUploadResume(String uploadResume) {
		this.uploadResume = uploadResume;
	}

	public String getOtherDocs() {
		return otherDocs;
	}

	public void setOtherDocs(String otherDocs) {
		this.otherDocs = otherDocs;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInterviewerEmailid() {
		return interviewerEmailid;
	}

	public void setInterviewerEmailid(String interviewerEmailid) {
		this.interviewerEmailid = interviewerEmailid;
	}

	public String getReceipentEmailId() {
		return receipentEmailId;
	}

	public void setReceipentEmailId(String receipentEmailId) {
		this.receipentEmailId = receipentEmailId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(String scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

	public String getScheduleFlag() {
		return scheduleFlag;
	}

	public void setScheduleFlag(String scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
	}

	public String getInterviewRound() {
		return interviewRound;
	}

	public void setInterviewRound(String interviewRound) {
		this.interviewRound = interviewRound;
	}

	public List<EmployeeDTO> getInterviewersDTOList() {
		return interviewersDTOList;
	}

	public void setInterviewersDTOList(List<EmployeeDTO> interviewersDTOList) {
		this.interviewersDTOList = interviewersDTOList;
	}

	public String getInterviewType() {
		return interviewType;
	}

	public void setInterviewType(String interviewType) {
		this.interviewType = interviewType;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public String getAppliedForLookUp() {
		return appliedForLookUp;
	}

	public void setAppliedForLookUp(String appliedForLookUp) {
		this.appliedForLookUp = appliedForLookUp;
	}

	public String getTimelineStatus() {
		return timelineStatus;
	}

	public void setTimelineStatus(String timelineStatus) {
		this.timelineStatus = timelineStatus;
	}

	public String getCandidateFirstName() {
		return candidateFirstName;
	}

	public void setCandidateFirstName(String candidateFirstName) {
		this.candidateFirstName = candidateFirstName;
	}

	public String getCandidateLastName() {
		return candidateLastName;
	}

	public void setCandidateLastName(String candidateLastName) {
		this.candidateLastName = candidateLastName;
	}

	public String getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(String recruiter) {
		this.recruiter = recruiter;
	}

	public Boolean getSendMailToCandidate() {
		return sendMailToCandidate;
	}

	public void setSendMailToCandidate(Boolean sendMailToCandidate) {
		this.sendMailToCandidate = sendMailToCandidate;
	}

	public Boolean getSendMailToInterviewer() {
		return sendMailToInterviewer;
	}

	public void setSendMailToInterviewer(Boolean sendMailToInterviewer) {
		this.sendMailToInterviewer = sendMailToInterviewer;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getAdhar() {
		return adhar;
	}

	public void setAdhar(String adhar) {
		this.adhar = adhar;
	}

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	public Integer getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Integer countryCode) {
		this.countryCode = countryCode;
	}

	
	
}
