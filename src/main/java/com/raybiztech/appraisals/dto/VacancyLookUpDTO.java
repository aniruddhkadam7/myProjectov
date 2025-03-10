package com.raybiztech.appraisals.dto;


public class VacancyLookUpDTO {
	Long VacancyLookUpId;
	String name;
	int displayOrder;
	
	public Long getVacancyLookUpId() {
		return VacancyLookUpId;
	}
	public void setVacancyLookUpId(Long vacancyLookUpId) {
		VacancyLookUpId = vacancyLookUpId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	
}
