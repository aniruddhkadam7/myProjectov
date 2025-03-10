package com.raybiztech.meetingrequest.dto;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.meetingrequest.business.MeetingRequest;

public class MeetingAttendeesDto {

	private Long id;
	private Employee employee;
	private MeetingRequest meetingRequest;
	private String employeeAvailability;
	private String employeeName;
	private Long employeeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MeetingRequest getMeetingRequest() {
		return meetingRequest;
	}

	public void setMeetingRequest(MeetingRequest meetingRequest) {
		this.meetingRequest = meetingRequest;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getEmployeeAvailability() {
		return employeeAvailability;
	}

	public void setEmployeeAvailability(String employeeAvailability) {
		this.employeeAvailability = employeeAvailability;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

}
