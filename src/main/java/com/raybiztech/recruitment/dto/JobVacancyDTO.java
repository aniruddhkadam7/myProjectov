package com.raybiztech.recruitment.dto;

import java.io.Serializable;

public class JobVacancyDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5936552480046104045L;
    private Long id;
    private String jobCode;
    private String positionVacant;//title
    private String minimumExperience;
    private String description;
    private String opendDate;
    private String expiryDate;
    private Integer noOfRequirements;
    private Integer offered;
    private Integer remaining;
    private String status;

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getOpendDate() {
        return opendDate;
    }

    public void setOpendDate(String opendDate) {
        this.opendDate = opendDate;
    }

    public Integer getOffered() {
        return offered;
    }

    public void setOffered(Integer offered) {
        this.offered = offered;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}
