package com.raybiztech.hiveworkpackages.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pdfcrowd.Client;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.hiveworkpackages.Exception.HiveWorkPackageEception;
import com.raybiztech.hiveworkpackages.builder.work_packagesBuilder;
import com.raybiztech.hiveworkpackages.business.Enumerations;
import com.raybiztech.hiveworkpackages.business.Journals;
import com.raybiztech.hiveworkpackages.business.CustomOptions;
import com.raybiztech.hiveworkpackages.business.CustomValues;
import com.raybiztech.hiveworkpackages.business.CustomizableJournals;
import com.raybiztech.hiveworkpackages.business.TimeEntryJournals;
import com.raybiztech.hiveworkpackages.business.WorkPackageJournals;
import com.raybiztech.hiveworkpackages.business.TimeEntries;
import com.raybiztech.hiveworkpackages.business.projects;
import com.raybiztech.hiveworkpackages.business.statuses;
import com.raybiztech.hiveworkpackages.business.types;
import com.raybiztech.hiveworkpackages.business.users;
import com.raybiztech.hiveworkpackages.business.versions;
import com.raybiztech.hiveworkpackages.business.work_packages;
import com.raybiztech.hiveworkpackages.dao.hiveworkpackagesDao;
import com.raybiztech.hiveworkpackages.dto.ActivityDTO;
import com.raybiztech.hiveworkpackages.dto.spentTimeDTO;
import com.raybiztech.hiveworkpackages.dto.versionsDto;
import com.raybiztech.hiveworkpackages.dto.work_packagesDto;
import com.raybiztech.projectmanagement.builder.ProjectBuilder;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dao.AllocationDetailsDTO;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.rolefeature.business.User;
import com.sun.org.apache.xpath.internal.operations.Bool;

@Transactional
@Service("hiveworkpackageServiceImpl")
public class hiveworkpackageServiceImpl implements hiveworkpackagesService {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	hiveworkpackagesDao hiveworkpackageDaoImpl;

	@Autowired
	work_packagesBuilder workpackagesBuilder;

	@Autowired
	SessionFactory sessionFactory1;

	@Autowired
	DAO dao;

	@Autowired
	ProjectService projectService;
	
	@Autowired
	ProjectBuilder projectBuilder;

	@Autowired
	ResourceManagementDAO resourceManagementDAO;


	@Override
	public Map<String, Object> getHiveTasks() {

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		// Long employeeId = 1863l;

		// String employeeNmae = "jyothi.dondapati";

		/*
		 * hiveTasks = workpackagesBuilder.toDto(hiveworkpackageDaoImpl
		 * .getHivetasks(employeeId));
		 */

		Map<String, Object> detailsMap = (hiveworkpackageDaoImpl
				.getHivetasks(employee.getUsername()));

		Map<String, Object> map = new HashMap<String, Object>();

		List<work_packagesDto> dtoList = workpackagesBuilder
				.toDtoList((List) detailsMap.get("list"));

		map.put("list", dtoList);
		map.put("size", detailsMap.get("size"));

		return map;
	}

	@Override
	public List<types> getTaskTypes() {

		return hiveworkpackageDaoImpl.getTaskTypes();
	}

	@Override
	public List<Enumerations> getPriorityList() {

		return hiveworkpackageDaoImpl.getPriorityList();
	}


