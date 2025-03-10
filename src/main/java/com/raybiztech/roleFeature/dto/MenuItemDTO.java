/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.roleFeature.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author naresh
 */
public class MenuItemDTO implements Serializable{

    private Long id;
    private String menuurl;
    private String menuName;
    private Integer orderNum;
    private String menuclass;
    private List<ChildMenuItemDTO> childmenuItems;

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

    public List<ChildMenuItemDTO> getChildmenuItems() {
        return childmenuItems;
    }

    public void setChildmenuItems(List<ChildMenuItemDTO> childmenuItems) {
        this.childmenuItems = childmenuItems;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
