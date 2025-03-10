package com.raybiztech.SQAAudit.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.SQAAudit.business.SQAAuditForm;
import com.raybiztech.SQAAudit.business.SQAAuditTimeline;
import com.raybiztech.SQAAudit.business.SQAAuditees;
import com.raybiztech.SQAAudit.business.SQAAuditors;
import com.raybiztech.SQAAudit.dao.SQAAuditDAO;
import com.raybiztech.SQAAudit.dto.SQAAuditFormDto;
import com.raybiztech.SQAAudit.dto.SQAAuditNewTimelineDto;
import com.raybiztech.SQAAudit.dto.SQAAuditTimelineDto;
import com.raybiztech.SQAAudit.dto.SQAAuditeesDto;
import com.raybiztech.SQAAudit.dto.SQAAuditorsDto;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.separation.service.SeparationServiceImpl;
import com.raybiztech.utils.SecondParser;

@Component("sqaAuditbuilder")
public class SQAAuditBuilder {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	SQAAuditDAO sqaDaoImpl;

	Logger logger = Logger.getLogger(SQAAuditBuilder.class);

	public SQAAuditForm toEntity(SQAAuditFormDto dto) {

		SQAAuditForm form = null;
		if (dto != null) {
			if (dto.getId() != null) {
				form = sqaDaoImpl.findBy(SQAAuditForm.class, dto.getId());
				if (!dto.getAuditRescheduleStatus()) {

					form.getAuditors().clear();
					form.getAuditees().clear();

					Set<Long> auditors = dto.getAuditorIds();
					Set<SQAAuditors> auditorList = new HashSet<>();
					SQAAuditors auditor = null;
					for (Long empId : auditors) {
						if (auditors != null) {
							auditor = new SQAAuditors();
							Employee emp = sqaDaoImpl.findBy(Employee.class, empId);
							auditor.setAuditor(emp);
							auditorList.add(auditor);
						}
					}

					form.getAuditors().addAll(auditorList);
					Set<Long> auditees = dto.getAuditeeIds();
					Set<SQAAuditees> auditeesList = new HashSet<>();
					SQAAuditees auditee = null;
					for (Long empId : auditees) {
						if (auditees != null) {
							auditee = new SQAAuditees();
							Employee emp = sqaDaoImpl.findBy(Employee.class, empId);
							auditee.setAuditee(emp);
							auditeesList.add(auditee);
						}
					}

					form.getAuditees().addAll(auditeesList);

				}

				Employee loggedInEmployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
						.get("employee");
				form.setUpdatedBy(loggedInEmployee);
				form.setUpdatedDate(new Date());

			} else { 
				// System.out.println("in else builder"); 
				form = new SQAAuditForm();
				Employee loggedInEmployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
						.get("employee");
				form.setCreatedBy(loggedInEmployee);
				form.setCreatedDate(new Date());

				Set<Long> auditors = dto.getAuditorIds();
				Set<SQAAuditors> auditorList = new HashSet<>();
				SQAAuditors auditor = null;
				for (Long empId : auditors) {
					if (auditors != null) {
						auditor = new SQAAuditors();
						Employee emp = sqaDaoImpl.findBy(Employee.class, empId);
						auditor.setAuditor(emp);
						auditorList.add(auditor);
					}
				}

				form.setAuditors(auditorList);
				Set<Long> auditees = dto.getAuditeeIds();
				Set<SQAAuditees> auditeesList = new HashSet<>();
				SQAAuditees auditee = null;
				for (Long empId : auditees) {
					if (auditees != null) {
						auditee = new SQAAuditees();
						Employee emp = sqaDaoImpl.findBy(Employee.class, empId);
						auditee.setAuditee(emp);
						auditeesList.add(auditee);
					}
				}

				form.setAuditees(auditeesList);
				form.setAuditStatus("Open");
				form.setContainsFile(Boolean.FALSE);
			}
			if (dto.getAuditType() != null) {
				form.setAuditType(dto.getAuditType());
			}
			
			if(dto.getProjectType() != null) {
			form.setProjectType(dto.getProjectType().equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE);
			}
			Project project = null;
			
			if (dto.getProjectId() != null) {
				 project = sqaDaoImpl.findBy(Project.class, dto.getProjectId());
				 form.setProjectName(project.getProjectName());
				 form.setProjectManager(project.getProjectManager());
			}
				form.setProject(project);
				
			

			if (dto.getProjectName() != null) {
				form.setProjectName(dto.getProjectName());
			}
			if (dto.getProjectManagerId() != null) {
				Employee emp = sqaDaoImpl.findBy(Employee.class, dto.getProjectManagerId());
				form.setProjectManager(emp);
			}

			try {
				form.setAuditDate(DateParser.toDate(dto.getAuditDate()));
			} catch (ParseException e) { // TODO Auto-generated catch block e.printStackTrace();
			}
			form.setStartTime(SecondParser.toSecond(dto.getStartTime()));
			form.setEndTime(SecondParser.toSecond(dto.getEndTime()));
			if (dto.getAuditStatus() != null) {
				form.setAuditStatus(dto.getAuditStatus());
			}
			if (dto.getFormStatus() != null) {
				form.setFormStatus(dto.getFormStatus());
			}
			form.setAuditRescheduleStatus(
					(dto.getAuditRescheduleStatus() != null && dto.getAuditRescheduleStatus()) ? Boolean.TRUE
							: Boolean.FALSE);

		}
		return form;

	
	}

