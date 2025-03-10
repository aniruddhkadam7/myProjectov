/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.recruitment.business.Document;
import com.raybiztech.recruitment.chart.StatusChart;
import com.raybiztech.recruitment.chart.StatusChartDetails;
import com.raybiztech.recruitment.chart.StatusData;
import com.raybiztech.recruitment.dto.CandidateDTO;
import com.raybiztech.recruitment.dto.CandidateInterviewCycleDTO;
import com.raybiztech.recruitment.dto.CandidateScheduleDto;
import com.raybiztech.recruitment.dto.ScheduledCadidateDTO;
import com.raybiztech.recruitment.utils.DateParser;

/**
 *
 * @author hari
 */
@Component("candidateBuilder")
public class CandidateBuilder {

	@Autowired
	JobVacancyBuilder jobVacancyBuilder;
	@Autowired
	AddressBuilder addressBuilder;
	@Autowired
	DocumentBuilder documentBuilder;
	@Autowired
	SourceLookUpBuilder sourceLookUpBuilder;
	@Autowired
	DAO dao;

	org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(CandidateBuilder.class);

	public CandidateDTO createCandidateDTO(Candidate candidate) {
		CandidateDTO candidateDTO = null;
		if (candidate != null) {
			candidateDTO = new CandidateDTO();
			if (candidate.getAddress() != null) {
				candidateDTO.setAddressDto(addressBuilder
						.createAddressDTO(candidate.getAddress()));
			}
			if (candidate.getAppliedFor() != null) {
				candidateDTO.setAppliedFor(jobVacancyBuilder
						.createJobVacancyDTO(candidate.getAppliedFor()));
			}

			candidateDTO.setDob(DateParser.toString(candidate.getDob()));
			candidateDTO
					.setRecruiter(candidate.getRecruiter() != null ? candidate
							.getRecruiter() != "" ? candidate.getRecruiter()
							: "N/A" : "N/A");

			candidateDTO.setEmail(candidate.getEmail());
			candidateDTO.setExperience(candidate.getExperience());
			candidateDTO.setFirstName(candidate.getFirstName());
			// candidateDTO.setId(candidate.getCandidateId());
			candidateDTO.setLastName(candidate.getLastName());
			candidateDTO.setMiddleName(candidate.getMiddleName());
			// if fullname is not there and lastname is null then its printing
			// null so to avoid it.
			if (candidate.getLastName() == null) {
				candidate.setLastName("");
			}
			candidateDTO
					.setFullName(candidate.getFullName() != null ? candidate
							.getFullName() : candidate.getFirstName() + " "
							+ candidate.getLastName());
			candidateDTO.setMobile(candidate.getMobile());
			candidateDTO.setPersonId(candidate.getPersonId());
			candidateDTO.setPhone(candidate.getPhone());
			candidateDTO.setQualification(candidate.getQualification());
			candidateDTO.setSkills(candidate.getSkill());
			candidateDTO.setResumePath(candidate.getResumePath());
			if (candidate.getCandidateInterviewStatus() != null) {
				candidateDTO.setCadidateInterviewStatus(candidate
						.getCandidateInterviewStatus().toString());
			}
			if (candidate.getSourcelookUp() != null) {
				candidateDTO.setSourcelookUp(sourceLookUpBuilder
						.createSourceLookUpDTO(candidate.getSourcelookUp()));
			}
			candidateDTO.setAppliedForVacancy(candidate.getAppliedForLookUp());
			
			candidateDTO.setCurrentEmployer(candidate.getCurrentEmployer() != null ? candidate.getCurrentEmployer(): null);

			candidateDTO.setTechnology(candidate.getTechnology());
	
			candidateDTO.setCadidateInterviewStatus(candidate.getCandidateInterviewStatus()!=null ? 
					           candidate.getCandidateInterviewStatus().toString():null);
			candidateDTO.setCountry(candidate.getCountry()!=null?candidate.getCountry():null);
			System.out.println(candidate.getMobile());
			System.out.println(candidate.getPhone());
			if(candidate.getCountryCode()!=null){
				System.out.println("in if");
			  CountryLookUp country = dao.findBy(CountryLookUp.class, candidate.getCountryCode());
			  candidateDTO.setMobile(country.getMobileCode() + candidate.getMobile());
			}else{
				System.out.println("in else");
				candidateDTO.setMobile(candidate.getMobile());
			}
			
		}
		return candidateDTO;
	}

