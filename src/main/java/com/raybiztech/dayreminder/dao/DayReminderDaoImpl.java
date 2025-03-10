package com.raybiztech.dayreminder.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.date.Date;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;

@Repository("DayReminderDaoImpl")
public class DayReminderDaoImpl extends DAOImpl implements DayReminderDao {


	@Override
	public List<CandidateInterviewCycle> getWhatsappNumbersofCandidate() {
		Date today = new Date();
		System.out.println("today's Date:" + today);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				CandidateInterviewCycle.class);
		criteria.add(Restrictions.eq("interviewDate", today));/*
		criteria.add(Restrictions.isNotNull("interviewTime"));*/
		criteria.addOrder(Order.desc("interviewCycleId"));
		System.out.println("list size:" + criteria.list().size());
		return criteria.list();
	}
	
	@Override
	public List<CandidateInterviewCycle> getWhatsappNumbersofCandidateforTomorrow() {
		Date date = new Date().next();
		System.out.println("date :" + date);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				CandidateInterviewCycle.class);
		criteria.add(Restrictions.eq("interviewDate", date));
		criteria.addOrder(Order.desc("interviewCycleId"));
		System.out.println("list size:" + criteria.list().size());
		return criteria.list();
	}
}