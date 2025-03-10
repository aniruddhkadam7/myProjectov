package com.raybiztech.recruitment.dto;

import java.io.Serializable;

public class QualificationCategoryDTO implements Serializable{
	
	private static final long serialVersionUID = 8075382522652624666L;
    private Long id;
    private String qualificationCategory;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQualificationCategory() {
		return qualificationCategory;
	}
	public void setQualificationCategory(String qualificationCategory) {
		this.qualificationCategory = qualificationCategory;
	}

}