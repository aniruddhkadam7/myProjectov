package com.raybiztech.projectMetrics.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectMetrics.business.EffortVariance;
import com.raybiztech.projectMetrics.business.ProjectSprints;
import com.raybiztech.projectMetrics.business.ProjectSprintsAudit;
import com.raybiztech.projectMetrics.business.ScheduleVariance;

public interface ProjectMetricsDao extends DAO {
	
	List<ScheduleVariance> getOverAllScheduleVariance(Long projectId);
	
	Map<String, Object> getSprintWiseTimeSheet(String hiveProjectName,String sprintName);

	List<ProjectSprintsAudit>getAuditForMetrics(String hiveProjectName);

	List<ProjectSprints> getScheduleVariance(String hiveProjectName);
	
	List<EffortVariance> getEffortVarianceList(Long projectId);
	
	public List<ProjectSprints> getProjectEffortVariance(String hiveProjectName);
	
	int checkingWorkpages(String hiveProjectName);
	
	Boolean checkingForProjectNameMatching(String hiveProjectName);

}