	public Candidate createCandidateEntity(CandidateDTO candidateDTO) {
		Candidate candidate = null;
		if (candidateDTO != null) {
			candidate = new Candidate();

			candidate.setAddress(addressBuilder
					.createAddressEntity(candidateDTO.getAddressDto()));
			candidate.setAppliedFor(jobVacancyBuilder
					.createJobVacancy(candidateDTO.getAppliedFor()));
			try {
				candidate.setDob(DateParser.toDate(candidateDTO.getDob()));
			} catch (ParseException ex) {
				Logger.getLogger(CandidateBuilder.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			candidate.setDocumentList(documentBuilder
					.createDocumentEntitySet(candidateDTO.getDocumentList()));
			candidate.setEmail(candidateDTO.getEmail());
			candidate.setExperience(candidateDTO.getExperience());
			candidate.setFirstName(candidateDTO.getFirstName());
			// candidate.setCandidateId(candidateDTO.getId());
			candidate.setLastName(candidateDTO.getLastName());
			candidate.setMiddleName(candidateDTO.getMiddleName());
			candidate.setFullName(candidateDTO.getFirstName() + " "
					+ candidate.getLastName());
			candidate.setMobile(candidateDTO.getMobile());
			candidate.setPersonId(candidateDTO.getPersonId());
			candidate.setPhone(candidateDTO.getPhone());
			candidate.setQualification(candidateDTO.getQualification());
			candidate.setSkill(candidateDTO.getSkills());
			candidate.setSourcelookUp(sourceLookUpBuilder
					.createSourceLookUpEntity(candidateDTO.getSourcelookUp()));

		}
		return candidate;
	}

	public List<Candidate> createCandidateEntityList(
			List<CandidateDTO> candidateDTOList) {
		List<Candidate> candidateList = null;
		if (candidateDTOList != null) {
			candidateList = new ArrayList<Candidate>();
			for (CandidateDTO candidateDTO : candidateDTOList) {
				candidateList.add(createCandidateEntity(candidateDTO));
			}
		}
		return candidateList;
	}

	public List<CandidateDTO> createCandidateDTOList(
			List<Candidate> candidateList) {
		List<CandidateDTO> candidateDTOList = null;
		if (candidateList != null) {
			candidateDTOList = new ArrayList<CandidateDTO>();

			for (Candidate candidate : candidateList) {

				candidateDTOList.add(createCandidateDTO(candidate));
			}
		}
		return candidateDTOList;
	}

	public List<CandidateDTO> convertCandidateListToDTOList(
			List<Candidate> candidateList) {
		List<CandidateDTO> candidateDTOList = null;
		if (candidateList != null) {
			candidateDTOList = new ArrayList<CandidateDTO>();

			for (Candidate candidate : candidateList) {

				candidateDTOList.add(createCandidateDTO(candidate));
			}
		}

		return candidateDTOList;
	}

	public List<ScheduledCadidateDTO> convertEntityToDTO(
			List<Candidate> candidates) {

		List<ScheduledCadidateDTO> scheduledCadidateDTOs = null;
		if (!candidates.isEmpty()) {
			scheduledCadidateDTOs = new ArrayList<ScheduledCadidateDTO>();
			for (Candidate candidate : candidates) {
				ScheduledCadidateDTO cadidateDTO = new ScheduledCadidateDTO();
				cadidateDTO
						.setCandidateName(candidate.getFullName() != null ? candidate
								.getFullName() : candidate.getFirstName() + " "
								+ candidate.getLastName());
				cadidateDTO.setCandidateEmail(candidate.getEmail());
				cadidateDTO.setAppliedForVacancy(candidate
						.getAppliedForLookUp());
				cadidateDTO.setExperience(candidate.getExperience());
				cadidateDTO.setCandidateJoinDate(candidate
						.getCandidateJoinDate().toString());
				scheduledCadidateDTOs.add(cadidateDTO);
			}
		}
		return scheduledCadidateDTOs;

	}

	public List<CandidateInterviewCycleDTO> convertCandidateInterviewDTO(
			List<CandidateInterviewCycle> cicList) {
		List<CandidateInterviewCycleDTO> interviewCycleDTOs = new ArrayList<CandidateInterviewCycleDTO>();
		for (CandidateInterviewCycle cycle : cicList) {
			CandidateInterviewCycleDTO cycleDTO = new CandidateInterviewCycleDTO();
			cycleDTO.setCandidateName(cycle.getCandidate().getFullName() != null ? cycle
					.getCandidate().getFullName() : cycle.getCandidate()
					.getFirstName() + " " + cycle.getCandidate().getLastName());
			cycleDTO.setExperiance(cycle.getCandidate().getExperience());
			cycleDTO.setInterviewDate(cycle.getInterviewDate().toString(
					"dd MMM yyyy"));
			cycleDTO.setInterviewTime(cycle.getInterviewTime());
			cycleDTO.setInterviewCycleId(cycle.getInterviewCycleId());
			cycleDTO.setInterviewRound(cycle.getCandidate().getInterview()
					.getRound());
			cycleDTO.setInterviewers(cycle.getInterviewers());
			interviewCycleDTOs.add(cycleDTO);
		}
		return interviewCycleDTOs;
	}

	public CandidateInterviewCycleDTO getCandidateInterviewCycleDTO(
			CandidateInterviewCycle interviewCycle) {
		CandidateInterviewCycleDTO cycleDTO = null;
		if (interviewCycle != null) {
			cycleDTO = new CandidateInterviewCycleDTO();
			Candidate candidate = interviewCycle.getCandidate();
			cycleDTO.setCandidateId(String.valueOf(candidate.getPersonId()));
			cycleDTO.setCandidateName(interviewCycle.getCandidate()
					.getFullName() != null ? interviewCycle.getCandidate()
					.getFullName() : interviewCycle.getCandidate()
					.getFirstName()
					+ " "
					+ interviewCycle.getCandidate().getLastName());
			cycleDTO.setInterviewMode(String.valueOf(interviewCycle
					.getCandidate().getInterview().getInterviewType()));
			cycleDTO.setInterviewRound(String.valueOf(interviewCycle
					.getInterviewRound()));
			cycleDTO.setInterviewTime(interviewCycle.getInterviewTime());
			cycleDTO.setInterviewDate(interviewCycle.getInterviewDate()
					.toString("dd MMM yyyy"));
			cycleDTO.setInterviewCycleId(interviewCycle.getInterviewCycleId());
			cycleDTO.setCandiadateEmailId(candidate.getEmail());
			cycleDTO.setSkills(candidate.getSkill());
			cycleDTO.setMobileNumber(candidate.getMobile());
			cycleDTO.setStatus(interviewCycle.getStatus());
			cycleDTO.setInterviewers(interviewCycle.getInterviewers());
			cycleDTO.setRating(interviewCycle.getRating());
		}
		return cycleDTO;
	}

	public List<CandidateInterviewCycleDTO> convertCandidateInterviewDTOsListForEmployee(
			List<CandidateInterviewCycle> cicList) {
		List<CandidateInterviewCycleDTO> interviewCycleDTOs = new ArrayList<CandidateInterviewCycleDTO>();
		List<CandidateInterviewCycleDTO> remove = new ArrayList<CandidateInterviewCycleDTO>();
		for (CandidateInterviewCycle cycle : cicList) {
			CandidateInterviewCycleDTO cycleDTO = new CandidateInterviewCycleDTO();
			Candidate candidate = cycle.getCandidate();
			cycleDTO.setCandidateId(String.valueOf(candidate.getPersonId()));
			cycleDTO.setCandidateName(cycle.getCandidate().getFullName() != null ? cycle
					.getCandidate().getFullName() : cycle.getCandidate()
					.getFirstName() + " " + cycle.getCandidate().getLastName());
			cycleDTO.setExperiance(cycle.getCandidate().getExperience());
			cycleDTO.setInterviewDate(cycle.getInterviewDate().toString(
					"dd MMM yyyy"));
			cycleDTO.setInterviewTime(cycle.getInterviewTime() != null ? cycle.getInterviewTime()
					: /*candidate.getStatusChangeTime()*/ null);
			if(cycle.getCandidate().getSourcelookUp().getSourceName()!=null){
			cycleDTO.setSourceName(cycle.getCandidate().getSourcelookUp().getSourceName());
			}
			cycleDTO.setInterviewCycleId(cycle.getInterviewCycleId());
			cycleDTO.setInterviewRound(String.valueOf(cycle.getInterviewRound()));
			cycleDTO.setInterviewers(cycle.getInterviewers()!=null? cycle.getInterviewers():null);
			cycleDTO.setCandiadateEmailId(candidate.getEmail());
			cycleDTO.setSkills(candidate.getSkill());
			cycleDTO.setInterviewStatus(candidate.getCandidateInterviewStatus()
					.name());
			cycleDTO.setSkypeId(cycle.getCandidate().getSkypeId());
			cycleDTO.setMobileNumber(cycle.getCandidate().getMobile());
			cycleDTO.setUpdatedBy(cycle.getUpdatedBy());
			cycleDTO.setInterviewResultStatus(cycle.getInterviewResultStatus()!=null?
					 cycle.getInterviewResultStatus():null);
			if (candidate.getInterview() != null) {
				cycleDTO.setInterviewMode(String.valueOf(candidate
						.getInterview().getInterviewType()));
				System.out.println("interview mode:" + candidate.getInterview().getInterviewType());
			}
			cycleDTO.setRecruiter(cycle.getCandidate().getRecruiter());
			int i = 0;

			if (interviewCycleDTOs.size() > 0) {
				System.out.println("in if");

				// we are checking duplicate candidates for schedule list
				for (CandidateInterviewCycleDTO cycleDTOz : interviewCycleDTOs) {
					if (cycleDTOz.getCandidateId().equals(
							cycleDTO.getCandidateId())) {
						System.out.println("in id");
						i = 1;
						/*if(cycleDTO.getInterviewResultStatus()!=null){
						if(!cycleDTO.getInterviewResultStatus().equalsIgnoreCase("ADD_COMMENTS")){
							System.out.println("stat:" + cycleDTO.getInterviewResultStatus());
						 if ((cycleDTOz.getInterviewCycleId() < cycleDTO.getInterviewCycleId())) {
							 System.out.println("in if");
							remove.add(cycleDTOz);
							i = 0;
						}
					}
				}*/ 
				System.out.println("id:" + cycleDTO.getCandidateId() + cycleDTO.getCandidateName() + cycleDTO.getInterviewResultStatus() + cycleDTO.getInterviewTime());
				System.out.println("id:" + cycleDTOz.getCandidateId() + cycleDTOz.getCandidateName() + cycleDTOz.getInterviewResultStatus() + cycleDTOz.getInterviewTime());
				
						if ((cycleDTOz.getInterviewCycleId() < cycleDTO.getInterviewCycleId())) {
							 System.out.println("previous id:" + cycleDTOz.getCandidateId() + "new:" + cycleDTO.getCandidateId());
							   remove.add(cycleDTOz);
							    i = 0;
							}
						if((cycleDTOz.getInterviewCycleId() > cycleDTO.getInterviewCycleId())){
							if(cycleDTOz.getInterviewResultStatus()!=null && cycleDTOz.getInterviewResultStatus().equalsIgnoreCase("ADD_COMMENTS")){
								System.out.println("check");
								remove.add(cycleDTOz);
								i=0;
							}
							
						}
						}
					}

			
			}
			for(CandidateInterviewCycleDTO cycleDTOz : interviewCycleDTOs){
				if(cycleDTOz.getCandidateId()!= cycleDTO.getCandidateId()){
					if(cycleDTOz.getInterviewResultStatus()!=null && cycleDTOz.getInterviewResultStatus().equalsIgnoreCase("ADD_COMMENTS")){
						System.out.println("before");
						remove.add(cycleDTOz);
						i=0;
					}
				}
			}
				
			if (i == 0 && cycle.getInterviewRound() > 0) {
				System.out.println("final");
				interviewCycleDTOs.add(cycleDTO);
			  
			}
			// Old Interview round are removed if latest are there
			interviewCycleDTOs.removeAll(remove);

		}
		return interviewCycleDTOs;
	}

	public CandidateScheduleDto editCandidateDTO(Candidate candidate) {

		CandidateScheduleDto cadidateDTO = new CandidateScheduleDto();
		if (candidate != null) {
			cadidateDTO.setCurrentEmployer(candidate.getCurrentEmployer());
			cadidateDTO.setCurrentLocation(candidate.getCurrentLocation());
			cadidateDTO.setSkypeId(candidate.getSkypeId());
			cadidateDTO.setCtc(candidate.getCtc());
			cadidateDTO.setEctc(candidate.getEctc());
			cadidateDTO.setNp(candidate.getNp());
			cadidateDTO.setReason(candidate.getReason());
			
			cadidateDTO.setCandidateId(candidate.getPersonId());
			cadidateDTO.setCandidateFirstName(candidate.getFirstName());
			cadidateDTO.setCandidateLastName(candidate.getLastName());
			cadidateDTO.setCandidateName(candidate.getFullName());
			cadidateDTO.setCandidateEmail(candidate.getEmail());
			cadidateDTO.setTechnology(candidate.getTechnology());
			String orignalPath = candidate.getResumePath();
			if (orignalPath != null) {
				String splitedPath = orignalPath.substring(orignalPath
						.lastIndexOf("/") + 1);
				cadidateDTO.setUploadResume(splitedPath);
			}

			cadidateDTO.setOtherDocs(candidate.getOtherDocumentPath());
			if (candidate.getExperience() != null) {
				cadidateDTO.setExperience(candidate.getExperience().toString());
			}

			cadidateDTO.setSkills(candidate.getSkill());
			if(candidate.getDob()!=null) {
			cadidateDTO.setDob(candidate.getDob().toString("dd/MM/yyyy"));
			}
			cadidateDTO.setMobile(candidate.getMobile());
			cadidateDTO.setAppliedForLookUp(candidate.getAppliedForLookUp());
			cadidateDTO.setVacancyId(candidate.getAppliedFor()
					.getJobVacancyId());
			cadidateDTO.setTimelineStatus(candidate.getTimelineStatus());
			cadidateDTO.setStatus(String.valueOf(candidate
					.getCandidateInterviewStatus()));
			cadidateDTO
					.setRecruiter(candidate.getRecruiter() != null ? candidate
							.getRecruiter() : null);

			if (candidate.getAppliedFor() != null) {
				cadidateDTO.setAppliedFor(jobVacancyBuilder
						.createJobVacancyDTO(candidate.getAppliedFor()));
			}
			if (candidate.getSourcelookUp() != null) {
				cadidateDTO.setSourceType(candidate.getSourcelookUp()
						.getSourceType());
			}
			if (candidate.getSourcelookUp() != null) {
				cadidateDTO.setSourceName(candidate.getSourcelookUp()
						.getSourceName());
			}

			if (!candidate.getDocumentList().isEmpty()) {
				for (Document document : candidate.getDocumentList()) {
					cadidateDTO.setUploadResume(document.getDoctokenId());
					cadidateDTO.setOtherDocs(document.getDoctokenId());
				}
			}
			
			if(candidate.getJobTypeName() !=null) {
				cadidateDTO.setJobTypeName(candidate.getJobTypeName());
			}
			if(candidate.getAdhar() !=null){
				cadidateDTO.setAdhar(candidate.getAdhar());
			}
			if(candidate.getPan() !=null){
				cadidateDTO.setPan(candidate.getPan());
			}
			if(candidate.getLinkedin() !=null){
				cadidateDTO.setLinkedin(candidate.getLinkedin());
			}
			if(candidate.getCountry()!=null){
				cadidateDTO.setCountry(candidate.getCountry());	
				cadidateDTO.setCountryId(candidate.getCountry().getId());
			}
			if(candidate.getNotifications()!=null){
				cadidateDTO.setNotifications(candidate.getNotifications()!=null?candidate.getNotifications():null);
			}
			if(candidate.getCountryCode()!=null){
			       cadidateDTO.setCountryCode(candidate.getCountryCode());
			}

		}
		return cadidateDTO;

	}
	
	
	public StatusChart buildChart(List<Object[]> statusList, String from, String to) {
		
		
		StatusChart statusChart = new StatusChart();
		
		StatusChartDetails details = new StatusChartDetails();
		
		details.setCaption("Candidate Status - Chart");
		details.setSubcaption("From " + from + " To " + to);
		details.setPlottooltext("Status : $label , count : $datavalue");
		details.setLabelDisplay("rotate");
		details.setSlantLabel("1");
		details.setTheme("fint");
		details.setxAxisName("Candidate Status");
		details.setyAxisName("Count");
		List<StatusData>  list = new ArrayList<StatusData>();
		for (Object[] obj : statusList) {
			StatusData data = new StatusData();
			data.setLabel(obj[0].toString());
			data.setValue(Integer.valueOf(obj[1].toString()));
			list.add(data);
			Collections.sort(list, new Comparator<StatusData>()
			 {
               @Override
				public int compare(StatusData o1, StatusData o2)
                   {
					   return o2.getValue().compareTo(o1.getValue());
				   }
			 });
		}

		statusChart.setChart(details);
		statusChart.setData(list);

		return statusChart;
		
	}
	public List<CandidateInterviewCycleDTO> convertCandidateInterviewDTOsListForEmploye(
			List<CandidateInterviewCycle> cicList) {
		List<CandidateInterviewCycleDTO> interviewCycleDTOs = new ArrayList<CandidateInterviewCycleDTO>();
		List<CandidateInterviewCycleDTO> remove = new ArrayList<CandidateInterviewCycleDTO>();
		for (CandidateInterviewCycle cycle : cicList) {
			CandidateInterviewCycleDTO cycleDTO = new CandidateInterviewCycleDTO();
			Candidate candidate = cycle.getCandidate();
			cycleDTO.setCandidateId(String.valueOf(candidate.getPersonId()));
			cycleDTO.setCandidateName(cycle.getCandidate().getFullName() != null ? cycle
					.getCandidate().getFullName() : cycle.getCandidate()
					.getFirstName() + " " + cycle.getCandidate().getLastName());
			cycleDTO.setExperiance(cycle.getCandidate().getExperience());
			cycleDTO.setInterviewDate(cycle.getInterviewDate().toString(
					"dd MMM yyyy"));
			cycleDTO.setInterviewTime(cycle.getInterviewTime() != null ? cycle.getInterviewTime()
					: /*candidate.getStatusChangeTime()*/ null);
			
			cycleDTO.setInterviewCycleId(cycle.getInterviewCycleId());
			cycleDTO.setInterviewRound(String.valueOf(cycle.getInterviewRound()));
			cycleDTO.setInterviewers(cycle.getInterviewers()!=null? cycle.getInterviewers():null);
			cycleDTO.setCandiadateEmailId(candidate.getEmail());
			cycleDTO.setSkills(candidate.getSkill());
			cycleDTO.setInterviewStatus(candidate.getCandidateInterviewStatus()
					.name());
			cycleDTO.setSkypeId(cycle.getCandidate().getSkypeId());
			cycleDTO.setMobileNumber(cycle.getCandidate().getMobile());
			cycleDTO.setUpdatedBy(cycle.getUpdatedBy());
			cycleDTO.setInterviewResultStatus(cycle.getInterviewResultStatus()!=null?
					 cycle.getInterviewResultStatus():null);
			if (candidate.getInterview() != null) {
				cycleDTO.setInterviewMode(String.valueOf(candidate
						.getInterview().getInterviewType()));
			}
			cycleDTO.setRecruiter(cycle.getCandidate().getRecruiter());
			cycleDTO.setStatus(cycle.getStatus());
			System.out.println("name:" + cycleDTO.getCandidateName() + "status:" + cycleDTO.getStatus());
			int i = 0;

			if (interviewCycleDTOs.size() > 0) {

				// we are checking duplicate candidates for schedule list
				for (CandidateInterviewCycleDTO cycleDTOz : interviewCycleDTOs) {
					if (cycleDTOz.getCandidateId().equals(
							cycleDTO.getCandidateId())) {
						i = 1;
				      if ((cycleDTOz.getInterviewCycleId() < cycleDTO.getInterviewCycleId())) {
							   remove.add(cycleDTOz);
							    i = 0;
							}
				 
					}

				}
			}
				
			if (i == 0 && cycle.getInterviewRound() > 0) {
				interviewCycleDTOs.add(cycleDTO);
			  
			}
			// Old Interview round are removed if latest are there
			interviewCycleDTOs.removeAll(remove);

		}
		return interviewCycleDTOs;
	}
}
