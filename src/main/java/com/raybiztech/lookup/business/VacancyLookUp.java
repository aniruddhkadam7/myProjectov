package com.raybiztech.lookup.business;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class VacancyLookUp implements Serializable {

 
    
    
    /**
     *
     */
    private static final long serialVersionUID = -6566731007576117187L;
    private Long VacancyLookUpId;
    private String name;
    private String jobCode;
    
    public Long getVacancyLookUpId() {
        return VacancyLookUpId;
    }

    public void setVacancyLookUpId(Long vacancyLookUpId) {
        VacancyLookUpId = vacancyLookUpId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }
    
   
    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 19).append(VacancyLookUpId).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        final VacancyLookUp other = (VacancyLookUp) obj;
        return new EqualsBuilder().append(other.VacancyLookUpId, VacancyLookUpId).isEquals();
    }
}
