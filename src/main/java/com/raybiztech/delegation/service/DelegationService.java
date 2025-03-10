package com.raybiztech.delegation.service;

import java.util.List;

import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.delegation.dto.DelegationDTO;

public interface DelegationService {

	void updateReportingManager(DelegationDTO delegationDTO);

	List<EmployeeDTO> getMangerUnderEmployees(Long mangerId);

	List<EmployeeDTO> getAllReportingManagerData();

	List<EmployeeDTO> getroleTypeMangers(String role);

	List<EmployeeDTO> getAllHRList();

	List<EmployeeDTO> getHrAssociates(Long hrId);

	void updateHrAssociates(DelegationDTO delegationDTO);
}
