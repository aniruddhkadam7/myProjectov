/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.roleFeature.dto;

import java.io.Serializable;

/**
 *
 * @author naresh
 */
public class PermissionDTO implements Serializable {

    private Long featureId;
    private Long roleId;
    private Boolean permission;
    private String type;

    public PermissionDTO() {
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Boolean getPermission() {
        return permission;
    }

    public void setPermission(Boolean permission) {
        this.permission = permission;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PermissionDTO{" + "featureId=" + featureId + ", roleId=" + roleId + ", permission=" + permission + ", type=" + type + '}';
    }
    
}
