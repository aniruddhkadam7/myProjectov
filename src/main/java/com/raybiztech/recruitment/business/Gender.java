package com.raybiztech.recruitment.business;

import java.io.Serializable;

public class Gender implements Serializable {

    private static final long serialVersionUID = 8075382522652624666L;
    private Long id;
    private String genderName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGenderName() {
		return genderName;
	}
	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}
    
}