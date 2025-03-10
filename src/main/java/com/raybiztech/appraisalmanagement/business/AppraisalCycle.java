package com.raybiztech.appraisalmanagement.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;

public class AppraisalCycle implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2067538939260780985L;
    private Long id;
    private String name;
    private String description;
    private DateRange configurationPeriod;
    private boolean active;
    private Set<AppraisalForm> appraisalForms;
    private String appraisalType;
    private String appraisalDuration;
    public Integer levelOfHierarchy;
    private DateRange appraisalPeriod;
    public Integer servicePeriod;
    private Long createdBy;
    private Long updatedBy;
    private Second createdDate;
    private Second updatedDate;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Set<AppraisalForm> getAppraisalForms() {
        return appraisalForms;
    }

    public void setAppraisalForms(Set<AppraisalForm> appraisalForms) {
        this.appraisalForms = appraisalForms;
    }

    public String getAppraisalType() {
        return appraisalType;
    }

    public void setAppraisalType(String appraisalType) {
        this.appraisalType = appraisalType;
    }

    public String getAppraisalDuration() {
        return appraisalDuration;
    }

    public void setAppraisalDuration(String appraisalDuration) {
        this.appraisalDuration = appraisalDuration;
    }

    public Integer getLevelOfHierarchy() {
        return levelOfHierarchy;
    }

    public void setLevelOfHierarchy(Integer levelOfHierarchy) {
        this.levelOfHierarchy = levelOfHierarchy;
    }

	public DateRange getConfigurationPeriod() {
		return configurationPeriod;
	}

	public void setConfigurationPeriod(DateRange configurationPeriod) {
		this.configurationPeriod = configurationPeriod;
	}

	public DateRange getAppraisalPeriod() {
		return appraisalPeriod;
	}

	public void setAppraisalPeriod(DateRange appraisalPeriod) {
		this.appraisalPeriod = appraisalPeriod;
	}

    public Integer getServicePeriod() {
        return servicePeriod;
    }

    public void setServicePeriod(Integer servicePeriod) {
        this.servicePeriod = servicePeriod;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Second getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Second createdDate) {
        this.createdDate = createdDate;
    }

    public Second getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Second updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    
}
