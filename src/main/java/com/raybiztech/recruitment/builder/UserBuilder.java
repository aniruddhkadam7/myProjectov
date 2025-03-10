/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.recruitment.builder;

import com.raybiztech.appraisals.dao.DAO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.raybiztech.recruitment.dto.FeatureDTO;
import com.raybiztech.recruitment.dto.RoleDTO;
import com.raybiztech.roleFeature.dto.ChildMenuItemDTO;
import com.raybiztech.roleFeature.dto.MenuItemDTO;
import com.raybiztech.roleFeature.dto.ParentFeatureDTO;
import com.raybiztech.roleFeature.dto.PermissionDTO;
import com.raybiztech.rolefeature.business.ChildMenuItem;
import com.raybiztech.rolefeature.business.Feature;
import com.raybiztech.rolefeature.business.MenuItem;
import com.raybiztech.rolefeature.business.ParentFeature;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.rolefeature.business.Role;
import com.raybiztech.rolefeature.business.User;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author naresh
 */
@Component("userBuilder")
public class UserBuilder {

    Logger logger = Logger.getLogger(UserBuilder.class);
    @Autowired
    DAO dao;

    public List<String> convertEntityToDTO(User user) {
        List<String> list = new ArrayList<String>();

        for (Role role : user.getRole()) {

            for (Feature feature : role.getFeatures()) {
                list.add(feature.getName());
            }
        }

        return list;
    }

    public List<ChildMenuItemDTO> getSubMenuItemsList(Set<ChildMenuItem> childmenuItems, List<FeatureDTO> featurelist) {
        List<ChildMenuItem> childMenuItemDTOs = new ArrayList<ChildMenuItem>(childmenuItems);

        List<ChildMenuItemDTO> childMenuItemDTOList = null;
        if (childMenuItemDTOs != null) {
            childMenuItemDTOList = new ArrayList<ChildMenuItemDTO>();
            for (ChildMenuItem childMenuItem : childMenuItemDTOs) {
                if (checkmenuItemvalid(childMenuItem.getMenuName(), featurelist)) {
                    ChildMenuItemDTO childMenuItemDTO = new ChildMenuItemDTO();
                    childMenuItemDTO.setId(childMenuItem.getId());
                    childMenuItemDTO.setMenuUrl(childMenuItem.getMenuUrl());
                    childMenuItemDTO.setMenuName(childMenuItem.getMenuName());
                    childMenuItemDTO.setMenuClass(childMenuItem.getMenuClass());
                    childMenuItemDTO.setOrderNum(childMenuItem.getOrderNum());
                    childMenuItemDTOList.add(childMenuItemDTO);
                }
            }
        }

        Collections.sort(childMenuItemDTOList, new Comparator<ChildMenuItemDTO>() {
            @Override
            public int compare(ChildMenuItemDTO a, ChildMenuItemDTO b) {
                return a.getOrderNum().compareTo(b.getOrderNum());
            }
        });

        return childMenuItemDTOList;

    }

    public List<MenuItemDTO> convertMenuEntityToDTO(List<MenuItem> menuItems, List<FeatureDTO> featurelist) {
        List<MenuItemDTO> menuItemDTOList = null;
        if (menuItems != null) {
            menuItemDTOList = new ArrayList<MenuItemDTO>();
            for (MenuItem menuItem : menuItems) {
                if (checkmenuItemvalid(menuItem.getMenuName(), featurelist)) {
                    MenuItemDTO itemDTO = new MenuItemDTO();
                    itemDTO.setId(menuItem.getId());
                    itemDTO.setMenuurl(menuItem.getMenuurl());
                    itemDTO.setMenuName(menuItem.getMenuName());
                    itemDTO.setMenuclass(menuItem.getMenuclass());
                    itemDTO.setOrderNum(menuItem.getOrderNum());
                    if (menuItem.getChildmenuItems() != null) {
                        itemDTO.setChildmenuItems(getSubMenuItemsList(menuItem.getChildmenuItems(), featurelist));
                    }
                    menuItemDTOList.add(itemDTO);
                }

            }
        }

        Collections.sort(menuItemDTOList, new Comparator<MenuItemDTO>() {
            @Override
            public int compare(MenuItemDTO a, MenuItemDTO b) {
                return a.getOrderNum().compareTo(b.getOrderNum());
            }
        });
        return menuItemDTOList;
    }

    public List<FeatureDTO> convertFeatureEntityToDTO(List<Feature> featuresList) {
        List<FeatureDTO> featureDTOList = null;
        if (featuresList != null) {
            featureDTOList = new ArrayList<FeatureDTO>();
            for (Feature feature : featuresList) {
                FeatureDTO featureDTO = new FeatureDTO();
                if(feature.getReferenceFeature()==null)
                {
                List<Feature> childfeatures= dao.getChildFeatures(feature.getFeatureId());
                featureDTO.setFeatureId(feature.getFeatureId());
                featureDTO.setName(feature.getName());
                featureDTO.setViewaccess(feature.getViewaccess());
                featureDTO.setCreateaccess(feature.getCreateaccess());
                featureDTO.setUpdateaccess(feature.getUpdateaccess());
                featureDTO.setDeleteaccess(feature.getDeleteaccess());
               if(childfeatures!=null)
               featureDTO.setChildFeatures(convertChildFeatureEntityToDTO(childfeatures));
               
                featureDTOList.add(featureDTO);
                }
            }
        }
        return featureDTOList;
    }
    
