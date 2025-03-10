package com.raybiztech.projectMetrics.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.projectMetrics.business.EffortVariance;
import com.raybiztech.projectMetrics.business.ProjectSprintsAudit;
import com.raybiztech.projectMetrics.business.ScheduleVariance;
import com.raybiztech.projectMetrics.dto.EffortVarianceDTO;
import com.raybiztech.projectMetrics.dto.ProjectSprintsAuditDto;
import com.raybiztech.projectMetrics.dto.ScheduleVarianceDto;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.separation.builder.SeparationBuilder;

@Component("projectMetricsbuilder")
public class ProjectMetricsbuilder {

	@Autowired
	DAO dao;
	@Autowired
	SeparationBuilder separationBuilder;
	@Autowired
	SecurityUtils securityUtils;

	public ScheduleVariance toEntity(ScheduleVarianceDto scheduledto) {
		ScheduleVariance schedulevariance = null;

		if (scheduledto != null) {
			schedulevariance = new ScheduleVariance();
			Project project = dao.findBy(Project.class,
					scheduledto.getProjectId());

			schedulevariance.setProject(project);

			schedulevariance
					.setBaselineStartdate(!scheduledto.getBaseLineStartDate()
							.equalsIgnoreCase("N/A") ? separationBuilder
							.stringToDate(scheduledto.getBaseLineStartDate())
							: null);

			schedulevariance
					.setBaselineEnddate(!scheduledto.getBaseLineEndDate()
							.equalsIgnoreCase("N/A") ? separationBuilder
							.stringToDate(scheduledto.getBaseLineEndDate())
							: null);

			schedulevariance
					.setActualStartdate(!scheduledto.getActualStartDate()
							.equalsIgnoreCase("N/A") ? separationBuilder
							.stringToDate(scheduledto.getActualStartDate())
							: null);

			schedulevariance.setActualEnddate(!scheduledto.getActualEndDate()
					.equalsIgnoreCase("N/A") ? separationBuilder
					.stringToDate(scheduledto.getActualEndDate()) : null);

			Double OverAllSheduleVariance = !scheduledto.getOverAllSheduleVariance()
					.equalsIgnoreCase("N/A")?Double.parseDouble(scheduledto.getOverAllSheduleVariance())
							:null;

			schedulevariance.setScheduleVariance(OverAllSheduleVariance);
			schedulevariance.setComments(scheduledto.getComments());

			Employee employee = dao.findBy(Employee.class,
					scheduledto.getCreatedBy());

			schedulevariance.setCreatedBy(employee);

			schedulevariance.setCreatedDate(new Second());

		}

		return schedulevariance;

	}

	public EffortVariance toEntity(EffortVarianceDTO effortVarianceDTO) {
		EffortVariance effortVariance = null;
		if (effortVarianceDTO != null) {
			Project project = dao.findBy(Project.class,
					effortVarianceDTO.getProjectId());
			effortVariance = new EffortVariance();
			effortVariance.setProject(project);
			effortVariance.setBaselineEffort(effortVarianceDTO
					.getBaselineEffort());
			effortVariance.setActualEffort(effortVarianceDTO.getActualEffort());
			effortVariance.setPercentageOfCompletion(effortVarianceDTO
					.getPercentageOfCompletion());
			effortVariance.setStatus(effortVarianceDTO.getStatus());
			effortVariance.setEffortVariance(effortVarianceDTO
					.getEffortVariance());
			effortVariance.setEffortsConsumed(effortVarianceDTO
					.getEffortsConsumed());
			effortVariance.setComments(effortVarianceDTO.getComments());
			effortVariance.setCreatedBy(dao
					.findBy(Employee.class, securityUtils
							.getLoggedEmployeeIdforSecurityContextHolder()));
			effortVariance.setCreatedDate(new Second());

		}
		return effortVariance;

	}

