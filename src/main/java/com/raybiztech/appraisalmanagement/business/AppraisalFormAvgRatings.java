/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisalmanagement.business;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author anil
 */
public class AppraisalFormAvgRatings implements Serializable{
    
    private static final long serialVersionUID = 91361553450952120L;
    
    private Long id;
    private Long employeeId;
    private Integer level;
    private Double defaultAvgRating;
    private Double adjustedAvgRating;
    private String finalFeedback;
    private Boolean iAgree;
    private String departmentName;
    private String designationName;
    private String employeeName;
    public String discussionSummary;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Double getDefaultAvgRating() {
        return defaultAvgRating;
    }

    public void setDefaultAvgRating(Double defaultAvgRating) {
        this.defaultAvgRating = defaultAvgRating;
    }

    public Double getAdjustedAvgRating() {
        return adjustedAvgRating;
    }

    public void setAdjustedAvgRating(Double adjustedAvgRating) {
        this.adjustedAvgRating = adjustedAvgRating;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getFinalFeedback() {
        return finalFeedback;
    }

    public void setFinalFeedback(String finalFeedback) {
        this.finalFeedback = finalFeedback;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.employeeId);
        hash = 71 * hash + Objects.hashCode(this.level);
        hash = 71 * hash + Objects.hashCode(this.defaultAvgRating);
        hash = 71 * hash + Objects.hashCode(this.adjustedAvgRating);
        hash = 71 * hash + Objects.hashCode(this.finalFeedback);
        return hash;
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
        final AppraisalFormAvgRatings other = (AppraisalFormAvgRatings) obj;
        if (!Objects.equals(this.finalFeedback, other.finalFeedback)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.employeeId, other.employeeId)) {
            return false;
        }
        if (!Objects.equals(this.level, other.level)) {
            return false;
        }
        if (!Objects.equals(this.defaultAvgRating, other.defaultAvgRating)) {
            return false;
        }
        if (!Objects.equals(this.adjustedAvgRating, other.adjustedAvgRating)) {
            return false;
        }
        return true;
    }

    public Boolean getiAgree() {
        return iAgree;
    }

    public void setiAgree(Boolean iAgree) {
        this.iAgree = iAgree;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDiscussionSummary() {
        return discussionSummary;
    }

    public void setDiscussionSummary(String discussionSummary) {
        this.discussionSummary = discussionSummary;
    }

}
