package com.raybiztech.hiveworkpackages.builder;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ibm.icu.util.Calendar;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.hiveworkpackages.Exception.InvalidUserException;
import com.raybiztech.hiveworkpackages.business.CustomValues;
import com.raybiztech.hiveworkpackages.business.CustomizableJournals;
import com.raybiztech.hiveworkpackages.business.Enumerations;
import com.raybiztech.hiveworkpackages.business.Journals;
import com.raybiztech.hiveworkpackages.business.TimeEntries;
import com.raybiztech.hiveworkpackages.business.TimeEntryJournals;
import com.raybiztech.hiveworkpackages.business.projects;
import com.raybiztech.hiveworkpackages.business.statuses;
import com.raybiztech.hiveworkpackages.business.types;
import com.raybiztech.hiveworkpackages.business.users;
import com.raybiztech.hiveworkpackages.business.versions;
import com.raybiztech.hiveworkpackages.business.work_packages;
import com.raybiztech.hiveworkpackages.dao.hiveworkpackagesDao;
import com.raybiztech.hiveworkpackages.dao.hiveworkpackagesDaoImpl;
import com.raybiztech.hiveworkpackages.dto.spentTimeDTO;
import com.raybiztech.hiveworkpackages.dto.work_packagesDto;
import com.raybiztech.projectmanagement.business.Project;


@Component("work_packagesBuilder")
public class work_packagesBuilder {

	@Autowired
	DAO dao;
	
	@Autowired
	hiveworkpackagesDao hiveworkpackageDaoImpl;
	
	@Autowired
	SecurityUtils securityUtils;


	public work_packages toEntity(work_packagesDto workPackageDto) throws ParseException {

		Employee loggedIn = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		work_packages workPackage = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		if (workPackageDto != null) {
			workPackage = new work_packages();

			workPackage.setSubject(workPackageDto.getSubject());
			workPackage.setDescription(workPackageDto.getDescription());
			if(workPackageDto.getStart_date() != null) {
			workPackage.setStart_date(formatter.parse(workPackageDto.getStart_date()));
			}
			if(workPackageDto.getDue_date() != null){
			workPackage.setDue_date(formatter.parse(workPackageDto.getDue_date()));
			}
			workPackage.setEstimated_hours(workPackageDto.getEstimated_hours());
			workPackage.setRemaining_hours(workPackageDto.getRemaining_hours());
			workPackage.setDone_ratio(workPackageDto.getDone_ratio());
			workPackage.setCreated_at(new Second());
			if(workPackageDto.getType_id() != null) {
			workPackage.setType_id(hiveworkpackageDaoImpl.findByUniqueProperty(types.class, "id", workPackageDto.getType_id()));
			}
			if(workPackageDto.getFixed_version_id() != null) {
			workPackage.setFixed_version_id(hiveworkpackageDaoImpl.findByUniqueProperty(versions.class, "id", workPackageDto.getFixed_version_id()));
			}
			if(workPackageDto.getProject_id() != null) {
			workPackage.setProject_id(hiveworkpackageDaoImpl.findByUniqueProperty(projects.class, "name", workPackageDto.getProjectname()));
			}
			if(workPackageDto.getAssigneeName() != null && workPackageDto.getAssigneeName()!="") {
				String assignee ="";
				if(workPackageDto.getAssigneeName().contains(" ")) {
					 assignee = workPackageDto.getAssigneeName().replace(" ", ".");
				}
				else {
					 assignee = workPackageDto.getAssigneeName();
				}
				
					Employee employee = dao.findByUniqueProperty(Employee.class, "userName", assignee);
					if(employee == null) {
						throw new InvalidUserException("please enter valid user name");
					}
					else {
						workPackage.setAssigned_to_id(hiveworkpackageDaoImpl.findByUniqueProperty(users.class, "login", assignee));
					}
			}
			if(workPackageDto.getResponsibleName() != null && workPackageDto.getResponsibleName()!="") {
				String responsible ="";
				if(workPackageDto.getResponsibleName().contains(" ")) {
					responsible = workPackageDto.getResponsibleName().replace(" ", ".");
				}
				else {
					responsible = workPackageDto.getAssigneeName();
				}
				Employee employee = dao.findByUniqueProperty(Employee.class, "userName", responsible);
				if(employee == null) {
					throw new InvalidUserException("please enter valid user name");
				}
				else {
					workPackage.setResponsible_id(hiveworkpackageDaoImpl.findByUniqueProperty(users.class, "login", responsible));
				}
			
			}
			
			workPackage.setPriority_id(hiveworkpackageDaoImpl.findByUniqueProperty(Enumerations.class, "id", workPackageDto.getPriority_id()));
			
			
			List<statuses> statusList= hiveworkpackageDaoImpl.getStatusList();
			workPackage.setStatus_id(statusList.get(0));
			
			workPackage.setAuthor_id(hiveworkpackageDaoImpl.findByUniqueProperty(users.class, "login", loggedIn.getUserName()));
			workPackage.setLock_version(0L);
			workPackage.setLft(1L);
			workPackage.setRgt(2L);
			workPackage.setPosition(3L);
			
		}

		return workPackage;

	}

