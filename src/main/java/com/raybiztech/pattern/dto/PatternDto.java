package com.raybiztech.pattern.dto;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;

public class PatternDto {
	
	private Long id;
	private String Type;
	private String Pattern;
	private String createdBy;
	private String createdDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getPattern() {
		return Pattern;
	}
	public void setPattern(String pattern) {
		Pattern = pattern;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	

}
