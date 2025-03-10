/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.builder;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Status;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.Interview;
import com.raybiztech.recruitment.business.InterviewType;
import com.raybiztech.recruitment.dto.CandidateDTO;
import com.raybiztech.recruitment.dto.InterviewDTO;

/**
 *
 * @author hari
 */
@Component("interviewBuilder")
public class InterviewBuilder {

    @Autowired
    CandidateBuilder candidateBuilder;
    @Autowired
    EmployeeBuilder employeeBuilder;

    public CandidateBuilder getCandidateBuilder() {
        return candidateBuilder;
    }

    public void setCandidateBuilder(CandidateBuilder candidateBuilder) {
        this.candidateBuilder = candidateBuilder;
    }

    public EmployeeBuilder getEmployeeBuilder() {
        return employeeBuilder;
    }

    public void setEmployeeBuilder(EmployeeBuilder employeeBuilder) {
        this.employeeBuilder = employeeBuilder;
    }
  
    public Interview createInterviewEntity(InterviewDTO interviewDTO) {
        Interview interview = null;
        if (interviewDTO != null) {
            interview = new Interview();
            interview.setCandidates((Set<Candidate>) candidateBuilder.createCandidateEntityList((List<CandidateDTO>) interviewDTO.getCandidates()));
            interview.setComments(interviewDTO.getComments());
            interview.setInterviewId(interviewDTO.getId());
            interview.setInterviewType(InterviewType.valueOf(interviewDTO.getInterviewType()));
            interview.setInterviewers((Set<Employee>) employeeBuilder.getemployeeEntityList((List<EmployeeDTO>) interviewDTO.getInterviewers()));
            interview.setResult(interviewDTO.getResult());
            interview.setRound(interviewDTO.getRound());
            interview.setStatus(Status.valueOf(interviewDTO.getStatus()));

        }
        return interview;
    }

    public InterviewDTO createInterviewDTO(Interview interview) {
        InterviewDTO interviewDTO = null;
        if (interview != null) {
            interviewDTO = new InterviewDTO();
            interviewDTO.setCandidates((Set<CandidateDTO>) candidateBuilder.createCandidateDTOList((List<Candidate>) interview.getCandidates()));
            interviewDTO.setComments(interview.getComments());
            interviewDTO.setId(interview.getInterviewId());
            interviewDTO.setInterviewType(interview.getInterviewType().toString());
            interviewDTO.setInterviewers((Set<EmployeeDTO>) employeeBuilder.getemployeeDTOList((List<Employee>) interview.getInterviewers()));
            interviewDTO.setResult(interview.getResult());
            interviewDTO.setRound(interview.getRound());
            interviewDTO.setStatus(interview.getStatus().toString());

        }
        return interviewDTO;
    }
}
