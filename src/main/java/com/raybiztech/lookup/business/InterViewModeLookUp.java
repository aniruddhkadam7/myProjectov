/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.lookup.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author hari
 */
public class InterViewModeLookUp implements Serializable {

    private static final long serialVersionUID = 3559009240369531519L;
    private Long id;

    private String name;

    private Integer diplayOrder;

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

    public Integer getDiplayOrder() {
        return diplayOrder;
    }

    public void setDiplayOrder(Integer diplayOrder) {
        this.diplayOrder = diplayOrder;
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(13, 17).append(id).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof InterViewModeLookUp) {
            final InterViewModeLookUp interViewModeLookUp = (InterViewModeLookUp) obj;
            return new EqualsBuilder().append(id, interViewModeLookUp.getId()).isEquals();
        }
        return false;
    }

}
