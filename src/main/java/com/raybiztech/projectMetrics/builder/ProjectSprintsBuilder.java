package com.raybiztech.projectMetrics.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.projectMetrics.business.ProjectSprints;
import com.raybiztech.projectMetrics.dto.ProjectSprintsDTO;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.recruitment.business.Holidays;
import com.raybiztech.recruitment.utils.DateParser;

@Component("projectSprintsBuilder")
public class ProjectSprintsBuilder{
	@Autowired
	DAO dao;
	@Autowired
	ResourceManagementDAO resourceManagementDAO;
	
	public ProjectSprintsDTO toDTO(ProjectSprints projectSprints){
		ProjectSprintsDTO projectSprintsDTO = new ProjectSprintsDTO();
		if(projectSprints!=null){
		
		projectSprintsDTO.setId(projectSprints.getId());
		projectSprintsDTO.setVersionName(projectSprints.getVersionName());
		projectSprintsDTO.setActuallEffort(projectSprints.getActuallEffort());
		projectSprintsDTO.setPlannedEffort(projectSprints.getPlannedEffort());
		/*if(!projectSprintsDTO.getPlannedEffort().isEmpty())
		{
			Double plannedEffort=Double.parseDouble(projectSprints.getPlannedEffort());
		}*/
		projectSprintsDTO.setPercentageOfCompletion(projectSprints.getPercentageOfCompletion());
		projectSprintsDTO.setStatus(projectSprints.getStatus());
		}
		return projectSprintsDTO;
		
	}
	
	public List<ProjectSprintsDTO> toDTOList(List<ProjectSprints> list) {
		
		ArrayList<ProjectSprintsDTO> arrayList = new ArrayList<ProjectSprintsDTO>();
		for(ProjectSprints sprints:list){
			arrayList.add(toDTO(sprints));
		}
		
		return arrayList;
		
	}
	//saving metrics %of completion
		public List<ProjectSprints> toEntity(List<ProjectSprintsDTO> projectSprintsDTOs) {
			List<ProjectSprints> sprints = null;
			if(projectSprintsDTOs!=null) {
				sprints = new ArrayList<ProjectSprints>();
				for(ProjectSprintsDTO sprintdto : projectSprintsDTOs) {
					sprints.add(toEntitySprints(sprintdto));
				}
			}
			
			return sprints;
			
		}

		public ProjectSprints toEntitySprints(ProjectSprintsDTO sprintsDTO) {
			
			ProjectSprints projectSprints = null;
			if(sprintsDTO!=null) {
				projectSprints = dao.findBy(ProjectSprints.class, sprintsDTO.getId());
				projectSprints.setPercentageOfCompletion(sprintsDTO.getPercentageOfCompletion());
			}
			return projectSprints;
		}
		
		public ProjectSprintsDTO toDto(ProjectSprints projectSprint) {

			ProjectSprintsDTO sprintDto = null;

			if (projectSprint != null) {

				sprintDto = new ProjectSprintsDTO();
				if (projectSprint.getVersionName() != null) {

					sprintDto.setVersionName(projectSprint.getVersionName());
				}

				if (projectSprint.getActualEndDate() != null) {
					sprintDto.setActualEndDate(projectSprint.getActualEndDate()
							.toString("dd/MM/yyyy"));
				}
				if (projectSprint.getActualStartDate() != null) {
					sprintDto.setActualStartDate(projectSprint.getActualStartDate()
							.toString("dd/MM/yyyy"));
				}
				if (projectSprint.getStartDate() != null) {
					sprintDto.setBaseLineStartDate(projectSprint.getStartDate()
							.toString("dd/MM/yyyy"));
				}
				if (projectSprint.getEffectiveDate() != null) {
					sprintDto.setBaseLineEndDate(projectSprint.getEffectiveDate()
							.toString("dd/MM/yyyy"));
				}
				if(projectSprint.getStatus()!= null) {
					sprintDto.setStatus(projectSprint.getStatus());
					//System.out.println("status:" + projectSprint.getStatus());
				}
				if(projectSprint.getProjectedStartDate() != null) {
					sprintDto.setProjectedStartDate(projectSprint.getProjectedStartDate().toString("dd/MM/yyyy"));
				}
				if(projectSprint.getProjectedEndDate() != null) {
					sprintDto.setProjectedEndDate(projectSprint.getProjectedEndDate().toString("dd/MM/yyyy"));
				}
				sprintDto.setActuallEffort(projectSprint.getActuallEffort());
				
				if(projectSprint.getStartDate()!=null && projectSprint.getEffectiveDate()!=null) {
					if(!projectSprint.getStatus().equalsIgnoreCase("locked") && !projectSprint.getStatus().equalsIgnoreCase("closed"))
					{
						//System.out.println("for-open");
						//System.out.println("sprintName:" + projectSprint.getVersionName());
							if(projectSprint.getProjectedEndDate()!=null) {
								ProjectSprintsDTO projectSprintsDTO = new ProjectSprintsDTO();
								projectSprintsDTO.setBaseLineStartDate(projectSprint.getStartDate().toString("dd/MM/yyyy"));
								projectSprintsDTO.setBaseLineEndDate(projectSprint.getEffectiveDate().toString("dd/MM/yyyy"));
								projectSprintsDTO.setActualEndDate(projectSprint.getProjectedEndDate().toString("dd/MM/yyyy"));
							
							sprintDto.setSheduleVariance(sheduleVariance(projectSprintsDTO));
							}

					}
					else
					{
						//System.out.println("for-closed");
						//System.out.println("sprintName:" + projectSprint.getVersionName());
						if(projectSprint.getProjectedEndDate()!=null && projectSprint.getActualEndDate() != null
								&& projectSprint.getActualEndDate().isBefore(projectSprint.getProjectedEndDate())) {
						
							//System.out.println("lesser");
							ProjectSprintsDTO projectSprintsDTO = new ProjectSprintsDTO();
							projectSprintsDTO.setBaseLineStartDate(projectSprint.getStartDate().toString("dd/MM/yyyy"));
							projectSprintsDTO.setBaseLineEndDate(projectSprint.getEffectiveDate().toString("dd/MM/yyyy"));
							projectSprintsDTO.setActualEndDate(projectSprint.getProjectedEndDate().toString("dd/MM/yyyy"));
						sprintDto.setSheduleVariance(sheduleVariance(projectSprintsDTO));
						}
						
						else {
							//System.out.println("greater");
							//System.out.println("sprintName:" + projectSprint.getVersionName());
							ProjectSprintsDTO projectSprintsDTO = new ProjectSprintsDTO();
							projectSprintsDTO.setBaseLineStartDate(projectSprint.getStartDate().toString("dd/MM/yyyy"));
							projectSprintsDTO.setBaseLineEndDate(projectSprint.getEffectiveDate().toString("dd/MM/yyyy"));
							projectSprintsDTO.setActualEndDate(projectSprint.getActualEndDate().toString("dd/MM/yyyy"));
							sprintDto.setSheduleVariance(sheduleVariance(projectSprintsDTO));
						}
						}
					
				}
			}

			return sprintDto;

		}
		
