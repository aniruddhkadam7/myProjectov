package com.raybiztech.compliance.dto;

public class ComplianceTaskSubmitDTO {
	
	private Long complianceTaskSubmitId;
	private String actualSubmitDate;
	private String comments;
	private ComplianceTaskDTO complianceTaskDTO;
	
	public Long getComplianceTaskSubmitId() {
		return complianceTaskSubmitId;
	}
	public void setComplianceTaskSubmitId(Long complianceTaskSubmitId) {
		this.complianceTaskSubmitId = complianceTaskSubmitId;
	}
	public String getActualSubmitDate() {
		return actualSubmitDate;
	}
	public void setActualSubmitDate(String actualSubmitDate) {
		this.actualSubmitDate = actualSubmitDate;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public ComplianceTaskDTO getComplianceTaskDTO() {
		return complianceTaskDTO;
	}
	public void setComplianceTaskDTO(ComplianceTaskDTO complianceTaskDTO) {
		this.complianceTaskDTO = complianceTaskDTO;
	}
	
	
}
