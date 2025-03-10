package com.raybiztech.appraisals.security.aop;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;

@Aspect
@Component
public class TimeInOfficeControllerAop {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;

	org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(TimeInOfficeControllerAop.class);

	@Before("execution(* com.raybiztech.TimeInOffice.controller.*.*(..))")
	public void TimeInOfficeController(JoinPoint joinPoint) throws IOException {

		String MethodName = joinPoint.getSignature().getName();

		Map<String, Object> employeeDetails = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();

		Long employeeid = (Long) employeeDetails.get("employeeId");

		Map<String, Object> methodParams = securityUtils
				.getMethodParams(joinPoint.getArgs());

		Long paramEmployeeId = (Long) methodParams.get("longParam");
		HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
				.get("httpServletResponse");

		if (MethodName.equalsIgnoreCase("getTimeInOfficeEmployeeReport")) {

			securityUtils.checkAccessForRespectiveEmployee(employeeid,
					paramEmployeeId, httpServletResponse);

		}

	}

}
