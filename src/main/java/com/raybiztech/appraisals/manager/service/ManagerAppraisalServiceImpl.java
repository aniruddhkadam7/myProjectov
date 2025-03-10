package com.raybiztech.appraisals.manager.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.builder.AppraisalBuilder;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Appraisal;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.AppraisalDTO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.manager.dao.ManagerAppraisalDAO;
import com.raybiztech.biometric.dao.BiometricDAO;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.rolefeature.business.Permission;

@Service("managerAppraisalService")
public class ManagerAppraisalServiceImpl implements ManagerAppraisalService {

	@Autowired
	DAO dao;
	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	ManagerAppraisalDAO managerAppraisalDao;
	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	AppraisalBuilder appraisalBuilder;
	@Autowired
	BiometricDAO biometricDAO;
	@Autowired
	ProjectService projectServiceImpl;

	public static Logger logger = Logger
			.getLogger(ManagerAppraisalServiceImpl.class);

	@Override
	public void saveKPIRatingTOKPIManagerAppraisalData(AppraisalDTO dto) {

	}

	@Transactional
	@Override
	public List<EmployeeDTO> getEmployees(Long managerEmployeeId) {

		List<Employee> employeesList = null;
		Employee employee = dao.findBy(Employee.class, managerEmployeeId);

		Permission totalList = dao.checkForPermission("Leave Approvals",
				employee);
		Permission hirerachyList = dao.checkForPermission(
				"Hierarchy Leave Approvals", employee);

		if (totalList.getView() && !hirerachyList.getView()) {
			employeesList = biometricDAO.getActiveEmployees();
		} else if (totalList.getView() && hirerachyList.getView()) {

			List<Long> managerIds = projectServiceImpl
					.mangerUnderManager(managerEmployeeId);
			employeesList = managerAppraisalDao.getEmployees(managerIds);
		}

		List<EmployeeDTO> employeeDTOList = employeeBuilder
				.leaveReportEmployeeDTOList(employeesList);
		return employeeDTOList;
	}

	@Transactional
	@Override
	public void saveManagerRating(AppraisalDTO appraisalDto) {
		logger.info("in getEmployees Service");
		Appraisal appraisal = appraisalBuilder.createAppraisal(appraisalDto);

		managerAppraisalDao.saveOrUpdate(appraisal);
	}

	@Transactional
	@Override
	public void saveManagerStatus(AppraisalDTO appraisalDTO) {
		Appraisal appraisal = managerAppraisalDao.findBy(Appraisal.class,
				appraisalDTO.getId());
		appraisal.setStatus(appraisalDTO.getStatus());
		managerAppraisalDao.saveOrUpdate(appraisal);
	}

	@Transactional
	@Override
	public void submitManagerAppraisal(Long employeeId) {
		logger.info("appraisalId in submit manager appraisal : " + employeeId);
		Employee employee = managerAppraisalDao.findBy(Employee.class,
				employeeId);
		employee.setManagerSubmitted(true);
		managerAppraisalDao.update(employee);
	}
}
