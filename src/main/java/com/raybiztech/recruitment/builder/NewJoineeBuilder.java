package com.raybiztech.recruitment.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.NewJoinee;
import com.raybiztech.recruitment.dto.NewJoineeDTO;

@Component("joineeBuilder")
public class NewJoineeBuilder {
	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(NewJoineeBuilder.class);

	public NewJoinee createJoineeEntityFromJoineeDTO(NewJoineeDTO joineeDTO)
			throws ParseException {
		NewJoinee newJoinee = null;
		if (joineeDTO instanceof NewJoineeDTO) {
			newJoinee = new NewJoinee();

			NewJoinee x = convertEditJoineeToJoinee(newJoinee, joineeDTO);
		}
		return newJoinee;
	}

	public List<NewJoineeDTO> convertJoineeDTOListTOEntityList(
			List<NewJoinee> joineeList) {
		List<NewJoineeDTO> joineeDtoList = null;

		if (joineeList != null) {

			joineeDtoList = new ArrayList<NewJoineeDTO>();
			for (NewJoinee newJoinee : joineeList) {
				Candidate candidate = dao.findBy(Candidate.class,
						newJoinee.getCandidateId());
				NewJoineeDTO newJoineeDTO = new NewJoineeDTO();
				newJoineeDTO.setAppliedForLookUp(newJoinee.getPosition());
				if(newJoinee.getAttachedDocumentPath()!=null){
					String[] sz = newJoinee.getAttachedDocumentPath().split("/");
				newJoineeDTO.setAttachedDocumentPath(sz[sz.length-1]);
				}
				newJoineeDTO.setCandidateEmail(candidate.getEmail());
				newJoineeDTO.setMobile(candidate.getMobile());

				newJoineeDTO.setCandidateName(newJoinee.getJoineeName());
				newJoineeDTO.setComments(newJoinee.getComments());
				newJoineeDTO.setCurrentCTC(newJoinee.getCurrentCTC());
				newJoineeDTO.setDateOfJoining(DateParser.toString(newJoinee
						.getDateOfJoining()));
				newJoineeDTO.setDesignation(newJoinee.getDesignation());
                                newJoineeDTO.setDepartmentName(newJoinee.getDepartmentName());
				newJoineeDTO.setEmploymentType(newJoinee.getEmploymentType());
				newJoineeDTO.setExperience(candidate.getExperience());
				newJoineeDTO.setId(newJoinee.getId());
				newJoineeDTO.setJobType(newJoinee.getJobType());
				newJoineeDTO.setStatus(newJoinee.getStatus());
				newJoineeDTO.setTechnology(newJoinee.getTechnology());
				newJoineeDTO.setCandidateId(newJoinee.getCandidateId());
				newJoineeDTO.setCandidateInterviewStatus(candidate.getCandidateInterviewStatus().name());
				joineeDtoList.add(newJoineeDTO);
				

			}
		}
		return joineeDtoList;
	}

	public NewJoinee convertEditJoineeToJoinee(NewJoinee newJoinee,
			NewJoineeDTO joineeDTO) {

		newJoinee.setPosition(joineeDTO.getAppliedForLookUp());
		newJoinee.setJoineeName(joineeDTO.getCandidateName());
		newJoinee.setDesignation(joineeDTO.getDesignation());
                newJoinee.setDepartmentName(joineeDTO.getDepartmentName());
		newJoinee.setTechnology(joineeDTO.getTechnology());
		try {
			newJoinee.setDateOfJoining(DateParser.toDate(joineeDTO
					.getDateOfJoining()));
		} catch (ParseException pfe) {
		}

		try {
			newJoinee.setDateOfBirth(Date.parse(joineeDTO.getDateOfBirth(),
					"dd MMM yyyy"));
		} catch (ParseException pfe) {
		}

		newJoinee.setCurrentCTC(joineeDTO.getCurrentCTC());
		newJoinee.setEmploymentType(joineeDTO.getEmploymentType());
		newJoinee.setJobType(joineeDTO.getJobType());
		newJoinee.setStatus(joineeDTO.getStatus());
		newJoinee.setComments(joineeDTO.getComments());
		newJoinee.setAttachedDocumentPath(joineeDTO.getAttachedDocumentPath());
		newJoinee.setExperience(joineeDTO.getExperience());
		newJoinee.setEmail(joineeDTO.getCandidateEmail());
		newJoinee.setCandidateId(joineeDTO.getCandidateId());
		newJoinee.setCandidateInterviewStatus(joineeDTO.getCandidateInterviewStatus());

		return newJoinee;
	}

	public NewJoineeDTO convertEntityToDTO(NewJoinee newJoinee) {

		NewJoineeDTO newJoineeDTO = null;
		if (newJoinee instanceof NewJoinee) {
			newJoineeDTO = new NewJoineeDTO();
			newJoineeDTO.setAppliedForLookUp(newJoinee.getPosition());
			newJoineeDTO.setCandidateEmail(newJoinee.getEmail());
			newJoineeDTO.setCandidateName(newJoinee.getJoineeName());
			if (newJoinee.getDateOfJoining() != null) {
				newJoineeDTO.setDateOfJoining(newJoinee.getDateOfJoining()
						.toString("dd/MM/yyyy"));
			}
			if (newJoinee.getDateOfBirth() != null) {
				newJoineeDTO.setDateOfBirth(newJoinee.getDateOfBirth()
						.toString("dd/MM/yyyy"));

			}
			newJoineeDTO.setDesignation(newJoinee.getDesignation());
                        newJoineeDTO.setDepartmentName(newJoinee.getDepartmentName());
			newJoineeDTO.setEmploymentType(newJoinee.getEmploymentType());
			newJoineeDTO.setJobType(newJoinee.getJobType());
		}
		return newJoineeDTO;
	}

}
