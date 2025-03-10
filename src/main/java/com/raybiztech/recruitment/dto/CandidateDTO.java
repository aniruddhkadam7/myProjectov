package com.raybiztech.recruitment.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

public class CandidateDTO extends PersonDTO implements Serializable {

    private static final long serialVersionUID = 8075382522652624666L;
    private Long id;
    private String experience;
    private String skills;
    private JobVacancyDTO appliedFor;
    private SourceLookUpDTO sourcelookUp;
    private Set<DocumentDTO> documentList;
    private String skillData;
    private String appliedForVacancy;
    private String resumePath;
    private String otherDocumentPath;
    private String cadidateInterviewStatus;
    private String recruiter;
    private String technology;
    private String updatedDate;
    private Date addedDate;
    private Integer countryId;
    private CountryLookUp country;
    
    

	public Integer getCountryId() {
		return countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	public CountryLookUp getCountry() {
		return country;
	}

	public void setCountry(CountryLookUp country) {
		this.country = country;
	}

	public String getOtherDocumentPath() {
        return otherDocumentPath;
    }

    public void setOtherDocumentPath(String otherDocumentPath) {
        this.otherDocumentPath = otherDocumentPath;
    }

    public String getResumePath() {
        return resumePath;
    }

    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }

    public CandidateDTO() {
        documentList = new HashSet<DocumentDTO>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public JobVacancyDTO getAppliedFor() {
        return appliedFor;
    }

    public void setAppliedFor(JobVacancyDTO appliedFor) {
        this.appliedFor = appliedFor;
    }

    public SourceLookUpDTO getSourcelookUp() {
        return sourcelookUp;
    }

    public void setSourcelookUp(SourceLookUpDTO sourcelookUp) {
        this.sourcelookUp = sourcelookUp;
    }

    public Set<DocumentDTO> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(Set<DocumentDTO> documentList) {
        this.documentList = documentList;
    }

    public String getSkillData() {
        return skillData;
    }

    public void setSkillData(String skillData) {
        this.skillData = skillData;
    }

    public String getAppliedForVacancy() {
        return appliedForVacancy;
    }

    public void setAppliedForVacancy(String appliedForVacancy) {
        this.appliedForVacancy = appliedForVacancy;
    }

    public String getCadidateInterviewStatus() {
        return cadidateInterviewStatus;
    }

    public void setCadidateInterviewStatus(String cadidateInterviewStatus) {
        this.cadidateInterviewStatus = cadidateInterviewStatus;
    }

	public String getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(String recruiter) {
		this.recruiter = recruiter;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	
}