	public work_packagesDto toDto(work_packages workPackage) {

		work_packagesDto workPackageDto = null;

		if (workPackage != null) {

			workPackageDto = new work_packagesDto();
			System.out.println("workPackageId:"+workPackage.getId());
			workPackageDto.setId(workPackage.getId());
			if(workPackage.getAssigned_to_id() != null){
			users userId = workPackage.getAssigned_to_id();
			System.out.println("username:"+userId.getLogin());
			Employee assignedToId = dao.findByUniqueProperty(Employee.class, "userName", userId.getLogin());
			workPackageDto.setAssigned_to_id(assignedToId.getEmployeeId());
			if(userId.getLogin().contains(".")){
			workPackageDto.setAssigneeName(assignedToId.getFullName());
			}else{
				workPackageDto.setAssigneeName(assignedToId.getFullName());
			}
			}else{
				workPackageDto.setAssigneeName("");
			}
			
			if(workPackage.getFixed_version_id() != null){
			versions versionsId = workPackage.getFixed_version_id();
			workPackageDto.setFixed_version_id(versionsId.getId());
			}
			
			types typeId = workPackage.getType_id();
			workPackageDto.setType_id(typeId.getId());
			System.out.println("projectName:"+workPackage.getProject_id().getName());
			Project project = dao.findByUniqueProperty(Project.class, "hiveProjectName", workPackage.getProject_id().getName());
			
			workPackageDto.setProject_id(project.getId());

			workPackageDto.setSubject(workPackage.getSubject());
			workPackageDto
					.setProjectname(project.getProjectName());
			workPackageDto.setTypename(workPackage.getType_id().getName());
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 

			if (workPackage.getStart_date() != null) {
				String date1 = formatter.format(workPackage.getStart_date());
				workPackageDto.setStart_date(date1);

			} else {
				workPackageDto.setStart_date(null);
			}
			

			if (workPackage.getDue_date() != null) {
				String date2 = formatter.format(workPackage.getDue_date());
				workPackageDto.setDue_date(date2);
			} else {
				workPackageDto.setDue_date(null);
			}
			workPackageDto.setDescription(workPackage.getDescription()!=null ? workPackage.getDescription():null);
			workPackageDto.setEstimated_hours(workPackage.getEstimated_hours()!=null ? workPackage.getEstimated_hours():0);
			workPackageDto.setRemaining_hours(workPackage.getRemaining_hours()!=null ? workPackage.getRemaining_hours():0);
			workPackageDto.setDone_ratio(workPackage.getDone_ratio());
			if(workPackage.getResponsible_id()!= null) {
				
			users user = workPackage.getResponsible_id();
			System.out.println("username:"+user.getLogin());
			Employee responsibleId = dao.findByUniqueProperty(Employee.class, "userName", user.getLogin());
			workPackageDto.setResponsible_id(responsibleId.getEmployeeId());
			
			if(workPackage.getResponsible_id().getLogin().contains(".")) {
				workPackageDto.setResponsibleName(responsibleId.getFullName());;
			}
			else {
				workPackageDto.setResponsibleName(responsibleId.getFullName());
			}
			}
			else {
				workPackageDto.setResponsibleName("");
			}
			
			workPackageDto.setPriority_id(workPackage.getPriority_id().getId());
			
			workPackageDto.setStatus_id(workPackage.getStatus_id().getId());
			
			Map<String, Object> journalMap = hiveworkpackageDaoImpl.getWorkPackageJournal(workPackage.getId());
			
			Journals journal = (Journals) journalMap.get("journal");
			
			if(journal != null) {
				List<CustomValues> customValues = hiveworkpackageDaoImpl.getCustomizedValues(workPackage.getId());	
				
				if(customValues != null) {
				
				for(CustomValues value : customValues) {
					if(value.getCustomFieldId() == 1L) {
						workPackageDto.setSeverity(Long.valueOf(value.getValue()));
					}
					if(value.getCustomFieldId() == 21L) {
						workPackageDto.setTestCaseType(Long.valueOf(value.getValue()));
					}
					if(value.getCustomFieldId() == 22L) {
						workPackageDto.setTestCaseCondition(value.getValue());;
					}
					if(value.getCustomFieldId() == 23L) {
						workPackageDto.setTestCaseExceptedResult(value.getValue());
					}
					if(value.getCustomFieldId() == 24L) {
						workPackageDto.setTestCaseActualResult(value.getValue());
					}
					if(value.getCustomFieldId() == 25L) {
						workPackageDto.setTestCaseAutomated(Boolean.parseBoolean(value.getValue()));
					}
					if(value.getCustomFieldId() == 26L) {
						workPackageDto.setTestCaseExecutionCycle(Long.valueOf(value.getValue()));
					}
					
				}
				
				}
			}
			
			
			String  workpackageEncyptedValue = Base64.encodeBase64String(workPackage.getId().toString().getBytes());
			workpackageEncyptedValue.replace("&","~");
			workPackageDto.setEncryptedWorkpackage(workpackageEncyptedValue);

		}

		return workPackageDto;
	}


