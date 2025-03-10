package com.raybiztech.meetingrequest.dto;

import java.util.List;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.meetingrequest.business.Location;
import com.raybiztech.meetingrequest.business.MeetingAttendees;

public class MeetingRequestDto {

	private Long id;
	private String agenda;
	private Long roomId;
	private String roomName;
	private String locationName;
	private String fromDate;
	private String toDate;
	private String startTime;
	private String endTime;
	private String projectName;
	private List<Long> employeeIds;
	private SearchEmpDetailsDTO authorName;
	private List<String> employeeNames;
	private Boolean isAuthorisedUser;
	private Long locationId;
	private String employeeAvailability;
	private String timeFomrat;
	private Boolean disableEdit;
	private List<MeetingEditDTO> meetingEditDTOList;
	private List<String> meetingAttendeesDto;
	private List<Availability> availability;
	private String meetingStatus;
	private String conferenceType;
	private String eventTypeName;
	private Long eventTypeId;
	private String eventLocation;
	private Long eventId;
	private String description;
	private Boolean eventEditAccess;
	private List<String> empDesignations;
	private List<SearchEmpDetailsDTO> employeeDto;
	private SearchEmpDetailsDTO trainerName;

	// this is only used to carry available dates while booking EVENT (IMP)
	private List<String> availableDates;

	/* private List<EmployeeDTO> employeeDTOs; */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public List<Long> getEmployeeIds() {
		return employeeIds;
	}

	public void setEmployeeIds(List<Long> employeeIds) {
		this.employeeIds = employeeIds;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public SearchEmpDetailsDTO getAuthorName() {
		return authorName;
	}

	public void setAuthorName(SearchEmpDetailsDTO authorName) {
		this.authorName = authorName;
	}

	public List<String> getEmployeeNames() {
		return employeeNames;
	}

	public void setEmployeeNames(List<String> employeeNames) {
		this.employeeNames = employeeNames;
	}

	public Boolean getIsAuthorisedUser() {
		return isAuthorisedUser;
	}

	public void setIsAuthorisedUser(Boolean isAuthorisedUser) {
		this.isAuthorisedUser = isAuthorisedUser;
	}

	public Long getLocationId() {
		return locationId;
	}

	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	public String getEmployeeAvailability() {
		return employeeAvailability;
	}

	public void setEmployeeAvailability(String employeeAvailability) {
		this.employeeAvailability = employeeAvailability;
	}

	public String getTimeFomrat() {
		return timeFomrat;
	}

	public void setTimeFomrat(String timeFomrat) {
		this.timeFomrat = timeFomrat;
	}

	public Boolean getDisableEdit() {
		return disableEdit;
	}

	public void setDisableEdit(Boolean disableEdit) {
		this.disableEdit = disableEdit;
	}

	public List<MeetingEditDTO> getMeetingEditDTOList() {
		return meetingEditDTOList;
	}

	public void setMeetingEditDTOList(List<MeetingEditDTO> meetingEditDTOList) {
		this.meetingEditDTOList = meetingEditDTOList;
	}

	public List<String> getMeetingAttendeesDto() {
		return meetingAttendeesDto;
	}

	public void setMeetingAttendeesDto(List<String> meetingAttendeesDto) {
		this.meetingAttendeesDto = meetingAttendeesDto;
	}

	public List<Availability> getAvailability() {
		return availability;
	}

	public void setAvailability(List<Availability> availability) {
		this.availability = availability;
	}

	public String getMeetingStatus() {
		return meetingStatus;
	}

	public void setMeetingStatus(String meetingStatus) {
		this.meetingStatus = meetingStatus;
	}

	public String getConferenceType() {
		return conferenceType;
	}

	public void setConferenceType(String conferenceType) {
		this.conferenceType = conferenceType;
	}

	public String getEventTypeName() {
		return eventTypeName;
	}

	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}

	public Long getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(Long eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public String getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public List<String> getAvailableDates() {
		return availableDates;
	}

	public void setAvailableDates(List<String> availableDates) {
		this.availableDates = availableDates;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getEventEditAccess() {
		return eventEditAccess;
	}

	public void setEventEditAccess(Boolean eventEditAccess) {
		this.eventEditAccess = eventEditAccess;
	}

	public List<String> getEmpDesignations() {
		return empDesignations;
	}

	public void setEmpDesignations(List<String> empDesignations) {
		this.empDesignations = empDesignations;
	}

	public List<SearchEmpDetailsDTO> getEmployeeDto() {
		return employeeDto;
	}

	public void setEmployeeDto(List<SearchEmpDetailsDTO> employeeDto) {
		this.employeeDto = employeeDto;
	}

	public SearchEmpDetailsDTO getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(SearchEmpDetailsDTO trainerName) {
		this.trainerName = trainerName;
	}

	

	

	

	

	/*
	 * public List<EmployeeDTO> getEmployeeDTOs() { return employeeDTOs; }
	 * 
	 * public void setEmployeeDTOs(List<EmployeeDTO> employeeDTOs) {
	 * this.employeeDTOs = employeeDTOs; }
	 */
	
	
	

}
