/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.roleFeature.Controler;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.recruitment.dto.FeatureDTO;
import com.raybiztech.recruitment.dto.RoleDTO;
import com.raybiztech.roleFeature.Service.UserServiceI;
import com.raybiztech.roleFeature.dto.MenuItemDTO;
import com.raybiztech.roleFeature.dto.ParentFeatureDTO;
import com.raybiztech.roleFeature.dto.PermissionDTO;

/**
 *
 * @author naresh
 */
@Controller
@RequestMapping("/roleFeature")
public class UserControler {

	Logger logger = Logger.getLogger(UserControler.class);
	@Autowired
	UserServiceI userServiceImpl;

	@RequestMapping(value = "/user", params = { "userId" }, method = RequestMethod.GET)
	public @ResponseBody List<FeatureDTO> getUser(@RequestParam Long userId,
			HttpServletResponse httpServletResponse) {
		return userServiceImpl.getUser(userId);
	}

	@RequestMapping(value = "/role", params = { "roleName",
			"reportingManagerFlag" }, method = RequestMethod.POST)
	public @ResponseBody Long addRole(@RequestParam String roleName,
			@RequestParam Boolean reportingManagerFlag) {
		return userServiceImpl.addRole(roleName, reportingManagerFlag);
	}

	@RequestMapping(value = "/isRoleExits", params = { "roleName" }, method = RequestMethod.GET)
	public @ResponseBody Boolean getUser(@RequestParam String roleName,
			HttpServletResponse response) {
		return userServiceImpl.isRoleExits(roleName);
	}

	@RequestMapping(value = "/menuItems", params = { "loggedInEmpId" }, method = RequestMethod.GET)
	public @ResponseBody List<MenuItemDTO> getMenuItems(Long loggedInEmpId,
			HttpServletResponse httpServletResponse) {
		return userServiceImpl.getMenuItems(loggedInEmpId);
	}

	@RequestMapping(value = "/features", params = { "startIndex", "endIndex" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllFeatures(Integer startIndex,
			Integer endIndex) {
		return userServiceImpl.getAllFeatures(startIndex, endIndex);
	}

	@RequestMapping(value = "/features_UnderRole", params = { "roleId" }, method = RequestMethod.GET)
	public @ResponseBody List<FeatureDTO> getAllFeatures_underRole(Long roleId) {
		return userServiceImpl.getAllFeatures_underRole(roleId);
	}

	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public @ResponseBody List<RoleDTO> getAllRoles() {
		return userServiceImpl.getAllRoles();
	}

	@RequestMapping(value = "/roleBasedRoles", method = RequestMethod.GET)
	public @ResponseBody List<RoleDTO> getRoleBasedRoles() {
		return userServiceImpl.getRolesOnBasisOfloggedInEmployeeRole();
	}

	@RequestMapping(value = "/assignPermission", method = RequestMethod.POST)
	public @ResponseBody void assignPermission(
			@RequestBody PermissionDTO permissionDTO) {
		userServiceImpl.assignPermission(permissionDTO);
	}

	@RequestMapping(value = "/deleterole", params = { "roleId" }, method = RequestMethod.POST)
	public @ResponseBody void deleteRole(@RequestParam Long roleId) {
		userServiceImpl.deleteRole(roleId);
	}

	@RequestMapping(value = "/SubFeatures", method = RequestMethod.GET)
	public @ResponseBody List<ParentFeatureDTO> getAllFeatures() {
		return userServiceImpl.getAllFeatures();
	}
}
