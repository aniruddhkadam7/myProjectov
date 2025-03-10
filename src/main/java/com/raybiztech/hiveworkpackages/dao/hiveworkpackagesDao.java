package com.raybiztech.hiveworkpackages.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.raybiztech.hiveworkpackages.business.Enumerations;
import com.raybiztech.hiveworkpackages.business.Journals;
import com.raybiztech.hiveworkpackages.business.CustomOptions;
import com.raybiztech.hiveworkpackages.business.CustomValues;
import com.raybiztech.hiveworkpackages.business.statuses;
import com.raybiztech.hiveworkpackages.business.TimeEntries;
import com.raybiztech.hiveworkpackages.business.types;
import com.raybiztech.hiveworkpackages.business.versions;
import com.raybiztech.hiveworkpackages.business.work_packages;
import com.raybiztech.hiveworkpackages.dto.spentTimeDTO;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dao.AllocationDetailsDTO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.appraisals.dao.DAO;

public interface hiveworkpackagesDao extends DAO {

	<T extends Serializable> T findByUniqueProperty(Class<T> clazz,
			String propertyName, Serializable name);

	<T extends Serializable> Serializable save(T object);

	<T extends Serializable> void update(T object);

	Map<String, Object> getHivetasks(String employeeNmae);

	List<types> getTaskTypes();

	List<Enumerations> getPriorityList();

	List<String> getHiveTasksClientsForEmployee(Long employeeId,Integer startIndex, Integer endIndex);

	List<work_packages> getSprintTasksListForEmployee(Long id, String loginUserName);

	List<Enumerations> getactivitytypes();

	List<statuses> getStatusList();

	List<work_packages> getProjectTasksListForEmployee(String hiveProjectName,
			String loginUserName);

	List<String> getHiveProjectsListForEmployee(Long employeeId,
			Long client);

	List<CustomOptions> getCustomValues();

	List<versions> getVersionsList(String projectName);

	List<String> getHiveTasksClientsForAdmin(Integer startIndex, Integer endIndex);

	List<String> getHiveTasksClientsForHierarchy(List<Long> managerId,
			Long employeeId);

	List<String> getHiveProjectsListForAdmin(Long client);

	List<String> getHiveProjectsListForHierarchy(
			List<Long> managerIds, Long employeeId, Long client);
	
	List<work_packages> getSprintTasksListForAdmin(Long id);
	
	List<work_packages> getSprintTasksListForHirerarchy(List<String> managerUserNames,Long id,String loginUserName);

	
	List<work_packages> getPrrojectTasksListForAdmin(String hiveProjectName
		);
	
	List<work_packages> getPrrojectTasksListForHierarchy(List<String> managerUserNames,String hiveProjectName,
			String loginUserName);
	
	Map<String, Object> getWorkPackageJournal(Long id);
	
	List<CustomValues> getCustomizedValues(Long id);
	
	List<AllocationDetailsDTO> getProjectsAssignedToEmployee(Long empId);
	
	List<statuses> getworkPkgStatuses();
}
