package com.raybiztech.recruitment.business;

import java.io.Serializable;
public class BloodGroup implements Serializable {

    private static final long serialVersionUID = 8075382522652624666L;
    private Long id;
    private String groupName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}