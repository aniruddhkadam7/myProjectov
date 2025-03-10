package com.raybiztech.compliance.service;

import java.util.List;
import java.util.Map;

import com.raybiztech.compliance.business.ComplianceTask;
import com.raybiztech.compliance.dto.ComplianceDTO;
import com.raybiztech.compliance.dto.ComplianceTaskSubmitDTO;
import com.raybiztech.compliance.dto.ComplianceTaskDTO;
	
public interface ComplianceService {
	
	Long addCompliance(ComplianceDTO complianceDTO);
	
	Map<String, Object> getComplianceTasks(Integer fromIndex,Integer toIndex);
	
	void closeComplianceTask(ComplianceTaskSubmitDTO complianceTaskSubmitDTO);
	
	Boolean isComplianceNameExist(String complianceName,Long id);
	
	Map<String, Object> getCompliances(Integer fromIndex,Integer toIndex);
	
	void editCompliance(ComplianceDTO complianceDTO);
}
