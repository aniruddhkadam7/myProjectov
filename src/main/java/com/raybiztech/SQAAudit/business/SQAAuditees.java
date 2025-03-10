package com.raybiztech.SQAAudit.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;

public class SQAAuditees implements Serializable,Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2826625063914819556L;
	
	private Long id;
	private Employee auditee;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Employee getAuditee() {
		return auditee;
	}
	public void setAuditee(Employee auditee) {
		this.auditee = auditee;
	}
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	

}
