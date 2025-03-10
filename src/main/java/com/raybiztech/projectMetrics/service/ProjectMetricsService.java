package com.raybiztech.projectMetrics.service;

import java.util.List;
import java.util.Map;

import com.raybiztech.projectMetrics.business.ProjectSprints;
import com.raybiztech.projectMetrics.dto.EffortVarianceDTO;
import com.raybiztech.projectMetrics.dto.ProjectSprintsAuditDto;
import com.raybiztech.projectMetrics.dto.ProjectSprintsDTO;
import com.raybiztech.projectMetrics.dto.ScheduleVarianceDto;
import com.raybiztech.projectmanagement.dto.AllocationEffortDto;

public interface ProjectMetricsService {

	public void saveSheduleVariance(ScheduleVarianceDto sheduledto);

	public void saveEffortVariance(EffortVarianceDTO effortVarianceDTO);

	public List<ScheduleVarianceDto> getOverAllScheduleVariance(Long projectId);
	
	public void updateProjectSprints( List<ProjectSprintsDTO> sprints);
	

	Map<String, Object> getSprintWiseTimeSheet(Long projectId,String sprintName);

	public List<ProjectSprintsAuditDto> getAuditForMetrics(Long projectId);
	
	List<ProjectSprintsDTO> getScheduleVariance(Long projectId);
	
	Double overAllSheduleVarince(ProjectSprintsDTO projectsprintDTO);
	
	List<EffortVarianceDTO> getEffortVarianceList(Long projectId);
	
	AllocationEffortDto getAllocationEffort(Long projectId);
	
	public List<ProjectSprintsDTO> getProjectEffortVariance(Long projectId);

}
