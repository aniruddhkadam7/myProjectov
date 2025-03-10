/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.date.Date;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.recruitment.dto.CandidateInterviewCycleDTO;
import com.raybiztech.recruitment.dto.CandidateInterviewTimelineDTO;

/**
 *
 * @author hari
 */
@Component("candidateInterviewCycleBuilder")
public class CandidateInterviewCycleBuilder {
    
    public List<CandidateInterviewCycleDTO> createCandidateCycleDTOList(List<CandidateInterviewCycle> candidateInterviewCycleList) {
        List<CandidateInterviewCycleDTO> candidateInterviewCycleDTOList = null;
        if (candidateInterviewCycleList != null) {
            candidateInterviewCycleDTOList = new ArrayList<CandidateInterviewCycleDTO>();
            for (CandidateInterviewCycle candidateInterviewCycle : candidateInterviewCycleList) {
                CandidateInterviewCycleDTO candidateInterviewCycleDTO = new CandidateInterviewCycleDTO();
                candidateInterviewCycleDTO.setCandidateId(String.valueOf(candidateInterviewCycle.getCandidate().getPersonId()));
                candidateInterviewCycleDTO.setInterviewComments(candidateInterviewCycle.getInterviewComments());
                candidateInterviewCycleDTO.setInterviewDate(candidateInterviewCycle.getInterviewDate().toString("dd MMM yyyy"));
                candidateInterviewCycleDTO.setInterviewRound(String.valueOf(candidateInterviewCycle.getInterviewRound()));
                candidateInterviewCycleDTO.setInterviewStatus(candidateInterviewCycle.getInterviewStatus());
                candidateInterviewCycleDTO.setInterviewTime(candidateInterviewCycle.getInterviewTime());
                candidateInterviewCycleDTO.setInterviewers(candidateInterviewCycle.getInterviewers());
                candidateInterviewCycleDTO.setRating(candidateInterviewCycle.getRating());
                candidateInterviewCycleDTO.setUpdatedBy(candidateInterviewCycle.getUpdatedBy());
                candidateInterviewCycleDTOList.add(candidateInterviewCycleDTO);
            }
        }
        return candidateInterviewCycleDTOList;
    }
    
