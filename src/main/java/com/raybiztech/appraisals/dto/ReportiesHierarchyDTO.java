package com.raybiztech.appraisals.dto;


public class ReportiesHierarchyDTO {

	private Long managerId;
	private String managerName;
	private Long reporteeId;
	private String reporteeName;
	private String allcoationDetails;
	private String mobile; 

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Long getReporteeId() {
		return reporteeId;
	}

	public void setReporteeId(Long reporteeId) {
		this.reporteeId = reporteeId;
	}

	public String getReporteeName() {
		return reporteeName;
	}

	public void setReporteeName(String reporteeName) {
		this.reporteeName = reporteeName;
	}

	public String getAllcoationDetails() {
		return allcoationDetails;
	}

	public void setAllcoationDetails(String allcoationDetails) {
		this.allcoationDetails = allcoationDetails;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
