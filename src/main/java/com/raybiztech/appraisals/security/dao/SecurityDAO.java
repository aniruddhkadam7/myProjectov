package com.raybiztech.appraisals.security.dao;

import com.raybiztech.appraisals.business.Employee;

public interface SecurityDAO {
	Employee getEmployee(String loginName);

}
