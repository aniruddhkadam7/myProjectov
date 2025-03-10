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
import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.dto.LeaveApplicationDTO;
import com.raybiztech.rolefeature.business.Permission;

@Aspect
@Component
public class LeaveManagementControllerAop {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;

	org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(LeaveManagementControllerAop.class);

	@Before("execution(* com.raybiztech.leavemanagement.controller.*.*(..))")
	public void LeaveManagementAop(JoinPoint joinPoint) throws IOException {

		
		
		String MethodName = joinPoint.getSignature().getName();

		/*
		 * String loggedEmployeeName = SecurityContextHolder.getContext()
		 * .getAuthentication().getName();
		 * 
		 * Employee employee = dao.findByUniqueProperty(Employee.class,
		 * "userName", loggedEmployeeName);
		 * 
		 * Long loggedEmpID = employee.getEmployeeId();
		 */

		Map<String, Object> employeeDeatils = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee employee = (Employee) employeeDeatils.get("employee");
		Long loggedEmpID = (Long) employeeDeatils.get("employeeId");

		String loggedEmployeeRole = employee.getRole();

		Map<String, Object> methodParams = securityUtils
				.getMethodParams(joinPoint.getArgs());

		Long paramId = (Long) methodParams.get("longParam");
		HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
				.get("httpServletResponse");
		LeaveApplicationDTO applicationDTO = (LeaveApplicationDTO) methodParams
				.get("applicationDTO");

		if (MethodName.equalsIgnoreCase("apply")) {

			if (applicationDTO != null) {
				securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
						applicationDTO.getEmployeeId(), httpServletResponse);

			}

		} else if (MethodName.equalsIgnoreCase("reject")
				|| MethodName.equalsIgnoreCase("approve")) {
			
			logger.warn("in approve");

			Permission permission = dao.checkForPermission("Leave Approvals",
					employee);
			
			logger.warn("Permision for leave approvals"+permission.getView());
			Permission permission2 = dao.checkForPermission(
					"Hierarchy Leave Approvals", employee);
			logger.warn("Permision for Hierarchy leave approvals"+permission2.getView());

			if (permission.getView() && permission2.getView()) {
				logger.warn("In if");
				LeaveDebit debit = dao.findBy(LeaveDebit.class, paramId);
				
				
				/*securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
						debit.getEmployee().getManager().getEmployeeId(),
						httpServletResponse);*/
				Long projectManagerId = null ;
				
				if(debit.getEmployee().getProjectManager() != null){
					
					projectManagerId =debit.getEmployee().getProjectManager().getEmployeeId();	
				}
				//Long projectManagerId =debit.getEmployee().getProjectManager().getEmployeeId();
				
				Long managerId = debit.getEmployee().getManager().getEmployeeId();
				
				logger.warn("logged in employee"+loggedEmpID);
				logger.warn("managerId"+managerId);
				
				logger.warn("condition"+ loggedEmpID.equals(managerId));
				
				if(loggedEmpID.equals(managerId)){
					logger.warn("in manager");
					securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
							managerId,
							httpServletResponse);
				}else{
					logger.warn("in projectManager");
					securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
							projectManagerId,
							httpServletResponse);
				}
			
				
			} else if (!permission.getView()) {
				securityUtils.printIpAddress();
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				throw new UnauthorizedUserException("Unauthorized User");
			}

			// if (!(loggedEmployeeRole.equalsIgnoreCase("admin") ||
			// loggedEmployeeRole
			// .equalsIgnoreCase("manager"))) {
			// securityUtils.printIpAddress();
			// httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
			// throw new UnauthorizedUserException("Unauthorized User");
			// }
			// if (loggedEmployeeRole.equalsIgnoreCase("manager")) {
			// LeaveDebit debit = dao.findBy(LeaveDebit.class, paramId);
			// securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
			// debit.getEmployee().getManager().getEmployeeId(),
			// httpServletResponse);
			//
			// }

		}
//                else if (MethodName.equalsIgnoreCase("leaveSummaries")
//				|| MethodName.equalsIgnoreCase("leaveCalendarSettings")) {
//			if (!(loggedEmployeeRole.equalsIgnoreCase("admin")
//					|| loggedEmployeeRole.equalsIgnoreCase("finance") || loggedEmployeeRole
//						.equalsIgnoreCase("HR"))) {
//				securityUtils.printIpAddress();
//				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
//				throw new UnauthorizedUserException("Unauthorized User");
//			}
//
//		} 
                else if (MethodName.equalsIgnoreCase("employeeLeaves")) {
			
			Employee employee2 = dao.findBy(Employee.class, paramId);
			Boolean authorisedFlag = false;
			// It will check whether the employee(which is coming as parameter)
			// contains
			// the view access of Leave Approvals then the work flow will
			// continue or else it will throw forbidden to the user.
			Permission permission = dao.checkForPermission("Leave Approvals",
					employee2);
		
			if (permission.getView()) {
				securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
						paramId, httpServletResponse);
			} else {
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				throw new UnauthorizedUserException("Unauthorized User");
			}

			// if (loggedEmployeeRole.equalsIgnoreCase("Employee")) {
			// httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
			// throw new UnauthorizedUserException("Unauthorized User");
			// }
			// if (loggedEmployeeRole.equalsIgnoreCase("manager") ||
			// loggedEmployeeRole.equalsIgnoreCase("It Admin")) {
			//
			// securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
			// paramId, httpServletResponse);
			//
			// }

		} else if (MethodName.equalsIgnoreCase("searchEmployees")) {
			Permission permission = dao.checkForPermission("Leave Approvals",
					employee);
			Permission permission2 = dao.checkForPermission(
					"Hierarchy Leave Approvals", employee);

			if (!permission.getView() && !permission2.getView()) {
				securityUtils.printIpAddress();
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				throw new UnauthorizedUserException("Unauthorized User");
			}

			// if (!(loggedEmployeeRole.equalsIgnoreCase("manager") ||
			// loggedEmployeeRole
			// .equalsIgnoreCase("admin"))) {
			// securityUtils.printIpAddress();
			// httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
			// throw new UnauthorizedUserException("Unauthorized User");
			// }

		} else if (MethodName.equalsIgnoreCase("leaveCategories")) {
			securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
					paramId, httpServletResponse);

		}

	}
}
