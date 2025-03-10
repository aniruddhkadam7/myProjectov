/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisalmanagement.dto;

import java.io.Serializable;

/**
 *
 * @author anil
 */
public class SearchQueryParamsInKRA implements Serializable {

    private static final long serialVersionUID = -288798395710020L;
    
    private Long departmentId;
    private Long designationId;
    private Long kraId;
    private String multipleSearch;
    private Integer startIndex;
    private Integer endIndex;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public String getMultipleSearch() {
        return multipleSearch;
    }

    public void setMultipleSearch(String multipleSearch) {
        this.multipleSearch = multipleSearch;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public Long getKraId() {
        return kraId;
    }

    public void setKraId(Long kraId) {
        this.kraId = kraId;
    }
    
    
}
