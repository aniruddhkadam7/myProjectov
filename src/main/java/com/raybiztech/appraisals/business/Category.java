/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.business;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author naresh
 */
public class Category implements Serializable, Comparable<Category> {

    private static final long serialVersionUID = 1L;
    private Long categoryId;
    private String categoryType;
    private Set<EmployeeSkillLookUp> employeeSkill;

    public Set<EmployeeSkillLookUp> getEmployeeSkill() {
        return employeeSkill;
    }

    public void setEmployeeSkill(Set<EmployeeSkillLookUp> employeeSkill) {
        this.employeeSkill = employeeSkill;
    }

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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1989, 55).append(categoryId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Category other = (Category) obj;
        return new EqualsBuilder().append(categoryId, other.categoryId)
                .isEquals();
    }

    @Override
    public int compareTo(Category category) {
    	Employee employee=new Employee();
        employee.setFirstName("pooja");
        return this.categoryId.compareTo(category.getCategoryId());
        
    }

}
