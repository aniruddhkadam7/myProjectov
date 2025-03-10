package com.raybiztech.appraisals.security.aop;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.meetingrequest.business.MeetingRequest;

@Aspect
@Component
public class MeetingRequestAop {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;

	org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(MeetingRequestAop.class);

	@Before("execution(* com.raybiztech.meetingrequest.controller.*.*(..))")
	public void MeetingRequestController(JoinPoint joinPoint)
			throws IOException {
		String methodName = joinPoint.getSignature().getName();
		Map<String, Object> employeeDeatils = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee employee = (Employee) employeeDeatils.get("employee");
		Map<String, Object> methodParams = securityUtils
				.getMethodParams(joinPoint.getArgs());

		if (methodName.equalsIgnoreCase("cancelMeeting")) {
			Long paramId = (Long) methodParams.get("longParam");
			HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
					.get("httpServletResponse");
			MeetingRequest meetingRequest = dao.findBy(MeetingRequest.class,
					paramId);
			String authorId = meetingRequest.getAuthorName();
			Employee authorEmployee = dao.findBy(Employee.class,
					Long.parseLong(authorId));
			if (!employee.getRole().equalsIgnoreCase("admin")) {
				if (!authorEmployee.getFullName().equalsIgnoreCase(
						employee.getFullName())) {
					securityUtils.printIpAddress();
					httpServletResponse
							.sendError(httpServletResponse.SC_FORBIDDEN);
					throw new UnauthorizedUserException("Employee with name  "
							+ employee.getFullName()
							+ " Trying to cancel Meeting Booked by "
							+ authorEmployee.getFullName());
				}
			}
		}

	}
}
