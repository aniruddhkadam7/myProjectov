package com.raybiztech.meetingrequest.service;

import java.net.URI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jms.JMSException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.fortuna.ical4j.model.property.Organizer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.appraisals.exception.FileUploaderUtilException;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.appraisals.utils.FileUploaderUtil;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import com.raybiztech.mail.sender.MessageSender;
import com.raybiztech.mailtemplates.util.ConferenceRoomMailConfiguration;
import com.raybiztech.meetingrequest.builder.EventTypeBuilder;
import com.raybiztech.meetingrequest.builder.LocationBuilder;
import com.raybiztech.meetingrequest.builder.MeetingRequestBuilder;
import com.raybiztech.meetingrequest.builder.RoomBuilder;
import com.raybiztech.meetingrequest.business.EventType;
import com.raybiztech.meetingrequest.business.FeedbackForm;
import com.raybiztech.meetingrequest.business.Location;
import com.raybiztech.meetingrequest.business.MeetingAttendees;
import com.raybiztech.meetingrequest.business.MeetingRequest;
import com.raybiztech.meetingrequest.business.Room;
import com.raybiztech.meetingrequest.dao.MeetingRequestDao;
import com.raybiztech.meetingrequest.dto.Availability;
import com.raybiztech.meetingrequest.dto.EventTypeDto;
import com.raybiztech.meetingrequest.dto.FeedbackFormDto;
import com.raybiztech.meetingrequest.dto.LocationDto;
import com.raybiztech.meetingrequest.dto.MeetingEditDTO;
import com.raybiztech.meetingrequest.dto.MeetingRequestDto;
import com.raybiztech.meetingrequest.dto.ReservedTimingsForEvent;
import com.raybiztech.meetingrequest.dto.RoomDto;
import com.raybiztech.meetingrequest.exception.CrossingTimeException;
import com.raybiztech.meetingrequest.exception.MeetingRoomAlreadyBookedException;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.recruitment.business.Holidays;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.recruitment.utils.FileUploaderUtility;
import com.raybiztech.recruitment.utils.MailSenderUtility;
import com.raybiztech.utils.SecondParser;

@Service("meetingRequestServiceImpl")
public class MeetingRequestServiceImpl implements MeetingRequestService {

	Logger logger = Logger.getLogger(MeetingRequestServiceImpl.class);

	private final MeetingRequestDao meetingRequestDaoImpl;
	private final LocationBuilder locationBuilder;
	private final RoomBuilder roomBuilder;
	private final MeetingRequestBuilder meetingRequestBuilder;
	private final EmployeeBuilder employeeBuilder;
	private final EventTypeBuilder eventTypeBuilder;

	@Autowired
	MessageSender messageSender;
	@Autowired
	PropBean propBean;
	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	ConferenceRoomMailConfiguration conferenceRoomMailConfiguration;
	@Autowired
	MailSenderUtility mailSenderUtility;

	@Autowired
	public MeetingRequestServiceImpl(MeetingRequestDao meetingRequestDaoImpl,
			LocationBuilder locationBuilder, RoomBuilder roomBuilder,
			MeetingRequestBuilder meetingRequestBuilder,
			EmployeeBuilder employeeBuilder, EventTypeBuilder eventTypeBuilder) {
		this.meetingRequestDaoImpl = meetingRequestDaoImpl;
		this.locationBuilder = locationBuilder;
		this.roomBuilder = roomBuilder;
		this.meetingRequestBuilder = meetingRequestBuilder;
		this.employeeBuilder = employeeBuilder;
		this.eventTypeBuilder = eventTypeBuilder;
	}

	@Override
	public void addLocation(LocationDto locationDto) {

		Location location = locationBuilder.toEntity(locationDto);
		meetingRequestDaoImpl.save(location);

	}

	@Override
	public void deleteLocation(Long id) {

		meetingRequestDaoImpl.delete(meetingRequestDaoImpl.findBy(
				Location.class, id));

	}

	@Override
	public List<LocationDto> getAllMeetingLocations() {

		return locationBuilder.convertEntityListToDtoList(meetingRequestDaoImpl
				.get(Location.class));

	}

	@Override
	public void addRoom(RoomDto roomDto) {

		// Location location = meetingRequestDaoImpl.findBy(Location.class,
		// roomDto.getLocationId());
		Room room = roomBuilder.toEntity(roomDto);
		// room.setLocation(location);
		meetingRequestDaoImpl.save(room);

	}

	@Override
	public void deleteRoom(Long roomId) {

		meetingRequestDaoImpl.delete(meetingRequestDaoImpl.findBy(Room.class,
				roomId));

	}

	@Override
	public List<RoomDto> getAllMeetingRooms() {

		List<Room> rooms = meetingRequestDaoImpl.get(Room.class);
		List<RoomDto> roomDto = roomBuilder.convertEntityListToDtoList(rooms);
		return roomDto;

	}

	@Override
	public List<RoomDto> getRoomsOfLocation(Long locationId) {
		List<Room> rooms = meetingRequestDaoImpl
				.getAllRoomsOfLocation(locationId);
		return roomBuilder.convertEntityListToDtoList(rooms);
	}

	@Override
	public synchronized void confirmNewMeetingAppointment(
			MeetingRequestDto meetingRequestDto,HttpServletRequest httpServletRequest) {

		// Meeting Request Timings//
		String start[] = meetingRequestDto.getStartTime().split("/");
		String end[] = meetingRequestDto.getEndTime().split("/");

		int startTimeMilliSeconds = Integer.parseInt(start[3]) * 3600000
				+ Integer.parseInt(start[4]) * 60000;
		int endTimeMilliSeconds = Integer.parseInt(end[3]) * 3600000
				+ Integer.parseInt(end[4]) * 60000;

		// Getting Details of already Reserved Meeting of Loaction from Dto
		// for both the tenants
		
		List<MeetingRequest> meetingRequests = new ArrayList<>();
		
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
				TenantContextHolder.setTenantType(type);
			meetingRequests.addAll(meetingRequestDaoImpl
				.getTimeForSelectedLocation(meetingRequestDto));
		}

		// this will give weather meeting loaction is busy or not

