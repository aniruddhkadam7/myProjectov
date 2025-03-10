/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author anil
 */
public class ProjectAuditDTO implements Serializable {

    private static final long serialVersionUID = -9038694851473926910L;
    private Long id;
    private String projectName;
    private String oldProjectName;
    //private Long managerId;
    private String startdate;
    private String oldStartDate;
    private String enddate;
    private String OldEndDate;
    private String description;
    private String oldDescription;
    private String status;
    private String oldStatus;
    private String managerName;
    private String oldManagerName;
    private Long count;
    private String allocation;
    private String oldAllocation;
    //private boolean billable;
    private Boolean isAllocated;
    private Boolean oldIsAllocated;
    private Long employeeId;
    private String health;
    private String oldHealth;
    private String client;
    private String oldClient;
    private String type;
    private String oldType;
    //private Long clientId;

    private String fromDate;
    private String oldFromDate;

    private String toDate;
    private String oldToDate;

    private String commnets;
    private String oldComments;
    private Boolean billable;
   private Boolean oldBillable;
    private String modifiedDate;
    private String modifiedBy;
    private String persistType;
    private Boolean projectDetailsFlag;
    private String employeeName;
    private String oldEmployeeName;
    private String columnName;
    private String oldvalue;
    private String newValue;
    private String additionalInfo;
    private List<String> allocatedEmpNames;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

//    public Long getManagerId() {
//        return managerId;
//    }
//
//    public void setManagerId(Long managerId) {
//        this.managerId = managerId;
//    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getAllocation() {
        return allocation;
    }

    public void setAllocation(String allocation) {
        this.allocation = allocation;
    }

   

    public Boolean isIsAllocated() {
        return isAllocated;
    }

    public void setIsAllocated(Boolean isAllocated) {
        this.isAllocated = isAllocated;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public Long getClientId() {
//        return clientId;
//    }
//
//    public void setClientId(Long clientId) {
//        this.clientId = clientId;
//    }

//    public Float getAllocationPercantage() {
//        return allocationPercantage;
//    }
//
//    public void setAllocationPercantage(Float allocationPercantage) {
//        this.allocationPercantage = allocationPercantage;
//    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getCommnets() {
        return commnets;
    }

    public void setCommnets(String commnets) {
        this.commnets = commnets;
    }

    public Boolean isBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
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

    public Boolean isProjectDetailsFlag() {
        return projectDetailsFlag;
    }

    public void setProjectDetailsFlag(Boolean projectDetailsFlag) {
        this.projectDetailsFlag = projectDetailsFlag;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getOldvalue() {
        return oldvalue;
    }

    public void setOldvalue(String oldvalue) {
        this.oldvalue = oldvalue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getOldProjectName() {
        return oldProjectName;
    }

    public void setOldProjectName(String oldProjectName) {
        this.oldProjectName = oldProjectName;
    }

    public String getOldStartDate() {
        return oldStartDate;
    }

    public void setOldStartDate(String oldStartDate) {
        this.oldStartDate = oldStartDate;
    }

    public String getOldManagerName() {
        return oldManagerName;
    }

    public void setOldManagerName(String oldManagerName) {
        this.oldManagerName = oldManagerName;
    }

    public String getOldClient() {
        return oldClient;
    }

    public void setOldClient(String oldClient) {
        this.oldClient = oldClient;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getOldEndDate() {
        return OldEndDate;
    }

    public void setOldEndDate(String OldEndDate) {
        this.OldEndDate = OldEndDate;
    }

    public String getOldDescription() {
        return oldDescription;
    }

    public void setOldDescription(String oldDescription) {
        this.oldDescription = oldDescription;
    }

    public Boolean isOldIsAllocated() {
        return oldIsAllocated;
    }

    public void setOldIsAllocated(Boolean oldIsAllocated) {
        this.oldIsAllocated = oldIsAllocated;
    }

    public String getOldHealth() {
        return oldHealth;
    }

    public void setOldHealth(String oldHealth) {
        this.oldHealth = oldHealth;
    }

    public String getOldType() {
        return oldType;
    }

    public void setOldType(String oldType) {
        this.oldType = oldType;
    }

    public String getOldFromDate() {
        return oldFromDate;
    }

    public void setOldFromDate(String oldFromDate) {
        this.oldFromDate = oldFromDate;
    }

    public String getOldToDate() {
        return oldToDate;
    }

    public void setOldToDate(String oldToDate) {
        this.oldToDate = oldToDate;
    }

    public String getOldComments() {
        return oldComments;
    }

    public void setOldComments(String oldComments) {
        this.oldComments = oldComments;
    }

    public Boolean isOldBillable() {
        return oldBillable;
    }

    public void setOldBillable(Boolean oldBillable) {
        this.oldBillable = oldBillable;
    }

    public String getOldEmployeeName() {
        return oldEmployeeName;
    }

    public void setOldEmployeeName(String oldEmployeeName) {
        this.oldEmployeeName = oldEmployeeName;
    }

    public String getOldAllocation() {
        return oldAllocation;
    }

    public void setOldAllocation(String oldAllocation) {
        this.oldAllocation = oldAllocation;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public List<String> getAllocatedEmpNames() {
        return allocatedEmpNames;
    }

    public void setAllocatedEmpNames(List<String> allocatedEmpNames) {
        this.allocatedEmpNames = allocatedEmpNames;
    }
    
    
    

}
