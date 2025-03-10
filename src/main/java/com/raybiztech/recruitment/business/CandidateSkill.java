/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.business;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author hari
 */
public class CandidateSkill implements Serializable {

    private static final long serialVersionUID = 8075382522652624666L;

    private Long skillId;
    private String skillName;

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1997, 13).append(skillId).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {

        if (obj instanceof CandidateSkill) {
            final CandidateSkill skill = (CandidateSkill) obj;

            return new EqualsBuilder().append(skillId, skill.getSkillId()).isEquals();
        } else {
            return false;
        }
    }
}
