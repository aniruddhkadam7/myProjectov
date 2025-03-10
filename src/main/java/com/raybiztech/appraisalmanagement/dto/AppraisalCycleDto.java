package com.raybiztech.appraisalmanagement.dto;

public class AppraisalCycleDto {
	private Long id;

	private String name;

	private String description;
	//This is used as configuration start date
	private String toDate;
	//This is used as configuration end date
	private String fromDate;

	private boolean active;
        
        private String appraisalType;
        
        private String appraisalDuration;
        
        private Integer level;
        
        private Boolean cycleStartedFlag;
        
        private String appraisalStartDate;
        
        private String appraisalEndDate;
        private Integer servicePeriod;
        

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

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

    public Boolean getCycleStartedFlag() {
        return cycleStartedFlag;
    }

    public void setCycleStartedFlag(Boolean cycleStartedFlag) {
        this.cycleStartedFlag = cycleStartedFlag;
    }

	public String getAppraisalEndDate() {
		return appraisalEndDate;
	}

	public void setAppraisalEndDate(String appraisalEndDate) {
		this.appraisalEndDate = appraisalEndDate;
	}

	public String getAppraisalStartDate() {
		return appraisalStartDate;
	}

	public void setAppraisalStartDate(String appraisalStartDate) {
		this.appraisalStartDate = appraisalStartDate;
	}

    public Integer getServicePeriod() {
        return servicePeriod;
    }

    public void setServicePeriod(Integer servicePeriod) {
        this.servicePeriod = servicePeriod;
    }
        
}
