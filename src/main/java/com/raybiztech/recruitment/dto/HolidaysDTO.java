/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.dto;

import java.io.Serializable;

/**
 *
 * @author naresh
 */
public class HolidaysDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 4319009577027570704L;
    private Long id;
    private String date;
    private String name;
    private String fullDate;
    private String week;
    private String country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullDate() {
        return fullDate;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
    
    public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