    public CandidateInterviewTimelineDTO createCandidateInterviewTimeLineDTOList(List<CandidateInterviewCycle> candidateInterviewCycleList, Candidate candidate) {
        
        CandidateInterviewTimelineDTO candidateInterviewTimelineDTO = new CandidateInterviewTimelineDTO();
        List<CandidateInterviewCycleDTO> candidateInterviewCycleDTOList = null;
        if (candidateInterviewCycleList != null) {
            candidateInterviewCycleDTOList = new ArrayList<CandidateInterviewCycleDTO>();
            for (CandidateInterviewCycle candidateInterviewCycle : candidateInterviewCycleList) {
                CandidateInterviewCycleDTO candidateInterviewCycleDTO = new CandidateInterviewCycleDTO();
                
                if(candidateInterviewCycle.getInterviewComments() != null) {
                String[] comments = candidateInterviewCycle.getInterviewComments().split(" @@@@");
                if(comments.length >1) {
                	candidateInterviewCycleDTO.setProactiveComments(comments[0]);
                    candidateInterviewCycleDTO.setCommunicationComments(comments[1]);
                    candidateInterviewCycleDTO.setExcellenceComments(comments[2]);
                    candidateInterviewCycleDTO.setInterviewComments(comments[3]);
                }
                else {
                	candidateInterviewCycleDTO.setInterviewComments(comments[0]);
                }
                }
                
                //candidateInterviewCycleDTO.setInterviewComments(candidateInterviewCycle.getInterviewComments());
                candidateInterviewCycleDTO.setInterviewRound(String.valueOf(candidateInterviewCycle.getInterviewRound()));
                candidateInterviewCycleDTO.setInterviewStatus(candidateInterviewCycle.getInterviewStatus());
                candidateInterviewCycleDTO.setInterviewers(candidateInterviewCycle.getInterviewers());
                candidateInterviewCycleDTO.setRating(candidateInterviewCycle.getRating());
                candidateInterviewCycleDTO.setStatus(candidateInterviewCycle.getStatus());
                candidateInterviewCycleDTO.setInterviewCycleId(candidateInterviewCycle.getInterviewCycleId());
                candidateInterviewCycleDTO.setInterviewResultStatus(candidateInterviewCycle.getInterviewResultStatus());
                candidateInterviewCycleDTO.setUpdatedBy(candidateInterviewCycle.getUpdatedBy());
               if( candidateInterviewCycle.getInterviewResultStatus()==null && candidateInterviewCycle.getCommentedDate()!=null){
            	   candidateInterviewCycleDTO.setInterviewDate(candidateInterviewCycle.getCommentedDate()!=null?
                		  candidateInterviewCycle.getCommentedDate().toString("dd MMM yyyy"):null);
            	   candidateInterviewCycleDTO.setInterviewTime(candidateInterviewCycle.getCommentedTime());
               }else{
                   candidateInterviewCycleDTO.setInterviewDate(candidateInterviewCycle.getInterviewDate()!=null?
		                      candidateInterviewCycle.getInterviewDate().toString("dd MMM yyyy"):null);
                   candidateInterviewCycleDTO.setInterviewTime(candidateInterviewCycle.getInterviewTime());
            	   
               }
                String a = candidateInterviewCycle.getCandidate().getReason();
               // a.replaceAll("<br/>","");
                candidateInterviewCycleDTO.setReason(a);
                candidateInterviewCycleDTOList.add(candidateInterviewCycleDTO);
                
              
                
            }
        }
        if (candidate != null) {
          //  candidateInterviewTimelineDTO.setFirstName(candidate.getFirstName());
        	candidateInterviewTimelineDTO.setRecruiter(candidate.getRecruiter()!=null?candidate.getRecruiter():"N/A");
            candidateInterviewTimelineDTO.setFullName(candidate.getFullName());
            candidateInterviewTimelineDTO.setEmail(candidate.getEmail());
            candidateInterviewTimelineDTO.setPersonId(candidate.getPersonId());
            candidateInterviewTimelineDTO.setQualification(candidate.getQualification());
            candidateInterviewTimelineDTO.setSkill(candidate.getSkill());
            candidateInterviewTimelineDTO.setStatusComments(candidate.getStatusComments());
            candidateInterviewTimelineDTO.setTimelineStatus(candidate.getTimelineStatus());
            candidateInterviewTimelineDTO.setInitialComments(candidate.getInitialComments());
            candidateInterviewTimelineDTO.setModeOfInterview(candidate.getInterview()!=null?
            	String.valueOf(candidate.getInterview().getInterviewType()):null);
            if(candidate.getOtherDocumentPath()!=null){
				String[] sz = candidate.getOtherDocumentPath().split("/");
				candidateInterviewTimelineDTO.setOtherDocumentPath(sz[sz.length-1]);
			}
            if (candidate.getAppliedFor() != null) {
                candidateInterviewTimelineDTO.setAppliedFor(candidate.getAppliedFor().getPositionVacant());
            }
            candidateInterviewTimelineDTO.setExperience(candidate.getExperience());
            candidateInterviewTimelineDTO.setCandidateStatus(String.valueOf(candidate.getCandidateInterviewStatus()));
            String orignalPath = candidate.getResumePath();
            if (orignalPath != null) {
                String splitedPath = orignalPath.substring(orignalPath.lastIndexOf("/") + 1);
                candidateInterviewTimelineDTO.setResumePath(splitedPath);
            }
            candidateInterviewTimelineDTO.setJoineeComments(candidate.getJoineeComments());
            candidateInterviewTimelineDTO.setHoldSubStatus(candidate.getHoldSubStatus()==null?"N/A" : candidate.getHoldSubStatus());
        }
        if(candidate.getAddedDate()!=null) {
        candidateInterviewTimelineDTO.setAddedDate(candidate.getAddedDate().toString("dd MMM yyyy"));
        }
        if(candidate.getReason()!=null){
       
        String a = candidate.getReason();
        //a.replaceAll("<br/>","");
        candidateInterviewTimelineDTO.setReason(a);
        }
        
        candidateInterviewTimelineDTO.setCycleDTOs(candidateInterviewCycleDTOList);
        
        return candidateInterviewTimelineDTO;
    }

	public List<CandidateInterviewCycleDTO> convertDTO(List<CandidateInterviewCycle> candidates) {

		List<CandidateInterviewCycleDTO> dtoList = null;
		if(candidates !=null) {
			dtoList=new ArrayList<CandidateInterviewCycleDTO>();
			for(CandidateInterviewCycle details :candidates ) {
				CandidateInterviewCycleDTO cycle = new CandidateInterviewCycleDTO();
				
				cycle.setCandidateName(details.getCandidate().getFullName());
				cycle.setExperiance(details.getCandidate().getExperience());
				cycle.setMobileNumber(details.getCandidate().getMobile());
				cycle.setInterviewers(details.getInterviewers());
				cycle.setInterviewDate(details.getInterviewDate().toString("dd-MM-yyyy"));
				cycle.setInterviewTime(details.getInterviewTime());
				cycle.setInterviewRound(details.getInterviewRound().toString());
				cycle.setInterviewStatus(details.getCandidate().getCandidateInterviewStatus().toString());
				cycle.setCtc(details.getCandidate().getCtc());  
				cycle.setEctc(details.getCandidate().getEctc());
				cycle.setNp(details.getCandidate().getNp());
				cycle.setTechnology(details.getCandidate().getTechnology());
				cycle.setRecruiter(details.getCandidate().getRecruiter());
				if(details.getCandidate().getInterview().getInterviewType()!=null){
					System.out.println("in if");
					cycle.setInterviewMode(details.getCandidate().getInterview().getInterviewType().toString());
				}
			
				dtoList.add(cycle);
				
			}
		}
		
		return dtoList;
	
		
	}

	
		
	
}
