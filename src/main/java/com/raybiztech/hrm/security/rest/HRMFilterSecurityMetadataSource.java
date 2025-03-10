package com.raybiztech.hrm.security.rest;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;

public class HRMFilterSecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {

	private final DAO dao;

	@Autowired
	public HRMFilterSecurityMetadataSource(DAO dao) {

		this.dao = dao;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		FilterInvocation filterInvocation = (FilterInvocation) object;
		HttpServletRequest request = filterInvocation.getHttpRequest();
		Collection<ConfigAttribute> result = new ArrayList<ConfigAttribute>();
		String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee employee = dao.findByEmployeeName(Employee.class, userName);
		// Find roles in database that secures the specified request
		// ...
		// For any role found, create a SecurityConfig object prefixed with
		// "ROLE_" ex :
		// for(String role : roles) {
		// ConfigAttribute attribute = new SecurityConfig("ROLE_"+roleFound);
		// result.add(attribute);
		// }
		//
		// // Instead of hard coding the roles lookup the roles from the
		// database using the url and/or HttpServletRequest
		// Do not forget to add caching of the lookup
		// String[] roles = new String[] { "ROLE_ADMIN", "ROLE_USER" };
		 //return SecurityConfig.createList(employee.getAuthorities());
		//
		//

		return result;
	}

	@Override
	public boolean supports(Class<?> clazz) {

		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	/*
	 * public List<ConfigAttribute> getAttributesByURL(String inputUrl) {
	 * List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();
	 * 
	 * 
	 * 
	 * // String selectquery = "select * from URL_ACCESS where URL = '" +
	 * inputUrl +"'"; // attributes.add(temp);
	 * 
	 * 
	 * return attributes; }
	 */
}
