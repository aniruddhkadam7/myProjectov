/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeInOffice.business;

import java.io.Serializable;

import com.raybiztech.date.Date;

/**
 *
 * @author naresh
 */
public class TimeInOffice implements Serializable, Comparable<TimeInOffice> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3112169406385339639L;
	private Long id;
	private String empID;
	private String empName;
	private Date dt;
	private String spentHours;
	private Integer flag;
	private Date insertedOn;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmpID() {
		return empID;
	}

	public void setEmpID(String empID) {
		this.empID = empID;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
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

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	@Override
	public int compareTo(TimeInOffice o) {

		int value = 0;

		if (this.getDt().isAfter(o.getDt()))
			value = 1;
		else if (this.getDt().isBefore(o.getDt()))
			value = -1;
		else
			value = 0;
		return value;
	}

}
