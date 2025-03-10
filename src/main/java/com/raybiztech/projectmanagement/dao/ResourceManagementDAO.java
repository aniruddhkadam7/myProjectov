package com.raybiztech.projectmanagement.dao;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.biometric.business.BioAttendance;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.AllocationDetailsAudit;
import com.raybiztech.projectmanagement.business.AllocationEffort;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Country;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.MilestoneAudit;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectAudit;
import com.raybiztech.projectmanagement.business.ProjectCheckList;
import com.raybiztech.projectmanagement.business.ProjectNumbers;
import com.raybiztech.projectmanagement.business.ProjectRequest;
import com.raybiztech.projectmanagement.business.ProjectRequestMail;
import com.raybiztech.projectmanagement.dto.ChangeRequestDTO;
import com.raybiztech.projectmanagement.dto.ProjectNumbersDTO;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.recruitment.business.Holidays;

public interface ResourceManagementDAO extends DAO {

	List<Project> activeProjects();

	List<Employee> reports(Employee employee);

	List<Employee> activeEmployeeList(Employee employee);

	List<Employee> getAllocationEmployees(Employee employee);

	List<AllocationDetails> getProjectDetails(Long projectId);

	List<AllocationDetails> getProjectDetailsForMilestone(Long projectId);

	List<Project> activeProjectsForEmployee(Long employeeid);

	Map<String, Object> getProjectDetailsEmployeeCount(List<Long> employeeid,
			Long empId, String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex);

	Map<String, Object> getProjectDetailsEmployeeCountAdmin(
			String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex);

	public Map<Project, AllocationDetails> getAllProjects_forEmployee(
			Long employeeId);

	Map<Project, AllocationDetails> getAllProjects_UnderEmployee(
			Long employeeId, String isBillale, List<String> employeeStatus,
			DateRange date);

	Map<String, Object> searchByEmployeeNameForManager(List<Long> employeeId,
			Integer firstIndex, Integer endIndex, String employeeName,
			String technology, String isBillable, List<String> employeeStatus,
			DateRange date);

	Map<String, Object> searchByEmployeeNameForAdmin(Long employeeId,
			Integer firstIndex, Integer endIndex, String employeeName,
			String technology, String isBillable,List<String > employeeStatus,
			DateRange date, List<String> departmentNames)
			throws HibernateException, ParseException;

	List<Employee> searchByEmployeeId(Long employeeId);

	Boolean isEmployeeAllocatedTosameProject(Long employeeId, Long projectId);

	Map<String, Object> getProfilePaginationEmployeesData(Long loggedInEmpId,
			String selectionStatus, int startIndex, int endIndex);

	List<Project> getAllProjects(String projectStatus);

	Map<String, Object> searchAllocationReportData(List<Long> employeeId,
			Long empid, String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex, DateRange dateRange,
			String multiSearch, Boolean internalOrNot);

	Map<String, Object> searchAllocationReportDataForAdmin(
			String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex, DateRange dateRange,
			String multiSearch, Boolean internalOrNot);

	void deleteProject(Long projectid);

	List<AllocationDetails> getAllocationProject(Long employeeId, Long id);

	Map<String, Object> getAllMilestones_UnderProject(Long projectId,
			Integer firstIndex, Integer endIndex);

	Map<String, Object> getAllStatusReports_UnderProject(Long projectId,
			Integer firstIndex, Integer endIndex);

	List<Project> getAllProjectSearch(String searchStr);

	Map<String, Object> getProjectDetailsEmployeeCountForEmployee(
			Long employeeid, String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex);

	Map<String, Object> getProjectDetailsForEmployeeProfile(Long employeeid,
			String projectStatus, String type, Integer firstIndex,
			Integer endIndex);
	
	Map<String, Object> getAllClients(Integer startIndex, Integer endIndex,String selectionStatus);

	List<Country> getCountries();

	Map<String, Object> getBillingReports(Integer startIndex, Integer endIndex,
			String status);

	Map<String, Object> getBillingReportsOnSearch(Integer startIndex,
			Integer endIndex, String status, DateRange dateRange,
			String searchText, String client);

	Map<String, Object> searchClients(String search, Integer firstIndex,
			Integer endIndex,String selectionStatus);

	List<Milestone> getUnclosedBillableMilestones();

	List<MilestoneAudit> getAllMileStoneHistory(Long mileStoneId);

	Map<String, Object> getProjectsOfClients(Integer startIndex,
			Integer endIndex, Long clientId, String type);

