package com.raybiztech.appraisals.security.aop;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;

@Aspect
@Component
public class BiometricControllerAop {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(BiometricControllerAop.class);

	@Before("execution(* com.raybiztech.biometric.controller.*.*(..))")
	public void BiometricAop(JoinPoint joinPoint) throws IOException {

		String MethodName = joinPoint.getSignature().getName();
		/*
		 * Employee employee = dao.findByUniqueProperty(Employee.class,
		 * "userName", SecurityContextHolder.getContext()
		 * .getAuthentication().getName());
		 */

		Map<String, Object> employeeDetails = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();

		Long employeeId = (Long) employeeDetails.get("employeeId");

		Map<String, Object> methodParams = securityUtils
				.getMethodParams(joinPoint.getArgs());

		Long paramEmployeeId = (Long) methodParams.get("longParam");

		HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
				.get("httpServletResponse");

		if (MethodName.equalsIgnoreCase("getMonthlyHiveReportForEmployee")
				|| MethodName.equalsIgnoreCase("getEmployeeTimeInOffice")
				|| MethodName.equalsIgnoreCase("attendance") || MethodName.equalsIgnoreCase("attendanceForAdminManager")) {

			securityUtils.checkAccessForRespectiveEmployee(employeeId,
					paramEmployeeId, httpServletResponse);
		}

	}
}
