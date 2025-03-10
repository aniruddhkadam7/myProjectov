package com.raybiztech.recruitment.business;

import java.io.Serializable;

public class Relations implements Serializable {
	private static final long serialVersionUID = 8075382522652624666L;
    private Long id;
    private String relationName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	
}