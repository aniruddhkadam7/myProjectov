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
public class User implements Serializable{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -2383505068270529785L;

	private Long userid;
    
    private Set<Role> role;

    public User() {
    }
   

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }
    
       
  
}
