package com.raybiztech.contact.business;

import java.io.Serializable;
import java.util.Date;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.checklist.business.ChecklistSection;
import com.raybiztech.date.Second;

public class ContactInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String title;
	private String description;
	private String pageName;
	private EmpDepartment empDepartment;
	private String type;
	private ChecklistSection section;
	private String userName;
	private Date createdDate;
	private Date updatedDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	

	public EmpDepartment getEmpDepartment() {
		return empDepartment;
	}

	public void setEmpDepartment(EmpDepartment empDepartment) {
		this.empDepartment = empDepartment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public ChecklistSection getSection() {
		return section;
	}

	public void setSection(ChecklistSection section) {
		this.section = section;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	


}