	Map<String, Object> getProjectDetailsEmployeeCountAdmin(Integer startIndex,
			Integer endIndex, String client, String type);

	Map<String, Object> searchProjectDetailsEmployeeCountForEmployee(
			Long employeeId, String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex, String multiSearch,
			DateRange dateRange);

	List<AllocationDetails> getBillableResources(Long projectId);

	List<Client> getclients();

	Double getAllMilestonePercentageCount(Project id);

	Double getRaisedCrPercentage(ChangeRequest id, Long milestoneId);

	List<ProjectNumbers> getProjectNumbers(Project projectId);

	Map<String, Object> getChangeRequestDTOList(Long projectId,
			Integer firstIndex, Integer endIndex);

	List<ChangeRequestDTO> getCRlookupForProjectNumbers(Long projectID);

	List<ChangeRequestDTO> getCRListForMilestone(Long projectId);

	void updateCRNumbers(ProjectNumbersDTO dto);

	<T extends Serializable> List<T> findManagers(Class<T> clazz);

	Map<String, Object> getAllProjectRequests(Integer firstIndex,
			Integer endIndex, String multiSearch);

	Map<String, Object> getAllProjectRequestFor(List<Long> employeeIds,
			Integer firstIndex, Integer endIndex, String multiSearch);

	List<ProjectAudit> getProjectHistory(Long projectId);

	List<AllocationDetailsAudit> getAllocationHistory(Long projectId);

	// public List<Employee> mangerUnderManager(Long empId);

	List<Holidays> getHolidaysBetweenDates(Date fromDate, Date toDate);

	Integer getApprovedLeavesOfEmployeeBetweenDateRange(String fromDate,
			String todate, Employee empId);

	List<BioAttendance> getBioAttendanceOfEmployeeBetweenDateRange(
			Date fromDate, Date toDate, Employee employee);

	Map<String, List<Audit>> getProjectAudit(Long projectId);

	Boolean isMilestoneExistsForProject(Long projectId);

	List<Invoice> getInvoiceListOfProject(Project projectId);

	Integer allocationSizeOfProjectForEmployee(Project projectid);

	Milestone getrecentlyInsertedMilestone(Project project);

	List<AllocationDetails> getallocateresources();

	List<Project> getProjectWhoseEndDateisInNextFiveDays();

	List<Project> getProjectsWhoseStartDateInLastFiveDays();

	List<Long> getAllocatedEmps(Long projectId);

	List<Long> getEmployeesForProjectHiveReport(Long projectId, String hivedate);

	void deAllocateWhenInactive(Long id);

	List<AllocationDetails> getMilestonePeople(Long projectId,
			String planningEndDate);

	ProjectRequestMail getProjectRequestCCandBCC();

	Employee getDeliveryManagerofProject(Project project);

	Boolean checkForRequestId(Long id);

	List<Project> getProjectList(Long clientId);

	List<Audit> getAuditForProjectRequest(Long id);

	List<ProjectCheckList> getChecklist();

	Boolean checkClientOrg(String organization);

	/* ProjectEffort getProjectEffort(String hiveProjectName); */

	List<AllocationEffort> getAllocationDetails(Long projectId);

	List<AllocationEffort> getEmployeeRecords(Long projectId, Long empId);
	
	List<Project> getActiveProjects(Long employeeId);
	
	//getting Project Numbers according to the Milestone Type
		ProjectNumbers getProjectNumbers(Milestone milestone);
		
		Map<String, Object> getNonClosedMilestoneList(Employee employeeId,Integer fromIndex,Integer toIndex);
		
		List<Project> getAllActiveProjects_UnderClient(Long clientId);
		
		Map<String, Object> getEmployeeProjectslist(Long employeeid,
				String projectStatus, String type, Integer firstIndex,
				Integer endIndex);
		
		/*public List<Employee> EmployeesUnderProjectManager(Long empId);
*/
		
		List<AllocationDetails> getEmployeeProjectDetails(Long projectId);
		
		List<EmpDepartment> getAllocationDepartments(List<String> deppartmnets);

        List<String> projectnotSupportedDepartments();
         
        List<String> projectSupportedDepartments();
        
        List<Employee> getNotAllocatedEmployeeDepartmentEmployees(List<String> departmentnames,List<Long> employee);
        
        List<Milestone> getClosedMilesonesForProject(Long projectId);
        
        List<ChangeRequest> getCrsUnderProject(Long projectId);

		List<Milestone> getMilestonesUnderCr(Long id);

		List<Project> getAllInprogressProjects();
 
}
