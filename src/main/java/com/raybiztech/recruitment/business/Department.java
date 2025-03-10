/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.business;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.businesscalendar.BusinessCalendar;

/**
 *
 * @author naresh
 */
public class Department implements Serializable {

	/**
     *
     */
	private static final long serialVersionUID = -8408877456338601638L;

	private Long id;
	private String name;
	private BusinessCalendar businessCalendar;
	

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

	public BusinessCalendar getBusinessCalendar() {
		return businessCalendar;
	}

	public void setBusinessCalendar(BusinessCalendar businessCalendar) {
		this.businessCalendar = businessCalendar;
	}


	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Department) {
			Department department = (Department) obj;
			return new EqualsBuilder().append(id, department.getId())
					.isEquals();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(11, 37).append(id).toHashCode();
	}
}
