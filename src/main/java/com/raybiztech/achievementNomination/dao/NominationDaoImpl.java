package com.raybiztech.achievementNomination.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.achievementNomination.business.InitiateNomination;
import com.raybiztech.achievementNomination.business.Nomination;
import com.raybiztech.achievementNomination.business.NominationCycle;
import com.raybiztech.achievementNomination.business.NominationQuestion;
import com.raybiztech.achievementNomination.dto.NominationCycleDto;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;

/**
 *
 * @author Aprajita
 */

@Repository("nominationManagement")
public class NominationDaoImpl extends DAOImpl implements NominationDao {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(NominationDaoImpl.class);

	/* To get all question List */
	@Override
	public Map<String, Object> getAllQuestions() {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				NominationQuestion.class);

		criteria.addOrder(Order.desc("id"));
		Integer noOfRecords = criteria.list().size();
		List<NominationQuestion> typeList = criteria.list();
		Map<String, Object> questionListmap = new HashMap<String, Object>();
		questionListmap.put("list", typeList);
		questionListmap.put("size", noOfRecords);

		return questionListmap;
	}

	/* To get all cycle List */
	@Override
	public NominationCycle getActiveCycle() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				NominationCycle.class);
		criteria.add(Restrictions.eq("activateFlag", true));
		return (NominationCycle) criteria.uniqueResult();
	}

	/* To get active cycle List */
	@Override
	public Map<String, Object> getAllCycles() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				NominationCycle.class);
		criteria.addOrder(Order.desc("id"));
		Integer noOfRecords = criteria.list().size();
		List<NominationCycle> nominationList = criteria.list();
		Map<String, Object> nominationCycleMap = new HashMap<String, Object>();
		nominationCycleMap.put("list", nominationList);
		nominationCycleMap.put("size", noOfRecords);
		return nominationCycleMap;
	}

	@Override
	public InitiateNomination isMappingExit(Long id) {
		return null;
	}

	/* get All question under active cycle */
	@Override
	public InitiateNomination getAllQuestionUnderCycle(Long activeCycleId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InitiateNomination.class);
		criteria.createAlias("nominationCycle", "nominationCycle");
		criteria.add(Restrictions.eq("nominationCycle.id", activeCycleId));
		// criteria.add(Restrictions.eq("activateFlag", true));
		return (InitiateNomination) criteria.uniqueResult();
	}

	@Override
	public InitiateNomination getNominationFormDetailsOfActiveCycle(
			NominationCycle activeCycle) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				InitiateNomination.class);
		criteria.add(Restrictions.eq("nominationCycle", activeCycle));
		return (InitiateNomination) criteria.uniqueResult();
	}

	@Override
	public Boolean checkNomineeAlreadyAdded(NominationCycle cycle,
			Employee employee) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Nomination.class);
		criteria.add(Restrictions.eq("employee", employee));
		criteria.add(Restrictions.eq("nominationCycleId", cycle));
		Nomination nomination = (Nomination) criteria.uniqueResult();
		return (nomination == null) ? false : true;
	}

	@Override
	public List<Nomination> getNominations(NominationCycle cycle) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Nomination.class);
		criteria.add(Restrictions.eq("nominationCycleId", cycle));
		return criteria.list();
	}

	@Override
	public List<Nomination> getHierarcalNomintions(NominationCycle cycle,
			List<Long> managersList) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Nomination.class);
		criteria.add(Restrictions.eq("nominationCycleId", cycle));
		criteria.add(Restrictions.in("createdBy", managersList));
		return criteria.list();
	}

	@Override
	public List<Nomination> getDuplicateDate(Date startDate, Date endDate,
			Long cycleId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				NominationCycle.class);
		Criterion criterion1 = Restrictions.between("fromMonth", startDate,
				endDate);
		Criterion criterion2 = Restrictions.between("toMonth", startDate,
				endDate);

		Criterion criterion3 = Restrictions.and(
				Restrictions.le("fromMonth", startDate),
				Restrictions.ge("toMonth", endDate));

		Criterion criterion4 = Restrictions.and(
				Restrictions.ge("fromMonth", startDate),
				Restrictions.le("toMonth", endDate));

		criteria.add(Restrictions.or(Restrictions.or(criterion1, criterion2),
				Restrictions.or(criterion3, criterion4)));
		if (cycleId != null) {
			criteria.add(Restrictions.ne("id", cycleId));
		}
		return criteria.list();
	}

}
