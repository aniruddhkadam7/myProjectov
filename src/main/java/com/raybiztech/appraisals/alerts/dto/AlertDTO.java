/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.alerts.dto;

import java.io.Serializable;

/**
 *
 * @author naresh
 */
public class AlertDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -224179155727721021L;
	private Long id;
	private String msg;
	private String empId;
	private String msgDate;
	private String alertType;
	private Boolean alertStatus;
	private Boolean latestSatatus;
	private Long projectId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getMsgDate() {
		return msgDate;
	}

	public void setMsgDate(String msgDate) {
		this.msgDate = msgDate;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public Boolean getAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(Boolean alertStatus) {
		this.alertStatus = alertStatus;
	}

	public Boolean getLatestSatatus() {
		return latestSatatus;
	}

	public void setLatestSatatus(Boolean latestSatatus) {
		this.latestSatatus = latestSatatus;
	}

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

}