	public List<work_packagesDto> toDtoList(List<work_packages> taskslist) {
		List<work_packagesDto> workpackagesDto = null;
		if (taskslist != null) {
			workpackagesDto = new ArrayList<work_packagesDto>();
			for (work_packages workpackge : taskslist) {
				workpackagesDto.add(toDto(workpackge));
			}
		}
		return workpackagesDto;

	}

	public TimeEntries convertLogtimeEntity(spentTimeDTO spentTimeDTO) throws ParseException {
		
		TimeEntries timeEntries = null;
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 

		
		
		if(spentTimeDTO!=null)
		{
			timeEntries = new TimeEntries();
			if(spentTimeDTO.getSpentDate()!=null)
			{
				timeEntries.setSpent_on(formatter.parse(spentTimeDTO.getSpentDate()));
				timeEntries.setProject_id(hiveworkpackageDaoImpl.findByUniqueProperty(projects.class, "id", spentTimeDTO.getProjectId()));
				timeEntries.setActivity_id(hiveworkpackageDaoImpl.findByUniqueProperty(Enumerations.class, "id", spentTimeDTO.getActivityId()));
				timeEntries.setUser_id(hiveworkpackageDaoImpl.findByUniqueProperty(users.class, "id", spentTimeDTO.getUserId()));
				timeEntries.setWork_package_id(hiveworkpackageDaoImpl.findByUniqueProperty(work_packages.class,"id",spentTimeDTO.getWorkPackageId()));
				
				timeEntries.setHours(spentTimeDTO.getSpentTime());
				timeEntries.setCreated_on(new Date());
				timeEntries.setUpdated_on(new Date());
				timeEntries.setComments(spentTimeDTO.getComments());
				timeEntries.setTmonth(spentTimeDTO.getMonth());
				timeEntries.setTyear(spentTimeDTO.getYear());
				Calendar cal = Calendar.getInstance();
				cal.setTime(timeEntries.getSpent_on());
				int week = cal.get(Calendar.WEEK_OF_YEAR);
				timeEntries.setTweek(week);
			
				
				
			}
			
		}

		return timeEntries;
	}

	public Journals convertTimeEntriesToJournals(spentTimeDTO spentTimeDTO,Long timeEntryId) {
		
		Journals journals = null;
		
		Map<String, Object> journalMap = hiveworkpackageDaoImpl.getWorkPackageJournal(timeEntryId);
		
		Integer versionSize = (Integer) journalMap.get("listSize");
		if(spentTimeDTO!=null)
		{
			journals = new Journals();
			journals.setActivity_type("time_entries");
			journals.setJournable_type("TimeEntry");
			journals.setUser_id(hiveworkpackageDaoImpl.findByUniqueProperty(users.class, "id", spentTimeDTO.getUserId()));
			journals.setVersion((versionSize == 0) ? 1L:((versionSize.longValue())+1));
			journals.setCreated_at(new Second());
			
			
		}
		
		return journals;
	}

