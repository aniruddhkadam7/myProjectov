package com.raybiztech.hrm.security.rest;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.multitenancy.TenantTypes;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.rolefeature.business.Role;
import com.raybiztech.rolefeature.business.URIAndFeatures;

/**
 *
 * @author hari
 */
@Component
public class AuthenticationTokenProcessingFilter extends GenericFilterBean {

	private final UserDetailsService userService;

	static Logger logger1 = Logger
			.getLogger(AuthenticationTokenProcessingFilter.class);

	public AuthenticationTokenProcessingFilter(UserDetailsService userService) {
		this.userService = userService;

	}

	@Autowired
	TokenUtils tokenUtils;
	@Autowired
	DAO dao;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = this.getAsHttpRequest(request);
		HttpServletResponse httpResponse = this.getAsHttpResponse(response);

		// String ipAddress = httpRequest.getHeader("X-FORWARDED-FOR");
		// if (ipAddress == null) {
		// ipAddress = httpRequest.getRemoteAddr();
		// }
		// logger1.warn("Ip address" + ipAddress);
		
		String tenantKey = this.extractTenantKeyFromRequest(httpRequest);
		TenantContextHolder.setTenantType(TenantTypes.valueOf(tenantKey));
		
		String authToken = this.extractAuthTokenFromRequest(httpRequest);
		String employeeId = this.extractUserIdFromRequest(httpRequest);
		String userName = TokenUtils.getUserNameFromToken(authToken);

		if (userName != null) {

			UserDetails userDetails = this.userService
					.loadUserByUsername(userName);

			Employee loggedEmployee = dao.findByUniqueProperty(Employee.class,
					"userName", userName);

			Role role = dao.findByUniqueProperty(Role.class, "name",
					loggedEmployee.getRole());

			/* List<Long> featureIds = dao.getRolePermissions(role.getRoleId()); */
			String httpuri = httpRequest.getRequestURI();
			String alteredURI = httpuri.substring(7);

			List<URIAndFeatures> validUrls = dao.getUrlId(alteredURI,
					httpRequest.getMethod());

			for (URIAndFeatures uri : validUrls) {
				Permission permission = dao.getPermissionType(role.getRoleId(),
						uri.getFeature().getFeatureId());

				if (uri.getAccessType() != null && permission != null) {
					if (!checkPermissionAndRequestUrihaveSameAccessType(
							permission, uri)) {
						httpResponse.sendError(httpResponse.SC_FORBIDDEN);
						return;
					}
				} else if (permission == null) {
					httpResponse.sendError(httpResponse.SC_FORBIDDEN);
					return;
				}
			}

			if (tokenUtils.validateToken(authToken)) {
//				if (employeeId != null) {
//					Employee employee = tokenUtils
//							.getEmployeeFromTokenUtils(userName);
//
//					if (!((String.valueOf(employee.getEmployeeId())
//							.equalsIgnoreCase(employeeId)) || ("admin"
//							.equalsIgnoreCase(employee.getRole())
//							|| "Finance".equalsIgnoreCase(employee.getRole())
//							|| "HR".equalsIgnoreCase(employee.getRole()) || httpRequest
//							.getRequestURI()
//							.equalsIgnoreCase(
//									"/hrm-ws/jobapplicant/loggedInEmployeeData")))) {
//
//						logger.warn("In Employee Token Processing Filter");
//
//						httpResponse
//								.setStatus(HttpServletResponse.SC_FORBIDDEN);
//
//					}
//
//				}
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, userDetails.getUsername(),
						userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource()
						.buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(
						authentication);
			} else {
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		} else {

			if (!((httpRequest.getRequestURI()
					.equalsIgnoreCase("/hrm-ws/auth/login"))
					|| (httpRequest.getRequestURI()
							.equalsIgnoreCase("/hrm-ws/user/action")) || (httpRequest
						.getRequestURI()
					.equalsIgnoreCase("/hrm-ws/jobapplicant/downloadFile")))) {
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}
		}

		chain.doFilter(request, response);
	}

	private Boolean checkPermissionAndRequestUrihaveSameAccessType(
			Permission permission, URIAndFeatures andFeatures) {

		if (permission.getView()
				&& andFeatures.getAccessType().equalsIgnoreCase("view")) {
			return Boolean.TRUE;
		}
		if (permission.getUpdate()
				&& andFeatures.getAccessType().equalsIgnoreCase("update")) {
			return Boolean.TRUE;
		}
		if (permission.getCreate()
				&& andFeatures.getAccessType().equalsIgnoreCase("create")) {
			return Boolean.TRUE;
		}
		if (permission.getDelete()
				&& andFeatures.getAccessType().equalsIgnoreCase("delete")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;

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

	private String extractAuthTokenFromRequest(HttpServletRequest httpRequest) {
		/* Get token from request header */
		String authToken = httpRequest.getHeader("X-Auth-Token");

		/* If token not there get it from request parameter */
		if (authToken == null) {
			authToken = httpRequest.getParameter("token");
		}

		return authToken;
	}

	private String extractUserIdFromRequest(HttpServletRequest httpRequest) {
		/* get empid from request parameter */
		String employeeId = httpRequest.getParameter("loggedInEmpId");
		return employeeId;
	}
	
	private String extractTenantKeyFromRequest(HttpServletRequest httpRequest) {
		/* Get tenantKey from request header */
		String tenantKey = httpRequest.getHeader("tenantKey");

		/* If tenantKey not there get it from request parameter */
		if (tenantKey == null) {
			tenantKey = httpRequest.getParameter("tenantKey");
		}

		return tenantKey;
	}
}
