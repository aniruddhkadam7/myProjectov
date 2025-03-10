package com.raybiztech.compliance.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.compliance.business.Compliance;
import com.raybiztech.compliance.business.ComplianceTask;


public interface ComplianceDao extends DAO {
	
	Map<String, Object> getDepartmentWiseTasks(Integer fromIndex,Integer toIndex,EmpDepartment department);
	
	Map<String, Object> getAllComplainceTasks(Integer fromIndex,Integer toIndex);
	
	ComplianceTask getComplianceTask(Compliance compliance);
	
	List<ComplianceTask> getLastComplianceTask(String lastMonth,String lastWeek,String yesterday);
	
	List<ComplianceTask> getMailComplianceTasks();
	
	Map<String, Object> getCompliances(Integer fromIndex,Integer toIndex);
	
	Boolean isComplianceNameExist(String complianceName,Long id);
	
	Long getComplianceTasksCount(Compliance compliance);
	
	List<Object> getTasksCount();
	
}