	public List<SQAAuditFormDto> toDTOList(List<SQAAuditForm> auditList) {
		List<SQAAuditFormDto> dtoList = null;
		if (auditList != null) {
			dtoList = new ArrayList<>();
			for (SQAAuditForm form : auditList) {
				dtoList.add(toDto(form));
			}
		}
		return dtoList;
	}

	public SQAAuditFormDto toDto(SQAAuditForm form) {
		SQAAuditFormDto dto = null;
		if (form != null) {
			dto = new SQAAuditFormDto();
			dto.setId(form.getId());
			dto.setAuditType(form.getAuditType());
			if(form.getProjectType() != null) {
			dto.setProjectType(form.getProjectType().equals(Boolean.TRUE) ? "true":"false");
			}
			if (form.getProject() != null) {

				dto.setProjectId(form.getProject().getId());
				dto.setProjectName(form.getProject().getProjectName());
				dto.setProjectManager(form.getProject().getProjectManager().getEmployeeFullName());
				dto.setProjectManagerId(form.getProject().getProjectManager().getEmployeeId());

			} else {
				dto.setProjectName(form.getProjectName() != null ? form.getProjectName() : null);
				dto.setProjectManager(
						form.getProjectManager().getFullName() != null ? form.getProjectManager().getFullName() : null);
				dto.setProjectManagerId(form.getProjectManager().getEmployeeId());
			}

			dto.setAuditDate(form.getAuditDate().toString("dd/MM/yyyy"));
			dto.setStartTime(twelveHoursFormate(form.getStartTime().getHourOfDay().getValue() + ":"
					+ form.getStartTime().getMinuteOfHour().getValue()));
			dto.setEndTime(twelveHoursFormate(form.getEndTime().getHourOfDay().getValue() + ":"
					+ form.getEndTime().getMinuteOfHour().getValue()));
			dto.setAuditStatus(form.getAuditStatus());
			dto.setFormStatus(form.getFormStatus());
			dto.setCreatedBy(form.getCreatedBy().getEmployeeFullName());
			dto.setCreatedDate(form.getCreatedDate().toString("dd/MM/yyyy"));
			dto.setAuditRescheduleStatus(form.getAuditRescheduleStatus());

			Set<SQAAuditors> auditors = form.getAuditors();
			Set<SQAAuditorsDto> auditorDtoList = null;
			if (auditors != null) {
				auditorDtoList = new HashSet<>();
				for (SQAAuditors auditor : auditors) {
					SQAAuditorsDto auditorDto = new SQAAuditorsDto();
					auditorDto.setId(auditor.getAuditor().getEmployeeId());
					auditorDto.setFullName(auditor.getAuditor().getEmployeeFullName());
					auditorDtoList.add(auditorDto);
				}

			}
			dto.setAuditors(auditorDtoList);

			Set<SQAAuditees> auditees = form.getAuditees();

			Set<SQAAuditeesDto> auditeesDtoList = null;
			if (auditees != null) {
				auditeesDtoList = new HashSet<>();
				for (SQAAuditees auditee : auditees) {
					SQAAuditeesDto auditeeDto = new SQAAuditeesDto();
					auditeeDto.setId(auditee.getAuditee().getEmployeeId());
					auditeeDto.setFullName(auditee.getAuditee().getEmployeeFullName());
					auditeesDtoList.add(auditeeDto);
				}

			}
			dto.setAuditees(auditeesDtoList);

			if (form.getPci() != null) {
				dto.setPci(form.getPci());
			}
			if (form.getFollowUpDate() != null) {
				dto.setFollowUpDate(form.getFollowUpDate().toString("dd/MM/yyyy"));
			}
			if (form.getSqaComments() != null) {
				dto.setSqaComments(form.getSqaComments());
			}
			if (form.getSqaFilesPath() != null) {
				dto.setSqaFilesPath(form.getSqaFilesPath());
				String filePath = form.getSqaFilesPath();
				String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
				dto.setSqaFileName(fileName);
			}
			if (form.getPmComments() != null) {
				dto.setPmComments(form.getPmComments());
			}
			if (form.getPmFilesPath() != null) {
				dto.setPmFilesPath(form.getPmFilesPath());
				String filePath = form.getPmFilesPath();
				String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
				dto.setPmFileName(fileName);
			}

			Employee loggedInemployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
					.get("employee");
			Permission totalList = sqaDaoImpl.checkForPermission("TotalProjectSQAAuditList", loggedInemployee);
			
			if(loggedInemployee.getRole().contains("SQA")){
				dto.setIsSQA(true);
			}
			/*
			 * if (!totalList.getView()) { if
			 * (dto.getFormStatus().equalsIgnoreCase("SQA Update") ) {
			 * dto.setDisableEditButton(Boolean.FALSE); } else {
			 * dto.setDisableEditButton(Boolean.TRUE); }
			 * 
			 * } else { if (dto.getFormStatus().equalsIgnoreCase("SQA Update") ||
			 * dto.getFormStatus().equalsIgnoreCase("PM Update")) {
			 * dto.setDisableEditButton(Boolean.TRUE); } else {
			 * dto.setDisableEditButton(Boolean.FALSE); } }
			 */

			/*
			 * System.out.println("condition" +
			 * ((dto.getFormStatus().equalsIgnoreCase("SQA Update") ||
			 * dto.getFormStatus().equalsIgnoreCase("PM Update")) && dto.getAuditStatus() !=
			 * "Closed"));
			 * 
			 * System.out.println("status"+ dto.getAuditStatus());
			 */

			// logger.warn("status"+dto.getAuditStatus());
			// logger.warn("status
			// condition"+(dto.getAuditStatus().equalsIgnoreCase("Closed")));
			// logger.warn("condition"+((dto.getFormStatus().equalsIgnoreCase("SQA Update")
			// || dto.getFormStatus().equalsIgnoreCase("PM Update"))
			// && !dto.getAuditStatus().equalsIgnoreCase("Closed")));

			// logger.warn("view"+(totalList.getView()));

			if (totalList.getView()) {
				dto.setShowEditButton(Boolean.TRUE);
				// logger.warn("inside" );
				if (dto.getAuditStatus().equalsIgnoreCase("Closed")) {
					dto.setDisableEditButton(Boolean.TRUE);
				} else {
					dto.setDisableEditButton(Boolean.FALSE);
				}
			}

			else {
				if (form.getProjectManager().equals(loggedInemployee)) {
					dto.setShowEditButton(Boolean.TRUE);
				} else {
					dto.setShowEditButton(Boolean.FALSE);
				}
				if (dto.getFormStatus().equalsIgnoreCase("Submit") || dto.getAuditStatus().equalsIgnoreCase("Closed")) {
					dto.setDisableEditButton(Boolean.TRUE);
				}
			}

		}
		return dto;
	}

