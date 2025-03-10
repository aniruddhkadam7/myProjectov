/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.roleFeature.Service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.menuItem.builder.ManuItemBuilder;
import com.raybiztech.recruitment.builder.UserBuilder;
import com.raybiztech.recruitment.dto.FeatureDTO;
import com.raybiztech.recruitment.dto.RoleDTO;
import com.raybiztech.roleFeature.Exception.RoleFeatureException;
import com.raybiztech.roleFeature.dao.UserDAO;
import com.raybiztech.roleFeature.dto.MenuItemDTO;
import com.raybiztech.roleFeature.dto.ParentFeatureDTO;
import com.raybiztech.roleFeature.dto.PermissionDTO;
import com.raybiztech.rolefeature.business.Feature;
import com.raybiztech.rolefeature.business.MenuItem;
import com.raybiztech.rolefeature.business.ParentFeature;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.rolefeature.business.Role;

/**
 *
 * @author naresh
 */
@Service("userServiceImpl")
@Transactional
public class UserServiceImpl implements UserServiceI {

	Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Autowired
	DAO dao;
	@Autowired
	UserBuilder userBuilder;
	@Autowired
	UserDAO userDAOImpl;
	@Autowired
	ManuItemBuilder manuItemBuilder;

	@Override
	public List<FeatureDTO> getUser(Long userId) {

		Employee employee = dao.findBy(Employee.class, userId);
		return getAllFeatures_underRole(employee.getEmpRole().getRoleId());
	}

	@Override
	public Long addRole(String roleName, Boolean reportingManagerFlag) {
		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		Employee emplo = dao.findByEmployeeName(Employee.class, userName);

		if (emplo.getRole().equalsIgnoreCase("admin")) {

			Role role = new Role();
			role.setName(roleName);
			role.setReportingMangerFlag(reportingManagerFlag);
			dao.save(role);
			return userDAOImpl.returnIdValue(roleName);
		} else {
			throw new RoleFeatureException("Your Unauthorized To add role");
		}
	}

	@Override
	public Boolean isRoleExits(String roleName) {
		return userDAOImpl.isRoleExits(roleName);
	}

	@Override
	public List<MenuItemDTO> getMenuItems(Long loggedInEmpId) {

		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		List<FeatureDTO> featureDTOList = getAllFeatures_underRole(employee
				.getEmpRole().getRoleId());
		List<MenuItem> menuItems = userDAOImpl.getMenuItems();
		return userBuilder.convertMenuEntityToDTO(menuItems, featureDTOList);

	}

	@Override
	public Map<String, Object> getAllFeatures(Integer startIndex,
			Integer endIndex) {

		Map<String, Object> featuresMap = userDAOImpl.getAllFeatures(
				startIndex, endIndex);
		List<Feature> featuresList = (List<Feature>) featuresMap.get("list");
		featuresMap.put("list",
				userBuilder.convertFeatureEntityToDTO(featuresList));
		return featuresMap;
	}

	@Override
	public List<FeatureDTO> getAllFeatures_underRole(Long roleId) {

		List<Permission> permissionsList = userDAOImpl
				.getAllFeatures_underRole(roleId);
		return userBuilder.convertPermissionEntityToDTO(permissionsList);
	}

	@Override
	public List<RoleDTO> getAllRoles() {

		return userBuilder.convertRoleEntityToDTo(dao.get(Role.class));

	}

	@Override
	public void assignPermission(PermissionDTO permissionDTO) {
		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		Employee emplo = dao.findByEmployeeName(Employee.class, userName);

		if (emplo.getRole().equalsIgnoreCase("admin")) {
			Feature feature = dao.findBy(Feature.class,
					permissionDTO.getFeatureId());
			Role role = dao.findBy(Role.class, permissionDTO.getRoleId());

			Permission permissionupdate = userDAOImpl.getPermissionData(
					feature, role);

			if (permissionupdate != null) {
				permissionupdate = assignPermissiontoExistingObject(
						permissionupdate, permissionDTO);
				dao.saveOrUpdate(permissionupdate);
			} else {
				Permission permissionsave = userBuilder
						.assignPermissionBuilder(permissionDTO);
				permissionsave.setFeature(feature);
				permissionsave.setRole(role);
				dao.save(permissionsave);
			}
		} else {
			throw new RoleFeatureException(
					"Your Unauthorized To assign permission");
		}

	}

	private Permission assignPermissiontoExistingObject(Permission permission,
			PermissionDTO permissionDTO) {
		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		Employee emplo = dao.findByEmployeeName(Employee.class, userName);

		if (emplo.getRole().equalsIgnoreCase("admin")) {
			if (permissionDTO.getType().equalsIgnoreCase("View")) {
				if (permissionDTO.getPermission()) {
					permission.setView(Boolean.TRUE);
				} else {
					permission.setView(Boolean.FALSE);
				}
			} else if (permissionDTO.getType().equalsIgnoreCase("Create")) {

				if (permissionDTO.getPermission()) {
					permission.setCreate(Boolean.TRUE);
				} else {
					permission.setCreate(Boolean.FALSE);
				}

			} else if (permissionDTO.getType().equalsIgnoreCase("Edit")) {
				if (permissionDTO.getPermission()) {
					permission.setUpdate(Boolean.TRUE);
				} else {
					permission.setUpdate(Boolean.FALSE);
				}

			} else if (permissionDTO.getType().equalsIgnoreCase("Delete")) {
				if (permissionDTO.getPermission()) {
					permission.setDelete(Boolean.TRUE);
				} else {
					permission.setDelete(Boolean.FALSE);
				}
			}

			return permission;
		} else {
			throw new RoleFeatureException(
					"Your Unauthorized To view This Page");
		}

	}

	@Override
	public void deleteRole(Long roleId) {
		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		Employee emplo = dao.findByEmployeeName(Employee.class, userName);

		if (emplo.getRole().equalsIgnoreCase("admin")) {

			userDAOImpl.deleteRole(roleId);
		} else {
			throw new RoleFeatureException(
					"Your Unauthorized To assign permission to existing object");
		}
	}

	@Override
	public List<ParentFeatureDTO> getAllFeatures() {

		List<ParentFeature> subFeatureList = dao.get(ParentFeature.class);
		List<ParentFeatureDTO> featureDTOList = userBuilder
				.buildFeatures(subFeatureList);
		return featureDTOList;
	}

	@Override
	public List<RoleDTO> getRolesOnBasisOfloggedInEmployeeRole() {
		String userName = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		Employee emplo = dao.findByEmployeeName(Employee.class, userName);
		
		if (emplo.getRole().equalsIgnoreCase("admin")) {
			return userBuilder.convertRoleEntityToDTo(dao.get(Role.class));
		} else {
			return userBuilder.convertRoleEntityToDTo(userDAOImpl
					.getRolesWithoutAdminRole());
		}
	}

}
