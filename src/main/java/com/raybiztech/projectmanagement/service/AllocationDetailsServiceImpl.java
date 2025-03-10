package com.raybiztech.projectmanagement.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.TimeActivity.builder.HiveTimeActivityBuilder;
import com.raybiztech.TimeActivity.business.EmpoloyeeHiveActivity;
import com.raybiztech.TimeActivity.dao.TimeActivityDAO;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityReport;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.builder.AllocationDetailsBuilder;
import com.raybiztech.projectmanagement.builder.AuditBuilder;
import com.raybiztech.projectmanagement.builder.MilestonePeopleBuilder;
import com.raybiztech.projectmanagement.builder.ProjectBuilder;
import com.raybiztech.projectmanagement.builder.ReportBuilder;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.AllocationDetailsAudit;
import com.raybiztech.projectmanagement.business.AllocationEffort;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.MilestonePeople;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectAudit;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.dto.AllocationDTO;
import com.raybiztech.projectmanagement.dto.MilestonePeopleDTO;
import com.raybiztech.projectmanagement.dto.MultipleAllocationDTO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.dto.ReportDTO;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.mailnotifcation.ProjectAllocationAcknowledgement;

@Service("allocationDetailstService")
@Transactional
public class AllocationDetailsServiceImpl implements AllocationDetailsService {

	@Autowired
	ReportBuilder builder;
	@Autowired
	ResourceManagementDAO resourceManagementDAO;
	@Autowired
	AllocationDetailsBuilder allocationDetailsBuilder;
	@Autowired
	ProjectBuilder projectBuilder;
	@Autowired
	MilestonePeopleBuilder milestonePeopleBuilder;
	@Autowired
	ProjectAllocationAcknowledgement allocationAcknowledgement;
	@Autowired
	AuditBuilder auditBuilder;
	@Autowired
	HiveTimeActivityBuilder hiveTimeActivityBuilder;
	@Autowired
	TimeActivityDAO timeActivityDAO;
	@Autowired
	EmployeeBuilder employeeBuilder;

	Logger logger = Logger.getLogger(AllocationDetailsServiceImpl.class);

	@Override
	public List<ReportDTO> reports(Long loginid) {

		Employee employee = resourceManagementDAO.findBy(Employee.class,
				loginid);

		return builder.convertEmplyeeTOReportList(resourceManagementDAO
				.reports(employee));
	}

