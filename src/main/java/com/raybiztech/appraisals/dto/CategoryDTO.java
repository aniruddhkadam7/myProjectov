/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.dto;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author naresh
 */
public class CategoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long categoryId;
    private String categoryType;
    private Set<EmployeeSkillLookUpDTO> employeeSkill;

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public Set<EmployeeSkillLookUpDTO> getEmployeeSkill() {
        return employeeSkill;
    }

    public void setEmployeeSkill(Set<EmployeeSkillLookUpDTO> employeeSkill) {
        this.employeeSkill = employeeSkill;
    }

}