	@Transactional("hiveTransactionManager")
	@Override
	public void logTime(spentTimeDTO spentTimeDTO) throws ParseException {
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		
		Project project = dao.findBy(Project.class, spentTimeDTO.getProjectId());
		
		projects hiveProject = hiveworkpackageDaoImpl.findByUniqueProperty(projects.class, "name", project.getHiveProjectName());
		spentTimeDTO.setProjectId(hiveProject.getId());
		Long workpackgeId = spentTimeDTO.getWorkPackageId();

		/*Boolean authorizedUser = this.checkPermissionForUser(employee,
				workpackgeId, "Hierarchy Tasks List", "Individual Tasks List");
*/
		Permission totalList = dao.checkForPermission("Hive Tasks", employee);

		Boolean totalListView = false;

		if (totalList.getView()) {
			totalListView = true;
		}


			users user = hiveworkpackageDaoImpl.findByUniqueProperty(
					users.class, "login", employee.getUsername());

			spentTimeDTO.setUserId(user.getId());

			// converting to time entries
			TimeEntries timeEntries = workpackagesBuilder
					.convertLogtimeEntity(spentTimeDTO);

			// saving time entries
			Long timeEntriesId = (Long) hiveworkpackageDaoImpl
					.save(timeEntries);

			System.out.println("Time entres id" + timeEntriesId);

			// converting to journals
			Journals journals = workpackagesBuilder
					.convertTimeEntriesToJournals(spentTimeDTO,timeEntriesId);

			journals.setJournable_id(timeEntriesId);

			// saving journals
			Long journalId = (Long) hiveworkpackageDaoImpl.save(journals);

			System.out.println("journal id" + journalId);

			// converting time entry journals
			TimeEntryJournals entryJournals = workpackagesBuilder
					.convertToTimeEntryJournals(spentTimeDTO);

			entryJournals.setJournal_id(hiveworkpackageDaoImpl
					.findByUniqueProperty(Journals.class, "id", journalId));

			// save time entry journals
			hiveworkpackageDaoImpl.save(entryJournals);
		

	}

	@Override
	public List<ActivityDTO> getactivitytypes() {

		List<ActivityDTO> activityList = new ArrayList<ActivityDTO>();

		List<Enumerations> activityTypes = hiveworkpackageDaoImpl
				.getactivitytypes();

		ActivityDTO activityDTO = null;
		for (Enumerations enumerations : activityTypes) {
			activityDTO = new ActivityDTO();
			activityDTO.setId(enumerations.getId());
			activityDTO.setName(enumerations.getName());
			activityList.add(activityDTO);
		}

		return activityList;
	}

	// creating a new workPackage in openproject database
	// where data is inserted in multiple table based on workPackage type
	@Transactional("hiveTransactionManager")
	@Override
	public void createNewWorkPackage(work_packagesDto workPackageDto)
			throws ParseException {

		work_packages workPackage = workpackagesBuilder
				.toEntity(workPackageDto);

		Long workPkgId = (Long) hiveworkpackageDaoImpl.save(workPackage);

		work_packages createdPkg = hiveworkpackageDaoImpl.findByUniqueProperty(
				work_packages.class, "id", workPkgId);
		createdPkg.setRoot_id(workPkgId);
		hiveworkpackageDaoImpl.update(createdPkg);

		// saving in journals table after saving in workPackages table
		Long journalId = this.saveJournals(createdPkg);

		// saving in workPackagejournals table after saving in journals table
		Journals journal = hiveworkpackageDaoImpl.findByUniqueProperty(
				Journals.class, "id", journalId);
		this.saveWorkPacakgeJournals(journal, createdPkg);

		// saving in customizableJournals table after saving in Journals table
		// for workPackage of type 'Bug' and 'TestCase'
		if (journalId != null) {
			if (workPackageDto.getSeverity() != null) {
				saveCustomizableJournals(journal, 1L,
						workPackageDto.getSeverity().toString());
				saveCustomValues(workPkgId, 1L, workPackageDto.getSeverity().toString());
			}
			if (workPackageDto.getTestCaseType() != null) {
				saveCustomizableJournals(journal, 21L,
						workPackageDto.getTestCaseType().toString());
				saveCustomValues(workPkgId, 21L, workPackageDto.getTestCaseType().toString());
			}
			if (workPackageDto.getTestCaseCondition() != null
					&& workPackageDto.getTestCaseCondition() != "") {
				saveCustomizableJournals(journal, 22L,
						workPackageDto.getTestCaseCondition());
				saveCustomValues(workPkgId, 22L, workPackageDto.getTestCaseCondition().toString());
			}
			if (workPackageDto.getTestCaseExceptedResult() != null
					&& workPackageDto.getTestCaseExceptedResult() != "") {
				saveCustomizableJournals(journal, 23L,
						workPackageDto.getTestCaseExceptedResult());
				saveCustomValues(workPkgId, 23L, workPackageDto.getTestCaseExceptedResult().toString());
			}
			if (workPackageDto.getTestCaseActualResult() != null
					&& workPackageDto.getTestCaseActualResult() != "") {
				saveCustomizableJournals(journal, 24L,
						workPackageDto.getTestCaseActualResult());
				saveCustomValues(workPkgId, 24L, workPackageDto.getTestCaseActualResult().toString());
			}
			if (workPackageDto.getTestCaseAutomated() != null) {
				saveCustomizableJournals(journal, 25L,
						workPackageDto.getTestCaseAutomated().toString());
				saveCustomValues(workPkgId, 25L, workPackageDto.getTestCaseAutomated().toString());
			}
			if (workPackageDto.getTestCaseExecutionCycle() != null) {
				saveCustomizableJournals(journal, 26L,
						workPackageDto.getTestCaseExecutionCycle().toString());
				saveCustomValues(workPkgId, 26L, workPackageDto.getTestCaseExecutionCycle().toString());
			}
		}

	}

