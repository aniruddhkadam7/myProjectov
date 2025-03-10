package com.raybiztech.projectmanagement.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.Domain;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.Platform;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectCheckList;
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
import com.raybiztech.projectmanagement.dto.ReportDTO;
import com.raybiztech.projectmanagement.dto.StatusReportDTO;
import com.raybiztech.projectmanagement.invoice.dto.InvoiceDTO;
import com.raybiztech.projectmanagement.invoice.lookup.Currency;

public interface ProjectService {

	/**
	 *
	 * @param projectDTO
	 *            this project will add a project.
	 */
	void addProject(ProjectDTO projectDTO);

	/**
	 *
	 * @param ProjectRequestDTO
	 *            this project will add a new project request.
	 */
	void addProjectRequest(ProjectRequestDTO projectRequestDTO);

	/**
	 * this project returns list of all existing projects.
	 */
	List<ProjectDTO> getAllProjects();

	/**
	 * this project returns list of all existing projects requests.
	 */
	/* List<ProjectRequestDTO> getAllProjectRequestList(); */

	/**
	 * this project returns list of all existing projects requests for manger.
	 */
	// List<ProjectRequestDTO> getManagerProjectRequests(Long empId);

	/**
	 * this project returns Reject projects request.
	 */
	void rejectProjectRequest(Long requestId, String comment);

	/**
	 * this method will delete a project
	 */
	void deleteProject(Long projectid);

	/**
	 * this method will delete a project request
	 */
	void deleteProjectRequest(Long id, HttpServletResponse response);

	/**
	 * this method will update an existing project.
	 */
	ProjectDTO updateProject(ProjectDTO projectDTO);

	List<ProjectDTO> activeProjects();

	void deAllocateProjectToEmployee(ReportDTO reportDTO);

	ProjectDTO getproject(Long projectid);

	ProjectRequestDTO getProjectRequest(Long id);

	void updateAllocateProject(ReportDTO dto);

	ReportDTO getAllocateProject(Long employeeid, Long projectid);

	List<ManagerDTO> getAllManagers();

	List<ProjectNameDTO> activeProjectsByProjectName();

	Map<String, Object> activeProjectsForEmployee(Long employeeid,
			String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex);

	Map<String, Object> projectsForMyProfile(Long employeeid,
			String projectStatus, String type, Integer firstIndex,
			Integer endIndex);

	List<ProjectDTO> getAllProjects_UnderEmployee(Long employeeId,
			String isBillale, String isAllocated, String startdate,
			String enddate, String dateSelection);

	Map<String, Object> searchByEmployeeName(Long loggedInEmpId,
			Integer firstIndex, Integer endIndex, String employeeName,
			String technology, String isBillable, String isAllocated,
			String startdate, String enddate, List<String> departmentNames,
			String dateSelection);

	Map<String, Object> searchByEmployeeId(Long employeeId);

	Map<String, Object> getProfilePaginationEmployeesData(Long loggedInEmpId,
			int startIndex, int endIndex, String selectionStatus);

	List<ProjectDTO> getAllProjects(String projectStatus);

	Map<String, Object> searchAllocationReportData(Long employeeId,
			String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex, String startdate,
			String enddate, String multiSearch, String datePeriod,
			Boolean intrnalOrNot);

	ByteArrayOutputStream exportProjectList(Long employeeId,
			String projectStatus, String type, String health, String startdate,
			String enddate, String multiSearch, String datePeriod,
			Boolean intrnalOrNot) throws Exception;

	void updateEmployeeAllocateProject(ProjectDTO dto);

	void addMileStone(MilestoneDTO dto);

	Map<String, Object> getAllMilestones_UnderProject(Long projectId,
			Integer firstIndex, Integer endIndex);

	void updateMileStone(MilestoneDTO dto);

	void deleteMilestone(Long milestoneId);

	void addStatusReport(StatusReportDTO dto);

	void updateStatusReport(StatusReportDTO dto);

	void deleteStatusReport(Long statusReportId);

	Map<String, Object> getAllStatusReports_UnderProject(Long projectId,
			Integer firstIndex, Integer endIndex);

	void addClient(ClientDTO clientDTO);

	public void deleteClient(Long clientId);

	public ClientDTO getclient(Long clientId);

	List<CountryDTO> getCountries();

	void updateClient(ClientDTO clientDTO);

	Map<String, Object> getBillingReports(Integer startIndex, Integer endIndex,
			String status);

