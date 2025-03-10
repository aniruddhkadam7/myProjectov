package com.raybiztech.appraisals.security.service;

import com.raybiztech.appraisals.dto.EmployeeDTO;




public interface EmployeeSecurityService {


	EmployeeDTO getLoginEmployee(String userName);
}
