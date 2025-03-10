package com.raybiztech.projecttailoring.business;

import java.io.Serializable;

public class ProcessArea implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private ProcessHead processHead;
	
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
	public ProcessHead getProcessHead() {
		return processHead;
	}
	public void setProcessHead(ProcessHead processHead) {
		this.processHead = processHead;
	}
	
	

}
