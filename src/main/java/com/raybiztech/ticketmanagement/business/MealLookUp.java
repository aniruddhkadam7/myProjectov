package com.raybiztech.ticketmanagement.business;

import java.io.Serializable;

public class MealLookUp implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1138287944019983222L;
	private Long id;
	private String mealName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMealName() {
		return mealName;
	}
	public void setMealName(String mealName) {
		this.mealName = mealName;
	}
	
	

}
