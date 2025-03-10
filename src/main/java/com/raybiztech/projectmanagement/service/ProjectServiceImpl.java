package com.raybiztech.projectmanagement.service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.pattern.LogEvent;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.HibernateException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.alerts.builder.AlertBuilder;
import com.raybiztech.appraisals.alerts.business.Alert;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.biometric.business.BioAttendance;
import com.raybiztech.commons.Percentage;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import com.raybiztech.hiveProject.business.HiveDetails;
import com.raybiztech.mailtemplates.util.ProjectManagementMailConfiguration;
import com.raybiztech.projectmanagement.builder.AllocationDetailsBuilder;
import com.raybiztech.projectmanagement.builder.AuditBuilder;
import com.raybiztech.projectmanagement.builder.ChangeRequestBuilder;
import com.raybiztech.projectmanagement.builder.MileStoneAuditBuilder;
import com.raybiztech.projectmanagement.builder.MilestonePeopleBuilder;
import com.raybiztech.projectmanagement.builder.ProjectAuditBuilder;
import com.raybiztech.projectmanagement.builder.ProjectBuilder;
import com.raybiztech.projectmanagement.builder.ProjectNumbersBuilder;
import com.raybiztech.projectmanagement.builder.ProjectRequestBuilder;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.AllocationEffort;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Country;
import com.raybiztech.projectmanagement.business.Domain;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.MilestoneAudit;
import com.raybiztech.projectmanagement.business.MilestonePeople;
import com.raybiztech.projectmanagement.business.Platform;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectCheckList;
import com.raybiztech.projectmanagement.business.ProjectInitiationChecklist;
import com.raybiztech.projectmanagement.business.ProjectNumbers;
import com.raybiztech.projectmanagement.business.ProjectProposals;
import com.raybiztech.projectmanagement.business.ProjectRequest;
import com.raybiztech.projectmanagement.business.ProjectRequestMail;
import com.raybiztech.projectmanagement.business.ProjectRequestMilestone;
import com.raybiztech.projectmanagement.business.ProjectStatus;
import com.raybiztech.projectmanagement.business.StatusReport;
import com.raybiztech.projectmanagement.dao.AllocationDetailsDTO;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.dto.ChangeRequestDTO;
import com.raybiztech.projectmanagement.dto.ClientDTO;
import com.raybiztech.projectmanagement.dto.CountryDTO;
import com.raybiztech.projectmanagement.dto.ManagerDTO;
import com.raybiztech.projectmanagement.dto.MileStoneAuditDTO;
import com.raybiztech.projectmanagement.dto.MilestoneDTO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.dto.ProjectInitiationChecklistDTO;
import com.raybiztech.projectmanagement.dto.ProjectNameDTO;
import com.raybiztech.projectmanagement.dto.ProjectNumbersDTO;
import com.raybiztech.projectmanagement.dto.ProjectProposalsDTO;
import com.raybiztech.projectmanagement.dto.ProjectRequestAuditDTO;
import com.raybiztech.projectmanagement.dto.ProjectRequestDTO;
import com.raybiztech.projectmanagement.dto.ProjectRequestMailDTO;
import com.raybiztech.projectmanagement.dto.ProjectRequestMilestoneDTO;
import com.raybiztech.projectmanagement.dto.ReportDTO;
import com.raybiztech.projectmanagement.dto.StatusReportDTO;
import com.raybiztech.projectmanagement.exception.NoCheckList;
import com.raybiztech.projectmanagement.exception.ProjectRequestIdAlreadyExistsException;
import com.raybiztech.projectmanagement.exceptions.DuplicateClientCodeException;
import com.raybiztech.projectmanagement.exceptions.DuplicateClientException;
import com.raybiztech.projectmanagement.exceptions.DuplicateClientOrganizationException;
import com.raybiztech.projectmanagement.exceptions.DuplicateProjectException;
import com.raybiztech.projectmanagement.exceptions.HiveProjectNameNotExistException;
import com.raybiztech.projectmanagement.exceptions.ProjectNameNotExistException;
import com.raybiztech.projectmanagement.exceptions.ProjectNumbersUpdationException;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.dao.InvoiceDao;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceQueryDTO;
import com.raybiztech.projectmanagement.invoice.lookup.Currency;
import com.raybiztech.projectmanagement.invoice.quartz.InvoiceReminder;
import com.raybiztech.projectmanagement.invoice.service.InvoiceService;
import com.raybiztech.projectmanagement.invoice.utility.ProjectNotification;
import com.raybiztech.recruitment.business.Holidays;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.rolefeature.business.Permission;

@Service("projectService")
@Transactional
public class ProjectServiceImpl implements ProjectService, Cloneable {

	@Autowired
	ProjectBuilder projectBuilder;
	@Autowired
	ResourceManagementDAO resourceManagementDAO;
	@Autowired
	InvoiceDao invoiceDao;
	@Autowired
	DAO dao;
	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	InvoiceReminder invoiceReminder;
	@Autowired
	AlertBuilder alertBuilder;
	@Autowired
	ProjectNotification projectNotification;
	@Autowired
	MileStoneAuditBuilder mileStoneBuilder;
	@Autowired
	ProjectNumbersBuilder projectNumbersBuilder;
	@Autowired
	MilestonePeopleBuilder milestonePeopleBuilder;
	@Autowired
	ChangeRequestBuilder changeRequestBuilder;
	@Autowired
	ProjectRequestBuilder projectRequestBuilder;
	@Autowired
	AllocationDetailsBuilder allocationDetailsBuilder;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	ProjectAuditBuilder projectAuditBuilder;
	@Autowired
	AuditBuilder auditBuilder;
	@Autowired
	ProjectManagementMailConfiguration projectManagementMailConfiguration;
	@Autowired
	InvoiceService invoiceServiceImpl;
	@Autowired
	AllocationDetailsService allocationDetailstService;
	@Autowired
	private PropBean propBean;
	@Autowired
	 InvoiceDao invoicedaoImpl;

	Long checkEmployeeId = 0L;
	public static Logger logger = Logger.getLogger(ProjectServiceImpl.class);

