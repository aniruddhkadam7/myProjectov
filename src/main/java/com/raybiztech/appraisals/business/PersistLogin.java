/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.date.Second;

/**
 *
 * @author hari
 */
public class PersistLogin implements Serializable {

    private Long id;
    private String userName;
    private String token;
    private Second login_Time;
    private Second last_Used;

    public PersistLogin() {
    }

    public PersistLogin(String userName, String token) {
        this.userName = userName;
        this.token = token;
    }

    public PersistLogin(String userName, String token, Second login_Time, Second last_Used) {
        this.userName = userName;
        this.token = token;
        this.login_Time = login_Time;
        this.last_Used = last_Used;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Second getLogin_Time() {
        return login_Time;
    }

    public void setLogin_Time(Second login_Time) {
        this.login_Time = login_Time;
    }

    public Second getLast_Used() {
        return last_Used;
    }

    public void setLast_Used(Second last_Used) {
        this.last_Used = last_Used;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(1989, 55).append(id).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersistLogin other = (PersistLogin) obj;
        return new EqualsBuilder().append(id, other.id)
                .isEquals();
    }

}