		public List<ProjectSprintsDTO> listDto(List<ProjectSprints> projectsprint) {
			List<ProjectSprintsDTO> sprintDto = null;
			if (projectsprint != null) {
				sprintDto = new ArrayList<ProjectSprintsDTO>();
				for (ProjectSprints sprint : projectsprint) {
					sprintDto.add(toDto(sprint));
				}
			}

			return sprintDto;

		}
		
		public Double sheduleVariance(ProjectSprintsDTO projectsprint) {
			
	       
			Date actualEndDate = null;
			Date baseLineEndDate = null;
			Date baseLineStartDate = null;
			try {
				actualEndDate = DateParser.toDate(projectsprint
						.getActualEndDate());
				baseLineEndDate = DateParser.toDate(projectsprint
						.getBaseLineEndDate());
				baseLineStartDate = DateParser.toDate(projectsprint
						.getBaseLineStartDate());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			Double sv = 0.0;

			int actualDuration = 0;
			int planDuration = 0;
			
			if (projectsprint.getActualEndDate() != null
					&& projectsprint.getBaseLineEndDate() != null) {
				//System.out.println("BaseLine End Date" + baseLineEndDate);
				//System.out.println("Actual End Date" + actualEndDate);

				actualDuration = sheduleVarianceCalculation(baseLineEndDate,
						actualEndDate);

				//System.out.println("Actual Durtation:" + actualDuration);

			}

			if (projectsprint.getBaseLineEndDate() != null
					&& projectsprint.getBaseLineStartDate() != null) {
				planDuration = sheduleVarianceCalculation(baseLineStartDate,
						baseLineEndDate);
				//System.out.println("planned Duration:" + planDuration);
			}

			if (planDuration != 0) {
				//System.out.println("sv in Actual Durtation: " + actualDuration);
				//System.out.println("sv in plan Durtation: " + planDuration);

				Double actual = (double) actualDuration;
				Double plan = (double) planDuration;
				//System.out.println(actual / plan);

				sv = (double) ((actual / plan) * 100);

			}
			//System.out.println("Shedule variamce :" + sv);

			
		
			return sv;

		}
		
		public int sheduleVarianceCalculation(com.raybiztech.date.Date fromDate,
				com.raybiztech.date.Date toDate) {

			//System.out.println("baseLineEndDate in calculation:" + fromDate);
			//System.out.println("actualEndDate in calculation:" + toDate);

			Calendar startDate = Calendar.getInstance();
			startDate.setTime(fromDate.getJavaDate());

			Calendar endDate = Calendar.getInstance();
			endDate.setTime(toDate.getJavaDate());

			int workingDays = 0;

			if (startDate.getTimeInMillis() == endDate.getTimeInMillis()) {
				return 0;
			}

			if (startDate.getTimeInMillis() > endDate.getTimeInMillis()) {
				startDate.setTime(toDate.getJavaDate());
				endDate.setTime(fromDate.getJavaDate());
			}

			while (!startDate.after(endDate)) {
				int day = startDate.get(Calendar.DAY_OF_WEEK);
				if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
					workingDays++;
				}
				startDate.add(Calendar.DATE, 1);
			}

			//System.out.println("total working days:" + workingDays);
			
			if(fromDate.isAfter(toDate)) {
				Date tempDate = fromDate;
				fromDate = toDate;
				toDate = tempDate;
			}
			
			List<Holidays> holidaysList = resourceManagementDAO
					.getHolidaysBetweenDates(fromDate, toDate);

			//System.out.println("Total Holidays :" + holidaysList.size());

			Integer holidays = (!holidaysList.isEmpty()) ? holidaysList.size() : 0;

			Integer totalDays = workingDays - holidays;

			return totalDays;

		}
}
