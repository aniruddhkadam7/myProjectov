/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.employeeprofile.dto;

/**
 *
 * @author naresh
 */
public class ProvisionPeriodDTO {

    private String username;
    private String month;
    private String joinDate;
    private String provisionDate;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public String getProvisionDate() {
        return provisionDate;
    }

    public void setProvisionDate(String provisionDate) {
        this.provisionDate = provisionDate;
    }
}