	public String twelveHoursFormate(String time) {

		String[] individualTime = time.split(":");

		if (individualTime[0].equals("0")) {
			individualTime[0] = individualTime[0].concat("0");
		}

		if (individualTime[1].equals("0")) {
			individualTime[1] = individualTime[1].concat("0");
		}

		String convertedTime = null;
		if (Integer.parseInt(individualTime[0]) > 12) {

			String hour = getRoundedTime(Integer.parseInt(individualTime[0]) - 12);
			String minute = getRoundedTime(Integer.parseInt(individualTime[1]));

			convertedTime = hour.concat(":").concat(minute).concat(" PM");

		} else if (Integer.parseInt(individualTime[0]) == 12) {

			String hour = getRoundedTime(Integer.parseInt(individualTime[0]));
			String minute = getRoundedTime(Integer.parseInt(individualTime[1]));

			convertedTime = hour.concat(":").concat(minute).concat(" PM");

		} else {

			String hour = getRoundedTime(Integer.parseInt(individualTime[0]));
			String minute = getRoundedTime(Integer.parseInt(individualTime[1]));

			convertedTime = hour.concat(":").concat(minute).concat(" AM");

		}

		return convertedTime;
	}

	public String getRoundedTime(int time) {
		String stringTime = String.valueOf(time);
		stringTime = (stringTime.length() == 1) ? ("0".concat(stringTime)) : stringTime;
		return stringTime;
	}

