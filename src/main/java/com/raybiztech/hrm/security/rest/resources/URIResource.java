package com.raybiztech.hrm.security.rest.resources;

import java.io.Serializable;

public class URIResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5379768557685947058L;

	private Long id;
	private String uri;
	private String method;
	private Boolean access;
	private String empRole;

	public URIResource() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Boolean getAccess() {
		return access;
	}

	public void setAccess(Boolean access) {
		this.access = access;
	}

	public String getEmpRole() {
		return empRole;
	}

	public void setEmpRole(String empRole) {
		this.empRole = empRole;
	}

}
