/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.rolefeature.business;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author naresh
 */
public class MenuItem implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8933936263656957328L;
	private Long id;
    private String menuurl;
    private String menuName;
    private Integer orderNum;
    private String menuclass;
    private Set<ChildMenuItem> childmenuItems;

    public String getMenuclass() {
        return menuclass;
    }

    public void setMenuclass(String menuclass) {
        this.menuclass = menuclass;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuurl() {
        return menuurl;
    }

    public void setMenuurl(String menuurl) {
        this.menuurl = menuurl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Set<ChildMenuItem> getChildmenuItems() {
        return childmenuItems;
    }

    public void setChildmenuItems(Set<ChildMenuItem> childmenuItems) {
        this.childmenuItems = childmenuItems;
    }

   

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MenuItem other = (MenuItem) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
