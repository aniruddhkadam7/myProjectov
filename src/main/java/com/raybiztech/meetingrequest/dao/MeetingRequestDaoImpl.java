package com.raybiztech.meetingrequest.dao;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import com.raybiztech.meetingrequest.builder.MeetingRequestBuilder;
import com.raybiztech.meetingrequest.business.EventType;
import com.raybiztech.meetingrequest.business.FeedbackForm;
import com.raybiztech.meetingrequest.business.Location;
import com.raybiztech.meetingrequest.business.MeetingAttendees;
import com.raybiztech.meetingrequest.business.MeetingRequest;
import com.raybiztech.meetingrequest.business.Room;
import com.raybiztech.meetingrequest.dto.MeetingRequestDto;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.recruitment.utils.DateParser;

@Repository("meetingRequestDaoImpl")
public class MeetingRequestDaoImpl extends DAOImpl implements MeetingRequestDao {
	@Autowired
	MeetingRequestBuilder meetingRequestBuilder;

	Logger logger = Logger.getLogger(MeetingRequestDaoImpl.class);

	@Override
	public List<AllocationDetails> attendiesOfProjectList(Long id) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.createAlias("project", "project")
				.setFetchMode("project", FetchMode.JOIN)
				.add(Restrictions.eq("project.id", id))
				.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<MeetingAttendees> getAttendees(Long meetingRequestId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MeetingAttendees.class);
		criteria.createAlias("meetingRequest", "meetingRequest");
		criteria.add(Restrictions.eq("meetingRequest.id", meetingRequestId));
		return criteria.list();

	}

	@Override
	public List<Room> getAllRoomsOfLocation(Long loactionId) {

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Room.class);
		criteria.createAlias("location", "location");
		criteria.add(Restrictions.eq("roomStatus", Boolean.TRUE));
		return criteria.add(Restrictions.eq("location.id", loactionId)).list();

	}

	@Override
	public List<MeetingRequest> getAllMeetingRequests(Date date, Room room) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MeetingRequest.class);
		criteria.add(Restrictions.eq("fromDate", date));
		criteria.add(Restrictions.eq("room", room));
		criteria.add(Restrictions.ne("meetingStatus", "Cancelled"));
		criteria.add(Restrictions.eq("conferenceType", "Meeting"));
		criteria.addOrder(Order.asc("startTime"));
		return criteria.list();
	}

	@Override
	public List<MeetingRequest> getAllMeetingRequestsWhileEdit(Long meetingId,
			Date date, Room room) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MeetingRequest.class);
		criteria.add(Restrictions.ne("id", meetingId));
		criteria.add(Restrictions.eq("fromDate", date));
		criteria.add(Restrictions.eq("room", room));
		criteria.add(Restrictions.ne("meetingStatus", "Cancelled"));
		criteria.add(Restrictions.eq("conferenceType", "Meeting"));
		criteria.addOrder(Order.asc("startTime"));
		return criteria.list();
	}

	@Override
	public List<MeetingRequest> getTimeForSelectedLocation(MeetingRequestDto dto) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(MeetingRequest.class);
		Date date = null;
		try {
			date = DateParser.toDate(dto.getFromDate());
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
		criteria.createAlias("room", "room");
		criteria.add(Restrictions.eq("room.id", dto.getRoomId()));
		criteria.add(Restrictions.eq("fromDate", date));
		criteria.add(Restrictions.ne("meetingStatus", "Cancelled"));
		criteria.add(Restrictions.eq("conferenceType", "Meeting"));
		return criteria.list();

	}

	@Override
	public List<MeetingRequest> getMeetingsFor(Date date, Room room) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(MeetingRequest.class);
		criteria.add(Restrictions.eq("room", room));
		criteria.add(Restrictions.eq("fromDate", date));
		criteria.add(Restrictions.ne("meetingStatus", "Cancelled"));
		criteria.add(Restrictions.eq("conferenceType", "Meeting"));
		return criteria.list();

	}

	@Override
	public List<MeetingAttendees> changeMeetingStatus(Long id) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(MeetingAttendees.class)
				.createAlias("meetingRequest", "meetingRequest")
				.add(Restrictions.eq("meetingRequest.id", id));

		return criteria.list();

	}

	@Override
	public List<MeetingRequest> uniqueAttendee(Second startTime, Second endTime) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(MeetingRequest.class);

		Criterion criterion1 = Restrictions.and(
				Restrictions.le("startTime", startTime),
				Restrictions.ge("endTime", endTime));
		Criterion criterion2 = Restrictions.or(
				Restrictions.between("startTime", startTime, endTime),
				Restrictions.between("endTime", startTime, endTime));
		Criterion between = Restrictions.or(criterion1, criterion2);
		criteria.add(between);
		return criteria.list();

	}

	@Override
	public List<MeetingRequest> getBookingsForSelection(Location location,
			Room room, Date date, String meetingstatus) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MeetingRequest.class);

		if (room != null) {
			criteria.add(Restrictions.eq("room", room));
			criteria.add(Restrictions.eq("fromDate", date));
			criteria.add(Restrictions.eq("conferenceType", "Meeting"));
			criteria.add(Restrictions.eq("meetingStatus", meetingstatus));
		} else if (location != null) {
			criteria.createAlias("room", "room");
			criteria.add(Restrictions.eq("room.location", location));
			criteria.add(Restrictions.eq("fromDate", date));
			criteria.add(Restrictions.eq("conferenceType", "Meeting"));
			criteria.add(Restrictions.eq("meetingStatus", meetingstatus));
		}
		criteria.addOrder(Order.asc("room"));
		criteria.addOrder(Order.asc("startTime"));

		return criteria.list();
	}

	@Override
	public Map<String, Object> getAllEvents(Integer startIndex, Integer endIndex,DateRange dateRange,
			EventType eventTypeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MeetingRequest.class);
		criteria.add(Restrictions.eq("conferenceType", "Event"));
		criteria.add(Restrictions.ne("meetingStatus", "Cancelled"));

		if (eventTypeId != null) {
			criteria.add(Restrictions.eq("eventType", eventTypeId));
		}

		Criterion fromDateCriterion = Restrictions.between("fromDate",
				dateRange.getMinimum(), dateRange.getMaximum());
		Criterion toDateCriterion = Restrictions.between("toDate",
				dateRange.getMinimum(), dateRange.getMaximum());

		criteria.add(Restrictions.or(fromDateCriterion, toDateCriterion));
		
		criteria.createAlias("location", "location");
		criteria.add(Restrictions.eq("location.locationStatus", Boolean.TRUE));
		
		
		criteria.addOrder(Order.desc("id"));
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("size",criteria.list().size());
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		map.put("list", criteriaMap.get("list"));
		return map;

	}

	@Override
	public List<MeetingRequest> getEventsForDashBoard() {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MeetingRequest.class);
		criteria.createAlias("eventType", "eventType");
		criteria.add(Restrictions.ne("eventType.name", "Training"));
		criteria.add(Restrictions.eq("conferenceType", "Event"));
		criteria.add(Restrictions.ne("meetingStatus", "Cancelled"));

		Criterion criterion = Restrictions.ge("fromDate", new Date());
		Criterion criterion2 = Restrictions.ge("toDate", new Date());
		criteria.add(Restrictions.or(criterion, criterion2));
		criteria.addOrder(Order.asc("fromDate"));
		criteria.addOrder(Order.asc("startTime"));
		criteria.setMaxResults(3);
		return criteria.list();
	}

	@Override
	public List<MeetingRequest> getTrainingsForDashBoard() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MeetingRequest.class);
		criteria.add(Restrictions.eq("conferenceType", "Event"));
		criteria.add(Restrictions.ne("meetingStatus", "Cancelled"));
		criteria.createAlias("eventType", "eventType");
		criteria.add(Restrictions.eq("eventType.name", "Training"));
		Criterion criterion = Restrictions.ge("fromDate", new Date());
		Criterion criterion2 = Restrictions.ge("toDate", new Date());
		criteria.add(Restrictions.or(criterion, criterion2));
		criteria.addOrder(Order.asc("fromDate"));
		criteria.addOrder(Order.asc("startTime"));
		criteria.setMaxResults(3);
		return criteria.list();
	}

	@Override
	public List<MeetingRequest> getAllNewStatusMeetingRequest(
			Location location, Room room, Date date) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MeetingRequest.class);

		if (room != null) {
			criteria.add(Restrictions.eq("room", room));
			criteria.add(Restrictions.eq("fromDate", date));
			criteria.add(Restrictions.eq("conferenceType", "Meeting"));
			criteria.add(Restrictions.or(
					Restrictions.eq("meetingStatus", "New"),
					Restrictions.eq("meetingStatus", "In Progress")));
		} else if (location != null) {
			criteria.createAlias("room", "room");
			criteria.add(Restrictions.eq("room.location", location));
			criteria.add(Restrictions.eq("fromDate", date));
			criteria.add(Restrictions.eq("conferenceType", "Meeting"));
			criteria.add(Restrictions.or(
					Restrictions.eq("meetingStatus", "New"),
					Restrictions.eq("meetingStatus", "In Progress")));
		}
		criteria.addOrder(Order.asc("room"));
		criteria.addOrder(Order.asc("startTime"));

		return criteria.list();

	}

	@Override
	public List<Object> getMeetingRequestsForEmp(Long employeeId) {
		String query = "SELECT  mr.`agenda`,TIME_FORMAT(Time(mr.`startTime`), '%h:%i%p') as startTime , TIME_FORMAT(Time(mr.`endTime`), '%h:%i%p') as endTime FROM `MeetingRequest` mr INNER JOIN `MeetingAttendees` ma ON\n" + 
				"mr.`id` = ma.`meetingRequestId` WHERE date(mr.`StartDate`) = '"+new Date().toString("yyyy-MM-dd")+"' and ma.`EmployeeId` = '"+employeeId+"' ORDER by mr.`id` ASC LIMIT 3";
		//TIME_FORMAT('15:02:28', '%h:%i%p');
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
		
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Object> list = sqlQuery.list();
		
		return list;
	}

	@Override
	public Map<String, Object> getFeedbackFormMap(Integer startIndex, Integer endIndex, Long eventId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(FeedbackForm.class);
		criteria.createAlias("event", "meetingRequest");
		criteria.add(Restrictions.eq("meetingRequest.id", eventId));
		criteria.addOrder(Order.desc("id"));
		Map<String, Object> feedbackFormMap = new HashMap<String, Object>();
		feedbackFormMap.put("size",criteria.list().size());
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		feedbackFormMap.put("list", criteriaMap.get("list"));
		return feedbackFormMap;
		
	}
}
