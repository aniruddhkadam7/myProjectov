package com.raybiztech.projecttailoring.business;

import java.io.Serializable;

public class ProcessSubHead implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6503991683936356193L;
	private Long id;
	private ProcessHead processHead;
	private String processSubHeadName;
	private String processName;
	private String documentName;
	private String responsible;
	private String common;
	private String links;
	private Boolean status;
	private Long order;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ProcessHead getProcessHead() {
		return processHead;
	}
	public void setProcessHead(ProcessHead processHead) {
		this.processHead = processHead;
	}
	public String getProcessSubHeadName() {
		return processSubHeadName;
	}
	public void setProcessSubHeadName(String processSubHeadName) {
		this.processSubHeadName = processSubHeadName;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	public String getCommon() {
		return common;
	}
	public void setCommon(String common) {
		this.common = common;
	}
	public String getLinks() {
		return links;
	}
	public void setLinks(String links) {
		this.links = links;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public Long getOrder() {
		return order;
	}
	public void setOrder(Long order) {
		this.order = order;
	}
	
	
	
	
	
	
	
}