	public SQAAuditTimeline toSQAAuditTimelineEntity(Long id, String persistType) {

		Employee employee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		SQAAuditForm form = sqaDaoImpl.findBy(SQAAuditForm.class, id);
		SQAAuditTimeline audit = new SQAAuditTimeline();

		audit.setAuditId(form.getId());
		audit.setAuditType(form.getAuditType());

		if (form.getProject() != null) {
			audit.setProject(form.getProject());
			audit.setProjectName(form.getProject().getProjectName());
			audit.setProjectManager(form.getProject().getProjectManager().getFullName());
		} else {
			audit.setProjectName(form.getProjectName());
			// System.out.println("project manager"+form.getProjectManager());
			Employee emp = sqaDaoImpl.findBy(Employee.class, form.getProjectManager().getEmployeeId());
			audit.setProjectManager(emp.getFullName());
		}

		audit.setAuditDate(form.getAuditDate());
		audit.setStartTime(form.getStartTime());
		audit.setEndTime(form.getEndTime());
		audit.setFormStatus(form.getFormStatus());
		audit.setAuditStatus(form.getAuditStatus());
		audit.setAuditRescheduleStatus(form.getAuditRescheduleStatus());
		if (form.getPci() != null) {
			audit.setPci(form.getPci());
		}
		if (form.getFollowUpDate() != null) {
			audit.setFollowUpDate(form.getFollowUpDate());
		}
		if (form.getSqaComments() != null) {
			audit.setSqaComments(form.getSqaComments());
		}
		if (form.getSqaFilesPath() != null) {
			audit.setSqaFilesPath(form.getSqaFilesPath());
		}
		if (form.getPmComments() != null) {
			audit.setPmComments(form.getPmComments());
		}
		if (form.getPmFilesPath() != null) {
			audit.setPmFilesPath(form.getPmFilesPath());
		}

		Set<SQAAuditors> auditors = form.getAuditors();
		String auditorsList = null;
		if (auditors != null) {
			Boolean flag = true;
			for (SQAAuditors auditor : auditors) {
				if (flag) {
					auditorsList = auditor.getAuditor().getEmployeeFullName();
					flag = false;
				} else {
					auditorsList = auditorsList + " , " + auditor.getAuditor().getEmployeeFullName();
				}
			}

		}
		audit.setAuditors(auditorsList);

		Set<SQAAuditees> auditees = form.getAuditees();
		String auditeesList = null;
		if (auditees != null) {
			Boolean flag = true;
			for (SQAAuditees auditee : auditees) {
				if (flag) {
					auditeesList = auditee.getAuditee().getEmployeeFullName();
					flag = false;
				} else {
					auditeesList = auditeesList + " , " + auditee.getAuditee().getEmployeeFullName();
				}
			}

		}
		audit.setAuditees(auditeesList);

		audit.setModifiedBy(employee);
		audit.setModifiedDate(new Second());
		audit.setPersistType(persistType);

		return audit;
	}

