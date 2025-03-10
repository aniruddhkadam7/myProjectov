package com.raybiztech.meetingrequest.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;

public class MeetingAttendees implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7889433172688717694L;
    private Long id;
    private Employee employee;
    private MeetingRequest meetingRequest;
    private String employeeAvailability;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public MeetingRequest getMeetingRequest() {
        return meetingRequest;
    }

    public void setMeetingRequest(MeetingRequest meetingRequest) {
        this.meetingRequest = meetingRequest;
    }

	public String getEmployeeAvailability() {
		return employeeAvailability;
	}

	public void setEmployeeAvailability(String employeeAvailability) {
		this.employeeAvailability = employeeAvailability;
	}

	

}
