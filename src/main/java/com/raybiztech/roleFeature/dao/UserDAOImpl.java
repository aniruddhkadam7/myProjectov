/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.roleFeature.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.rolefeature.business.Feature;
import com.raybiztech.rolefeature.business.MenuItem;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.rolefeature.business.Role;

/**
 *
 * @author naresh
 */
@Repository("userDAOImpl")
public class UserDAOImpl extends DAOImpl implements UserDAO {

	@Autowired
	DAO dao;
	Logger logger = Logger.getLogger(UserDAOImpl.class);

	@Override
	public Boolean isRoleExits(String roleName) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Role.class)
				.add(Restrictions.ilike("name", roleName));
		return criteria.list().size() > 0 ? true : false;
	}

	@Override
	public List<MenuItem> getMenuItems() {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(MenuItem.class);
		return criteria.list();
	}

	@Override
	public List<Permission> getAllFeatures_underRole(Long roleId) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Permission.class).createAlias("role", "r1")
				.add(Restrictions.eq("r1.roleId", roleId));
		return criteria.list();
	}

	@Override
	public Permission getPermissionData(Feature feature, Role role) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Permission.class).createAlias("role", "r1")
				.add(Restrictions.eq("r1.roleId", role.getRoleId()))
				.createAlias("feature", "f1")
				.add(Restrictions.eq("f1.featureId", feature.getFeatureId()));
		return (Permission) criteria.uniqueResult();

	}

	@Override
	public void deleteRole(Long roleId) {
		// Assign the default Role Employee replace of deleted Role.
		Query updateQry = getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"update Employee e set e.empRole.roleId=:NewRole , e.role=:RoleName where e.empRole.roleId=:OldRoleID");
		updateQry.setParameter("NewRole", 2l);
		updateQry.setParameter("RoleName", "Employee");
		updateQry.setParameter("OldRoleID", roleId);
		int updateRes = updateQry.executeUpdate();

		// Delete Permissions Existing Role.
		Query deleteQry = getSessionFactory().getCurrentSession().createQuery(
				"delete from Permission p  where p.role.roleId=:OldRoleID");
		deleteQry.setParameter("OldRoleID", roleId);
		int deleteRes = deleteQry.executeUpdate();

		// delete Role From Database.
		Role role = dao.findBy(Role.class, roleId);
		dao.delete(role);
	}

	@Override
	public Map<String, Object> getAllFeatures(Integer startIndex,
			Integer endIndex) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Feature.class);
		Integer noOfRecords = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		List<Feature> featuresList = criteria.list();
		Map<String, Object> featuresMap = new HashMap<String, Object>();
		featuresMap.put("list", featuresList);
		featuresMap.put("size", noOfRecords);
		return featuresMap;
	}

	@Override
	public Long returnIdValue(String roleName) {

		Role role = dao.findByName(Role.class, roleName);
		return role.getRoleId();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> getRolesWithoutAdminRole() {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Role.class);
		criteria.add(Restrictions.ne("name", "admin"));
		return criteria.list();

	}
}
