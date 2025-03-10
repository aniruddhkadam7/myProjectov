package com.raybiztech.leavemanagement.dto;

import java.io.Serializable;

public class LeaveCategoryDTO implements Serializable,Comparable<LeaveCategoryDTO>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3866867759494645536L;
	private Long id;
	private String name;
	private String leaveType;

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



	public String getLeaveType() {
		return leaveType;
	}



	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}



	@Override
	public int compareTo(LeaveCategoryDTO o) {
		// TODO Auto-generated method stub
		return (int) (this.getName().compareTo(o.getName()));
	}
	

}