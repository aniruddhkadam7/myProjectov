package com.raybiztech.appraisals.security.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.security.service.EmployeeSecurityService;

@Aspect
@Component
public class LoggingManagerAspect {

	@Autowired
	EmployeeSecurityService employeeSecurityService;

	// @Before("execution(* com.finzy.controller.*.*(..))")

	@Before("execution(* com.raybiztech.appraisals.service.*.*(..))")
	public void logBefore(JoinPoint joinPoint) throws Exception {
		/*String loginName = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		String roleName = employeeSecurityService
		.getLoginEmployee(loginName).getRole();
		if (!("Manager".equalsIgnoreCase(roleName))) {
			throw new UnauthorizedUserException("Sorry you don't have access!");
		}*/

	}
}
