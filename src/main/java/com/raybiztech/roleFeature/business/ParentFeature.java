/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.rolefeature.business;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author naresh
 */
public class ParentFeature implements Serializable {

    private Long id;
    private String name;
    private Set<Feature> features;

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

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    
}
