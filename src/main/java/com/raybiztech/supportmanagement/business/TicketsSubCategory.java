package com.raybiztech.supportmanagement.business;

import java.io.Serializable;

import com.raybiztech.date.Second;

public class TicketsSubCategory implements Serializable,Cloneable{
	
	
	/**
	 * @author sravani
	 */
	private static final long serialVersionUID = 3381845267731647645L;
	
	private Long id;
	private TicketsCategory ticketsCategory;
	public String subCategoryName;
	public String estimatedTime;
	public Boolean workFlow;
	private Long createdBy;
	private Second createdDate;
	public Integer levelOfHierarchy;
	
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
	public TicketsCategory getTicketsCategory() {
		return ticketsCategory;
	}
	public void setTicketsCategory(TicketsCategory ticketsCategory) {
		this.ticketsCategory = ticketsCategory;
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
	
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	public Integer getLevelOfHierarchy() {
		return levelOfHierarchy;
	}
	public void setLevelOfHierarchy(Integer levelOfHierarchy) {
		this.levelOfHierarchy = levelOfHierarchy;
	}

}
