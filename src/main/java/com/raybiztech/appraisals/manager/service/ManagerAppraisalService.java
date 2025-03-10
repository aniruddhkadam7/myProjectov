package com.raybiztech.appraisals.manager.service;

import java.util.List;

import com.raybiztech.appraisals.dto.AppraisalDTO;
import com.raybiztech.appraisals.dto.EmployeeDTO;

public interface ManagerAppraisalService {
	public void saveKPIRatingTOKPIManagerAppraisalData(AppraisalDTO dto);

	List<EmployeeDTO> getEmployees(Long managerEmployeeId);

	void saveManagerRating(AppraisalDTO appraisalDto);

	void saveManagerStatus(AppraisalDTO appraisalDTO);

	void submitManagerAppraisal(Long employeeId);
}
