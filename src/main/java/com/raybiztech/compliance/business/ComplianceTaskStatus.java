package com.raybiztech.compliance.business;

public enum ComplianceTaskStatus {
INPROGRESS("In Progress"),CLOSED("Closed");
	
	private String complianceTaskStatus;

	private ComplianceTaskStatus(String complianceTaskStatus) {
		this.complianceTaskStatus = complianceTaskStatus;
	}

	public String getComplianceTaskStatus() {
		return complianceTaskStatus;
	}
}
