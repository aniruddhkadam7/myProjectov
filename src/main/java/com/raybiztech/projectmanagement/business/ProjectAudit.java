/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.business;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import java.io.Serializable;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author anil
 */
public class ProjectAudit implements Serializable {

    Logger logger = Logger.getLogger(ProjectAudit.class);
    private static final long serialVersionUID = 11L;

    private Long id;
    private String projectName;
    private Employee projectManager;
    private DateRange period;
    private String description;
    private ProjectStatus status;
    private String health;
    private Client client;
    private ProjectType type;
    private Second modifiedDate;
    private String modifiedBy;
    private String persistType;
    private Set<AllocationDetailsAudit> allocationDetailsAudit;
    private Long projectId;

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

    public Employee getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(Employee projectManager) {
        this.projectManager = projectManager;
    }

    public DateRange getPeriod() {
        return period;
    }

    public void setPeriod(DateRange period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public ProjectType getType() {
        return type;
    }

    public void setType(ProjectType type) {
        this.type = type;
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

    public Set<AllocationDetailsAudit> getAllocationDetailsAudit() {
        return allocationDetailsAudit;
    }

    public void setAllocationDetailsAudit(Set<AllocationDetailsAudit> allocationDetailsAudit) {
        this.allocationDetailsAudit = allocationDetailsAudit;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

}
