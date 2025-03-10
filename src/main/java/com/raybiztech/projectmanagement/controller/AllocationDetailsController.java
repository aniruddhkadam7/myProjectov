package com.raybiztech.projectmanagement.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityReport;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.projectmanagement.dto.AllocationDTO;
import com.raybiztech.projectmanagement.dto.MilestonePeopleDTO;
import com.raybiztech.projectmanagement.dto.MultipleAllocationDTO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projectmanagement.dto.ReportDTO;
import com.raybiztech.projectmanagement.service.AllocationDetailsService;

@Controller
@RequestMapping("/allocation-mgmt")
public class AllocationDetailsController {

	@Autowired
	AllocationDetailsService allocationDetailsService;
	Logger logger = Logger.getLogger(AllocationDetailsController.class);

	@RequestMapping(value = "/allocation", method = RequestMethod.GET)
	public @ResponseBody List<AllocationDTO> resources() {

		return null;
	}

	@RequestMapping(value = "/allocation", method = RequestMethod.POST)
	public @ResponseBody void addResourceToProject(
			@RequestBody MultipleAllocationDTO multipleAllocationDTO) {
		/*
		 * for (ReportDTO reportDTO2 : reportDTO) {
		 * logger.info("reportDTO2 in controller is :"
		 * +reportDTO2.getEmpFirstName()); }
		 */
		allocationDetailsService.addResourceToProject(multipleAllocationDTO);
	}

	@RequestMapping(value = "/allocation", method = RequestMethod.PUT)
	public @ResponseBody AllocationDTO updateResourceToProject(
			@RequestBody AllocationDTO resourceDTO) {
		return resourceDTO;

	}

	@RequestMapping(value = "/allocation", params = { "allocationid" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteResourceFromProject(Long allocationid) {
	}

	@RequestMapping(value = "/report/{loginid}", method = RequestMethod.GET)
	public @ResponseBody List<ReportDTO> reports(
			@PathVariable("loginid") Long loginid) {
		return allocationDetailsService.reports(loginid);

	}

	@RequestMapping(value = "/actievEmployee/{loginid}", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportDTO> activeEmployeeList(
			@PathVariable("loginid") Long loginid) {
		return allocationDetailsService.activeEmployeeList(loginid);
	}

	@RequestMapping(value = "/projects", params = { "projectId" }, method = RequestMethod.GET)
	@ResponseBody
	public List<ReportDTO> getProjectDetails(@RequestParam Long projectId,
			HttpServletResponse httpServletResponse) {
		return allocationDetailsService.getProjectDetails(projectId);
	}

	// @RequestMapping(value = "/projectsCount/{emloyeeid}/{projectStatus}",
	// method = RequestMethod.GET)
	// @ResponseBody
	// public List<AllocationDetailsDTO>
	// getProjectDetailsEmployeeCount(@PathVariable("emloyeeid") Long
	// emloyeeid,@PathVariable("projectStatus") String projectStatus) {
	// return
	// allocationDetailsService.getProjectDetailsEmployeeCount(emloyeeid,projectStatus);
	// }

	@RequestMapping(value = "/allocated", params = { "employeeId", "projectId" }, method = RequestMethod.GET)
	@ResponseBody
	public Boolean isEmployeeAllocatedTosameProject(
			@RequestParam Long employeeId, @RequestParam Long projectId) {
		return allocationDetailsService.isEmployeeAllocatedTosameProject(
				employeeId, projectId);
	}

	@RequestMapping(value = "/getAllProjectSearch", params = { "searchStr" }, method = RequestMethod.GET)
	public @ResponseBody List<ProjectDTO> getAllProjectSearch(
			@RequestParam String searchStr) {
		return allocationDetailsService.getAllProjectSearch(searchStr);
	}

	@RequestMapping(value = "/billableResources/{projectId}", method = RequestMethod.GET)
	@ResponseBody
	public List<ReportDTO> billableResources(
			@PathVariable("projectId") Long projectId) {
		return allocationDetailsService.getBillableResources(projectId);
	}

	@RequestMapping(value = "/getmilestonebillableresources/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<MilestonePeopleDTO> getmilestonebillableresources(
			@PathVariable("id") Long id) {
		return allocationDetailsService
				.getmilestonebillableresourceswithInvoiceRate(id);
	}

	@RequestMapping(value = "/getPeopleForMilestone", params = { "projectId" }, method = RequestMethod.GET)
	@ResponseBody
	public List<ReportDTO> getPeopleForMilestone(@RequestParam Long projectId) {

		return allocationDetailsService.getPeopleForMilestone(projectId,null);
	}
	
	@RequestMapping(value = "/getMilestoneResouces", params = { "projectId" ,"planningEndDate"}, method = RequestMethod.GET)
	@ResponseBody
	public List<ReportDTO> getMilestoneResouces(@RequestParam Long projectId,String planningEndDate) {

		return allocationDetailsService.getPeopleForMilestone(projectId,planningEndDate);
	}

	@RequestMapping(value = "/getProjectTimeSheet", params = { "projectId",
			"hiveDate" }, method = RequestMethod.GET)
	@ResponseBody
	public List<EmpoloyeeHiveActivityReport> getProjectTimeSheet(
			@RequestParam Long projectId, @RequestParam String hiveDate,
			HttpServletResponse httpServletResponse) {
		return allocationDetailsService
				.getProjectTimeSheet(projectId, hiveDate);
	}
	
	@RequestMapping(value = "/directoryProjects", params = { "projectId" }, method = RequestMethod.GET)
	@ResponseBody
	public List<ReportDTO> getEmployeeProjectDetails(@RequestParam Long projectId,
			HttpServletResponse httpServletResponse) {
		return allocationDetailsService.getEmployeeProjectDetails(projectId);
	}
	
	@RequestMapping(value = "/getProjectEmployees", params = { "projectId" }, method = RequestMethod.GET)
	@ResponseBody
	public List<SearchEmpDetailsDTO> getProjectEmployees(@RequestParam Long projectId,
			HttpServletResponse httpServletResponse) {
		return allocationDetailsService.getProjectEmployees(projectId);
	}
	
	
	
	@PutMapping("/{allocationId}/end-date")
	public ResponseEntity<AllocationDTO> updateEndDate(
	        @PathVariable Long allocationId, 
	        @RequestParam String endDate) {
	    
	    // Validate the endDate format (e.g., yyyy-MM-dd)
	    if (!isValidDate(endDate)) {
	        return ResponseEntity.badRequest().body(null); // Return bad request if the date is invalid
	    }

	    try {
	        // Call the service to update the end date
	        AllocationDTO updatedAllocation = allocationDetailsService.updateEndDate(allocationId, endDate);
	        
	        if (updatedAllocation == null) {
	            return ResponseEntity.notFound().build(); // Return 404 if allocation was not found
	        }

	        // Return the updated allocation wrapped in a ResponseEntity with 200 OK status
	        return ResponseEntity.ok(updatedAllocation);
	    } catch (Exception e) {
	        // Handle any errors and return a 500 Internal Server Error
	        return ResponseEntity.status(500).body(null);
	    }
	}

	/**
	 * Helper method to validate the date format (yyyy-MM-dd)
	 * @param date the date string
	 * @return true if the date is in the correct format
	 */
	private boolean isValidDate(String date) {
	    try {
	        // Try parsing the date to check if it’s valid
	        java.time.LocalDate.parse(date);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}

	
	
	
	
	
	
	
}
