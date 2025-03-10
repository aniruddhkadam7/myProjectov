package com.raybiztech.appraisals.security.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.raybiztech.appraisalmanagement.dto.AppraisalFormDto;
import com.raybiztech.appraisals.PIPManagement.dto.PIPDTO;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.certification.Dto.CertificationDto;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.SkillDTO;
import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.employeeprofile.dto.EmployeeBankInformationDTO;
import com.raybiztech.employeeprofile.dto.EmployeeFamilyInformationDTO;
import com.raybiztech.employeeprofile.dto.FinanceDTO;
import com.raybiztech.leavemanagement.dto.LeaveApplicationDTO;
import com.raybiztech.projectmanagement.dto.ChangeRequestDTO;
import com.raybiztech.projectmanagement.dto.MilestoneDTO;
import com.raybiztech.projectmanagement.dto.StatusReportDTO;
import com.raybiztech.supportmanagement.dto.SupportTicketsDTO;
import com.raybiztech.ticketmanagement.DTO.TicketDTO;

@Component
public class SecurityUtils {

	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(SecurityUtils.class);

	public Long getLoggedEmployeeIdforSecurityContextHolder() {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails)principal).getUsername();
	//System.out.println("getname:"+username);
		Employee employee = dao.findByUniqueProperty(Employee.class,
				"userName",username);

		return employee.getEmployeeId();
	}

	public Map<String, Object> getLoggedEmployeeDetailsSecurityContextHolder() {

		Map<String, Object> employeeDetailMap = new HashMap<String, Object>();
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((Employee)principal).getUsername();
		//System.out.println("name:"+username);
		Employee employee = dao.findByUniqueProperty(Employee.class,
				"userName", username);
		Long employeeId = employee.getEmployeeId();
		employeeDetailMap.put("employee", employee);
		employeeDetailMap.put("employeeId", employeeId);

		return employeeDetailMap;
	}

	public Map<String, Object> getMethodParams(Object[] paramObjects) {

		Map<String, Object> paramMap = null;

		if (paramObjects != null) {

			paramMap = new HashMap<String, Object>();
			for (Object object : paramObjects) {

				if (object instanceof Long) {
					paramMap.put("longParam", object);
				}
				if (object instanceof HttpServletResponse) {
					paramMap.put("httpServletResponse", object);
				}
				if (object instanceof EmployeeFamilyInformationDTO) {
					paramMap.put("familyInformationDTO", object);
				}
				if (object instanceof CertificationDto) {
					paramMap.put("certificationDto", object);
				}
				if (object instanceof FinanceDTO) {
					paramMap.put("financeDto", object);
				}
				if (object instanceof EmployeeBankInformationDTO) {
					paramMap.put("bankInformationDTO", object);
				}
				if (object instanceof String) {
					paramMap.put("stringParam", object);
				}
				if (object instanceof EmployeeDTO) {
					paramMap.put("employeeDTO", object);
				}
				if (object instanceof SkillDTO) {
					paramMap.put("skillDTO", object);
				}
				if (object instanceof LeaveApplicationDTO) {
					paramMap.put("applicationDTO", object);
				}
				if (object instanceof AppraisalFormDto) {
					paramMap.put("appraisalFormDto", object);
				}
				if (object instanceof TicketDTO) {
					paramMap.put("ticketDTO", object);
				}
				if (object instanceof ChangeRequestDTO) {
					paramMap.put("changeRequestDTO", object);
				}
				if (object instanceof MilestoneDTO) {
					paramMap.put("milestoneDto", object);
				}
				if (object instanceof StatusReportDTO) {
					paramMap.put("statusReportDTO", object);
				}
                if (object instanceof SupportTicketsDTO) {
					paramMap.put("supportTicketsDTO", object);
                }
                if(object instanceof PIPDTO){
                	paramMap.put("pipDto", object);
                }
			}

		}
		return paramMap;
	}

	public void checkAccessForRespectiveEmployee(Long loggedEmployeeId,
			Long paramId, HttpServletResponse httpServletResponse)
			throws IOException {

		if (paramId != null) {
			if (!loggedEmployeeId.equals(paramId)) {

				String ipAddress = getIPAddressOfLoggedInEmployee();
				logger.warn("Employee With id "
						+ loggedEmployeeId
						+ "  is trying to access anthor Employee's information whose id is"
						+ paramId + " From following address " + ipAddress);
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				throw new UnauthorizedUserException("Unauthorized User");
			}
		}

	}

	public void checkListContainsParam(List<Long> list, Long parameter,
			HttpServletResponse httpServletResponse) throws IOException {

		if (!list.isEmpty()) {
                    int id=list.indexOf(parameter);
                        if(id==-1)
                        {
                    printIpAddress();
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				throw new UnauthorizedUserException(
						"Unauthorized User(This Entity Doen't exist in our Records )");
                        }
//			if (!list.contains(parameter)) {
//				printIpAddress();
//				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
//				throw new UnauthorizedUserException(
//						"Unauthorized User(This Entity Doen't exist in our Records )");
//			}
		} else {
			logger.warn("SecurityUtils LIST is empty so Unable to check access");
		}
	}

	public String getIPAddressOfLoggedInEmployee() {

		HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();

		String ipAddress = null;
		if (httpRequest != null) {
			ipAddress = httpRequest.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = httpRequest.getRemoteAddr();
			}
		}
		return ipAddress;
	}

	public void printIpAddress() {

		HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();

		String ipAddress = null;
		if (httpRequest != null) {
			ipAddress = httpRequest.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = httpRequest.getRemoteAddr();
			}
		}
		logger.warn("Logged employee Id is "
				+ getLoggedEmployeeIdforSecurityContextHolder()
				+ "and  IpAddress is " + ipAddress);

	}

}
