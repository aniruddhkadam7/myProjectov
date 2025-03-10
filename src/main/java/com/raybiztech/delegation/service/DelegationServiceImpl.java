package com.raybiztech.delegation.service;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.PIPManagement.service.PIPManagementServiceImpl;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.delegation.Builder.DelegationBuilder;
import com.raybiztech.delegation.Dao.DelegationDao;
import com.raybiztech.delegation.dto.DelegationDTO;
import com.raybiztech.projectmanagement.builder.AuditBuilder;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.ProjectRequest;
import com.raybiztech.recruitment.controller.JobPortalController;
import com.raybiztech.recruitment.service.JobPortalService;
import com.raybiztech.recruitment.service.JobPortalServiceImpl;
import com.raybiztech.supportmanagement.service.SupportManagementServiceImpl;

@Service("delegationServiceImpl")
@Transactional
public class DelegationServiceImpl implements DelegationService, Cloneable {

	@Autowired
	DelegationBuilder delegationBuilder;

	@Autowired
	DAO dao;

	@Autowired
	DelegationDao delegationDaoImpl;

	@Autowired
	AuditBuilder auditBuilder;

	@Autowired
	JobPortalService jobPortalServiceImpl;

	@Autowired
	SecurityUtils securityUtils;

	Logger logger = Logger.getLogger(DelegationServiceImpl.class);

	// update reporting manager to employees
	public void updateReportingManager(DelegationDTO delegationDTO) {

		List<Employee> employeeList = delegationBuilder.convertDtoToEntity(delegationDTO);

		for (Employee e : employeeList) {

			Long empId = e.getEmployeeId();

			Employee emp = dao.findBy(Employee.class, e.getManager().getEmployeeId());
			Employee oldEmployee = new Employee();

			try {
				oldEmployee = (Employee) emp.clone();
			} catch (CloneNotSupportedException ce) {
				java.util.logging.Logger.getLogger(DelegationServiceImpl.class.getName()).log(Level.SEVERE, null, ce);
			}
			Employee newManger = dao.findBy(Employee.class, delegationDTO.getManagerId());
			e.setManager(newManger);
			dao.update(e);

			Audit audit = new Audit();
			audit.setModifiedDate(new Second());
			audit.setModifiedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
			audit.setPersistType("UPDATED");
			audit.setTableName("EMPLOYEE");
			audit.setColumnName("manager");
			audit.setOldValue(oldEmployee.getFullName());
			audit.setNewValue(newManger.getFullName());
			audit.setReferenceId(empId);
			dao.save(audit);
		}
	}

	// get reportees under manager
	@Override
	public List<EmployeeDTO> getMangerUnderEmployees(Long mangerId) {

		List<Long> mangerIds = new ArrayList<Long>();
		mangerIds.add(mangerId);
		List<Employee> managerReportees = dao.getReportiesUnderManager(mangerIds);

		List<EmployeeDTO> employeeList = delegationBuilder.convertEntityToDTOList(managerReportees);

		return employeeList;
	}

	// getting all reportees manger data
	@Override
	public List<EmployeeDTO> getAllReportingManagerData() {

		List<Employee> mangers = dao.findByManagerName(Employee.class);

		List<EmployeeDTO> managersList = delegationBuilder.convertEntityToMangersList(mangers);

		return managersList;
	}

	@Override
	public List<EmployeeDTO> getroleTypeMangers(String role) {

		List<EmployeeDTO> roleTypeManagers = new ArrayList<EmployeeDTO>();

		List<EmployeeDTO> managers = getAllReportingManagerData();

		for (EmployeeDTO e : managers) {
			if (e.getRole().equalsIgnoreCase(role)) {
				roleTypeManagers.add(e);
			}
		}

		return roleTypeManagers;
	}

	@Override
	public List<EmployeeDTO> getAllHRList() {

		List<Employee> hr = delegationDaoImpl.getAllHRList();

		List<EmployeeDTO> hrList = delegationBuilder.convertEntityToMangersList(hr);

		return hrList;
	}

	// getting all reportees manger data
	@Override
	public List<EmployeeDTO> getHrAssociates(Long hrId) {

		List<Employee> hrAssociates = delegationDaoImpl.getHrAssociates(hrId);

		List<EmployeeDTO> hrAssociatesList = delegationBuilder.convertEntityToMangersList(hrAssociates);

		return hrAssociatesList;
	}

	@Override
	public void updateHrAssociates(DelegationDTO delegationDTO) {

		List<Employee> employeeList = delegationBuilder.convertDtoToHrEntity(delegationDTO);
		for (Employee e : employeeList) {
			
			Long empId = e.getEmployeeId();
			
			Employee emp = dao.findBy(Employee.class, e.getHrAssociate().getEmployeeId());
		
			Employee oldEmployee = new Employee();
			
			Employee hrAssociates = dao.findBy(Employee.class, delegationDTO.getManagerId());
			
			try {
				oldEmployee = (Employee) emp.clone();
			} catch (CloneNotSupportedException ce) {
				java.util.logging.Logger.getLogger(DelegationServiceImpl.class.getName()).log(Level.SEVERE, null, ce);
			}
			e.setHrAssociate(hrAssociates);
			dao.update(e);
			
			Audit audit = new Audit();
			audit.setModifiedDate(new Second());
			audit.setModifiedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
			audit.setPersistType("UPDATED");
			audit.setTableName("EMPLOYEE");
			audit.setColumnName("hrAssociate");
		
			audit.setOldValue(oldEmployee.getFullName());
			audit.setNewValue(hrAssociates.getFullName());
			audit.setReferenceId(empId);
			dao.save(audit);
		}

	}

	public Employee cloneMethod(Employee emp) {
		Employee oldEmp = new Employee();
		try {
			oldEmp = (Employee) emp.clone();
		} catch (CloneNotSupportedException ex) {
			java.util.logging.Logger.getLogger(SupportManagementServiceImpl.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return oldEmp;
	}

}