	@Override
	public List<String> getHiveTasksClients(Integer startIndex, Integer endIndex) {

		List<AllocationDetailsDTO> allocationDetailsDTOs = new ArrayList<AllocationDetailsDTO>();

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Boolean totalListFalg = false;
		Boolean individualListFalg = false;
		Boolean hierarchyListFlag = false;

		Permission totalList = dao.checkForPermission("Hive Tasks", employee);
		Permission individual = dao.checkForPermission("Individual Tasks List",
				employee);
		Permission Hierarchy = dao.checkForPermission("Hierarchy Tasks List",
				employee);

		if (totalList.getView() && !individual.getView()
				&& !Hierarchy.getView())
			totalListFalg = true;
		else if (individual.getView() && totalList.getView()
				&& !Hierarchy.getView())
			individualListFalg = true;
		else if (Hierarchy.getView() && !individual.getView()
				&& totalList.getView())
			hierarchyListFlag = true;

		List<String> clientsData = new ArrayList<String>();

		if (totalListFalg) {
			System.out.println("intotalListFlag");
			clientsData = (hiveworkpackageDaoImpl.getHiveTasksClientsForAdmin(startIndex, endIndex));
		} else if (hierarchyListFlag) {

			System.out.println("hierarchyListFlag");
			/*List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());
			managerIds.add(employee.getEmployeeId());

			clientsData = (hiveworkpackageDaoImpl
					.getHiveTasksClientsForHierarchy(managerIds,
							employee.getEmployeeId()));*/

			clientsData = (hiveworkpackageDaoImpl
					.getHiveTasksClientsForEmployee(employee.getEmployeeId(),startIndex, endIndex));
			
			
		} else if (individualListFalg) {

			System.out.println("individualListFalg");
			clientsData = (hiveworkpackageDaoImpl
					.getHiveTasksClientsForEmployee(employee.getEmployeeId(),startIndex, endIndex));
		}

		/*
		 * List<String> clientsData = (hiveworkpackageDaoImpl
		 * .getHiveTasksClients(employee.getEmployeeId()));
		 */

		System.out.println("allocation details" + clientsData.size());

		return clientsData;
	}

