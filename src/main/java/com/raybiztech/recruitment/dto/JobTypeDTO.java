package com.raybiztech.recruitment.dto;

import java.io.Serializable;

public class JobTypeDTO implements Serializable{

    private static final long serialVersionUID = 8075382522652624666L;
    private Long id;
    private String jobType;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
}