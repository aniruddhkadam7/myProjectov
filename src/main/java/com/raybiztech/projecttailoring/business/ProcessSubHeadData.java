package com.raybiztech.projecttailoring.business;

import java.io.Serializable;

public class ProcessSubHeadData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4170967830534494065L;
	
	private Long id;
	private ProcessSubHead processSubHead;
	private String specificToProject;
	private String comments;
	private String sqaComments;
	private String sqaApproval;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ProcessSubHead getProcessSubHead() {
		return processSubHead;
	}
	public void setProcessSubHead(ProcessSubHead processSubHead) {
		this.processSubHead = processSubHead;
	}
	public String getSpecificToProject() {
		return specificToProject;
	}
	public String getComments() {
		return comments;
	}
	public String getSqaComments() {
		return sqaComments;
	}
	public String getSqaApproval() {
		return sqaApproval;
	}
	public void setSpecificToProject(String specificToProject) {
		this.specificToProject = specificToProject;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public void setSqaComments(String sqaComments) {
		this.sqaComments = sqaComments;
	}
	public void setSqaApproval(String sqaApproval) {
		this.sqaApproval = sqaApproval;
	}
	
	
	
	

}
