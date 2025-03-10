package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;
import java.util.List;

public class MultipleAllocationDTO implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Long> employeeIds;
    
    private String empFirstName;
	
	private String empLastName;
	
	private String projectName;
	
	private Long projectId;
	
	private String startDate;
	
	private String endDate;
	
	private String Allocation;
	
	private boolean billable;
	
	private String comments;
	
	public List<Long> getEmployeeIds() {
		return employeeIds;
	}

	public void setEmployeeIds(List<Long> employeeIds) {
		this.employeeIds = employeeIds;
	}

	public String getEmpFirstName() {
		return empFirstName;
	}

	public void setEmpFirstName(String empFirstName) {
		this.empFirstName = empFirstName;
	}

	public String getEmpLastName() {
		return empLastName;
	}

	public void setEmpLastName(String empLastName) {
		this.empLastName = empLastName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAllocation() {
		return Allocation;
	}

	public void setAllocation(String allocation) {
		Allocation = allocation;
	}

	public boolean isBillable() {
		return billable;
	}

	public void setBillable(boolean billable) {
		this.billable = billable;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	
}
