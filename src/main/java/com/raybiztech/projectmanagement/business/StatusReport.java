/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.business;

import java.io.Serializable;

import com.raybiztech.date.Date;

/**
 *
 * @author naresh
 */
public class StatusReport implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long id;
    private String prevstatus;
    private Date prevDate;
    private String nextstatus;
    private Date nextDate;
    private Date addOn;
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrevstatus() {
        return prevstatus;
    }

    public void setPrevstatus(String prevstatus) {
        this.prevstatus = prevstatus;
    }

    public Date getPrevDate() {
        return prevDate;
    }

    public void setPrevDate(Date prevDate) {
        this.prevDate = prevDate;
    }

    public String getNextstatus() {
        return nextstatus;
    }

    public void setNextstatus(String nextstatus) {
        this.nextstatus = nextstatus;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

    public Date getAddOn() {
        return addOn;
    }

    public void setAddOn(Date addOn) {
        this.addOn = addOn;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        StatusReport other = (StatusReport) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
