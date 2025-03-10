/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.dto;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.appraisals.dto.EmployeeDTO;

/**
 *
 * @author naresh
 */
public class InterviewDTO implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 421188953947961583L;

    private Long id;

    private Set<EmployeeDTO> interviewers;

    private Set<CandidateDTO> candidates;

    private String result;

    private String comments;

    private String status;

    private String round;

    private String interviewType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<EmployeeDTO> getInterviewers() {
        return interviewers;
    }

    public void setInterviewers(Set<EmployeeDTO> interviewers) {
        this.interviewers = interviewers;
    }

    public Set<CandidateDTO> getCandidates() {
        return candidates;
    }

    public void setCandidates(Set<CandidateDTO> candidates) {
        this.candidates = candidates;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

}
