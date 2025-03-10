package com.raybiztech.projectmanagement.builder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.commons.Percentage;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.AllocationDetailsAudit;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dto.MultipleAllocationDTO;
import com.raybiztech.projectmanagement.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;

@Component("allocationDetailsBuilder")
public class AllocationDetailsBuilder {
    @Autowired
    SecurityUtils securityUtils;
    @Autowired
    DAO dao;

	public AllocationDetails convertReportDTOtoAllocationDeatils(
			ReportDTO reportDTO) {

		AllocationDetails allocationDetails = new AllocationDetails();
		allocationDetails.setBillable(reportDTO.isBillable());
		allocationDetails.setComments(reportDTO.getComments());
		allocationDetails.setPercentage(Percentage.valueOf(Double
				.valueOf(reportDTO.getAllocation())));
		try {
			Date fromDate = Date.parse(reportDTO.getStartDate(), "dd/MM/yyyy");
			Date toDate = Date.parse(reportDTO.getEndDate(), "dd/MM/yyyy");
			allocationDetails.setPeriod(new DateRange(fromDate, toDate));
		} catch (ParseException parseException) {

		}
		return allocationDetails;
	}

	public AllocationDetails convertMultipleAllocationDTOtoAllocationDeatils(
			MultipleAllocationDTO multipleAllocationDTO) {

		AllocationDetails allocationDetails = new AllocationDetails();
		allocationDetails.setBillable(multipleAllocationDTO.isBillable());
		allocationDetails.setComments(multipleAllocationDTO.getComments());
		allocationDetails.setIsAllocated(Boolean.TRUE);
		allocationDetails.setPercentage(Percentage.valueOf(Double
				.valueOf(multipleAllocationDTO.getAllocation())));
		try {
			Date fromDate = Date.parse(multipleAllocationDTO.getStartDate(),
					"dd/MM/yyyy");
			Date toDate = Date.parse(multipleAllocationDTO.getEndDate(),
					"dd/MM/yyyy");
			allocationDetails.setPeriod(new DateRange(fromDate, toDate));
		} catch (ParseException parseException) {

		}
		return allocationDetails;
	}
        public AllocationDetailsAudit convertTOAllocationDetailsAudit(AllocationDetails details,Employee allocateEmployee,Long id,String persistType){
             Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
            Employee employee = dao.findBy(Employee.class, loggedInEmpId);
           // System.out.println("allocation%%%%");
            AllocationDetailsAudit audit = new AllocationDetailsAudit();
            audit.setBillable(details.getBillable());
            audit.setComments(details.getComments());
            audit.setEmployee(allocateEmployee);
            audit.setIsAllocated(details.getIsAllocated());
            audit.setPercentage(details.getPercentage());
            audit.setPeriod(details.getPeriod());
            audit.setModifiedDate(new Second());
            audit.setProjectId(id);
            audit.setModifiedBy(employee.getFullName());
            audit.setPersistType(persistType);
            
            
            return audit;
            
        }

	public List<ReportDTO> convertEntityToDTO(
			List<AllocationDetails> allocationDetailsList, Project project) {

		List<ReportDTO> reportDTOList = new ArrayList<ReportDTO>();

		for (AllocationDetails details : allocationDetailsList) {
			if(details.getEmployee().getStatusName().equalsIgnoreCase("Active") ){
			ReportDTO dTO = new ReportDTO();
			dTO.setEmployeeId(details.getEmployee().getEmployeeId());
			dTO.setEmpFirstName(details.getEmployee().getFirstName());
			dTO.setEmpLastName(details.getEmployee().getLastName());
			dTO.setComments(details.getComments());
			dTO.setBillable(details.getBillable());
			// dTO.setProjectId(details.getProjectid());
			dTO.setStartDate(details.getPeriod().getMinimum()
					.toString("dd/MM/yyyy"));
			dTO.setEndDate(details.getPeriod().getMaximum()
					.toString("dd/MM/yyyy"));
			dTO.setProjectName(project.getProjectName());
			dTO.setProjectId(project.getId());
			dTO.setDepartment(details.getEmployee().getDepartmentName());
			dTO.setUserName(details.getEmployee().getFullName());
			dTO.setDesigination(details.getEmployee().getDesignation());
			dTO.setAllocation(details.getPercentage().toString("#0", false));
			if(details.getEmployee().getStatusName().equalsIgnoreCase("Active")){
			dTO.setIsAllocated(details.getIsAllocated());
			}else{
				dTO.setIsAllocated(Boolean.FALSE);
			}
			reportDTOList.add(dTO);
		}
		}
		return reportDTOList;
	}

	public List<ReportDTO> getBillableResourcesDTO(
			List<AllocationDetails> allocationDetailsList) {

		List<ReportDTO> reportDTOList = new ArrayList<ReportDTO>();

		for (AllocationDetails details : allocationDetailsList) {

			ReportDTO dTO = new ReportDTO();
			dTO.setEmployeeId(details.getEmployee().getEmployeeId());
			dTO.setEmpFirstName(details.getEmployee().getFullName());
			dTO.setComments(details.getComments());
			dTO.setDepartment(details.getEmployee().getDepartmentName());
			dTO.setDesigination(details.getEmployee().getDesignation());
			reportDTOList.add(dTO);

		}
		return reportDTOList;

	}
}
