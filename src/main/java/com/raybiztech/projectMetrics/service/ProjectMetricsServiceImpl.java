package com.raybiztech.projectMetrics.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.TimeActivity.builder.HiveTimeActivityBuilder;
import com.raybiztech.TimeActivity.business.EmpoloyeeHiveActivity;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityDTO;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.commons.Percentage;
import com.raybiztech.date.Date;
import com.raybiztech.mailtemplates.util.ProjectManagementMailConfiguration;
import com.raybiztech.projectMetrics.builder.ProjectMetricsbuilder;
import com.raybiztech.projectMetrics.builder.ProjectSprintsBuilder;
import com.raybiztech.projectMetrics.business.EffortVariance;
import com.raybiztech.projectMetrics.business.ProjectSprints;
import com.raybiztech.projectMetrics.business.ProjectSprintsAudit;
import com.raybiztech.projectMetrics.business.ScheduleVariance;
import com.raybiztech.projectMetrics.dao.ProjectMetricsDao;
import com.raybiztech.projectMetrics.dto.EffortVarianceDTO;
import com.raybiztech.projectMetrics.dto.ProjectSprintsAuditDto;
import com.raybiztech.projectMetrics.dto.ProjectSprintsDTO;
import com.raybiztech.projectMetrics.dto.ScheduleVarianceDto;
import com.raybiztech.projectmanagement.business.AllocationEffort;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectRequestMilestone;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.dto.AllocationEffortDto;
import com.raybiztech.projectmanagement.exceptions.HiveProjectNameNotExistException;
import com.raybiztech.projectmanagement.exceptions.NoWorkpackagesCreatedExceptions;
import com.raybiztech.projectmanagement.exceptions.ProjectNameNotExistException;
import com.raybiztech.recruitment.business.Holidays;

@Service("projectMetricsService")
@Transactional
public class ProjectMetricsServiceImpl implements ProjectMetricsService {
	@Autowired
	ProjectMetricsbuilder projectMetricsbuilder;
	@Autowired
	DAO dao;
	@Autowired
	ProjectMetricsDao projectMetricsDao;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	ProjectSprintsBuilder projectSprintsBuilder;
	@Autowired
	ProjectManagementMailConfiguration projectManagementMailConfiguration;
	@Autowired
	HiveTimeActivityBuilder hiveTimeActivityBuilder;
	@Autowired
	ResourceManagementDAO resourceManagementDAO;
	

	@Override
	public void saveSheduleVariance(ScheduleVarianceDto sheduledto) {
		Employee loggedEmployee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		sheduledto.setCreatedBy(loggedEmployee.getEmployeeId());

		ScheduleVariance schedulevariance = projectMetricsbuilder
				.toEntity(sheduledto);
		dao.save(schedulevariance);

		projectManagementMailConfiguration
				.sendSheduleVariancemailNotification(sheduledto);

	}

	@Override
	public List<ScheduleVarianceDto> getOverAllScheduleVariance(Long projectId) {

		List<ScheduleVariance> shedule = projectMetricsDao
				.getOverAllScheduleVariance(projectId);

		return projectMetricsbuilder.toDtoList(shedule);

	}

	public void saveEffortVariance(EffortVarianceDTO effortVarianceDTO) {
		EffortVariance effortVariance = projectMetricsbuilder
				.toEntity(effortVarianceDTO);
		ProjectSprints projectSprints = new ProjectSprints();

		dao.save(effortVariance);

		projectManagementMailConfiguration
				.sendEffortVariancemailNotification(effortVarianceDTO);

	}

	public void updateProjectSprints(List<ProjectSprintsDTO> sprints) {
		List<ProjectSprintsDTO> sprintNew = new ArrayList<ProjectSprintsDTO>();
		for(Object object : sprints) {
			LinkedHashMap hs = (LinkedHashMap) object;
			ProjectSprintsDTO dto = new ProjectSprintsDTO();
			Integer i = (Integer) hs.get("id");
			dto.setId(Long.valueOf(i.toString()));
			dto.setPercentageOfCompletion((String) hs.get("percentageOfCompletion"));
			sprintNew.add(dto);
		}
		List<ProjectSprints> ProjectSprints = projectSprintsBuilder
				.toEntity(sprintNew);
		for (ProjectSprints projectSprints2 : ProjectSprints) {
			dao.update(projectSprints2);
		}
		
	}
	
	@Override
	public Map<String, Object> getSprintWiseTimeSheet(Long projectId, String sprintName) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Project project = dao.findBy(Project.class, projectId);
		
		String projectName = project.getProjectName();
		
		Map<String, Object> timeSheetMap = projectMetricsDao.getSprintWiseTimeSheet(project.getHiveProjectName(),sprintName);
		
		List<EmpoloyeeHiveActivityDTO> timesheetList = hiveTimeActivityBuilder.convertEntityToDTO(
				 (List<EmpoloyeeHiveActivity>) timeSheetMap.get("list"));
		
		
		Integer noOfRecords = (Integer) timeSheetMap.get("size");
		map.put("list", timesheetList);
		map.put("size", noOfRecords);
		map.put("projectName", projectName);
		
