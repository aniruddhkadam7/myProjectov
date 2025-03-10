package com.raybiztech.meetingrequest.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.meetingrequest.dto.EventTypeDto;
import com.raybiztech.meetingrequest.dto.LocationDto;
import com.raybiztech.meetingrequest.dto.MeetingRequestDto;
import com.raybiztech.meetingrequest.dto.ReservedTimingsForEvent;
import com.raybiztech.meetingrequest.dto.RoomDto;
import com.raybiztech.meetingrequest.exception.CrossingTimeException;
import com.raybiztech.meetingrequest.exception.MeetingRoomAlreadyBookedException;
import com.raybiztech.meetingrequest.service.MeetingRequestService;
import com.raybiztech.recruitment.utils.DateParser;

@Controller
@RequestMapping("/meetingRequest")
public class MeetingRequestController {

	Logger logger = Logger.getLogger(MeetingRequestController.class);

	@Autowired
	MeetingRequestService meetingRequestServiceImpl;
	@Autowired
	SecurityUtils securityUtils;

	@RequestMapping(value = "/addLocation", method = RequestMethod.POST)
	public @ResponseBody void addLocation(@RequestBody LocationDto locationDto) {
		meetingRequestServiceImpl.addLocation(locationDto);
	}

	@RequestMapping(value = "/updateLocation", method = RequestMethod.PUT)
	public @ResponseBody void updateLocation(
			@RequestBody LocationDto locationDto) {
		meetingRequestServiceImpl.updateLocation(locationDto);
	}

	@RequestMapping(value = "/deleteLocation", params = { "locationId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteLocation(@RequestParam Long locationId) {
		meetingRequestServiceImpl.deleteLocation(locationId);
	}

	@RequestMapping(value = "/getAllMeetingLocations", method = RequestMethod.GET)
	public @ResponseBody List<LocationDto> getAllMeetingLocations() {
		return meetingRequestServiceImpl.getAllMeetingLocations();
	}

	@RequestMapping(value = "/addRoom", method = RequestMethod.POST)
	public @ResponseBody void addRoom(@RequestBody RoomDto roomDto) {
		meetingRequestServiceImpl.addRoom(roomDto);
	}

	@RequestMapping(value = "/updateRoom", method = RequestMethod.PUT)
	public @ResponseBody void updateRoom(@RequestBody RoomDto roomDto) {
		meetingRequestServiceImpl.updateRoom(roomDto);
	}

	@RequestMapping(value = "/deleteRoom", params = { "id" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteRoom(@RequestParam Long id) {
		meetingRequestServiceImpl.deleteRoom(id);
	}

	@RequestMapping(value = "/getAllMeetingRooms", method = RequestMethod.GET)
	public @ResponseBody List<RoomDto> getAllMeetingRooms() {
		return meetingRequestServiceImpl.getAllMeetingRooms();
	}

	@RequestMapping(value = "/confirmNewMeetingAppointment", method = RequestMethod.POST)
	public @ResponseBody void confirmNewMeetingAppointment(
			@RequestBody MeetingRequestDto meetingRequestDto,
			HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
		try {
			meetingRequestServiceImpl
					.confirmNewMeetingAppointment(meetingRequestDto,httpServletRequest);
		} catch (MeetingRoomAlreadyBookedException me) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);

		}
	}

	@RequestMapping(value = "/addNewEvent", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> addNewEvent(
			@RequestBody MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest)
			throws ParseException {

		return meetingRequestServiceImpl
				.checkRoomsAvailableBetweenDates(meetingRequestDto, httpServletRequest);
	}

	@RequestMapping(value = "/updateEvent", method = RequestMethod.POST)
	public @ResponseBody void updateEvent(
			@RequestBody MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest) {
		meetingRequestServiceImpl.editEvent(meetingRequestDto,httpServletRequest);
	}

	@RequestMapping(value = "/addEventOnAvailableDates", method = RequestMethod.POST)
	public @ResponseBody void addEventOnAvailableDates(
			@RequestBody MeetingRequestDto meetingRequestDto,HttpServletRequest httpServletRequest)
			throws ParseException {

		List<String> availabledates = meetingRequestDto.getAvailableDates();
		meetingRequestServiceImpl.addEventMeetingOnGivenDates(availabledates,
				meetingRequestDto, httpServletRequest);

	}

	@RequestMapping(value = "/confirmupdateMeetingRequest", method = RequestMethod.POST)
	public @ResponseBody void updateMeetingRequest(
			@RequestBody MeetingRequestDto meetingRequestDto,
			HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest) {
		try {
			meetingRequestServiceImpl
					.confirmupdateMeetingRoomRequest(meetingRequestDto,httpServletRequest);
		} catch (MeetingRoomAlreadyBookedException me) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);

		} catch (CrossingTimeException ce) {
			httpServletResponse
					.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
		}
	}

