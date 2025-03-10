/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.dto;

import java.io.Serializable;

/**
 *
 * @author naresh
 */
public class EmployeeSkillLookUpDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long skillId;
    private String skill;

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
}
