package com.raybiztech.appraisals.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.security.dao.SecurityDAO;


@Transactional
@Service("employeeSecurityService")
public class EmployeeSecurityServiceImpl implements EmployeeSecurityService {

	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	SecurityDAO securityDao;
	@Override
	public EmployeeDTO getLoginEmployee(String userName) {
		// TODO Auto-generated method stub
		return employeeBuilder.createEmployeeDTO(securityDao.getEmployee(userName));
	}


}
