package com.raybiztech.handbook.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.checklist.business.ChecklistSection;

public class HandbookItem implements Serializable, Comparable<HandbookItem> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private int displayOrder;
	private String title;
	private String description;
	private String pageName;
	private EmpDepartment empDepartment;
	private String type;
	private ChecklistSection section;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
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

	@Override
	public int compareTo(HandbookItem o) {
		return (int) (this.displayOrder - o.displayOrder);
	}
}
