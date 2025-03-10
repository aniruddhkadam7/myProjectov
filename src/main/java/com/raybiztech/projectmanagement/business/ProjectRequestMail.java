package com.raybiztech.projectmanagement.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class ProjectRequestMail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1614079519096003688L;
	private Long id;
	private String cc;
	private String bcc;
	private Long createdBy;
	private Second createdDate;
	
	
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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	

}
