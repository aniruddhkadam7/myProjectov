package com.raybiztech.appraisals.security.dao;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.Employee;

@Component("securityDao")
@Transactional
public class SecurityDAOImpl implements SecurityDAO {

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Employee getEmployee(String loginName) {
		// TODO Auto-generated method stub

		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);

		criteria.add(Restrictions.eq("userName", loginName));
		 criteria.add(Restrictions.eq("statusName", "Active"));
		// criteria.add(Restrictions.or(Restrictions.eq("statusName", "Active"), Restrictions.eq("statusName", "underNotice")));

		Employee employee = (Employee) criteria.uniqueResult();

		return employee;
	}

}
