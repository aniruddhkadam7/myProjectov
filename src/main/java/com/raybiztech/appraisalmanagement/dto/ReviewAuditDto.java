package com.raybiztech.appraisalmanagement.dto;

import java.util.Set;



public class ReviewAuditDto {
	private Long id;
	private String comments;
	private String status;
	private String createdDate;
	private String employeeName;
	private Long appraisalFormId;
	private String kpiName;
	private String oldValue;
	private String newValue;
	private Set<KPIReviewDto> kpiReviewDtos;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Long getAppraisalFormId() {
		return appraisalFormId;
	}
	public void setAppraisalFormId(Long appraisalFormId) {
		this.appraisalFormId = appraisalFormId;
	}
	public String getKpiName() {
		return kpiName;
	}
	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public Set<KPIReviewDto> getKpiReviewDtos() {
		return kpiReviewDtos;
	}
	public void setKpiReviewDtos(Set<KPIReviewDto> kpiReviewDtos) {
		this.kpiReviewDtos = kpiReviewDtos;
	}
	
	
	
}
