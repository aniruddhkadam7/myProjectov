package com.raybiztech.appraisals.security.aop;

import com.raybiztech.appraisalmanagement.business.AppraisalForm;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisalmanagement.dto.AppraisalFormDto;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.rolefeature.business.Permission;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class AppraisalControllerAop {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	ProjectService projectService;

	Logger logger = Logger.getLogger(AppraisalControllerAop.class);

	@Before("execution(* com.raybiztech.appraisalmanagement.controller.*.*(..))")
	public void appraisalAop(JoinPoint joinPoint) throws IOException {

		Map<String, Object> employeeDetails = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();

		Long loggedEmployeeId = (Long) employeeDetails.get("employeeId");
		Employee loggedEmployee = (Employee) employeeDetails.get("employee");

		String MethodName = joinPoint.getSignature().getName();

		Map<String, Object> methodParams = securityUtils
				.getMethodParams(joinPoint.getArgs());

		Long paramId = (Long) methodParams.get("longParam");
		HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
				.get("httpServletResponse");
		AppraisalFormDto appraisalFormDto = (AppraisalFormDto) methodParams
				.get("appraisalFormDto");

		if (MethodName.equalsIgnoreCase("getAppraisalForm")) {

			Permission appraisalFromList = dao.checkForPermission(
					"Review List", loggedEmployee);
			Permission hierarchyAppraisalFromList = dao.checkForPermission(
					"Hierarchy Review List", loggedEmployee);
			Permission individualAppraisalFromList = dao.checkForPermission(
					"Individual Review List", loggedEmployee);
			// if (!(loggedEmployee.getRole().equalsIgnoreCase("admin") ||
			// loggedEmployee.getRole().equalsIgnoreCase("HR"))) {
			if (appraisalFromList.getView()
					&& hierarchyAppraisalFromList.getView()
					&& !individualAppraisalFromList.getView()) {
				// for manager role
				Employee paramEmployee = dao.findBy(Employee.class, paramId);
				if (paramEmployee != null) {
					if (!loggedEmployeeId.equals(paramId)) {
						List<Long> empIds = projectService
								.mangerUnderManager(loggedEmployeeId);
						if (!empIds.contains(paramEmployee.getManager()
								.getEmployeeId())) {
							securityUtils.printIpAddress();
							httpServletResponse
									.sendError(httpServletResponse.SC_FORBIDDEN);
							throw new UnauthorizedUserException(
									"Tryring to access anthor Employee appraisal form which are not allocated to   "
											+ loggedEmployeeId);
						}
					}
				} else {
					securityUtils.printIpAddress();
					httpServletResponse
							.sendError(httpServletResponse.SC_FORBIDDEN);
					throw new UnauthorizedUserException(
							"Tryring to access anthor Employee appraisal form which are not allocated to   "
									+ loggedEmployeeId);
				}
			} else if (appraisalFromList.getView()
					&& !hierarchyAppraisalFromList.getView()
					&& individualAppraisalFromList.getView()) {
				// for employee role
				securityUtils.checkAccessForRespectiveEmployee(
						loggedEmployeeId, paramId, httpServletResponse);
			}
			// }
		}
		if (MethodName.equalsIgnoreCase("employeeAppraisalForm")) {
			Permission appraisalFromList = dao.checkForPermission(
					"Review List", loggedEmployee);
			Permission hierarchyAppraisalFromList = dao.checkForPermission(
					"Hierarchy Review List", loggedEmployee);
			Permission individualAppraisalFromList = dao.checkForPermission(
					"Individual Review List", loggedEmployee);

			// if (!(loggedEmployee.getRole().equalsIgnoreCase("admin") ||
			// loggedEmployee.getRole().equalsIgnoreCase("HR"))) {

			if (appraisalFromList.getView()
					&& hierarchyAppraisalFromList.getView()
					&& !individualAppraisalFromList.getView()) {
				// for manager role
				if (!loggedEmployeeId.equals(appraisalFormDto.getEmployee()
						.getId())) {

					Employee employee = dao.findBy(Employee.class,
							appraisalFormDto.getEmployee().getId());

					Long paramEmployeeId = employee.getManager()
							.getEmployeeId();
					List<Long> empIds = projectService
							.mangerUnderManager(loggedEmployeeId);
					if (!empIds.contains(paramEmployeeId)) {
						// if (!loggedEmployeeId.equals(paramEmployeeId)) {
						securityUtils.printIpAddress();
						httpServletResponse
								.sendError(httpServletResponse.SC_FORBIDDEN);
						throw new UnauthorizedUserException(
								"Unauthorized to update Employee Appraisal Form");
					}
				}
			} else if (appraisalFromList.getView()
					&& !hierarchyAppraisalFromList.getView()
					&& individualAppraisalFromList.getView()) {
				// for employee role
				List<AppraisalForm> appraisalForms = dao.getAllOfProperty(
						AppraisalForm.class, "employee", loggedEmployee);
				List<Long> appFormIdList = null;
				if (appraisalForms != null) {
					appFormIdList = new ArrayList<Long>();
					for (AppraisalForm form : appraisalForms) {
						appFormIdList.add(form.getId());
					}
				}

				if (appraisalFormDto.getId() != null) {

					if (!appFormIdList.contains(appraisalFormDto.getId())) {
						securityUtils.printIpAddress();
						logger.warn("Employee  "
								+ loggedEmployee.getFullName()
								+ "  with employee Id"
								+ loggedEmployeeId
								+ "  trying to update anthor employee aprraisal data by changing form id to"
								+ appraisalFormDto.getId());

						httpServletResponse
								.sendError(httpServletResponse.SC_FORBIDDEN);
						throw new UnauthorizedUserException(
								"Unauthorized User(This Entity Doen't exist in our Records )");

					}

					/*
					 * securityUtils.checkListContainsParam(appFormIdList,
					 * appraisalFormDto.getId(), httpServletResponse);
					 */
				}
				securityUtils.checkAccessForRespectiveEmployee(
						loggedEmployeeId, appraisalFormDto.getEmployee()
								.getId(), httpServletResponse);
			}

			// }

		}
		if (MethodName.equalsIgnoreCase("addKpiToKra")) {
			Permission addKpi = dao.checkForPermission("KRA", loggedEmployee);
			if (!addKpi.getView()) {
				securityUtils.printIpAddress();
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				throw new UnauthorizedUserException(
						"KPI add  access is only for ADMIN & Manager");
			}
		}
		if (MethodName.equalsIgnoreCase("deleteKpi")) {
			Permission deleteKpi = dao
					.checkForPermission("KRA", loggedEmployee);
			if (!deleteKpi.getDelete()) {
				securityUtils.printIpAddress();
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				throw new UnauthorizedUserException(
						"KPI Delete access is only for ADMIN");
			}

		}
		if (MethodName.equalsIgnoreCase("getAllAppraisalCycle")) {
			Permission appraisalFromList = dao.checkForPermission(
					"Review List", loggedEmployee);
			if (!appraisalFromList.getView()) {
				securityUtils.printIpAddress();
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				;
				throw new UnauthorizedUserException(
						"Apprisal Cycle is only for admin and manager ");

			}

		}
		if (MethodName.equalsIgnoreCase("getExistingAppraisalForm")) {
			Permission appraisalFromList = dao.checkForPermission(
					"Review List", loggedEmployee);
			Permission hierarchyAppraisalFromList = dao.checkForPermission(
					"Hierarchy Review List", loggedEmployee);
			Permission individualAppraisalFromList = dao.checkForPermission(
					"Individual Review List", loggedEmployee);
			AppraisalForm appraisalForm = dao.findBy(AppraisalForm.class,
					paramId);
			if (appraisalForm != null) {
				if (appraisalFromList.getView()
						&& hierarchyAppraisalFromList.getView()
						&& !individualAppraisalFromList.getView()) {
					List<Long> empIds = projectService
							.mangerUnderManager(loggedEmployeeId);
					if (!empIds.contains(appraisalForm.getEmployee()
							.getManager().getEmployeeId())
							&& !loggedEmployeeId.equals(appraisalForm
									.getEmployee().getEmployeeId())) {
						securityUtils.printIpAddress();
						httpServletResponse
								.sendError(httpServletResponse.SC_FORBIDDEN);
						throw new UnauthorizedUserException(
								"Unauthorized to view the Employee Appraisal Form");
					}
				} else if (appraisalFromList.getView()
						&& !hierarchyAppraisalFromList.getView()
						&& individualAppraisalFromList.getView()) {
					if (!loggedEmployeeId.equals(appraisalForm.getEmployee()
							.getEmployeeId())) {
						securityUtils.printIpAddress();
						httpServletResponse
								.sendError(httpServletResponse.SC_FORBIDDEN);
						throw new UnauthorizedUserException(
								"Unauthorized to view the Employee Appraisal Form");
					}
				}
			} else {
				securityUtils.printIpAddress();
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				throw new UnauthorizedUserException(
						"Unauthorized to view the Employee Appraisal Form");
			}
		}
		if (MethodName.equalsIgnoreCase("getDesignationsUnderCycle")) {
			Permission appraisalTemplate = dao.checkForPermission(
					"Appraisal Template", loggedEmployee);
			if (!appraisalTemplate.getView()) {
				securityUtils.printIpAddress();
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				throw new UnauthorizedUserException(
						"Unauthorized to get the Appraisal Template");
			}
		}
		if (MethodName.equalsIgnoreCase("closeAppraisalForm")) {

			Permission hierarchyAppraisalFromList = dao.checkForPermission(
					"Hierarchy Review List", loggedEmployee);
			Permission individualAppraisalFromList = dao.checkForPermission(
					"Individual Review List", loggedEmployee);
			if (hierarchyAppraisalFromList.getView()
					|| individualAppraisalFromList.getView()) {
				securityUtils.printIpAddress();
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				throw new UnauthorizedUserException(
						"Unauthorized to get the Appraisal Template");
			}
		}
		if (MethodName.equalsIgnoreCase("kraForIndividual")) {
//			List<Long> managerList= projectService.mangerUnderManager(loggedEmployeeId);
//			List<Employee> employees=dao.getReportiesUnderManager(managerList);
//			employees.contains(loggedEmployee);
			Employee paramEmployee = dao.findBy(Employee.class, paramId);
			if (paramEmployee != null) {
			if (!loggedEmployeeId.equals(paramId)) {
				List<Long> empIds = projectService
						.mangerUnderManager(loggedEmployeeId);
				if (!empIds.contains(paramEmployee.getManager()
						.getEmployeeId())) {
					securityUtils.printIpAddress();
					httpServletResponse
							.sendError(httpServletResponse.SC_FORBIDDEN);
					throw new UnauthorizedUserException(
							"Tryring to access anthor Employee appraisal form which are not allocated to   "
									+ loggedEmployeeId);
				}
			}
			}
		}

	}
}
