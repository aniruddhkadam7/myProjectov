package com.raybiztech.projectMetrics.Controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.projectMetrics.business.ProjectSprints;
import com.raybiztech.projectMetrics.dto.EffortVarianceDTO;
import com.raybiztech.projectMetrics.dto.ProjectSprintsAuditDto;
import com.raybiztech.projectMetrics.dto.ProjectSprintsDTO;
import com.raybiztech.projectMetrics.dto.ScheduleVarianceDto;
import com.raybiztech.projectMetrics.service.ProjectMetricsService;
import com.raybiztech.projectmanagement.dto.AllocationEffortDto;
import com.raybiztech.projectmanagement.exceptions.HiveProjectNameNotExistException;
import com.raybiztech.projectmanagement.exceptions.NoWorkpackagesCreatedExceptions;
import com.raybiztech.projectmanagement.exceptions.ProjectNameNotExistException;

@Controller
@RequestMapping(value = "/projectmetrics")
public class ProjectMetricsController {
	@Autowired
	DAO dao;

	@Autowired
	ProjectMetricsService projectMetricsService;
	

	@RequestMapping(value = "/saveSheduleVariance", params = "projectId", method = RequestMethod.POST)
	@ResponseBody
	public void saveSheduleVariance(
			@RequestBody ScheduleVarianceDto sheduledto,
			@RequestParam Long projectId) {

		sheduledto.setProjectId(projectId);
		projectMetricsService.saveSheduleVariance(sheduledto);

	}
	//saving project effort variance to db
	@RequestMapping(value = "/saveEffortVariance",method = RequestMethod.POST)
	public @ResponseBody void saveEffortVariance(@RequestBody EffortVarianceDTO effortVarianceDTO){
		projectMetricsService.saveEffortVariance(effortVarianceDTO);
		
	}

	@RequestMapping(value = "/getOverAllScheduleVariance", params = "projectId", method = RequestMethod.GET)
	@ResponseBody
	public List<ScheduleVarianceDto> getOverAllScheduleVariance(
			@RequestParam Long projectId) {

		return projectMetricsService.getOverAllScheduleVariance(projectId);

	}
	@RequestMapping(value = "/updateSprintsEffortVarianceList",method = RequestMethod.PUT)
	public @ResponseBody void updateProjectSprints(@RequestBody List<ProjectSprintsDTO> sprints) {
		
		//System.out.println("sprints"+sprints.get(0).getClass());
		projectMetricsService.updateProjectSprints(sprints);
		
	}
	
	@RequestMapping(value = "/sprintWiseHiveTimeSheet", params = {"projectId" , "sprintName"},
			method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getSprintWiseTimeSheet(@RequestParam Long projectId,
			@RequestParam String sprintName){
		
				return projectMetricsService.getSprintWiseTimeSheet(projectId,sprintName);
		
	}
	

	@RequestMapping(value="/getAuditForMetrics",params="projectId",method=RequestMethod.GET)
	@ResponseBody
	public List<ProjectSprintsAuditDto> getAuditForMetrics(@RequestParam Long projectId)
	{
		
		return projectMetricsService.getAuditForMetrics(projectId);
		
	}
	
	@RequestMapping(value = "/getScheduleVariance", params = { "projectId" }, method = RequestMethod.GET)
	@ResponseBody
	public List<ProjectSprintsDTO> getScheduleVariance(
			@RequestParam Long projectId,
			HttpServletResponse httpServletResponse) {
		List<ProjectSprintsDTO> projectSprintDto = null;

		try {
			projectSprintDto = projectMetricsService.getScheduleVariance(projectId);
		} catch (HiveProjectNameNotExistException e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);

		} catch (ProjectNameNotExistException e) {
			httpServletResponse.setStatus(httpServletResponse.SC_GONE);
		}
		catch (NoWorkpackagesCreatedExceptions e) {
			httpServletResponse.setStatus(httpServletResponse.SC_BAD_GATEWAY);
		}
		

		return projectSprintDto;

	}
	
	@RequestMapping(value = "/overAllSheduleVarince", params = {
			"baseLineStartDate", "baseLineEndDate", "actualStartDate",
			"actualEndDate" }, method = RequestMethod.POST)
	public @ResponseBody Double overAllSheduleVarince(
			@RequestParam String baseLineStartDate,
			@RequestParam String baseLineEndDate,
			@RequestParam String actualStartDate,
			@RequestParam String actualEndDate) {
		ProjectSprintsDTO projectsprintDTO = new ProjectSprintsDTO();
		projectsprintDTO.setActualStartDate(!actualStartDate
				.equalsIgnoreCase("N/A") ? 
				actualStartDate : null);
		projectsprintDTO
				.setActualEndDate(!actualEndDate.equalsIgnoreCase("N/A") ? 
						actualEndDate : null);
		projectsprintDTO
				.setBaseLineStartDate(!baseLineStartDate.equalsIgnoreCase("N/A") ? 
						baseLineStartDate : null);
		projectsprintDTO
				.setBaseLineEndDate(!baseLineEndDate.equalsIgnoreCase("N/A") ? 
						baseLineEndDate : null);

		return projectMetricsService.overAllSheduleVarince(projectsprintDTO);

	}
	
	
	// getting effort variance list
		@RequestMapping(value = "/getEffortVarianceList", params = { "projectId" }, method = RequestMethod.GET)
		@ResponseBody
		public List<EffortVarianceDTO> getEffortVarianceList(
				@RequestParam Long projectId) {
			return projectMetricsService.getEffortVarianceList(projectId);

		}

	//getting Allocation Effort
		@RequestMapping(value = "/getAllocationEffort", params = { "projectId" }, method = RequestMethod.GET)
		@ResponseBody
		public AllocationEffortDto getAllocationEffort(@RequestParam Long projectId) {
			return projectMetricsService.getAllocationEffort(projectId);
		}
		
	// getting project effort variance
		@RequestMapping(value = "/getProjectEffortVariance", params = { "projectId" }, method = RequestMethod.GET)
		public @ResponseBody List<ProjectSprintsDTO> getProjectEffortVariance(
				@RequestParam Long projectId,
				HttpServletResponse httpServletResponse) {
			
			List<ProjectSprintsDTO> projectSprintDto = null;
			try {
				projectSprintDto = projectMetricsService
						.getProjectEffortVariance(projectId);
			} catch (HiveProjectNameNotExistException e) {
				httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);

			} catch (ProjectNameNotExistException e) {
				httpServletResponse.setStatus(httpServletResponse.SC_GONE);
			}
			catch (NoWorkpackagesCreatedExceptions e) {
				httpServletResponse.setStatus(httpServletResponse.SC_BAD_GATEWAY);
			}

			return projectSprintDto;
		}
	
}