	@Override
	public List<work_packagesDto> getSprintTasksList(Long id) {

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		
		Long loggedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		String loginUserName = employee.getUsername();

		Boolean totalListFalg = false;
		Boolean individualListFalg = false;
		Boolean hierarchyListFlag = false;

		Permission totalList = dao.checkForPermission("Hive Tasks", employee);
		Permission individual = dao.checkForPermission("Individual Tasks List",
				employee);
		Permission Hierarchy = dao.checkForPermission("Hierarchy Tasks List",
				employee);

		if (totalList.getView() && !individual.getView()
				&& !Hierarchy.getView())
			totalListFalg = true;
		else if (individual.getView() && totalList.getView()
				&& !Hierarchy.getView())
			individualListFalg = true;
		else if (Hierarchy.getView() && !individual.getView()
				&& totalList.getView())
			hierarchyListFlag = true;

		List<work_packages> sprinttasksList = null;

		if (totalListFalg) {
			System.out.println("intotalListFlag");
			sprinttasksList = hiveworkpackageDaoImpl
					.getSprintTasksListForAdmin(id);
		} else if (hierarchyListFlag) {

			System.out.println("version id" +id);
			System.out.println("loggedin employee id" +loggedInEmpId);

			List<String> projectPeople = new ArrayList<String>();
			
			versions version =hiveworkpackageDaoImpl.findByUniqueProperty(versions.class, "id", id);
			
			
			/*work_packages work_packages = hiveworkpackageDaoImpl
					.findByUniqueProperty(work_packages.class,
							"fixed_version_id", id);*/
			
			System.out.println("workpackage id"+version.getId());
			System.out.println("hiveProjectName"+version.getProject_id().getName());

			Project project = dao.findByUniqueProperty(Project.class,
					"hiveProjectName", version.getProject_id().getName());
			
			System.out.println("Project Id "+project.getId());
			
			System.out.println("empId"+employee.getEmployeeId());
			
			System.out.println("emp"+employee);
			
			
			List<Project> allocationDetialsMap =new ArrayList<Project>();
			Set<AllocationDetails> allocationDetils = new HashSet<AllocationDetails>();
			//allocationDetialsMap=this.mangerUnderProjectPeoples(employee.getEmployeeId());
			
			
			List<AllocationDetails> allocationDetailsList = resourceManagementDAO
					.getProjectDetails(project.getId());
			
			System.out.println("alloction details map"+allocationDetialsMap);
			
			/* allocationDetils = allocationDetialsMap
					.get(project.getId());*/
			 
			 System.out.println("allocation dertaiuls"+allocationDetils.size());

			for (AllocationDetails details : allocationDetailsList) {
				projectPeople.add(details.getEmployee().getUsername());
				System.out.println("people"+projectPeople);
			}

			sprinttasksList = hiveworkpackageDaoImpl
					.getSprintTasksListForHirerarchy(projectPeople, id,
							loginUserName);

		} else if (individualListFalg) {

			System.out.println("individualListFalg");
			sprinttasksList = hiveworkpackageDaoImpl
					.getSprintTasksListForEmployee(id, loginUserName);
		}

		// List<work_packages> sprinttasksList =
		// hiveworkpackageDaoImpl.getSprintTasksList(id,loginUserName);

		List<work_packagesDto> dtoList = workpackagesBuilder
				.toDtoList(sprinttasksList);
		System.out.println("dto size" + dtoList.size());

		return dtoList;
	}

	@Override
	public List<work_packagesDto> getProjecttasksList(String hiveProjectName) {

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		String loginUserName = employee.getUsername();

		Boolean totalListFalg = false;
		Boolean individualListFalg = false;
		Boolean hierarchyListFlag = false;

		Permission totalList = dao.checkForPermission("Hive Tasks", employee);
		Permission individual = dao.checkForPermission("Individual Tasks List",
				employee);
		Permission Hierarchy = dao.checkForPermission("Hierarchy Tasks List",
				employee);

		if (totalList.getView() && !individual.getView()
				&& !Hierarchy.getView())
			totalListFalg = true;
		else if (individual.getView() && totalList.getView()
				&& !Hierarchy.getView())
			individualListFalg = true;
		else if (Hierarchy.getView() && !individual.getView()
				&& totalList.getView())
			hierarchyListFlag = true;

		List<work_packages> projectTasksList = null;

		if (totalListFalg) {
			System.out.println("intotalListFlag");
			projectTasksList = hiveworkpackageDaoImpl
					.getPrrojectTasksListForAdmin(hiveProjectName);
		} else if (hierarchyListFlag) {
			
			List<String> projectPeople = new ArrayList<String>();
			System.out.println("hierarchyListFlag");
			
			Project project = dao.findByUniqueProperty(Project.class,
					"hiveProjectName", hiveProjectName);

	
			List<AllocationDetails> allocationDetailsList = resourceManagementDAO
					.getProjectDetails(project.getId());


			for (AllocationDetails details : allocationDetailsList) {
				projectPeople.add(details.getEmployee().getUsername());
				System.out.println("people"+projectPeople);
			}


			projectTasksList = hiveworkpackageDaoImpl
					.getPrrojectTasksListForHierarchy(projectPeople,
							hiveProjectName, loginUserName);

		} else if (individualListFalg) {

			System.out.println("individualListFalg");
			projectTasksList = hiveworkpackageDaoImpl
					.getProjectTasksListForEmployee(hiveProjectName,
							loginUserName);
		}

		/*
		 * List<work_packages> projectTasksList = hiveworkpackageDaoImpl
		 * .getPrrojectTasksList(hiveProjectName, loginUserName);
		 */

		List<work_packagesDto> projectTaskslist = workpackagesBuilder
				.toDtoList(projectTasksList);

		return projectTaskslist;
	}

