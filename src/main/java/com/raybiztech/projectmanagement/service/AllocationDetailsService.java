package com.raybiztech.projectmanagement.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityReport;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.projectmanagement.dto.AllocationDTO;
import com.raybiztech.projectmanagement.dto.MilestonePeopleDTO;
import com.raybiztech.projectmanagement.dto.MultipleAllocationDTO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.dto.ReportDTO;

public interface AllocationDetailsService {

	List<ReportDTO> reports(Long loginid);

	void addResourceToProject(MultipleAllocationDTO multipleAllocationDTO);

	List<ReportDTO> activeEmployeeList(Long loginid);

	List<ReportDTO> getProjectDetails(Long projectId);

	// List<AllocationDetailsDTO> getProjectDetailsEmployeeCount(Long
	// emloyeeid,String ProjectStatus);

	Boolean isEmployeeAllocatedTosameProject(Long employeeId, Long projectId);

	List<ProjectDTO> getAllProjectSearch(@RequestParam String searchStr);

	List<ReportDTO> getBillableResources(Long projectId);

	List<MilestonePeopleDTO> getmilestonebillableresources(Long id);

	List<ReportDTO> getPeopleForMilestone(Long projectId,String planningEndDate);

	public List<MilestonePeopleDTO> getmilestonebillableresourceswithInvoiceRate(
			Long id);
	List<EmpoloyeeHiveActivityReport> getProjectTimeSheet(Long projectId,String hiveDate);
	
	List<ReportDTO> getEmployeeProjectDetails(Long projectId);
	
	List<SearchEmpDetailsDTO> getProjectEmployees(Long projectId);
	
    AllocationDTO updateEndDate(Long allocationId, String endDate);


}