	@CacheEvict(value = "employeeAllocation", allEntries = true)
	@Override
	public void addResourceToProject(MultipleAllocationDTO multipleAllocationDTO) {
		Map<Project, AllocationDetails> map;

		List<Long> ids = multipleAllocationDTO.getEmployeeIds();

		for (Long currentId : ids) {
			Employee employee = resourceManagementDAO.findBy(Employee.class,
					currentId);
			Project project = resourceManagementDAO.findBy(Project.class,
					multipleAllocationDTO.getProjectId());
			AllocationDetails allocationDetails = allocationDetailsBuilder
					.convertMultipleAllocationDTOtoAllocationDeatils(multipleAllocationDTO);
			if (employee.getAllocations().isEmpty()) {
				map = new HashMap<Project, AllocationDetails>();
			} else {
				map = employee.getAllocations();
			}
			map.put(project, allocationDetails);

			employee.setAllocations(map);
			resourceManagementDAO.saveOrUpdate(employee);

			// for inserting a record in allocation effort table with created
			// date
			// for the purpose of calculating employee allocation effort
			AllocationEffort allocationEffort = new AllocationEffort();
			allocationEffort.setBillable(allocationDetails.getBillable());
			allocationEffort.setComments(allocationDetails.getComments());
			allocationEffort.setEmployee(employee);
			allocationEffort.setIsAllocated(allocationDetails.getIsAllocated());
			allocationEffort.setPercentage(allocationDetails.getPercentage());
			allocationEffort.setPeriod(allocationDetails.getPeriod());
			allocationEffort.setProject(project);
			allocationEffort.setCreatedDate(new Second());
			resourceManagementDAO.save(allocationEffort);

			List<Audit> audits = auditBuilder.allocationDetailsTOPOSTAudit(
					allocationDetails, employee, project.getId(),
					"ALLOCATIONDETAILS", "CREATED");
			for (Audit audit : audits) {
				resourceManagementDAO.save(audit);
			}
			// AllocationDetailsAudit detailsAudit =
			// allocationDetailsBuilder.convertTOAllocationDetailsAudit(allocationDetails,employee,project.getId(),"CREATED");

			try {
				allocationAcknowledgement.sendProjectAllocationAcknowledgement(
						allocationDetails, employee, project);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public List<ReportDTO> activeEmployeeList(Long loginid) {
		Employee employee = resourceManagementDAO.findBy(Employee.class,
				loginid);
		return builder.convertEmplyeeTOReport(resourceManagementDAO
				.activeEmployeeList(employee));

	}

	@Override
	public List<ReportDTO> getProjectDetails(Long projectId) {
		Project project = resourceManagementDAO
				.findBy(Project.class, projectId);
		List<AllocationDetails> allocationDetailsList = resourceManagementDAO
				.getProjectDetails(projectId);
		List<ReportDTO> allocationDetailsDTOList = allocationDetailsBuilder
				.convertEntityToDTO(allocationDetailsList, project);
		return allocationDetailsDTOList;
	}

	// @Override
	// public List<AllocationDetailsDTO> getProjectDetailsEmployeeCount(Long
	// emloyeeid,String ProjectStatus) {
	//
	// List<AllocationDetailsDTO> list = resourceManagementDAO
	// .getProjectDetailsEmployeeCount(emloyeeid,ProjectStatus);
	// // List<AllocationDetailsDTO> dtos = new
	// ArrayList<AllocationDetailsDTO>();
	// //
	// // for (AllocationDetailsDTO detailsDTO : list) {
	// // AllocationDetailsDTO detailsDTO2 = new AllocationDetailsDTO();
	// // ProjectDTO e = new ProjectDTO();
	// // e.setProjectName(detailsDTO.getProject().getProjectName());
	// // detailsDTO2.setProjectDto(e);
	// // dtos.add(detailsDTO2);
	// // }
	// return null;
	// }

	@Override
	public Boolean isEmployeeAllocatedTosameProject(Long employeeId,
			Long projectId) {
		return resourceManagementDAO.isEmployeeAllocatedTosameProject(
				employeeId, projectId);
	}

	@Override
	public List<ProjectDTO> getAllProjectSearch(String searchStr) {
		List<Project> projects = resourceManagementDAO
				.getAllProjectSearch(searchStr);
		List<ProjectDTO> dTOs = projectBuilder
				.createProjectActiveList(projects);
		return dTOs;
	}

	@Override
	public List<ReportDTO> getBillableResources(Long projectId) {
		List<AllocationDetails> allocationDetailsList = resourceManagementDAO
				.getBillableResources(projectId);
		List<ReportDTO> allocationDetailsDTOList = allocationDetailsBuilder
				.getBillableResourcesDTO(allocationDetailsList);
		return allocationDetailsDTOList;
	}

	// this is important method used while edit of Invoice to get Resources
	@Override
	public List<MilestonePeopleDTO> getmilestonebillableresources(Long id) {

		Milestone milestone = resourceManagementDAO.findBy(Milestone.class, id);
		// milestone.getMilestonePeople();
		Set<MilestonePeople> milestonePeoples = milestone.getMilestonePeople();
		List<MilestonePeople> milestonepeopleList = new ArrayList<MilestonePeople>(
				milestonePeoples);

		return milestonePeopleBuilder.convertTODTOList(milestonepeopleList);
	}

	@Override
	public List<MilestonePeopleDTO> getmilestonebillableresourceswithInvoiceRate(
			Long id) {
		Milestone milestone = resourceManagementDAO.findBy(Milestone.class, id);
		// milestone.getMilestonePeople();
		Set<MilestonePeople> milestonePeoples = milestone.getMilestonePeople();
		List<MilestonePeople> milestonepeopleList = new ArrayList<MilestonePeople>(
				milestonePeoples);

		// here from milestone we are getting project and by project we are
		// getting
		// Invoices to retrieve rate and amount
		Long projectId = milestone.getProject().getId();
		Project project = resourceManagementDAO
				.findBy(Project.class, projectId);

		// here we are getting latest invoice (desc) order invoice list
		List<Invoice> invoiceListofProject = resourceManagementDAO
				.getInvoiceListOfProject(project);

		// sending invoices of project and milestonePeople
		return milestonePeopleBuilder.getMilestonePeopleWithInvoiceRate(
				milestonepeopleList, invoiceListofProject);
	}

	@Override
	public List<ReportDTO> getPeopleForMilestone(Long projectId,
			String planningEndDate) {

		List<AllocationDetails> allocationDetailsList = resourceManagementDAO
				.getMilestonePeople(projectId, planningEndDate);
		List<ReportDTO> reportDTOs = new ArrayList<ReportDTO>();
		for (AllocationDetails allocationDetails : allocationDetailsList) {
			ReportDTO reportDTO = new ReportDTO();
			reportDTO.setEmployeeId(allocationDetails.getEmployee()
					.getEmployeeId());
			reportDTO.setEmpName(allocationDetails.getEmployee().getFullName());
			reportDTO.setIsAllocated(allocationDetails.getIsAllocated());
			reportDTO.setBillable(allocationDetails.getBillable());
			reportDTO.setDesigination(allocationDetails.getEmployee()
					.getDesignation());
			reportDTO.setAllocation(allocationDetails.getPercentage().toString(
					"#0", false));
			reportDTO
					.setUserName(allocationDetails.getEmployee().getFullName());
			reportDTOs.add(reportDTO);

		}

		return reportDTOs;
	}

	@Override
	public List<EmpoloyeeHiveActivityReport> getProjectTimeSheet(
			Long projectId, String hiveDate) {
		Project project = resourceManagementDAO
				.findBy(Project.class, projectId);
		/*
		 * List<Long> employeeIds = resourceManagementDAO
		 * .getAllocatedEmps(projectId);
		 */

		List<Long> employeeIds = resourceManagementDAO
				.getEmployeesForProjectHiveReport(projectId, hiveDate);

		List<EmpoloyeeHiveActivityReport> activityDTOList = null;
		if (!employeeIds.isEmpty()) {
			Map<String, Object> hiveReportMap = timeActivityDAO
					.projectTimeSheet(hiveDate, employeeIds,
							project.getHiveProjectName());
			activityDTOList = (List) hiveReportMap.get("list");
		}
		return activityDTOList;

	}

	@Override
	public List<ReportDTO> getEmployeeProjectDetails(Long projectId) {
		Project project = resourceManagementDAO
				.findBy(Project.class, projectId);
		List<AllocationDetails> allocationDetailsList = resourceManagementDAO
				.getEmployeeProjectDetails(projectId);
		List<ReportDTO> allocationDetailsDTOList = allocationDetailsBuilder
				.convertEntityToDTO(allocationDetailsList, project);
		return allocationDetailsDTOList;
	}

	@Override
	public List<SearchEmpDetailsDTO> getProjectEmployees(Long projectId) {
		Project project = resourceManagementDAO
				.findBy(Project.class, projectId);
		List<AllocationDetails> allocationDetailsList = resourceManagementDAO
				.getEmployeeProjectDetails(projectId);
		List<ReportDTO> allocationDetailsDTOList = allocationDetailsBuilder
				.convertEntityToDTO(allocationDetailsList, project);

		List<Employee> employeesList = new ArrayList<Employee>();
		for (ReportDTO emps : allocationDetailsDTOList) {

			Employee emp = resourceManagementDAO.findBy(Employee.class,
					emps.getEmployeeId());
			employeesList.add(emp);

		}

		List<SearchEmpDetailsDTO> dTOs = employeeBuilder
				.getemployeeDTOListForSearch(employeesList);
		
		return dTOs;
	}
	
	
	
	
	@Override
    public AllocationDTO updateEndDate(Long allocationId, String endDate) {
        // Fetch the allocation by ID
        Optional<Allocation> allocationOptional = allocationRepository.findById(allocationId);
        
        if (allocationOptional.isPresent()) {
            // Get the allocation object
            Allocation allocation = allocationOptional.get();

            // Update the end date (convert the string to LocalDate)
            allocation.setEndDate(LocalDate.parse(endDate));

            // Save the updated allocation
            Allocation updatedAllocation = allocationRepository.save(allocation);

            // Return the updated allocation as a DTO
            return new AllocationDTO(updatedAllocation);
        }

        // If allocation not found, handle appropriately (throw exception or return null)
        throw new RuntimeException("Allocation not found with ID " + allocationId);
    }
	
	
	
	
	
	

}
