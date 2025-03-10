package com.raybiztech.projectMetrics.business;

import java.io.Serializable;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class ProjectSprintsAudit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8886760292089101390L;

	private Long id;
	private Integer versionId;
	private String versionName;
	private String columnName;
	private String oldValue;
	private String newValue;
	private Long projectId;
	private String projectName;
	private Second modifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersionId() {
		return versionId;
	}

	public void setVersionId(Integer versionId) {
		this.versionId = versionId;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Second getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Second modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}
