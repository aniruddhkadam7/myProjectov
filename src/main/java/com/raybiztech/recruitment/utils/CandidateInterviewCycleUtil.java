/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.recruitment.dto.CandidateScheduleDto;
import com.raybiztech.recruitment.service.JobPortalServiceImpl;

/**
 *
 * @author hari
 */
@Component("candidateInterviewCycleUtil")
public class CandidateInterviewCycleUtil {

    @Autowired
    DAO dao;

    public CandidateInterviewCycle generateCandidateInterviewCycle(Candidate candidate, CandidateScheduleDto candidateScheduleDto) {
        CandidateInterviewCycle interviewCycle = new CandidateInterviewCycle();
        interviewCycle.setCandidate(candidate);
        try {
            interviewCycle.setInterviewDate(DateParser
                    .toDate(candidateScheduleDto.getScheduleDate()));
        } catch (ParseException ex) {
            Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        interviewCycle.setInterviewRound(Long.valueOf(candidateScheduleDto
                .getInterviewRound()));
        interviewCycle.setInterviewStatus(candidateScheduleDto
                .getInterviewStatus());
        interviewCycle.setInterviewTime(candidateScheduleDto
                .getScheduleTime());
        interviewCycle.setStatus("pending");

        //String interviewersNames = "";
//        for (EmployeeDTO employeeDTO : candidateScheduleDto
//                .getInterviewersDTOList()) {
//            interviewersList.add(dao.findBy(Employee.class,
//                    employeeDTO.getId()));
//            interviewersNames += employeeDTO.getFirstName() + " "
//                    + employeeDTO.getLastName() + ",";
//        }
        Set<Employee> interviewersList = new HashSet<Employee>();
        Employee interviewer = dao.findBy(Employee.class, candidateScheduleDto.getInterviewerId());
        interviewersList.add(interviewer);
        interviewCycle.setEmployeeList(interviewersList);
        interviewCycle.setInterviewers(interviewer.getFullName());
        return interviewCycle;
    }

    public List<CandidateInterviewCycle> generateCandidateInterviewCycleForReschdule(CandidateScheduleDto candidateScheduleDto, List<CandidateInterviewCycle> cycles) {
        List<CandidateInterviewCycle> candidateInterviewCyclesList = null;
        for (CandidateInterviewCycle candidateInterviewCycle : cycles) {
            candidateInterviewCyclesList = new ArrayList<CandidateInterviewCycle>();
            try {
                candidateInterviewCycle.setInterviewDate(DateParser.toDate(candidateScheduleDto.getScheduleDate()));
            } catch (ParseException ex) {
                Logger.getLogger(CandidateInterviewCycleUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            candidateInterviewCycle.setInterviewTime(candidateScheduleDto.getScheduleTime());

            Set<Employee> interviewersList = new HashSet<Employee>();
            Employee interviewer = dao.findBy(Employee.class, candidateScheduleDto.getInterviewerId());
            interviewersList.add(interviewer);
            candidateInterviewCycle.setEmployeeList(interviewersList);
            candidateInterviewCycle.setInterviewers(interviewer.getFullName());

            candidateInterviewCycle.setDescription(candidateScheduleDto.getDescription());
            candidateInterviewCyclesList.add(candidateInterviewCycle);
        }
        return candidateInterviewCyclesList;
    }

}
