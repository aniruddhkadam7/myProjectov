package com.raybiztech.projecttailoring.dto;

import java.util.Set;

public class ProcessHeadDto {

	private Long id;
	private Long processHeadId;
	private String processHeadname;
	private Set<ProcessSubHeadDto> processSubHeadsDto;
	private String tailoredCount;
	private String waivedCount;
	private String documentCount;
	private String processSubHeadCount;
	private String processCount;

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

	public Set<ProcessSubHeadDto> getProcessSubHeadsDto() {
		return processSubHeadsDto;
	}

	public void setProcessSubHeadsDto(Set<ProcessSubHeadDto> processSubHeadsDto) {
		this.processSubHeadsDto = processSubHeadsDto;
	}

	public Long getProcessHeadId() {
		return processHeadId;
	}

	public void setProcessHeadId(Long processHeadId) {
		this.processHeadId = processHeadId;
	}

	public String getTailoredCount() {
		return tailoredCount;
	}

	public void setTailoredCount(String tailoredCount) {
		this.tailoredCount = tailoredCount;
	}

	public String getDocumentCount() {
		return documentCount;
	}

	public void setDocumentCount(String documentCount) {
		this.documentCount = documentCount;
	}

	public String getProcessSubHeadCount() {
		return processSubHeadCount;
	}

	public void setProcessSubHeadCount(String processSubHeadCount) {
		this.processSubHeadCount = processSubHeadCount;
	}

	public String getProcessCount() {
		return processCount;
	}

	public void setProcessCount(String processCount) {
		this.processCount = processCount;
	}

	public String getWaivedCount() {
		return waivedCount;
	}

	public void setWaivedCount(String waivedCount) {
		this.waivedCount = waivedCount;
	}

	
	
}
