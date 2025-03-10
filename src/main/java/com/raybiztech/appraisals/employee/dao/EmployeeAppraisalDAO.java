package com.raybiztech.appraisals.employee.dao;

import java.util.List;

import com.raybiztech.appraisals.business.Appraisal;
import com.raybiztech.appraisals.business.KPIRating;
import com.raybiztech.appraisals.business.KraWithWeightage;
import com.raybiztech.appraisals.dao.DAO;

public interface EmployeeAppraisalDAO extends DAO {
	Appraisal getEmployeeAppraisalData(Long employeeId);

	void saveKPIRating(KPIRating kpiRating);
	
	List<KPIRating> getKPIRatingsForAnEmployee(Long employeeId, Long kpiId);
	
	List<KraWithWeightage> getKraWithWeightagesForADesignation(Long designationId);
}
