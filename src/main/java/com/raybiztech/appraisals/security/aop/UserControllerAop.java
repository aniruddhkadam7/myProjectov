package com.raybiztech.appraisals.security.aop;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;

@Aspect
@Component
public class UserControllerAop {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityutils;

	@Before("execution(* com.raybiztech.roleFeature.Controler.*.*(..))")
	public void userControllerAop(JoinPoint joinPoint) throws IOException {

		String methodName = joinPoint.getSignature().getName();
		Map<String, Object> employeeDetails = securityutils
				.getLoggedEmployeeDetailsSecurityContextHolder();
		Long loggedemployeeId = (Long) employeeDetails.get("employeeId");

		Map<String, Object> methodParams = securityutils
				.getMethodParams(joinPoint.getArgs());
		Long paramid = (Long) methodParams.get("longParam");
		HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
				.get("httpServletResponse");

		if (methodName.equalsIgnoreCase("getUser")
				|| methodName.equalsIgnoreCase("getMenuItems")) {

			securityutils.checkAccessForRespectiveEmployee(loggedemployeeId,
					paramid, httpServletResponse);

		}
	}

}
