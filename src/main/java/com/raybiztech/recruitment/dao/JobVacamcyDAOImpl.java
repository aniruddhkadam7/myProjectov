package com.raybiztech.recruitment.dao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.recruitment.business.JobVacancy;
import com.raybiztech.recruitment.dto.JobVacancyDTO;
@Repository
@Transactional
public class JobVacamcyDAOImpl extends DAOImpl implements JobVacancyDAO {
	Logger logger=Logger.getLogger(JobVacamcyDAOImpl.class);
        @Override
	public <T extends Serializable> T findByPositionVacant(Class<T> clazz,
			Serializable name) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("positionVacant", name)).uniqueResult();
		
	}

//		@Override
//		public List<JobVacancy> getAllJobVacancies(String searchJobTitle) {
//			Criteria criteria=getSessionFactory().getCurrentSession().createCriteria(JobVacancy.class);
//			Criterion criterion=Restrictions.ilike("positionVacant", searchJobTitle,MatchMode.ANYWHERE);
//			Criterion criterion1=Restrictions.eq("minimumExperience", searchJobTitle);
//			Criterion criterion2=Restrictions.ilike("jobCode", searchJobTitle,MatchMode.ANYWHERE);
//			Criterion criterion3 = Restrictions.or(criterion, criterion1);
//			criteria.add(Restrictions.or(criterion3, criterion2));
//			List<JobVacancy> list=criteria.list();
//			logger.warn("display list line no:39 " + list.size());
//			return list;
//		}

		@Override
		public Map<String, Object> getOpenJobVacancies(Integer startIndex, Integer endIndex, String status, String searchJobTitle) {
			Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(JobVacancy.class);
			if(status.equalsIgnoreCase("open"))
			{
						criteria.add(Restrictions.eq("status", Boolean.TRUE));
			}
			else
			{
				criteria.add(Restrictions.eq("status", Boolean.FALSE));
			}
			if(!searchJobTitle.isEmpty()) {
			Criterion criterion=Restrictions.ilike("positionVacant", searchJobTitle,MatchMode.ANYWHERE);
			Criterion criterion1=Restrictions.eq("minimumExperience", searchJobTitle);
			Criterion criterion2=Restrictions.ilike("jobCode", searchJobTitle,MatchMode.ANYWHERE);
			Criterion criterion3 = Restrictions.or(criterion, criterion1);
			criteria.add(Restrictions.or(criterion3, criterion2));
			}
			criteria.addOrder(Order.desc("jobVacancyId"));
			Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
			Map<String, Object> jobVacanciesMap = new HashMap<String, Object>();
			jobVacanciesMap.put("list", criteriaMap.get("list"));
			jobVacanciesMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));
			return jobVacanciesMap;
		}

		@Override
		public Map<String , List<Audit>>  getJobVacancyAudit(Long id) {
			Criteria criteria=sessionFactory.getCurrentSession().createCriteria(Audit.class);
			criteria.add(Restrictions.eq("referenceId", id));
			criteria.add(Restrictions.eq("tableName", "JOBVACANCY"));
			criteria.addOrder(Order.asc("modifiedDate"));
			List<Audit> audits = criteria.list();
			Map<String, List<Audit>> map = null;
			if (!audits.isEmpty())
				map = getPairValue(audits);
			return map;
		}
		
}