	public ScheduleVarianceDto toDto(ScheduleVariance schedule) {
		ScheduleVarianceDto scheduleDto = null;
		if (schedule != null) {
			scheduleDto = new ScheduleVarianceDto();
			scheduleDto.setId(schedule.getId());

			scheduleDto
					.setBaseLineStartDate(schedule.getBaselineStartdate() != null ? schedule
							.getBaselineStartdate().toString("dd/MM/yyyy")
							: "N/A");
			scheduleDto
					.setBaseLineEndDate(schedule.getBaselineEnddate() != null ? schedule
							.getBaselineEnddate().toString("dd/MM/yyyy")
							: "N/A");
			scheduleDto
					.setActualStartDate(schedule.getActualStartdate() != null ? schedule
							.getActualStartdate().toString("dd/MM/yyyy")
							: "N/A");
			scheduleDto
					.setActualEndDate(schedule.getActualEnddate() != null ? schedule
							.getActualEnddate().toString("dd/MM/yyyy") : "N/A");
			
			scheduleDto.setOverAllSheduleVariance(schedule.getScheduleVariance() != null ? schedule
					.getScheduleVariance().toString():"N/A");
			
			scheduleDto.setComments(schedule.getComments());
			Employee employee = schedule.getCreatedBy();
			scheduleDto.setEmployeeName(employee.getFullName());
			scheduleDto.setCreatedDate(schedule.getCreatedDate().toString(
					"dd/MM/yyyy"));
			scheduleDto.setCreatedTime(schedule.getCreatedDate().toString(
					"hh:mm a"));
		}
		return scheduleDto;

	}

	public List<ScheduleVarianceDto> toDtoList(List<ScheduleVariance> schedule) {
		List<ScheduleVarianceDto> scheduledto = null;
		if (schedule != null) {
			scheduledto = new ArrayList<ScheduleVarianceDto>();
			for (ScheduleVariance variance : schedule) {
				scheduledto.add(toDto(variance));
			}
		}
		return scheduledto;
	}

	public EffortVarianceDTO toDTO(EffortVariance effortVariance) {
		EffortVarianceDTO dto = null;
		if (effortVariance != null) {
			dto = new EffortVarianceDTO();
			dto.setId(effortVariance.getId());
			dto.setBaselineEffort(effortVariance.getBaselineEffort());
			dto.setActualEffort(effortVariance.getActualEffort());
			dto.setPercentageOfCompletion(effortVariance
					.getPercentageOfCompletion());
			dto.setStatus(effortVariance.getStatus());
			dto.setEffortVariance(effortVariance.getEffortVariance());
			dto.setEffortsConsumed(effortVariance.getEffortsConsumed());
			dto.setComments(effortVariance.getComments());
			dto.setCreatedBy(effortVariance.getCreatedBy()
					.getEmployeeFullName());
			dto.setCreatedDate(effortVariance.getCreatedDate().toString(
					"dd/MM/yyyy"));
			dto.setCreatedTime(effortVariance.getCreatedDate().toString(
					"hh:mm a"));

		}
		return dto;
	}

	public List<EffortVarianceDTO> toDTOList(List<EffortVariance> effort) {
		List<EffortVarianceDTO> effortDtoList = null;
		if (effort != null) {
			effortDtoList = new ArrayList<EffortVarianceDTO>();
			for (EffortVariance effortList : effort) {
				effortDtoList.add(toDTO(effortList));
			}
		}

		return effortDtoList;

	}

	public ProjectSprintsAuditDto toAuditDTO(ProjectSprintsAudit sprintAudit) {
		ProjectSprintsAuditDto auditDto = null;
		if (sprintAudit != null) {
			auditDto = new ProjectSprintsAuditDto();
			auditDto.setId(sprintAudit.getId());
			auditDto.setColumnName(sprintAudit.getColumnName());
			auditDto.setNewValue(sprintAudit.getNewValue());
			auditDto.setOldValue(sprintAudit.getOldValue());
			auditDto.setProjectName(sprintAudit.getProjectName());
			auditDto.setVersionName(sprintAudit.getVersionName());
			auditDto.setModifiedDate(sprintAudit.getModifiedDate().toString(
					"dd-MMM-yyyy hh:mm:ss a"));
		}
		return auditDto;
	}

	public List<ProjectSprintsAuditDto> DTOList(List<ProjectSprintsAudit> audit) {
		List<ProjectSprintsAuditDto> auditDtoList = null;
		if (audit != null) {
			auditDtoList = new ArrayList<ProjectSprintsAuditDto>();
			for (ProjectSprintsAudit effortList : audit) {
				auditDtoList.add(toAuditDTO(effortList));
			}
		}

		return auditDtoList;

	}

	// converting String into Ray Biz Tech Date
	private Date toDate(String stringDate) {
		Date date = null;
		try {
			date = Date.parse(stringDate, "dd/MM/yyyy");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

}
