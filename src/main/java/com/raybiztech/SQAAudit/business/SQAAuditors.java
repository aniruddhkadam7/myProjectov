package com.raybiztech.SQAAudit.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;

public class SQAAuditors implements Serializable,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7900394135601862341L;
	
	private Long id;
	private Employee auditor;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Employee getAuditor() {
		return auditor;
	}
	public void setAuditor(Employee auditor) {
		this.auditor = auditor;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	
	
}
