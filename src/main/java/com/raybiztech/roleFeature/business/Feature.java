/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.rolefeature.business;

import java.io.Serializable;

/**
 *
 * @author naresh
 */
public class Feature implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3240451488471781714L;
	private Long featureId;
    private String name;
    private Boolean viewaccess;
    private Boolean createaccess;
    private Boolean updateaccess;
    private Boolean deleteaccess;
    private ParentFeature parentFeature;
    private Feature referenceFeature;

    public Feature() {
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

    public ParentFeature getParentFeature() {
        return parentFeature;
    }

    public void setParentFeature(ParentFeature parentFeature) {
        this.parentFeature = parentFeature;
    }

    public Feature getReferenceFeature() {
        return referenceFeature;
    }

    public void setReferenceFeature(Feature referenceFeature) {
        this.referenceFeature = referenceFeature;
    }

    

   
}
