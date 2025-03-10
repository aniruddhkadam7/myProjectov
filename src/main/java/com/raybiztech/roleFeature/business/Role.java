/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.rolefeature.business;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author naresh
 */
public class Role implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5072070023339951727L;

	private Long roleId;
    
    private String name;
    
    private Boolean reportingMangerFlag;
    
    private Set<Feature>  features;

    public Role() {
    }
    
        public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getReportingMangerFlag() {
        return reportingMangerFlag;
    }

    public void setReportingMangerFlag(Boolean reportingMangerFlag) {
        this.reportingMangerFlag = reportingMangerFlag;
    }
    
       
    
}
