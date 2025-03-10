/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.dto;

import java.util.List;

import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

/**
 *
 * @author hari
 */
public class CandidateInterviewCycleDTO {

    private String candidateId;
    private String interviewers;
    private List<EmployeeDTO> interviewersDTOList;
    private String interviewDate;
    private String interviewTime;
    private String interviewComments;
    private String interviewRound;
    private String interviewStatus;
    private String candidateName;
    private String interviewMode;
    private Long interviewCycleId;
    private String experiance;
    private String rating;
    private String status;
    private String candiadateEmailId;
    private String skills;
    private String mobileNumber;
    private List<CandidateInterviewCycleDTO> cycleDTOs;
    private String interviewResultStatus;
	private String description;
	private String skypeId;
	private String proactiveComments;
	private String communicationComments;
	private String excellenceComments;
	private String updatedBy;
	private String recruiter;
	private String reason;
	private String ctc;
	private String ectc;
	private String technology;
	private String np;
	private String country;
	private String jobCode;
	private String sourceName;
	private String personId;
	
	
	
	
	
  public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    

    public String getMobileNumber() {
        return mobileNumber;
    }

    public List<EmployeeDTO> getInterviewersDTOList() {
        return interviewersDTOList;
    }

    public void setInterviewersDTOList(List<EmployeeDTO> interviewersDTOList) {
        this.interviewersDTOList = interviewersDTOList;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getCandiadateEmailId() {
        return candiadateEmailId;
    }

    public void setCandiadateEmailId(String candiadateEmailId) {
        this.candiadateEmailId = candiadateEmailId;
    }

    public Long getInterviewCycleId() {
        return interviewCycleId;
    }

    public void setInterviewCycleId(Long interviewCycleId) {
        this.interviewCycleId = interviewCycleId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getInterviewMode() {
        return interviewMode;
    }

    public void setInterviewMode(String interviewMode) {
        this.interviewMode = interviewMode;
    }

    public String getInterviewers() {
        return interviewers;
    }

    public void setInterviewers(String interviewers) {
        this.interviewers = interviewers;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }

    public String getInterviewDate() {
        return interviewDate;
    }

    public void setInterviewDate(String interviewDate) {
        this.interviewDate = interviewDate;
    }

    public String getInterviewTime() {
        return interviewTime;
    }

    public void setInterviewTime(String interviewTime) {
        this.interviewTime = interviewTime;
    }

    public String getInterviewComments() {
        return interviewComments;
    }

    public void setInterviewComments(String interviewComments) {
        this.interviewComments = interviewComments;
    }

    public String getInterviewRound() {
        return interviewRound;
    }

    public void setInterviewRound(String interviewRound) {
        this.interviewRound = interviewRound;
    }

    public String getInterviewStatus() {
        return interviewStatus;
    }

    public void setInterviewStatus(String interviewStatus) {
        this.interviewStatus = interviewStatus;
    }

    public String getExperiance() {
        return experiance;
    }

    public void setExperiance(String experiance) {
        this.experiance = experiance;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CandidateInterviewCycleDTO> getCycleDTOs() {
        return cycleDTOs;
    }

    public void setCycleDTOs(List<CandidateInterviewCycleDTO> cycleDTOs) {
        this.cycleDTOs = cycleDTOs;
    }

	public String getInterviewResultStatus() {
		return interviewResultStatus;
	}

	public void setInterviewResultStatus(String interviewResultStatus) {
		this.interviewResultStatus = interviewResultStatus;
	}

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getProactiveComments() {
		return proactiveComments;
	}

	public void setProactiveComments(String proactiveComments) {
		this.proactiveComments = proactiveComments;
	}

	public String getCommunicationComments() {
		return communicationComments;
	}

	public void setCommunicationComments(String communicationComments) {
		this.communicationComments = communicationComments;
	}

	public String getExcellenceComments() {
		return excellenceComments;
	}

	public void setExcellenceComments(String excellenceComments) {
		this.excellenceComments = excellenceComments;
	}

	public String getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(String recruiter) {
		this.recruiter = recruiter;
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

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getNp() {
		return np;
	}

	public void setNp(String np) {
		this.np = np;
	}


	
	

}
