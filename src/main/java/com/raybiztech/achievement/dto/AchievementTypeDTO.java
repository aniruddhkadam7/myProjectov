package com.raybiztech.achievement.dto;

import java.io.Serializable;

/**
 * @author Aprajita
 */

public class AchievementTypeDTO implements Serializable {

	private static final long serialVersionUID = 61378616004244403L;

	private Long id;
	private String typeName;
	private String createdBy;
	private String createdDate;
	private String updatedBy;
	private String updatedDate;
	private Boolean status;
	private Long order;
	private Boolean timeperiodrequired;
	private Boolean daterequired;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getOrder() {
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	public Boolean getTimeperiodrequired() {
		return timeperiodrequired;
	}

	public void setTimeperiodrequired(Boolean timeperiodrequired) {
		this.timeperiodrequired = timeperiodrequired;
	}

	public Boolean getDaterequired() {
		return daterequired;
	}

	public void setDaterequired(Boolean daterequired) {
		this.daterequired = daterequired;
	}
	

}
