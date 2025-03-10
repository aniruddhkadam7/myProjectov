/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Category;
import com.raybiztech.appraisals.business.EmployeeSkillLookUp;
import com.raybiztech.appraisals.dto.CategoryDTO;
import com.raybiztech.appraisals.dto.EmployeeSkillLookUpDTO;

/**
 *
 * @author naresh
 */
@Component("categoryBuilder")
public class CategoryBuilder {

    public List<CategoryDTO> convertFromCategoryToCategoryDTO(List<Category> categoryList) {

        List<CategoryDTO> categoryDTOs = null;

        if (!categoryList.isEmpty()) {
            categoryDTOs = new ArrayList<CategoryDTO>();

            for (Category category : categoryList) {
                CategoryDTO dTO = new CategoryDTO();
                dTO.setCategoryId(category.getCategoryId());
                dTO.setCategoryType(category.getCategoryType());
                categoryDTOs.add(dTO);
            }

        }
        return categoryDTOs;

    }

    public List<EmployeeSkillLookUpDTO> convertFromEmployeeSkillLookUpToEmployeeSkillLookUpDTO(Set<EmployeeSkillLookUp> EmployeeSkillLookUpList) {

        List<EmployeeSkillLookUpDTO> esludtos = null;
        if (!EmployeeSkillLookUpList.isEmpty()) {
            esludtos=new ArrayList<EmployeeSkillLookUpDTO>();
            for (EmployeeSkillLookUp lookUp : EmployeeSkillLookUpList) {
                EmployeeSkillLookUpDTO dTO = new EmployeeSkillLookUpDTO();
                dTO.setSkillId(lookUp.getSkillId());
                dTO.setSkill(lookUp.getSkill());
                esludtos.add(dTO);
            }

        }

        return esludtos;

    }

}
