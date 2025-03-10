/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.roleFeature.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.rolefeature.business.Feature;
import com.raybiztech.rolefeature.business.MenuItem;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.rolefeature.business.Role;

/**
 *
 * @author naresh
 */
public interface UserDAO {

    Boolean isRoleExits(String roleName);

    List<MenuItem> getMenuItems();

    List<Permission> getAllFeatures_underRole(Long roleId);

    Permission getPermissionData(Feature feature, Role role);

    void deleteRole(Long roleId);

    Map<String, Object> getAllFeatures(Integer startIndex, Integer endIndex);
    
    public Long returnIdValue(String roleName);
    
    List<Role> getRolesWithoutAdminRole();
}
