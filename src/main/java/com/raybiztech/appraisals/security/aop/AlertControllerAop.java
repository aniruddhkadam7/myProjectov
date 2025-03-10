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
import com.raybiztech.appraisals.security.utils.SecurityUtils;

@Aspect
@Component
public class AlertControllerAop {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;

	@Before("execution(* com.raybiztech.appraisals.alerts.controller.*.*(..))")
	public void alertControllerAop(JoinPoint joinPoint) throws IOException {

		/*
		 * Long loggedEmpID = securityUtils
		 * .getLoggedEmployeeIdforSecurityContextHolder();
		 */

		Map<String, Object> employeeDetails = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();

		Long loggedEmpID = (Long) employeeDetails.get("employeeId");
		Employee loggedEmployee = (Employee) employeeDetails.get("employee");

		String MethodName = joinPoint.getSignature().getName();

		Map<String, Object> methodParams = securityUtils
				.getMethodParams(joinPoint.getArgs());

		Long paramId = (Long) methodParams.get("longParam");
		HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
				.get("httpServletResponse");

		if (MethodName.equalsIgnoreCase("getAlerts")
				|| MethodName.equalsIgnoreCase("getUpdateAllAlerts")
				|| MethodName.equalsIgnoreCase("getAllAlerts")
				|| MethodName.equalsIgnoreCase("updateAlertDetails")) {

			if (!(loggedEmployee.getRole().equalsIgnoreCase("admin")
					|| loggedEmployee.getRole().equalsIgnoreCase("finance") || loggedEmployee
					.getRole().equalsIgnoreCase("HR"))) {

				securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
						paramId, httpServletResponse);
			}
		}

	}
}
