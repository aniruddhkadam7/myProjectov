package com.raybiztech.recruitment.dto;

import java.io.Serializable;
public class MaritalStatusDTO implements Serializable {

    private static final long serialVersionUID = 8075382522652624666L;
    private Long id;
    private String maritalStatus;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMaritalStatus() {
		return maritalStatus;
	}
	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

}