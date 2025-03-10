/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.business;

/**
 *
 * @author naresh
 */
public enum HealthStatus {

    Overall("overall"), Milestones("milestones"), Issues("issues");
    
    private String healthStatus;

    public String getHealthStatus() {
        return healthStatus;
    }

    private HealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }
}