	Map<String, Object> getBillingReportsOnSearch(Integer startIndex,
			Integer endIndex, String status, String from, String to,
			String searchText, String client, String dateSelection);

	Map<String, Object> getAllClients(Integer startIndex, Integer endIndex,String selectionStatus);

	Map<String, Object> searchClients(String search, Integer firstIndex,
			Integer endIndex ,String selectionStatus);

	void closeMilestone(Long milestoneId);

	List<MileStoneAuditDTO> getAllMileStoneHistory(Long mileStoneId);

	Map<String, Object> getProjectsOfClients(Integer startIndex,
			Integer endIndex, String client, String type);

	void closeProject(Long projectId);

	ClientDTO getClientInfo(Long id);

	public ByteArrayOutputStream exportCsv(Long ids,
			HttpServletResponse response, int startIndex, int endIndex,
			String empName, String technology, String isBillable,
			String isAllocate, String startdate, String lastdate,
			List<String> departmentNames, String dateSelection);

	List<ClientDTO> getclients();

	Double getAllMilestonePercentageCount(Long projectId);

   Double getRaisedCRPercentage(Long crId);

	Map<String, Object> getProjectNumbers(Long projectId);

	void addProjectNumbers(ProjectNumbersDTO dto);

	List<ReportDTO> getPeopleUnderMilestone(Long milestoneId, Long projectId);

	List<Currency> getCurrencyLookUp();

	void addCR(ChangeRequestDTO changeRequestDTO);

	Map<String, Object> getChangeRequestDTOList(Long projectId,
			Integer firstIndex, Integer endIndex);

	List<ChangeRequestDTO> getCRlookupForProjectNumbers(Long ProjectId);

	void deleteCR(Long crId);

	void updateProjectNumbers(ProjectNumbersDTO dto);

	List<ChangeRequestDTO> getCRListForMilestone(Long projectId);

	void updateChangeRequest(ChangeRequestDTO changeRequestDto);

	void updateCRNumbers(ProjectNumbersDTO dto);

	Map<String, Object> getProjectHistory(Long projectId);

	List<Long> mangerUnderManager(Long empid);

	Map<String, Object> getWorkingDaysOfEmployeeForDetails(String formDate,
			String toDate, Long empId);

	MilestoneDTO getMilestone(Long milestoneId);

	/*
	 * String getMilestoneNumber(Long projectId);
	 * 
	 * String generateMilestoneNumber(Project project, Integer count);
	 * 
	 * Boolean milestoneNumberAlreadyExists(String milestoneNumber);
	 */

	String getNextMilestoneNumber(Long projectId);

	List<Project> getProjectWhoseEndDateisInNextFiveDays();

	void addProjectProposal(ProjectProposalsDTO dto);

	List<ProjectProposalsDTO> getProjectProposals(Long projectId);

	void reOpenMilestone(Long milestoneId);

	void projectRequestMailId(String cc, String bcc);

	ProjectRequestMailDTO getProjectRequestMailIds();

	List<ProjectInitiationChecklistDTO> getInitiationCheckList();

	ByteArrayOutputStream downloadInitationCheckList(Long projectRequestId)
			throws IOException;

	List<ProjectRequestAuditDTO> getAuditForProjectRequest(Long id);

	Boolean checkClientOrg(String organization);

	Map<String, Object> getAllProjectRequestList(Integer firstIndex,
			Integer endIndex, String multiSearch);

	List<Platform> getAllPlatforms();

	List<Domain> getAllDomains();

	ByteArrayOutputStream exportEmployeeAllocation(Long ids, int startIndex,
			int endIndex, String empName, String technology, String isBillable,
			String isAllocate, String startdate, String lastdate,
			List<String> departmentNames, String dateSelection)
			throws IOException;
	
	Map<String, Object> getNumbersAndPercentage(Long milestoneid, String id);
	
	Map<String, Object> getNonClosedMilestones(Integer fromIndex,Integer toIndex);
	
	List<ProjectDTO> getAllActiveProjects_UnderClient(Long clientId);
	
	Map<String, Object> getEmployeeProjectslist(Long employeeid,
			String projectStatus, String type,
			Integer firstIndex, Integer endIndex);
	
	void reOpenRaisedInvoiceMilestone(Long milestoneId);
	
	/*List<Long> getEmployeesUnderProjectManager(Long empid);*/
	
	List<InvoiceDTO> getRaisedInvoices(Long milestoneId);
	
	Map<String, Object> getClosedMilestonesandCRs(Long projectId);

}
