/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.hrm.security.rest.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.PersistLogin;
import com.raybiztech.appraisals.dao.DAO;

/**
 *
 * @author hari
 */
@Component("userDaoImpl")
public interface UserDao extends UserDetailsService, DAO {

	@Override
	UserDetails loadUserByUsername(String string)
			throws UsernameNotFoundException, DataAccessException;

	<T extends Serializable> T findByEmployeeName(Class<T> clazz,
			Serializable employeeNaame);

	<T extends Serializable> T findByTokenName(Class<T> clazz,
			Serializable token);

	boolean isURIResourceAccess(String role, String uri, String method);

	public List<PersistLogin> getTokensByUserName(String userName);
}
