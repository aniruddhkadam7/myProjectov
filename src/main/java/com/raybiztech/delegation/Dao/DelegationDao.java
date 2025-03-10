package com.raybiztech.delegation.Dao;

import java.util.List;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;

public interface DelegationDao  extends DAO{
	
	
	public List<Employee> getAllHRList();
	
	public List<Employee> getHrAssociates(Long hrId);

}
