package com.raybiztech.appraisals.dto;

import java.io.Serializable;

public class EmpDepartmentDTO implements Serializable {

	/**
    *
    */
	private static final long serialVersionUID = -8408877456338601644L;

	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
