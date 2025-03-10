package com.raybiztech.supportmanagement.dto;

public class TicketsSubCategoryDTO {

	
	private Long subCategoryId;
	private String subCategoryName;
	private String estimatedTime;
	private Boolean workFlow;
	private Long categoryId;
	private String categoryName;
	private String departmentName;
	private Long departmentId;
	private String levelOfHierarchy;
	
	public Long getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(Long subCategoryId) {
		this.subCategoryId = subCategoryId;
	}
	public String getSubCategoryName() {
		return subCategoryName;
	}
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
	
	public Boolean getWorkFlow() {
		return workFlow;
	}
	public void setWorkFlow(Boolean workFlow) {
		this.workFlow = workFlow;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public String getLevelOfHierarchy() {
		return levelOfHierarchy;
	}
	public void setLevelOfHierarchy(String levelOfHierarchy) {
		this.levelOfHierarchy = levelOfHierarchy;
	}


	
}
