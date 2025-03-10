package com.raybiztech.recruitment.dto;

import java.io.Serializable;

import com.raybiztech.date.Date;


public class ScheduleDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2914065108905795593L;

    private Long id;

    private String scheduleTime;
    
    private Date scheduleDate;

    private String description;

    private AddressDTO venue;

    private InterviewDTO interviewDTO;


    public Long getId() {
        return id;
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

   

    public void setId(Long id) {
        this.id = id;
    }

  
    public void setDescription(String description) {
        this.description = description;
    }

    public AddressDTO getVenue() {
        return venue;
    }

    public void setVenue(AddressDTO venue) {
        this.venue = venue;
    }

    public InterviewDTO getInterviewDTO() {
        return interviewDTO;
    }

    public void setInterviewDTO(InterviewDTO interviewDTO) {
        this.interviewDTO = interviewDTO;
    }

    
   
}