	@Override
	public List<ProjectDTO> getHiveProjectsList(String client) {

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		
		com.raybiztech.projectmanagement.business.Client client2 = dao.findByUniqueProperty(com.raybiztech.projectmanagement.business.Client.class, "name", client);

		Boolean totalListFalg = false;
		Boolean individualListFalg = false;
		Boolean hierarchyListFlag = false;

		Permission totalList = dao.checkForPermission("Hive Tasks", employee);
		Permission individual = dao.checkForPermission("Individual Tasks List",
				employee);
		Permission Hierarchy = dao.checkForPermission("Hierarchy Tasks List",
				employee);

		if (totalList.getView() && !individual.getView()
				&& !Hierarchy.getView())
			totalListFalg = true;
		else if (individual.getView() && totalList.getView()
				&& !Hierarchy.getView())
			individualListFalg = true;
		else if (Hierarchy.getView() && !individual.getView()
				&& totalList.getView())
			hierarchyListFlag = true;

		List<String> projectsList = null;

		if (totalListFalg) {
			System.out.println("intotalListFlag");
			projectsList = (hiveworkpackageDaoImpl
					.getHiveProjectsListForAdmin(client2.getId()));
			;
		} else if (hierarchyListFlag) {

			System.out.println("hierarchyListFlag");
			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());
			managerIds.add(employee.getEmployeeId());

			projectsList = (hiveworkpackageDaoImpl
					.getHiveProjectsListForHierarchy(managerIds,
							employee.getEmployeeId(), client2.getId()));


		} else if (individualListFalg) {

			System.out.println("individualListFalg");
			projectsList = (hiveworkpackageDaoImpl
					.getHiveProjectsListForEmployee(employee.getEmployeeId(),
							client2.getId()));
		}

		/*
		 * List<AllocationDetails> projectsList = (hiveworkpackageDaoImpl
		 * .getHiveProjectsList(employee.getEmployeeId(),client));
		 */

		List<ProjectDTO> projectDTOs = new ArrayList<ProjectDTO>();
		ProjectDTO projectDTO =  null;
		for (String allocationDetails : projectsList) {

			Project project = dao.findByUniqueProperty(Project.class, "projectName", allocationDetails);
			if (allocationDetails!= null && allocationDetails!=" ") {
				 projectDTO = new ProjectDTO();
				projectDTO.setProjectName(project.getProjectName());
				projectDTO.setHiveProjectName(project.getHiveProjectName());
			}
			projectDTOs.add(projectDTO);

		}

