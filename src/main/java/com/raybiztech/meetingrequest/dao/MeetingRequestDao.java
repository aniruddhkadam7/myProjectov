package com.raybiztech.meetingrequest.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.meetingrequest.business.EventType;
import com.raybiztech.meetingrequest.business.Location;
import com.raybiztech.meetingrequest.business.MeetingAttendees;
import com.raybiztech.meetingrequest.business.MeetingRequest;
import com.raybiztech.meetingrequest.business.Room;
import com.raybiztech.meetingrequest.dto.MeetingRequestListDto;
import com.raybiztech.meetingrequest.dto.MeetingAttendeesDto;
import com.raybiztech.meetingrequest.dto.MeetingRequestDto;

public interface MeetingRequestDao extends DAO {

	public List<AllocationDetails> attendiesOfProjectList(Long id);

	List<Room> getAllRoomsOfLocation(Long loactionId);

	public List<MeetingAttendees> getAttendees(Long meetingRequestId);

	public List<MeetingRequest> getTimeForSelectedLocation(MeetingRequestDto dto);

	public List<MeetingRequest> getMeetingsFor(Date date, Room room);

	public List<MeetingRequest> getAllMeetingRequests(Date date, Room room);

	public List<MeetingRequest> getAllMeetingRequestsWhileEdit(Long meetingId,
			Date date, Room room);

	public List<MeetingAttendees> changeMeetingStatus(Long id);

	public List<MeetingRequest> uniqueAttendee(Second startTime, Second endTime);

	public List<MeetingRequest> getBookingsForSelection(Location location,
			Room room, Date date, String meetingstatus);

	public Map<String, Object> getAllEvents(Integer startIndex, Integer endIndex,DateRange dateRange,
			EventType eventType);

	public List<MeetingRequest> getEventsForDashBoard();

	public List<MeetingRequest> getTrainingsForDashBoard();

	public List<MeetingRequest> getAllNewStatusMeetingRequest(
			Location location, Room room, Date date);
	
	List<Object> getMeetingRequestsForEmp(Long employeeId);

	public Map<String, Object> getFeedbackFormMap(Integer startIndex, Integer endIndex, Long eventId);

	/*
	 * public List<MeetingRequestListDto> getAllcustomizedMeetingList(Date
	 * date);
	 */
}
