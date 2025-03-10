/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.appraisals.security.utils;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author anil
 */
public class CustomUserDetails implements UserDetails {
    
    private static final long serialVersionUID = 701407330001L;
	private int userID;
	private String username;
	private String password;
        private Collection<GrantedAuthority> authorities;
    
        private static final Logger LOGGER = Logger.getLogger(CustomUserDetails.class);

    
    public CustomUserDetails() {
    }

      /**
     *
     * @param username1
     * @param password1
     * @param authorities1
     */
     public CustomUserDetails(String username1, String password1,Collection<GrantedAuthority> authorities1) {
         System.out.println("customuserdetails:");

		this.username = username1;
		this.password = password1;
		this.authorities = authorities1;
	}
     
     	/**
     *
     * @return
     */
    public int getUserID() {
		return userID;
	}

	/**
     *
     * @param userID1
     */
    public void setUserID(int userID1) {
		this.userID = userID1;
	}

    @Override
	public String getPassword() {
    	LOGGER.warn("get password **************************%%%%" + password);
		return password;
	}

	/**
     *
     * @param password1
     */
    public void setPassword(String password1) {
        this.password=password1;
    	LOGGER.warn(this.password);
	}
    
    
     
      public String getUsername() {
		return username;
	}

	/**
     *
     * @param username1
     */
    public void setUsername(String username1) {
		this.username = username1;
	}

    /**
     *
     * @return
     */
   
   

     @Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	/**
     *
     * @param authorities1
     */
    public void setAuthorities(Collection<GrantedAuthority> authorities1) {
		this.authorities = authorities1;
	}


  
    @Override
    public boolean isAccountNonExpired() {
        return true;
        //throw new UnsupportedOperationException("Not supported yet."); // To
        // change
        // body
        // of
        // generated
        // methods,
        // choose
        // Tools
        // |
        // Templates.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
        // change
        // body
        // of
        // generated
        // methods,
        // choose
        // Tools
        // |
        // Templates.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;// To
        // change
        // body
        // of
        // generated
        // methods,
        // choose
        // Tools
        // |
        // Templates.
    }

    @Override
    public boolean isEnabled() {
        return true;
        // change
        // body
        // of
        // generated
        // methods,
        // choose
        // Tools
        // |
        // Templates.
    }

    
   
    
}
