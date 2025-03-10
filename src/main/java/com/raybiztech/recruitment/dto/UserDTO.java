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
public class UserDTO {

    private Long userid;

    private Set<RoleDTO> role;

    public UserDTO() {
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Set<RoleDTO> getRole() {
        return role;
    }

    public void setRole(Set<RoleDTO> role) {
        this.role = role;
    }

}
