package com.raybiztech.appraisals.security.aop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.StatusReport;
import com.raybiztech.projectmanagement.dto.ChangeRequestDTO;
import com.raybiztech.projectmanagement.dto.MilestoneDTO;
import com.raybiztech.projectmanagement.dto.ReportDTO;
import com.raybiztech.projectmanagement.dto.StatusReportDTO;
import com.raybiztech.projectmanagement.service.AllocationDetailsService;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.rolefeature.business.Permission;

@Aspect
@Component
public class ProjectManagementAop {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	ProjectService projectService;
	@Autowired
	AllocationDetailsService allocationDetailsService;

	Logger logger = Logger.getLogger(ProjectManagementAop.class);

	@Before("execution(* com.raybiztech.projectmanagement.controller.*.*(..))")
	public void projectAop(JoinPoint joinPoint) throws IOException {

		Long paramId = null;

		String methodName = joinPoint.getSignature().getName();

		Map<String, Object> employeeDeatils = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();

		Employee employee = (Employee) employeeDeatils.get("employee");
		String employeeRole = employee.getRole();

		Boolean financeRole = !employeeRole.equalsIgnoreCase("finance");
		Boolean adminRole = !employeeRole.equalsIgnoreCase("admin");

		Map<String, Object> methodParams = securityUtils
				.getMethodParams(joinPoint.getArgs());

		paramId = (Long) methodParams.get("longParam");

		ChangeRequestDTO changeRequestDTO = (ChangeRequestDTO) methodParams
				.get("changeRequestDTO");

		MilestoneDTO milestoneDto = (MilestoneDTO) methodParams
				.get("milestoneDto");

		StatusReportDTO statusReportDTO = (StatusReportDTO) methodParams
				.get("statusReportDTO");

		HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
				.get("httpServletResponse");

		// here adding all methods to list where only one aop method is
		// applicable
		List<String> methodNameList = new ArrayList<String>();
		// methodNameList.add("getproject");
		methodNameList.add("getAllMilestones_UnderProject");
		methodNameList.add("getchangeRequestDTOList");
		methodNameList.add("AddCR");
		methodNameList.add("updateChangeRequest");
		methodNameList.add("deleteCR");
		methodNameList.add("updateMileStone");
		methodNameList.add("addMileStone");
		methodNameList.add("deleteMilestone");
		methodNameList.add("closeMilestone");
		methodNameList.add("getAllMileStoneAuldit");
		methodNameList.add("getProjectDetails");
		methodNameList.add("deleteStatusReport");
		methodNameList.add("addStatusReport");
		methodNameList.add("updateStatusReport");
		methodNameList.add("getProjectHistory");
		methodNameList.add("getProjectTimeSheet");

		if (methodName.equalsIgnoreCase("AddCR")
				|| methodName.equalsIgnoreCase("updateChangeRequest")) {
			paramId = changeRequestDTO.getProjectId();
		} else if (methodName.equalsIgnoreCase("deleteCR")) {
			ChangeRequest changeRequest = dao.findBy(ChangeRequest.class,
					paramId);
			paramId = changeRequest.getProjectId();
		} else if (methodName.equalsIgnoreCase("updateMileStone")) {
			Milestone milestone = dao.findBy(Milestone.class,
					milestoneDto.getId());
			paramId = milestone.getProject().getId();
		} else if (methodName.equalsIgnoreCase("deleteMilestone")
				|| methodName.equalsIgnoreCase("closeMilestone")
				|| methodName.equalsIgnoreCase("getAllMileStoneAuldit")) {
			Milestone milestone = dao.findBy(Milestone.class, paramId);
			paramId = milestone.getProject().getId();
		} else if (methodName.equalsIgnoreCase("updateStatusReport")) {
			StatusReport report = dao.findBy(StatusReport.class,
					statusReportDTO.getId());
			paramId = report.getProject().getId();
		} else if (methodName.equalsIgnoreCase("deleteStatusReport")) {
			StatusReport statusReport = dao.findBy(StatusReport.class, paramId);
			paramId = statusReport.getProject().getId();
		}

		paramId = ((methodName.equalsIgnoreCase("addMilestone") ? (milestoneDto
				.getProjectId()) : paramId));

		paramId = ((methodName.equalsIgnoreCase("addStatusReport") ? (statusReportDTO
				.getProjectId()) : paramId));

		Permission projectCR = dao.checkForPermission("Project-CR", employee);
		Permission projectMilestine = dao.checkForPermission(
				"Project-Milestone", employee);
		Permission hierarchyProjectCR = dao.checkForPermission(
				"Hierarchy Project-CR", employee);
		Permission hierarchyProjectMilestine = dao.checkForPermission(
				"Hierarchy Project-Milestone", employee);
		Permission projectList = dao.checkForPermission("Projects",
				employee);
		Permission indvidualProject = dao.checkForPermission(
				"Individual Project List", employee);

		if (methodNameList.contains(methodName)) {

			Project project = dao.findBy(Project.class, paramId);
			List<ReportDTO> allocationDetails = allocationDetailsService
					.getProjectDetails(project.getId());
			List<Long> resourcesOfProject = new ArrayList<Long>();

			for (ReportDTO details : allocationDetails) {
				resourcesOfProject.add(details.getEmployeeId());
			}
			
			// for managerial level 
			if ((projectCR.getView() && hierarchyProjectCR.getView())
					|| (projectMilestine.getView() && hierarchyProjectMilestine
							.getView())) {

				Long id = project.getProjectManager().getEmployeeId();
				List<Long> manager = projectService.mangerUnderManager(employee
						.getEmployeeId());
				// for DM if not reporting manager of PM

				if (resourcesOfProject != null) {
					if (resourcesOfProject.contains(employee.getEmployeeId())) {
						manager.add(id);
					}
				}
				securityUtils.checkListContainsParam(manager, id,
						httpServletResponse);
			}
			// for employees
			else if (projectList.getView() && indvidualProject.getView()) {
				securityUtils.checkListContainsParam(resourcesOfProject,
						employee.getEmployeeId(), httpServletResponse);
			}

		}

		// if (methodNameList.contains(methodName)) {
		// if (adminRole || financeRole) {
		// if (employeeRole.equalsIgnoreCase("manager")) {
		// Project project = dao.findBy(Project.class, paramId);
		// Long id = project.getProjectManager().getEmployeeId();
		// List<Long> manager = projectService
		// .mangerUnderManager(employee.getEmployeeId());
		// securityUtils.checkListContainsParam(manager, id,
		// httpServletResponse);
		// }
		// }
		//
		// }

		/*
		 * if (!methodName.equalsIgnoreCase("admin") ||
		 * !methodName.equalsIgnoreCase("finance")) {
		 * 
		 * if (employee.getRole().equalsIgnoreCase("Manager")) { Project project
		 * = dao.findBy(Project.class, paramId); Long id =
		 * project.getProjectManager().getEmployeeId(); List<Long> manager =
		 * projectService.mangerUnderManager(employee .getEmployeeId());
		 * securityUtils.checkListContainsParam(manager, id,
		 * httpServletResponse); } else {
		 * securityUtils.checkAccessForRespectiveEmployee(
		 * employee.getEmployeeId(), paramId, httpServletResponse); } }
		 */

	}
}
