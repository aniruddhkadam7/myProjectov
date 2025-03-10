package com.raybiztech.leavemanagement.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;

public class CarryForwardLeave implements Serializable, Comparable<CarryForwardLeave> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7257357806113682305L;

	private Long id;
	private Employee employee;
	private Double daysCredited;
	private String comments;
	private LeaveType leaveType;
	private Date leaveCreditedOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Double getDaysCredited() {
		return daysCredited;
	}

	public void setDaysCredited(Double daysCredited) {
		this.daysCredited = daysCredited;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public Date getLeaveCreditedOn() {
		return leaveCreditedOn;
	}

	public void setLeaveCreditedOn(Date leaveCreditedOn) {
		this.leaveCreditedOn = leaveCreditedOn;
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
		final CarryForwardLeave other = (CarryForwardLeave) obj;
		return new EqualsBuilder().append(id, other.id).isEquals();
	}

	@Override
	public int compareTo(CarryForwardLeave o) {
		// TODO Auto-generated method stub
		return (int) (this.id - o.id);
	}

}
