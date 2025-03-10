package com.raybiztech.meetingrequest.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.date.Date;
import com.raybiztech.meetingrequest.dto.EventTypeDto;
import com.raybiztech.meetingrequest.dto.LocationDto;
import com.raybiztech.meetingrequest.dto.MeetingRequestDto;
import com.raybiztech.meetingrequest.dto.ReservedTimingsForEvent;
import com.raybiztech.meetingrequest.dto.RoomDto;

public interface MeetingRequestService {

	void addLocation(LocationDto locationDto);

	void updateLocation(LocationDto locationDto);

	void deleteLocation(Long id);

	List<LocationDto> getAllMeetingLocations();

	void addRoom(RoomDto roomDto);

	void updateRoom(RoomDto roomDto);

	void deleteRoom(Long roomId);

	List<RoomDto> getAllMeetingRooms();

	List<RoomDto> getRoomsOfLocation(Long locationId);

	void confirmNewMeetingAppointment(MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest);

	List<MeetingRequestDto> getAllMeetingRequestAppointments(Date date,
			Long roomId, HttpServletRequest httpServletRequest);

	List<ReservedTimingsForEvent> getAllBookedDetailsForEvent(Date fromDate,
			Date toDate, Long roomId, HttpServletRequest httpServletRequest);

	List<MeetingRequestDto> getMeetingRequestDetailsWhileEdit(Long meetingId,
			Date date, Long roomId, HttpServletRequest httpServletRequest);

	public List<EmployeeDTO> getAttendiesProbability(String projectName);

	public void saveMeetingRequest(MeetingRequestDto meetingRequestDto);

	public void changeMeetingStatus(Long requestId, String meetingStatus);

	public Boolean uniqueAttendee(Long attendeeId, String attendeeName, String startTime,
			String endTime);

	public Boolean uniqueAttendeeWithId(Long attendeeId, String attendeeName, String startTime,
			String endTime, Long meetingRequestId);

	/*
	 * List<MeetingRequestListDto> getAllCustomizedMeetingRequestList(Date
	 * date);
	 */

	public SearchEmpDetailsDTO getDefaultAuthorName(Long employeeId);

	MeetingRequestDto editmeeting(Long id);

	String getserverdateandtime();

	void confirmupdateMeetingRoomRequest(MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest);

	Map<String, Object> checkRoomsAvailableBetweenDates(
			MeetingRequestDto meetingRequestDto,  HttpServletRequest httpServletRequest) throws ParseException;

	void addEventMeetingOnGivenDates(List<String> availableDates,
			MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest) throws ParseException;

	/*
	 * List<String> addNewEvent(MeetingRequestDto meetingRequestDto) throws
	 * ParseException;
	 */

	void UpdateMeetingRequest(MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest);

	void confirmupdateMeetingRoomRequestAfterValidation(
			MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest);

	public Boolean timechecking(String time);

	public List<MeetingRequestDto> getBookingsForSelection(Long location,
			Long room, String status, String meetingstatus, HttpServletRequest httpServletRequest);

	void updateLocationForMeetings();

	public List<EventTypeDto> getAllEventTypes();

	public void addEventType(EventTypeDto dto);

	public void updateEventType(EventTypeDto dto);

	public void deleteEventType(Long eventTypeId);

	public Map<String,Object> getAllEvents(Integer startIndex, Integer endIndex, String dateSelection,
			String searchFromDate, String searchToDate, Long eventTypeId, HttpServletRequest httpServletRequest);

	public List<MeetingRequestDto> getEventsForDashBoard(HttpServletRequest httpServletRequest);

	public List<MeetingRequestDto> getTrainingsForDashBoard(HttpServletRequest httpServletRequest);

	public void cancelEvent(Long eventId);

	public void editEvent(MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest);

	List<Object> getMeetingRequestsForEmployee();

	void uploadFeedbackForm(MultipartFile mpf, String eventId);

	Map<String, Object> getFeedbackForms(Integer startIndex, Integer endIndex, Long eventId);

	void downloadFeedbackForm(String fileName, HttpServletResponse response);
}
