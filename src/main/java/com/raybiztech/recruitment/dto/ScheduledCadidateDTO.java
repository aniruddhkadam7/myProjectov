/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.dto;

import java.io.Serializable;
import java.util.List;

import com.raybiztech.appraisals.dto.EmployeeDTO;

/**
 *
 * @author naresh
 */
public class ScheduledCadidateDTO implements Serializable {

    private Long scheduleid;
    private Long candidateid;

    private String time;

    private String candidateName;

    private String candidateEmail;

    private String candidateMobile;

    private String candidateskill;

    private String interviewer;

    private String interviewStatus;

    private String interviewDate;

    private List<EmployeeDTO> empoyeeData;

    private String round;
    private String interviewType;
    private String comments;
    private String candidateInterviewStatus;
    private String candidateJoinDate;
    private String appliedForVacancy;
    private String experience;
    private String interviewTime;
    private String interviewcycleId;

    public String getInterviewcycleId() {
        return interviewcycleId;
    }

    public void setInterviewcycleId(String interviewcycleId) {
        this.interviewcycleId = interviewcycleId;
    }

    public String getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(String interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAppliedForVacancy() {
        return appliedForVacancy;
    }

    public void setAppliedForVacancy(String appliedForVacancy) {
        this.appliedForVacancy = appliedForVacancy;
    }

    public String getCandidateInterviewStatus() {
        return candidateInterviewStatus;
    }

    public void setCandidateInterviewStatus(String candidateInterviewStatus) {
        this.candidateInterviewStatus = candidateInterviewStatus;
    }

    public String getCandidateJoinDate() {
        return candidateJoinDate;
    }

    public void setCandidateJoinDate(String candidateJoinDate) {
        this.candidateJoinDate = candidateJoinDate;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getInterviewType() {
        return interviewType;
    }

    public void setInterviewType(String interviewType) {
        this.interviewType = interviewType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getScheduleid() {
        return scheduleid;
    }

    public void setScheduleid(Long scheduleid) {
        this.scheduleid = scheduleid;
    }

    public Long getCandidateid() {
        return candidateid;
    }

    public void setCandidateid(Long candidateid) {
        this.candidateid = candidateid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getCandidateMobile() {
        return candidateMobile;
    }

    public void setCandidateMobile(String candidateMobile) {
        this.candidateMobile = candidateMobile;
    }

    public String getCandidateskill() {
        return candidateskill;
    }

    public void setCandidateskill(String candidateskill) {
        this.candidateskill = candidateskill;
    }

    public String getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(String interviewer) {
        this.interviewer = interviewer;
    }

    public String getInterviewStatus() {
        return interviewStatus;
    }

    public void setInterviewStatus(String interviewStatus) {
        this.interviewStatus = interviewStatus;
    }

    public String getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(String interviewDate) {
        this.interviewDate = interviewDate;
    }

    public List<EmployeeDTO> getEmpoyeeData() {
        return empoyeeData;
    }

    public void setEmpoyeeData(List<EmployeeDTO> empoyeeData) {
        this.empoyeeData = empoyeeData;
    }

}
