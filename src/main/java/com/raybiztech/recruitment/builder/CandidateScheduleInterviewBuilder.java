/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.builder;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Status;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.lookup.business.SourceLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.DocType;
import com.raybiztech.recruitment.business.Document;
import com.raybiztech.recruitment.business.Interview;
import com.raybiztech.recruitment.business.InterviewType;
import com.raybiztech.recruitment.business.JobVacancy;
import com.raybiztech.recruitment.business.Schedule;
import com.raybiztech.recruitment.dto.CandidateScheduleDto;
import com.raybiztech.recruitment.utils.DateParser;

/**
 *
 * @author hari
 */
@Component("candidateScheduleInterviewBuilder")
public class CandidateScheduleInterviewBuilder {

	@Autowired
	DAO dao;
	@Autowired
	SourceLookUpBuilder sourceLookUpBuilder;
	@Autowired
	DocumentBuilder documentBuilder;
	@Autowired
	EmployeeBuilder employeeBuilder;
	Logger logger = Logger.getLogger(CandidateScheduleInterviewBuilder.class);
	@Autowired
	JobVacancyBuilder jobVacancyBuilder;

	public Candidate createCandidateEntity(
			CandidateScheduleDto candidateScheduleDto) {
		Candidate candidate = null;

		if (candidateScheduleDto != null) {
			candidate = new Candidate();
			candidate.setCurrentEmployer(candidateScheduleDto.getCurrentEmployer());
			candidate.setCurrentLocation(candidateScheduleDto.getCurrentLocation());
			candidate.setSkypeId(candidateScheduleDto.getSkypeId());
			candidate.setCtc(candidateScheduleDto.getCtc());
			candidate.setEctc(candidateScheduleDto.getEctc());
			candidate.setNp(candidateScheduleDto.getNp());
			candidate.setReason(candidateScheduleDto.getReason());
			candidate
					.setFirstName(candidateScheduleDto.getCandidateFirstName());
			candidate.setLastName(candidateScheduleDto.getCandidateLastName());
			candidate.setFullName(WordUtils
					.capitalizeFully(candidateScheduleDto
							.getCandidateFirstName()
							+ " "
							+ candidateScheduleDto.getCandidateLastName()));

			candidate.setEmail(candidateScheduleDto.getCandidateEmail());
			candidate.setExperience(candidateScheduleDto.getExperience());
			candidate.setSkill(candidateScheduleDto.getSkills());
			candidate.setTechnology(candidateScheduleDto.getTechnology());
			candidate
					.setRecruiter(candidateScheduleDto.getRecruiter() != null ? candidateScheduleDto
							.getRecruiter() : null);
			SourceLookUp lookUp = new SourceLookUp();
			lookUp.setSourceName(candidateScheduleDto.getSourceName());
			lookUp.setSourceType(candidateScheduleDto.getSourceType());
			candidate.setSourcelookUp(lookUp);
			candidate.setCreatedDate(new Second());
			try {
				candidate.setDob(DateParser.toDate(candidateScheduleDto
						.getDob()));
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						CandidateScheduleInterviewBuilder.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			Set<Document> documentList = new HashSet<Document>();
			Document uploadResume = new Document();
			uploadResume.setDocType(DocType.RESUME);
			uploadResume.setDoctokenId(candidateScheduleDto.getUploadResume());

			Document otherDoc = new Document();
			otherDoc.setDocType(DocType.OTHER);
			otherDoc.setDoctokenId(candidateScheduleDto.getOtherDocs());

			documentList.add(uploadResume);
			documentList.add(otherDoc);

			candidate.setDocumentList(documentList);

			candidate.setAppliedForLookUp(candidateScheduleDto
					.getAppliedForLookUp());
			candidate.setCandidateInterviewStatus(Status.NEW);
			candidate.setStatus("1");
			candidate.setJobTypeName(candidateScheduleDto.getJobTypeName()!=null?
					candidateScheduleDto.getJobTypeName():null);
			candidate.setAddedDate(new Date());
			
			candidate.setAdhar(candidateScheduleDto.getAdhar());
			candidate.setPan(candidateScheduleDto.getPan());
			candidate.setLinkedin(candidateScheduleDto.getLinkedin());
			//candidate.setMiddleName(candidateScheduleDto.getmid);
			candidate.setMobile(candidateScheduleDto.getMobile());
			if(candidateScheduleDto.getCountryId()!=null){
			CountryLookUp con = dao.findBy(CountryLookUp.class, candidateScheduleDto.getCountryId());
			candidate.setCountry(con);
			if(candidateScheduleDto.getNotifications()!=null){
				candidate.setNotifications(candidateScheduleDto.getNotifications()!=null?candidateScheduleDto.getNotifications():null);
			}
			}
			if(candidateScheduleDto.getCountryCode()!=null){
				CountryLookUp con = dao.findBy(CountryLookUp.class, candidateScheduleDto.getCountryCode());
				candidate.setCountryCode(candidateScheduleDto.getCountryCode());
			}else{
				candidate.setCountryCode(null);	
			}

		}
		return candidate;
	}

	public Schedule createScheduleForCandidateEntity(Candidate candidate,
			CandidateScheduleDto candidateScheduleDto) {
		Schedule schedule = null;

		if (candidateScheduleDto != null) {
			schedule = new Schedule();
			String interViewDate = candidateScheduleDto.getScheduleDate();

			try {
				schedule.setScheduleDate(DateParser.toDate(interViewDate));
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						CandidateScheduleInterviewBuilder.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			schedule.setScheduleTime(candidateScheduleDto.getScheduleTime());
			schedule.setDescription(candidateScheduleDto.getDescription());
			if(candidateScheduleDto.getContactDetails()!=null){
			schedule.setContactDetails(candidateScheduleDto.getContactDetails());
			}
			Interview interview = new Interview();

			interview.setRound(candidateScheduleDto.getInterviewRound());
			if ("FACE TO FACE".equals(candidateScheduleDto.getInterviewType())) {
				interview.setInterviewType(InterviewType
						.valueOf("FACE_TO_FACE"));
			} else {
				interview.setInterviewType(InterviewType
						.valueOf(candidateScheduleDto.getInterviewType()));
			}
			// Set<Employee> interviewers = new
			// HashSet<Employee>(employeeBuilder.getemployeeEntityList1(candidateScheduleDto.getInterviewersDTOList()));

			// interview.setInterviewers(interviewers);
			Set<Employee> interviewers = new HashSet<Employee>();
			Employee interviewer = dao.findBy(Employee.class,
					candidateScheduleDto.getInterviewerId());
			interviewers.add(interviewer);
			interview.setInterviewers(interviewers);
			//candidate.setInterview(interview);
			//candidate.setInterview(schedule.getInterview());
			Set<Candidate> candidateSet = new HashSet<Candidate>();
			candidateSet.add(candidate);
			interview.setCandidates(candidateSet);
			schedule.setInterview(interview);
		}
		return schedule;
	}

	public Candidate editCandidateEntity(Candidate candidate,
			CandidateScheduleDto candidateScheduleDto) {
		if (candidateScheduleDto != null) {
			candidate.setNp(candidateScheduleDto.getNp());
			candidate.setCtc(candidateScheduleDto.getCtc());
			candidate.setEctc(candidateScheduleDto.getEctc());
			candidate.setReason(candidateScheduleDto.getReason());
			candidate
					.setFirstName(candidateScheduleDto.getCandidateFirstName());
			candidate.setLastName(candidateScheduleDto.getCandidateLastName());
			candidate.setFullName(candidateScheduleDto.getCandidateFirstName()
					+ " " + candidateScheduleDto.getCandidateLastName());
			candidate.setEmail(candidateScheduleDto.getCandidateEmail());
			candidate.setExperience(candidateScheduleDto.getExperience());
			candidate.setMobile(candidateScheduleDto.getMobile());
			candidate.setTechnology(candidateScheduleDto.getTechnology());
			candidate
					.setRecruiter(candidateScheduleDto.getRecruiter() != null ? candidateScheduleDto
							.getRecruiter() : null);
			candidate.setCurrentEmployer(candidateScheduleDto.getCurrentEmployer());
			candidate.setCurrentLocation(candidateScheduleDto.getCurrentLocation());
			candidate.setSkypeId(candidateScheduleDto.getSkypeId());
			JobVacancy jobVacancy = dao.findBy(JobVacancy.class,
					candidateScheduleDto.getVacancyId());
			candidate.setAppliedFor(jobVacancy);
			candidate.setSkill(candidateScheduleDto.getSkills());

			if (candidate.getSourcelookUp() != null) {
				candidate.getSourcelookUp().setSourceName(
						candidateScheduleDto.getSourceName());
				candidate.getSourcelookUp().setSourceType(
						candidateScheduleDto.getSourceType());
			} else {
				SourceLookUp lookUp = new SourceLookUp();
				lookUp.setSourceName(candidateScheduleDto.getSourceName());
				lookUp.setSourceType(candidateScheduleDto.getSourceType());
				candidate.setSourcelookUp(lookUp);
			}
			
				Set<Document> documentList = new HashSet<Document>();

				Document uploadResume = new Document();
				uploadResume.setDocType(DocType.RESUME);
				uploadResume.setDoctokenId(candidateScheduleDto
						.getUploadResume());

				Document otherDoc = new Document();
				otherDoc.setDocType(DocType.OTHER);
				otherDoc.setDoctokenId(candidateScheduleDto.getOtherDocs());

				documentList.add(uploadResume);
				documentList.add(otherDoc);

				candidate.setDocumentList(documentList);
				if(candidateScheduleDto.getAdhar() !=null){
					candidate.setAdhar(candidateScheduleDto.getAdhar());
				}
				if(candidateScheduleDto.getPan() != null){
					candidate.setPan(candidateScheduleDto.getPan());
				}
				if(candidateScheduleDto.getLinkedin() != null)
					candidate.setLinkedin(candidateScheduleDto.getLinkedin());
			try {
				candidate.setDob(DateParser.toDate(candidateScheduleDto
						.getDob()));
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						CandidateScheduleInterviewBuilder.class.getName()).log(
						Level.SEVERE, null, ex);
			}

			candidate.setAppliedForLookUp(jobVacancy.getPositionVacant());
			candidate.setTimelineStatus(candidateScheduleDto
					.getTimelineStatus());
			CountryLookUp coun = dao.findBy(CountryLookUp.class, candidateScheduleDto.getCountryId());
			candidate.setCountry(coun);
			if(candidateScheduleDto.getNotifications()!=null){
				candidate.setNotifications(candidateScheduleDto.getNotifications()!=null?candidateScheduleDto.getNotifications():null);
			}
		    if(candidateScheduleDto.getCountryCode()!=null){
		    	CountryLookUp con = dao.findBy(CountryLookUp.class, candidateScheduleDto.getCountryCode());
		    	//candidate.setMobile(con.getMobileCode() + candidateScheduleDto.getMobile());
		    	candidate.setCountryCode(candidateScheduleDto.getCountryCode());
		    }else{
		    	candidate.setCountryCode(null);
		    }

		}
		return candidate;
	}
}
