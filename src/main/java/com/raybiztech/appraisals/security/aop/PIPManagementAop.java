package com.raybiztech.appraisals.security.aop;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.PIPManagement.dto.PIPDTO;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.rolefeature.business.Permission;

/**
 *
 * @author aprajita
 */

@Aspect
@Component
public class PIPManagementAop {

	Logger logger = Logger.getLogger(PIPManagementAop.class);

	@Autowired
	DAO dao;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	ProjectService projectService;

	@Before("execution(* com.raybiztech.appraisals.PIPManagement.controller.*.*(..))")
	public void pipManagementController(JoinPoint joinPoint) throws IOException {
		String methodName = joinPoint.getSignature().getName();
		Map<String, Object> employeeDetails = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee employee = (Employee) employeeDetails.get("employee");
		Map<String, Object> methodParams = securityUtils
				.getMethodParams(joinPoint.getArgs());
		HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
				.get("httpServletResponse");

		PIPDTO pipdto = (PIPDTO) methodParams.get("pipDto");
		Permission pip = dao.checkForPermission("PIP List", employee);
		Permission pip2 = dao
				.checkForPermission("Hierarchy PIP List", employee);

		if (methodName.equalsIgnoreCase("addPIP")) {
			if (pip.getView() && pip2.getView()) {
				List<Long> empIdList = projectService
						.mangerUnderManager(employee.getEmployeeId());

				Employee paramEmpId = dao.findBy(Employee.class,
						pipdto.getEmpId());
				securityUtils.checkListContainsParam(empIdList, paramEmpId
						.getManager().getEmployeeId(), httpServletResponse);
			}
		} else if (methodName.equalsIgnoreCase("updatePipDetails")
				|| methodName.equalsIgnoreCase("extendPip")
				|| methodName.equalsIgnoreCase("removeFromPip")) {
			PIP pips = dao.findBy(PIP.class, pipdto.getId());
			if (pip.getView() && pip2.getView()) {
				List<Long> empIdList = projectService
						.mangerUnderManager(employee.getEmployeeId());
				securityUtils.checkListContainsParam(empIdList, pips
						.getEmployee().getManager().getEmployeeId(),
						httpServletResponse);
			}
		} else if (methodName.equalsIgnoreCase("getPIPHistory")) {
			Long paramId = (Long) methodParams.get("longParam");
			PIP pip1 = dao.findBy(PIP.class, paramId);

			if (pip.getView() && pip2.getView()) {
				List<Long> empIdList = projectService
						.mangerUnderManager(employee.getEmployeeId());

				securityUtils.checkListContainsParam(empIdList, pip1
						.getEmployee().getManager().getEmployeeId(),
						httpServletResponse);
			}
		} else if (methodName.equalsIgnoreCase("viewPipDetails")) {
			Long paramId = (Long) methodParams.get("longParam");
			PIP pip1 = dao.findBy(PIP.class, paramId);

			if (pip.getView() && pip2.getView()) {
				List<Long> empIdList = projectService
						.mangerUnderManager(employee.getEmployeeId());

				securityUtils.checkListContainsParam(empIdList, pip1
						.getEmployee().getManager().getEmployeeId(),
						httpServletResponse);
			}
		}
	}

}
