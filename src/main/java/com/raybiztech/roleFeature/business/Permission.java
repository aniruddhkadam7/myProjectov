/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.rolefeature.business;

import java.io.Serializable;

/**
 *
 * @author naresh
 */
public class Permission implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7261041395313284032L;
	private Long id;
    private Boolean view;
    private Boolean create;
    private Boolean update;
    private Boolean delete;
    private Feature feature;
    private Role role;

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public Boolean getCreate() {
        return create;
    }

    public void setCreate(Boolean create) {
        this.create = create;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return "Permission{" + "id=" + id + ", view=" + view + ", create=" + create + ", update=" + update + ", delete=" + delete + ", feature=" + feature + ", role=" + role + '}';
    }

}
