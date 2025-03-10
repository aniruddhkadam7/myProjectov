/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.dto;

import java.util.List;

/**
 *
 * @author naresh
 */
public class FeatureDTO {

    private Long featureId;
    private String name;
    private Boolean viewaccess;
    private Boolean createaccess;
    private Boolean updateaccess;
    private Boolean deleteaccess;
    private List<FeatureDTO> childFeatures;
    
    public FeatureDTO() {
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getViewaccess() {
        return viewaccess;
    }

    public void setViewaccess(Boolean viewaccess) {
        this.viewaccess = viewaccess;
    }

    public Boolean getCreateaccess() {
        return createaccess;
    }

    public void setCreateaccess(Boolean createaccess) {
        this.createaccess = createaccess;
    }

    public Boolean getUpdateaccess() {
        return updateaccess;
    }

    public void setUpdateaccess(Boolean updateaccess) {
        this.updateaccess = updateaccess;
    }

    public Boolean getDeleteaccess() {
        return deleteaccess;
    }

    public void setDeleteaccess(Boolean deleteaccess) {
        this.deleteaccess = deleteaccess;
    }

    public List<FeatureDTO> getChildFeatures() {
        return childFeatures;
    }

    public void setChildFeatures(List<FeatureDTO> childFeatures) {
        this.childFeatures = childFeatures;
    }

}
