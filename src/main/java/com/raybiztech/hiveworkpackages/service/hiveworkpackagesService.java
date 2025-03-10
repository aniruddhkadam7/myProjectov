package com.raybiztech.hiveworkpackages.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.raybiztech.hiveworkpackages.business.Enumerations;
import com.raybiztech.hiveworkpackages.business.WorkPackageJournals;
import com.raybiztech.hiveworkpackages.business.statuses;
import com.raybiztech.hiveworkpackages.business.CustomOptions;
import com.raybiztech.hiveworkpackages.business.types;
import com.raybiztech.hiveworkpackages.business.versions;
import com.raybiztech.hiveworkpackages.dto.ActivityDTO;
import com.raybiztech.hiveworkpackages.dto.spentTimeDTO;
import com.raybiztech.hiveworkpackages.dto.versionsDto;
import com.raybiztech.hiveworkpackages.dto.work_packagesDto;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dto.ProjectDTO;

public interface hiveworkpackagesService {
	
	Map<String, Object> getHiveTasks();
	
	List<types> getTaskTypes();
	
	List<Enumerations> getPriorityList();
	
	List<String> getHiveTasksClients(Integer startIndex,Integer endIndex);
	
	List<work_packagesDto> getSprintTasksList(Long id);
	
	void logTime(spentTimeDTO spentTimeDTO) throws ParseException;

	List<ActivityDTO> getactivitytypes();
	
	void createNewWorkPackage(work_packagesDto workPackageDto) throws ParseException;
	
	
	List<work_packagesDto> getProjecttasksList(String hiveProjectName);
	
	List<ProjectDTO> getHiveProjectsList(String client);
	List<CustomOptions> getCustomValues();
	
	List<versionsDto> getVersionsList(String projectName);

	work_packagesDto getWorkapackageData(String workpackageId) throws ParseException;
	
	List<ProjectDTO> getProjectsAssignedToEmployee();
	
	List<statuses> getworkPkgStatuses();
	
	void updateWorkPackage(work_packagesDto workPackageDto) throws ParseException;

}
