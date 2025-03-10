/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.business;

import java.io.Serializable;

import com.raybiztech.date.Date;

/**
 *
 * @author naresh
 */
public class Holidays implements Serializable, Comparable<Holidays> {

    /**
     *
     */
    private static final long serialVersionUID = 913010853630509449L;
    private Long id;
    private Date date;
    private String name;
    private String country;
    
    public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Holidays o) {
        // TODO Auto-generated method stub
        return (int) (this.id - o.id);
    }

}
