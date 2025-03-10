package com.raybiztech.delegation.dto;

import java.util.Set;

public class DelegationDTO {
	
	private Set <Long> employeeId;
	private Long managerId;
	private String roletype;
	
	public Set <Long> getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Set <Long> employeeId) {
		this.employeeId = employeeId;
	}
	public Long getManagerId() {
		return managerId;
	}
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	public String getRoletype() {
		return roletype;
	}
	public void setRoletype(String roletype) {
		this.roletype = roletype;
	}
	
	
	
}
