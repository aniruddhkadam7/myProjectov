/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author naresh
 */
public class EmployeeSkillLookUp implements Serializable, Comparable<EmployeeSkillLookUp> {

    private static final long serialVersionUID = 1L;
    private Long skillId;
    private String skill;
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1989, 55).append(skillId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EmployeeSkillLookUp other = (EmployeeSkillLookUp) obj;
        return new EqualsBuilder().append(skillId, other.skillId)
                .isEquals();
    }

    @Override
    public int compareTo(EmployeeSkillLookUp lookUp) {
        return this.skillId.compareTo(lookUp.getSkillId());
    }

}
