package com.raybiztech.hiveworkpackages.dto;

import java.io.Serializable;
import java.util.Date;

import com.raybiztech.date.Second;

public class work_packagesDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -720892086613726098L;
	
	
	private Long id;
	private Long type_id;
	private Long project_id;
	private String subject;
	private String description;
	private String due_date;
	private Long category_id;
	private Long status_id;
	private Long assigned_to_id;
	private Long priority_id;
	private Long fixed_version_id;
	private Long author_id;
	private Long lock_version;
	private Long done_ratio;
	private Float estimated_hours;
	private String created_at;
	private String updated_at;
	private String start_date;
	private Long parent_id;
	private Long responsible_id;
	private Long root_id;
	private Long lft;
	private Long rgt;
	private Long position;
	private Long story_points;
	private Float remaining_hours;
	private Long cost_object_id;
	private String projectname;
	private String typename;
	private String assigneeName;
	private String responsibleName;
	private Long severity;
	private Long testCaseType;
	private String testCaseCondition;
	private String testCaseExceptedResult;
	private String testCaseActualResult;
	private Boolean testCaseAutomated;
	private Long testCaseExecutionCycle;
	private String encryptedWorkpackage;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getType_id() {
		return type_id;
	}
	public void setType_id(Long type_id) {
		this.type_id = type_id;
	}
	public Long getProject_id() {
		return project_id;
	}
	public void setProject_id(Long project_id) {
		this.project_id = project_id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	public Long getCategory_id() {
		return category_id;
	}
	public void setCategory_id(Long category_id) {
		this.category_id = category_id;
	}
	public Long getStatus_id() {
		return status_id;
	}
	public void setStatus_id(Long status_id) {
		this.status_id = status_id;
	}
	public Long getAssigned_to_id() {
		return assigned_to_id;
	}
	public void setAssigned_to_id(Long assigned_to_id) {
		this.assigned_to_id = assigned_to_id;
	}
	public Long getPriority_id() {
		return priority_id;
	}
	public void setPriority_id(Long priority_id) {
		this.priority_id = priority_id;
	}
	public Long getFixed_version_id() {
		return fixed_version_id;
	}
	public void setFixed_version_id(Long fixed_version_id) {
		this.fixed_version_id = fixed_version_id;
	}
	public Long getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(Long author_id) {
		this.author_id = author_id;
	}
	public Long getLock_version() {
		return lock_version;
	}
	public void setLock_version(Long lock_version) {
		this.lock_version = lock_version;
	}
	public Long getDone_ratio() {
		return done_ratio;
	}
	public void setDone_ratio(Long done_ratio) {
		this.done_ratio = done_ratio;
	}
	public Float getEstimated_hours() {
		return estimated_hours;
	}
	public void setEstimated_hours(Float estimated_hours) {
		this.estimated_hours = estimated_hours;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public Long getParent_id() {
		return parent_id;
	}
	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}
	public Long getResponsible_id() {
		return responsible_id;
	}
	public void setResponsible_id(Long responsible_id) {
		this.responsible_id = responsible_id;
	}
	public Long getRoot_id() {
		return root_id;
	}
	public void setRoot_id(Long root_id) {
		this.root_id = root_id;
	}
	public Long getLft() {
		return lft;
	}
	public void setLft(Long lft) {
		this.lft = lft;
	}
	public Long getRgt() {
		return rgt;
	}
	public void setRgt(Long rgt) {
		this.rgt = rgt;
	}
	public Long getPosition() {
		return position;
	}
	public void setPosition(Long position) {
		this.position = position;
	}
	public Long getStory_points() {
		return story_points;
	}
	public void setStory_points(Long story_points) {
		this.story_points = story_points;
	}
	public Float getRemaining_hours() {
		return remaining_hours;
	}
	public void setRemaining_hours(Float remaining_hours) {
		this.remaining_hours = remaining_hours;
	}
	public Long getCost_object_id() {
		return cost_object_id;
	}
	public void setCost_object_id(Long cost_object_id) {
		this.cost_object_id = cost_object_id;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public String getAssigneeName() {
		return assigneeName;
	}
	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}
	public String getResponsibleName() {
		return responsibleName;
	}
	public void setResponsibleName(String responsibleName) {
		this.responsibleName = responsibleName;
	}
	public Long getSeverity() {
		return severity;
	}
	public void setSeverity(Long severity) {
		this.severity = severity;
	}
	public Long getTestCaseType() {
		return testCaseType;
	}
	public void setTestCaseType(Long testCaseType) {
		this.testCaseType = testCaseType;
	}
	public String getTestCaseCondition() {
		return testCaseCondition;
	}
	public void setTestCaseCondition(String testCaseCondition) {
		this.testCaseCondition = testCaseCondition;
	}
	public String getTestCaseExceptedResult() {
		return testCaseExceptedResult;
	}
	public void setTestCaseExceptedResult(String testCaseExceptedResult) {
		this.testCaseExceptedResult = testCaseExceptedResult;
	}
	public String getTestCaseActualResult() {
		return testCaseActualResult;
	}
	public void setTestCaseActualResult(String testCaseActualResult) {
		this.testCaseActualResult = testCaseActualResult;
	}
	public Boolean getTestCaseAutomated() {
		return testCaseAutomated;
	}
	public void setTestCaseAutomated(Boolean testCaseAutomated) {
		this.testCaseAutomated = testCaseAutomated;
	}
	public Long getTestCaseExecutionCycle() {
		return testCaseExecutionCycle;
	}
	public void setTestCaseExecutionCycle(Long testCaseExecutionCycle) {
		this.testCaseExecutionCycle = testCaseExecutionCycle;
	}
	public String getEncryptedWorkpackage() {
		return encryptedWorkpackage;
	}
	public void setEncryptedWorkpackage(String encryptedWorkpackage) {
		this.encryptedWorkpackage = encryptedWorkpackage;
	}

	
	
	
}
