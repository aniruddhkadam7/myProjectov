package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;
import java.util.Date;

import com.raybiztech.date.Second;

public class work_packages implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5411065138040726993L;

	private Long id;
	private types type_id;
	private projects project_id;
	private String subject;
	private String description;
	private Date due_date;
	private categories category_id;
	private statuses status_id;
	private users assigned_to_id;
	private Enumerations priority_id;
	private versions fixed_version_id;
	private users author_id;
	private Long lock_version;
	private Long done_ratio;
	private Float estimated_hours;
	private Second created_at;
	private Second updated_at;
	private Date start_date;
	private Long parent_id;
	private users responsible_id;
	private Long root_id;
	private Long lft;
	private Long rgt;
	private Long position;
	private Long story_points;
	private Float remaining_hours;
	private Long cost_object_id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public types getType_id() {
		return type_id;
	}
	public void setType_id(types type_id) {
		this.type_id = type_id;
	}
	public projects getProject_id() {
		return project_id;
	}
	public void setProject_id(projects project_id) {
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
	public Date getDue_date() {
		return due_date;
	}
	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}
	public categories getCategory_id() {
		return category_id;
	}
	public void setCategory_id(categories category_id) {
		this.category_id = category_id;
	}
	public statuses getStatus_id() {
		return status_id;
	}
	public void setStatus_id(statuses status_id) {
		this.status_id = status_id;
	}
	public users getAssigned_to_id() {
		return assigned_to_id;
	}
	public void setAssigned_to_id(users assigned_to_id) {
		this.assigned_to_id = assigned_to_id;
	}
	public Enumerations getPriority_id() {
		return priority_id;
	}
	public void setPriority_id(Enumerations priority_id) {
		this.priority_id = priority_id;
	}
	public versions getFixed_version_id() {
		return fixed_version_id;
	}
	public void setFixed_version_id(versions fixed_version_id) {
		this.fixed_version_id = fixed_version_id;
	}
	public users getAuthor_id() {
		return author_id;
	}
	public void setAuthor_id(users author_id) {
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
	public Second getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Second created_at) {
		this.created_at = created_at;
	}
	public Second getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Second updated_at) {
		this.updated_at = updated_at;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Long getParent_id() {
		return parent_id;
	}
	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}
	public users getResponsible_id() {
		return responsible_id;
	}
	public void setResponsible_id(users responsible_id) {
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
	
	
	

}