	public TimeEntryJournals convertToTimeEntryJournals(
			spentTimeDTO spentTimeDTO) throws ParseException {
		
		TimeEntryJournals entryJournals = null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
		if(spentTimeDTO!=null)
		{
			entryJournals = new TimeEntryJournals();
			entryJournals.setSpent_on(formatter.parse(spentTimeDTO.getSpentDate()));
			entryJournals.setProject_id(hiveworkpackageDaoImpl.findByUniqueProperty(projects.class, "id", spentTimeDTO.getProjectId()));
			entryJournals.setActivity_id(hiveworkpackageDaoImpl.findByUniqueProperty(Enumerations.class, "id", spentTimeDTO.getActivityId()));
			entryJournals.setUser_id(hiveworkpackageDaoImpl.findByUniqueProperty(users.class, "id", spentTimeDTO.getUserId()));
			entryJournals.setWork_package_id(hiveworkpackageDaoImpl.findByUniqueProperty(work_packages.class,"id",spentTimeDTO.getWorkPackageId()));
			entryJournals.setHours(spentTimeDTO.getSpentTime());
			entryJournals.setComments(spentTimeDTO.getComments());
			entryJournals.setTmonth(spentTimeDTO.getMonth());
			entryJournals.setTyear(spentTimeDTO.getYear());
			Calendar cal = Calendar.getInstance();
			cal.setTime(entryJournals.getSpent_on());
			int week = cal.get(Calendar.WEEK_OF_YEAR);
			entryJournals.setTweek(week);
		}
		
		return entryJournals;
	}
	
	
	
	public work_packages editWorkPackageToEntity(work_packagesDto workPackageDto,work_packages workPackage) throws ParseException {

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		if (workPackageDto != null) {
			workPackage.setSubject(workPackageDto.getSubject());
			workPackage.setDescription(workPackageDto.getDescription());
			if(workPackageDto.getFixed_version_id() != null) {
				workPackage.setFixed_version_id(hiveworkpackageDaoImpl.findByUniqueProperty(versions.class, "id", workPackageDto.getFixed_version_id()));
				}
			if(workPackageDto.getStart_date() != null) {
			workPackage.setStart_date(formatter.parse(workPackageDto.getStart_date()));
			}
			if(workPackageDto.getDue_date() != null){
			workPackage.setDue_date(formatter.parse(workPackageDto.getDue_date()));
			}
			workPackage.setEstimated_hours(workPackageDto.getEstimated_hours());
			workPackage.setRemaining_hours(workPackageDto.getRemaining_hours());
			workPackage.setDone_ratio(workPackageDto.getDone_ratio());
			workPackage.setUpdated_at(new Second());
			if(workPackageDto.getAssigneeName() != null && workPackageDto.getAssigneeName()!="") {
				String assignee ="";
				if(workPackageDto.getAssigneeName() == "") {
					workPackage.setAssigned_to_id(null);
				}
				else {
				if(workPackageDto.getAssigneeName().contains(" ")) {
					 assignee = workPackageDto.getAssigneeName().replace(" ", ".");
				}
				else {
					 assignee = workPackageDto.getAssigneeName();
				}
				Employee employee = dao.findByUniqueProperty(Employee.class, "userName", assignee);
				if(employee == null) {
					throw new InvalidUserException("please enter valid user name");
				}
				else {
					workPackage.setAssigned_to_id(hiveworkpackageDaoImpl.findByUniqueProperty(users.class, "login", assignee));
				}
				}
				
			}
			if(workPackageDto.getResponsibleName() != null) {
				String responsible ="";
				if(workPackageDto.getResponsibleName() == "") {
					workPackage.setResponsible_id(null);
				}
				else {
				if(workPackageDto.getResponsibleName().contains(" ")) {
					responsible = workPackageDto.getResponsibleName().replace(" ", ".");
				}
				else {
					responsible = workPackageDto.getResponsibleName();
				}
				Employee employee = dao.findByUniqueProperty(Employee.class, "userName", responsible);
				if(employee == null) {
					throw new InvalidUserException("please enter valid user name");
				}
				else {
					workPackage.setResponsible_id(hiveworkpackageDaoImpl.findByUniqueProperty(users.class, "login", responsible));
				}
				}
			
			}
			
			workPackage.setPriority_id(hiveworkpackageDaoImpl.findByUniqueProperty(Enumerations.class, "id", workPackageDto.getPriority_id()));
			
			workPackage.setStatus_id(hiveworkpackageDaoImpl.findByUniqueProperty(statuses.class, "id",workPackageDto.getStatus_id()));
			
			
		}

		return workPackage;

	}


}
