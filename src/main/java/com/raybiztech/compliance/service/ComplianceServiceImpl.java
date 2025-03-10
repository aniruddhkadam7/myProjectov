package com.raybiztech.compliance.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.compliance.builder.ComplianceBuilder;
import com.raybiztech.compliance.business.Compliance;
import com.raybiztech.compliance.business.ComplianceTask;
import com.raybiztech.compliance.business.ComplianceTaskStatus;
import com.raybiztech.compliance.business.ComplianceTaskSubmit;
import com.raybiztech.compliance.dao.ComplianceDao;
import com.raybiztech.compliance.dto.ComplianceDTO;
import com.raybiztech.compliance.dto.ComplianceTaskSubmitDTO;
import com.raybiztech.compliance.dto.ComplianceTaskDTO;
import com.raybiztech.rolefeature.business.Permission;

@Transactional				
@Service("complianceServiceImpl")
public class ComplianceServiceImpl implements ComplianceService{

	@Autowired
    ComplianceDao complianceDaoImpl;
	
	@Autowired
	ComplianceBuilder complianceBuilder;
	
	@Autowired
	SecurityUtils securityUtils;
	
	@Override
	public Long addCompliance(ComplianceDTO complianceDTO) {
		Compliance compliance = complianceBuilder.toEntity(complianceDTO);
		
		Long complianceId = (Long) complianceDaoImpl.save(compliance);
		
		
		return complianceId;
	}

	@Override
	public Map<String, Object> getComplianceTasks(Integer fromIndex , Integer toIndex) {
		Map<String, Object> complianceTaskList = new HashMap<>();
		Employee employee = complianceDaoImpl.findBy(Employee.class, securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
		Permission complianceList = complianceDaoImpl.checkForPermission("Complaince Task List", employee);
		Permission departmentWise = complianceDaoImpl.checkForPermission("Department Wise Complaince Task List", employee);
		
		if(complianceList.getView() && !departmentWise.getView()) {
			Map<String, Object> listMap = complianceDaoImpl.getAllComplainceTasks(fromIndex, toIndex);
			complianceTaskList.put("complianceTaskList", complianceBuilder.toDTOList((List<ComplianceTask>) listMap.get("complianceTaskList")));
			complianceTaskList.put("complianceTaskListSize",listMap.get("complianceTaskListSize"));
			
		}else if(complianceList.getView() && departmentWise.getView()) {
			EmpDepartment empDepartment = complianceDaoImpl.findByUniqueProperty(EmpDepartment.class, "departmentName", employee.getDepartmentName());
			Map<String, Object> listMap = complianceDaoImpl.getDepartmentWiseTasks(fromIndex, toIndex, empDepartment);
			complianceTaskList.put("complianceTaskList", complianceBuilder.toDTOList((List<ComplianceTask>) listMap.get("complianceTaskList")));
			complianceTaskList.put("complianceTaskListSize",listMap.get("complianceTaskListSize"));
		}
		
		return complianceTaskList;
	}

	@Override
	public void closeComplianceTask(ComplianceTaskSubmitDTO complianceTaskSubmitDTO) {
		ComplianceTaskSubmit ctSubmit = complianceBuilder.toEntity(complianceTaskSubmitDTO); 
		complianceDaoImpl.save(ctSubmit);
		ComplianceTask complianceTask = ctSubmit.getComplianceTask();
		complianceTask.setComplianceTaskStatus(ComplianceTaskStatus.CLOSED);
		complianceDaoImpl.update(complianceTask);
		
	}

	@Override
	public Boolean isComplianceNameExist(String complianceName,Long id) {
		
		return complianceDaoImpl.isComplianceNameExist(complianceName, id);
	}

	@Override
	public Map<String, Object> getCompliances(Integer fromIndex, Integer toIndex) {
		Map<String, Object> listMap = complianceDaoImpl.getCompliances(fromIndex, toIndex);
		List<ComplianceDTO> dtoList = complianceBuilder.toComplianceDTOList((List<Compliance>) listMap.remove("list"));
		listMap.put("complianceList", dtoList);
		listMap.put("complianceListSize", listMap.remove("listSize"));
		return listMap;
	}

	@Override
	public void editCompliance(ComplianceDTO complianceDTO) {
		Compliance compliance = complianceBuilder.toEntity(complianceDTO);
		complianceDaoImpl.update(compliance);
	}
	
	
}
