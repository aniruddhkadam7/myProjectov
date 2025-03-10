package com.raybiztech.projecttailoring.business;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProcessHead implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3459491417225523693L;

	private Long id;
	private String processHeadname;
	private Set<ProcessSubHead> processSubHeads;
	private String processSubHeadCount;
	private String processCount;

	public Set<ProcessSubHead> getProcessSubHeads() {
		return processSubHeads;
	}

	public void setProcessSubHeads(Set<ProcessSubHead> processSubHeads) {
		this.processSubHeads = processSubHeads;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProcessHeadname() {
		return processHeadname;
	}

	public void setProcessHeadname(String processHeadname) {
		this.processHeadname = processHeadname;
	}

	public String getProcessSubHeadCount() {
		return processSubHeadCount;
	}

	public String getProcessCount() {
		return processCount;
	}

	public void setProcessSubHeadCount(String processSubHeadCount) {
		this.processSubHeadCount = processSubHeadCount;
	}

	public void setProcessCount(String processCount) {
		this.processCount = processCount;
	}
	
	public Set<ProcessSubHead> getActiveProcessSubHeadList(){
		
		Set<ProcessSubHead> processSubHeadSet = null;
		
		if(this.getProcessSubHeads() != null) {
			processSubHeadSet = new HashSet<ProcessSubHead>();
			for(ProcessSubHead subHead : this.getProcessSubHeads()) {
				if(subHead.getStatus() == Boolean.TRUE) {
					processSubHeadSet.add(subHead);
				}
			}
		}
		
		
		return Collections.unmodifiableSet(processSubHeadSet);
		
	}

	
}
