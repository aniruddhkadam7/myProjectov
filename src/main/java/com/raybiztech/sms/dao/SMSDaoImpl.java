package com.raybiztech.sms.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.date.Second;
import com.raybiztech.meetingrequest.business.MeetingRequest;
import com.raybiztech.projectmanagement.invoice.utility.HibernateSupressWaringsUtil;
import com.raybiztech.sms.business.SmsTemplates;

@Repository("smsDaoImpl")
public class SMSDaoImpl extends DAOImpl implements SMSDao {

	@Override
	public List<MeetingRequest> getMeetingsWhichStartsInNextFifteenMinutes() {

		Second currentTime = new Second();
		Second timeAfterFifteenMin = new Second(
				System.currentTimeMillis() + 15 * 60 * 1000);

		// System.out.println(" currentTime " + currentTime);
		// System.out.println("timeAfterFifteenMin " + timeAfterFifteenMin);

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				MeetingRequest.class);
		criteria.add(Restrictions.between("startTime", currentTime,
				timeAfterFifteenMin));
		// criteria.add(Restrictions.isNotNull("meetingAttendees"));

		return HibernateSupressWaringsUtil.listAndCast(criteria);
	}

	@Override
	public String getMeetingSMSAlertContent(String content) {
		SmsTemplates smsTemplates = (SmsTemplates) getSessionFactory()
				.getCurrentSession().createCriteria(SmsTemplates.class)
				.add(Restrictions.eq("type", content)).uniqueResult();
		return smsTemplates.getTemplate();
	}

}
