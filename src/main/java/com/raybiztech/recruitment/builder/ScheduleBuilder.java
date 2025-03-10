/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Status;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.Interview;
import com.raybiztech.recruitment.business.InterviewType;
import com.raybiztech.recruitment.business.Schedule;
import com.raybiztech.recruitment.dto.ScheduleDTO;
import com.raybiztech.recruitment.dto.ScheduledCadidateDTO;
import com.raybiztech.recruitment.utils.DateParser;

/**
 *
 * @author hari
 */
@Component("scheduleBuilder")
public class ScheduleBuilder {

    @Autowired
    InterviewBuilder interviewBuilder;
    @Autowired
    AddressBuilder addressBuilder;
    @Autowired
    DAO dao;
    @Autowired
    EmployeeBuilder employeeBuilder;

    public InterviewBuilder getInterviewBuilder() {
        return interviewBuilder;
    }

    public void setInterviewBuilder(InterviewBuilder interviewBuilder) {
        this.interviewBuilder = interviewBuilder;
    }

    public AddressBuilder getAddressBuilder() {
        return addressBuilder;
    }

    public void setAddressBuilder(AddressBuilder addressBuilder) {
        this.addressBuilder = addressBuilder;
    }

    public DAO getDao() {
        return dao;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }

    public EmployeeBuilder getEmployeeBuilder() {
        return employeeBuilder;
    }

    public void setEmployeeBuilder(EmployeeBuilder employeeBuilder) {
        this.employeeBuilder = employeeBuilder;
    }

    public Schedule createScheduleEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = null;
        if (scheduleDTO != null) {
            schedule = new Schedule();
            schedule.setDescription(scheduleDTO.getDescription());
            schedule.setScheduleId(scheduleDTO.getId());
            schedule.setInterview(interviewBuilder.createInterviewEntity(scheduleDTO.getInterviewDTO()));
            //schedule.setScheduleDate(scheduleDTO.getScheduleDate());
            schedule.setScheduleTime(scheduleDTO.getScheduleTime());
            schedule.setVenue(addressBuilder.createAddressEntity(scheduleDTO.getVenue()));
        }
        return schedule;
    }

    public ScheduleDTO createScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = null;
        if (schedule != null) {
            scheduleDTO = new ScheduleDTO();
            scheduleDTO.setDescription(schedule.getDescription());
            scheduleDTO.setId(schedule.getScheduleId());
            scheduleDTO.setInterviewDTO(interviewBuilder.createInterviewDTO(schedule.getInterview()));
            // scheduleDTO.setScheduleDate(schedule.getScheduleDate());
            scheduleDTO.setScheduleTime(schedule.getScheduleTime());
            scheduleDTO.setVenue(addressBuilder.createAddressDTO(schedule.getVenue()));
        }
        return scheduleDTO;
    }




    public Schedule createUpdateScheduleEntity(ScheduledCadidateDTO scheduleDTO) {
        Schedule schedule = null;
        if (scheduleDTO != null) {
            schedule = dao.findBy(Schedule.class, scheduleDTO.getScheduleid());
            String interViewDate = scheduleDTO.getInterviewDate();
            try {
                schedule.setScheduleDate(DateParser.toDate(interViewDate));
            } catch (ParseException ex) {
                Logger.getLogger(ScheduleBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
            schedule.getInterview().setStatus(Status.valueOf(scheduleDTO.getInterviewStatus()));

            Set<Employee> interviewers = new HashSet<Employee>(employeeBuilder.getemployeeEntityList1(scheduleDTO.getEmpoyeeData()));
            Interview interview = schedule.getInterview();
            interview.setRound(scheduleDTO.getRound());
            interview.setInterviewType(InterviewType.valueOf(scheduleDTO.getInterviewType()));
            interview.setComments(scheduleDTO.getComments());
            interview.setInterviewers(interviewers);
            schedule.setInterview(interview);
            schedule.setScheduleTime(scheduleDTO.getTime());

            for (Candidate candidate : schedule.getInterview().getCandidates()) {

                if (candidate.getPersonId().equals(scheduleDTO.getCandidateid())) {

                    candidate.setFirstName(scheduleDTO.getCandidateName());

                    candidate.setEmail(scheduleDTO.getCandidateEmail());
                    candidate.setMobile(scheduleDTO.getCandidateMobile());
                    candidate.setCandidateInterviewStatus(Status.valueOf(scheduleDTO.getCandidateInterviewStatus()));

                    try {
                        candidate.setCandidateJoinDate(DateParser.toDateOtherFormat(scheduleDTO.getCandidateJoinDate()));
                    } catch (ParseException ex) {
                        Logger.getLogger(ScheduleBuilder.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    candidate.setSkill(scheduleDTO.getCandidateskill());

                }

            }

        }
        return schedule;
    }

//    public String convertStringDatetoMysqlDate(String start) {
//        //String start = "01/02/2014";
//        SimpleDateFormat startFormat = new SimpleDateFormat("dd MMM yyyy");
//        SimpleDateFormat toFormat = new SimpleDateFormat("dd/MM/yyyy");
//        java.util.Date date = null;
//        try {
//            date = startFormat.parse(start);
//        } catch (ParseException ex) {
//            java.util.logging.Logger.getLogger(JobPortalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        String mysqlString = toFormat.format(date);
//        return mysqlString;
//    }
}
