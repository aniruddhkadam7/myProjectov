package com.raybiztech.recruitment.dto;

public class CompaniesDTO {
	
	private String companyNmae;
	private Long candidatesCount;
	private Long  employeesCount;
	
	
	public String getCompanyNmae() {
		return companyNmae;
	}
	public void setCompanyNmae(String companyNmae) {
		this.companyNmae = companyNmae;
	}
	public Long getCandidatesCount() {
		return candidatesCount;
	}
	public void setCandidatesCount(Long candidatesCount) {
		this.candidatesCount = candidatesCount;
	}
	public Long getEmployeesCount() {
		return employeesCount;
	}
	public void setEmployeesCount(Long employeesCount) {
		this.employeesCount = employeesCount;
	}
	

}