     public List<FeatureDTO> convertChildFeatureEntityToDTO(List<Feature> featuresList) {
        List<FeatureDTO> featureDTOList = null;
        if (featuresList != null) {
            featureDTOList = new ArrayList<FeatureDTO>();
            for (Feature feature : featuresList) {
                FeatureDTO featureDTO = new FeatureDTO();
               
                featureDTO.setFeatureId(feature.getFeatureId());
                featureDTO.setName(feature.getName());
                featureDTO.setViewaccess(feature.getViewaccess());
                featureDTO.setCreateaccess(feature.getCreateaccess());
                featureDTO.setUpdateaccess(feature.getUpdateaccess());
                featureDTO.setDeleteaccess(feature.getDeleteaccess());
               
               
                
                featureDTOList.add(featureDTO);
            }
        }
        return featureDTOList;
    }

    public List<FeatureDTO> convertPermissionEntityToDTO(List<Permission> permissions) {
        List<FeatureDTO> featureDTOList = null;
        if (permissions != null) {
            featureDTOList = new ArrayList<FeatureDTO>();
            for (Permission permission : permissions) {
                FeatureDTO featureDTO = new FeatureDTO();
                featureDTO.setFeatureId(permission.getFeature().getFeatureId());
                featureDTO.setName(permission.getFeature().getName());
                featureDTO.setViewaccess(permission.getView());
                featureDTO.setCreateaccess(permission.getCreate());
                featureDTO.setUpdateaccess(permission.getUpdate());
                featureDTO.setDeleteaccess(permission.getDelete());
                featureDTOList.add(featureDTO);
            }
        }
        return featureDTOList;
    }

    public List<RoleDTO> convertRoleEntityToDTo(List<Role> roles) {
        List<RoleDTO> roleDTOList = null;
        if (roles != null) {
            roleDTOList = new ArrayList<RoleDTO>();
            for (Role role : roles) {
                RoleDTO roleDTO = new RoleDTO();
                roleDTO.setRoleId(role.getRoleId());
                roleDTO.setName(role.getName());
                roleDTOList.add(roleDTO);
            }
        }
        return roleDTOList;
    }

    public Boolean checkmenuItemvalid(String menuItem, List<FeatureDTO> roles) {
        Boolean modevalue = false;
        for (FeatureDTO featureDTO : roles) {
            if (menuItem.equalsIgnoreCase(featureDTO.getName())) {
                if (featureDTO.getViewaccess()) {
                    modevalue = true;
                }

            }
        }
        return modevalue;
    }

    public Permission assignPermissionBuilder(PermissionDTO permissionDTO) {
        Permission permission = null;
        if (permissionDTO != null) {
            permission = new Permission();
            if (permissionDTO.getType().equalsIgnoreCase("View")) {
                if (permissionDTO.getPermission()) {
                    permission.setView(Boolean.TRUE);
                    permission.setCreate(Boolean.FALSE);
                    permission.setUpdate(Boolean.FALSE);
                    permission.setDelete(Boolean.FALSE);
                } else {
                    permission.setView(Boolean.FALSE);
                    permission.setCreate(Boolean.FALSE);
                    permission.setUpdate(Boolean.FALSE);
                    permission.setDelete(Boolean.FALSE);
                }
            } else if (permissionDTO.getType().equalsIgnoreCase("Create")) {

                if (permissionDTO.getPermission()) {
                    permission.setCreate(Boolean.TRUE);
                    permission.setView(Boolean.FALSE);
                    permission.setUpdate(Boolean.FALSE);
                    permission.setDelete(Boolean.FALSE);
                } else {
                    permission.setCreate(Boolean.FALSE);
                    permission.setView(Boolean.FALSE);
                    permission.setUpdate(Boolean.FALSE);
                    permission.setDelete(Boolean.FALSE);
                }

            } else if (permissionDTO.getType().equalsIgnoreCase("Edit")) {
                if (permissionDTO.getPermission()) {
                    permission.setUpdate(Boolean.TRUE);
                    permission.setCreate(Boolean.FALSE);
                    permission.setView(Boolean.FALSE);
                    permission.setDelete(Boolean.FALSE);
                } else {
                    permission.setUpdate(Boolean.FALSE);
                    permission.setCreate(Boolean.FALSE);
                    permission.setView(Boolean.FALSE);
                    permission.setDelete(Boolean.FALSE);
                }

            } else if (permissionDTO.getType().equalsIgnoreCase("Delete")) {
                if (permissionDTO.getPermission()) {
                    permission.setDelete(Boolean.TRUE);
                    permission.setUpdate(Boolean.FALSE);
                    permission.setCreate(Boolean.FALSE);
                    permission.setView(Boolean.FALSE);
                } else {
                    permission.setDelete(Boolean.FALSE);
                    permission.setUpdate(Boolean.FALSE);
                    permission.setCreate(Boolean.FALSE);
                    permission.setView(Boolean.FALSE);
                }
            }

        }
        return permission;
    }

    public List<ParentFeatureDTO> buildFeatures(List<ParentFeature> subFeatureList) {
        List<ParentFeatureDTO> featureDTOs = new ArrayList<ParentFeatureDTO>();
        for (ParentFeature subFeature : subFeatureList) {
            ParentFeatureDTO featureDTO = new ParentFeatureDTO();
            featureDTO.setId(subFeature.getId());
            featureDTO.setName(subFeature.getName());
            featureDTO.setFeatures(convertFeatureEntityToDTO(new ArrayList<Feature>(subFeature.getFeatures())));
            featureDTOs.add(featureDTO);
        }
        return featureDTOs;

    }
}
