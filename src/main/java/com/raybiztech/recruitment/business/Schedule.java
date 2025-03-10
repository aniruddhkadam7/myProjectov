package com.raybiztech.recruitment.business;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.date.Date;

public class Schedule implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8571443118577954631L;

    private Long scheduleId;

    private String scheduleTime;
    
    private Date scheduleDate;

    private String description;

    private Address venue;

    private Interview interview;
    
    private String contactDetails;
    
    

    public String getContactDetails() {
		return contactDetails;
	}

	public void setContactDetails(String contactDetails) {
		this.contactDetails = contactDetails;
	}

	public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

  
    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    

    public String getDescription() {
        return description;
    }

    public Address getVenue() {
        return venue;
    }

    public Interview getInterview() {
        return interview;
    }

    

  
    public void setDescription(String description) {
        this.description = description;
    }

    public void setVenue(Address venue) {
        this.venue = venue;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Schedule) {
            final Schedule schedule = (Schedule) obj;
            return new EqualsBuilder().append(scheduleId, schedule.getScheduleId()).isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 37).append(scheduleId).toHashCode();
    }

}
