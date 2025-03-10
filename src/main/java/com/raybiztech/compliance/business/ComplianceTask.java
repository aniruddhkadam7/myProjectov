package com.raybiztech.compliance.business;

import java.io.Serializable;

import com.raybiztech.date.Calendar;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
	
public class ComplianceTask implements Serializable{

	/**
	 * @author venkatesh urlana 
	 */
	private static final long serialVersionUID = -2600043656658504198L;
	
	private Long complianceTaskId;
	private String complianceName;
	private Date complianceDate;
	private ComplianceTaskStatus complianceTaskStatus;
	private Long createdBy;
	private Compliance compliance;
	private Second createdDate;
	
	
	public Second getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public String getComplianceName() {
		return complianceName;
	}
	public void setComplianceName(String complianceName) {
		this.complianceName = complianceName;
	}
	public Long getComplianceTaskId() {
		return complianceTaskId;
	}
	public void setComplianceTaskId(Long complianceTaskId) {
		this.complianceTaskId = complianceTaskId;
	}
	public Compliance getCompliance() {
		return compliance;
	}
	public void setCompliance(Compliance compliance) {
		this.compliance = compliance;
	}

	@Override
	public String toString() {
		return "ComplianceTask [complianceTaskId=" + complianceTaskId + ", complianceName=" + complianceName
				+ ", createdBy=" + createdBy + ", compliance=" + compliance + ", createdDate=" + createdDate
				+ ", getCreatedDate()=" + getCreatedDate() + ", getCreatedBy()=" + getCreatedBy()
				+ ", getComplianceName()=" + getComplianceName() + ", getComplianceTaskId()=" + getComplianceTaskId()
				+ ", getCompliance()=" + getCompliance() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
	public Date getComplianceDate() {
		return complianceDate;
	}
	public void setComplianceDate(Date complianceDate) {
		this.complianceDate = complianceDate;
	}
	public ComplianceTaskStatus getComplianceTaskStatus() {
		return complianceTaskStatus;
	}
	public void setComplianceTaskStatus(ComplianceTaskStatus complianceTaskStatus) {
		this.complianceTaskStatus = complianceTaskStatus;
	}
	
	
}
