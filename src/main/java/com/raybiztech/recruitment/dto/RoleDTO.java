/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.dto;

import java.util.Set;

/**
 *
 * @author naresh
 */
public class RoleDTO {

    private Long roleId;

    private String name;

    private Set<FeatureDTO> features;

    public RoleDTO() {
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Set<FeatureDTO> getFeatures() {
        return features;
    }

    public void setFeatures(Set<FeatureDTO> features) {
        this.features = features;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
