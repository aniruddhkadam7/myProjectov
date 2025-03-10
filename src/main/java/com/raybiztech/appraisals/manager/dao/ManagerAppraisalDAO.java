package com.raybiztech.appraisals.manager.dao;

import java.util.List;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.AppraisalDTO;

public interface ManagerAppraisalDAO extends DAO {
	public void saveKPIRatingTOKPIManagerAppraisalData(AppraisalDTO dto);

	List<Employee> getEmployees(List<Long> managerId);

	List<Employee> getInActiveEmployees(Long managerId);

	List<Employee> getReporteesIncludingManager(Long managerId);

}
