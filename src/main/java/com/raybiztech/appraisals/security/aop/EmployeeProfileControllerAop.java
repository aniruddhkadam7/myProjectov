package com.raybiztech.appraisals.security.aop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.EmployeeBankInformation;
import com.raybiztech.appraisals.business.EmployeeFamilyInformation;
import com.raybiztech.appraisals.certification.Dto.CertificationDto;
import com.raybiztech.appraisals.certification.business.Certification;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.employeeprofile.dto.EmployeeBankInformationDTO;
import com.raybiztech.employeeprofile.dto.EmployeeFamilyInformationDTO;
import com.raybiztech.employeeprofile.dto.FinanceDTO;
import com.raybiztech.lookup.business.BankNameLookup;
import com.raybiztech.rolefeature.business.Permission;

@Aspect
@Component
public class EmployeeProfileControllerAop {

	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;

	org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(EmployeeProfileControllerAop.class);

	@Before("execution(* com.raybiztech.employeeprofile.controller.*.*(..))")
	public void EmployeeProfileController(JoinPoint joinPoint)
			throws IOException {

		String MethodName = joinPoint.getSignature().getName();

		Map<String, Object> employeeDeatils = securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder();

		Employee employee = (Employee) employeeDeatils.get("employee");

		/*
		 * String loggedEmployeeName = SecurityContextHolder.getContext()
		 * .getAuthentication().getName();
		 * 
		 * Employee employee = dao.findByUniqueProperty(Employee.class,
		 * "userName", loggedEmployeeName);
		 */

		String loggedEmployeeRole = employee.getRole();

		Map<String, Object> methodParams = securityUtils
				.getMethodParams(joinPoint.getArgs());

		// ///////Here getting method params from map////////////

		Long paramId = (Long) methodParams.get("longParam");

		HttpServletResponse httpServletResponse = (HttpServletResponse) methodParams
				.get("httpServletResponse");

		EmployeeFamilyInformationDTO familyInformationDTO = (EmployeeFamilyInformationDTO) methodParams
				.get("familyInformationDTO");

		EmployeeBankInformationDTO bankInformationDTO = (EmployeeBankInformationDTO) methodParams
				.get("bankInformationDTO");

		CertificationDto certificationDto = (CertificationDto) methodParams
				.get("certificationDto");

		FinanceDTO financeDto = (FinanceDTO) methodParams.get("financeDto");

		// //////////////////////////////////////////////////////////////////////

		if (MethodName.equalsIgnoreCase("getUpcomingProvisionalPeriod")) {

			securityUtils.checkAccessForRespectiveEmployee(
					employee.getEmployeeId(), paramId, httpServletResponse);

		} else if (MethodName.equalsIgnoreCase("saveEmployeeFamilyDetails")
				|| MethodName.equalsIgnoreCase("updateEmployeeFamilyDetails")) {

			securityUtils.checkAccessForRespectiveEmployee(
					employee.getEmployeeId(),
					familyInformationDTO.getEmployeeId(), httpServletResponse);

		} else if (MethodName.equalsIgnoreCase("deleteFamilymember")
				|| MethodName.equalsIgnoreCase("getFamilyInformation")) {

			Set<EmployeeFamilyInformation> familyInformations = employee
					.getEmployeeFamilyDetails();

			List<Long> EmployeeFamilyIdList = null;
			if (familyInformations != null) {
				EmployeeFamilyIdList = new ArrayList<Long>();
				for (EmployeeFamilyInformation employeeFamilyInformation : familyInformations) {

					EmployeeFamilyIdList.add(employeeFamilyInformation
							.getFamilyId());
				}
			}

			securityUtils.checkListContainsParam(EmployeeFamilyIdList, paramId,
					httpServletResponse);

		} else if (MethodName.equalsIgnoreCase("updateCertification")
				|| MethodName.equalsIgnoreCase("certification")) {

			securityUtils.checkAccessForRespectiveEmployee(
					employee.getEmployeeId(), certificationDto.getEmployeeId(),
					httpServletResponse);

		} else if (MethodName.equalsIgnoreCase("getCertificate")) {

			List<Long> certificationIdList = null;
			List<Certification> certifications = dao.getAllOfProperty(
					Certification.class, "employee", employee);

			if (certifications != null) {
				certificationIdList = new ArrayList<Long>();
				for (Certification certification : certifications) {
					certificationIdList.add(certification.getId());
				}
			}

			securityUtils.checkListContainsParam(certificationIdList, paramId,
					httpServletResponse);

		} else if (MethodName.equalsIgnoreCase("updateFinanceInformation")) {

			if (!(loggedEmployeeRole.equalsIgnoreCase("admin") || loggedEmployeeRole
					.equalsIgnoreCase("finance"))) {

				securityUtils.checkAccessForRespectiveEmployee(
						employee.getEmployeeId(), financeDto.getEmployeeId(),
						httpServletResponse);

			}
		} else if (MethodName.equalsIgnoreCase("updateEmployeeBankDetails")
				|| MethodName.equalsIgnoreCase("saveEmployeeBankDetails")) {

			List<BankNameLookup> banks = dao.get(BankNameLookup.class);

			List<String> bankNamesList = (banks != null) ? new ArrayList<String>()
					: null;

			for (BankNameLookup lookup : banks) {
				bankNamesList.add(lookup.getBankName());
			}

			if (!bankNamesList.contains(bankInformationDTO.getBankName())) {
				httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
				securityUtils.printIpAddress();
				throw new UnauthorizedUserException(
						"Trying to add unauthorized bank");
			}

			if (!(loggedEmployeeRole.equalsIgnoreCase("admin") || loggedEmployeeRole
					.equalsIgnoreCase("finance"))) {
				securityUtils
						.checkAccessForRespectiveEmployee(
								employee.getEmployeeId(),
								bankInformationDTO.getEmployeeId(),
								httpServletResponse);
			}

		} else if (MethodName.equalsIgnoreCase("deleteBankAccount")
				|| MethodName.equalsIgnoreCase("getBankInformation")) {
			Permission financeAll = dao.checkForPermission("Finance All",
					employee);

			if (!financeAll.getView()) {

				List<EmployeeBankInformation> bankInfo = dao.getAllOfProperty(
						EmployeeBankInformation.class, "employee", employee);

				List<Long> BankIdList = new ArrayList<Long>();

				for (EmployeeBankInformation bankInformation : bankInfo) {

					if (bankInformation != null) {
						BankIdList.add(bankInformation.getBankId());
					}
				}
				securityUtils.checkListContainsParam(BankIdList, paramId,
						httpServletResponse);
			}

		} else if (MethodName.equalsIgnoreCase("getEmployeeBankInformation")
				|| MethodName.equalsIgnoreCase("getFinanceInformation")
				|| MethodName.equalsIgnoreCase("getEmployeeFamilyInformation")) {
			Permission financeAll = dao.checkForPermission("Finance All",
					employee);
			if (!financeAll.getView()) {
				securityUtils.checkAccessForRespectiveEmployee(
						employee.getEmployeeId(), paramId, httpServletResponse);
			}
		} else if (MethodName.equalsIgnoreCase("getEmployeeCertifications")) {
			Permission allSkills = dao.checkForPermission("All Skills",
					employee);
			Permission hierarchySkills = dao.checkForPermission(
					"Hierarchy Skills", employee);
			if (!allSkills.getView()) {
				if (hierarchySkills.getView()) {
					if (!(paramId.equals(employee.getEmployeeId()))) {
						Employee employee1 = dao
								.findBy(Employee.class, paramId);
						/*
						 * securityUtils.checkAccessForRespectiveEmployee(employee
						 * .getEmployeeId(), employee1.getManager()
						 * .getEmployeeId(), httpServletResponse);
						 */
						if (!employee.getEmployeeId().equals(
								employee1.getManager().getEmployeeId())) {
							securityUtils.printIpAddress();
							httpServletResponse
									.sendError(httpServletResponse.SC_NOT_ACCEPTABLE);
							throw new UnauthorizedUserException(
									"Unauthorized User");
						}
					}
				} else {
					securityUtils.checkAccessForRespectiveEmployee(
							employee.getEmployeeId(), paramId,
							httpServletResponse);
				}
			}

		}

		if (MethodName.equalsIgnoreCase("getEmployeeAssets")) {
			Permission allInfra = dao.checkForPermission("Infra All", employee);
			if (!allInfra.getView()) {
				securityUtils.checkAccessForRespectiveEmployee(
						employee.getEmployeeId(), paramId, httpServletResponse);
			}

		}

		// else if (MethodName.equalsIgnoreCase("getEmployeeHistory")) {
		// Permission history = dao.checkForPermission("My Profile-History",
		// employee);
		// if (!history.getView()) {
		// securityUtils.printIpAddress();
		// httpServletResponse.sendError(httpServletResponse.SC_FORBIDDEN);
		// throw new UnauthorizedUserException("Unauthorized User");
		// }
		//
		// }

	}
}
