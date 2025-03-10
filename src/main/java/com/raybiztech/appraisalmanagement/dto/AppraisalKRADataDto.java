package com.raybiztech.appraisalmanagement.dto;

import java.util.Set;

public class AppraisalKRADataDto {
	private Long id;

	private String name;
	private String description;
	private int count;
	private Double designationKraPercentage ;

	private Set<AppraisalKPIDataDto> kpis;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<AppraisalKPIDataDto> getKpis() {
		return kpis;
	}

	public void setKpis(Set<AppraisalKPIDataDto> kpis) {
		this.kpis = kpis;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Double getDesignationKraPercentage() {
		return designationKraPercentage;
	}

	public void setDesignationKraPercentage(Double designationKraPercentage) {
		this.designationKraPercentage = designationKraPercentage;
	}
	

}
