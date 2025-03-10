package com.raybiztech.appraisals.observation.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.recruitment.business.Document;

public class Observation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3141342901111035648L;
	
	private Long id;
	private String description;
	private Date date;
	private Employee employee;
	private Employee addedBy;
	private Integer rating;
	private String comments;
	private Document document;
	private String obsFilePath;
	private Date observationMonth;
	
	public Long getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}
	public Date getDate() {
		return date;
	}
	public Employee getAddedBy() {
		return addedBy;
	}
	public Integer getRating() {
		return rating;
	}
	public String getComments() {
		return comments;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setAddedBy(Employee addedBy) {
		this.addedBy = addedBy;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Employee getEmployee() {
		return employee;
	}
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	public String getObsFilePath() {
		return obsFilePath;
	}
	public void setObsFilePath(String obsFilePath) {
		this.obsFilePath = obsFilePath;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public Date getObservationMonth() {
		return observationMonth;
	}
	public void setObservationMonth(Date observationMonth) {
		this.observationMonth = observationMonth;
	}
	
	
	

}
