package com.raybiztech.separation.business;

public enum SeparationStatus {
	SUBMITRESIGNATION("Resigned"), DISCUSSIONWITHREPORTINGMANAGER("Pending Approval"), UNDERNOTICE(
			"Under Notice"), RELIEVED("Relieved"), REVOKED("Resignation Withdrawn"),ABSCOND("Absconding"),TERMINATED("Terminated");

	private String status;

	private SeparationStatus(String status) {
		this.status = status;
	}

	public String getSeperationStatus() {
		return status;
	}

}
