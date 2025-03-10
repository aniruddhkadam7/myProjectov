/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.hrm.security.rest.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.jboss.logging.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.PersistLogin;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.hrm.security.rest.resources.URIResource;

/**
 *
 * @author hari
 */
public class UserDaoImpl extends DAOImpl implements UserDao {

	Logger logger = Logger.getLogger(UserDaoImpl.class);

	@Override
	@Transactional(readOnly = true)
	public <T extends Serializable> T findByEmployeeName(Class<T> clazz,
			Serializable employeeNaame) {
		return (T) getSessionFactory().getCurrentSession()
				.createCriteria(Employee.class)
				.add(Restrictions.eq("userName", employeeNaame)).uniqueResult();

	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String string)
			throws UsernameNotFoundException, DataAccessException {
		
		Employee employee = this.findByActiveEmployeeName(Employee.class,
				string);
		
		if (null == employee) {
			throw new UsernameNotFoundException("The user with name "
					+ employee + " was not found");
		}

		return employee;
	}

	@Override
	public <T extends Serializable> T findByTokenName(Class<T> clazz,
			Serializable token) {
		return (T) getSessionFactory().getCurrentSession()
				.createCriteria(PersistLogin.class)
				.add(Restrictions.eq("token", token)).uniqueResult();
	}

	@Override
	public boolean isURIResourceAccess(String role, String uri, String method) {

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(URIResource.class);
		criteria.add(Restrictions.eq("empRole", role));
		criteria.add(Restrictions.eq("uri", uri));
		criteria.add(Restrictions.eq("method", method));

		return criteria.list().size() != 0;
	}

	@Override
	public List<PersistLogin> getTokensByUserName(String userName) {
		return (List<PersistLogin>) sessionFactory.getCurrentSession()
				.createCriteria(PersistLogin.class)
				.add(Restrictions.eq("userName", userName)).list();
	}

}
