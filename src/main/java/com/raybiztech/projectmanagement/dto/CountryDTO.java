/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.dto;

import java.io.Serializable;

/**
 *
 * @author naresh
 */
public class CountryDTO implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -1765030640445049529L;
    private Long id;
    private String name;
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
