package com.raybiztech.recruitment.business;

import java.io.Serializable;

public class EmploymentType implements Serializable{

	    private static final long serialVersionUID = 8075382522652624666L;
	    private Long id;
	    private String employmentType;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getEmploymentType() {
			return employmentType;
		}
		public void setEmploymentType(String employmentType) {
			this.employmentType = employmentType;
		}
}