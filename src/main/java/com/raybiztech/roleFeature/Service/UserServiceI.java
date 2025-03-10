/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.roleFeature.Service;

import java.util.List;
import java.util.Map;

import com.raybiztech.recruitment.dto.FeatureDTO;
import com.raybiztech.recruitment.dto.RoleDTO;
import com.raybiztech.roleFeature.dto.MenuItemDTO;
import com.raybiztech.roleFeature.dto.ParentFeatureDTO;
import com.raybiztech.roleFeature.dto.PermissionDTO;

/**
 *
 * @author naresh
 */
public interface UserServiceI {

	List<FeatureDTO> getUser(Long userId);

	Long addRole(String roleName, Boolean reportingManagerFlag);

	Boolean isRoleExits(String roleName);

	List<MenuItemDTO> getMenuItems(Long loggedInEmpId);

	Map<String, Object> getAllFeatures(Integer startIndex, Integer endIndex);

	List<FeatureDTO> getAllFeatures_underRole(Long roleId);

	List<RoleDTO> getAllRoles();

	void assignPermission(PermissionDTO permissionDTO);

	void deleteRole(Long roleId);

	List<ParentFeatureDTO> getAllFeatures();

	List<RoleDTO> getRolesOnBasisOfloggedInEmployeeRole();
}
