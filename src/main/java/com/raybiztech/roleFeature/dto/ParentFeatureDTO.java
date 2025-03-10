/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.roleFeature.dto;

import java.util.List;

import com.raybiztech.recruitment.dto.FeatureDTO;

/**
 *
 * @author naresh
 */
public class ParentFeatureDTO {

    private Long id;
    private String name;
    private List<FeatureDTO> features;

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

    public List<FeatureDTO> getFeatures() {
        return features;
    }

    public void setFeatures(List<FeatureDTO> features) {
        this.features = features;
    }

  
}
