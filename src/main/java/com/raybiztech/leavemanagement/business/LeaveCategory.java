package com.raybiztech.leavemanagement.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class LeaveCategory implements Serializable,Comparable<LeaveCategory> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5622274795094032299L;

	private Long id;
	private String name;
	private LeaveType leaveType;

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

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(1989, 55).append(id).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final LeaveCategory other = (LeaveCategory) obj;
		return new EqualsBuilder().append(id, other.id)
				.isEquals();
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(LeaveCategory o) {
		// TODO Auto-generated method stub
		return (int) (this.getName().compareTo(o.getName()));
	}
	
	

}
