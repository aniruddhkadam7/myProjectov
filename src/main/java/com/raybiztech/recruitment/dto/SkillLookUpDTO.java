package com.raybiztech.recruitment.dto;

import java.io.Serializable;

public class SkillLookUpDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8894843678482420692L;

	private Long id;

	private String name;

	private Integer diplayOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDiplayOrder() {
		return diplayOrder;
	}

	public void setDiplayOrder(Integer diplayOrder) {
		this.diplayOrder = diplayOrder;
	}

}
