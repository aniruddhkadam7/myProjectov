package com.raybiztech.achievement.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.itextpdf.text.pdf.fonts.cmaps.CidLocation;
import com.raybiztech.achievement.business.Achievement;
import com.raybiztech.achievement.business.AchievementType;
import com.raybiztech.achievement.business.Leadership;
import com.raybiztech.achievement.dto.AchievementDTO;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;

/**
 *
 * @author Aprajita
 */

@Repository("achievementManagement")
public class AchievementDaoImpl extends DAOImpl implements AchievementDao {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(AchievementDaoImpl.class);

	@Override
	public Map<String, Object> getAllAchievementType() {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AchievementType.class);

		criteria.addOrder(Order.desc("id"));
		Integer noOfRecords = criteria.list().size();
		List<AchievementType> typeList = criteria.list();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", typeList);
		map.put("size", noOfRecords);

		return map;
	}

	@Override
	public Map<String, Object> getAllAchievement(Integer startIndex, Integer endIndex, Long achievementTypeId,
			DateRange dateRange) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Achievement.class);
		criteria.createAlias("achievementType", "achievementType");
		if (achievementTypeId != null) {
			criteria.add(Restrictions.eq("achievementType.id", achievementTypeId));
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(
					Restrictions.or(Restrictions.between("startDate", dateRange.getMinimum(), dateRange.getMaximum()),
							Restrictions.between("endDate", dateRange.getMinimum(), dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
									Restrictions.le("endDate", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
											Restrictions.le("endDate", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("startDate", dateRange.getMinimum()),
											Restrictions.ge("endDate", dateRange.getMaximum()))))));

		} else if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {

			criteria.add(Restrictions.or(Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.isNull("startDate")), Restrictions.ge("startDate", dateRange.getMinimum())));

		} else if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(Restrictions.and(Restrictions.le("endDate", dateRange.getMaximum()),
					Restrictions.isNull("endDate")), Restrictions.le("endDate", dateRange.getMaximum())));
		}
		criteria.addOrder(Order.desc("id"));
		/*
		 * Integer noOfRecords = criteria.list().size();
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); List<Achievement> achievementList = criteria.list();
		 */

		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> achievementMap = new HashMap<String, Object>();
		achievementMap.put("list", criteriaMap.get("list"));
		achievementMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return achievementMap;
	}

	/* To show Associate Award in dash board */
	@Override
	public List<Achievement> getAchieversOnDashBoard() {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Achievement.class);
		criteria.createAlias("achievementType", "achievementType");
		criteria.add(Restrictions.ne("achievementType.achievementType", "Service Award"));
		criteria.add(Restrictions.eq("achievementType.achievementType", "Associate of Quarter Award"));
		criteria.add(Restrictions.eq("showOnDashBoard", Boolean.TRUE));
		criteria.addOrder(Order.desc("id"));
		// criteria.setMaxResults(4);
		return criteria.list();
	}

	@Override
	public List<Achievement> getServiceOnDashBoard() {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Achievement.class);
		criteria.createAlias("achievementType", "achievementType");
		criteria.add(Restrictions.ne("achievementType.achievementType", "Associate of Quarter Award"));
		criteria.add(Restrictions.eq("achievementType.achievementType", "Service Award"));
		criteria.add(Restrictions.eq("showOnDashBoard", Boolean.TRUE));
		criteria.addOrder(Order.desc("id"));
		// criteria.setMaxResults(4);
		return criteria.list();
	}

	@Override
	public List<Achievement> showStarOfTheMonth() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Achievement.class);
		criteria.createAlias("achievementType", "achievementType");
		criteria.add(Restrictions.eq("achievementType.achievementType", "Star of the Month"));
		criteria.add(Restrictions.eq("showOnDashBoard", Boolean.TRUE));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public List<Achievement> getAllAchievementList() {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Achievement.class);
		criteria.createAlias("achievementType", "achievementType");
		criteria.add(Restrictions.eq("achievementType.status", Boolean.TRUE));
		criteria.add(Restrictions.eq("showOnDashBoard", Boolean.TRUE));
		criteria.addOrder(Order.asc("achievementType.order"));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public AchievementType getAchievementTypeDetails(Long typeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AchievementType.class);
		criteria.add(Restrictions.eq("id", typeId));
		return (AchievementType) criteria.uniqueResult();
	}

@Override
	public List<Leadership> getlist(Date fromDate,Date toDate,String dateSelection,String statusSelection) {
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Leadership.class);
		 criteria.add(Restrictions.between("createdDate", fromDate, toDate));
		 criteria.add(Restrictions.eq("status", statusSelection));
	     return criteria.list();
	}


@Override
public Leadership getLeadershipDetails(Long id) {
	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Leadership.class);
	criteria.createAlias("employee", "employee");
	criteria.add(Restrictions.eq("employee.employeeId", id));
	
	return (Leadership)criteria.list().get(0);
}




}
