package com.raybiztech.appraisals.security.aop;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

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
import com.raybiztech.rolefeature.business.Permission;

@Aspect
@Component
public class ExpenseManagementControllerAop {
	
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	DAO dao;
	
	org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ExpenseManagementControllerAop.class);

	@Before("execution(* com.raybiztech.expenseManagement.controller.*.*(..))")
	public void expenseAdvice(JoinPoint joinpoint) throws IOException{
		
		System.out.println("in method");
		
		String methodName = joinpoint.getSignature().getName();
		
		Employee loggedInEmployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		Long empId = loggedInEmployee.getEmployeeId();
		
		Map<String, Object> params = securityUtils.getMethodParams(joinpoint.getArgs());
		
		HttpServletResponse httpServletResponse = (HttpServletResponse) params
				.get("httpServletResponse");	
		
		Permission expenseManagement = dao.checkForPermission("Expense Management", loggedInEmployee);
		
		if(!expenseManagement.getView()) {
			securityUtils.printIpAddress();
			httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
			throw new UnauthorizedUserException("user cannot access expense details");
		}
		
		else if(methodName.equalsIgnoreCase("getExpensesYearlyReport")) {
			//System.out.println("in else method");
			
			Permission totalExpensesList = dao.checkForPermission("TotalExpensesList", loggedInEmployee);	
			
			if(!totalExpensesList.getView()) {
				
				securityUtils.printIpAddress();
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				throw new UnauthorizedUserException("user cannot access expense yearly report details");
			}

		}
		
	}
	
	
	
	
}