	@Override
	public void addProject(ProjectDTO projectDTO) {

		Project duplicateProject = resourceManagementDAO.findByUniqueProperty(
				Project.class, "projectName", projectDTO.getProjectName());
		if (duplicateProject != null) {

			throw new DuplicateProjectException();
		}

		Project project = projectBuilder.createProjectEntityForAddProject(
				projectDTO,
				resourceManagementDAO.findBy(Employee.class,
						projectDTO.getManagerId()));
		Client client = resourceManagementDAO.findByUniqueProperty(
				Client.class, "organization", projectDTO.getClient());

		// generating project code
		List<Project> projectlist = resourceManagementDAO.getProjectList(client
				.getId());
		int listsize = projectlist.size() + 1;
		String ls = (listsize > 10 ? "" + listsize : "0" + listsize);
		project.setProjectCode(projectDTO.getType().substring(0, 1)
				+ projectDTO.getModel().substring(0, 1)
				+ client.getClientCode() + ls);
		// ends here

		project.setClient(client);
		Serializable projectId = resourceManagementDAO.save(project);
		List<Audit> projectAudit = auditBuilder.convertTOProjectEntity(project,
				(Long) projectId, "PROJECT", "CREATED");
		for (Audit audit : projectAudit) {
			resourceManagementDAO.save(audit);
		}

		// Project requested id not null then status should be approved and
		// approved mail should go
		if (projectDTO.getProjectRequestId() != null) {

			ProjectRequest pr = resourceManagementDAO.findBy(
					ProjectRequest.class, projectDTO.getProjectRequestId());
			pr.setStatus("Approved");
			resourceManagementDAO.update(pr);

			projectManagementMailConfiguration.approvedProjectRequest(project);
		} else {

			Project project2 = resourceManagementDAO.findBy(Project.class,
					projectId);
			projectManagementMailConfiguration.projectAddtion(project2);
		}

		Project projectDetails = resourceManagementDAO.findBy(Project.class,
				projectId);
		if (projectDetails.getProjectRequest() != null) {
			ProjectRequest projectRequest = resourceManagementDAO.findBy(
					ProjectRequest.class, projectDetails.getProjectRequest()
							.getId());
			// for updating ProjectRequestMilestone
			Set<ProjectRequestMilestone> milestonesRequestMilestones = new HashSet<ProjectRequestMilestone>();

			for (ProjectRequestMilestoneDTO dto : projectDTO
					.getProjectRequestMilestoneDTO()) {
				ProjectRequestMilestone milestone = dao.findBy(
						ProjectRequestMilestone.class, dto.getId());

				milestone.setMilestoneTitle(dto.getTitle());
				milestone.setEffort(dto.getEffort());
				milestone.setComments(dto.getComments());
				try {
					Date fromDate = null;
					Date toDate = Date.parse(dto.getToDate(), "dd/MM/yyyy");

					fromDate = Date.parse(dto.getFromDate(), "dd/MM/yyyy");

					milestone.setPeriod(new DateRange(fromDate, toDate));
					milestone.setBillable(dto.getBillable());
					if (dto.getMilestonePercentage() != null) {
						milestone.setMilestonePercentage(dto
								.getMilestonePercentage());
					}
				} catch (ParseException parseException) {
					logger.error("parse exception came while converitng String into Date");
				}
				milestonesRequestMilestones.add(milestone);
			}
			projectRequest
					.setProjectRequestMilestone(milestonesRequestMilestones);
			dao.update(projectRequest);

			//
			Set<ProjectRequestMilestone> milestones = projectRequest
					.getProjectRequestMilestone();
			for (ProjectRequestMilestone milestone : milestones) {
				MilestoneDTO dto = new MilestoneDTO();
				dto.setProjectId(projectDetails.getId());
				dto.setProjectType(projectDetails.getType().toString());
				dto.setPlanedDate(milestone.getPeriod().getMaximum()
						.toString("dd/MM/yyyy"));
				dto.setTitle(milestone.getMilestoneTitle());
				dto.setBillable(milestone.getBillable());
				dto.setComments(milestone.getComments());
				dto.setMilestonePercentage(milestone.getMilestonePercentage());
				String milestoneNumber = this
						.getNextMilestoneNumber(projectDetails.getId());
				dto.setMilestoneNumber(milestoneNumber);
				if (milestone.getEffort() != null) {
					dto.setEffort(Long.parseLong(milestone.getEffort()));
				}

				this.addMileStone(dto);

			}
		}

		// this below code is used to create project in hive if u want please uncomment it
		if (projectDetails.getHiveProjectFlag()) {
			String response = null;
			try {
				response = this.createProjectInHive(projectDetails);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (response.equalsIgnoreCase("success")) {
				projectDetails.setHiveProjectName(projectDetails
						.getProjectName());
				dao.update(projectDetails);
			}
		}

	}

	@Override
	public void addProjectRequest(ProjectRequestDTO projectRequestDTO) {

		Project duplicateProject = resourceManagementDAO.findByUniqueProperty(
				Project.class, "projectName",
				projectRequestDTO.getProjectName());
		if (duplicateProject != null) {
			throw new DuplicateProjectException();
		}
		
		ProjectRequest projReqOld = null;
 		if(projectRequestDTO.getId() != null) {
 			//cloning existing project
 			
 			ProjectRequest existingProjectRequest = dao.findBy(ProjectRequest.class,
 					projectRequestDTO.getId());
 
 			try {
 				projReqOld = (ProjectRequest) existingProjectRequest.clone();
 				System.out.println("old status:"+ projReqOld.getStatus());
 			} catch (CloneNotSupportedException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		}

		ProjectRequest projectRequest = projectRequestBuilder
				.createProjectRequestEntity(projectRequestDTO,
						resourceManagementDAO.findBy(Employee.class,
								projectRequestDTO.getManagerId()));

		if (projectRequestDTO.getClient() != null) {

			Client client = resourceManagementDAO
					.findByUniqueProperty(Client.class, "organization",
							projectRequestDTO.getClient());
			projectRequest.setClient(client);
		}
		Long projectRequestId = null;
		// if projectRequest.getId() is not null then we are updating
		// ProjectRequest
		if (projectRequest.getId() != null) {
			projectRequestId = projectRequest.getId();
			resourceManagementDAO.update(projectRequest);
			// inserting audit while editing ProjectRequest
			Audit audit = new Audit();
			audit.setModifiedDate(new Second());
			audit.setModifiedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			audit.setPersistType("UPDATED");
			audit.setTableName("PROJECTREQUEST");
			audit.setReferenceId(projectRequestId);
			dao.save(audit);
			if(projReqOld.getStatus().equalsIgnoreCase("Rejected")) {
	 			projectManagementMailConfiguration
	 					.sendProjectRequestUpdationMail(projectRequest);
	 			}

		}
		// if projectRequest.getId() is null then we are adding ProjectRequest
		else {

			projectRequestId = (Long) resourceManagementDAO
					.save(projectRequest);
			ProjectRequest request = dao.findBy(ProjectRequest.class,
					projectRequestId);
			// inserting audit while creating ProjectRequest
			Audit audit = new Audit();
			audit.setModifiedDate(new Second());
			audit.setModifiedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			audit.setPersistType("CREATED");
			audit.setTableName("PROJECTREQUEST");
			audit.setReferenceId(projectRequestId);
			dao.save(audit);
			try {
				projectManagementMailConfiguration
						.managerRaisedProjectRequest(request);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/*
	 * @Override public List<ProjectRequestDTO> getAllProjectRequestList() { //
	 * Map<String, Object> employeeDetails = securityUtils //
	 * .getLoggedEmployeeDetailsSecurityContextHolder(); // Employee employee =
	 * (Employee) employeeDetails.get("employee"); // Long loggdinEmpId = (Long)
	 * employeeDetails.get("employeeId"); // if
	 * (employee.getRole().equalsIgnoreCase("Admin")) { // return
	 * projectRequestBuilder // .getAllProjectRequestsDTO(resourceManagementDAO
	 * // .get(ProjectRequest.class)); // } else {
	 * 
	 * Long empId = securityUtils
	 * .getLoggedEmployeeIdforSecurityContextHolder(); Employee employee =
	 * dao.findBy(Employee.class, empId);
	 * 
	 * Permission totalList = dao.checkForPermission("Project Request List",
	 * employee); Permission individualList = dao.checkForPermission(
	 * "Individual Project Request List", employee);
	 * 
	 * if (totalList.getView() && !individualList.getView()) { return
	 * projectRequestBuilder .getAllProjectRequestsDTO(resourceManagementDAO
	 * .getAllProjectRequests()); } else if (individualList.getView() &&
	 * totalList.getView()) { List<Long> employeeIds =
	 * mangerUnderManager(empId); return projectRequestBuilder
	 * .getAllProjectRequestsDTO(resourceManagementDAO
	 * .getAllProjectRequestFor(employeeIds)); } else { return null; }
	 * 
	 * // } }
	 */

	// @Override
	// public List<ProjectRequestDTO> getManagerProjectRequests(Long empId) {
	// return projectRequestBuilder
	// .getAllProjectRequestsDTO(resourceManagementDAO
	// .getManagerProjectRequests(empId));
	// }

	@Override
	public void rejectProjectRequest(Long requestId, String comment) {
		ProjectRequest projReqOld = null;
		ProjectRequest projectRequest = dao.findBy(ProjectRequest.class,
				requestId);

		try {
			projReqOld = (ProjectRequest) projectRequest.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		projectRequest.setComment(comment);
		projectRequest.setStatus("Rejected");

		dao.update(projectRequest);
		Audit audit = auditBuilder.updateProjectRequestAduit(projectRequest,
				requestId, projReqOld, "PROJECTREQUEST", "REJECTED");
		dao.save(audit);
		projectManagementMailConfiguration.rejectProjectRequest(requestId,
				comment);
	}

	@Override
	public List<ProjectDTO> getAllProjects() {
		return projectBuilder.createProjectDTOList(resourceManagementDAO
				.get(Project.class));

	}

	@Override
	public void deleteProject(Long projectid) {
		Project project = resourceManagementDAO
				.findBy(Project.class, projectid);
		resourceManagementDAO.deleteProject(projectid);
		Long empId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, empId);
		Audit projectAudit = new Audit();
		projectAudit.setTableName("PROJECT");
		projectAudit.setPersistType("DELETED");
		projectAudit.setReferenceId(project.getId());
		projectAudit.setModifiedBy(employee.getEmployeeId());
		projectAudit.setModifiedDate(new Second());
		resourceManagementDAO.save(projectAudit);
		projectManagementMailConfiguration.sendprojectDeletionMail(project,
				employee);
	}

	@Override
	public void deleteProjectRequest(Long id, HttpServletResponse response) {
		Boolean isExists = resourceManagementDAO.checkForRequestId(id);
		if (isExists) {
			ProjectRequest projReq = dao.findBy(ProjectRequest.class, id);
			Set<ProjectInitiationChecklist> set = projReq.getChecklist();

			for (ProjectInitiationChecklist projInit : set) {
				dao.delete(projInit);
			}

			dao.delete(dao.findBy(ProjectRequest.class, id));
		} else {
			throw new ProjectRequestIdAlreadyExistsException(
					"Projectrequest already mapped with project");
		}
	}

	@Override
	public ProjectDTO updateProject(ProjectDTO projectDTO) {

		Project project = resourceManagementDAO.findBy(Project.class,
				projectDTO.getId());

		Project oldProject = null;
		try {
			oldProject = (Project) project.clone();
		} catch (CloneNotSupportedException ex) {
			java.util.logging.Logger.getLogger(
					ProjectServiceImpl.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		Employee employee = (resourceManagementDAO.findBy(Employee.class,
				projectDTO.getManagerId()));
		projectBuilder.getProjectFromProjectDTO(projectDTO, project, employee);

		project.setClient(dao.findByUniqueProperty(Client.class,
				"organization", projectDTO.getClient()));

		resourceManagementDAO.update(project);

		List<Audit> projectAudit = auditBuilder.projectUpdateAudit(project,
				project.getId(), oldProject, "PROJECT", "UPDATED");
		for (Audit audit : projectAudit) {
			resourceManagementDAO.save(audit);
		}
		if (projectDTO.getStatus().equals("Closed")) {
			projectManagementMailConfiguration.closeProject(project.getId());
		}
		return projectDTO;
	}

	@Override
	public List<ProjectDTO> activeProjects() {
		List<Project> activeprojectlist = resourceManagementDAO
				.activeProjects();

		return projectBuilder.createProjectActiveList(activeprojectlist);
	}

	@CacheEvict(value = "employeeAllocation", allEntries = true)
	@Override
	public void deAllocateProjectToEmployee(ReportDTO reportDTO) {
		Long empId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		Employee emp = dao.findBy(Employee.class, empId);
		Employee employee = dao.findBy(Employee.class,
				reportDTO.getEmployeeId());
		Map<Project, AllocationDetails> map = employee.getAllocations();
		Project project = null;
		for (Map.Entry<Project, AllocationDetails> enter : map.entrySet()) {
			if (enter.getKey().getId().equals(reportDTO.getProjectId())) {
				project = enter.getKey();
				break;
			}

		}
		if (project != null) {
			employee.getAllocations().get(project)
					.setIsAllocated(Boolean.FALSE);
		}
		dao.saveOrUpdate(employee);
		Audit audit = new Audit();
		audit.setTableName("ALLOCATIONDETAILS");
		audit.setColumnName("isAllocated");
		audit.setOldValue("true");
		audit.setNewValue("false");
		audit.setAdditionalInfo(employee.getEmployeeId().toString());
		audit.setReferenceId(project.getId());
		audit.setPersistType("UPDATED");
		audit.setModifiedBy(emp.getEmployeeId());
		audit.setModifiedDate(new Second());
		resourceManagementDAO.save(audit);
		// AllocationDetailsAudit detailsAudit =
		// allocationDetailsBuilder.convertTOAllocationDetailsAudit(map.get(project),
		// employee, project.getId(), "UPDATED");
		projectManagementMailConfiguration
				.sendProjectDeAllocationAcknowledgement(map.get(project));

		/*
		 * try { acknowledgement.sendProjectDeAllocationAcknowledgement(map
		 * .get(project)); } catch (Exception e) { e.printStackTrace(); }
		 */

	}

	@CacheEvict(value = "employeeAllocation", allEntries = true)
	@Override
	public void updateAllocateProject(ReportDTO dto) {
		Employee employee = dao.findBy(Employee.class, dto.getEmployeeId());
		Project project = dao.findBy(Project.class, dto.getProjectId());
		Map<Project, AllocationDetails> map = employee.getAllocations();
		AllocationDetails allocationDetails = map.get(project);
		AllocationDetails existedDetails = null;
		try {
			existedDetails = (AllocationDetails) allocationDetails.clone();
		} catch (CloneNotSupportedException ex) {
			java.util.logging.Logger.getLogger(
					ProjectServiceImpl.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		Date startDate = null;
		Date enddate = null;
		try {
			startDate = Date.parse(dto.getStartDate(), "dd/MM/yyyy");
			enddate = Date.parse(dto.getEndDate(), "dd/MM/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateRange dateRange = new DateRange(startDate, enddate);
		allocationDetails.setPeriod(dateRange);
		
		allocationDetails.setComments(dto.getComments());
		allocationDetails.setIsAllocated(dto.getIsAllocated());
		System.out.println("allocation status:" + dto.getIsAllocated());
		if(dto.getIsAllocated()== false){
			System.out.println("in if boolean");
			allocationDetails.setPercentage(Percentage.valueOf(0d));
			System.out.println("percentage :" + allocationDetails.getPercentage());
			allocationDetails.setBillable(Boolean.FALSE);
			System.out.println(allocationDetails.getBillable());
		}else{
			allocationDetails.setBillable(dto.isBillable());
			Double allocation = Double.valueOf(dto.getAllocation());
			if (allocation > 100) {
				allocation = 100d;
			}
			allocationDetails.setPercentage(Percentage.valueOf(Double
					.valueOf(allocation)));
		}
		map.put(project, allocationDetails);
		employee.setAllocations(map);
		dao.saveOrUpdate(employee);

		// for updating a (edit)record in allocation effort table
		// for purpose of calculating employee allocation effort
		List<AllocationEffort> list = resourceManagementDAO.getEmployeeRecords(
				dto.getProjectId(), dto.getEmployeeId());
		AllocationEffort allocationEffort = null;
		if (list.size() > 0) {
			allocationEffort = list.get(list.size() - 1);

			allocationEffort.setBillable(allocationDetails.getBillable());
			allocationEffort.setIsAllocated(allocationDetails.getIsAllocated());
			allocationEffort.setPercentage(allocationDetails.getPercentage());
			allocationEffort.setUpdatedDate(new Second());
			dao.update(allocationEffort);

		}

		List<Audit> audits = auditBuilder.updateAllocationDetailsTOAudit(
				allocationDetails, existedDetails, employee, project.getId(),
				"ALLOCATIONDETAILS", "UPDATED");
		for (Audit audit : audits) {
			resourceManagementDAO.save(audit);
		}
		// AllocationDetailsAudit detailsAudit =
		// allocationDetailsBuilder.convertTOAllocationDetailsAudit(allocationDetails,
		// employee, project.getId(), "UPDATED");
		if (allocationDetails.getIsAllocated().equals(true)) {

			projectManagementMailConfiguration
					.sendProjectUpdationAcknowledgement(allocationDetails,
							allocationDetails.getEmployee(),
							allocationDetails.getProject());

			/*
			 * acknowledgement.sendProjectUpdationAcknowledgement(
			 * allocationDetails, allocationDetails.getEmployee(),
			 * allocationDetails.getProject());
			 */

		} else if (allocationDetails.getIsAllocated().equals(false)) {
			try {

				projectManagementMailConfiguration
						.sendProjectDeAllocationAcknowledgement(allocationDetails);

				/*
				 * acknowledgement
				 * .sendProjectDeAllocationAcknowledgement(allocationDetails);
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public ProjectDTO getproject(Long projectid) {
		ProjectDTO projectDTO = projectBuilder
				.convertFormProjectToProjecDTO(dao.findBy(Project.class,
						projectid));

		Boolean milestoneExists = resourceManagementDAO
				.isMilestoneExistsForProject(projectid);
		if (milestoneExists)
			projectDTO.setStatuEditFlag("exist");
		else
			projectDTO.setStatuEditFlag("notexist");

		return projectDTO;

	}

	@Override
	public ProjectRequestDTO getProjectRequest(Long id) {
		return projectRequestBuilder.getProjectRequestDTO(dao.findBy(
				ProjectRequest.class, id));
	}

	@Override
	public ReportDTO getAllocateProject(Long employeeid, Long projectid) {
		Employee employee = dao.findBy(Employee.class, employeeid);
		Project project = dao.findBy(Project.class, projectid);
		Map<Project, AllocationDetails> map = employee.getAllocations();
		AllocationDetails allocationDetails = map.get(project);
		ReportDTO dto = new ReportDTO();
		dto.setAllocation(allocationDetails.getPercentage().toString("#0",
				false));
		dto.setBillable(allocationDetails.getBillable());
		dto.setComments(allocationDetails.getComments());
		dto.setStartDate(allocationDetails.getPeriod().getMinimum()
				.toString("dd/MM/yyyy"));
		dto.setEndDate(allocationDetails.getPeriod().getMaximum()
				.toString("dd/MM/yyyy"));
		dto.setProjectId(project.getId());
		dto.setProjectName(project.getProjectName());
		dto.setEmpFirstName(employee.getFirstName());
		dto.setEmpLastName(employee.getLastName());
		dto.setEmployeeId(employee.getEmployeeId());
		return dto;

	}

	@Override
	public List<ManagerDTO> getAllManagers() {
		List<ManagerDTO> managerDTOs = new ArrayList<ManagerDTO>();
		List<Employee> employees = dao.findByManagerName(Employee.class);
		for (Employee employee : employees) {
			ManagerDTO managerDTO = projectBuilder.createManagerDTO(employee);
			managerDTOs.add(managerDTO);
		}

		return managerDTOs;
	}

	@Override
	public List<ProjectNameDTO> activeProjectsByProjectName() {
		List<Project> activeprojectlist = resourceManagementDAO
				.activeProjects();

		return projectBuilder
				.createProjectActiveListByProjectName(activeprojectlist);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> activeProjectsForEmployee(Long employeeid,
			String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex) {
		Employee employee = resourceManagementDAO.findBy(Employee.class,
				employeeid);

		Map<String, Object> projectDTOList = null;

		Boolean totalListFalg = false;
		Boolean individualListFalg = false;
		Boolean hierarchyListFlag = false;

		Permission totalList = dao.checkForPermission("Projects", employee);
		Permission individual = dao.checkForPermission(
				"Individual Project List", employee);
		Permission Hierarchy = dao.checkForPermission("Hierarchy Project List",
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

		if (totalListFalg) {
			projectDTOList = resourceManagementDAO
					.getProjectDetailsEmployeeCountAdmin(projectStatus, type,
							health, firstIndex, endIndex);
		} else if (hierarchyListFlag) {
			List<Long> managers = mangerUnderManager(employeeid);
			managers.add(employeeid);
			projectDTOList = resourceManagementDAO
					.getProjectDetailsEmployeeCount(managers, employeeid,
							projectStatus, type, health, firstIndex, endIndex);

		} else if (individualListFalg) {
			projectDTOList = resourceManagementDAO
					.getProjectDetailsEmployeeCountForEmployee(employeeid,
							projectStatus, type, health, firstIndex, endIndex);
		}

		// if ("Manager".equalsIgnoreCase(employee.getRole())) {
		// List<Long> managers = mangerUnderManager(employeeid);
		// projectDTOList = resourceManagementDAO
		// .getProjectDetailsEmployeeCount(managers, projectStatus,
		// type, health, firstIndex, endIndex);
		//
		// } else if ("admin".equalsIgnoreCase(employee.getRole())
		// || "It Admin".equalsIgnoreCase(employee.getRole())
		// || "Finance".equalsIgnoreCase(employee.getRole())) {
		// projectDTOList = resourceManagementDAO
		// .getProjectDetailsEmployeeCountAdmin(projectStatus, type,
		// health, firstIndex, endIndex);
		// } else if ("Employee".equalsIgnoreCase(employee.getRole())) {
		// projectDTOList = resourceManagementDAO
		// .getProjectDetailsEmployeeCountForEmployee(employeeid,
		// projectStatus, type, health, firstIndex, endIndex);
		// }

		Map<String, Object> map = new HashMap<String, Object>();
		List<ProjectDTO> pdtos = null;
		if (projectDTOList != null) {

			// here for individual List we are calling anthor bulider method
			// where we are setting project people count manually

			if (individualListFalg) {
				pdtos = projectBuilder
						.createProjectActiveListcountForEmployee((List<AllocationDetailsDTO>) projectDTOList
								.get("projectList"));

			} else {
				pdtos = projectBuilder
						.createProjectActiveListcount((List<AllocationDetailsDTO>) projectDTOList
								.get("projectList"));
			}
			map.put("Projsize", projectDTOList.get("size"));
			map.put("Projs", pdtos);
		}

		return map;
	}

	@Override
	public Map<String, Object> projectsForMyProfile(Long employeeid,
			String projectStatus, String type, Integer firstIndex,
			Integer endIndex) {

		Employee employee = resourceManagementDAO.findBy(Employee.class,
				employeeid);
		Map<String, Object> projectDTOList = resourceManagementDAO
				.getProjectDetailsForEmployeeProfile(employeeid, projectStatus,
						type, firstIndex, endIndex);

		Map<String, Object> map = new HashMap<String, Object>();
		if (projectDTOList != null) {
			List<ProjectDTO> pdtos = projectBuilder
					.createProjectActiveListcount((List<AllocationDetailsDTO>) projectDTOList
							.get("projectList"));
			map.put("Projsize", projectDTOList.get("size"));
			map.put("Projs", pdtos);
		}

		return map;
	}

	@Override
	public List<ProjectDTO> getAllProjects_UnderEmployee(Long employeeId,
			String isBillale, String isAllocated, String startdate,
			String endDate, String dateSelection) {
		// Employee emp=dao.findBy(Employee.class, employeeId);
		Date startDate = null;
		Date enddate = null;
		List<ProjectDTO> projectDTOsList = null;
		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				startDate = DateParser.toDate(startdate);
				enddate = DateParser.toDate(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			startDate = dateMap.get("startDate");
			enddate = dateMap.get("endDate");
		}
		DateRange date = new DateRange(startDate, enddate);
		// if(!"Employee".equalsIgnoreCase(emp.getRole())){
		List<String> employeeStatus = null;

		if (isAllocated != null) {
			employeeStatus = new ArrayList<String>();
			String statusArray[] = isAllocated.split(",");

			for (String str : statusArray) {
				if (!str.isEmpty()) {
					employeeStatus.add(str);

				}
			}
		}
		projectDTOsList = projectBuilder
				.ConvertProjectAllocationDetailsEntyToDTO(resourceManagementDAO
						.getAllProjects_UnderEmployee(employeeId, isBillale,
								employeeStatus, date), employeeId);

		/*
		 * } // If we want all projects of the employee then uncomment else
		 * block else {
		 * projectDTOsList=projectBuilder.ConvertProjectAllocationDetailsEntyToDTO
		 * (resourceManagementDAO .getAllProjects_forEmployee(employeeId),
		 * employeeId); }
		 */
		return projectDTOsList;
	}

	@Override
	public Map<String, Object> searchByEmployeeName(Long loggedInEmpId,
			Integer firstIndex, Integer endIndex, String employeeName,
			String technology, String isBillable, String isAllocated,
			String startdate, String enddate, List<String> departmentNames,
			String dateSelection) {
		Employee employee = resourceManagementDAO.findBy(Employee.class,
				loggedInEmpId);
		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(startdate);
				toDate = DateParser.toDate(enddate);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						ProjectServiceImpl.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		List<String> employeeStatus = null;

		if (isAllocated != null) {
			employeeStatus = new ArrayList<String>();
			String statusArray[] = isAllocated.split(",");

			for (String str : statusArray) {
				if (!str.isEmpty()) {
					employeeStatus.add(str);

				}
			}
		}

		Permission totalList = dao.checkForPermission("Employee Allocation",
				employee);
		Permission individual = dao.checkForPermission(
				"Individual Employee Allocation", employee);
		Permission hierarchy = dao.checkForPermission(
				"Hierarchy Employee Allocation", employee);

		DateRange date = new DateRange(fromDate, toDate);

		Map<String, Object> employeeDTOList = null;
		List<EmployeeDTO> dTOs = null;
		// for Manager role
		if (totalList.getView() && hierarchy.getView() && !individual.getView()) {

			List<Long> list = mangerUnderManager(loggedInEmpId);
			employeeDTOList = resourceManagementDAO
					.searchByEmployeeNameForManager(list, firstIndex, endIndex,
							employeeName, technology, isBillable,
							employeeStatus, date);
			dTOs = employeeBuilder.getemployeeDTOListForSearchByNameReport(
					(List<Employee>) employeeDTOList.get("employeeList"), date,
					employeeStatus, isBillable);
		}
		// for admin role
		else if (totalList.getView() && !hierarchy.getView()
				&& !individual.getView()) {
			System.out.println("in if ");
			try {

				employeeDTOList = resourceManagementDAO
						.searchByEmployeeNameForAdmin(loggedInEmpId,
								firstIndex, endIndex, employeeName, technology,
								isBillable, employeeStatus, date,
								departmentNames);

			} catch (HibernateException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dTOs = employeeBuilder.getemployeeDTOListForSearchByNameReport(
					(List<Employee>) employeeDTOList.get("employeeList"), date,
					employeeStatus, isBillable);
		}
		// for employee
		else if (totalList.getView() && !hierarchy.getView()
				&& individual.getView()) {

			EmployeeDTO employeedto = employeeBuilder
					.allcationSearchForEmployee(employee, date);
			List<EmployeeDTO> dtoList = new ArrayList<EmployeeDTO>();
			Map<String, Object> mapList = new HashMap<String, Object>();
			dtoList.add(employeedto);
			mapList.put("employeeList", dtoList);
			mapList.put("size", dtoList.size());
			employeeDTOList = mapList;

			dTOs = dtoList;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		List<EmployeeDTO> finalEmpDTos = new ArrayList<EmployeeDTO>();
		if (employeeDTOList != null) {
			if ((Integer) employeeDTOList.get("size") < endIndex) {
				endIndex = (Integer) employeeDTOList.get("size");
			}
			finalEmpDTos = dTOs.subList(firstIndex, endIndex);
			// if (employeeDTOList != null) {
			map.put("Empsize", employeeDTOList.get("size"));
			map.put("emps", finalEmpDTos);
		}
		return map;
	}

	@Override
	public Map<String, Object> searchByEmployeeId(Long employeeId) {
		List<Employee> employeeDTOList = resourceManagementDAO
				.searchByEmployeeId(employeeId);
		List<EmployeeDTO> dTOs = employeeBuilder
				.getemployeeDTOList(employeeDTOList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Empsize", employeeDTOList.size());
		map.put("emps", dTOs);
		return map;
	}

	@Cacheable(value = "employeeAllocation")
	@Override
	public Map<String, Object> getProfilePaginationEmployeesData(
			Long loggedInEmpId, int startIndex, int endIndex,
			String selectionStatus) {

		Map<String, Object> serachEmpList = resourceManagementDAO
				.getProfilePaginationEmployeesData(loggedInEmpId,
						selectionStatus, startIndex, endIndex);
		List<EmployeeDTO> dTOs = employeeBuilder
				.getemployeeDTOListForSearchByName((List) serachEmpList
						.get("employeeList"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Empsize", serachEmpList.get("size"));
		map.put("emps", dTOs);
		return map;
	}

	@Override
	public List<ProjectDTO> getAllProjects(String projectStatus) {
		List<Project> projects = resourceManagementDAO
				.getAllProjects(projectStatus);
		return projectBuilder.createProjectDTOList(projects);
	}

	@Override
	public Map<String, Object> searchAllocationReportData(Long employeeId,
			String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex, String startdate,
			String enddate, String multiSearch, String datePeriod,
			Boolean intrnalOrNot) {
		Date fromDate = null;
		Date toDate = null;
		Map<String, Date> mapValue = new HashMap<String, Date>();
		if (datePeriod.equalsIgnoreCase("custom")) {
			try {
				fromDate = DateParser.toDate(startdate);
				toDate = DateParser.toDate(enddate);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						ProjectServiceImpl.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		} else {
			mapValue = resourceManagementDAO.getCustomDates(datePeriod);
			fromDate = mapValue.get("startDate");
			toDate = mapValue.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Employee employee = resourceManagementDAO.findBy(Employee.class,
				employeeId);
		Map<String, Object> projectDTOList = null;

		// Boolean totalListFalg = false;
		// Boolean individualListFalg = false;
		// Boolean hierarchyListFlag = false;

		Permission totalList = dao.checkForPermission("Projects", employee);
		Permission individual = dao.checkForPermission(
				"Individual Project List", employee);
		Permission Hierarchy = dao.checkForPermission("Hierarchy Project List",
				employee);
		// if (totalList.getView() && !individual.getView()
		// && !Hierarchy.getView())
		// totalListFalg = true;
		// else if (individual.getView() && totalList.getView()
		// && !Hierarchy.getView())
		// individualListFalg = true;
		// else if (Hierarchy.getView() && !individual.getView()
		// && totalList.getView())
		// hierarchyListFlag = true;

		if (Hierarchy.getView() && !individual.getView() && totalList.getView()) {
			List<Long> subManagerId = mangerUnderManager(employeeId);
			projectDTOList = resourceManagementDAO.searchAllocationReportData(
					subManagerId, employeeId, projectStatus, type, health,
					firstIndex, endIndex, dateRange, multiSearch, intrnalOrNot);
		} else if (totalList.getView() && !individual.getView()
				&& !Hierarchy.getView()) {
			projectDTOList = resourceManagementDAO
					.searchAllocationReportDataForAdmin(projectStatus, type,
							health, firstIndex, endIndex, dateRange,
							multiSearch, intrnalOrNot);
		} else if (individual.getView() && totalList.getView()
				&& !Hierarchy.getView()) {
			projectDTOList = resourceManagementDAO
					.searchProjectDetailsEmployeeCountForEmployee(employeeId,
							projectStatus, type, health, firstIndex, endIndex,
							multiSearch, dateRange);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		if (projectDTOList != null) {
			List<ProjectDTO> pdtos = projectBuilder
					.createProjectActiveListcount((List<AllocationDetailsDTO>) projectDTOList
							.get("projectList"));
			map.put("Projsize", projectDTOList.get("size"));
			map.put("Projs", pdtos);
		}
		return map;

	}

	@Override
	public ByteArrayOutputStream exportProjectList(Long employeeId,
			String projectStatus, String type, String health, String startdate,
			String enddate, String multiSearch, String datePeriod,
			Boolean intrnalOrNot) throws IOException {

		Date fromDate = null;
		Date toDate = null;
		Map<String, Date> mapValue = new HashMap<String, Date>();
		if (datePeriod.equalsIgnoreCase("custom")) {
			try {
				fromDate = DateParser.toDate(startdate);
				toDate = DateParser.toDate(enddate);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						ProjectServiceImpl.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		} else {
			mapValue = resourceManagementDAO.getCustomDates(datePeriod);
			fromDate = mapValue.get("startDate");
			toDate = mapValue.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Employee employee = resourceManagementDAO.findBy(Employee.class,
				employeeId);
		Map<String, Object> projectDTOList = null;
		Permission totalList = dao.checkForPermission("Projects", employee);
		Permission individual = dao.checkForPermission(
				"Individual Project List", employee);
		Permission Hierarchy = dao.checkForPermission("Hierarchy Project List",
				employee);

		if (Hierarchy.getView() && !individual.getView() && totalList.getView()) {
			List<Long> subManagerId = mangerUnderManager(employeeId);
			projectDTOList = resourceManagementDAO.searchAllocationReportData(
					subManagerId, employeeId, projectStatus, type, health, 0,
					1000, dateRange, multiSearch, intrnalOrNot);
		} else if (totalList.getView() && !individual.getView()
				&& !Hierarchy.getView()) {
			projectDTOList = resourceManagementDAO
					.searchAllocationReportDataForAdmin(projectStatus, type,
							health, 0, 1000, dateRange, multiSearch,
							intrnalOrNot);
		} else if (individual.getView() && totalList.getView()
				&& !Hierarchy.getView()) {
			projectDTOList = resourceManagementDAO
					.searchProjectDetailsEmployeeCountForEmployee(employeeId,
							projectStatus, type, health, 0, 1000, multiSearch,
							dateRange);
		}
		List<ProjectDTO> pdtos = null;
		if (projectDTOList != null) {
			pdtos = projectBuilder
					.createProjectActiveListcount((List<AllocationDetailsDTO>) projectDTOList
							.get("projectList"));
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int rowIndex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Project Name");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Pricing model");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Client");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Resources");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Project Manager");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Delivery Manager");
		cell5.setCellStyle(style);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Start Date");
		cell6.setCellStyle(style);

		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("End Date");
		cell7.setCellStyle(style);

		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("Status");
		cell8.setCellStyle(style);

		for (ProjectDTO dto : pdtos) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getProjectName());

			Cell cel1 = row.createCell(1);
			String projecttype = dto.getType();

			if (projecttype != null) {

				cel1.setCellValue(projecttype.equalsIgnoreCase("FIXEDBID") ? "Fixed Bid"
						: projecttype.equalsIgnoreCase("Retainer") ? "Retainer"
								: projecttype.equalsIgnoreCase("Support") ? "Support"
										: "N/A");
			} else {
				cel1.setCellValue("N/A");
			}

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(dto.getClient());

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(dto.getCount());

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(dto.getManagerName());

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(dto.getDeliveryManager());

			Cell cel6 = row.createCell(6);
			cel6.setCellValue(dto.getStartdate());

			Cell cel7 = row.createCell(7);
			cel7.setCellValue(dto.getEnddate());

			Cell cel8 = row.createCell(8);
			cel8.setCellValue(dto.getStatus());

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;

	}

	@CacheEvict(value = "employeeAllocation", allEntries = true)
	@Override
	public void updateEmployeeAllocateProject(ProjectDTO dto) {
		List<AllocationDetails> details = resourceManagementDAO
				.getAllocationProject(dto.getEmployeeId(), dto.getId());

		for (AllocationDetails ad : details) {
			AllocationDetails existedDetails = null;
			try {
				existedDetails = (AllocationDetails) ad.clone();
			} catch (CloneNotSupportedException ex) {
				java.util.logging.Logger.getLogger(
						ProjectServiceImpl.class.getName()).log(Level.SEVERE,
						null, ex);
			}
			Date startingDate = null;
			Date lastDate = null;
			try {
				startingDate = DateParser.toDate(dto.getStartdate());
				lastDate = DateParser.toDate(dto.getEnddate());
			} catch (ParseException pe) {

			}
			DateRange period = new DateRange(startingDate, lastDate);
			ad.setBillable(dto.isBillable());
			ad.setIsAllocated(dto.getIsAllocated());
			ad.setPercentage(Percentage.valueOf(Double.valueOf(dto
					.getAllocation())));
			ad.setPeriod(period);
			dao.saveOrUpdate(ad);
			List<Audit> audits = auditBuilder.updateAllocationDetailsTOAudit(
					ad, existedDetails, ad.getEmployee(), ad.getProject()
							.getId(), "ALLOCATIONDETAILS", "UPDATED");
			for (Audit audit : audits) {
				resourceManagementDAO.save(audit);
			}
			// AllocationDetailsAudit detailsAudit =
			// allocationDetailsBuilder.convertTOAllocationDetailsAudit(ad,
			// ad.getEmployee(), ad.getProject().getId(), "UPDATED");
			if (ad.getIsAllocated().equals(true)) {
				projectManagementMailConfiguration
						.sendProjectUpdationAcknowledgement(ad,
								ad.getEmployee(), ad.getProject());

			} else if (ad.getIsAllocated().equals(false)) {
				projectManagementMailConfiguration
						.sendProjectDeAllocationAcknowledgement(ad);
			}
		}

		// saving allocation details in allocation effort so as
		// to get the employee allocation effort in project metrics
		AllocationEffort allocationEffort = new AllocationEffort();

		Date oldEndDate = null;
		Date oldStartDate = null;
		Date startDate = null;
		Date lastDate = null;
		Date changedEndDate = null;
		Percentage newPercentage = Percentage.valueOf(Double.valueOf(dto
				.getAllocation()));
		try {

			startDate = DateParser.toDate(dto.getStartdate());
			lastDate = DateParser.toDate(dto.getEnddate());
		} catch (ParseException e) {

			e.printStackTrace();
		}

		DateRange range = new DateRange(startDate, lastDate);

		List<AllocationEffort> list = resourceManagementDAO.getEmployeeRecords(
				dto.getId(), dto.getEmployeeId());

		logger.info("list size:" + list.size());

		if (list.size() != 0) {
			AllocationEffort allocEffort = list.get(list.size() - 1);

			oldEndDate = allocEffort.getPeriod().getMaximum();
			oldStartDate = allocEffort.getPeriod().getMinimum();
			Percentage oldPercentage = allocEffort.getPercentage();
			Date currentDate = new Date();

			/*
			 * System.out.println("oldStartDate:" + oldStartDate);
			 * System.out.println("oldEndDate:" + oldEndDate);
			 * System.out.println("newStartDate:" + startDate);
			 * System.out.println("newEndDate:" + lastDate);
			 * System.out.println("oldPercentage:" + oldPercentage);
			 * System.out.println("newPercentage:" + newPercentage);
			 */
			if (dto.getIsAllocated() == false && lastDate.isBefore(oldEndDate)) {

				// System.out.println("deallocating before");
				DateRange period = new DateRange(oldStartDate, lastDate);
				allocEffort.setPeriod(period);
				allocEffort.setPercentage(Percentage.valueOf(Double.valueOf(dto
						.getAllocation())));
				allocEffort.setIsAllocated(dto.getIsAllocated());
				allocEffort.setBillable(dto.isBillable());
				allocEffort.setUpdatedDate(new Second());

				dao.update(allocEffort);

			}

			else if (dto.getIsAllocated() == false
					&& lastDate.equals(oldEndDate)) {

				// System.out.println("deallocating on the same day");
				DateRange period = new DateRange(oldStartDate, oldEndDate);
				allocEffort.setPeriod(period);
				allocEffort.setPercentage(Percentage.valueOf(Double.valueOf(dto
						.getAllocation())));
				allocEffort.setIsAllocated(dto.getIsAllocated());
				allocEffort.setBillable(dto.isBillable());
				allocEffort.setUpdatedDate(new Second());

				dao.update(allocEffort);

			}

			else if (allocEffort.isAllocated == Boolean.FALSE) {
				// System.out.println("inserting after deallocation");
				allocationEffort.setBillable(dto.isBillable());
				allocationEffort.setIsAllocated(dto.getIsAllocated());
				allocationEffort.setPercentage(Percentage.valueOf(Double
						.valueOf(dto.getAllocation())));
				allocationEffort.setPeriod(range);
				allocationEffort.setCreatedDate(new Second());
				allocationEffort.setEmployee(dao.findBy(Employee.class,
						dto.getEmployeeId()));
				allocationEffort.setProject(dao.findBy(Project.class,
						dto.getId()));

				dao.save(allocationEffort);

			}

			else if (currentDate.isAfter(oldStartDate)
					&& currentDate.isBefore(oldEndDate)
					|| currentDate.equals(oldStartDate)) {

				if ((startDate.equals(oldStartDate) && (lastDate
						.equals(oldEndDate)))
						|| (lastDate.isBefore(oldEndDate) && newPercentage
								.equals(oldPercentage))
						|| (lastDate.isAfter(oldEndDate) && newPercentage
								.equals(oldPercentage))
						|| (currentDate.equals(oldStartDate) && !newPercentage
								.equals(oldPercentage))) {

					DateRange period = new DateRange(oldStartDate, lastDate);
					// System.out.println("date is between these dates-update");
					allocEffort.setPeriod(period);
					allocEffort.setPercentage(Percentage.valueOf(Double
							.valueOf(dto.getAllocation())));
					allocEffort.setIsAllocated(dto.getIsAllocated());
					allocEffort.setBillable(dto.isBillable());
					allocEffort.setUpdatedDate(new Second());

					dao.update(allocEffort);

				}

				else {
					// System.out.println("date is between these dates-insert");

					Date changedStartDate = currentDate;
					changedEndDate = currentDate.previous();

					DateRange period = new DateRange(oldStartDate,
							changedEndDate);

					allocEffort.setPeriod(period);
					dao.update(allocEffort);

					DateRange range1 = new DateRange(currentDate, lastDate);
					allocationEffort.setBillable(dto.isBillable());
					allocationEffort.setIsAllocated(dto.getIsAllocated());
					allocationEffort.setPercentage(Percentage.valueOf(Double
							.valueOf(dto.getAllocation())));
					allocationEffort.setPeriod(range1);
					allocationEffort.setCreatedDate(new Second());
					allocationEffort.setEmployee(dao.findBy(Employee.class,
							dto.getEmployeeId()));
					allocationEffort.setProject(dao.findBy(Project.class,
							dto.getId()));

					dao.save(allocationEffort);

				}

			}

			else {
				// System.out.println("future dates-update");
				DateRange period = new DateRange(startDate, lastDate);
				allocEffort.setPeriod(period);
				allocEffort.setPercentage(Percentage.valueOf(Double.valueOf(dto
						.getAllocation())));
				allocEffort.setIsAllocated(dto.getIsAllocated());
				allocEffort.setBillable(dto.isBillable());
				allocEffort.setUpdatedDate(new Second());

				dao.update(allocEffort);

			}
		}
	}

	@Override
	public void addMileStone(MilestoneDTO dto) {

		logger.warn("in add milestone");
		Project project = dao.findBy(Project.class, dto.getProjectId());
		String projectType = project.getType().toString();
		Milestone milestone = new Milestone();
		milestone.setTitle(dto.getTitle());
		milestone.setMilestoneNumber(dto.getMilestoneNumber());
		Date actualDate = null;
		Date planeddate = null;
		try {
			planeddate = Date.parse(dto.getPlanedDate(), "dd/MM/yyyy");
			actualDate = Date.parse(dto.getActualDate(), "dd/MM/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		milestone.setActualDate(actualDate);
		milestone.setPlanedDate(planeddate);
		milestone.setBillable(dto.getBillable());
		milestone.setComments(dto.getComments());
		milestone.setProject(project);
		if (dto.getEffort() != null) {
			milestone.setEffort(dto.getEffort());
		}

		logger.warn(projectType);

		logger.warn("condition"
				+ (projectType.equalsIgnoreCase("Retainer") || projectType
						.equalsIgnoreCase("Support")));

		if (projectType.equalsIgnoreCase("Retainer")
				|| projectType.equalsIgnoreCase("Support")) {
			milestone.setMilestonePercentage("100");
		} else {
			milestone.setMilestonePercentage(dto.getMilestonePercentage());
		}

		/*
		 * milestone.setMilestonePercentage(((!projectType
		 * .equalsIgnoreCase("Retainer")) || (!projectType
		 * .equalsIgnoreCase("Support"))) ? dto .getMilestonePercentage() :
		 * "100");
		 */

		logger.warn("milestone percentage" + milestone.getMilestonePercentage());

		milestone.setAddOn(new Date());
		milestone.setClosed(Boolean.FALSE);
		milestone.setInvoiceStatus(Boolean.FALSE);
		milestone.setInvoiceReopenFlag(Boolean.FALSE);

		// logger.warn("milestoneType flag"+dto.getMilestoneTypeFlag());

		// logger.warn("type of flag"+dto.getMilestoneTypeFlag().getClass().getTypeName());

		milestone.setMilestoneTypeFlag(dto.getMilestoneTypeFlag() != null ? dto
				.getMilestoneTypeFlag() : Boolean.FALSE);

		// logger.warn("after setting the flag value"+milestone.getMilestoneTypeFlag());

		if (dto.getCrId() != null) {
			ChangeRequest changeRequest = dao.findBy(ChangeRequest.class,
					dto.getCrId());

			if (!projectType.equalsIgnoreCase("Retainer")
					&& !projectType.equalsIgnoreCase("Support")) {

				// here we are checking only if milestone is billable
				if (dto.getBillable()) {
					// here while adding we are not sending miletone id because
					// it
					// is not available
					Double CRAlreadyRaisedPercentage = resourceManagementDAO
							.getRaisedCrPercentage(changeRequest, null);
					if (dto.getMilestoneTypeFlag().equals(Boolean.FALSE)) {
						Double raisingCRPercentage = Double.valueOf(dto
								.getMilestonePercentage());

						Double allOverpercentage = CRAlreadyRaisedPercentage
								+ raisingCRPercentage;

						if (allOverpercentage >= 100) {
							changeRequest.setMilestoneStatus(Boolean.TRUE);
							dao.update(changeRequest);
						}

					}
				}
			} else {
				changeRequest.setMilestoneStatus(Boolean.TRUE);
				dao.update(changeRequest);
			}

			milestone.setChangeRequest(changeRequest);
		}

		if (projectType.equalsIgnoreCase("RETAINER")
				|| projectType.equalsIgnoreCase("SUPPORT")) {
			if (dto.getAllocatedMilestonePeople() != null) {
				List<MilestonePeople> milestonePeoples = milestonePeopleBuilder
						.convertReportDtoToList(dto
								.getAllocatedMilestonePeople());
				Set<MilestonePeople> milestonePeoplesSet = new HashSet<MilestonePeople>(
						milestonePeoples);
				milestone.setMilestonePeople(milestonePeoplesSet);
			}
		}
		Serializable milestoneId = dao.save(milestone);
		MilestoneAudit milestoneAudit = mileStoneBuilder
				.convertMileStoneToMilestoneAudit(milestone,
						(Long) milestoneId, "CREATED");

		dao.save(milestoneAudit);
	}

	@Override
	public Map<String, Object> getAllMilestones_UnderProject(Long projectId,
			Integer firstIndex, Integer endIndex) {
		Map<String, Object> milestones = resourceManagementDAO
				.getAllMilestones_UnderProject(projectId, firstIndex, endIndex);
		Integer size = (Integer) milestones.get("size");
		List<Milestone> milestones2 = (List<Milestone>) milestones.get("list");
		List<MilestoneDTO> milestonesList = new ArrayList<MilestoneDTO>();
		Double projectAllMilestonesPercentage = 0.0;
		Double projectCrsMilestonePercentage = 0.0;

		// below code is used to calucate the overall milestone percentage of
		// project
		if (milestones2 != null) {
			for (Milestone milestone : milestones2) {
				if(milestone.getMilestonePercentage()!=null){
				if (!milestone.getMilestonePercentage().isEmpty()
						&& milestone.getChangeRequest() == null) {
					projectAllMilestonesPercentage += Double
							.parseDouble(milestone.getMilestonePercentage());
				}
				}
			}

			/*
			 * below code is for cr milestones reopen button functionality here
			 * code is calucating cr's over allpercentage of the project
			 */
			Map<Long, Double> crValues = new HashMap<Long, Double>();
			List<ChangeRequest> crlist = resourceManagementDAO
					.getCrsUnderProject(projectId);
			if (crlist != null) {
				for (ChangeRequest changeRequest : crlist) {
					List<Milestone> crMilestonesList = resourceManagementDAO
							.getMilestonesUnderCr(changeRequest.getId());
					logger.warn("crId" + changeRequest.getId());
					logger.warn("size" + crMilestonesList.size());
					if (crMilestonesList != null) {

						for (Milestone milestonescrList : crMilestonesList) {
							logger.warn(milestonescrList.getChangeRequest()
									.getId());
                             if(milestonescrList.getMilestonePercentage()!=null){
							if (!milestonescrList.getMilestonePercentage()
									.isEmpty()) {

								logger.warn("inside milestonepercentage"
										+ milestonescrList
												.getMilestonePercentage());
								projectCrsMilestonePercentage += Double
										.parseDouble(milestonescrList
												.getMilestonePercentage());

								logger.warn("project cr percentage"
										+ projectCrsMilestonePercentage);
							}
                             }
						}
					}
					crValues.put(changeRequest.getId(),
							projectCrsMilestonePercentage);
				}
			}

			/* end of cr reopenbutton functionality */

			for (Milestone milestone : milestones2) {
				MilestoneDTO milestoneDTO = new MilestoneDTO();
				milestoneDTO.setId(milestone.getId());
				milestoneDTO.setTitle(milestone.getTitle());
				milestoneDTO
						.setProject(milestone.getProject().getProjectName());
				milestoneDTO.setClient(milestone.getProject().getClient()
						.getName());
				milestoneDTO
						.setMilestoneNumber((milestone.getMilestoneNumber() != null ? milestone
								.getMilestoneNumber() : null));
				if (milestone.getActualDate() != null) {
					milestoneDTO.setActualDate(milestone.getActualDate()
							.toString("dd/MM/yyyy"));
				}
				milestoneDTO
						.setAllocatedMilestonePeople(getPeopleUnderMilestone(
								milestone.getId(), projectId));
				milestoneDTO.setPlanedDate(milestone.getPlanedDate().toString(
						"dd/MM/yyyy"));
				milestoneDTO.setBillable(milestone.isBillable());
				milestoneDTO.setComments(milestone.getComments());
				milestoneDTO.setIsClosed(milestone.isClosed());
				milestoneDTO.setInvoiceStatus(milestone.getInvoiceStatus());

				milestoneDTO.setMilestonePercentage(milestone
						.getMilestonePercentage());

				milestoneDTO.setMilestoneTypeFlag(milestone
						.getMilestoneTypeFlag() != null ? milestone
						.getMilestoneTypeFlag() : null);
				milestoneDTO.setInvoiceReopenFlag(milestone
						.getInvoiceReopenFlag());
				if (milestone.getChangeRequest() != null) {
					milestoneDTO.setCrId(milestone.getChangeRequest().getId());
					milestoneDTO.setCrName(milestone.getChangeRequest()
							.getName());
				}
				milestoneDTO.setEffort(milestone.getEffort());
				milestoneDTO.setProjectType(milestone.getProject().getType()
						.name());

				/*
				 * if milestone is cr then checking for that cr milestone
				 * percentage is reached to 100 or not
				 */

				/*
				 * If milestone is not cr it will check projectOverallPercentage
				 * is reached to 100 or not
				 */

				if (milestoneDTO.getCrId() != null) {
					Double overAllCrPercentage = crValues.get(milestoneDTO
							.getCrId());
					if (overAllCrPercentage != 100.0
							&& (milestone.getInvoiceStatus() == Boolean.TRUE)) {
						milestoneDTO.setEnableReopenFlag(Boolean.TRUE);
					} else {
						milestoneDTO.setEnableReopenFlag(Boolean.FALSE);
					}
				} else {
					if ((projectAllMilestonesPercentage != 100.0)
							&& (milestone.getInvoiceStatus() == Boolean.TRUE)) {
						milestoneDTO.setEnableReopenFlag(Boolean.TRUE);
					} else {
						milestoneDTO.setEnableReopenFlag(Boolean.FALSE);
					}

				}

				/*
				 * List<Invoice> invoice = invoiceDao
				 * .getInvoicesUnderMilestone(milestone.getProject() .getId());
				 * 
				 * logger.warn(invoice.size());
				 * 
				 * Long overAllPercentage = 0L;
				 * 
				 * List<Long> milestoneIds = new ArrayList<Long>(); if
				 * (invoice.size() != 0) { for (Invoice invoices : invoice) {
				 * overAllPercentage += Long.parseLong(invoices
				 * .getPercentage());
				 * milestoneIds.add(invoices.getMilsestone().getId()); }
				 * 
				 * if(milestoneIds.contains(milestone.getId())){
				 * milestoneDTO.setInvoiceExits(Boolean.TRUE); }else{
				 * milestoneDTO.setInvoiceExits(Boolean.FALSE); }
				 * 
				 * }
				 */

				// logger.warn("enableFlag" +
				// milestoneDTO.getEnableReopenFlag());

				milestonesList.add(milestoneDTO);
			}
		}
		List<MilestoneDTO> milestoneDTOs = new ArrayList<MilestoneDTO>();
		if (size < endIndex) {
			milestoneDTOs = milestonesList.subList(firstIndex, size);
		} else {
			milestoneDTOs = milestonesList.subList(firstIndex, endIndex);
		}
		milestones.put("list", milestoneDTOs);
		milestones.put("size", milestones.get("size"));
		return milestones;
	}

	@Override
	public void updateMileStone(MilestoneDTO dto) {
		Date actualDate = null;
		Date planeddate = null;
		String projectType = null;
		try {
			planeddate = Date.parse(dto.getPlanedDate(), "dd/MM/yyyy");
			actualDate = Date.parse(dto.getActualDate(), "dd/MM/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Milestone milestone = dao.findBy(Milestone.class, dto.getId());
		milestone.setTitle(dto.getTitle());
		milestone.setMilestoneNumber(dto.getMilestoneNumber());
		milestone.setActualDate(actualDate);
		milestone.setPlanedDate(planeddate);

		projectType = milestone.getProject().getType().toString();

		List<InvoiceDTO> raisedInvoiceList = this.getRaisedInvoices(milestone
				.getId());
		Double totalinvoiceRaisedPercentage = 0.0;
		for (InvoiceDTO list : raisedInvoiceList) {

			totalinvoiceRaisedPercentage += Double.parseDouble(list
					.getPercentage());

		}

		/*
		 * in updating milestone if we are changing the
		 * milestooneAmount/percentge is changed to amount to percentge the
		 * below condition will be true
		 */

		/*
		 * In else condition updating the milestone to raised invoice percentage
		 * then Raise invoice will be daisabled
		 */

		if (projectType.equalsIgnoreCase("FIXEDBID")
				&& (dto.getBillable() == Boolean.TRUE)
				&& (dto.getMilestoneTypeFlag() == Boolean.FALSE)
				&& (milestone.getMilestoneTypeFlag() == Boolean.TRUE)
				&& (milestone.isBillable() == Boolean.TRUE)) {

			/*
			 * here while adding the milestone user selected the amount radio
			 * button then percentage will be empty ,while updating amount to
			 * percentgae then check the below condition.
			 */

			if (!milestone.getMilestonePercentage().isEmpty()) {
				if (Double.parseDouble(dto.getMilestonePercentage()) < Double
						.parseDouble(milestone.getMilestonePercentage())
						&& (Double.parseDouble(dto.getMilestonePercentage()) == totalinvoiceRaisedPercentage)) {
					milestone.setInvoiceStatus(Boolean.TRUE);
				} else {
					milestone.setInvoiceStatus(Boolean.FALSE);
				}
			}

			if ((milestone.getInvoiceStatus() == Boolean.TRUE)
					&& (milestone.getInvoiceReopenFlag() == Boolean.TRUE)) {
				milestone.setInvoiceReopenFlag(Boolean.FALSE);
			}

		} else if (projectType.equalsIgnoreCase("FIXEDBID")
				&& (dto.getBillable() == Boolean.TRUE)
				&& (dto.getMilestoneTypeFlag() == Boolean.FALSE)
				&& (milestone.getMilestoneTypeFlag() == Boolean.FALSE)
				&& (milestone.isBillable() == Boolean.TRUE)) {

			if (Double.parseDouble(dto.getMilestonePercentage()) <= Double
					.parseDouble(milestone.getMilestonePercentage())
					&& (Double.parseDouble(dto.getMilestonePercentage()) == totalinvoiceRaisedPercentage)) {
				milestone.setInvoiceStatus(Boolean.TRUE);
			} else {
				milestone.setInvoiceStatus(Boolean.FALSE);
			}

			if ((milestone.getInvoiceStatus() == Boolean.TRUE)
					&& (milestone.getInvoiceReopenFlag() == Boolean.TRUE)) {
				milestone.setInvoiceReopenFlag(Boolean.FALSE);
			}

		}

		milestone.setMilestoneTypeFlag(dto.getMilestoneTypeFlag() != null ? dto
				.getMilestoneTypeFlag() : null);
		milestone.setBillable(dto.getBillable());
		milestone.setMilestonePercentage((!projectType
				.equalsIgnoreCase("Retainer")) ? dto.getMilestonePercentage()
				: "100");

		milestone.setComments(dto.getComments());
		if (dto.getIsClosed() != null) {
			milestone.setClosed(dto.getIsClosed());
		} else {
			milestone.setClosed(Boolean.FALSE);
		}

		// milestone people are to be added for only retainer project
		if (projectType.equalsIgnoreCase("RETAINER")
				|| projectType.equalsIgnoreCase("SUPPORT")) {
			milestone.setMilestonePeople(new HashSet<MilestonePeople>(
					milestonePeopleBuilder.convertReportDtoToList(dto
							.getAllocatedMilestonePeople())));
		}
		resourceManagementDAO.saveOrUpdate(milestone);
		// set the milestone flag as true in the cr if the
		// milestone has been updating for a cr
		if (dto.getCrId() != null) {

			ChangeRequest changeRequest = dao.findBy(ChangeRequest.class,
					dto.getCrId());

			if (!projectType.equalsIgnoreCase("Retainer")
					&& !projectType.equalsIgnoreCase("Support")) {

				// here while editing we have to see wheather milestone is
				// billable if it is then check validation for invoice status of
				// milestone and if not billable set CR if available to that
				// milestone
				if (milestone.isBillable()) {
					// here while editing we are sending milestone id because we
					// should not add updating milestone percentage into
					// calculation
					if (dto.getMilestoneTypeFlag().equals(Boolean.FALSE)) {
						Double CRAlreadyRaisedPercentage = resourceManagementDAO
								.getRaisedCrPercentage(changeRequest,
										milestone.getId());

						Double raisingCRPercentage = (dto
								.getMilestonePercentage() != "") ? Double
								.valueOf(dto.getMilestonePercentage()) : (dto
								.getMilestonePercentage() != null) ? Double
								.valueOf(dto.getMilestonePercentage()) : 0;

						Double allOverpercentage = CRAlreadyRaisedPercentage
								+ raisingCRPercentage;

						logger.warn("Already raised % "
								+ CRAlreadyRaisedPercentage + " Now Raising % "
								+ raisingCRPercentage + " All Over % "
								+ allOverpercentage);
						logger.warn("allOverpercentage >= 100"
								+ (allOverpercentage >= 100));

						// here we are checking if all over percentage for cr is
						// 100
						// percent or not if yes making milestone status true
						// else
						// flase
						if (allOverpercentage >= 100) {
							changeRequest.setMilestoneStatus(Boolean.TRUE);
							dao.update(changeRequest);
							milestone.setChangeRequest(changeRequest);
						} else {
							changeRequest.setMilestoneStatus(Boolean.FALSE);
							dao.update(changeRequest);
							milestone.setChangeRequest(changeRequest);
						}
					} else {
						changeRequest.setMilestoneStatus(Boolean.FALSE);
						dao.update(changeRequest);
						milestone.setChangeRequest(changeRequest);

					}
				} else {
					milestone.setChangeRequest(changeRequest);
				}
			} else {
				changeRequest.setMilestoneStatus(Boolean.TRUE);
				dao.update(changeRequest);
				milestone.setChangeRequest(changeRequest);
			}
		}

		MilestoneAudit milestoneAudit = mileStoneBuilder
				.convertMileStoneToMilestoneAudit(milestone, milestone.getId(),
						"UPDATED");
		dao.save(milestoneAudit);
	}

	@Override
	public void deleteMilestone(Long milestoneId) {
		ChangeRequest cr = null;
		Milestone milestone = dao.findBy(Milestone.class, milestoneId);

		resourceManagementDAO.delete(milestone);
		// if milestone is to be deleted then if the milestone contains the cr
		// then we should make the milestone flag in cr table as false
		if (milestone.getChangeRequest() != null) {
			cr = milestone.getChangeRequest();
			cr.setMilestoneStatus(Boolean.FALSE);
			dao.update(cr);
		}
	}

	@Override
	public MilestoneDTO getMilestone(Long milestoneId) {
		Milestone milestone = dao.findBy(Milestone.class, milestoneId);

		MilestoneDTO milestoneDTO = null;
		if (milestone != null) {
			milestoneDTO = new MilestoneDTO();
			milestoneDTO.setId(milestone.getId());
			milestoneDTO.setTitle(milestone.getTitle());
			milestoneDTO
					.setMilestoneNumber((milestone.getMilestoneNumber() != null ? milestone
							.getMilestoneNumber() : null));
			if (milestone.getActualDate() != null) {
				milestoneDTO.setActualDate(milestone.getActualDate().toString(
						"dd/MM/yyyy"));
			}
			milestoneDTO.setAllocatedMilestonePeople(getPeopleUnderMilestone(
					milestone.getId(), milestone.getProject().getId()));
			milestoneDTO.setPlanedDate(milestone.getPlanedDate().toString(
					"dd/MM/yyyy"));
			milestoneDTO.setBillable(milestone.isBillable());
			milestoneDTO.setComments(milestone.getComments());
			milestoneDTO.setIsClosed(milestone.isClosed());
			milestoneDTO.setInvoiceStatus(milestone.getInvoiceStatus());
			milestoneDTO.setMilestonePercentage(milestone
					.getMilestonePercentage());
			milestoneDTO
					.setMilestoneTypeFlag(milestone.getMilestoneTypeFlag() != null ? milestone
							.getMilestoneTypeFlag() : null);
			if (milestone.getChangeRequest() != null) {
				milestoneDTO.setCrId(milestone.getChangeRequest().getId());
				milestoneDTO.setCrName(milestone.getChangeRequest().getName());
			}
		}

		return milestoneDTO;
	}

	@Override
	public void addStatusReport(StatusReportDTO dto) {
		Project project = dao.findBy(Project.class, dto.getProjectId());
		Date prveDate = null;
		Date nextdate = null;
		try {
			prveDate = Date.parse(dto.getPrevDate(), "dd/MM/yyyy");
			nextdate = Date.parse(dto.getNextDate(), "dd/MM/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		StatusReport statusReport = new StatusReport();
		statusReport.setPrevstatus(dto.getPrevstatus());
		statusReport.setPrevDate(prveDate);
		statusReport.setNextstatus(dto.getNextstatus());
		statusReport.setNextDate(nextdate);
		statusReport.setProject(project);
		statusReport.setAddOn(new Date());
		dao.save(statusReport);
	}

	@Override
	public void updateStatusReport(StatusReportDTO dto) {
		StatusReport statusReport = dao.findBy(StatusReport.class, dto.getId());
		Date prveDate = null;
		Date nextdate = null;
		try {
			prveDate = Date.parse(dto.getPrevDate(), "dd/MM/yyyy");
			nextdate = Date.parse(dto.getNextDate(), "dd/MM/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		statusReport.setPrevstatus(dto.getPrevstatus());
		statusReport.setPrevDate(prveDate);
		statusReport.setNextstatus(dto.getNextstatus());
		statusReport.setNextDate(nextdate);
		resourceManagementDAO.saveOrUpdate(statusReport);

	}

	@Override
	public void deleteStatusReport(Long statusReportId) {
		StatusReport statusReport = dao.findBy(StatusReport.class,
				statusReportId);
		dao.delete(statusReport);
	}

	@Override
	public Map<String, Object> getAllStatusReports_UnderProject(Long projectId,
			Integer firstIndex, Integer endIndex) {
		Map<String, Object> statusReports = resourceManagementDAO
				.getAllStatusReports_UnderProject(projectId, firstIndex,
						endIndex);
		Integer size = (Integer) statusReports.get("size");
		List<StatusReport> statusReports2 = (List<StatusReport>) statusReports
				.get("list");
		List<StatusReportDTO> statusReportsList = new ArrayList<StatusReportDTO>();
		if (statusReports2 != null) {
			for (StatusReport report : statusReports2) {
				StatusReportDTO reportDTO = new StatusReportDTO();
				reportDTO.setId(report.getId());
				reportDTO.setNextDate(report.getNextDate().toString());
				reportDTO.setNextstatus(report.getNextstatus());
				reportDTO.setPrevDate(report.getPrevDate().toString());
				reportDTO.setPrevstatus(report.getPrevstatus());
				reportDTO.setProjectId(projectId);
				statusReportsList.add(reportDTO);
			}
		}
		List<StatusReportDTO> statusReportDTOs = new ArrayList<StatusReportDTO>();
		if (size < endIndex) {
			statusReportDTOs = statusReportsList.subList(firstIndex, size);
		} else {
			statusReportDTOs = statusReportsList.subList(firstIndex, endIndex);
		}
		statusReports.put("list", statusReportDTOs);
		statusReports.put("size", statusReports.get("size"));
		return statusReports;
	}

	@Override
	public void addClient(ClientDTO clientDTO) {

		Client duplicateClient = resourceManagementDAO.findByUniqueProperty(
				Client.class, "name", clientDTO.getName());
		Client dupilicateClientCode = resourceManagementDAO
				.findByUniqueProperty(Client.class, "clientCode",
						clientDTO.getClientCode());
		if (dupilicateClientCode != null) {
			throw new DuplicateClientCodeException();
		}
		if (duplicateClient != null) {
			throw new DuplicateClientException();
		}
		Client duplicateOrganization = resourceManagementDAO
				.findByUniqueProperty(Client.class, "organization",
						clientDTO.getOrganization());
		if (duplicateOrganization != null) {
			throw new DuplicateClientOrganizationException();
		}

		Country country = resourceManagementDAO.findByUniqueProperty(
				Country.class, "name", clientDTO.getCountry());
		Client client = projectBuilder.createClientEntity(clientDTO);
		client.setCountry(country);
		dao.save(client);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getAllClients(Integer startIndex,
			Integer endIndex, String selectionStatus) {
		Map<String, Object> map = resourceManagementDAO.getAllClients(
				startIndex, endIndex, selectionStatus);

		map.put("clients", projectBuilder
				.createClientDTOsList((List<Client>) map.get("clients")));

		return map;
	}

	@Override
	public void deleteClient(Long clientId) {
		Client client = dao.findBy(Client.class, clientId);
		dao.delete(client);
	}

	@Override
	public ClientDTO getclient(Long clientId) {
		Client client = resourceManagementDAO.findBy(Client.class, clientId);
		return projectBuilder.createClientDTO(client);
	}

	@Override
	public List<CountryDTO> getCountries() {

		return projectBuilder.getCountriesDTOsList(resourceManagementDAO
				.getCountries());
	}

	@Override
	public void updateClient(ClientDTO clientDTO) {
		Client client = resourceManagementDAO.findBy(Client.class,
				clientDTO.getId());
		projectBuilder.getUpdatedClient(client, clientDTO);
		client.setCountry(resourceManagementDAO.findByUniqueProperty(
				Country.class, "name", clientDTO.getCountry()));
		resourceManagementDAO.save(client);
	}

	@Override
	public Map<String, Object> getBillingReports(Integer startIndex,
			Integer endIndex, String status) {

		Map<String, Object> map = resourceManagementDAO.getBillingReports(
				startIndex, endIndex, status);
		Integer size = (Integer) map.get("size");
		List<Milestone> milestones = (List<Milestone>) map.get("reports");

		if (size < endIndex) {
			map.put("reports", projectBuilder.createBillingDTO(milestones
					.subList(startIndex, size)));

		} else {
			map.put("reports", projectBuilder.createBillingDTO(milestones
					.subList(startIndex, endIndex)));
		}

		// map.put("reports", projectBuilder
		// .createBillingDTO(milestones));
		return map;

	}

	@Override
	public Map<String, Object> searchClients(String search, Integer firstIndex,
			Integer endIndex, String selectionStatus) {

		Map<String, Object> map = resourceManagementDAO.searchClients(search,
				firstIndex, endIndex, selectionStatus);

		map.put("clients", projectBuilder
				.createClientDTOsList((List<Client>) map.get("clients")));

		return map;
	}

	@Override
	public void closeMilestone(Long milestoneId) {

		Milestone milestone = resourceManagementDAO.findBy(Milestone.class,
				milestoneId);

		milestone.setClosed(Boolean.TRUE);
		resourceManagementDAO.update(milestone);
		if (milestone.isBillable()) {

			List<Employee> employees = (List<Employee>) invoiceReminder
					.getOutput().get("employees");
			// String cc = (String) invoiceReminder.getOutput().get("cc");

			projectNotification.alertMilestoneClosing(milestone.getProject(),
					milestone.getTitle(), "milestoneClose");

			for (Employee employee : employees) {

				Alert alert = alertBuilder.createMilestoneAlert(milestone,
						employee, milestone.getProject().getProjectName()
								+ "'s " + milestone.getTitle()
								+ " milestone was closed");
				resourceManagementDAO.save(alert);
			}
		}

		resourceManagementDAO.save(milestone);
		MilestoneAudit milestoneAudit = mileStoneBuilder
				.convertMileStoneToMilestoneAudit(milestone, milestone.getId(),
						"CLOSED");
		dao.save(milestoneAudit);

	}

	public List<MileStoneAuditDTO> getAllMileStoneHistory(Long mileStoneId) {

		return mileStoneBuilder.convertToMileStoneDTOList(resourceManagementDAO
				.getAllMileStoneHistory(mileStoneId));

	}

	@Override
	public Map<String, Object> getBillingReportsOnSearch(Integer startIndex,
			Integer endIndex, String status, String from, String to,
			String searchText, String client, String dateSelection) {

		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						ProjectServiceImpl.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Map<String, Object> map = resourceManagementDAO
				.getBillingReportsOnSearch(startIndex, endIndex, status,
						dateRange, searchText, client);
		Integer size = (Integer) map.get("size");
		List<Milestone> milestones = (List<Milestone>) map.get("reports");

		if (size < endIndex) {
			map.put("reports", projectBuilder.createBillingDTO(milestones
					.subList(startIndex, size)));
		} else {
			map.put("reports", projectBuilder.createBillingDTO(milestones
					.subList(startIndex, endIndex)));
		}
		/*
		 * map.put("reports", projectBuilder .createBillingDTO((List<Milestone>)
		 * map.get("reports")));
		 */
		return map;
	}

	@Override
	public Map<String, Object> getProjectsOfClients(Integer startIndex,
			Integer endIndex, String client, String type) {
		Map<String, Object> map = resourceManagementDAO
				.getProjectDetailsEmployeeCountAdmin(startIndex, endIndex,
						client, type);
		map.put("projectList", projectBuilder
				.createProjectActiveListcount((List<AllocationDetailsDTO>) map
						.get("projectList")));
		return map;
	}

	@Override
	public void closeProject(Long projectId) {
		Project project = resourceManagementDAO
				.findBy(Project.class, projectId);
		Project oldProject = null;
		try {
			oldProject = (Project) project.clone();
		} catch (CloneNotSupportedException ex) {
			java.util.logging.Logger.getLogger(
					ProjectServiceImpl.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		project.setStatus(ProjectStatus.CLOSED);
		resourceManagementDAO.update(project);
		List<Audit> projectAudit = auditBuilder.projectUpdateAudit(project,
				project.getId(), oldProject, "PROJECT", "CLOSED");

		for (Audit audit : projectAudit) {
			resourceManagementDAO.save(audit);
		}
		projectManagementMailConfiguration.closeProject(projectId);

	}

	@Override
	public ByteArrayOutputStream exportCsv(Long id,
			HttpServletResponse response, int startIndex, int endIndex,
			String empName, String technology, String isBillable,
			String isAllocated, String startdate, String lastdate,
			List<String> departmentNames, String dateSelection) {

		Employee loggedEmployee = resourceManagementDAO.findBy(Employee.class,
				id);

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			Map<Employee, Project> map = new HashMap<Employee, Project>();
			Workbook workbook = new HSSFWorkbook();
			Sheet sheet = workbook.createSheet();
			Row row1 = sheet.createRow(0);

			Font font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontHeightInPoints((short) 10);
			CellStyle style = workbook.createCellStyle();

			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setFont(font);

			Cell cell0 = row1.createCell(0);
			cell0.setCellValue("ID");
			cell0.setCellStyle(style);

			Cell cell1 = row1.createCell(1);
			cell1.setCellValue("Name");
			cell1.setCellStyle(style);

			Cell cell2 = row1.createCell(2);
			cell2.setCellValue("Designation");
			cell2.setCellStyle(style);

			Cell cell3 = row1.createCell(3);
			cell3.setCellValue("Department");
			cell3.setCellStyle(style);

			// Cell cell4 = row1.createCell(4);
			// cell4.setCellValue("Project Allocation(%)");
			// cell4.setCellStyle(style);
			Cell cell4 = row1.createCell(4);
			cell4.setCellValue("Project");
			cell4.setCellStyle(style);

			Cell cell5 = row1.createCell(5);
			cell5.setCellValue("Manager");
			cell5.setCellStyle(style);

			Cell cell6 = row1.createCell(6);
			cell6.setCellValue("Delivery Manager");
			cell6.setCellStyle(style);

			Cell cell7 = row1.createCell(7);
			cell7.setCellValue("Status");
			cell7.setCellStyle(style);

			Cell cell8 = row1.createCell(8);
			cell8.setCellValue("Start Date");
			cell8.setCellStyle(style);

			Cell cell9 = row1.createCell(9);
			cell9.setCellValue("End Date ");
			cell9.setCellStyle(style);

			Cell cell10 = row1.createCell(10);
			cell10.setCellValue("Allocation(%) ");
			cell10.setCellStyle(style);

			Cell cell11 = row1.createCell(11);
			cell11.setCellValue("Billing Status");
			cell11.setCellStyle(style);

			Cell cell12 = row1.createCell(12);
			cell12.setCellValue("Allocation Status");
			cell12.setCellStyle(style);

			Date fromDate = null;
			Date toDate = null;

			if (dateSelection.equalsIgnoreCase("Custom")) {
				try {
					fromDate = DateParser.toDate(startdate);
					toDate = DateParser.toDate(lastdate);
				} catch (ParseException ex) {
					java.util.logging.Logger.getLogger(
							ProjectServiceImpl.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			} else {
				Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
				fromDate = dateMap.get("startDate");
				toDate = dateMap.get("endDate");

			}
			List<String> employeeStatus = null;

			if (isAllocated != null) {
				employeeStatus = new ArrayList<String>();
				String statusArray[] = isAllocated.split(",");

				for (String str : statusArray) {
					if (!str.isEmpty()) {
						employeeStatus.add(str);

					}
				}
			}
			Permission totalList = dao.checkForPermission(
					"Employee Allocation", loggedEmployee);
			Permission individual = dao.checkForPermission(
					"Individual Employee Allocation", loggedEmployee);
			Permission hierarchy = dao.checkForPermission(
					"Hierarchy Employee Allocation", loggedEmployee);

			DateRange date = new DateRange(fromDate, toDate);

			Map employeeList = null;
			List<Employee> employeelist = null;
			if (totalList.getView() && !hierarchy.getView()
					&& !individual.getView()) {

				employeeList = resourceManagementDAO
						.searchByEmployeeNameForAdmin(id, startIndex, endIndex,
								empName, technology, isBillable,
								employeeStatus, date, departmentNames);
				employeelist = (List<Employee>) employeeList
						.get("employeeList");
				Collections.sort(employeelist);

			} else if (totalList.getView() && hierarchy.getView()
					&& !individual.getView()) {
				List<Long> idlist = mangerUnderManager(id);
				employeeList = resourceManagementDAO
						.searchByEmployeeNameForManager(idlist, startIndex,
								endIndex, empName, technology, isBillable,
								employeeStatus, date);
				employeelist = (List<Employee>) employeeList
						.get("employeeList");
				Collections.sort(employeelist);

			}
			int rowIndex = 1;
			List<Long> employeeIds = new ArrayList<Long>();
			for (Employee emp : employeelist) {
				employeeIds.add(emp.getEmployeeId());
			}

			for (Employee employee : employeelist) {
				Map<Project, AllocationDetails> map1 = resourceManagementDAO
						.getAllProjects_UnderEmployee(employee.getEmployeeId(),
								isBillable, employeeStatus, date);
				try {
					for (Map.Entry<Project, AllocationDetails> entry : map1
							.entrySet()) {

						Row row = sheet.createRow(rowIndex++);

						Cell cel0 = row.createCell(0);
						cel0.setCellValue(employee.getEmployeeId());

						Cell cel1 = row.createCell(1);
						cel1.setCellValue(employee.getFullName());

						Cell cel2 = row.createCell(2);
						cel2.setCellValue(employee.getDesignation());

						Cell cel3 = row.createCell(3);
						cel3.setCellValue(employee.getDepartmentName());

						Project projects = entry.getKey();
						AllocationDetails alocation = entry.getValue();
						Cell cel4 = row.createCell(4);
						cel4.setCellValue(projects.getProjectName() != null ? projects
								.getProjectName() : "N/A");
						Cell cel5 = row.createCell(5);
						cel5.setCellValue(projects.getProjectManager() != null ? projects
								.getProjectManager().getFullName() : "N/A");

						Cell cel6 = row.createCell(6);
						cel6.setCellValue(projects.getProjectManager() != null ? projects
								.getProjectManager().getManager().getFullName()
								: "N/A");

						Cell cel7 = row.createCell(7);
						cel7.setCellValue(projects.getStatus() != null ? projects
								.getStatus().getProjectStatus() : "N/A");

						Cell cel8 = row.createCell(8);
						cel8.setCellValue(alocation.getPeriod() != null ? alocation
								.getPeriod().getMinimum().toString()
								: "N/A");

						Cell cel9 = row.createCell(9);
						cel9.setCellValue(alocation.getPeriod() != null ? alocation
								.getPeriod().getMaximum().toString()
								: "N/A");

						Cell cel10 = row.createCell(10);
						cel10.setCellValue(alocation.getPercentage() != null ? alocation
								.getPercentage().toString() : "N/A");

						Cell cel11 = row.createCell(11);
						cel11.setCellValue(alocation.getBillable() != null ? (alocation
								.getBillable().equals(true) ? "BILLABLE"
								: "NONBILLABLE") : "N/A");

						Cell cel12 = row.createCell(12);
						cel12.setCellValue(alocation.getIsAllocated() != null ? (alocation
								.getIsAllocated().equals(true) ? "ALLOCATED"
								: "DEALLOCATED") : "N/A");

					}
					sheet.autoSizeColumn(0);
				} catch (NullPointerException npe) {
				}

			}
			workbook.write(bos);
			bos.flush();
			bos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bos;
	}

	@Override
	public ClientDTO getClientInfo(Long id) {
		return projectBuilder.createClientDTO(dao.findBy(Client.class, id));

	}

	@Override
	public List<ClientDTO> getclients() {

		return projectBuilder.createClientDTOsList(resourceManagementDAO
				.getclients());
	}

	@Override
	public Double getAllMilestonePercentageCount(Long projectId) {
		Project project = resourceManagementDAO
				.findBy(Project.class, projectId);
		return resourceManagementDAO.getAllMilestonePercentageCount(project);
	}

	@Override
	public Double getRaisedCRPercentage(Long crId) {

		ChangeRequest changeRequest = resourceManagementDAO.findBy(
				ChangeRequest.class, crId);
		return resourceManagementDAO.getRaisedCrPercentage(changeRequest, null);
	}

	@Override
	public Map<String, Object> getProjectNumbers(Long projectId) {
		try {
			Thread.sleep(400);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Object> numbersMap = new HashMap<String, Object>();

		List<ProjectNumbers> projectNumbersList = resourceManagementDAO
				.getProjectNumbers(resourceManagementDAO.findBy(Project.class,
						projectId));
		if (projectNumbersList.size() > 0) {

			Set<ProjectNumbersDTO> projectNumbers = (projectNumbersList != null) ? new HashSet<ProjectNumbersDTO>()
					: null;
			List<ProjectNumbersDTO> changeRequestNumbers = (projectNumbersList != null) ? new ArrayList<ProjectNumbersDTO>()
					: null;

			for (ProjectNumbers numbers : projectNumbersList) {
				if (numbers.getChangeRequest() != null) {
					changeRequestNumbers.add(projectNumbersBuilder
							.toDto(numbers));
				} else {
					projectNumbers.add(projectNumbersBuilder.toDto(numbers));
				}

			}
			numbersMap.put("projectNumbers", projectNumbers);
			numbersMap.put("changeRequestNumbers", changeRequestNumbers);
		}

		return numbersMap;
	}

	@Override
	public void addProjectNumbers(ProjectNumbersDTO dto) {
		ProjectNumbers numbers = projectNumbersBuilder.toEntity(dto);
		ChangeRequest changeRequest = (numbers != null) ? numbers
				.getChangeRequest() : null;
		if (changeRequest != null) {
			changeRequest.setNumbersStatus(Boolean.TRUE);
		}

		dao.save(numbers);
	}

	// The method is for to make the people who are allocated for milestone as
	// billable and
	// the remaining people in the project will be made as billable false
	// and will be returned to edit of the milestone
	@Override
	public List<ReportDTO> getPeopleUnderMilestone(Long milestoneId,
			Long projectId) {
		Milestone milestone = resourceManagementDAO.findBy(Milestone.class,
				milestoneId);
		Project project = resourceManagementDAO
				.findBy(Project.class, projectId);

		// here we are getting all employees of project (Irrespective of
		// allocated and deallocated)
		List<AllocationDetails> allocationDetailsList = resourceManagementDAO
				.getMilestonePeople(projectId, milestone.getPlanedDate()
						.toString("dd/MM/yyyy"));

		List<ReportDTO> reportDTOs = new ArrayList<ReportDTO>();
		for (Milestone milestone1 : project.getMilestones()) {

			if (milestone1.getId() == milestoneId) {
				ReportDTO dTO = null;

				for (AllocationDetails details : allocationDetailsList) {

					dTO = new ReportDTO();
					dTO.setEmployeeId(details.getEmployee().getEmployeeId());
					dTO.setEmpFirstName(details.getEmployee().getFirstName());
					dTO.setEmpLastName(details.getEmployee().getLastName());
					dTO.setProjectName(project.getProjectName());
					dTO.setProjectId(project.getId());
					dTO.setDepartment(details.getEmployee().getDepartmentName());
					dTO.setUserName(details.getEmployee().getFullName());
					dTO.setDesigination(details.getEmployee().getDesignation());
					dTO.setAllocation(details.getPercentage().toString("#0",
							false));
					dTO.setIsAllocated(details.getIsAllocated());
					for (MilestonePeople milestonePeople : milestone
							.getMilestonePeople()) {

						if (details.getEmployee().getEmployeeId() == milestonePeople
								.getEmployee().getEmployeeId()) {
							dTO.setStartDate(milestonePeople.getStartDate() != null ? milestonePeople
									.getStartDate().toString("dd/MM/yyyy")
									: null);
							dTO.setEndDate(milestonePeople.getEndDate() != null ? milestonePeople
									.getEndDate().toString("dd/MM/yyyy") : null);
							dTO.setComments(milestonePeople.getComments());
							dTO.setCount(milestonePeople.getCount());
							dTO.setMonthWorkingDays(milestonePeople
									.getMonthWorkingDays());
							dTO.setHolidays(milestonePeople.getHolidays());
							dTO.setLeaves(milestonePeople.getLeaves());
							dTO.setTotalDays(milestonePeople.getTotalDays());
							dTO.setHours(milestonePeople.getHours());
							dTO.setTotalValue(milestonePeople.getTotalValue());
							dTO.setRole(milestonePeople.getRole());
							dTO.setBillable(milestonePeople.getIsBillable());
							break;

						}

					}
					reportDTOs.add(dTO);
				}

			}
		}
		return reportDTOs;
	}

	@Override
	public List<Currency> getCurrencyLookUp() {

		List<Currency> currencies = dao.get(Currency.class);
		return currencies;
	}

	@Override
	public void addCR(ChangeRequestDTO changeRequestDTO) {
		ChangeRequest changeRequest = changeRequestBuilder
				.toEntity(changeRequestDTO);
		dao.save(changeRequest);
	}

	@Override
	public Map<String, Object> getChangeRequestDTOList(Long projectId,
			Integer firstIndex, Integer endIndex) {

		Map<String, Object> map = resourceManagementDAO
				.getChangeRequestDTOList(projectId, firstIndex, endIndex);
		Integer size = (Integer) map.get("size");
		List<ChangeRequest> changeRequests = (List<ChangeRequest>) map
				.get("list");

		if (size < endIndex) {
			map.put("list", changeRequestBuilder.toListDto(changeRequests
					.subList(firstIndex, size)));
		} else {
			map.put("list", changeRequestBuilder.toListDto(changeRequests
					.subList(firstIndex, endIndex)));
		}

		return map;
	}

	@Override
	public void deleteCR(Long crId) {
		dao.delete(dao.findBy(ChangeRequest.class, crId));
	}

	@Override
	public List<ChangeRequestDTO> getCRlookupForProjectNumbers(Long ProjectId) {
		return resourceManagementDAO.getCRlookupForProjectNumbers(ProjectId);
	}

	@Override
	public void updateProjectNumbers(ProjectNumbersDTO dto) {

		Map<String, Object> map = invoiceDao.getProjectInvoices(
				dto.getProjectId(), 0, 20);
		List<Invoice> invoiceList = (List<Invoice>) map.get("list");

		if (invoiceList.isEmpty()) {
			ProjectNumbers numbers = projectNumbersBuilder.toEntity(dto);
			dao.update(numbers);
			List<ProjectNumbers> numbers2 = resourceManagementDAO
					.getProjectNumbers(resourceManagementDAO.findBy(
							Project.class, dto.getProjectId()));
			for (ProjectNumbers number : numbers2) {
				if (number.getChangeRequest() != null) {
					number.setCurrency(dto.getCurrency());
					dao.update(number);
				}

			}

		} else {
			throw new ProjectNumbersUpdationException(
					"Cant Update project Numbers already Invoices are  raised for this project ");
		}

	}

	@Override
	public void updateCRNumbers(ProjectNumbersDTO dto) {
		resourceManagementDAO.updateCRNumbers(dto);

	}

	@Override
	public List<ChangeRequestDTO> getCRListForMilestone(Long projectId) {
		return resourceManagementDAO.getCRListForMilestone(projectId);
	}

	@Override
	public void updateChangeRequest(ChangeRequestDTO changeRequestDto) {
		ChangeRequest changeRequest = changeRequestBuilder
				.toEntity(changeRequestDto);
		resourceManagementDAO.update(changeRequest);

	}

	// Project history
	@Override
	public Map<String, Object> getProjectHistory(Long projectId) {

		// List<ProjectAudit> projectAudits =
		// resourceManagementDAO.getProjectHistory(projectId);
		// List<AllocationDetailsAudit> detailsAudits =
		// resourceManagementDAO.getAllocationHistory(projectId);
		// return projectAuditBuilder.convertToDTO(projectAudits,
		// detailsAudits);
		Map<String, List<Audit>> map = resourceManagementDAO
				.getProjectAudit(projectId);

		return projectAuditBuilder.ToProjectAuditDTO(map);
	}

	// This method returns all the Manager's id who are directly or indirectly
	// reporting to logged in Manager
	@Override
	public List<Long> mangerUnderManager(Long empid) {

		if (checkEmployeeId == 0L) {
			checkEmployeeId = empid;
		}
		List<Employee> allmanager = resourceManagementDAO
				.mangerUnderManager(empid);
		List<Long> managerId = new ArrayList<Long>();
		managerId.add(empid);
		if (allmanager.size() > 0) {
			for (Employee employee : allmanager) {

				if (!checkEmployeeId.equals(employee.getEmployeeId())) {

					List<Long> submanager = mangerUnderManager(employee
							.getEmployeeId());
					managerId.addAll(submanager);
				}

			}
		}
		return managerId;
	}

	@Override
	public Map<String, Object> getWorkingDaysOfEmployeeForDetails(
			String formDate, String toDate, Long empId) {

		Map<String, Object> details = new HashMap<String, Object>();

		// THIS CODE SNIPPET CALCULATES TOTAL WORKING DAYS IS SELECTED DATERANGE
		Date date = null;
		Date date2 = null;

		try {
			date = DateParser.toDate(formDate);
			date2 = DateParser.toDate(toDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(date.getJavaDate());

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(date2.getJavaDate());

		// Here we are ignoring saturdays and sundays and calculating other days
		int workDays = 0;
		while (!startCal.after(endCal)) {
			int day = startCal.get(Calendar.DAY_OF_WEEK);
			if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)) {
				workDays++;
			}
			startCal.add(Calendar.DATE, 1);
		}

		// WORKING DAYS CODE ENDS HERE//

		// THIS CODE GETS HOLIDAYS LIST IN SELECTED DATE RANGE//

		List<Holidays> holidaysList = resourceManagementDAO
				.getHolidaysBetweenDates(date, date2);

		Integer holidays = (!holidaysList.isEmpty()) ? holidaysList.size() : 0;

		// Here we are storing all holiday dates into list this is used when
		// calculating emp attendance
		List<Date> holidayDates = new ArrayList<Date>();
		for (Holidays holidays2 : holidaysList) {
			holidayDates.add(holidays2.getDate());
		}

		// HOLIDAYS LIST CODE ENDS HERE//

		// HERE BIOATTENDANCE OF EMPLOYEE STARTS//

		Employee employee = dao.findBy(Employee.class, empId);

		// we are getting all data when employee was ABSENT
		List<BioAttendance> bioAttendances = resourceManagementDAO
				.getBioAttendanceOfEmployeeBetweenDateRange(date, date2,
						employee);

		// Here We are removing Duplicate value if Attandance date is duplicate
		Map<Date, BioAttendance> map = new LinkedHashMap<>();
		for (BioAttendance bio : bioAttendances) {
			map.put(bio.getAttendanceDate(), bio);
		}
		bioAttendances.clear();
		bioAttendances.addAll(map.values());

		// In Bioattendance for LEAVES,HOLIDAYS,NON-WORKING days status is
		// ABSENT so we need to ignore nonworking days and holidays and only
		// calculate dates on which employee was ABSENT(Irrespective of leave
		// type thus following code as follows )
		int attendanceCount = 0;
		Calendar attendanceCalendar = Calendar.getInstance();
		for (BioAttendance attendance : bioAttendances) {
			Boolean holidayFlag = holidayDates.contains(attendance
					.getAttendanceDate());
			attendanceCalendar.setTime(attendance.getAttendanceDate()
					.getJavaDate());
			int attendanceDay = attendanceCalendar.get(Calendar.DAY_OF_WEEK);
			if ((attendanceDay != Calendar.SATURDAY)
					&& (attendanceDay != Calendar.SUNDAY) && !holidayFlag) {
				attendanceCount++;
			}
		}

		// HERE BIOATTENDANCE OF EMPLOYEE CODE ENDS//

		Integer daysWithOutHolidays = workDays - holidays;
		Integer totalDays = daysWithOutHolidays - attendanceCount;
		Integer hours = 8;
		Integer totalValue = hours * totalDays;

		details.put("workingDays", workDays);
		details.put("holidays", holidays);
		details.put("Leaves", attendanceCount);
		details.put("totalDays", totalDays);
		details.put("employeeId", empId);
		details.put("hours", hours);
		details.put("totalValue", totalValue);

		return details;
	}

	/*
	 * @Override public String getMilestoneNumber(Long projectId) {
	 * 
	 * Project project = dao.findBy(Project.class, projectId);
	 * 
	 * List<Milestone> milestones = dao.getAllOfProperty(Milestone.class,
	 * "project", project); int count = milestones.size();
	 * 
	 * if (count == 0) { count = 1; } else { count++; } String milestoneNumber =
	 * generateMilestoneNumber(project, count); return milestoneNumber; }
	 * 
	 * @Override public String generateMilestoneNumber(Project project, Integer
	 * count) {
	 * 
	 * StringBuilder builder = new StringBuilder(); builder.append("RB");
	 * 
	 * int month = new Date().getMonthOfYear().getValue() + 1;
	 * 
	 * builder.append(String.format("%02d", month)).append( String.valueOf(new
	 * Date().getYearOfEra().getValue()).substring( 2, 4));
	 * 
	 * builder.append("MS");
	 * 
	 * builder.append(String.format("%03d", project.getId()));
	 * 
	 * builder.append(String.format("%03d", count)); String
	 * generatedMilestoneNumber = builder.toString();
	 * 
	 * return (milestoneNumberAlreadyExists(generatedMilestoneNumber)) ?
	 * generateMilestoneNumber( project, count + 1) : generatedMilestoneNumber;
	 * }
	 * 
	 * @Override public Boolean milestoneNumberAlreadyExists(String
	 * milestoneNumber) { Milestone milestone =
	 * dao.findByUniqueProperty(Milestone.class, "milestoneNumber",
	 * milestoneNumber); return (milestone != null) ? true : false; }
	 */

	@Override
	public String getNextMilestoneNumber(Long projectId) {
		
		logger.warn("in service");

		Project project = dao.findBy(Project.class, projectId);

		StringBuilder builder = new StringBuilder();

		String patternforInvoice = invoicedaoImpl.getInvoicePattern();
		
	

		logger.warn("service" + patternforInvoice);

		if (patternforInvoice != null) {
			logger.warn("patternforInvoice" + patternforInvoice);
			builder.append(patternforInvoice);
		}
		//builder.append("RB");

		int month = new Date().getMonthOfYear().getValue() + 1;

		builder.append(String.format("%02d", month)).append(
				String.valueOf(new Date().getYearOfEra().getValue()).substring(
						2, 4));

		builder.append("MS");

		builder.append(String.format("%03d", project.getId()));

		Integer milestoneSize = dao.getAllOfProperty(Milestone.class,
				"project", project).size();

		if (milestoneSize == 0) {
			builder.append("001");
		} else {
			Milestone milestone = resourceManagementDAO
					.getrecentlyInsertedMilestone(project);

			if (milestone.getMilestoneNumber() != null) {
				String milestoneNumber = milestone.getMilestoneNumber();
				String lastThree = milestoneNumber.substring(milestoneNumber
						.length() - 3);
				Integer lastThreeInteger = Integer.valueOf(lastThree);
				Integer incrementedNumber = lastThreeInteger + 1;
				String stringNumber = incrementedNumber.toString();
				if (stringNumber.length() == 1) {
					builder.append("00" + stringNumber);
				} else if (stringNumber.length() == 2) {
					builder.append("0" + stringNumber);
				} else {
					builder.append(stringNumber);
				}
			} else {

				// here we are writing this because for projects already
				// milestone are raised for those milestones there will be no
				// milestone number so in such case we are writing this
				builder.append(String.format("%03d", milestoneSize + 1));

			}
		}

		return builder.toString();
	}

	@Override
	public List<Project> getProjectWhoseEndDateisInNextFiveDays() {
		return resourceManagementDAO.getProjectWhoseEndDateisInNextFiveDays();
	}

	@Override
	public void addProjectProposal(ProjectProposalsDTO dto) {

		ProjectProposals projectProposals = null;
		if (dto != null) {
			projectProposals = new ProjectProposals();
			projectProposals.setId(dto.getId());
			projectProposals.setPost(dto.getPost());
			projectProposals.setPostedBy(dao
					.findBy(Employee.class, securityUtils
							.getLoggedEmployeeIdforSecurityContextHolder()));
			projectProposals.setPostedOn(new Second());
			projectProposals.setProject(dao.findBy(Project.class,
					dto.getProjectId()));
			dao.save(projectProposals);
		}

	}

	@Override
	public List<ProjectProposalsDTO> getProjectProposals(Long projectId) {

		List<ProjectProposals> projectProposalsList = dao.getAllOfProperty(
				ProjectProposals.class, "project",
				dao.findBy(Project.class, projectId));

		if (projectProposalsList != null) {
			Collections.sort(projectProposalsList,
					new Comparator<ProjectProposals>() {
						@Override
						public int compare(ProjectProposals o1,
								ProjectProposals o2) {
							return o2.getId().compareTo(o1.getId());
						}
					});

		}

		List<ProjectProposalsDTO> projectProposalsDTOList = null;
		if (projectProposalsList != null) {
			projectProposalsDTOList = new ArrayList<ProjectProposalsDTO>();
			for (ProjectProposals proposal : projectProposalsList) {
				ProjectProposalsDTO dto = new ProjectProposalsDTO();
				dto.setId(proposal.getId());
				dto.setPost(proposal.getPost());
				dto.setPostedBy(proposal.getPostedBy().getFullName());
				dto.setProjectId(proposal.getProject().getId());
				dto.setProjectName(proposal.getProject().getProjectName());
				dto.setPostedOn(proposal.getPostedOn().toString(
						"dd-MMM-yyyy hh:mm a"));
				projectProposalsDTOList.add(dto);
			}

		}

		return projectProposalsDTOList;
	}

	@Override
	public void reOpenMilestone(Long milestoneId) {

		Milestone milestone = dao.findBy(Milestone.class, milestoneId);
		milestone.setClosed(Boolean.FALSE);
		resourceManagementDAO.update(milestone);

		MilestoneAudit milestoneAudit = mileStoneBuilder
				.convertMileStoneToMilestoneAudit(milestone, milestone.getId(),
						"REOPENED");
		dao.save(milestoneAudit);
	}

	@Override
	public void projectRequestMailId(String cc, String bcc) {

		ProjectRequestMail mailDetails = resourceManagementDAO
				.getProjectRequestCCandBCC();
		if (mailDetails != null) {
			mailDetails.setCc(cc);
			mailDetails.setBcc(bcc);
			mailDetails.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			mailDetails.setCreatedDate(new Second());
			resourceManagementDAO.update(mailDetails);
		}

	}

	@Override
	public ProjectRequestMailDTO getProjectRequestMailIds() {

		ProjectRequestMail projectRequestMail = resourceManagementDAO
				.getProjectRequestCCandBCC();
		ProjectRequestMailDTO projectRequestMailDTO = null;
		if (projectRequestMail != null) {
			projectRequestMailDTO = new ProjectRequestMailDTO();
			projectRequestMailDTO.setId(projectRequestMail.getId());
			projectRequestMailDTO.setCc(projectRequestMail.getCc());
			projectRequestMailDTO.setBcc(projectRequestMail.getBcc());
		}
		return projectRequestMailDTO;
	}

	@Override
	public List<ProjectInitiationChecklistDTO> getInitiationCheckList() {
		List<ProjectCheckList> checkLists = resourceManagementDAO
				.getChecklist();
		return projectRequestBuilder.getProjectChecklistDTO(checkLists);
	}

	@Override
	public ByteArrayOutputStream downloadInitationCheckList(
			Long projectRequestId) throws IOException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int rowIndex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Checkpoint");
		cell0.setCellStyle(cellStyle);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("PM Review(Yes/No/N/A)");
		cell1.setCellStyle(cellStyle);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Remarks/Details");
		cell2.setCellStyle(cellStyle);

		ProjectRequest request = dao.findBy(ProjectRequest.class,
				projectRequestId);

		Set<ProjectInitiationChecklist> checkLists = new HashSet<ProjectInitiationChecklist>();

		if (request.getChecklist().size() > 0) {
			checkLists = request.getChecklist();

			for (ProjectInitiationChecklist dto : checkLists) {

				Row row = sheet.createRow(rowIndex++);

				Cell cel0 = row.createCell(0);
				cel0.setCellValue(dto.getChecklist().getName());

				Cell cel1 = row.createCell(1);
				cel1.setCellValue(dto.getAnswer());

				Cell cel2 = row.createCell(2);

				if (dto.getComments().isEmpty()) {
					cel2.setCellValue("N/A");
				}

				else {
					cel2.setCellValue(dto.getComments());
				}

				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);
				sheet.autoSizeColumn(2);

			}
		}

		else {
			try {
				throw new NoCheckList(
						"Check list is not available for particular project");
			} catch (NoCheckList e) {

			}
		}

		workbook.write(byteArrayOutputStream);
		byteArrayOutputStream.flush();
		byteArrayOutputStream.close();

		return byteArrayOutputStream;
	}

	@Override
	public List<ProjectRequestAuditDTO> getAuditForProjectRequest(Long id) {

		return projectAuditBuilder.getProjReqAudits(resourceManagementDAO
				.getAuditForProjectRequest(id));
	}

	@Override
	public Boolean checkClientOrg(String organization) {

		Boolean organizationexist = resourceManagementDAO
				.checkClientOrg(organization);
		return organizationexist;
	}

	@Override
	public Map<String, Object> getAllProjectRequestList(Integer firstIndex,
			Integer endIndex, String multiSearch) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> projectRequestDtoList = null;
		Long empId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, empId);

		Boolean totalListFlag = false;
		Boolean individualListFlag = false;

		Permission totalList = dao.checkForPermission(
				"Project Creation Requests", employee);
		Permission individualList = dao.checkForPermission(
				"Individual Project Request List", employee);

		if (totalList.getView() && !individualList.getView())
			totalListFlag = true;

		else if (individualList.getView() && totalList.getView())
			individualListFlag = true;

		if (totalListFlag) {
			projectRequestDtoList = resourceManagementDAO
					.getAllProjectRequests(firstIndex, endIndex, multiSearch);

		} else if (individualListFlag) {
			List<Long> employeeIds = mangerUnderManager(empId);
			projectRequestDtoList = resourceManagementDAO
					.getAllProjectRequestFor(employeeIds, firstIndex, endIndex,
							multiSearch);
		}

		List<ProjectRequestDTO> dto = null;
		if (projectRequestDtoList != null) {

			if (totalListFlag) {
				dto = projectRequestBuilder
						.getAllProjectRequestsDTO((List<ProjectRequest>) projectRequestDtoList
								.get("projectRequestList"));
			} else if (individualListFlag) {
				dto = projectRequestBuilder
						.getAllProjectRequestsDTO((List<ProjectRequest>) projectRequestDtoList
								.get("projectRequestList"));
			}
		}

		map.put("projectrequestList", dto);
		map.put("projectRequestListSize", projectRequestDtoList.get("size"));

		return map;
	}

	@Override
	public List<Platform> getAllPlatforms() {
		List<Platform> platList = dao.get(Platform.class);
		return platList;
	}

	@Override
	public List<Domain> getAllDomains() {
		List<Domain> domainList = dao.get(Domain.class);
		return domainList;
	}

	@Override
	public ByteArrayOutputStream exportEmployeeAllocation(Long loggedInEmpId,
			int firstIndex, int endIndex, String employeeName,
			String technology, String isBillable, String isAllocated,
			String startdate, String enddate, List<String> departmentNames,
			String dateSelection) throws IOException {
		Employee employee = resourceManagementDAO.findBy(Employee.class,
				loggedInEmpId);

		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(startdate);
				toDate = DateParser.toDate(enddate);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(
						ProjectServiceImpl.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}
		List<String> employeeStatus = null;

		if (isAllocated != null) {
			employeeStatus = new ArrayList<String>();
			String statusArray[] = isAllocated.split(",");

			for (String str : statusArray) {
				if (!str.isEmpty()) {
					employeeStatus.add(str);

				}
			}
		}

		Permission totalList = dao.checkForPermission("Employee Allocation",
				employee);
		Permission individual = dao.checkForPermission(
				"Individual Employee Allocation", employee);
		Permission hierarchy = dao.checkForPermission(
				"Hierarchy Employee Allocation", employee);

		DateRange date = new DateRange(fromDate, toDate);

		Map employeeDTOList = null;
		List<Employee> employeelist = null;

		// for admin role
		if (totalList.getView() && !hierarchy.getView()
				&& !individual.getView()) {

			try {
				employeeDTOList = resourceManagementDAO
						.searchByEmployeeNameForAdmin(loggedInEmpId,
								firstIndex, endIndex, employeeName, technology,
								isBillable, employeeStatus, date,
								departmentNames);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			employeelist = (List<Employee>) employeeDTOList.get("employeeList");
			Collections.sort(employeelist);

		}

		Map<String, Object> map = new HashMap<String, Object>();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int rowindex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Id");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Name");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Designation");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Department");
		cell3.setCellStyle(style);
		
			
		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Experience");
		cell4.setCellStyle(style);
			
		Cell cell5 = row1.createCell(5);
		cell5.setCellValue(" Project Allocation");
		cell5.setCellStyle(style);
			
		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Allocation");
		cell6.setCellStyle(style);
			
		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("Status");
		cell7.setCellStyle(style);
		
		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("Country");
		cell8.setCellStyle(style);
		
		Cell cell9 = row1.createCell(9);
		cell9.setCellValue("Technology");
		cell9.setCellStyle(style);
			
		

		/*
		 * Cell cell4 = row1.createCell(4); cell4.setCellValue("Percent");
		 * cell4.setCellStyle(style);
		 * 
		 * Cell cell5 = row1.createCell(5); cell5.setCellValue("Manager");
		 * cell5.setCellStyle(style);
		 * 
		 * Cell cell6 = row1.createCell(6);
		 * cell6.setCellValue("Delivery manager"); cell6.setCellStyle(style);
		 * 
		 * Cell cell7 = row1.createCell(7);
		 * cell7.setCellValue("Billing Status"); cell7.setCellStyle(style);
		 * 
		 * Cell cell8 = row1.createCell(8);
		 * cell8.setCellValue("Allocation Status"); cell8.setCellStyle(style);
		 */

		for (Employee employees : employeelist) {
			/*
			 * Boolean benchFlag = true; Boolean allocate = true;
			 */

			
			  Map<Project, AllocationDetails> map1 = resourceManagementDAO
			  .getAllProjects_UnderEmployee(employees.getEmployeeId(),
			  isBillable, employeeStatus, date);

			/*
			 * for (Map.Entry<Project, AllocationDetails> entry :
			 * map1.entrySet()) { Row under = sheet.createRow(rowindex++);
			 * benchFlag = false; allocate = false; AllocationDetails alocation
			 * = entry.getValue();
			 * 
			 * if (alocation.getBillable().equals(true)) { Cell cel7 =
			 * under.createCell(7); cel7.setCellValue("ONBENCH");
			 * 
			 * Cell cel8 = under.createCell(8); cel8.setCellValue("ONBENCH"); }
			 * else { Project project=entry.getKey();
			 * 
			 * Cell cel9 = under.createCell(0);
			 * cel9.setCellValue(employees.getEmployeeId());
			 * 
			 * Cell cel10 = under.createCell(1);
			 * cel10.setCellValue(employees.getFullName());
			 * 
			 * Cell cel11 = under.createCell(2);
			 * cel11.setCellValue(employees.getDesignation());
			 * 
			 * Cell cel12 = under.createCell(3);
			 * cel12.setCellValue(employees.getDepartmentName());
			 * 
			 * Cell cel13 = under.createCell(4);
			 * cel13.setCellValue(alocation.getPercentage().toString());
			 * 
			 * Cell cel14 = under.createCell(5);
			 * cel14.setCellValue(project.getProjectManager() .getFullName());
			 * 
			 * Cell cel15 = under.createCell(6);
			 * cel15.setCellValue(project.getProjectManager()
			 * .getManager().getFullName());
			 * 
			 * Cell cel7 = under.createCell(7);
			 * cel7.setCellValue("NONBILLABLE"); Cell cel8 =
			 * under.createCell(8);
			 * cel8.setCellValue(alocation.getIsAllocated().equals(true) ?
			 * "ALLOCATED" : "DEALLOCATED"); }
			 * 
			 * }
			 */
			   Row row = sheet.createRow(rowindex++);
				    Cell cel0 = row.createCell(0);
					cel0.setCellValue(employees.getEmployeeId());
					
                    Cell cel1 = row.createCell(1);
					cel1.setCellValue(employees.getFullName());
					
				   
					Cell cel2 = row.createCell(2);
					cel2.setCellValue(employees.getDesignation());

					Cell cel3 = row.createCell(3);
					cel3.setCellValue(employees.getDepartmentName());
					Double exp =null;
					
						if(employees.getExperience() == null && employees.getCompanyExperience()!=null){
						     exp= employees.getCompanyExperience();
						}
						if(employees.getExperience() != null && employees.getCompanyExperience()==null){
						     exp=employees.getExperience();
						}
						if(employees.getExperience()!=null && employees.getCompanyExperience()!=null){
							  exp = employees.getExperience() + employees.getCompanyExperience();
							
						}
					Cell cel4 = row.createCell(4);
					cel4.setCellValue(exp);
					
	
				

			 for (Map.Entry<Project, AllocationDetails> entry :map1.entrySet()) { 
					 
				  AllocationDetails alocation = entry.getValue();
				   Project project = entry.getKey();

  
		          Cell cel5 = row.createCell(5);
				  cel5.setCellValue(project.getProjectName()!=null? project.getProjectName():"N/A");
				  
				  Cell cel6 = row.createCell(6);
				  cel6.setCellValue(alocation.getPercentage()!=null? alocation.getPercentage().toString():"N/A");
				  
				  Cell cel7 = row.createCell(7);
				  cel7.setCellValue(alocation.getBillable().equals(true)? "Billable" : "Non Billable");
				  
				  
				  

					 System.out.println("id:" + employees.getEmployeeId() +"exp:" + employees.getCompanyExperience() + "per:"+
					alocation.getPercentage().toString() + "pro:"+ alocation.getProject().getProjectName() + "billable:" +alocation.getBillable());
				
			  
			}
			 Cell cel8 = row.createCell(8);
			  cel8.setCellValue(employees.getCountry()!=null?employees.getCountry():"N/A");
			  
			 Cell cel9 = row.createCell(9);
			 cel9.setCellValue(employees.getTechnology()!=null? employees.getTechnology():"N/A");

			/*
			 * Cell cel4 = row.createCell(4); cel4.setCellValue(0);
			 * 
			 * Cell cel5 = row.createCell(5);
			 * cel5.setCellValue(employees.getManager().getFullName());
			 * 
			 * Cell cel6 = row.createCell(6);
			 * cel6.setCellValue(employees.getManager().getManager()
			 * .getFullName());
			 * 
			 * if (benchFlag) { Cell cel7 = row.createCell(7);
			 * cel7.setCellValue("ONBENCH");
			 * 
			 * Cell cel8 = row.createCell(8); cel8.setCellValue("ONBENCH"); }
			 */

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			
			/*
			 * sheet.autoSizeColumn(4); sheet.autoSizeColumn(5);
			 * sheet.autoSizeColumn(6); sheet.autoSizeColumn(7);
			 * sheet.autoSizeColumn(8);
			 */

		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;

	}

	@Override
	public Map<String, Object> getNumbersAndPercentage(Long milestoneid,
			String id) {
		// Creating Map Object
		Map<String, Object> content = new HashMap<String, Object>();
		// loading the Milestone Object
		Milestone milestone = dao.findBy(Milestone.class, milestoneid);
		// storing Project Object for further operations
		Project project = milestone.getProject();
		String projectType = project.getType().getType();
		// Storing invoice Object // here we are getting the Invoice with unique
		// Id
		System.out.println("unique");
		// Invoice invoice = invoiceDao.findByUniqueProperty(Invoice.class,
		// "milsestone", milestone);

		if (projectType.equalsIgnoreCase("FIXEDBID")) {
			// ChangeRequest Object to verify cr or not
			ChangeRequest cr = milestone.getChangeRequest();
			// getting Project Numbers and CR Numbers
			ProjectNumbersDTO projectNumbers = projectNumbersBuilder
					.toDto(resourceManagementDAO.getProjectNumbers(milestone));
			// Previously we are loading through this method
			// Map<String , Object> content =
			// getProjectNumbers(project.getId());

			// putting content into the Map.

			content.put(cr == null ? "projectNumbers" : "changeRequestNumbers",
					projectNumbers);
		} else {

			// Checking Whether invoice raised raised for the milestone or not
			if (!id.equalsIgnoreCase("null")) {
				// if raised we will load bill able resources added while raise
				// invoice.
				// edit retainer milestone resource
				content.put(
						"milestonebillableResources",
						invoiceServiceImpl.getBillableForRetainer(
								Long.valueOf(id), milestoneid));
			} else {

				// while raise invoice
				// if raised we will load bill able resources added while raise
				// invoice.
				content.put(
						"milestonebillableList",
						allocationDetailstService
								.getmilestonebillableresourceswithInvoiceRate(milestoneid));
			}

		}

		// Milestone Raised percentage is load for here
		content.put("percentage", invoiceServiceImpl
				.getAllInvoicePercentageForMilestone(milestoneid));

		return content;
	}

	@Override
	public Map<String, Object> getNonClosedMilestones(Integer fromIndex,
			Integer toIndex) {
		Employee employeeId = resourceManagementDAO.findBy(Employee.class,
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder());

		Map<String, Object> nonClosedMilestoneList = resourceManagementDAO
				.getNonClosedMilestoneList(employeeId, fromIndex, toIndex);

		Integer size = (Integer) nonClosedMilestoneList.get("size");
		List<Milestone> milestones2 = (List<Milestone>) nonClosedMilestoneList
				.get("list");

		List<MilestoneDTO> milestonesList = new ArrayList<MilestoneDTO>();
		if (milestones2 != null) {
			for (Milestone milestone : milestones2) {
				MilestoneDTO milestoneDTO = new MilestoneDTO();
				milestoneDTO.setId(milestone.getId());
				milestoneDTO.setTitle(milestone.getTitle());
				milestoneDTO
						.setProject(milestone.getProject().getProjectName());
				milestoneDTO.setClient(milestone.getProject().getClient()
						.getName());
				milestoneDTO
						.setMilestoneNumber((milestone.getMilestoneNumber() != null ? milestone
								.getMilestoneNumber() : null));
				if (milestone.getActualDate() != null) {
					milestoneDTO.setActualDate(milestone.getActualDate()
							.toString("dd/MM/yyyy"));
				}
				milestoneDTO.setPlanedDate(milestone.getPlanedDate().toString(
						"dd/MM/yyyy"));
				milestoneDTO.setBillable(milestone.isBillable());
				milestoneDTO.setComments(milestone.getComments());
				milestoneDTO.setIsClosed(milestone.isClosed());
				milestoneDTO.setInvoiceStatus(milestone.getInvoiceStatus());
				milestoneDTO.setMilestonePercentage(milestone
						.getMilestonePercentage());
				if (milestone.getChangeRequest() != null) {
					milestoneDTO.setCrId(milestone.getChangeRequest().getId());
					milestoneDTO.setCrName(milestone.getChangeRequest()
							.getName());
				}
				milestoneDTO.setEffort(milestone.getEffort());
				milestonesList.add(milestoneDTO);
			}
		}

		// List<MilestoneDTO> milestoneDTOs = new ArrayList<MilestoneDTO>();
		List<MilestoneDTO> milestoneDTOs = new ArrayList<MilestoneDTO>();
		if (size < toIndex) {
			milestoneDTOs = milestonesList.subList(fromIndex, size);
		} else {
			milestoneDTOs = milestonesList.subList(fromIndex, toIndex);
		}

		nonClosedMilestoneList.put("list", milestoneDTOs);
		nonClosedMilestoneList.put("size", nonClosedMilestoneList.get("size"));

		return nonClosedMilestoneList;
	}

	@Override
	public List<ProjectDTO> getAllActiveProjects_UnderClient(Long clientId) {

		List<ProjectDTO> projectDTOsList = null;

		projectDTOsList = projectBuilder
				.createProjectDTOList(resourceManagementDAO
						.getAllActiveProjects_UnderClient(clientId));

		return projectDTOsList;
	}

	@Override
	public Map<String, Object> getEmployeeProjectslist(Long employeeid,
			String projectStatus, String type, Integer firstIndex,
			Integer endIndex) {

		Employee employee = resourceManagementDAO.findBy(Employee.class,
				employeeid);
		Map<String, Object> projectDTOList = resourceManagementDAO
				.getEmployeeProjectslist(employeeid, projectStatus, type,
						firstIndex, endIndex);

		Map<String, Object> map = new HashMap<String, Object>();
		if (projectDTOList != null) {
			List<ProjectDTO> pdtos = projectBuilder
					.createProjectActiveListcount((List<AllocationDetailsDTO>) projectDTOList
							.get("projectList"));
			map.put("Projsize", projectDTOList.get("size"));
			map.put("Projs", pdtos);
		}

		return map;

	}

	@Override
	public void reOpenRaisedInvoiceMilestone(Long milestoneId) {

		Milestone milestone = dao.findBy(Milestone.class, milestoneId);
		milestone.setInvoiceReopenFlag(Boolean.TRUE);
		resourceManagementDAO.update(milestone);

	}

	@Override
	public List<InvoiceDTO> getRaisedInvoices(Long milestoneId) {
		List<Invoice> invoiceList = invoiceDao.getRaisedInvoices(milestoneId);

		List<InvoiceDTO> invoiceDtoList = null;
		InvoiceDTO invoiceDto = null;

		if (invoiceList != null) {
			invoiceDtoList = new ArrayList<InvoiceDTO>();

			for (Invoice invoice : invoiceList) {
				invoiceDto = new InvoiceDTO();
				invoiceDto.setId(invoice.getId());
				invoiceDto.setMilestoneId(invoice.getMilsestone().getId());
				invoiceDto.setMilestonePercentage(invoice.getMilsestone()
						.getMilestonePercentage());
				invoiceDto.setPercentage(invoice.getPercentage());
				if(invoice.getProformaReferenceNo() != null){
					invoiceDto.setProformaReferenceNo(invoice.getProformaReferenceNo().getInvoiceNumber());
				}
				invoiceDtoList.add(invoiceDto);
			}

		}

		return invoiceDtoList;
	}

	@Override
	public Map<String, Object> getClosedMilestonesandCRs(Long projectId) {

		Map<String, Object> map = new HashMap<>();

		List<Milestone> milestones = resourceManagementDAO
				.getClosedMilesonesForProject(projectId);

		List<ChangeRequestDTO> changeRequestList = new ArrayList<>();

		List<Long> crIds = new ArrayList<>();

		ChangeRequestDTO changeRequestDTO = null;

		List<MilestoneDTO> milestonesList = new ArrayList<MilestoneDTO>();

		MilestoneDTO milestoneDTO = null;

		for (Milestone milestone : milestones) {

			milestoneDTO = new MilestoneDTO();

			milestoneDTO.setId(milestone.getId());
			milestoneDTO.setTitle(milestone.getTitle());
			milestoneDTO.setInvoiceStatus(milestone.getInvoiceStatus());
			milestoneDTO.setProject(milestone.getProject().getProjectName());
			if (milestone.getProject().getType() != null)
				milestoneDTO.setProjectType(milestone.getProject().getType()
						.name());
			if (milestone.getProject().getClient() != null)
				milestoneDTO.setClient(milestone.getProject().getClient()
						.getName());
			milestoneDTO.setTitle(milestone.getTitle());
			milestoneDTO.setProjectId(milestone.getProject().getId());
			milestoneDTO.setIsClosed(milestone.isClosed());
			milestoneDTO
					.setMilestoneTypeFlag(milestone.getMilestoneTypeFlag() != null ? milestone
							.getMilestoneTypeFlag() : null);
			milestoneDTO.setMilestonePercentage(milestone
					.getMilestonePercentage());

			if (milestone.getChangeRequest() != null) {
				milestoneDTO.setCrId(milestone.getChangeRequest().getId());
				milestoneDTO.setCrName(milestone.getChangeRequest().getName());

				// get only those cr's for which milestone is created

				if (!crIds.contains(milestone.getChangeRequest().getId())) {
					crIds.add(milestone.getChangeRequest().getId());
				}

			}

			Double totalinvoiceRaisedPercentage = 0.0;
			Double remainigPercentage = 0.0;

			if (milestone.getProject().getType().toString()
					.equalsIgnoreCase("FIXEDBID")
					&& (milestone.isBillable() == Boolean.TRUE)) {

				List<InvoiceDTO> raisedInvoiceList = this
						.getRaisedInvoices(milestone.getId());

				if (raisedInvoiceList.size() > 0) {
					for (InvoiceDTO list : raisedInvoiceList) {
						// When raise invoice from Proforma Invoice
						if(list.getProformaReferenceNo() == null){
							totalinvoiceRaisedPercentage += (double) Math
									.round(Double.parseDouble(list.getPercentage()) * 100.0) / 100.0;
						}
					}
					
					//System.out.println("Milestone persentage = "+ milestone.getMilestonePercentage());
					if(milestone.getMilestonePercentage()!=null && !milestone.getMilestonePercentage().isEmpty()){
					remainigPercentage = ((double) Math
							.round(Double.valueOf(milestone
									.getMilestonePercentage()) * 100.0) / 100.0)
							- totalinvoiceRaisedPercentage;
					}

					milestoneDTO
							.setRaisedInvoicePercentage(totalinvoiceRaisedPercentage
									.toString());

					milestoneDTO.setRemainingPercentage(remainigPercentage
							.toString());

				}

				else {
					// if invoice is not raised and milestone is in form of
					// amount
					if (milestone.getMilestoneTypeFlag() == Boolean.TRUE) {

						milestoneDTO
								.setRaisedInvoicePercentage(totalinvoiceRaisedPercentage
										.toString());

						milestoneDTO.setRemainingPercentage(remainigPercentage
								.toString());
					}

					// if invoice is not raised and milestone is in form of
					// percentage
					else {

						milestoneDTO
								.setRaisedInvoicePercentage(totalinvoiceRaisedPercentage
										.toString());

						milestoneDTO
								.setRemainingPercentage((String.valueOf((double) Math.round(Double
										.valueOf(milestone
												.getMilestonePercentage()) * 100.0) / 100.0)));
					}
				}
			}
			// for retainer project type
			else {

				if (milestone.isBillable() == Boolean.TRUE) {
					// if invoice raised
					if (milestone.getInvoiceStatus() == Boolean.TRUE) {

						milestoneDTO.setRaisedInvoicePercentage((Double
								.valueOf(milestone.getMilestonePercentage())
								.toString()));

						milestoneDTO.setRemainingPercentage(remainigPercentage
								.toString());
					}
					// if invoice is not raised
					else {

						milestoneDTO
								.setRaisedInvoicePercentage(totalinvoiceRaisedPercentage
										.toString());

						milestoneDTO.setRemainingPercentage((Double
								.valueOf(milestone.getMilestonePercentage())
								.toString()));
					}

				}
			}

			milestonesList.add(milestoneDTO);

		}

		// getting only those CR's for which milestone is created

		Collections.sort(crIds, Collections.reverseOrder());

		for (Long id : crIds) {
			ChangeRequest cr = resourceManagementDAO.findBy(
					ChangeRequest.class, id);
			changeRequestDTO = new ChangeRequestDTO();
			changeRequestDTO.setId(cr.getId());
			changeRequestDTO.setName(cr.getName());
			changeRequestList.add(changeRequestDTO);
		}

		map.put("milestoneList", milestonesList);
		map.put("CRList", changeRequestList);

		return map;
	}

	// this below code is used to create project in hive if u want please uncomment it
	
	private String createProjectInHive(Project projectDetails)
			throws IOException, ParseException, JSONException {

		Employee loggedInEmp = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		//logger.warn("loggedInEmployee:" + loggedInEmp.getEmployeeFullName());

		String fileLocation = (String) propBean.getPropData().get("HiveLog");
		String fileName = DateParser.toDate(new Date().toString("dd/MM/yyyy"))
				+ ".log";
		File file = new File(fileLocation + fileName);
		FileOutputStream fos = null;
		if (file.exists()) {
			// System.out.println("in if");
			fos = new FileOutputStream(file, true);
		} else {
			// System.out.println("in else");
			fos = new FileOutputStream(file);
		}

		PrintWriter writer = new PrintWriter(fos);

		writer.println(new Date().toString("dd-MM-yyyy HH:mm:ss"));
		writer.println("loggedInEmployee:" + loggedInEmp.getEmployeeFullName());

		List<HiveDetails> hiveInfo = dao.get(HiveDetails.class);
		String location = hiveInfo.get(0).getLocation();
		String key = hiveInfo.get(0).getKey();

		String[] projectName = projectDetails.getProjectName().split(" ");
		// System.out.println("length:"+projectName.length);
		String Identifier = "";
		for (int i = 0; i < projectName.length; i++) {
			if (i == 0) {
				Identifier += projectName[i].toLowerCase();
			} else {
				Identifier += "-" + projectName[i].toLowerCase();
			}
		}

		// System.out.println("identifier:"+ Identifier);

		StringBuilder url = new StringBuilder(location);
		url.append("?key=");
		url.append(URLEncoder.encode(key, "UTF-8"));
		url.append("&name=");
		url.append(URLEncoder.encode(projectDetails.getProjectName(), "UTF-8"));
		url.append("&id=");
		url.append(URLEncoder.encode(Identifier, "UTF-8"));
		url.append("&start_date=");
		url.append(URLEncoder.encode(projectDetails.getPeriod().getMinimum()
				.toString("yyyy-MM-dd"), "UTF-8"));
		url.append("&end_date=");
		url.append(URLEncoder.encode(projectDetails.getPeriod().getMaximum()
				.toString("yyyy-MM-dd"), "UTF-8"));
		url.append("&desc=");
		url.append(URLEncoder.encode(projectDetails.getDescription(), "UTF-8"));
		url.append("&project_code=");
		url.append(URLEncoder.encode(projectDetails.getProjectCode(), "UTF-8"));

		URL urlObj = new URL(url.toString());

		HttpURLConnection connection = (HttpURLConnection) urlObj
				.openConnection();

		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Accept", "application/json");
		connection.connect();

		//logger.warn("URL:" + url);
		//logger.warn("responsecode:" + connection.getResponseCode());

		writer.println("URL:" + url);
		writer.println("responsecode:" + connection.getResponseCode());

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		StringBuilder response = new StringBuilder();
		String input;
		while ((input = reader.readLine()) != null) {
			response.append(input);
		}
		reader.close();
		//logger.warn("response:" + response);
		JSONObject object = new JSONObject(response.toString());
		String responseValue = (String) object.getString("type");
		writer.println("response:" + response);
		writer.close();
		return responseValue;
	}

	/*
	 * @Override public List<Long> getEmployeesUnderProjectManager(Long empid) {
	 * 
	 * List<Employee> allProjectmanager = resourceManagementDAO
	 * .EmployeesUnderProjectManager(empid); List<Long> projectManagerIds =new
	 * ArrayList<Long>(); projectManagerIds.add(empid);
	 * if(allProjectmanager.size() > 0){ for(Employee employee
	 * :allProjectmanager){ List<Long> resources =
	 * getEmployeesUnderProjectManager(employee.getEmployeeId());
	 * projectManagerIds.addAll(resources); } }
	 * 
	 * return projectManagerIds; }
	 */

}
