/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.business;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.commons.Percentage;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import java.io.Serializable;

/**
 *
 * @author anil
 */
public class AllocationDetailsAudit implements Serializable {

    private static final long serialVersionUID = -1533427740154460328L;

    private Long id;

    private Percentage percentage;

    private DateRange period;

    private String comments;

    private Boolean billable;
    private Employee employee;
    private Long projectId;
    private Boolean isAllocated;
    private Second modifiedDate;
    private String modifiedBy;
    private String persistType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Percentage getPercentage() {
        return percentage;
    }

    public void setPercentage(Percentage percentage) {
        this.percentage = percentage;
    }

    public DateRange getPeriod() {
        return period;
    }

    public void setPeriod(DateRange period) {
        this.period = period;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

  

    public Boolean getIsAllocated() {
        return isAllocated;
    }

    public void setIsAllocated(Boolean isAllocated) {
        this.isAllocated = isAllocated;
    }

    public Second getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Second modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getPersistType() {
        return persistType;
    }

    public void setPersistType(String persistType) {
        this.persistType = persistType;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
    
}
