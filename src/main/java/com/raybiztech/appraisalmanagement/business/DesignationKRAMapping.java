package com.raybiztech.appraisalmanagement.business;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DesignationKRAMapping implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2135129207091012244L;

    private Long id;

    private Designation designation;

    private AppraisalCycle cycle;

    private Set<KRA> kraLookups;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Designation getDesignation() {
        return designation;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }

    public Set<KRA> getKraLookups() {
        return kraLookups;
    }

    public void setKraLookups(Set<KRA> kraLookups) {
        this.kraLookups = kraLookups;
    }

    public AppraisalCycle getCycle() {
        return cycle;
    }

    public void setCycle(AppraisalCycle cycle) {
        this.cycle = cycle;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1967, 77).append(this.getId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DesignationKRAMapping) {
            final DesignationKRAMapping other = (DesignationKRAMapping) obj;
            return new EqualsBuilder().append(id, other.getId()).isEquals();
        } else {
            return false;
        }
    }

}
