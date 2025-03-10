package com.raybiztech.hrm.security.rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.hrm.security.rest.dao.UserDao;

public class FeatureCheckingFilter extends GenericFilterBean {

	private final DAO dao;
	private final UserDao userDaoImpl;

	@Autowired
	public FeatureCheckingFilter(DAO dao, UserDao userDaoImpl) {
		this.dao = dao;
		this.userDaoImpl = userDaoImpl;

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = this.getAsHttpRequest(request);
		HttpServletResponse httpResponse = this.getAsHttpResponse(response);

		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal().toString();
		Employee employee = dao.findByEmployeeName(Employee.class, userName);
		String role = employee.getRole();
		Boolean isExist = userDaoImpl.isURIResourceAccess(role,
				httpRequest.getRequestURI(), httpRequest.getMethod());
		if (!isExist) {
			httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		filterChain.doFilter(request, response);

	}

	private HttpServletRequest getAsHttpRequest(ServletRequest request) {
		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Expecting an HTTP request");
		}

		return (HttpServletRequest) request;
	}

	private HttpServletResponse getAsHttpResponse(ServletResponse response) {
		if (!(response instanceof HttpServletResponse)) {
			throw new RuntimeException("Expecting an HTTP response");
		}

		return (HttpServletResponse) response;
	}

}
