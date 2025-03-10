/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.meetingrequest.builder;

import com.raybiztech.meetingrequest.business.MeetingAttendees;
import com.raybiztech.meetingrequest.dto.MeetingAttendeesDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 *
 * @author sravani
 */
@Component("meetimnAttendeesBuilder")
public class MeetingAttendeesBuilder {

    public MeetingAttendees toEntity(MeetingAttendeesDto meetingAttendeesDto) {

        return null;

    }

    public List<MeetingAttendees> getmeetingAttendeesEntity(List<Long> employeeIds, Long meetingRequestId) {
        List<MeetingAttendees> meetingAttendeeses = null;
        MeetingAttendees meetingAttendees = null;
        if (!employeeIds.isEmpty()) {
            meetingAttendeeses = new ArrayList<MeetingAttendees>();
            for (Long empId : employeeIds) {
//                meetingAttendees.setMeetingRequestId(meetingRequestId);
//                meetingAttendees.setEmployeeId(empId);
//                meetingAttendeeses.add(meetingAttendees);
            }
        }
        return meetingAttendeeses;
    }

}
