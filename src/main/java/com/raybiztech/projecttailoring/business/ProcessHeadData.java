package com.raybiztech.projecttailoring.business;

import java.io.Serializable;
import java.util.Set;

public class ProcessHeadData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5510545720319912991L;

	private Long id;
	private ProcessHead processHead;
	private Set<ProcessSubHeadData> processSubHeadData;
	private String processSubHeadCount;
	private String processCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ProcessHead getProcessHead() {
		return processHead;
	}

	public void setProcessHead(ProcessHead processHead) {
		this.processHead = processHead;
	}

	public Set<ProcessSubHeadData> getProcessSubHeadData() {
		return processSubHeadData;
	}

	public void setProcessSubHeadData(Set<ProcessSubHeadData> processSubHeadData) {
		this.processSubHeadData = processSubHeadData;
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

	
}
