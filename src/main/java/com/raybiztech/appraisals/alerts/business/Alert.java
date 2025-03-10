/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.alerts.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;

/**
 *
 * @author naresh
 */
public class Alert implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4593563832585563411L;
	private Long id;
    private String msg;
    private Employee employee;
    private Second msgDate;
    private String alertType;
    private Boolean alertStatus;
    private Boolean latestSatatus;
    private Second insertOn;
    
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Second getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(Second msgDate) {
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

    public Second getInsertOn() {
        return insertOn;
    }

    public void setInsertOn(Second insertOn) {
        this.insertOn = insertOn;
    }
          
          
}
