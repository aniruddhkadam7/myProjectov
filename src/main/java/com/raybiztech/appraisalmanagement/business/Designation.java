package com.raybiztech.appraisalmanagement.business;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.date.Second;
import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Designation implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1110605467980263719L;

    private Long id;

    private String name;

    private String code;
    
    private EmpDepartment empDepartment;
    
    private Long createdBy;
     
    private Second createdDate;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EmpDepartment getEmpDepartment() {
        return empDepartment;
    }

    public void setEmpDepartment(EmpDepartment empDepartment) {
        this.empDepartment = empDepartment;
    }
    

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1967, 77).append(this.getId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Designation) {
            final Designation other = (Designation) obj;
            return new EqualsBuilder().append(id, other.getId()).isEquals();
        } else {
            return false;
        }
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Second getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Second createdDate) {
        this.createdDate = createdDate;
    }
    
    

}