		// checkRoomReserved method is used at many places eg:Events
		Boolean roomStatus = !checkRoomReserved(startTimeMilliSeconds,
				endTimeMilliSeconds, meetingRequests);
		if (roomStatus) {
			//set the tenant so that room booking is created for respective tenant
			TenantContextHolder.setTenantType(TenantTypes.valueOf(httpServletRequest.getHeader("tenantKey")));
			// logger.warn("Its not reserved.So you can book it");
			saveMeetingRequest(meetingRequestDto);
			conferenceRoomMailConfiguration
					.sendMeetingConfirmationMail(meetingRequestDto);

			/*
			 * try { sendMeetingRequestMail(meetingRequestDto); } catch
			 * (Exception e) { e.printStackTrace(); }
			 */

		} else {
			// logger.warn("You cant book a room which was already reserved");
			throw new MeetingRoomAlreadyBookedException(
					"Room was already reserved");
		}

	}

	@Override
	public void saveMeetingRequest(MeetingRequestDto meetingRequestDto) {

		Long meetingRequestId = (Long) meetingRequestDaoImpl
				.save(meetingRequestBuilder.toEntity(meetingRequestDto));
		MeetingRequest meetingReq = meetingRequestDaoImpl.findBy(
				MeetingRequest.class, meetingRequestId);
		// List<Long> empIds = meetingRequestDto.getEmployeeIds();
		List<Availability> empIds = meetingRequestDto.getAvailability();
		MeetingAttendees meetingAttendees = null;
		for (Availability empId : empIds) {
			meetingAttendees = new MeetingAttendees();
			meetingAttendees.setMeetingRequest(meetingReq);
			Employee employee = meetingRequestDaoImpl.findBy(Employee.class,
					empId.getId());
			meetingAttendees.setEmployee(employee);
			meetingAttendees.setEmployeeAvailability(empId.getAvailability());
			// meetingAttendeeses.add(meetingAttendees);
			meetingRequestDaoImpl.save(meetingAttendees);

		}

		try {
			if (empIds.size() > 0) {
				send(meetingRequestDto, meetingRequestId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// EVENT FUNCTIONALITY METHODS STARTS HERE/

	// Gives all Date and time related details
	public Map<String, Object> getDateandTimeDetails(
			MeetingRequestDto meetingRequestDto) throws ParseException {

		Map<String, Object> details = new HashMap<String, Object>();

		Date fromDate = DateParser.toDate(meetingRequestDto.getFromDate());
		Date toDate = DateParser.toDate(meetingRequestDto.getToDate());

		String start[] = meetingRequestDto.getStartTime().split("/");
		String end[] = meetingRequestDto.getEndTime().split("/");

		int startTimeMilliSeconds = Integer.parseInt(start[3]) * 3600000
				+ Integer.parseInt(start[4]) * 60000;
		int endTimeMilliSeconds = Integer.parseInt(end[3]) * 3600000
				+ Integer.parseInt(end[4]) * 60000;

		Room room = dao.findBy(Room.class, meetingRequestDto.getRoomId());

		details.put("fromDate", fromDate);
		details.put("toDate", toDate);
		details.put("startTimeMilliSeconds", startTimeMilliSeconds);
		details.put("endTimeMilliSeconds", endTimeMilliSeconds);
		details.put("startarray", start);
		details.put("endArray", end);
		details.put("room", room);

		return details;
	}

	// While Booking event
	@Override
	public Map<String, Object> checkRoomsAvailableBetweenDates(
			MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest) throws ParseException {

		Map<String, Object> availabiltyDetails = null;
		List<String> availableDates = null;
		List<String> notAvailableDates = null;

		// IF room is not selected or custom place is given directly add event
		// else check availabilty
		if (meetingRequestDto.getRoomId() != null) {

			availabiltyDetails = new HashMap<String, Object>();
			availableDates = new ArrayList<String>();
			notAvailableDates = new ArrayList<String>();

			// gives time and daterange details of event
			Map<String, Object> details = getDateandTimeDetails(meetingRequestDto);

			Date fromDate = (Date) details.get("fromDate");
			Date toDate = (Date) details.get("toDate");
			int startTimeMilliSeconds = (int) details
					.get("startTimeMilliSeconds");
			int endTimeMilliSeconds = (int) details.get("endTimeMilliSeconds");
			Room room = (Room) details.get("room");

			// gives WORKING DAYS between given date range
			List<Date> dateRange = getDatesBetweenDateRange(fromDate, toDate);

			for (Date date : dateRange) {

				List<MeetingRequest> meetingRequests = meetingRequestDaoImpl
						.getMeetingsFor(date, room);
				// Gives room resrved details on DATE,ROOM,TIME selected
				if (!checkRoomReserved(startTimeMilliSeconds,
						endTimeMilliSeconds, meetingRequests)) {
					availableDates.add(date.toString("dd/MM/yyyy"));
				} else {
					notAvailableDates.add(date.toString("dd/MM/yyyy"));
				}

			}

			if (notAvailableDates.isEmpty()) {
				addEventMeetingOnGivenDates(availableDates, meetingRequestDto, httpServletRequest);

			} else {
				availabiltyDetails.put("availableDates", availableDates);
				availabiltyDetails.put("notAvailableDates", notAvailableDates);
				return availabiltyDetails;
			}

		} else {
			addEvent(meetingRequestDto, httpServletRequest);
		}

		return availabiltyDetails;
	}

	// Add Individual Meetings For Event for Date Range
	public void addEventMeetingOnGivenDates(List<String> availableDates,
			MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest) throws ParseException {
		
		if(availableDates.size() > 0) {
		Map<String, Object> details = getDateandTimeDetails(meetingRequestDto);
		Room room = (Room) details.get("room");
		String[] start = (String[]) details.get("startarray");
		String[] end = (String[]) details.get("endArray");

		Long eventId = addEvent(meetingRequestDto, httpServletRequest);

		for (String date : availableDates) {
			addMeetingRequestForEvent(DateParser.toDate(date), room, eventId,
					meetingRequestDto, start, end, httpServletRequest);

		}
		}

	}

	// Add Event
	public Long addEvent(MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest) {
		
		TenantContextHolder.setTenantType(TenantTypes.valueOf(httpServletRequest.getHeader("tenantKey")));

		Long eventId = (Long) meetingRequestDaoImpl.save(meetingRequestBuilder
				.toEntity(meetingRequestDto));

		List<Availability> empIds = meetingRequestDto.getAvailability();
		if (!empIds.isEmpty()) {
			MeetingAttendees meetingAttendees = null;
			for (Availability empId : empIds) {
				meetingAttendees = new MeetingAttendees();
				meetingAttendees.setMeetingRequest(dao.findBy(
						MeetingRequest.class, eventId));
				Employee employee = meetingRequestDaoImpl.findBy(
						Employee.class, empId.getId());
				meetingAttendees.setEmployee(employee);
				meetingAttendees.setEmployeeAvailability(empId
						.getAvailability());
				// meetingAttendeeses.add(meetingAttendees);
				meetingRequestDaoImpl.save(meetingAttendees);

			}
		}

		conferenceRoomMailConfiguration
				.sendEventConfirmationMail(meetingRequestDto);

		return eventId;
	}

	public List<Date> getDatesBetweenDateRange(Date fromDate, Date toDate) {
		List<Date> dates = new ArrayList<Date>();

		// checks wheather working day or not
		if (checkDayIsWorkingDay(fromDate)) {
			dates.add(fromDate);
		}

		while (!fromDate.equals(toDate)) {
			Date nextDate = fromDate.next();
			if (checkDayIsWorkingDay(nextDate)) {
				dates.add(nextDate);
			}
			fromDate = nextDate;
		}
		return dates;
	}

	public Boolean checkDayIsWorkingDay(Date date) {
		Boolean workingDay = true;
		Holidays holiday = dao.findByUniqueProperty(Holidays.class, "date",
				date);

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(date.getJavaDate());
		int day = startCal.get(Calendar.DAY_OF_WEEK);
		if ((day == Calendar.SATURDAY) || (day == Calendar.SUNDAY)
				|| (holiday != null)) {

			workingDay = false;
		} else {

			workingDay = true;
		}

		return workingDay;
	}

	// This Method is used While Booking Meeting as well as Event
	public Boolean checkRoomReserved(int startTimeMilliSeconds,
			int endTimeMilliSeconds, List<MeetingRequest> meetingrequests) {

		Boolean roomReserved = false;

		if (meetingrequests.size() == 0) {
			roomReserved = false;
		} else {
			for (MeetingRequest meetingRequest : meetingrequests) {

				int totalStartTime = meetingRequest.getStartTime()
						.getHourOfDay().getValue()
						* 3600000
						+ meetingRequest.getStartTime().getMinuteOfHour()
								.getValue() * 60000;
				int totalEndTime = meetingRequest.getEndTime().getHourOfDay()
						.getValue()
						* 3600000
						+ meetingRequest.getEndTime().getMinuteOfHour()
								.getValue() * 60000;

				if ((startTimeMilliSeconds >= totalStartTime && startTimeMilliSeconds < totalEndTime)
						|| (endTimeMilliSeconds > totalStartTime && endTimeMilliSeconds <= totalEndTime)
						|| (startTimeMilliSeconds <= totalStartTime && endTimeMilliSeconds >= totalEndTime)) {
					// logger.warn("the room is already reserved");
					roomReserved = true;
					break;
				}
			}

		}

		return roomReserved;

	}

	public void addMeetingRequestForEvent(Date date, Room room, Long eventId,
			MeetingRequestDto meetingRequestDto, String start[], String end[], HttpServletRequest httpServletRequest) {

		TenantContextHolder.setTenantType(TenantTypes.valueOf(httpServletRequest.getHeader("tenantKey")));
		
		MeetingRequest meetingRequest = new MeetingRequest();
		meetingRequest.setAgenda(meetingRequestDto.getAgenda());
		meetingRequest.setId(meetingRequestDto.getId());
		meetingRequest.setProjectName(meetingRequestDto.getProjectName());

		meetingRequest.setRoom(room);
		meetingRequest.setLocation(meetingRequestDaoImpl.findBy(Location.class,
				meetingRequestDto.getLocationId()));

		meetingRequest.setAuthorName(String.valueOf(meetingRequestDto
				.getAuthorName().getId()));
		meetingRequest.setReservedBy(securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder());
		meetingRequest.setTrainerName(meetingRequestDto.getTrainerName().getId());

		meetingRequest.setMeetingStatus("New");
		meetingRequest.setConferenceType("Meeting");

		meetingRequest.setEventId(eventId);
		meetingRequest.setEventLocation(meetingRequestDto.getEventLocation());

		StringBuffer newStartTime = new StringBuffer();
		newStartTime.append(date.toString("dd/MM/yyyy")).append("/")
				.append(start[3]).append("/").append(start[4]);

		StringBuffer newEndTime = new StringBuffer();
		newEndTime.append(date.toString("dd/MM/yyyy")).append("/")
				.append(end[3]).append("/").append(end[4]);

		meetingRequest.setFromDate(date);

		meetingRequest.setStartTime(SecondParser.toSecond(newStartTime
				.toString()));
		meetingRequest.setEndTime(SecondParser.toSecond(newEndTime.toString()));
		meetingRequest.setDescription(meetingRequestDto.getDescription());
		Long meetingId = (Long) dao.save(meetingRequest);

		List<Availability> meetingempIds = meetingRequestDto.getAvailability();
		if (!meetingempIds.isEmpty()) {

			MeetingAttendees meetingAttendees = null;
			for (Availability empId : meetingempIds) {
				meetingAttendees = new MeetingAttendees();
				meetingAttendees.setMeetingRequest(dao.findBy(
						MeetingRequest.class, meetingId));
				Employee employee = meetingRequestDaoImpl.findBy(
						Employee.class, empId.getId());
				meetingAttendees.setEmployee(employee);
				meetingAttendees.setEmployeeAvailability(empId
						.getAvailability());
				// meetingAttendeeses.add(meetingAttendees);
				meetingRequestDaoImpl.save(meetingAttendees);

			}

			// This for sending mail notification for Individual meetings
			meetingRequestDto.setStartTime(newStartTime.toString());
			meetingRequestDto.setEndTime(newEndTime.toString());
			sendMeetingInvitation(meetingRequestDto, meetingId);

		}

	}

	public void sendMeetingInvitation(MeetingRequestDto dto, Long meetingId) {
		try {
			send(dto, meetingId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// EVENT FUNCTIONALITY METHODS ENDS HERE/

	@Override
	public void UpdateMeetingRequest(MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest ) {

		TenantContextHolder.setTenantType(TenantTypes.valueOf(httpServletRequest.getHeader("tenantKey")));
		
		MeetingRequest meetingRequest = meetingRequestDaoImpl.findBy(
				MeetingRequest.class, meetingRequestDto.getId());

		meetingRequest.setAgenda(meetingRequestDto.getAgenda());
		meetingRequest.setAuthorName(String.valueOf(meetingRequestDto
				.getAuthorName().getId()));
		meetingRequest.setEndTime(SecondParser.toSecond(meetingRequestDto
				.getEndTime()));
		try {
			meetingRequest.setFromDate(DateParser.toDate(meetingRequestDto
					.getFromDate()));
			meetingRequest.setToDate(DateParser.toDate(meetingRequestDto
					.getToDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		meetingRequest.setProjectName(meetingRequestDto.getProjectName());
		meetingRequest.setStartTime(SecondParser.toSecond(meetingRequestDto
				.getStartTime()));
		meetingRequest.setEndTime(SecondParser.toSecond(meetingRequestDto
				.getEndTime()));
		meetingRequest.setRoom(meetingRequestDaoImpl.findBy(Room.class,
				meetingRequestDto.getRoomId()));

		meetingRequestDaoImpl.update(meetingRequest);
		List<MeetingEditDTO> meetingAttendeeslist = meetingRequestDto
				.getMeetingEditDTOList();

		List<MeetingAttendees> attendees = dao.getAllOfProperty(
				MeetingAttendees.class, "meetingRequest", meetingRequest);
		for (MeetingAttendees attendees2 : attendees) {
			dao.delete(attendees2);
		}

		MeetingAttendees meetingAttendees = null;
		for (MeetingEditDTO empId : meetingAttendeeslist) {

			meetingAttendees = new MeetingAttendees();

			Employee employee = meetingRequestDaoImpl.findBy(Employee.class,
					empId.getId());

			meetingAttendees.setEmployeeAvailability(empId.getAvailability());
			meetingAttendees.setEmployee(employee);
			meetingAttendees.setMeetingRequest(meetingRequest);

			meetingRequestDaoImpl.save(meetingAttendees);

		}
		try {
			if (meetingAttendeeslist.size() > 0) {
				send(meetingRequestDto, meetingRequestDto.getId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<MeetingRequestDto> getAllMeetingRequestAppointments(Date date,
			Long roomId, HttpServletRequest httpServletRequest) {

		Room room = meetingRequestDaoImpl.findBy(Room.class, roomId);
		// meetingRequests = new ArrayList<>();
		List<MeetingRequestDto> meetingRequestsDto = new ArrayList<>();
		
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			List<MeetingRequest> meetingRequests = meetingRequestDaoImpl
					.getAllMeetingRequests(date, room);
			
			meetingRequestsDto.addAll(getMeetingRequestWithValidations(meetingRequests, httpServletRequest));
		}
			
		 
		return meetingRequestsDto;
	}

	@Override
	public List<MeetingRequestDto> getMeetingRequestDetailsWhileEdit(
			Long meetingId, Date date, Long roomId,HttpServletRequest httpServletRequest) {
		Room room = meetingRequestDaoImpl.findBy(Room.class, roomId);
		
		List<MeetingRequestDto> meetingRequestsDto = new ArrayList<>();
		
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
			List<MeetingRequest> meetingRequests = meetingRequestDaoImpl
			.getAllMeetingRequestsWhileEdit(meetingId, date, room);
			meetingRequestsDto.addAll(getMeetingRequestWithValidations(meetingRequests,httpServletRequest));
		}
		return meetingRequestsDto;

	}

	@Override
	public List<EmployeeDTO> getAttendiesProbability(String projectName) {
		EmployeeDTO empdto = null;
		List<EmployeeDTO> list = new ArrayList<EmployeeDTO>();

		Project project = meetingRequestDaoImpl.findByUniqueProperty(
				Project.class, "projectName", projectName);
		if (project != null) {

			List<AllocationDetails> allocationlist = meetingRequestDaoImpl
					.attendiesOfProjectList(project.getId());
			if (allocationlist != null) {

				for (AllocationDetails details : allocationlist) {
					empdto = new EmployeeDTO();
					Employee employee = meetingRequestDaoImpl.findBy(
							Employee.class, details.getEmployee()
									.getEmployeeId());
					empdto.setId(details.getEmployee().getEmployeeId());
					empdto.setFirstName(employee.getFirstName());
					empdto.setLastName(employee.getLastName());
					empdto.setFullName(employee.getFullName());
					list.add(empdto);

				}

			}
		}
		return list;
	}

	@Override
	public void changeMeetingStatus(Long requestId, String meetingStatus) {
		MeetingRequest meetingRequest = meetingRequestDaoImpl.findBy(
				MeetingRequest.class, requestId);
		if (meetingStatus.equalsIgnoreCase("Cancelled")) {

			try {
				cancelMeeting(requestId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			conferenceRoomMailConfiguration.sendCancelMeetingMail(requestId);

			List<MeetingAttendees> meetingAttendees = meetingRequestDaoImpl
					.changeMeetingStatus(requestId);
			for (MeetingAttendees ma : meetingAttendees) {
				meetingRequestDaoImpl.delete(ma);
			}
		} else if (meetingStatus.equalsIgnoreCase("Completed")) {
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy/HH/mm");
			Calendar calobj = Calendar.getInstance();
			String servertime = df.format(calobj.getTime());
			meetingRequest.setEndTime(SecondParser.toSecond(servertime));
		}

		meetingRequest.setMeetingStatus(meetingStatus);
		meetingRequestDaoImpl.update(meetingRequest);

	}

	@Override
	public Boolean uniqueAttendeeWithId(Long attendeeId, String attendeeName,String startTime,
			String endTime, Long meetingRequestId) {
		Set<Long> ids = new HashSet<Long>();
		List<MeetingRequest> meetingrequestlisted = new ArrayList<>();
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
				TenantContextHolder.setTenantType(type);
				List<MeetingRequest> meetingRequests = meetingRequestDaoImpl
						.uniqueAttendee(SecondParser.toSecond(startTime),
								SecondParser.toSecond(endTime));
				for(MeetingRequest meetingRequest : meetingRequests) {
					Set<MeetingAttendees> attendees = new HashSet<MeetingAttendees>(meetingRequestDaoImpl
							.changeMeetingStatus(meetingRequest.getId()));
					meetingRequest.setMeetingAttendees(attendees);
				}
				meetingrequestlisted.addAll(meetingRequests);
		}
		/*List<MeetingRequest> meetingrequestlisted = meetingRequestDaoImpl
				.uniqueAttendee(SecondParser.toSecond(startTime),
						SecondParser.toSecond(endTime));
*/
		String start[] = startTime.split("/");
		String end[] = endTime.split("/");
		int startTimeMilliSeconds = Integer.parseInt(start[3]) * 3600000
				+ Integer.parseInt(start[4]) * 60000;
		int endTimeMilliSeconds = Integer.parseInt(end[3]) * 3600000
				+ Integer.parseInt(end[4]) * 60000;

		Boolean uniqueFlag = false;
		// List<Long> empid = meetingRequestDto.getEmployeeIds();

		for (MeetingRequest meetingRequestforattendies : meetingrequestlisted) {
			if (!meetingRequestId.equals(meetingRequestforattendies.getId())) {
				/*List<MeetingAttendees> attendees = meetingRequestDaoImpl
						.changeMeetingStatus(meetingRequestforattendies.getId());*/
				int totalStartTime = meetingRequestforattendies.getStartTime()
						.getHourOfDay().getValue()
						* 3600000
						+ meetingRequestforattendies.getStartTime()
								.getMinuteOfHour().getValue() * 60000;
				int totalEndTime = meetingRequestforattendies.getEndTime()
						.getHourOfDay().getValue()
						* 3600000
						+ meetingRequestforattendies.getEndTime()
								.getMinuteOfHour().getValue() * 60000;

				for (MeetingAttendees attendees2 : meetingRequestforattendies.getMeetingAttendees()) {

					if ((attendees2.getEmployee().getEmployeeId()
							.equals(attendeeId)|| attendees2.getEmployee().getEmployeeFullName().equals(attendeeName))
							&& (endTimeMilliSeconds > totalStartTime)
							&& (startTimeMilliSeconds < totalEndTime)) {
						uniqueFlag = true;
					}
				}
			}
		}
		return uniqueFlag;
	}

	@Override
	public Boolean uniqueAttendee(Long attendeeId, String attendeeName, String startTime,
			String endTime) {
		Set<Long> ids = new HashSet<Long>();
		List<MeetingRequest> meetingrequestlisted = new ArrayList<>();
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
				TenantContextHolder.setTenantType(type);
				List<MeetingRequest> meetingRequests = meetingRequestDaoImpl
						.uniqueAttendee(SecondParser.toSecond(startTime),
								SecondParser.toSecond(endTime));
				for(MeetingRequest meetingRequest : meetingRequests) {
					Set<MeetingAttendees> attendees = new HashSet<MeetingAttendees>(meetingRequestDaoImpl
							.changeMeetingStatus(meetingRequest.getId()));
					meetingRequest.setMeetingAttendees(attendees);
				}
				meetingrequestlisted.addAll(meetingRequests);
		}
		
		/*List<MeetingRequest> meetingrequestlisted = meetingRequestDaoImpl
				.uniqueAttendee(SecondParser.toSecond(startTime),
						SecondParser.toSecond(endTime));*/

		String start[] = startTime.split("/");
		String end[] = endTime.split("/");
		int startTimeMilliSeconds = Integer.parseInt(start[3]) * 3600000
				+ Integer.parseInt(start[4]) * 60000;
		int endTimeMilliSeconds = Integer.parseInt(end[3]) * 3600000
				+ Integer.parseInt(end[4]) * 60000;

		Boolean uniqueFlag = false;
		// List<Long> empid = meetingRequestDto.getEmployeeIds();

		for (MeetingRequest meetingRequestforattendies : meetingrequestlisted) {

			/*List<MeetingAttendees> attendees = meetingRequestDaoImpl
					.changeMeetingStatus(meetingRequestforattendies.getId());*/
			
			int totalStartTime = meetingRequestforattendies.getStartTime()
					.getHourOfDay().getValue()
					* 3600000
					+ meetingRequestforattendies.getStartTime()
							.getMinuteOfHour().getValue() * 60000;
			int totalEndTime = meetingRequestforattendies.getEndTime()
					.getHourOfDay().getValue()
					* 3600000
					+ meetingRequestforattendies.getEndTime().getMinuteOfHour()
							.getValue() * 60000;

			for (MeetingAttendees attendees2 : meetingRequestforattendies.getMeetingAttendees()) {

				if (((attendees2.getEmployee().getEmployeeId().equals(attendeeId)) || (attendees2.getEmployee().getEmployeeFullName().equals(attendeeName)))
						&& (endTimeMilliSeconds > totalStartTime)
						&& (startTimeMilliSeconds < totalEndTime)) {
					uniqueFlag = true;
				}

			}
		}
		return uniqueFlag;
	}

	@Override
	public SearchEmpDetailsDTO getDefaultAuthorName(Long employeeId) {
		Employee employee = meetingRequestDaoImpl.findBy(Employee.class,
				employeeId);

		return employeeBuilder.createEmployeeDTOForSearch(employee);
	}

	public PropBean getPropBean() {
		return propBean;
	}

	public void setPropBean(PropBean propBean) {
		this.propBean = propBean;
	}

	public void sendMeetingRequestMail(MeetingRequestDto meetingRequestDto) {

		Employee loggedInEmpId = meetingRequestDaoImpl.findBy(Employee.class,
				meetingRequestDto.getAuthorName().getId());
		String reservedBy = loggedInEmpId.getFullName();
		String to = null;
		String date = meetingRequestDto.getFromDate();
		String startTime = meetingRequestDto.getStartTime();
		String endTime = meetingRequestDto.getEndTime();
		String[] stime = startTime.split("/");
		String startTimeFormate = stime[3] + ":" + stime[4];
		String[] endTimeFormate = endTime.split("/");
		String s2 = endTimeFormate[3] + ":" + endTimeFormate[4];
		String endHour = meetingRequestBuilder.twelveHoursFormate(s2);
		String startHour = meetingRequestBuilder
				.twelveHoursFormate(startTimeFormate);
		List<Availability> empId = meetingRequestDto.getAvailability();
		String agenda = meetingRequestDto.getAgenda();

		Room room = meetingRequestDaoImpl.findBy(Room.class,
				meetingRequestDto.getRoomId());
		Boolean flag = true;
		String room1 = room.getRoomName();
		String location = room.getLocation().getLocationName();

		for (Availability empid : empId) {
			Employee employee = meetingRequestDaoImpl.findBy(Employee.class,
					empid.getId());
			if (flag) {
				to = employee.getEmail();
				flag = false;
			} else {
				to = to + " ," + employee.getEmail();
			}
		}
		// logger.warn("send mail to:" + to);
		// String cc = (String) propBean.getPropData().get("mail.hrMailId");
		String bcc = " ";

		String subject = "<![CDATA[ Meeting Request for ]]>" + agenda;
		/*
		 * StringBuffer buffer = new StringBuffer();
		 * buffer.append("<![CDATA[Dear All") .append(
		 * ", <br><br><br> Please be informed that you have been invited to attend "
		 * 
		 * + reservedBy + " on " + date + " from " + startHour + " to " +
		 * endHour + " to provide "
		 * 
		 * + agenda +
		 * ". Request you to make yourself available. <br><br> Venue details : "
		 * + location + " - " + room1 + ".<br>" + " Schedule : " + date +
		 * "<br> Time : " + startHour + " to " + endHour)
		 * .append(" <br><br><br> Regards,<br>") .append(reservedBy +
		 * " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA. "
		 * + "]]>");
		 * 
		 * String body = buffer.toString();
		 */
		// logger.warn("Sending Meeting conduction Mail.....");

		StringBuffer buffer1 = new StringBuffer();
		buffer1.append("<![CDATA[Dear " + reservedBy)
				.append(", <br><br><br> Please be informed that the " + agenda
						+ " is organised. " + "<br><br> Venue details : "
						+ location + " - " + room1 + ".<br>" + " Schedule : "
						+ date + "<br> Time : " + startHour + " to " + endHour)
				.append(" <br><br><br> Regards,<br>")
				.append(reservedBy
						+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA. "
						+ "]]>");
		String body1 = buffer1.toString();

		try {
			/*
			 * messageSender.sendMessage("<email><to>" + to + "</to>" // +
			 * "<cc>"+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>" +
			 * subject + "</subject>" + "<body>" + body + "</body></email>");
			 */
			// logger.warn("mail sent successfully");

			messageSender.sendMessage("<email><to>" + loggedInEmpId.getEmail()
					+ "</to>"
					// + "<cc>"+ cc + "</cc>"
					+ "<bcc>" + bcc + "</bcc>" + "<subject>" + subject
					+ "</subject>" + "<body>" + body1 + "</body></email>");
			// logger.warn("mail sent successfully to orgnizer");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}
	}

	public void sendCancelMeetingMail(Long meetingRequestId) {
		List<MeetingAttendees> meetingAttendees = meetingRequestDaoImpl
				.changeMeetingStatus(meetingRequestId);

		MeetingRequest meetingRequest = meetingRequestDaoImpl.findBy(
				MeetingRequest.class, meetingRequestId);

		Employee loggedInEmpId = meetingRequestDaoImpl.findBy(Employee.class,
				Long.parseLong(meetingRequest.getAuthorName()));
		String to = loggedInEmpId.getEmail();
		String date = DateParser.toString(meetingRequest.getFromDate());
		String endTime = meetingRequestBuilder
				.twelveHoursFormate(meetingRequest.getEndTime().getHourOfDay()
						.getValue()
						+ ":"
						+ meetingRequest.getEndTime().getMinuteOfHour()
								.getValue());
		String startTime = meetingRequestBuilder
				.twelveHoursFormate(meetingRequest.getStartTime()
						.getHourOfDay().getValue()
						+ ":"
						+ meetingRequest.getStartTime().getMinuteOfHour()
								.getValue());
		String reservedBy = loggedInEmpId.getFullName();
		String agenda = meetingRequest.getAgenda();
		String room = null;
		if (meetingRequest.getRoom() != null) {
			room = meetingRequest.getRoom().getRoomName();
		} else {
			room = meetingRequest.getEventLocation();
		}
		String location = meetingRequest.getLocation().getLocationName();

		for (MeetingAttendees meeting : meetingAttendees) {
			to = to + " ," + meeting.getEmployee().getEmail();
		}
		// logger.warn("cancel mail to:" + to);
		// String cc = (String)
		// propBean.getPropData().get("mail.supportMailId");
		String bcc = " ";
		String subject = "<![CDATA[ Meeting Cancelled for ]]>" + agenda;
		/*
		 * StringBuffer buffer = new StringBuffer();
		 * 
		 * buffer.append("<![CDATA[Dear All")
		 * .append(", <br><br><br> Please be informed that the " + agenda +
		 * " has been cancelled by " + startTime + " to " +endTime+ " at "
		 * +room+ " in " +location + " has been cancelled by Mr./Ms. " +
		 * reservedBy + ". For further queries feel free to contact " +
		 * reservedBy + ".") .append(" <br><br><br> Regards,<br>")
		 * .append(reservedBy +
		 * "<br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA. "
		 * + "]]>"); String body = buffer.toString();
		 */
		// logger.warn("Sending Meeting Cancel Mail.....");

		StringBuffer buffer1 = new StringBuffer();
		buffer1.append("<![CDATA[Dear " + reservedBy)
				.append(", <br><br><br> Please be informed that the " + agenda
						+ " has been Cancelled. ")
				.append(" <br><br><br> Regards,<br>")
				.append(reservedBy
						+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA. "
						+ "]]>");
		String body1 = buffer1.toString();
		// logger.warn("Sending Meeting conduction Mail to orgnizer....."+reservedBy);

		try {
			messageSender.sendMessage("<email><to>" + loggedInEmpId.getEmail()
					+ "</to>"
					// + "<cc>"+ cc + "</cc>"
					+ "<bcc>" + bcc + "</bcc>" + "<subject>" + subject
					+ "</subject>" + "<body>" + body1 + "</body></email>");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}

	private void sendUpdateMeetingRequestMail(
			MeetingRequestDto meetingRequestDto) {

		Employee loggedInEmpId = meetingRequestDaoImpl.findBy(Employee.class,
				meetingRequestDto.getAuthorName().getId());
		String reservedBy = loggedInEmpId.getFullName();
		String to = null;
		String date = meetingRequestDto.getFromDate();
		String startTime = meetingRequestDto.getStartTime();
		String endTime = meetingRequestDto.getEndTime();
		String[] stime = startTime.split("/");
		String startTimeFormate = stime[3] + ":" + stime[4];
		String[] endTimeFormate = endTime.split("/");
		String s2 = endTimeFormate[3] + ":" + endTimeFormate[4];
		String endHour = meetingRequestBuilder.twelveHoursFormate(s2);
		String startHour = meetingRequestBuilder
				.twelveHoursFormate(startTimeFormate);
		List<MeetingEditDTO> empId = meetingRequestDto.getMeetingEditDTOList();
		String agenda = meetingRequestDto.getAgenda();

		Room room = meetingRequestDaoImpl.findBy(Room.class,
				meetingRequestDto.getRoomId());
		Boolean flag = true;
		String room1 = room.getRoomName();
		String location = room.getLocation().getLocationName();

		for (MeetingEditDTO empid : empId) {
			Employee employee = meetingRequestDaoImpl.findBy(Employee.class,
					empid.getId());
			if (flag) {
				to = employee.getEmail();
				flag = false;
			} else {
				to = to + " ," + employee.getEmail();
			}
		}
		// logger.warn("send mail to:" + to);
		// String cc = (String) propBean.getPropData().get("mail.hrMailId");
		String bcc = " ";

		String subject = "<![CDATA[ Meeting Request for ]]>" + agenda;
		/*
		 * StringBuffer buffer = new StringBuffer();
		 * buffer.append("<![CDATA[Dear All") .append(
		 * ", <br><br><br> Please be informed that you have been invited to attend "
		 * 
		 * + reservedBy + " on " + date + " from " + startHour + " to " +
		 * endHour + " to provide "
		 * 
		 * + agenda +
		 * ". Request you to make yourself available. <br><br> Venue details : "
		 * + location + " - " + room1 + ".<br>" + " Schedule : " + date +
		 * "<br> Time : " + startHour + " to " + endHour)
		 * .append(" <br><br><br> Regards,<br>") .append(reservedBy +
		 * " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA. "
		 * + "]]>");
		 * 
		 * String body = buffer.toString();
		 */
		// logger.warn("Sending Meeting conduction Mail.....");

		StringBuffer buffer1 = new StringBuffer();
		buffer1.append("<![CDATA[Dear " + reservedBy)
				.append(", <br><br><br> Please be informed that the following changes are made for  "
						+ agenda
						+ ". "
						+ "<br><br> Venue details : "
						+ location
						+ " - "
						+ room1
						+ ".<br>"
						+ " Schedule : "
						+ date + "<br> Time : " + startHour + " to " + endHour)
				.append(" <br><br><br> Regards,<br>")
				.append(reservedBy
						+ " <br><br> Raybiztech is a CMMI Level 3, ISO 27001:2013 and ISO 9001:2008 Certified Company and The Member of NASSCOM and HYSEA. "
						+ "]]>");
		String body1 = buffer1.toString();

		try {
			/*
			 * messageSender.sendMessage("<email><to>" + to + "</to>" // +
			 * "<cc>"+ cc + "</cc>" + "<bcc>" + bcc + "</bcc>" + "<subject>" +
			 * subject + "</subject>" + "<body>" + body + "</body></email>");
			 */
			// logger.warn("mail sent successfully");

			messageSender.sendMessage("<email><to>" + loggedInEmpId.getEmail()
					+ "</to>"
					// + "<cc>"+ cc + "</cc>"
					+ "<bcc>" + bcc + "</bcc>" + "<subject>" + subject
					+ "</subject>" + "<body>" + body1 + "</body></email>");
			// logger.warn("mail sent successfully to orgnizer");
		} catch (JMSException ex) {
			logger.error(ex.getMessage());
		}

	}

	@Override
	public MeetingRequestDto editmeeting(Long id) {

		MeetingRequest meetingRequest = dao.findBy(MeetingRequest.class, id);
		return meetingRequestBuilder.toEditEntity(meetingRequest);

	}

	// For Checking Update Validations
	@Override
	public synchronized void confirmupdateMeetingRoomRequest(
			MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest) {

		MeetingRequest meetingRequestData = dao.findBy(MeetingRequest.class,
				meetingRequestDto.getId());

		String serverDateTime = this.getserverdateandtime();
		String serverDateTimeSplit[] = serverDateTime.split(" ");
		String serverDate = serverDateTimeSplit[0];
		String serverTimevariable = serverDateTimeSplit[1];
		String[] serverTimeSplit = serverTimevariable.split(":");

		Second startDate = meetingRequestData.getStartTime();
		String startTimeDate = startDate.toString("dd/MM/yyyy HH:mm:ss");
		String splitDateTime[] = startTimeDate.split(" ");
		String dbDate = splitDateTime[0];
		String dbTime = splitDateTime[1];
		String dbsplit[] = dbTime.split(":");
		Long savedHours = Long.valueOf(dbsplit[0]);
		Long savedMinutes = Long.valueOf(dbsplit[1]);

		Long serverHours = Long.valueOf(serverTimeSplit[0]);
		Long severMinutes = Long.valueOf(serverTimeSplit[1]);
		Date serverd = null;
		Date dbDate1 = null;
		try {
			serverd = DateParser.toDate(serverDate);
			dbDate1 = DateParser.toDate(dbDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (serverd.compareTo(dbDate1) < 0) {
			this.confirmupdateMeetingRoomRequestAfterValidation(meetingRequestDto,httpServletRequest);

		} else if (serverd.compareTo(dbDate1) == 0) {
			if (serverHours == savedHours) {
				if (severMinutes > savedMinutes) {
					throw new CrossingTimeException("you cross the start time");

				} else {
					this.confirmupdateMeetingRoomRequestAfterValidation(meetingRequestDto,httpServletRequest);
				}

			} else if (serverHours > savedHours) {
				throw new CrossingTimeException("you cross the start time");

			} else {
				this.confirmupdateMeetingRoomRequestAfterValidation(meetingRequestDto,httpServletRequest);

			}

		} else {
			throw new CrossingTimeException("you cross the start time");
		}
	}

	// Meeting request Update Functionality
	@Override
	public synchronized void confirmupdateMeetingRoomRequestAfterValidation(
			MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest) {

		TenantContextHolder.setTenantType(TenantTypes.valueOf(httpServletRequest.getHeader("tenantKey")));
		MeetingRequest meetingRequestData = dao.findBy(MeetingRequest.class,
				meetingRequestDto.getId());

		int totalStartTimeBack = meetingRequestData.getStartTime()
				.getHourOfDay().getValue()
				* 3600000
				+ meetingRequestData.getStartTime().getMinuteOfHour()
						.getValue() * 60000;
		int totalEndTimeTimeBack = meetingRequestData.getEndTime()
				.getHourOfDay().getValue()
				* 3600000
				+ meetingRequestData.getEndTime().getMinuteOfHour().getValue()
				* 60000;

		List<MeetingRequest> meetingrequests = new ArrayList<>();
		
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
				TenantContextHolder.setTenantType(type);
				meetingrequests.addAll(meetingRequestDaoImpl
					.getTimeForSelectedLocation(meetingRequestDto));
		}
	

		String start[] = meetingRequestDto.getStartTime().split("/");
		String end[] = meetingRequestDto.getEndTime().split("/");
		int startTimeMilliSeconds = Integer.parseInt(start[3]) * 3600000
				+ Integer.parseInt(start[4]) * 60000;
		int endTimeMilliSeconds = Integer.parseInt(end[3]) * 3600000
				+ Integer.parseInt(end[4]) * 60000;

		if (meetingrequests.size() != 0) {
			Boolean alreadyReservedFlag = false;
			for (MeetingRequest meetingRequest : meetingrequests) {
				if (meetingRequest.getId().equals(meetingRequestDto.getId())) {
					if (((startTimeMilliSeconds == totalStartTimeBack && endTimeMilliSeconds == totalEndTimeTimeBack))) {
						alreadyReservedFlag = false;
						break;
					}
				} else {

					int totalStartTime = meetingRequest.getStartTime()
							.getHourOfDay().getValue()
							* 3600000
							+ meetingRequest.getStartTime().getMinuteOfHour()
									.getValue() * 60000;
					int totalEndTime = meetingRequest.getEndTime()
							.getHourOfDay().getValue()
							* 3600000
							+ meetingRequest.getEndTime().getMinuteOfHour()
									.getValue() * 60000;
					if ((startTimeMilliSeconds >= totalStartTime && startTimeMilliSeconds < totalEndTime)
							|| (endTimeMilliSeconds > totalStartTime && endTimeMilliSeconds <= totalEndTime)
							|| (startTimeMilliSeconds <= totalStartTime && endTimeMilliSeconds >= totalEndTime)) {
						// logger.warn("the room is already reserved");
						alreadyReservedFlag = true;
						break;
					}
				}
			}

			if (!alreadyReservedFlag) {
				// logger.warn("Its not reserved.So you can book it");
				UpdateMeetingRequest(meetingRequestDto,httpServletRequest);

				conferenceRoomMailConfiguration
						.sendMeetingUpdationMail(meetingRequestDto);
			} else {
				// logger.warn("You cant book a room which was already reserved");
				throw new MeetingRoomAlreadyBookedException(
						"Room was already reserved");
			}
		} else {
			UpdateMeetingRequest(meetingRequestDto,httpServletRequest);
			conferenceRoomMailConfiguration
					.sendMeetingUpdationMail(meetingRequestDto);
		}
	}

	@Override
	public String getserverdateandtime() {

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar calobj = Calendar.getInstance();
		String date = df.format(calobj.getTime());
		return date;

	}

	@Override
	public void updateLocation(LocationDto locationDto) {

		Location location = dao.findBy(Location.class, locationDto.getId());
		location.setLocationName(locationDto.getLocationName());
		meetingRequestDaoImpl.update(location);

	}

	@Override
	public void updateRoom(RoomDto roomDto) {

		Room room = dao.findBy(Room.class, roomDto.getId());
		room.setRoomName(roomDto.getRoomName());
		room.setRoomStatus(roomDto.getRoomStatus()? Boolean.TRUE :Boolean.FALSE);
		meetingRequestDaoImpl.update(room);
	}

	// changing time formate to timechecking() suitable parameter formate;
	public String formatechange(String time) {
		String splitedDateTime[] = time.split(" ");
		String dbTime = splitedDateTime[1];
		String dbsplit[] = dbTime.split(":");
		String timeformate = splitedDateTime[0] + "/" + dbsplit[0] + "/"
				+ dbsplit[1];
		return timeformate;
	}

	// For checking the given time with server time
	public Boolean timechecking(String time) {

		Boolean valid = Boolean.FALSE;
		String serverDateTime = this.getserverdateandtime();
		String serverDateTimeSplit[] = serverDateTime.split(" ");
		String serverDate = serverDateTimeSplit[0];
		String serverTimevariable = serverDateTimeSplit[1];
		String[] serverTimeSplit = serverTimevariable.split(":");

		String startTimeDate = time;
		String splitDateTime[] = startTimeDate.split("/");
		String dbDate = splitDateTime[0] + "/" + splitDateTime[1] + "/"
				+ splitDateTime[2];
		Long savedHours = Long.valueOf(splitDateTime[3]);
		Long savedMinutes = Long.valueOf(splitDateTime[4]);

		Long serverHours = Long.valueOf(serverTimeSplit[0]);
		Long severMinutes = Long.valueOf(serverTimeSplit[1]);
		Date serverd = null;
		try {
			serverd = DateParser.toDate(serverDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date dbDate1 = null;
		try {
			dbDate1 = DateParser.toDate(dbDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (serverd.compareTo(dbDate1) < 0) {

			valid = Boolean.FALSE;
		} else if (serverd.compareTo(dbDate1) == 0) {
			if (serverHours == savedHours) {
				if (severMinutes > savedMinutes) {

					valid = Boolean.TRUE;
				}

			} else if (serverHours > savedHours) {

				valid = Boolean.TRUE;
			} else {
				valid = Boolean.FALSE;
			}

		} else {

			valid = Boolean.TRUE;
		}

		return valid;

	}

	public void send(MeetingRequestDto meetingRequestDto, Long id)
			throws Exception {
		Session session = mailSenderUtility.mailSession();
		List<Availability> empId = meetingRequestDto.getAvailability();
		Boolean flag = true;
		String to = null;
		if (meetingRequestDto.getId() == null) {
			for (Availability empid : empId) {
				Employee employee = dao.findBy(Employee.class, empid.getId());
				if (flag) {
					to = employee.getEmail();
					flag = false;
				} else {
					to = to + " ," + employee.getEmail();
				}
			}
		} else {
			List<MeetingEditDTO> meetingAttendeeslist = meetingRequestDto
					.getMeetingEditDTOList();

			if (meetingAttendeeslist != null) {
				for (MeetingEditDTO empI : meetingAttendeeslist) {
					Employee employee = meetingRequestDaoImpl.findBy(
							Employee.class, empI.getId());
					if (flag) {
						to = employee.getEmail();
						flag = false;
					} else {
						to = to + " ," + employee.getEmail();
					}
				}
			}
		}
		String[] recipients = to.split(",");
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress((String) propBean.getPropData()
					.get("mail.fromMailId"),meetingRequestDto.getAuthorName().getFullName()));
			message.setSubject("Meeting Invitation for "
					+ meetingRequestDto.getAgenda());
			for (int i = 0; i < recipients.length; i++) {
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(recipients[i]));
			}
			// Create an alternative Multipart
			Multipart multipart = new MimeMultipart("alternative");
			// part 1, html text
			BodyPart messageBodyPart = buildHtmlTextPart(meetingRequestDto
					.getAgenda());
			multipart.addBodyPart(messageBodyPart);
			// Add part two, the calendar
			BodyPart calendarPart = buildCalendarPart(meetingRequestDto, id);
			multipart.addBodyPart(calendarPart);
			// Put the multipart in message
			message.setContent(multipart);
			// send the message
			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			Thread.sleep(1000);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	private BodyPart buildHtmlTextPart(String agenda) throws MessagingException {
		MimeBodyPart descriptionPart = new MimeBodyPart();

		// Note: even if the content is spcified as being text/html, Zimbra
		// won't read correctly tables at all
		// and only some properties from div:s. Thus, try to avoid too fancy
		// content
		String content = "<font >" + agenda + "</font>";
		descriptionPart.setContent(content, "text/html; charset=utf-8");
		return descriptionPart;
	}

	// define somewhere the icalendar date format

	private SimpleDateFormat iCalendarDateFormat = new SimpleDateFormat(
			"yyyyMMdd'T'HHmm'00'");

	private BodyPart buildCalendarPart(MeetingRequestDto meetingRequestDto,
			Long id) throws Exception {

		BodyPart calendarPart = new MimeBodyPart();

		java.util.Date start = javaUtildate(meetingRequestDto.getStartTime());
		java.util.Date end = javaUtildate(meetingRequestDto.getEndTime());
		// Employee loggedInEmpId = dao.findBy(Employee.class, meetingRequestDto
		// .getAuthorName().getId());
		// String from = loggedInEmpId.getEmail();
		List<Availability> empId = meetingRequestDto.getAvailability();
		Room room = dao.findBy(Room.class, meetingRequestDto.getRoomId());
		String room1 = room.getRoomName();
		String location = room.getLocation().getLocationName();
		String to = null;
		Boolean flag = true;
		if (meetingRequestDto.getId() == null) {
			for (Availability empid : empId) {
				Employee employee = dao.findBy(Employee.class, empid.getId());
				if (flag) {
					to = "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:"
							+ employee.getEmail() + "\n";
					flag = false;
				} else {
					to = to +

					"ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:"
							+ employee.getEmail() + "\n";
				}

			}
		} else {

			List<MeetingEditDTO> meetingAttendeeslist = meetingRequestDto
					.getMeetingEditDTOList();

			for (MeetingEditDTO empI : meetingAttendeeslist) {

				Employee employee = meetingRequestDaoImpl.findBy(
						Employee.class, empI.getId());
				if (flag) {
					to = "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:"
							+ employee.getEmail() + "\n";
					flag = false;
				} else {
					to = to +

					"ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:"
							+ employee.getEmail() + "\n";
				}
			}

		}

		Organizer org1 = new Organizer(URI.create("MAILTO:"
				+ (String) propBean.getPropData().get("mail.fromMailId")));
		String calendarContent = "BEGIN:VCALENDAR\n" + "METHOD:REQUEST\n"
				+
				// "PRODID: BCP - Meeting\n" +
				"PRODID:Zimbra-Calendar-Provider" + "VERSION:2.0\n"
				+ "BEGIN:VEVENT\n" + "DTSTAMP:"
				+ iCalendarDateFormat.format(start)
				+ "\n"
				+ "DTSTART:"
				+ iCalendarDateFormat.format(start)
				+ "\n"
				+ "DTEND:"
				+ iCalendarDateFormat.format(end)
				+ "\n"
				+ "SUMMARY:"
				+ meetingRequestDto.getAgenda()
				+ "\n"
				+ "UID:"
				+ id.toString()
				+ "\n"
				// +
				// "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:***@raybiztech.com\n"
				+ to
				+ org1
				// "ORGANIZER:MAILTO:***@raybiztech.com\n"
				+ "LOCATION:"
				+ location
				+ " - "
				+ room1
				+ "\n"
				+ "DESCRIPTION:"
				+ meetingRequestDto.getAgenda()
				+ "\n"
				+ "SEQUENCE:0\n"
				+ "PRIORITY:5\n"
				+ "CLASS:PUBLIC\n"
				+ "STATUS:CONFIRMED\n"
				+ "TRANSP:OPAQUE\n"
				+ "BEGIN:VALARM\n"
				+ "ACTION:DISPLAY\n"
				+ "DESCRIPTION:REMINDER\n"
				+ "TRIGGER;RELATED=START:-PT00H15M00S\n"
				+ "END:VALARM\n"
				+ "END:VEVENT\n" + "END:VCALENDAR";
		calendarPart.addHeader("Content-Class",
				"urn:content-classes:calendarmessage");
		calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");
		return calendarPart;
	}

	public java.util.Date javaUtildate(String inputDate) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy/HH/mm");

		java.util.Date date = null;
		try {
			date = dateFormat.parse(inputDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	// cancel meeting mail start here
	public void cancelMeeting(Long id) throws Exception {

		MeetingRequest meetingRequest = dao.findBy(MeetingRequest.class, id);
		Session session = mailSenderUtility.mailSession();
		Boolean flag = true;
		String to = null;
		List<MeetingAttendees> meetingAttendeeslist = meetingRequestDaoImpl
				.getAttendees(id);

		for (MeetingAttendees empI : meetingAttendeeslist) {

			Employee employee = meetingRequestDaoImpl.findBy(Employee.class,
					empI.getEmployee().getEmployeeId());
			if (flag) {
				to = employee.getEmail();
				flag = false;
			} else {
				to = to + " ," + employee.getEmail();
			}
		}
		String[] recipients = to.split(",");
		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress((String) propBean.getPropData()
					.get("mail.fromMailId")));
			message.setSubject("Meeting Cancelled for "
					+ meetingRequest.getAgenda());
			for (int i = 0; i < recipients.length; i++) {
				message.addRecipient(Message.RecipientType.TO,
						new InternetAddress(recipients[i]));
			}
			// Create an alternative Multipart
			Multipart multipart = new MimeMultipart("alternative");
			// part 1, html text
			BodyPart messageBodyPart = buildHtmlTextPart(meetingRequest
					.getAgenda());
			multipart.addBodyPart(messageBodyPart);
			// Add part two, the calendar
			BodyPart calendarPart = buildCalendarPartForCancel(meetingRequest,
					id);
			multipart.addBodyPart(calendarPart);
			// Put the multipart in message
			message.setContent(multipart);
			// send the message
			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			Thread.sleep(1000);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	private BodyPart buildCalendarPartForCancel(MeetingRequest meetingRequest,
			Long id) throws Exception {

		BodyPart calendarPart = new MimeBodyPart();

		java.util.Date start = javaUtildate(meetingRequest.getStartTime()
				.toString("dd/MM/yyyy/HH/mm"));
		java.util.Date end = javaUtildate(meetingRequest.getEndTime().toString(
				"dd/MM/yyyy/HH/mm"));
		Room room = dao.findBy(Room.class, meetingRequest.getRoom().getId());
		String room1 = room.getRoomName();
		String location = room.getLocation().getLocationName();
		String to = null;
		Boolean flag = true;

		List<MeetingAttendees> meetingAttendeeslist = meetingRequestDaoImpl
				.getAttendees(id);

		for (MeetingAttendees empI : meetingAttendeeslist) {

			Employee employee = meetingRequestDaoImpl.findBy(Employee.class,
					empI.getEmployee().getEmployeeId());
			if (flag) {
				to = "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:"
						+ employee.getEmail() + "\n";
				flag = false;
			} else {
				to = to +

				"ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:"
						+ employee.getEmail() + "\n";
			}
		}

		Organizer org1 = new Organizer(URI.create("MAILTO:"
				+ (String) propBean.getPropData().get("mail.fromMailId")));
		String calendarContent = "BEGIN:VCALENDAR\n" + "METHOD:cancel\n"
				+
				// "PRODID: BCP - Meeting\n" +
				"PRODID:Zimbra-Calendar-Provider" + "VERSION:2.0\n"
				+ "BEGIN:VEVENT\n" + "DTSTAMP:"
				+ iCalendarDateFormat.format(start)
				+ "\n"
				+ "DTSTART:"
				+ iCalendarDateFormat.format(start)
				+ "\n"
				+ "DTEND:"
				+ iCalendarDateFormat.format(end)
				+ "\n"
				+ "SUMMARY:"
				+ meetingRequest.getAgenda()
				+ "\n"
				+ "UID:"
				+ id.toString()
				+ "\n"
				// +
				// "ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=NEEDS-ACTION;RSVP=TRUE:MAILTO:***@raybiztech.com\n"
				+ to
				+ org1
				// "ORGANIZER:MAILTO:***@raybiztech.com\n"
				+ "LOCATION:"
				+ location
				+ " - "
				+ room1
				+ "\n"
				+ "DESCRIPTION:"
				+ meetingRequest.getAgenda()
				+ "\n"
				+ "SEQUENCE:0\n"
				+ "PRIORITY:5\n"
				+ "CLASS:PUBLIC\n"
				+ "STATUS:CANCELLED\n"
				+ "TRANSP:OPAQUE\n"
				+ "BEGIN:VALARM\n"
				+ "ACTION:DISPLAY\n"
				+ "DESCRIPTION:REMINDER\n"
				+ "TRIGGER;RELATED=START:-PT00H15M00S\n"
				+ "END:VALARM\n"
				+ "END:VEVENT\n" + "END:VCALENDAR";
		calendarPart.addHeader("Content-Class",
				"urn:content-classes:calendarmessage");
		calendarPart.setContent(calendarContent, "text/calendar;method=CANCEL");
		return calendarPart;
	}

	@Override
	public List<MeetingRequestDto> getBookingsForSelection(Long location,
			Long room, String status, String meetingstatus, HttpServletRequest httpServletRequest) {

		Location location2 = (location != null) ? dao.findBy(Location.class,
				location) : null;
		Room room2 = (room != null) ? dao.findBy(Room.class, room) : null;
		Date date = null;
		try {
			date = status.equalsIgnoreCase("Today") ? new Date() : status
					.equalsIgnoreCase("Tommorow") ? new Date().next()
					: DateParser.toDate(status);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		meetingRequestBuilder.validateMeetingRequestStatus(location2, room2,
				date);
		List<MeetingRequestDto> meetingRequesDto = new ArrayList<>();
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);
		List<MeetingRequest> meetingRequests = meetingRequestDaoImpl
				.getBookingsForSelection(location2, room2, date, meetingstatus);
		meetingRequesDto.addAll(meetingRequestBuilder.toDTOList(meetingRequests,httpServletRequest));
		}
		return meetingRequesDto;
	}

	// This Method is used to calculate all validations on Meeting Request such
	// as editing meeting meeting status change etc
	public List<MeetingRequestDto> getMeetingRequestWithValidations(
			List<MeetingRequest> meetingRequests,HttpServletRequest httpServletRequest) {
		
		Collections.sort(meetingRequests, new Comparator<MeetingRequest>() {

			@Override
			public int compare(MeetingRequest o1, MeetingRequest o2) {
				int k = 0;
				if(o2.getStartTime().isAfter(o1.getStartTime())) {
					k=-1;
				}
				if(o2.getStartTime().isBefore(o1.getStartTime())) {
						k=1;
				}
				return k;
			}
			
		});

		List<MeetingRequestDto> meetingRequestDtos = new ArrayList<MeetingRequestDto>();
		MeetingRequestDto meetingRequestDto1 = null;

		// HERE WE ARE SETTING ALL MEETING LIST TO MEETINGDTO LIST
		for (MeetingRequest meetingRequest : meetingRequests) {
			// Auto Updation to completed when meeting time is completed

			if (meetingRequest.getEndTime().isBefore(new Second())) {
				String status = meetingRequest.getMeetingStatus();
				if (!status.equalsIgnoreCase("Completed")
						|| !status.equalsIgnoreCase("Cancelled")) {
					meetingRequest.setMeetingStatus("Completed");
					dao.update(meetingRequest);
				}
				continue;
			}

			meetingRequestDto1 = new MeetingRequestDto();
			meetingRequestDto1 = meetingRequestBuilder.toDto(meetingRequest,httpServletRequest);
			meetingRequestDto1.setTimeFomrat(SecondParser
					.toString(meetingRequest.getStartTime()));
			meetingRequestDto1.setDisableEdit(meetingRequest.getStartTime()
					.isBefore(new Second()));

			meetingRequestDtos.add(meetingRequestDto1);
		}

		return meetingRequestDtos;
	}

	@Override
	public void updateLocationForMeetings() {
		logger.warn("Updating Loactions for meeting");
		for (MeetingRequest meetingRequest : dao.get(MeetingRequest.class)) {
			if (meetingRequest.getRoom() != null) {
				Location location = meetingRequest.getRoom().getLocation();
				if (location != null) {
					meetingRequest.setLocation(location);
					meetingRequest.setConferenceType("Meeting");
					dao.update(meetingRequest);
				}
			}
		}
		logger.warn("Updating Loactions for meeting Complete");

	}

	@Override
	public List<EventTypeDto> getAllEventTypes() {
		return eventTypeBuilder.toDtoList(dao.get(EventType.class));
	}

	@Override
	public void addEventType(EventTypeDto dto) {
		EventType eventType = eventTypeBuilder.toEntity(dto);
		dao.save(eventType);
	}

	@Override
	public void updateEventType(EventTypeDto dto) {
		EventType eventType = dao.findBy(EventType.class, dto.getId());
		eventType.setName(dto.getName());
		dao.update(eventType);
	}

	@Override
	public void deleteEventType(Long eventTypeId) {
		dao.delete(dao.findBy(EventType.class, eventTypeId));

	}

	@Override
	public Map<String, Object> getAllEvents(Integer startIndex, Integer endIndex, String dateSelection,
			String searchFromDate, String searchToDate, Long eventTypeId,HttpServletRequest httpServletRequest) {

		Date startDate = null;
		Date endDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				startDate = DateParser.toDate(searchFromDate);
				endDate = DateParser.toDate(searchToDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		} else {
			Map<String, Date> dates = dao.getCustomDates(dateSelection);
			startDate = dates.get("startDate");
			endDate = dates.get("endDate");
		}
		DateRange dateRange = new DateRange(startDate, endDate);

		EventType eventType = null;
		if (eventTypeId != null) {
			eventType = dao.findBy(EventType.class, eventTypeId);
		}
		
		Map<String, Object> eventMap = meetingRequestDaoImpl
				.getAllEvents(startIndex, endIndex,dateRange, eventType);
		List<MeetingRequest> meetingRequests = (List<MeetingRequest>) eventMap.get("list");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", meetingRequestBuilder.toDTOList(meetingRequests,httpServletRequest));
		map.put("size", eventMap.get("size"));
		return map;
	}

	@Override
	public void cancelEvent(Long eventId) {

		MeetingRequest event = dao.findBy(MeetingRequest.class, eventId);
		event.setMeetingStatus("Cancelled");
		dao.update(event);

		List<MeetingRequest> meetingRequests = dao.getAllOfProperty(
				MeetingRequest.class, "eventId", eventId);
		for (MeetingRequest meetingRequest : meetingRequests) {
			meetingRequest.setMeetingStatus("Cancelled");
			dao.update(meetingRequest);
			try {
				cancelMeeting(meetingRequest.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		conferenceRoomMailConfiguration.sendCancelMeetingMail(eventId);

	}

	@Override
	public List<MeetingRequestDto> getEventsForDashBoard(HttpServletRequest httpServletRequest) {
		return meetingRequestBuilder.toDTOList(meetingRequestDaoImpl
				.getEventsForDashBoard(),httpServletRequest);
	}

	@Override
	public List<MeetingRequestDto> getTrainingsForDashBoard(HttpServletRequest httpServletRequest) {
		return meetingRequestBuilder.toDTOList(meetingRequestDaoImpl
				.getTrainingsForDashBoard(),httpServletRequest);
	}

	@Override
	public List<ReservedTimingsForEvent> getAllBookedDetailsForEvent(
			Date fromDate, Date toDate, Long roomId, HttpServletRequest httpServletRequest) {

		List<ReservedTimingsForEvent> events = new ArrayList<ReservedTimingsForEvent>();

		Room room = dao.findBy(Room.class, roomId);

		List<Date> dates = getDatesBetweenDateRange(fromDate, toDate);

		List<String> timings = null;

		ReservedTimingsForEvent reservedTimings = null;
		for (Date date : dates) {
			timings = new ArrayList<String>();
			reservedTimings = new ReservedTimingsForEvent();
			
			List<MeetingRequestDto> meetingRequestDtos = meetingRequestBuilder
					.toDTOList(meetingRequestDaoImpl.getAllMeetingRequests(
							date, room),httpServletRequest);
			
			for (MeetingRequestDto dto : meetingRequestDtos) {

				String meetingTimings = dto.getStartTime() + " to "
						+ dto.getEndTime();
				timings.add(meetingTimings);
			}
			if (!timings.isEmpty()) {
				reservedTimings.setDate(date.toString("dd/MM/yyyy"));
				reservedTimings.setTimings(timings);
				events.add(reservedTimings);
			}

		}

		return events;
	}

	@Override
	public void editEvent(MeetingRequestDto meetingRequestDto, HttpServletRequest httpServletRequest) {

		// Here updating Event
		MeetingRequest meetingRequest = meetingRequestDaoImpl.findBy(
				MeetingRequest.class, meetingRequestDto.getId());

		meetingRequest.setAgenda(meetingRequestDto.getAgenda());
		meetingRequest.setProjectName(meetingRequestDto.getProjectName());
		meetingRequest.setDescription(meetingRequestDto.getDescription());
		if(meetingRequestDto.getTrainerName() != null) {
			meetingRequest.setTrainerName(meetingRequestDto.getTrainerName().getId());
		}
		meetingRequestDaoImpl.update(meetingRequest);
		for (MeetingAttendees attendees : dao.getAllOfProperty(
				MeetingAttendees.class, "meetingRequest", meetingRequest)) {
			dao.delete(attendees);
		}
		List<MeetingEditDTO> meetingAttendeeslist = meetingRequestDto
				.getMeetingEditDTOList();

		if (!meetingAttendeeslist.isEmpty()) {
			for (MeetingEditDTO empId : meetingAttendeeslist) {
				MeetingAttendees meetingAttendees = new MeetingAttendees();
				meetingAttendees.setEmployee(dao.findBy(Employee.class,
						empId.getId()));
				meetingAttendees.setEmployeeAvailability(empId
						.getAvailability());
				meetingAttendees.setMeetingRequest(meetingRequest);
				meetingRequestDaoImpl.save(meetingAttendees);
			}
		}

		// here updating all event meetings
		List<MeetingRequest> eventMeetings = dao.getAllOfProperty(
				MeetingRequest.class, "eventId", meetingRequest.getId());
		//logger.warn("meetings list:"+eventMeetings.size());
		for (MeetingRequest eventMeeting : eventMeetings) {
			eventMeeting.setAgenda(meetingRequestDto.getAgenda());
			eventMeeting.setProjectName(meetingRequestDto.getProjectName());
			eventMeeting.setDescription(meetingRequestDto.getDescription());
			if(meetingRequestDto.getTrainerName() != null) {
			eventMeeting.setTrainerName(meetingRequestDto.getTrainerName().getId());
			}
			dao.update(eventMeeting);
			try {
				deleteExistingMeetingAttendeesandAddNewAndSendMail(
						eventMeeting, meetingRequestDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void deleteExistingMeetingAttendeesandAddNewAndSendMail(
			MeetingRequest meetingRequest, MeetingRequestDto dto)
			throws Exception {

		List<MeetingAttendees> attendees = dao.getAllOfProperty(
				MeetingAttendees.class, "meetingRequest", meetingRequest);
		List<Long> oldAttendeesIds = new ArrayList<>();
		
		//logger.warn("old Attendees List:"+attendees.size());
		// here deleting all old meeting attendeees
		for (MeetingAttendees attendees2 : attendees) {
			//logger.warn("old Attendees:"+attendees2.getEmployee().getFullName());
			dao.delete(attendees2);
			oldAttendeesIds.add(attendees2.getEmployee().getEmployeeId());
		}

		List<MeetingEditDTO> meetingAttendeeslist = dto.getMeetingEditDTOList();
		List<MeetingEditDTO> newMeetingAttendeesListAfterEdit = new ArrayList<>();
		//logger.warn("new Attendees List:"+meetingAttendeeslist.size());
		if (!meetingAttendeeslist.isEmpty()) {
			for (MeetingEditDTO empId : meetingAttendeeslist) {
				//logger.warn("new Attendees:"+empId.getId());
				MeetingAttendees meetingAttendees = new MeetingAttendees();
				meetingAttendees.setEmployee(dao.findBy(Employee.class,
						empId.getId()));
				meetingAttendees.setEmployeeAvailability(empId
						.getAvailability());
				meetingAttendees.setMeetingRequest(meetingRequest);
				meetingRequestDaoImpl.save(meetingAttendees);
				
			//List of new attendees after updating the event
				if(!oldAttendeesIds.contains(empId.getId())){
					newMeetingAttendeesListAfterEdit.add(empId);
				}
		}
			
			//sending mail to only new attendees added after updating the event
			if(!newMeetingAttendeesListAfterEdit.isEmpty()) {
				dto.setMeetingEditDTOList(newMeetingAttendeesListAfterEdit);
			
				String originalStartTime = dto.getStartTime();
				String originalEndTime = dto.getEndTime();
				String date = meetingRequest.getFromDate().toString(
						"dd/MM/yyyy");
				String startTime = date + "/"
						+ getFormattedTime(dto.getStartTime());
				String endTime = date + "/"
						+ getFormattedTime(dto.getEndTime());

				dto.setStartTime(startTime);
				dto.setEndTime(endTime);
				dto.setRoomId(meetingRequest.getRoom().getId());
				send(dto, meetingRequest.getId());
				dto.setStartTime(originalStartTime);
				dto.setEndTime(originalEndTime);
				dto.setMeetingEditDTOList(meetingAttendeeslist);
			}
			
		}
			

	}

	public String getFormattedTime(String time) {
		
		String space[] = time.split(" ");
		
		String time1[] = space[0].split(":");
		String hours = time1[0];
		String minutes = time1[1];
		
		if(space[1].equals("PM") && !time1[0].equals("12")) {
			hours = String.valueOf((Integer.parseInt(time1[0])+12));
		}

		String finalstring = hours + "/" + minutes;
		return finalstring.trim();

		/*String space[] = time.split(" ");
		String semi = space[0];
		// String slash[] = semi.split(":");
		String finalstring = semi.replace(":", "/");
		//logger.warn("Final String" + finalstring.trim());
		return finalstring.trim();*/

	}

	@Override
	public List<Object> getMeetingRequestsForEmployee() {
		
		return meetingRequestDaoImpl.getMeetingRequestsForEmp(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
	}

	@Override
	public void uploadFeedbackForm(MultipartFile mpf, String eventId) {
		if (eventId != null) {
			MeetingRequest meeting = dao.findBy(MeetingRequest.class, Long.parseLong(eventId));
			FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
			String path = null;
			try {
			 path = fileUploaderUtility.uploadFeedbackForm(meeting, mpf, propBean);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			Employee loggedInEmp = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
			
			FeedbackForm fb = new FeedbackForm();
			fb.setEvent(meeting);
			fb.setFeedbackFormPath(path);
			fb.setFeedBackFormName(mpf.getOriginalFilename());
			fb.setCreatedBy(loggedInEmp);
			fb.setCreatedDate(new Date());
			dao.save(fb);
		}
	}

	@Override
	public Map<String, Object> getFeedbackForms(Integer startIndex, Integer endIndex, Long eventId) {
		Map<String, Object> feedbackFormMap = meetingRequestDaoImpl.getFeedbackFormMap(startIndex,endIndex,eventId);
		List<FeedbackForm> feedbackFormList = (List<FeedbackForm>) feedbackFormMap.get("list");
		List<FeedbackFormDto> dtoList = new ArrayList<>();
		FeedbackFormDto dto = null;
		for(FeedbackForm form : feedbackFormList) {
			dto =new FeedbackFormDto();
			dto.setId(form.getId());
			dto.setEventId(form.getEvent().getId());
			dto.setFeedbackFormPath(form.getFeedbackFormPath());
			dto.setFeedBackFormName(form.getFeedBackFormName());
			Employee emp = dao.findBy(Employee.class, form.getCreatedBy().getEmployeeId());
			dto.setCreatedBy(emp.getEmployeeFullName());
			dto.setCreatedDate(form.getCreatedDate().toString("dd/MM/yyyy"));
			dtoList.add(dto);	
		}
		Map<String, Object> feedbackFormListMap = new HashMap<>();
		feedbackFormListMap.put("list", dtoList);
		feedbackFormListMap.put("size", feedbackFormMap.get("size"));
		return feedbackFormListMap;
	}

	

	@Override
	public void downloadFeedbackForm(String fileName, HttpServletResponse response) {
		try {
			FileUploaderUtil util = new FileUploaderUtil();
			util.downloadFeedbackForm(response, fileName, propBean);
		}
		catch (Exception ex) {
			throw new FileUploaderUtilException("Exception occured while downloading a file"
					+ ex.getMessage(),ex);
		}
		
	}

}