	public SQAAuditTimelineDto toSQATimelineDto(SQAAuditTimeline audit) {
		SQAAuditTimelineDto auditDto = null;
		if (audit != null) {
			auditDto = new SQAAuditTimelineDto();
			auditDto.setId(audit.getId());
			auditDto.setAuditId(audit.getAuditId());
			auditDto.setAuditType(audit.getAuditType());
			if (audit.getProject() != null) {
				auditDto.setProjectId(audit.getProject().getId());
				auditDto.setProjectName(audit.getProject().getProjectName());
				auditDto.setProjectManager(audit.getProject().getProjectManager().getEmployeeFullName());
			} else {
				auditDto.setProjectName(audit.getProjectName() != null ? audit.getProjectName() : null);
				auditDto.setProjectManager(audit.getProjectManager() != null ? audit.getProjectManager() : null);
			}

			auditDto.setAuditDate(audit.getAuditDate().toString("dd/MM/yyyy"));
			auditDto.setStartTime(twelveHoursFormate(audit.getStartTime().getHourOfDay().getValue() + ":"
					+ audit.getStartTime().getMinuteOfHour().getValue()));
			auditDto.setEndTime(twelveHoursFormate(audit.getEndTime().getHourOfDay().getValue() + ":"
					+ audit.getEndTime().getMinuteOfHour().getValue()));
			auditDto.setAuditors(audit.getAuditors());
			auditDto.setAuditees(audit.getAuditees());
			auditDto.setAuditStatus(audit.getAuditStatus());
			auditDto.setFormStatus(audit.getFormStatus());
			auditDto.setAuditRescheduleStatus(audit.getAuditRescheduleStatus());
			if (audit.getPci() != null) {
				auditDto.setPci(audit.getPci());
			}
			if (audit.getFollowUpDate() != null) {
				auditDto.setFollowUpDate(audit.getFollowUpDate().toString("dd/MM/yyyy"));
			}
			if (audit.getSqaComments() != null) {
				auditDto.setSqaComments(audit.getSqaComments());
			}
			if (audit.getSqaFilesPath() != null) {
				auditDto.setSqaFilesPath(audit.getSqaFilesPath());
				String filePath = audit.getSqaFilesPath();
				String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
				auditDto.setSqaFileName(fileName);
			}
			if (audit.getPmComments() != null) {
				auditDto.setPmComments(audit.getPmComments());
			}
			if (audit.getPmFilesPath() != null) {
				auditDto.setPmFilesPath(audit.getPmFilesPath());
				String filePath = audit.getPmFilesPath();
				String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
				auditDto.setPmFileName(fileName);
			}
			auditDto.setModifiedBy(audit.getModifiedBy().getEmployeeFullName());
			auditDto.setModifiedDate(audit.getModifiedDate().toString("dd-MM-yyyy hh:mm a"));
			auditDto.setPersistType(audit.getPersistType());
		}
		return auditDto;
	}

	public List<SQAAuditTimelineDto> toSQATimelineDtoList(List<SQAAuditTimeline> auditList) {
		List<SQAAuditTimelineDto> auditDtos = new ArrayList<SQAAuditTimelineDto>();
		if (auditDtos != null) {
			for (SQAAuditTimeline audit : auditList) {
				auditDtos.add(toSQATimelineDto(audit));
			}
		}
		return auditDtos;
	}

// 

	public Map<String, Object> toSQATimelineDtoList(Map<String, List<Audit>> map) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		List<SQAAuditNewTimelineDto> AuditDTOs = new ArrayList<SQAAuditNewTimelineDto>();

