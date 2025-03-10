package com.raybiztech.supportmanagement.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.date.Second;

public class TicketsCategory implements Serializable {

	/**
	 * @author sravani
	 */
	private static final long serialVersionUID = -1245680362892518681L;

	private Long id;
	private EmpDepartment empDepartment;
	private String categoryName;
	private Long createdBy;
	private Second createdDate;
	private Boolean mealType;

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmpDepartment getEmpDepartment() {
		return empDepartment;
	}

	public void setEmpDepartment(EmpDepartment empDepartment) {
		this.empDepartment = empDepartment;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Boolean isMealType() {
		return mealType;
	}

	public void setMealType(Boolean mealType) {
		this.mealType = mealType;
	}

}
