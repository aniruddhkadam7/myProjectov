package com.raybiztech.delegation.Dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAOImpl;

@Repository("delegationDaoImpl")
public class DelegationDaoImpl extends DAOImpl implements DelegationDao {

	Logger logger = Logger.getLogger(DelegationDaoImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getAllHRList() {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.createAlias("empRole", "empRole");
		Criterion HR = Restrictions.ilike("empRole.name", "HR", MatchMode.ANYWHERE);
		Criterion HRManager = Restrictions.ilike("empRole.name", "HR Manager", MatchMode.ANYWHERE);
		criteria.add(Restrictions.or(HR, HRManager));
		criteria.add(Restrictions.eq("statusName", "Active"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Employee> getHrAssociates(Long hrId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.createAlias("hrAssociate", "hrAssociate");
		criteria.add(Restrictions.eq("hrAssociate.employeeId", hrId));
		criteria.add(Restrictions.eq("statusName", "Active"));
		return criteria.list();
	}

}
