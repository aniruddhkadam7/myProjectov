package com.raybiztech.appraisals.security.aop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Skill;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.SkillDTO;
import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.recruitment.controller.JobPortalController;
import com.raybiztech.rolefeature.business.Permission;

@Aspect
@Component
public class JobPortalControllerAop {

	@Autowired
	DAO dao;
	@Autowired
	JobPortalController jobPortalController;
	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(JobPortalControllerAop.class);

	@Before("execution(* com.raybiztech.recruitment.controller.*.*(..))")
	public void JobPortalAop(JoinPoint joinPoint) throws IOException {

		String MethodName = joinPoint.getSignature().getName();

		/*
		 * String LoggedEmployeeName = SecurityContextHolder.getContext()
		 * .getAuthentication().getName();
		 * 
		 * Employee employee = dao.findByUniqueProperty(Employee.class,
		 * "userName", LoggedEmployeeName);
		 */

		Map<String, Object> employeeDeatils = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee employee = (Employee) employeeDeatils.get("employee");

		String loggedEmployeeRole = null;
		if (employee != null) {
			loggedEmployeeRole = employee.getRole();
		}

		Map<String, Object> methodParams = securityUtils
				.getMethodParams(joinPoint.getArgs());

		HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
				.get("httpServletResponse");
		Long paramId = (Long) methodParams.get("longParam");
		String stringParam = (String) methodParams.get("stringParam");
		EmployeeDTO employeeDTO = (EmployeeDTO) methodParams.get("employeeDTO");
		SkillDTO skillDTO = (SkillDTO) methodParams.get("skillDTO");

		List<Long> candidateInterviewIdList = null;
		List<Long> candidateIDsList = null;

		Long loggedEmpID = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		if (MethodName.equalsIgnoreCase("empScheduleInterviewDetailsByID")) {
			Permission scheduledInterviewsAll = dao.checkForPermission(
					"Scheduled Interviews All", employee);
			logger.warn("at line 83:" + scheduledInterviewsAll);

			if (!scheduledInterviewsAll.getView()) {

				List<CandidateInterviewCycle> candidate = dao
						.getAllInterviewCylesUnderEmployee(loggedEmpID);
				if (candidate != null) {
					candidateInterviewIdList = new ArrayList<Long>();
					for (CandidateInterviewCycle cycle : candidate) {
						candidateInterviewIdList.add(cycle
								.getInterviewCycleId());

					}

					securityUtils.checkListContainsParam(
							candidateInterviewIdList, paramId,
							httpServletResponse);
				}
			}
		} else if (MethodName.equalsIgnoreCase("timeLineDetails")) {

			Permission candidateTimeLine = dao.checkForPermission(
					"candidateTimeLine", employee);

			if (!candidateTimeLine.getView()) {

				List<CandidateInterviewCycle> candidate = dao
						.getAllInterviewCylesUnderEmployee(loggedEmpID);

				if (candidate != null) {
					candidateIDsList = new ArrayList<Long>();
					for (CandidateInterviewCycle cycle : candidate) {
						candidateIDsList
								.add(cycle.getCandidate().getPersonId());
					}

					securityUtils.checkListContainsParam(candidateIDsList,
							Long.parseLong(stringParam), httpServletResponse);
				}
			}
		} else if (MethodName.equalsIgnoreCase("addSkillToEmployee")
				|| MethodName.equalsIgnoreCase("updateEmployeeSkill")) {

			securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
					skillDTO.getEmployee().getId(), httpServletResponse);

		} else if (MethodName.equalsIgnoreCase("editSkill")
				|| MethodName.equalsIgnoreCase("deleteEmployeeSkill")) {
			List<Long> SkillIDs = null;
			Set<Skill> skills = employee.getEmployeeSkills();
			if (skills != null) {
				SkillIDs = new ArrayList<Long>();
			}
			for (Skill skill : skills) {
				SkillIDs.add(skill.getSkillId());
			}
			securityUtils.checkListContainsParam(SkillIDs,
					Long.parseLong(stringParam), httpServletResponse);

		} else if (MethodName.equalsIgnoreCase("updateEmployeeDetails")
				|| MethodName.equalsIgnoreCase("editEmployee")) {
			Permission employeeEdit = dao.checkForPermission("Employee",
					employee);

			if (!employeeEdit.getUpdate()) {

				securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
						employeeDTO.getId(), httpServletResponse);

			}
		} else if (MethodName
				.equalsIgnoreCase("getProfilePaginationEmployeesData")) {

			Permission employeeDirectoryOptions = dao.checkForPermission(
					"Employee Directory-Options", employee);

			 if ((!employeeDirectoryOptions.getView() && (stringParam
			 .equalsIgnoreCase("inactive") ||
			  stringParam.equalsIgnoreCase("all") || stringParam
			  .equalsIgnoreCase("UnderNotice")))) {
			  securityUtils.printIpAddress();
			  httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
			  throw new UnauthorizedUserException(
			  "Not Authorized to watch all employee status");
			  
			  }
			 
		} else if (MethodName.equalsIgnoreCase("loggedInEmployeeData")) {
			Permission employeeEdit = dao.checkForPermission("Employee",
					employee);
			if (!employeeEdit.getUpdate()) {
				securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
						Long.parseLong(stringParam), httpServletResponse);

			}

		} else if (MethodName.equalsIgnoreCase("getEmployeeskills")) {
			Permission allSkills = dao.checkForPermission("All Skills",
					employee);
			Permission hierarchySkills = dao.checkForPermission(
					"Hierarchy Skills", employee);
			if (!allSkills.getView()) {
				if (hierarchySkills.getView()) {
					if (!(paramId.equals(loggedEmpID))) {
						Employee employee1 = dao
								.findBy(Employee.class, paramId);
						/*
						 * logger.warn("manager id" +
						 * employee1.getManager().getEmployeeId());
						 * securityUtils.checkAccessForRespectiveEmployee(
						 * loggedEmpID, employee1.getManager() .getEmployeeId(),
						 * httpServletResponse);
						 */

						if (!loggedEmpID.equals(employee1.getManager()
								.getEmployeeId())) {
							securityUtils.printIpAddress();
							httpServletResponse
									.sendError(httpServletResponse.SC_NOT_ACCEPTABLE);
							throw new UnauthorizedUserException(
									"Unauthorized User");
						}

					}

				} else {
					securityUtils.checkAccessForRespectiveEmployee(loggedEmpID,
							paramId, httpServletResponse);
				}

			}

		}
	}
}
