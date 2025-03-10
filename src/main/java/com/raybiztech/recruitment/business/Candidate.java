package com.raybiztech.recruitment.business;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.raybiztech.appraisals.business.Status;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.lookup.business.SourceLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.recruitment.exception.NoJobVacancyException;

public class Candidate extends Person implements Serializable {

    private static final long serialVersionUID = 8075382522652624666L;
    private String experience;
    private String skill;
    private JobVacancy appliedFor;
    private SourceLookUp sourcelookUp;
    private Set<Document> documentList;
    private String appliedForLookUp;
    private Interview interview;
    private Status candidateInterviewStatus;
    private Date candidateJoinDate;
    private String status;
    private String resumePath;
    private String otherDocumentPath;
    private String statusComments;
    private String timelineStatus;
    private String technology;
    private String joineeComments;
    private String recruiter;
    private String initialComments;
    private String holdSubStatus;
    private String statusChangeTime;
    private Second updatedDate;
    private String jobTypeName;
    private Date addedDate;
    private String pan;
    private String adhar;
    private String linkedin;
	private String addComments;
	private CountryLookUp country;
	private String Notifications;
	private Integer countryCode;
	
	
	
	public Integer getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(Integer countryCode) {
		this.countryCode = countryCode;
	}

public String getNotifications() {
		return Notifications;
	}

	public void setNotifications(String notifications) {
		Notifications = notifications;
	}

	public CountryLookUp getCountry() {
		return country;
	}

	public void setCountry(CountryLookUp country) {
		this.country = country;
	}

	public String getAddComments() {
		return addComments;
	}

	public void setAddComments(String addComments) {
		this.addComments = addComments;
	}



    public String getInitialComments() {
		return initialComments;
	}

	public void setInitialComments(String initialComments) {
		this.initialComments = initialComments;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Candidate() {
        documentList = new HashSet<Document>();
    }

    public Status getCandidateInterviewStatus() {
        return candidateInterviewStatus;
    }

    public void setCandidateInterviewStatus(Status candidateInterviewStatus) {
        this.candidateInterviewStatus = candidateInterviewStatus;
    }

    public Date getCandidateJoinDate() {
        return candidateJoinDate;
    }

    public void setCandidateJoinDate(Date candidateJoinDate) {
        this.candidateJoinDate = candidateJoinDate;
    }

    public String getExperience() {
        return experience;
    }

    public JobVacancy getAppliedFor() {
        return appliedFor;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setAppliedFor(JobVacancy appliedFor) {
        this.appliedFor = appliedFor;
    }

    public Set<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(Set<Document> documentList) {
        this.documentList = documentList;
    }

    public SourceLookUp getSourcelookUp() {
        return sourcelookUp;
    }

    public void setSourcelookUp(SourceLookUp sourcelookUp) {
        this.sourcelookUp = sourcelookUp;
    }

    public String getAppliedForLookUp() {
        return appliedForLookUp;
    }

    public void setAppliedForLookUp(String appliedForLookUp) {
        this.appliedForLookUp = appliedForLookUp;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public String getStatusComments() {
        return statusComments;
    }

    public void setStatusComments(String statusComments) {
        this.statusComments = statusComments;
    }

    public String getTimelineStatus() {
        return timelineStatus;
    }

    public void setTimelineStatus(String timelineStatus) {
        this.timelineStatus = timelineStatus;
    }
    
    public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	//
    public void applyForJobVacancy(JobVacancy jobVacancy) {
        if (jobVacancy.getNoOfRequirements() > 0) {
            this.setAppliedFor(jobVacancy);
        } else {
            throw new NoJobVacancyException("No Job Vacancy");
        }

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1997, 13).append(personId).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof Candidate) {
            Candidate candidate = (Candidate) obj;
            return new EqualsBuilder()
                    .append(personId, candidate.getPersonId()).isEquals();
        }
        return false;
    }

	public String getJoineeComments() {
		return joineeComments;
	}

	public void setJoineeComments(String joineeComments) {
		this.joineeComments = joineeComments;
	}

	public String getRecruiter() {
		return recruiter;
	}

	public void setRecruiter(String recruiter) {
		this.recruiter = recruiter;
	}

	public String getHoldSubStatus() {
		return holdSubStatus;
	}

	public void setHoldSubStatus(String holdSubStatus) {
		this.holdSubStatus = holdSubStatus;
	}

	public String getStatusChangeTime() {
		return statusChangeTime;
	}

	public void setStatusChangeTime(String statusChangeTime) {
		this.statusChangeTime = statusChangeTime;
	}

	public Second getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getAdhar() {
		return adhar;
	}

	public void setAdhar(String adhar) {
		this.adhar = adhar;
	}

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	
}
