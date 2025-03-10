package com.raybiztech.spentHours.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;

public class SpentTime implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9204084618697282647L;

	private Long id;
	private Employee employee;
	private String employeeName;
	private Date date;
	private String spentHours;
	private Integer flag;
	private Date insertedOn;

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

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSpentHours() {
		return spentHours;
	}

	public void setSpentHours(String spentHours) {
		this.spentHours = spentHours;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Date getInsertedOn() {
		return insertedOn;
	}

	public void setInsertedOn(Date insertedOn) {
		this.insertedOn = insertedOn;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return new HashCodeBuilder(1989, 55).append(id).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SpentTime other = (SpentTime) obj;
		return new EqualsBuilder().append(id, other.id).isEquals();
	}

	
	
}
