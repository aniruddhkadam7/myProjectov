/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.dto;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author naresh
 */
public class SkillDTO implements Serializable, Comparable<SkillDTO> {

    private static final long serialVersionUID = 1L;
    private Long skillId;
    private Long categoryId;
    private String categoryType;
    private String skillType;
    private String expYear;
    private String expMonth;
    private String competency;
    private String comments;
    private EmployeeDTO employee;
    private List<EmployeeSkillLookUpDTO> lookUpDTOs;
    private String categoryTypeId;

    public String getCategoryTypeId() {
        return categoryTypeId;
    }

    public void setCategoryTypeId(String categoryTypeId) {
        this.categoryTypeId = categoryTypeId;
    }
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<EmployeeSkillLookUpDTO> getLookUpDTOs() {
        return lookUpDTOs;
    }

    public void setLookUpDTOs(List<EmployeeSkillLookUpDTO> lookUpDTOs) {
        this.lookUpDTOs = lookUpDTOs;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public Long getSkillId() {
        return skillId;
    }

    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getSkillType() {
        return skillType;
    }

    public void setSkillType(String skillType) {
        this.skillType = skillType;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getCompetency() {
        return competency;
    }

    public void setCompetency(String competency) {
        this.competency = competency;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
        final SkillDTO other = (SkillDTO) obj;
        return new EqualsBuilder().append(skillId, other.skillId)
                .isEquals();
    }

    @Override
    public int compareTo(SkillDTO skill) {
        return this.skillId.compareTo(skill.getSkillId());
    }

}
