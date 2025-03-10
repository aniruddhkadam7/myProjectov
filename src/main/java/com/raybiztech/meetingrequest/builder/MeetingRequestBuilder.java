/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.meetingrequest.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.meetingrequest.business.EventType;
import com.raybiztech.meetingrequest.business.Location;
import com.raybiztech.meetingrequest.business.MeetingAttendees;
import com.raybiztech.meetingrequest.business.MeetingRequest;
import com.raybiztech.meetingrequest.business.Room;
import com.raybiztech.meetingrequest.dao.MeetingRequestDao;
import com.raybiztech.meetingrequest.dto.MeetingEditDTO;
import com.raybiztech.meetingrequest.dto.MeetingRequestDto;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.utils.SecondParser;

/**
 *
 * @author sravani
 */

@Component("meetingRequestBuilder")
public class MeetingRequestBuilder {

	org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(MeetingRequestBuilder.class);

	@Autowired
	RoomBuilder roomBuilder;
	@Autowired
	LocationBuilder locationBuilder;
	@Autowired
	MeetingRequestDao meetingRequestDaoImpl;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	EmployeeBuilder employeeBuilder;

	public MeetingRequest toEntity(MeetingRequestDto meetingRequestDto) {
		MeetingRequest meetingRequest = null;
		if (meetingRequestDto != null) {
			meetingRequest = new MeetingRequest();
			meetingRequest.setAgenda(meetingRequestDto.getAgenda());
			meetingRequest.setEndTime(SecondParser.toSecond(meetingRequestDto
					.getEndTime()));
			try {
				meetingRequest.setFromDate(DateParser.toDate(meetingRequestDto
						.getFromDate()));
				meetingRequest.setToDate(DateParser.toDate(meetingRequestDto
						.getToDate()));
			} catch (ParseException ex) {
				Logger.getLogger(MeetingRequestBuilder.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			meetingRequest.setId(meetingRequestDto.getId());
			meetingRequest.setProjectName(meetingRequestDto.getProjectName());
			meetingRequest.setStartTime(SecondParser.toSecond(meetingRequestDto
					.getStartTime()));

			if (meetingRequestDto.getRoomId() != null) {
				meetingRequest.setRoom(meetingRequestDaoImpl.findBy(Room.class,
						meetingRequestDto.getRoomId()));
			}
			meetingRequest.setLocation(meetingRequestDaoImpl.findBy(
					Location.class, meetingRequestDto.getLocationId()));

			meetingRequest.setAuthorName(String.valueOf(meetingRequestDto
					.getAuthorName().getId()));
			meetingRequest.setReservedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			
			if(meetingRequestDto.getTrainerName() != null) {
			meetingRequest.setTrainerName(meetingRequestDto.getTrainerName().getId());
			}
			
			meetingRequest.setMeetingStatus("New");
			meetingRequest.setConferenceType(meetingRequestDto
					.getConferenceType());
			if (meetingRequestDto.getEventTypeId() != null) {
				meetingRequest.setEventType(meetingRequestDaoImpl.findBy(
						EventType.class, meetingRequestDto.getEventTypeId()));
			}
			meetingRequest.setEventId(meetingRequestDto.getEventId());
			meetingRequest.setEventLocation(meetingRequestDto
					.getEventLocation());
			meetingRequest.setDescription(meetingRequestDto.getDescription());

		}

		return meetingRequest;
	}

	public MeetingRequestDto toDto(MeetingRequest meetingRequest, HttpServletRequest httpServletRequest) {
		TenantTypes tenantKey = TenantContextHolder.getTenantType();
		/*Long LoggedInempId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		Employee employee = meetingRequestDaoImpl.findBy(Employee.class,
				LoggedInempId);*/

		MeetingRequestDto meetingRequestDto = null;
		if (meetingRequest != null) {
			meetingRequestDto = new MeetingRequestDto();
			meetingRequestDto.setAgenda(meetingRequest.getAgenda());
			meetingRequestDto.setMeetingStatus(meetingRequest
					.getMeetingStatus());

			meetingRequestDto.setId(meetingRequest.getId());
			meetingRequestDto.setProjectName(meetingRequest.getProjectName());
			if (meetingRequest.getRoom() != null) {
				meetingRequestDto.setRoomId(meetingRequest.getRoom().getId());
				meetingRequestDto.setRoomName(meetingRequest.getRoom()
						.getRoomName());
			}

			meetingRequestDto.setLocationName(meetingRequest.getLocation()
					.getLocationName());
			meetingRequestDto.setLocationId(meetingRequest.getLocation()
					.getId());
			meetingRequestDto.setConferenceType(meetingRequest
					.getConferenceType());
			// meetingRequestDto.setEventType(meetingRequest.getEventType());

			if (meetingRequest.getEventType() != null) {
				meetingRequestDto.setEventTypeId(meetingRequest.getEventType()
						.getId());
				meetingRequestDto.setEventTypeName(meetingRequest
						.getEventType().getName());
			}

			meetingRequestDto.setEventLocation(meetingRequest
					.getEventLocation());
			meetingRequestDto.setEventId(meetingRequest.getEventId());


			meetingRequestDto.setToDate(DateParser.toString(meetingRequest
					.getToDate()));
			
			List<MeetingAttendees> meetingAttendeeses = meetingRequestDaoImpl
					.getAttendees(meetingRequest.getId());
			List<Long> empIds = new ArrayList<Long>();
			List<String> employeeNames = new ArrayList<String>();
			List<String> empDesignations = new ArrayList<String>();
			
			//HashMap<Long, String> designation = new HashMap<Long, String>();
			
			List<SearchEmpDetailsDTO> empdetails = new ArrayList<SearchEmpDetailsDTO>();

			for (MeetingAttendees meetingAttendees : meetingAttendeeses) {
				empIds.add(meetingAttendees.getEmployee().getEmployeeId());
				employeeNames.add(meetingAttendees.getEmployee().getFullName());
				empDesignations.add(meetingAttendees.getEmployee().getDesignation());
				empdetails.add(employeeBuilder.createEmployeeDTOForSearch(meetingAttendees.getEmployee()));
			}
		
			meetingRequestDto.setEmployeeIds(empIds);
			meetingRequestDto.setEmployeeNames(employeeNames);
			meetingRequestDto.setEmpDesignations(empDesignations);
			meetingRequestDto.setEmployeeDto(empdetails);

			meetingRequestDto.setFromDate(DateParser.toString(meetingRequest
					.getFromDate()));

			meetingRequestDto
					.setEndTime(twelveHoursFormate(meetingRequest.getEndTime()
							.getHourOfDay().getValue()
							+ ":"
							+ meetingRequest.getEndTime().getMinuteOfHour()
									.getValue()));

			meetingRequestDto.setStartTime(twelveHoursFormate(meetingRequest
					.getStartTime().getHourOfDay().getValue()
					+ ":"
					+ meetingRequest.getStartTime().getMinuteOfHour()
							.getValue()));
			meetingRequestDto.setDescription(meetingRequest.getDescription());
			meetingRequestDto.setTimeFomrat(SecondParser
					.toString(meetingRequest.getStartTime()));
			meetingRequestDto.setDisableEdit(meetingRequest.getStartTime()
					.isBefore(new Second()));
			if(meetingRequest.getTrainerName() != null) {
			Employee trainer = meetingRequestDaoImpl.findBy(Employee.class, meetingRequest.getTrainerName());
			meetingRequestDto.setTrainerName(employeeBuilder
					.createEmployeeDTOForSearch(trainer));
			}
			
			Employee employee2 = meetingRequestDaoImpl.findBy(Employee.class,
					Long.parseLong(meetingRequest.getAuthorName()));

			meetingRequestDto.setAuthorName(employeeBuilder
					.createEmployeeDTOForSearch(employee2));
			
			//  here tenant is set because to get logged in employee details for current tenant based on which authorized is set

			TenantContextHolder.setTenantType(TenantTypes.valueOf(httpServletRequest.getHeader("tenantKey")));
			Long LoggedInempId = securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder();
			
			Employee employee = meetingRequestDaoImpl.findBy(Employee.class,
					LoggedInempId);
			
			if (employee2.getFullName().equalsIgnoreCase(employee.getFullName())) {

				meetingRequestDto.setIsAuthorisedUser(Boolean.TRUE);

			} else {
				meetingRequestDto.setIsAuthorisedUser(Boolean.FALSE);
			}

			Permission permission = meetingRequestDaoImpl.checkForPermission(
					"New Event", employee);

			if (permission.getUpdate()
					|| employee2.getFullName().equalsIgnoreCase(employee.getFullName())) {
				meetingRequestDto.setEventEditAccess(Boolean.TRUE);
			} else {
				meetingRequestDto.setEventEditAccess(Boolean.FALSE);
			}

			TenantContextHolder.setTenantType(tenantKey);
		}

		return meetingRequestDto;
	}

	public List<MeetingRequestDto> toDTOList(
			List<MeetingRequest> meetingRequestList, HttpServletRequest httpServletRequest) {

		List<MeetingRequestDto> meetingrequestDtoList = null;
		if (meetingRequestList != null) {
			meetingrequestDtoList = new ArrayList<MeetingRequestDto>();
			for (MeetingRequest meetingRequest : meetingRequestList) {
				meetingrequestDtoList.add(toDto(meetingRequest,httpServletRequest));
			}

		}

		return meetingrequestDtoList;
	}

	public String twelveHoursFormate(String time) {

		String[] individualTime = time.split(":");

		if (individualTime[0].equals("0")) {
			individualTime[0] = individualTime[0].concat("0");
		}

		if (individualTime[1].equals("0")) {
			individualTime[1] = individualTime[1].concat("0");
		}

		String convertedTime = null;
		if (Integer.parseInt(individualTime[0]) > 12) {
			/*
			 * convertedTime = String
			 * .valueOf(Integer.parseInt(individualTime[0]) - 12)
			 * .concat(":").concat(individualTime[1]).concat(" PM");
			 */

			String hour = getRoundedTime(Integer.parseInt(individualTime[0]) - 12);
			String minute = getRoundedTime(Integer.parseInt(individualTime[1]));

			convertedTime = hour.concat(":").concat(minute).concat(" PM");

		} else if (Integer.parseInt(individualTime[0]) == 12) {

			String hour = getRoundedTime(Integer.parseInt(individualTime[0]));
			String minute = getRoundedTime(Integer.parseInt(individualTime[1]));

			convertedTime = hour.concat(":").concat(minute).concat(" PM");

			/*
			 * convertedTime = individualTime[0].concat(":")
			 * .concat(individualTime[1]).concat(" PM");
			 */
		} else {

			String hour = getRoundedTime(Integer.parseInt(individualTime[0]));
			String minute = getRoundedTime(Integer.parseInt(individualTime[1]));

			convertedTime = hour.concat(":").concat(minute).concat(" AM");

			/*
			 * convertedTime = individualTime[0].concat(":")
			 * .concat(individualTime[1]).concat(" AM");
			 */
		}

		return convertedTime;
	}

	public String getRoundedTime(int time) {
		String stringTime = String.valueOf(time);
		stringTime = (stringTime.length() == 1) ? ("0".concat(stringTime))
				: stringTime;
		return stringTime;
	}

	// for edit meeting room
	public MeetingRequestDto toEditEntity(MeetingRequest meetingRequest) {

		Long LoggedInempId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		Employee employee = meetingRequestDaoImpl.findBy(Employee.class,
				LoggedInempId);

		String fullName = employee.getFullName();
		MeetingRequestDto meetingRequestDto = null;
		if (meetingRequest != null) {
			meetingRequestDto = new MeetingRequestDto();
			meetingRequestDto.setAgenda(meetingRequest.getAgenda());

			meetingRequestDto.setId(meetingRequest.getId());
			meetingRequestDto.setProjectName(meetingRequest.getProjectName());
			meetingRequestDto.setRoomId(meetingRequest.getRoom().getId());
			meetingRequestDto.setRoomName(meetingRequest.getRoom()
					.getRoomName());

			meetingRequestDto.setLocationName(meetingRequest.getLocation()
					.getLocationName());
			meetingRequestDto.setLocationId(meetingRequest.getLocation()
					.getId());

			meetingRequestDto.setConferenceType(meetingRequest
					.getConferenceType());
			// meetingRequestDto.setEventType(meetingRequest.getEventType());

			if (meetingRequest.getEventType() != null) {
				meetingRequestDto.setEventTypeId(meetingRequest.getEventType()
						.getId());
				meetingRequestDto.setEventTypeName(meetingRequest
						.getEventType().getName());
			}

			meetingRequestDto.setEventLocation(meetingRequest
					.getEventLocation());
			meetingRequestDto.setEventId(meetingRequest.getEventId());

			Employee employee2 = meetingRequestDaoImpl.findBy(Employee.class,
					Long.parseLong(meetingRequest.getAuthorName()));

			meetingRequestDto.setAuthorName(employeeBuilder
					.createEmployeeDTOForSearch(employee2));

			Employee employee3 = meetingRequestDaoImpl.findBy(Employee.class,
					Long.valueOf(meetingRequest.getAuthorName()));
			if (employee.getRole().equalsIgnoreCase("admin")
					|| employee3.getFullName().equalsIgnoreCase(fullName)) {

				meetingRequestDto.setIsAuthorisedUser(Boolean.TRUE);

			} else {
				meetingRequestDto.setIsAuthorisedUser(Boolean.FALSE);
			}

			List<MeetingAttendees> meetingAttendeeses = meetingRequestDaoImpl
					.getAttendees(meetingRequest.getId());
			List<Long> empIds = new ArrayList<Long>();
			List<String> employeeNames = new ArrayList<String>();

			List<MeetingEditDTO> meetingEditdtos = new ArrayList<MeetingEditDTO>();
			for (MeetingAttendees meetingAttendees : meetingAttendeeses) {
				MeetingEditDTO meetingEditDTO = new MeetingEditDTO();

				meetingEditDTO.setId(meetingAttendees.getEmployee()
						.getEmployeeId());

				meetingEditDTO.setFullName(meetingAttendees.getEmployee()
						.getFullName());
				meetingEditDTO.setAvailability(meetingAttendees
						.getEmployeeAvailability());
				meetingEditDTO.setFlag(meetingAttendees
						.getEmployeeAvailability());
				meetingEditdtos.add(meetingEditDTO);

				empIds.add(meetingAttendees.getEmployee().getEmployeeId());
				employeeNames.add(meetingAttendees.getEmployee().getFullName());
			}
			// meetingRequestDto.setMeetingAttendeesDto(meetingatten);
			meetingRequestDto.setMeetingEditDTOList(meetingEditdtos);
			// meetingRequestDto.setEmployeeIds(empIds);
			meetingRequestDto.setEmployeeNames(employeeNames);

			meetingRequestDto.setFromDate(meetingRequest.getFromDate()
					.toString("dd/MM/yyyy"));

			if (meetingRequest.getToDate() != null) {
				meetingRequestDto.setToDate(meetingRequest.getToDate()
						.toString("dd/MM/yyyy"));
			}

			meetingRequestDto
					.setEndTime(twelveHoursFormate(meetingRequest.getEndTime()
							.getHourOfDay().getValue()
							+ ":"
							+ meetingRequest.getEndTime().getMinuteOfHour()
									.getValue()));

			meetingRequestDto.setStartTime(twelveHoursFormate(meetingRequest
					.getStartTime().getHourOfDay().getValue()
					+ ":"
					+ meetingRequest.getStartTime().getMinuteOfHour()
							.getValue()));

			meetingRequestDto.setDescription(meetingRequest.getDescription());
			if(meetingRequest.getTrainerName() != null) {
			Employee trainer = meetingRequestDaoImpl.findBy(Employee.class, meetingRequest.getTrainerName());
			meetingRequestDto.setTrainerName(employeeBuilder
					.createEmployeeDTOForSearch(trainer));
			}

		}

		return meetingRequestDto;
	}

	public void validateMeetingRequestStatus(Location location, Room room,
			Date date) {
		TenantTypes[] list = TenantTypes.class.getEnumConstants();
		for(TenantTypes type : list) {
			TenantContextHolder.setTenantType(type);

		List<MeetingRequest> meetingRequests = meetingRequestDaoImpl
				.getAllNewStatusMeetingRequest(location, room, date);

		if (!meetingRequests.isEmpty()) {
			for (MeetingRequest meetingRequest : meetingRequests) {
				if (meetingRequest.getEndTime().isBefore(new Second())) {
					meetingRequest.setMeetingStatus("Completed");
					meetingRequestDaoImpl.update(meetingRequest);
				}
			}
		}
	}
  }

}
