package com.raybiztech.recruitment.business;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.date.Date;

public class JobVacancy implements Serializable,Cloneable{

    /**
     *
     */
    private static final long serialVersionUID = 2588183763078822369L;
    private Long jobVacancyId;
    public String jobCode;
    public String positionVacant;//title
    public String minimumExperience;
    public String description;
    public Date expiryDate;
    public Date openedDate;
    public Integer noOfRequirements;
    public Integer offered;
    public Boolean status;

    public JobVacancy() {

    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public Date getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(Date openedDate) {
        this.openedDate = openedDate;
    }

    public Integer getOffered() {
        return offered;
    }

    public void setOffered(Integer offered) {
        this.offered = offered;
    }

    public Long getJobVacancyId() {
        return jobVacancyId;
    }

    public void setJobVacancyId(Long jobVacancyId) {
        this.jobVacancyId = jobVacancyId;
    }

    public String getPositionVacant() {
        return positionVacant;
    }

    public void setPositionVacant(String positionVacant) {
        this.positionVacant = positionVacant;
    }

    public Integer getNoOfRequirements() {
        return noOfRequirements;
    }

    public void setNoOfRequirements(Integer noOfRequirements) {
        this.noOfRequirements = noOfRequirements;
    }

    public String getMinimumExperience() {
        return minimumExperience;
    }

    public void setMinimumExperience(String minimumExperience) {
        this.minimumExperience = minimumExperience;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 19).append(jobVacancyId).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JobVacancy) {
            final JobVacancy other = (JobVacancy) obj;
            return new EqualsBuilder().append(other.jobVacancyId, jobVacancyId).isEquals();
        } else {
            return false;
        }
    }

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}
}
