package com.raybiztech.appraisalmanagement.dto;

import java.util.List;
import java.util.Set;


public class KRADto {

    private Long id;
    private String name;
    private String description;
    private Set<KPIDto> kpiLookps;
    private int count;
    private Boolean checkType;
    private String designationName;
    private Long designationId;
    private String departmentName;
    private Long departmentId;
    private Double designationKraPercentage;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<KPIDto> getKpiLookps() {
        return kpiLookps;
    }

    public void setKpiLookps(Set<KPIDto> kpiLookps) {
        this.kpiLookps = kpiLookps;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getCheckType() {
        return checkType;
    }

    public void setCheckType(Boolean checkType) {
        this.checkType = checkType;
    }

   

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public Long getDesignationId() {
        return designationId;
    }

    public void setDesignationId(Long designationId) {
        this.designationId = designationId;
    }

    public Double getDesignationKraPercentage() {
        return designationKraPercentage;
    }

    public void setDesignationKraPercentage(Double designationKraPercentage) {
        this.designationKraPercentage = designationKraPercentage;
    }

   
    
}
