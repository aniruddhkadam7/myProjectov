package com.raybiztech.compliance.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.compliance.business.Compliance;
import com.raybiztech.compliance.business.ComplianceTask;
import com.raybiztech.compliance.business.ComplianceTaskStatus;
import com.raybiztech.compliance.business.Recurring;
import com.raybiztech.date.Calendar;
import com.raybiztech.date.Date;

@Repository("complianceDaoImpl")
public class ComplianceDaoImpl extends DAOImpl implements ComplianceDao {

	@Override
	public Map<String, Object> getDepartmentWiseTasks(Integer fromIndex, Integer toIndex, EmpDepartment department) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ComplianceTask.class);
		criteria.add(Restrictions.eq("complianceTaskStatus", ComplianceTaskStatus.INPROGRESS));
		criteria.createAlias("compliance", "compliance");
		criteria.add(Restrictions.eq("compliance.department", department));
		criteria.addOrder(Order.desc("createdDate"));
		Map<String, Object> complianceTaskList = getPaginationList(criteria, fromIndex, toIndex);
		complianceTaskList.put("complianceTaskList", complianceTaskList.remove("list"));
		complianceTaskList.put("complianceTaskListSize", complianceTaskList.remove("listSize"));
		return complianceTaskList;
	}

	@Override
	public Map<String, Object> getAllComplainceTasks(Integer fromIndex, Integer toIndex) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ComplianceTask.class);
		criteria.add(Restrictions.eq("complianceTaskStatus", ComplianceTaskStatus.INPROGRESS));
		criteria.addOrder(Order.desc("createdDate"));
		Map<String, Object> complianceTaskList = getPaginationList(criteria, fromIndex, toIndex);
		Object list = complianceTaskList.remove("list");
		complianceTaskList.put("complianceTaskList", list);
		complianceTaskList.put("complianceTaskListSize", complianceTaskList.remove("listSize"));

		return complianceTaskList;
	}

	// only call this method if u r sure of having one ComplianceTask for Compliance
	@Override
	public ComplianceTask getComplianceTask(Compliance compliance) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ComplianceTask.class);

		criteria.add(Restrictions.eq("compliance", compliance));

		return (ComplianceTask) criteria.uniqueResult();
	}

	@Override
	public List<ComplianceTask> getLastComplianceTask(String lastMonth, String lastWeek, String yesterday) {
		/*
		 * If last month and current month having same no .of days then directly we load
		 * records whose createdDate exactly one month back from today
		 * 
		 * If current month having lesser days than last month then on the last day of current month we will load records 
		 * whose createdDate from exactly one month back from today to the last Month last Day
		 * 
		 * If current month having more days than last month then we will stop loading records when
		 * today is after the day one month forward to the lastMonth Last Day 
		 */

		System.out.println("last week"+lastWeek+"yesterday"+yesterday);
		// Loading Compliance tasks According to the Last Created ComplianceTasks
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ComplianceTask.class);
		String alias = criteria.getAlias() + "_.";
		Criteria complianceCriteria = criteria.createCriteria("compliance");
		
		Map<String, String> lastMonthDates = isCurrentMonthGreater();
		Criterion monthly = Restrictions.and(
				Restrictions.sqlRestriction("date(" + alias + "created_date)='" + lastMonth + "'"),
				Restrictions.eq("recurring",Recurring.MONTHLY));

		if (lastMonthDates.get("status").equalsIgnoreCase("lesser")
				&& lastMonthDates.get("exactOneMonthBack") != null) {
			String exactBack = lastMonthDates.get("exactOneMonthBack");
			String lastMonthLastDay = lastMonthDates.get("lastMonthLastDay");
			// Criterion lastMonthGreater
			monthly = Restrictions.and(Restrictions.sqlRestriction(
					"date(" + alias + "created_date) between '" + exactBack + "' and '" + lastMonthLastDay + "'"), Restrictions.eq("recurring",Recurring.MONTHLY));

		}

		System.out.println("last Week"+lastWeek);
		Criterion weekly = Restrictions.and(
				Restrictions.sqlRestriction("date(" + alias + "created_date)='" + lastWeek + "'"),
				Restrictions.eq("recurring",Recurring.WEEKLY));
		System.out.println("yesterday"+yesterday);
		Criterion daily = Restrictions.and(
				Restrictions.sqlRestriction("date(" + alias + "created_date)='" + yesterday + "'"),
				Restrictions.eq("recurring",Recurring.DAILY));

		// adding the Criterions
		Disjunction disjunction = Restrictions.disjunction();

		if (!lastMonthDates.get("status").equals("greater")) {
			disjunction.add(monthly);
		}
		disjunction.add(weekly);
		disjunction.add(daily);
		complianceCriteria.add(disjunction);

		return criteria.list();
	}

	@Override
	@Transactional
	public List<ComplianceTask> getMailComplianceTasks() {
		// Loading Compliance tasks According to the Compliance Before Notification and
		// Compliance Date

		// query Based
		/*
		 * SELECT ct.* FROM `ComplianceTask` ct INNER JOIN `Compliance` c ON
		 * ct.`compliance_id` = c.`compliance_id` WHERE
		 * date(DATE_SUB(ct.`compliance_date`, INTERVAL c.`before_notification` DAY)) <=
		 * '2017-10-30' and date(ct.`compliance_date`)>='2017-10-30'
		 */

		System.out.println("Came request");
		Date today = new Date();
		String todayIfan = today.toString("yyyy-MM-dd");

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ComplianceTask.class);
		String rootAlias = criteria.getAlias();

		criteria.add(Restrictions.eq("complianceTaskStatus", ComplianceTaskStatus.INPROGRESS));

		Criteria complianceCriteria = criteria.createCriteria("compliance");
		//Getting all Daily ComplianceTasks
		complianceCriteria.add(Restrictions.eq("recurring", Recurring.DAILY));
		Criterion dateCriterion = Restrictions.sqlRestriction(
				"date(DATE_SUB(" + rootAlias + "_.compliance_date , INTERVAL {alias}.before_notification DAY)) <='"
						+ todayIfan + "' and date(" + rootAlias + "_.compliance_date) >= '" + todayIfan + "'");
		//Compliance Tasks with date restrictions and excluding Daily
		complianceCriteria.add(Restrictions.and(Restrictions.not(Restrictions.eq("recurring", Recurring.DAILY)),dateCriterion));
		
		return criteria.list();
	}

	public Map<String, String> isCurrentMonthGreater() {

		// Checking Whether this Month is greater

		Map<String, String> lastDates = new HashMap<>();
		// Today
		Date today = new Date();
		Calendar calendarToday = today.getCalendar();

		// Getting the Last Day of Current month
		Date currentMonthLastDay = new Date(
				calendarToday.getActualMaximum(Calendar.DAY_OF_MONTH, calendarToday.getJavaDate()).getTime());
		int currentLastDay = currentMonthLastDay.getDayOfMonth().getValue();

		// Traversing exactly One Month Back from today
		calendarToday.add(Calendar.MONTH_OF_YEAR, -1);
		Date exactOneMonthBack = new Date(calendarToday.getTimeInMillis());

		// Last Day of Last Month
		Date lastMonthLastDay = new Date(
				calendarToday.getActualMaximum(Calendar.DAY_OF_MONTH, calendarToday.getJavaDate()).getTime());
		int lastLastDay = lastMonthLastDay.getDayOfMonth().getValue();

		// Checking Whether today is the last Day of Month

		// Checking Whether last Month is Greater than Current
		if (currentLastDay < lastLastDay) {
			lastDates.put("status", "lesser");
			if (today.equals(currentMonthLastDay)) {
				// on lesser we send this dates to get the previous dates
				lastDates.put("lastMonthLastDay", lastMonthLastDay.toString("yyyy-MM-dd"));
				lastDates.put("exactOneMonthBack", exactOneMonthBack.toString("yyyy-MM-dd"));
			}
		} else if (currentLastDay > lastLastDay) {
			lastDates.put("status", "");
			Calendar isAfter = lastMonthLastDay.getCalendar();
			isAfter.add(Calendar.MONTH_OF_YEAR, 1);
			Date forward = new Date(isAfter.getTimeInMillis());
			if (today.isAfter(forward)) {
				lastDates.put("status", "greater");
			}
		} else {
			lastDates.put("status", "equal");
		}

		return lastDates;
	}

	// Compliance List
	@Override
	public Map<String, Object> getCompliances(Integer fromIndex, Integer toIndex) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Compliance.class);

		return getPaginationList(criteria, fromIndex, toIndex);
	}

	@Override
	public Boolean isComplianceNameExist(String complianceName, Long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Compliance.class);
		// Checking Whether already Compliance Name exist for New Compliance
		criteria.add(Restrictions.eq("complianceName", complianceName));
		if (!id.equals(-1l)) {
			// Checking Whether already Compliance Name exist while editing Compliance
			Compliance compliance = findBy(Compliance.class, id);
			criteria.add(Restrictions.ne("complianceName", compliance.getComplianceName()));
		}
		Compliance compliance = (Compliance) criteria.uniqueResult();
		return compliance != null;
	}

	@Override
	public Long getComplianceTasksCount(Compliance compliance) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ComplianceTask.class);
		criteria.add(Restrictions.eq("compliance", compliance));
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count;
	}

	@Override
	public List<Object> getTasksCount() {
		String query = "SELECT count(*) as tasksCount , compliance_id as compliance FROM `ComplianceTask` GROUP BY compliance_id ;";
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
		sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return sqlQuery.list();
	}
}
