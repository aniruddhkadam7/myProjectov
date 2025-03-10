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

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;

@Aspect
@Component
public class PayslipControllerAop {

	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(PayslipControllerAop.class);

	@Before("execution(* com.raybiztech.payslip.controller.*.*(..))")
	public void payslipAop(JoinPoint joinPoint) throws IOException {

		String methodName = joinPoint.getSignature().getName();
		/*Employee employee = dao.findByUniqueProperty(Employee.class,
				"userName", SecurityContextHolder.getContext()
						.getAuthentication().getName());*/
		Map<String, Object> employeeDeatils = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee employee = (Employee) employeeDeatils.get("employee");


		Map<String, Object> params = securityUtils.getMethodParams(joinPoint
				.getArgs());

		Long paramID = (Long) params.get("longParam");
		HttpServletResponse httpServletResponse = (HttpServletResponse) params
				.get("httpServletResponse");

		if (methodName.equalsIgnoreCase("getEmployeePayslipsForSelectedYear")
				|| methodName
						.equalsIgnoreCase("getPayslipDataForViewToEmployee")
				|| methodName
						.equalsIgnoreCase("generatePayslipandDownloadPayslip")
				|| methodName.equalsIgnoreCase("downloadPayslip")) {

			securityUtils.checkAccessForRespectiveEmployee(
					employee.getEmployeeId(), paramID, httpServletResponse);

		}

	}
}