		return map;

	}

	@Override
	public List<ProjectSprintsAuditDto> getAuditForMetrics(Long projectId) {

		Project project = dao.findBy(Project.class, projectId);
		List<ProjectSprintsAudit> audit = projectMetricsDao
				.getAuditForMetrics(project.getHiveProjectName());
		return projectMetricsbuilder.DTOList(audit);
	}
	
	@Override
	public List<ProjectSprintsDTO> getScheduleVariance(Long projectId) {
		Project project = dao.findBy(Project.class, projectId);
		List<ProjectSprints> projectSprints = new ArrayList<ProjectSprints>();
		if (project.getHiveProjectName() != null && !project.getHiveProjectName().isEmpty()) {
			 Boolean projectNameMatchedOrNot=projectMetricsDao.checkingForProjectNameMatching(project.getHiveProjectName());
				
	          if(!projectNameMatchedOrNot)
	          {
	        	  throw new ProjectNameNotExistException("Project name not matched with OVH Project name");
	          }
			int number =projectMetricsDao.checkingWorkpages(project.getHiveProjectName());
			if(number==0)
			{
				throw new NoWorkpackagesCreatedExceptions("No Workpackages created in hive");
			}
			
			projectSprints = projectMetricsDao.getScheduleVariance(project.getHiveProjectName());
		} else {
			throw new HiveProjectNameNotExistException("Current project doesn't have Hive project");

		}

		return projectSprintsBuilder.listDto(projectSprints);
	}
	
	@Override
	public Double overAllSheduleVarince(ProjectSprintsDTO projectsprintDTO) {
		return projectSprintsBuilder.sheduleVariance(projectsprintDTO);
	}

	
	@Override
	public List<EffortVarianceDTO> getEffortVarianceList(Long projectId) {
		List<EffortVariance> effortVarianceList = projectMetricsDao.getEffortVarianceList(projectId);
		return projectMetricsbuilder.toDTOList(effortVarianceList);

	}
	
	// Calculating EmployeeAllocation Effort
		@Override
		public AllocationEffortDto getAllocationEffort(Long projectId)

		{

			// System.out.println("allocation effort");

			// getting list of employees allocated to project
			List<AllocationEffort> allocationlist = resourceManagementDAO.getAllocationDetails(projectId);

			// System.out.println("allocation list size:" + allocationlist.size());

			Float totalEffort = 0f;
			Float resourceAllocation = 0f;
			Date startDate = null;
			Date endDate = null;

			for (AllocationEffort details : allocationlist) {

				// System.out.println("EmpId:" +
				// details.getEmployee().getEmployeeId());

				startDate = details.getPeriod().getMinimum();
				endDate = details.getPeriod().getMaximum();
				// System.out.println("startdate:" + startDate);
				// System.out.println("endDate:" + endDate);

				Calendar startcal = Calendar.getInstance();
				startcal.setTime(startDate.getJavaDate());

				Calendar endcal = Calendar.getInstance();
				endcal.setTime(endDate.getJavaDate());

				int workingDays = 0;

				while (!startcal.after(endcal)) {
					int day = startcal.get(Calendar.DAY_OF_WEEK);
					if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
						workingDays++;
					}
					startcal.add(Calendar.DATE, 1);
				}
				// System.out.println("working days:" + workingDays);

				List<Holidays> holidaysList = resourceManagementDAO.getHolidaysBetweenDates(startDate, endDate);

				Integer holidays = (!holidaysList.isEmpty()) ? holidaysList.size() : 0;

				// System.out.println("holidays: " + holidays);
				Integer totalDays = workingDays - holidays;

				// System.out.println("totalDays: " + totalDays);
				Float hours = 0f;

				Integer unitOfWork = 8;

				Percentage percentage = details.getPercentage();

				String per = percentage.toString();

				String p[] = per.split(" ");

				// System.out.println("percentage: " + p[0]);

				if (Double.valueOf(p[0]) != 0) {

					String result = String.valueOf(((Double.valueOf(p[0]) / 100) * unitOfWork));
					hours = (float) (totalDays * Double.valueOf(result));
					// System.out.println("hours: " + hours);
				}

				// total effort of all the employee
				totalEffort = totalEffort + hours;
				// System.out.println("totalEffort: " + totalEffort);
			}

			Long baselineEffort = getBaselineEffort(projectId);

			resourceAllocation = ((totalEffort - baselineEffort) / baselineEffort) * 100;

			//System.out.println("resource allocation overrun:" + resourceAllocation);

			AllocationEffortDto dto = new AllocationEffortDto();
			dto.setEmployeeAllocationEffort(totalEffort);
			dto.setBaselineEffort(baselineEffort);
			dto.setResourceAllocationOverrun(resourceAllocation);
			return dto;
		}

		public Long getBaselineEffort(Long projectId) {
			Long baselineEffort = 0L;
			Project project = dao.findBy(Project.class, projectId);

			if (project.getProjectRequest() != null) {

				Set<ProjectRequestMilestone> projectRequestmilestones = project.getProjectRequest()
						.getProjectRequestMilestone();
				for (ProjectRequestMilestone milestone : projectRequestmilestones) {
					baselineEffort = baselineEffort + Long.valueOf(milestone.getEffort());
				}

			}

			// System.out.println(baselineEffort);
			return baselineEffort;

		}
		
		
		@Override
		public List<ProjectSprintsDTO> getProjectEffortVariance(Long projectId) {
			Project project = dao.findBy(Project.class, projectId);
			List<ProjectSprints> list = new ArrayList<ProjectSprints>();
			if (project.getHiveProjectName() != null && !project.getHiveProjectName().isEmpty()) {
				 Boolean projectNameMatchedOrNot=projectMetricsDao.checkingForProjectNameMatching(project.getHiveProjectName());
					
		          if(!projectNameMatchedOrNot)
		          {
		        	  throw new ProjectNameNotExistException("Project name not matched with OVH Project name");
		          }
				
				int number =projectMetricsDao.checkingWorkpages(project.getHiveProjectName());
				if(number==0)
				{
					throw new NoWorkpackagesCreatedExceptions("No Workpackages created in hive");
				}
				
				list = projectMetricsDao.getProjectEffortVariance(project.getHiveProjectName());
			
			} else {
				throw new HiveProjectNameNotExistException("Current project doesn't have Hive project Name");

			}
			return projectSprintsBuilder.toDTOList(list);
		}
	
}
