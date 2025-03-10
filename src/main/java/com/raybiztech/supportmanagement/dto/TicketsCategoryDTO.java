package com.raybiztech.supportmanagement.dto;


public class TicketsCategoryDTO {
	

	private Long categoryId;
	private String categoryName;
	private Long departmentId;
	private String departmentName;
        private Boolean mealType;
	
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
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
