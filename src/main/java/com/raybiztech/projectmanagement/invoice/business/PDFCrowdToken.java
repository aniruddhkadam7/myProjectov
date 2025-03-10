package com.raybiztech.projectmanagement.invoice.business;

import java.io.Serializable;

public class PDFCrowdToken implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 199579758318254688L;
	
	private Long id;
	private String userName;
	private String token;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	

}
