package com.raybiztech.projectmanagement.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dto.ReportDTO;

@Component
public class ReportBuilder {

	public List<ReportDTO> convertEmplyeeTOReportList(
			List<Employee> employeelist) {
		List<ReportDTO> reportDTOs = new ArrayList<ReportDTO>();
		for (Employee employee : employeelist) {
			ReportDTO dto = null;
			if (employee.getAllocations().isEmpty()) {
				dto = new ReportDTO();
				dto.setEmployeeId(employee.getEmployeeId());
				dto.setEmpFirstName(employee.getFirstName());
				dto.setEmpLastName(employee.getLastName());
				dto.setDepartment(employee.getDepartmentName());
				reportDTOs.add(dto);
			} else {
				Map<Project, AllocationDetails> allocations = employee
						.getAllocations();
				for (Map.Entry<Project, AllocationDetails> entry : allocations
						.entrySet()) {
					dto = new ReportDTO();
					dto.setEmployeeId(employee.getEmployeeId());
					dto.setEmpFirstName(employee.getFirstName());
					dto.setEmpLastName(employee.getLastName());
					dto.setDepartment(employee.getDepartmentName());
					dto.setProjectId(entry.getKey().getId());
					dto.setProjectName(entry.getKey().getProjectName());
					dto.setAllocation(entry.getValue().getPercentage()
							.toString("#0",false));
					dto.setBillable(entry.getValue().getBillable());
					dto.setEndDate(entry.getValue().getPeriod().getMaximum()
							.toString("dd/MM/yyyy"));
					dto.setStartDate(entry.getValue().getPeriod().getMinimum()
							.toString("dd/MM/yyyy"));
					reportDTOs.add(dto);
				}
			}
			
		}

		return reportDTOs;
	}
	public List<ReportDTO> convertEmplyeeTOReport(List<Employee> employeelist){
		List<ReportDTO> dtolist = new ArrayList<ReportDTO>();
		ReportDTO dto = null;
		for (Employee employee : employeelist) {
		dto = new ReportDTO();
		dto.setEmployeeId(employee.getEmployeeId());
		dto.setEmpFirstName(employee.getFirstName());
		dto.setEmpLastName(employee.getLastName());
		dtolist.add(dto);
		}
		return dtolist;
	}
	

}