		return projectDTOs;
	}

	@Override
	public List<CustomOptions> getCustomValues() {

		return hiveworkpackageDaoImpl.getCustomValues();
	}

	// method to save customizable journals
	public void saveCustomizableJournals(Journals journal, Long fieldId,
			String value) {

		CustomizableJournals customJournal = new CustomizableJournals();
		customJournal.setJournal_id(journal);
		customJournal.setCustom_field_id(fieldId);
		customJournal.setValue(value);
		hiveworkpackageDaoImpl.save(customJournal);

	}

	@Override
	public work_packagesDto getWorkapackageData(String workpackageId)
			throws ParseException {
		workpackageId.replace("~", "&");
		byte[] value = Base64.decodeBase64(workpackageId);
		String decodeValue = new String(value);
		Long wKpId = 0L;
		wKpId = Long.parseLong(decodeValue);

		work_packages work_packages = hiveworkpackageDaoImpl
				.findByUniqueProperty(work_packages.class, "id", wKpId);
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Permission totalList = dao.checkForPermission("Hive Tasks", employee);
		Boolean totalListView = false;
		if (totalList.getView()) {
			totalListView = true;
		}

		Boolean authorizedUser = this.checkPermissionForUser(employee, wKpId,
				"Hierarchy Tasks List", "Individual Tasks List");

		work_packagesDto packagesDto = null;

		if (authorizedUser || totalListView) {
			packagesDto = new work_packagesDto();
			packagesDto.setId(work_packages.getId());
			packagesDto.setProject_id(work_packages.getProject_id().getId());
			if(work_packages.getFixed_version_id() != null)
			{
				packagesDto.setFixed_version_id(work_packages.getFixed_version_id()
						.getId());
			}
			
		} else {
			throw new HiveWorkPackageEception("UnAuthorized user");
		}

	    if(authorizedUser || totalListView)
	    {
	         /*packagesDto = new work_packagesDto();
	         packagesDto.setId(work_packages.getId());
	         packagesDto.setProject_id(work_packages.getProject_id().getId());
	         packagesDto.setFixed_version_id(work_packages.getFixed_version_id().getId());*/
	    	packagesDto = workpackagesBuilder.toDto(work_packages);
	    }
	    else
	    {
	        throw new HiveWorkPackageEception("UnAuthorized user");
	    }

		return packagesDto;
	}

	public List<versionsDto> getVersionsList(String projectName) {
		List<versionsDto> versionsDTOList = new ArrayList<versionsDto>();
		List<versions> versionsList = hiveworkpackageDaoImpl.getVersionsList(projectName);
		versionsDto versionDTO = null;
		if (versionsList != null) {
			for (versions version : versionsList) {
				versionDTO = new versionsDto();
				versionDTO.setId(version.getId());
				versionDTO.setName(version.getName());
				versionsDTOList.add(versionDTO);
			}

		}
		return versionsDTOList;

	}
	
	public Boolean checkPermissionForUser(Employee employee,
			Long workpackageId, String hierchypermission, String individualTask) {

		Permission individual = dao
				.checkForPermission(individualTask, employee);
		Permission Hierarchy = dao.checkForPermission(hierchypermission,
				employee);

		Boolean authorizedUser = false;
		work_packages work_packages = hiveworkpackageDaoImpl
				.findByUniqueProperty(work_packages.class, "id", workpackageId);
		
		users userId = work_packages.getAssigned_to_id();
		List<String> projectPeople = new ArrayList<String>();

		if (Hierarchy.getView()) {
			Project project = dao.findByUniqueProperty(Project.class,
					"hiveProjectName", work_packages.getProject_id().getName());

			List<AllocationDetails> allocationDetailsList = resourceManagementDAO
					.getProjectDetails(project.getId());
			for (AllocationDetails details : allocationDetailsList) {
				projectPeople.add(details.getEmployee().getUsername());
			}

		}

		
		if(individual.getView())
		{
			Project project = dao.findByUniqueProperty(Project.class,"hiveProjectName" ,work_packages.getProject_id().getName());
			List<AllocationDetails> allocationDetails=resourceManagementDAO
					.getProjectDetails(project.getId());
			for(AllocationDetails allocationDetails2 : allocationDetails)
			{
				projectPeople.add(allocationDetails2.getEmployee().getUserName());
			}
			
		}

		if (projectPeople.contains(userId.getLogin())) {
			authorizedUser = true;
		}

		return authorizedUser;
	}

	public List<Project> mangerUnderProjectPeoples(
			Long managerId) {
		List<Project> projectPeoples = dao.mangerUnderProjectPeoples(managerId);
		
		
		
		
	/*	Map<Long, Set<AllocationDetails>> projectByPeople = new HashMap<Long, Set<AllocationDetails>>();
		for (AllocationDetails alloc : projectPeoples) {
			//projectIds.add(p.getId());
			///System.out.println("IN for loop "+p.getAllocationDetails().size());
			System.out.println("alloc Size**********"+projectPeoples.size());
			projectByPeople.put(alloc.getProject().getId(),(Set<AllocationDetails>) alloc);
		}
		
		System.out.println("project peoples"+projectPeoples.size());*/
		
		/*List<Long> projectIds = new ArrayList<Long>();
		
		
		
		
		List<AllocationDetails> allocationDetails = dao.getAllocationDetialsForProjects(projectIds);
		List<ProjectDTO> projectAllocation = new ArrayList<ProjectDTO>();
		for(AllocationDetails allocationDetails2 : allocationDetails)
		{
			ProjectDTO dto = new ProjectDTO();
			dto.setEmployeeId(allocationDetails2.getEmployee().getEmployeeId());
			projectAllocation.add(dto);
		}
		System.out.println("project people map"+projectByPeople.size());*/
		return projectPeoples;

	}
	

	@Override
	public List<ProjectDTO> getProjectsAssignedToEmployee() {
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		
		List<ProjectDTO> projectList = projectBuilder.createProjectActiveListcountForEmployee(
				hiveworkpackageDaoImpl.getProjectsAssignedToEmployee(employee.getEmployeeId()));
		
		return projectList;
	}

	@Override
	public List<statuses> getworkPkgStatuses() {
		return hiveworkpackageDaoImpl.getworkPkgStatuses();
		 
	}
	
	
	//updating a work package
	@Transactional("hiveTransactionManager")
	@Override
	public void updateWorkPackage(work_packagesDto workPackageDto) throws ParseException {
		
		work_packages oldworkPackage = hiveworkpackageDaoImpl.findByUniqueProperty(work_packages.class, "id", workPackageDto.getId());
		
		work_packages workPackage = workpackagesBuilder
				.editWorkPackageToEntity(workPackageDto,oldworkPackage);
		
		hiveworkpackageDaoImpl.update(workPackage);

		work_packages updatedPkg = hiveworkpackageDaoImpl.findByUniqueProperty(
				work_packages.class, "id", oldworkPackage.getId());
		
		Long journalId = this.saveJournals(updatedPkg);
		
		Journals journal = hiveworkpackageDaoImpl.findByUniqueProperty(
				Journals.class, "id", journalId);
		
		this.saveWorkPacakgeJournals(journal, updatedPkg);
		
		List<CustomValues> customvalues = (List<CustomValues>) hiveworkpackageDaoImpl.getCustomizedValues(oldworkPackage.getId());
		for(CustomValues value: customvalues) {
			if((Long.valueOf(value.getCustomFieldId()) == 1L) && workPackageDto.getSeverity() != null) {
				value.setValue(workPackageDto.getSeverity().toString());
			}
			if ((Long.valueOf(value.getCustomFieldId()) == 21L) && workPackageDto.getTestCaseType() != null) {
				value.setValue(workPackageDto.getTestCaseType().toString());
			}
			if ((Long.valueOf(value.getCustomFieldId()) == 22L) && workPackageDto.getTestCaseCondition() != null
					&& workPackageDto.getTestCaseCondition() != "") {
				value.setValue(workPackageDto.getTestCaseCondition().toString());
			}
			if ((Long.valueOf(value.getCustomFieldId()) == 23L) && workPackageDto.getTestCaseExceptedResult() != null
					&& workPackageDto.getTestCaseExceptedResult() != "") {
				value.setValue(workPackageDto.getTestCaseExceptedResult().toString());
			}
			if ((Long.valueOf(value.getCustomFieldId()) == 24L) && workPackageDto.getTestCaseActualResult() != null
					&& workPackageDto.getTestCaseActualResult() != "") {
				value.setValue(workPackageDto.getTestCaseActualResult().toString());
			}
			if ((Long.valueOf(value.getCustomFieldId()) == 25L) && workPackageDto.getTestCaseAutomated() != null) {
				value.setValue(workPackageDto.getTestCaseAutomated().toString());
			}
			if ((Long.valueOf(value.getCustomFieldId()) == 26L) && workPackageDto.getTestCaseExecutionCycle() != null) {
				value.setValue(workPackageDto.getTestCaseExecutionCycle().toString());
			}
			hiveworkpackageDaoImpl.update(value);
		}
		if (journalId != null) {
			if (workPackageDto.getSeverity() != null) {
				saveCustomizableJournals(journal, 1L,
						workPackageDto.getSeverity().toString());
			}
			if (workPackageDto.getTestCaseType() != null) {
				saveCustomizableJournals(journal, 21L,
						workPackageDto.getTestCaseType().toString());
			}
			if (workPackageDto.getTestCaseCondition() != null
					&& workPackageDto.getTestCaseCondition() != "") {
				saveCustomizableJournals(journal, 22L,
						workPackageDto.getTestCaseCondition());
			}
			if (workPackageDto.getTestCaseExceptedResult() != null
					&& workPackageDto.getTestCaseExceptedResult() != "") {
				saveCustomizableJournals(journal, 23L,
						workPackageDto.getTestCaseExceptedResult());
			}
			if (workPackageDto.getTestCaseActualResult() != null
					&& workPackageDto.getTestCaseActualResult() != "") {
				saveCustomizableJournals(journal, 24L,
						workPackageDto.getTestCaseActualResult());
			}
			if (workPackageDto.getTestCaseAutomated() != null) {
				saveCustomizableJournals(journal, 25L,
						workPackageDto.getTestCaseAutomated().toString());
			}
			if (workPackageDto.getTestCaseExecutionCycle() != null) {
				saveCustomizableJournals(journal, 26L,
						workPackageDto.getTestCaseExecutionCycle().toString());
			}
		}

		
	}
	
	// method to  journals
		public Long saveJournals(work_packages workPackage) {

			Long journalId = 0L;
			if (workPackage != null) {
				Employee loggedIn = (Employee) securityUtils
						.getLoggedEmployeeDetailsSecurityContextHolder()
						.get("employee");
				
				Map<String, Object> journalMap = hiveworkpackageDaoImpl.getWorkPackageJournal(workPackage.getId());
				Integer versionSize = (Integer) journalMap.get("listSize");
				System.out.println("versionSize:"+versionSize);
				Journals journal = new Journals();
				journal.setJournable_id(workPackage.getId());
				journal.setActivity_type("work_packages");
				journal.setUser_id(hiveworkpackageDaoImpl.findByUniqueProperty(users.class, "login", loggedIn.getUserName()));
				journal.setVersion((versionSize == 0) ? 1L:((versionSize.longValue())+1));
				journal.setCreated_at(new Second());
				journal.setJournable_type("WorkPackage");
				journalId = (Long) hiveworkpackageDaoImpl.save(journal);
		}
			return journalId;
		}

		
	// method to save in workPackage journals
		public void saveWorkPacakgeJournals(Journals journal ,work_packages workPackage) {
			if (journal != null) {
				WorkPackageJournals workPkgJournal = new WorkPackageJournals();
				workPkgJournal.setJournal_id(journal);
				workPkgJournal.setType_id(workPackage.getType_id());
				workPkgJournal.setProject_id(workPackage.getProject_id());
				workPkgJournal.setFixed_version_id(workPackage.getFixed_version_id());
				workPkgJournal.setSubject(workPackage.getSubject());
				workPkgJournal.setDescription(workPackage.getDescription());
				workPkgJournal.setStart_date(workPackage.getStart_date());
				workPkgJournal.setDue_date(workPackage.getDue_date());
				workPkgJournal.setStatus_id(workPackage.getStatus_id());
				workPkgJournal.setAssigned_to_id(workPackage.getAssigned_to_id());
				workPkgJournal.setResponsible_id(workPackage.getResponsible_id());
				workPkgJournal.setPriority_id(workPackage.getPriority_id());
				workPkgJournal.setAuthor_id(workPackage.getAuthor_id());
				workPkgJournal.setEstimated_hours(workPackage.getEstimated_hours());
				workPkgJournal.setRemaining_hours(workPackage.getRemaining_hours());
				workPkgJournal.setDone_ratio(workPackage.getDone_ratio());
				hiveworkpackageDaoImpl.save(workPkgJournal);
					}
					
				}	
		
		
		//saving custom values
		public void saveCustomValues(Long wpId ,Long fieldId,String value) {
			if(wpId !=null) {
			CustomValues customValues = new CustomValues();
			customValues.setCustomizedType("WorkPackage");
			customValues.setCustomizedId(wpId);
			customValues.setCustomFieldId(fieldId);
			customValues.setValue(value);
			hiveworkpackageDaoImpl.save(customValues);
			
			}
		}
}
