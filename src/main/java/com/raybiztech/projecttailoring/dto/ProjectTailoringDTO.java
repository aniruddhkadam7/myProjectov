package com.raybiztech.projecttailoring.dto;

import java.util.Set;

public class ProjectTailoringDTO {
private Long id;
private Long projectId;
private Set<ProcessHeadDto> processHeaddto;
private String tailoringStatus;
private String rejectComments;

public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public Long getProjectId() {
	return projectId;
}
public void setProjectId(Long projectId) {
	this.projectId = projectId;
}
public Set<ProcessHeadDto> getProcessHeaddto() {
	return processHeaddto;
}
public void setProcessHeaddto(Set<ProcessHeadDto> processHeaddto) {
	this.processHeaddto = processHeaddto;
}
public String getTailoringStatus() {
	return tailoringStatus;
}
public void setTailoringStatus(String tailoringStatus) {
	this.tailoringStatus = tailoringStatus;
}
public String getRejectComments() {
	return rejectComments;
}
public void setRejectComments(String rejectComments) {
	this.rejectComments = rejectComments;
}



}
