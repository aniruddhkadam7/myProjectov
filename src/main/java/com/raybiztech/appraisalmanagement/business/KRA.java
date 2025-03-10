package com.raybiztech.appraisalmanagement.business;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.date.Second;
import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class KRA implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8909362168639524151L;
    private Long id;
    private String name;
    private String description;
    private Set<KPI> kpiLookps;
    private EmpDepartment empDepartment;
    private Designation designation;
    private Double designationKraPercentage; 
    private Long createdBy;
    private Long updatedBy;
    private Second createdDate;
    private Second updatedDate;
    
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

    public Set<KPI> getKpiLookps() {
        return kpiLookps;
    }

    public void setKpiLookps(Set<KPI> kpiLookps) {
        this.kpiLookps = kpiLookps;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public EmpDepartment getEmpDepartment() {
        return empDepartment;
    }

    public void setEmpDepartment(EmpDepartment empDepartment) {
        this.empDepartment = empDepartment;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1967, 77).append(id).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof KRA) {
            final KRA other = (KRA) obj;
            return new EqualsBuilder().append(id, other.getId()).isEquals();
        } else {
            return false;
        }
    }

    public Double getDesignationKraPercentage() {
        return designationKraPercentage;
    }

    public void setDesignationKraPercentage(Double designationKraPercentage) {
        this.designationKraPercentage = designationKraPercentage;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Second getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Second createdDate) {
        this.createdDate = createdDate;
    }

    public Second getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Second updatedDate) {
        this.updatedDate = updatedDate;
    }
    
}
