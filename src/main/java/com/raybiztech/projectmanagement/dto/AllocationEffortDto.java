package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;

public class AllocationEffortDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6634066905156251117L;
	
	private Float employeeAllocationEffort;
	private Long baselineEffort;
	private Float resourceAllocationOverrun;
	
	public Float getEmployeeAllocationEffort() {
		return employeeAllocationEffort;
	}
	public void setEmployeeAllocationEffort(Float employeeAllocationEffort) {
		this.employeeAllocationEffort = employeeAllocationEffort;
	}
	public Long getBaselineEffort() {
		return baselineEffort;
	}
	public void setBaselineEffort(Long baselineEffort) {
		this.baselineEffort = baselineEffort;
	}
	public Float getResourceAllocationOverrun() {
		return resourceAllocationOverrun;
	}
	public void setResourceAllocationOverrun(Float resourceAllocationOverrun) {
		this.resourceAllocationOverrun = resourceAllocationOverrun;
	}
	
	
	

}
