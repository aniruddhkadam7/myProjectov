package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;

public class ProjectRequestMailDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1697302463576310602L;
	
	private Long id;
	private String cc;
	private String bcc;
	private String createdBy;
	private String createdDate;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	
	

}