		if (map != null) {
			for (Map.Entry<String, List<Audit>> entry : map.entrySet()) {
				SQAAuditNewTimelineDto auditDTO = new SQAAuditNewTimelineDto();
				for (Audit audit : entry.getValue()) {

					Employee employee = sqaDaoImpl.findBy(Employee.class, audit.getModifiedBy());
					auditDTO.setModifiedBy(employee.getFullName());
					auditDTO.setModifiedDate(audit.getModifiedDate().toString("dd-MMM-yyyy hh:mm:ss a"));
					auditDTO.setPersistType(audit.getPersistType());

					switch (audit.getColumnName()) {

					case "auditType":
						auditDTO.setAuditType(audit.getNewValue());
						auditDTO.setOldAuditType(audit.getOldValue());
						break;
					case "projectType":
						auditDTO.setProjectType(audit.getNewValue());
						auditDTO.setOldProjectType(audit.getOldValue());
					case "projectId":
						auditDTO.setProjectId(audit.getNewValue());
						auditDTO.setOldProjectId(audit.getOldValue());
						break;
					case "projectName":
						auditDTO.setProjectName(audit.getNewValue());
						auditDTO.setOldProjectName(audit.getOldValue());
						break;

					case "projectManager":

						auditDTO.setProjectManager(audit.getNewValue());
						auditDTO.setOldProjectManager(audit.getOldValue());
						break;
					case "auditors":
						auditDTO.setAuditors(audit.getNewValue() != null ? audit.getNewValue().toString() : null);
						auditDTO.setOldAuditors(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;
					case "auditees":
						auditDTO.setAuditees(audit.getNewValue() != null ? audit.getNewValue().toString() : null);
						auditDTO.setOldAuditees(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;
					case "auditDate":
						auditDTO.setAuditDate(audit.getNewValue() != null ? audit.getNewValue() : null);
						auditDTO.setOldAuditDate(audit.getOldValue() != null ? audit.getOldValue() : null);
						break;
					case "startTime":
						auditDTO.setStartTime(audit.getNewValue());
						auditDTO.setOldStartTime(audit.getOldValue() != null ? audit.getOldValue() : null);
						break;
					case "endTime":
						auditDTO.setEndTime(audit.getNewValue());
						//logger.warn("End time new value"+ audit.getNewValue());
						auditDTO.setOldEndTime(audit.getOldValue() != null ? audit.getOldValue(): null);
						//logger.warn("End time old value"+ auditDTO.getOldEndTime());
						break;

					case "auditStatus":
						auditDTO.setAuditStatus(audit.getNewValue().toString());
						auditDTO.setOldAuditStatus(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;

					case "formStatus":
						auditDTO.setFormStatus(audit.getNewValue().toString());
						auditDTO.setOldFormStatus(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;

					case "auditRescheduleStatus":
						auditDTO.setAuditRescheduleStatus(audit.getNewValue().toString());
						break;

					case "pci":
						auditDTO.setPci(audit.getNewValue().toString());
						auditDTO.setOldPci(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;

					case "followUpDate":
						auditDTO.setFollowUpDate(audit.getNewValue().toString());
						auditDTO.setOldEndTime(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;

					case "sqaComments":
						auditDTO.setSqaComments(audit.getNewValue().toString());
						auditDTO.setOldSqaComments(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;

					case "sqaFileName":
						auditDTO.setSqaFileName(audit.getNewValue().toString());
						auditDTO.setOldSqaFileName(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;

					case "sqaFilesPath":
						auditDTO.setSqaFilesPath(audit.getNewValue().toString());
						auditDTO.setOldSqaFilesPath(
								audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						
						if(auditDTO.getSqaFilesPath() != null){
							String filePath = auditDTO.getSqaFilesPath();
							String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
							auditDTO.setSqaFileName(fileName);
						}
						
						if(auditDTO.getOldSqaFilesPath() != null){
							String filePath = auditDTO.getOldSqaFilesPath();
							String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
							auditDTO.setOldSqaFileName(fileName);
						}
						break;

					case "pmComments":
						auditDTO.setPmComments(audit.getNewValue().toString());
						auditDTO.setOldPmComments(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;

					case "pmFileName":
						auditDTO.setPmFileName(audit.getNewValue().toString());
						auditDTO.setOldPmFileName(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;

					case "pmFilesPath":
						auditDTO.setPmFilesPath(audit.getNewValue().toString());
						auditDTO.setOldPmFilesPath(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						
						if(auditDTO.getPmFilesPath() != null){
							String filePath = auditDTO.getPmFilesPath();
							String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
							auditDTO.setPmFileName(fileName);
						}
						
						if(auditDTO.getOldPmFilesPath()!= null){
							String filePath = auditDTO.getOldPmFilesPath();
							String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
							auditDTO.setOldPmFileName(fileName);
						}
						break;

					case "modifiedBy":
						auditDTO.setModifiedBy(audit.getNewValue().toString());
						auditDTO.setOldModifiedBy(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;

					case "modifiedDate":
						auditDTO.setModifiedDate(audit.getNewValue().toString());
						auditDTO.setOldModifiedDate(
								audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;

					case "persistType":
						auditDTO.setPersistType(audit.getNewValue().toString());
						auditDTO.setOldPersistType(audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;
					}
				}
				AuditDTOs.add(auditDTO);

			}

		}

		if (AuditDTOs != null) {
			Collections.sort(AuditDTOs, new Comparator<SQAAuditNewTimelineDto>() {

				@Override
				public int compare(SQAAuditNewTimelineDto audit1, SQAAuditNewTimelineDto audit2) {
					int k = 0;
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
						java.util.Date date1 = sdf.parse(audit1.getModifiedDate());
						java.util.Date date2 = sdf.parse(audit2.getModifiedDate());

						if (date1.after(date2)) {
							k = -1;
						}
						if (date1.before(date2)) {
							k = 1;
						}

					} catch (ParseException ex) {
						java.util.logging.Logger.getLogger(SQAAuditBuilder.class.getName()).log(Level.SEVERE, null, ex);
					}
					return k;
				}
			});
		}

		map1.put("list", AuditDTOs != null ? AuditDTOs : "");
		map1.put("size", AuditDTOs != null ? AuditDTOs.size() : "");

		return map1;
	}

}