	@RequestMapping(value = "/getRoomsOfLocation", params = { "locationId" }, method = RequestMethod.GET)
	public @ResponseBody List<RoomDto> getRoomsOfLocation(
			@RequestParam Long locationId) {
		return meetingRequestServiceImpl.getRoomsOfLocation(locationId);
	}

	@RequestMapping(value = "/getAllAttendies", method = RequestMethod.GET)
	public @ResponseBody List<EmployeeDTO> getAttendiesProbability(
			@RequestParam String projectName) {
		return meetingRequestServiceImpl.getAttendiesProbability(projectName);
	}

	@RequestMapping(value = "/getLoggedEmployeeName", method = RequestMethod.GET)
	public @ResponseBody SearchEmpDetailsDTO getLoggedInEmployeeName() {

		Long empId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		return meetingRequestServiceImpl.getDefaultAuthorName(empId);
	}

	@RequestMapping(value = "/getAllMeetingAppointmentList", params = { "date",
			"roomid" }, method = RequestMethod.GET)
	public @ResponseBody List<MeetingRequestDto> getAllMeetingAppointmentList(
			@RequestParam String date, Long roomid,  HttpServletRequest httpServletRequest) {
		Date parsedDate = null;

		try {
			if (date.equalsIgnoreCase("today")) {
				parsedDate = new Date();
			} else if (date.equalsIgnoreCase("tomorrow")) {
				parsedDate = new Date().next();
			} else {
				parsedDate = DateParser.toDate(date);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return meetingRequestServiceImpl.getAllMeetingRequestAppointments(
				parsedDate, roomid, httpServletRequest);
	}

	@RequestMapping(value = "/getAllMeetingAppointmentListWhileEdit", params = {
			"meetingId", "date", "roomid" }, method = RequestMethod.GET)
	public @ResponseBody List<MeetingRequestDto> getAllMeetingAppointmentListWhileEdit(
			@RequestParam Long meetingId, String date, Long roomid, HttpServletRequest httpServletRequest) {
		Date parsedDate = null;

		try {
			parsedDate = DateParser.toDate(date);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return meetingRequestServiceImpl.getMeetingRequestDetailsWhileEdit(
				meetingId, parsedDate, roomid, httpServletRequest);
	}

	@RequestMapping(value = "/getAllBookedDetailsForEvent", params = {
			"fromdate", "toDate", "roomid" }, method = RequestMethod.GET)
	public @ResponseBody List<ReservedTimingsForEvent> getAllBookedDetailsForEvent(
			@RequestParam String fromdate, String toDate, Long roomid, HttpServletRequest httpServletRequest) {
		Date parsedFromdate = null;
		Date parsedToDate = null;

		try {
			parsedFromdate = DateParser.toDate(fromdate);
			parsedToDate = DateParser.toDate(toDate);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return meetingRequestServiceImpl.getAllBookedDetailsForEvent(
				parsedFromdate, parsedToDate, roomid, httpServletRequest);
	}

	@RequestMapping(value = "/changeMeetingStatus/{requestId}/{meetingStatus}", method = RequestMethod.PUT)
	public @ResponseBody void changeMeetingStatus(@PathVariable Long requestId,
			@PathVariable String meetingStatus,
			HttpServletResponse httpServletResponse) {

		meetingRequestServiceImpl.changeMeetingStatus(requestId, meetingStatus);
	}

	@RequestMapping(value = "/uniqueAttendee", params = { "attendeeId","attendeeName",
			"startTime", "endTime" }, method = RequestMethod.GET)
	public @ResponseBody void uniqueAttendee(@RequestParam Long attendeeId, @RequestParam String attendeeName,
			String startTime, String endTime,
			HttpServletResponse httpServletResponse) {
		String finishtime = null;
		String[] splitString = endTime.split("/");

		Boolean uniqueFlag = meetingRequestServiceImpl.uniqueAttendee(
				attendeeId, attendeeName, startTime, endTime);

		if (uniqueFlag == true) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "/uniqueAttendeeuniqueAttendeeWithId", params = {
			"attendeeId","attendeeName", "startTime", "endTime", "meetingRequestId" }, method = RequestMethod.GET)
	public @ResponseBody void uniqueAttendeeuniqueAttendeeWithId(
			@RequestParam Long attendeeId, @RequestParam String attendeeName, String startTime, String endTime,
			Long meetingRequestId, HttpServletResponse httpServletResponse) {
		String finishtime = null;
		String[] splitString = endTime.split("/");

		Boolean uniqueFlag = meetingRequestServiceImpl.uniqueAttendeeWithId(
				attendeeId,attendeeName, startTime, endTime, meetingRequestId);

		if (uniqueFlag == true) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	}

	@RequestMapping(value = "/editmeeting", params = { "id" }, method = RequestMethod.GET)
	public @ResponseBody MeetingRequestDto editmeeting(@RequestParam Long id) {
		return meetingRequestServiceImpl.editmeeting(id);
	}

	@RequestMapping(value = "/timechecking", params = { "time" }, method = RequestMethod.GET)
	public @ResponseBody Boolean timechecking(@RequestParam String time) {

		return meetingRequestServiceImpl.timechecking(time);
	}

	@RequestMapping(value = "/getBookingsForSelection", params = { "location",
			"room", "status", "meetingStatus" }, method = RequestMethod.GET)
	public @ResponseBody List<MeetingRequestDto> getBookingsForSelection(
			@RequestParam Long location, Long room, String status,
			String meetingStatus, HttpServletRequest httpServletRequest) {

		return meetingRequestServiceImpl.getBookingsForSelection(location,
				room, status, meetingStatus, httpServletRequest);
	}

	@RequestMapping(value = "/getAllEvents", params = {"startIndex", "endIndex", "dateSelection",
			"searchFromDate", "searchToDate", "eventTypeId" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllEvents(@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam String dateSelection, String searchFromDate,
			String searchToDate, Long eventTypeId, HttpServletRequest httpServletRequest) {
		return meetingRequestServiceImpl.getAllEvents(startIndex,endIndex,dateSelection,
				searchFromDate, searchToDate, eventTypeId, httpServletRequest);
	}

	@RequestMapping(value = "/getEventsForDashBoard", method = RequestMethod.GET)
	public @ResponseBody List<MeetingRequestDto> getEventsForDashBoard(HttpServletRequest httpServletRequest) {
		return meetingRequestServiceImpl.getEventsForDashBoard(httpServletRequest);
	}

	@RequestMapping(value = "/getTrainingsForDashBoard", method = RequestMethod.GET)
	public @ResponseBody List<MeetingRequestDto> getTrainingsForDashBoard(HttpServletRequest httpServletRequest) {
		return meetingRequestServiceImpl.getTrainingsForDashBoard(httpServletRequest);
	}

	@RequestMapping(value = "/cancelEvent", params = { "eventId" }, method = RequestMethod.PUT)
	public @ResponseBody void cancelEvent(@RequestParam Long eventId) {
		meetingRequestServiceImpl.cancelEvent(eventId);
	}

	@RequestMapping(value = "/updateLocationForMeetings", method = RequestMethod.POST)
	public @ResponseBody void updateLocationForMeetings() {
		// meetingRequestServiceImpl.addLocation(locationDto);
		meetingRequestServiceImpl.updateLocationForMeetings();
	}

	// Events

	@RequestMapping(value = "/getAllEventTypes", method = RequestMethod.GET)
	public @ResponseBody List<EventTypeDto> getAllEventTypes() {
		return meetingRequestServiceImpl.getAllEventTypes();
	}

	@RequestMapping(value = "/addEventType", method = RequestMethod.POST)
	public @ResponseBody void addEventType(
			@RequestBody EventTypeDto eventTypeDto) {
		meetingRequestServiceImpl.addEventType(eventTypeDto);
	}

	@RequestMapping(value = "/updateEventType", method = RequestMethod.PUT)
	public @ResponseBody void updateEventType(
			@RequestBody EventTypeDto eventTypeDto) {
		meetingRequestServiceImpl.updateEventType(eventTypeDto);
	}

	@RequestMapping(value = "/deleteEventType", params = { "eventTypeId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deteleEventType(@RequestParam Long eventTypeId) {
		meetingRequestServiceImpl.deleteEventType(eventTypeId);
	}

	@RequestMapping(value = "/getMeetingsForEmployeeToday", method = RequestMethod.GET)
	public @ResponseBody List<Object> getMeetingsForEmployeeToday() {
		return meetingRequestServiceImpl.getMeetingRequestsForEmployee();
	}
	
	@RequestMapping(value = "/getFeedbackFormList", params = {"startIndex", "endIndex", "eventId"}, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getFeedbackForms(@RequestParam Integer startIndex, @RequestParam Integer endIndex, @RequestParam Long eventId) {
		return meetingRequestServiceImpl.getFeedbackForms(startIndex, endIndex, eventId);
	}
	
	@RequestMapping(value="/downloadFeedbackForm", params = {"fileName"}, method = RequestMethod.GET)
	public @ResponseBody void downloadFeedbackForm(@RequestParam String fileName,HttpServletResponse response) {
		meetingRequestServiceImpl.downloadFeedbackForm(fileName, response);
	}
}
