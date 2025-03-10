package com.raybiztech.meetingrequest.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class MeetingRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7364386467783640982L;
	private Long id;
	private String agenda;
	private Room room;
	private Date fromDate;
	private Date toDate;
	private Second startTime;
	private Second endTime;
	private String projectName;
	private String authorName;
	private Long reservedBy;
	private Set<MeetingAttendees> meetingAttendees;
	private String meetingStatus;
	private String conferenceType;
	private EventType eventType;
	private Location location;
	private String eventLocation;
	private Long eventId;
	private String description;
	private Long trainerName;

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

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Second getStartTime() {
		return startTime;
	}

	public void setStartTime(Second startTime) {
		this.startTime = startTime;
	}

	public Second getEndTime() {
		return endTime;
	}

	public void setEndTime(Second endTime) {
		this.endTime = endTime;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Set<MeetingAttendees> getMeetingAttendees() {
		return meetingAttendees;
	}

	public void setMeetingAttendees(Set<MeetingAttendees> meetingAttendees) {
		this.meetingAttendees = meetingAttendees;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Long getReservedBy() {
		return reservedBy;
	}

	public void setReservedBy(Long reservedBy) {
		this.reservedBy = reservedBy;
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

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getTrainerName() {
		return trainerName;
	}

	public void setTrainerName(Long trainerName) {
		this.trainerName = trainerName;
	}

	

}
