package com.raybiztech.appraisalmanagement.dto;

public class AppraisalFormListDto {

	private Long id;
	// private EmployeeDTO employee;
	private Long empId;
	private String employeeName;
	private String formStatus;
	private Integer formStatusvalue;
	private String appraisalFormStatus;
	private Double overallAvgRating;
	private Long finalRating;
	private String pendingWith;
	private String empDepartmentName;
	private String empDesignationName;
	private Double empAvgRating;
	private String manager1Name;
	private String cycleStartDate;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFormStatus() {
		return formStatus;
	}

	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}

	public Integer getFormStatusvalue() {
		return formStatusvalue;
	}

	public void setFormStatusvalue(Integer formStatusvalue) {
		this.formStatusvalue = formStatusvalue;
	}

	public String getAppraisalFormStatus() {
		return appraisalFormStatus;
	}

	public void setAppraisalFormStatus(String appraisalFormStatus) {
		this.appraisalFormStatus = appraisalFormStatus;
	}

	public Double getOverallAvgRating() {
		return overallAvgRating;
	}

	public void setOverallAvgRating(Double overallAvgRating) {
		this.overallAvgRating = overallAvgRating;
	}

	public Long getFinalRating() {
		return finalRating;
	}

	public void setFinalRating(Long finalRating) {
		this.finalRating = finalRating;
	}

	public String getPendingWith() {
		return pendingWith;
	}

	public void setPendingWith(String pendingWith) {
		this.pendingWith = pendingWith;
	}

	public String getEmpDepartmentName() {
		return empDepartmentName;
	}

	public void setEmpDepartmentName(String empDepartmentName) {
		this.empDepartmentName = empDepartmentName;
	}

	public String getEmpDesignationName() {
		return empDesignationName;
	}

	public void setEmpDesignationName(String empDesignationName) {
		this.empDesignationName = empDesignationName;
	}

	public Double getEmpAvgRating() {
		return empAvgRating;
	}

	public void setEmpAvgRating(Double empAvgRating) {
		this.empAvgRating = empAvgRating;
	}

	public String getManager1Name() {
		return manager1Name;
	}

	public void setManager1Name(String manager1Name) {
		this.manager1Name = manager1Name;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	
	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getCycleStartDate() {
		return cycleStartDate;
	}

	public void setCycleStartDate(String cycleStartDate) {
		this.cycleStartDate = cycleStartDate;
	}

	
	

}
