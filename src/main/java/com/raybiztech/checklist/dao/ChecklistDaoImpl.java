package com.raybiztech.checklist.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.pattern.LogEvent;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Status;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.checklist.business.ChecklistSection;
import com.raybiztech.checklist.business.DepartmentSection;
import com.raybiztech.checklist.dto.ChecklistConfigurationDto;
import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.business.ProjectCheckList;

@Repository("checklistDao")
public class ChecklistDaoImpl extends DAOImpl implements ChecklistDao {

	@Override
	public List<ChecklistSection> getSections(Long deptId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ChecklistSection.class);
		criteria.createAlias("department", "dept");
		criteria.add(Restrictions.eq("dept.departmentId", deptId));

		return criteria.list();
	}

	@Override
	public List<ProjectCheckList> getChecklistItems(Long departmentId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectCheckList.class);
		criteria.createAlias("checklistSection", "sectionId");
		criteria.createAlias("sectionId.department", "dept");
		criteria.add(Restrictions.eq("dept.departmentId", departmentId));
		return criteria.list();
	}

	@Override
	public Boolean isSectionExist(String sectionName, Long departmentId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ChecklistSection.class);

		criteria.add(Restrictions.eq("sectionName", sectionName));
		criteria.createAlias("department", "dept");

		criteria.add(Restrictions.eq("dept.departmentId", departmentId));
		return criteria.uniqueResult() != null;
	}

	@Override
	public Boolean isAlreadyExist(String name, Long departmentId, Long sectionId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectCheckList.class);
		criteria.createAlias("checklistSection", "sectionId");
		criteria.createAlias("sectionId.department", "dept");
		criteria.add(Restrictions.ilike("name", name));
		criteria.add(Restrictions.eq("dept.departmentId", departmentId));
		criteria.add(Restrictions.eq("sectionId.checklistsectionId", sectionId));
		if (criteria.list().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Map<String, Object> getDepartmentSections(Long departmentId,
			Date fromDate, Date toDate, String multiSearch, Integer startindex,
			Integer endIndex) {
		Map<String, Object> map = new HashMap<String, Object>();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				DepartmentSection.class);
		criteria.add(Restrictions.between("checklistDate", fromDate, toDate));

		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.ilike("employee.employeeFullName",
				multiSearch, MatchMode.ANYWHERE));
		if (departmentId != null) {
			criteria.createAlias("section", "sectionId");
			criteria.createAlias("sectionId.department", "dept");
			criteria.add(Restrictions.eq("dept.departmentId", departmentId));
		}
		criteria.addOrder(Order.desc("id"));

		Long records = (long) criteria.list().size();

		criteria.setFirstResult(startindex);
		criteria.setMaxResults(endIndex - startindex);

		map.put("list", criteria.list());
		map.put("size", records);

		return map;
	}

	@Override
	public List<ProjectCheckList> checkLists(Long sectionsId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectCheckList.class);
		criteria.add(Restrictions.eq("type", "checklist"));
		criteria.add(Restrictions.eq("status", Boolean.TRUE));
		criteria.createAlias("checklistSection", "cls");
		criteria.add(Restrictions.eq("cls.checklistsectionId", sectionsId));

		return criteria.list();
	}

}
