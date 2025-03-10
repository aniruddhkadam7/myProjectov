package com.raybiztech.handbook.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.business.Manufacturer;
import com.raybiztech.handbook.business.HandbookCountry;
import com.raybiztech.handbook.business.HandbookItem;
import com.raybiztech.handbook.dto.HandbookItemDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.rolefeature.business.Permission;

@Repository("handBookDAO")
public class HandbookItemDAOImpl extends DAOImpl implements HandbookItemDAO {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DAO dao;

	@Override
	public Map<String, Object> getAll(Integer startIndex, Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HandbookItem.class);
		criteria.add(Restrictions.eq("type", "HandBook"));
		//criteria.addOrder(Order.desc("id"));
		criteria.addOrder(Order.asc("displayOrder"));
		Integer noOfRecords = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		List<HandbookItem> handBookList = criteria.list();
		Map<String, Object> handBookmap = new HashMap<String, Object>();
		handBookmap.put("list", handBookList);
		handBookmap.put("size", noOfRecords);

		return handBookmap;

	}

	@Override
	public boolean duplicatePageTitleCheck(HandbookItemDTO book) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HandbookItem.class);
		criteria.add(Restrictions.eq("title", book.getTitle()));
		criteria.add(Restrictions.eq("pageName", book.getPageName()));
		//criteria.add(Restrictions.eq("displayOrder", book.getDisplayOrder()));
		return criteria.list().size() > 0 ? true : false;
	}
	@Override
	public List<CountryLookUp> getlookuplist(List<Integer> list) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CountryLookUp.class);
		criteria.add(Restrictions.in("id", list));
		return criteria.list();
	}

	@Override
	public boolean duplicatePageTitleCheckUpdate(HandbookItemDTO book) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HandbookItem.class);
		criteria.add(Restrictions.ne("id", book.getId()));
		criteria.add(Restrictions.eq("title", book.getTitle()));
		criteria.add(Restrictions.eq("pageName", book.getPageName()));
		//criteria.add(Restrictions.eq("displayOrder", book.getDisplayOrder()));
		return criteria.list().size() > 0 ? true : false;
	}

	@Override
	public Map<String, Object> getAllCheckList(Integer startIndex, Integer endIndex) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HandbookItem.class);
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);

		Boolean totalListFalg = false;
		Boolean seperateListFalg = false;

		Permission totalList = dao.checkForPermission("Checklists", employee);
		Permission departmentList = dao.checkForPermission("Department Checklist", employee);

		if (totalList.getView() && !departmentList.getView())
			totalListFalg = true;
		else if (departmentList.getView() && totalList.getView())
			seperateListFalg = true;

		if (totalListFalg) {
			criteria.list();
		} else if (seperateListFalg) {
			criteria.createAlias("empDepartment", "department");
			criteria.add(Restrictions.eq("department.departmentName", employee.getDepartmentName()));
		}
		criteria.add(Restrictions.ilike("type", "departmentCheckList"));
		criteria.addOrder(Order.asc("displayOrder"));
		Integer noOfRecords = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		List<HandbookItem> checkList = criteria.list();
		Map<String, Object> checkListMap = new HashMap<String, Object>();
		checkListMap.put("list", checkList);
		checkListMap.put("size", noOfRecords);

		return checkListMap;

	}

	@Override
	public boolean duplicateCheckList(HandbookItemDTO checkList) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HandbookItem.class);
		criteria.createAlias("empDepartment", "empDepartment");
		criteria.createAlias("section", "section");
		criteria.add(Restrictions.eq("title", checkList.getTitle()));
		// criteria.add(Restrictions.eq("pageName", checkList.getPageName()));
		criteria.add(Restrictions.eq("displayOrder", checkList.getDisplayOrder()));
		criteria.add(Restrictions.eq("section.checklistsectionId", checkList.getSectionId()));
		criteria.add(Restrictions.eq("empDepartment.departmentId", checkList.getDepartmentId()));
		return criteria.list().size() > 0 ? true : false;
	}

	@Override
	public boolean duplicateCheckListForUpdate(HandbookItemDTO checkList) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HandbookItem.class);
		criteria.createAlias("empDepartment", "empDepartment");
		criteria.add(Restrictions.ne("id", checkList.getId()));
		criteria.add(Restrictions.eq("title", checkList.getTitle()));
		// criteria.add(Restrictions.eq("pageName", checkList.getPageName()));
		criteria.add(Restrictions.eq("displayOrder", checkList.getDisplayOrder()));
		criteria.add(Restrictions.eq("section.checklistsectionId", checkList.getSectionId()));
		criteria.add(Restrictions.eq("empDepartment.departmentId", checkList.getDepartmentId()));
		return criteria.list().size() > 0 ? true : false;
	}

	@Override
	public List<EmpDepartment> getDepartmentList(Employee employee) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmpDepartment.class);
		criteria.add(Restrictions.eq("departmentName", employee.getDepartmentName()));
		return criteria.list();
	}

	@Override
	public HandbookItem findByPageTittle(String title) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HandbookItem.class);
		criteria.add(Restrictions.eq("title", title));
		return (HandbookItem) criteria.uniqueResult();
	}

	@Override
	public List<HandbookItem> getTotalHandbookList() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HandbookItem.class);
		criteria.add(Restrictions.eq("type", "HandBook"));
		criteria.addOrder(Order.asc("displayOrder"));
		System.out.println("total handbooks list:" + criteria.list().size());
		return criteria.list();
	}
	 public List<HandbookCountry> getList(List<HandbookItem> handbook,String country){
		   Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HandbookCountry.class);
		   criteria.add(Restrictions.in("handbook", handbook));
		   criteria.createAlias("country", "country");
		   criteria.add(Restrictions.eq("country.name", country));
			System.out.println("country handbooks list:" + criteria.list().size());
		   return criteria.list();
		   
	   }
	 public List<HandbookCountry> getHandbookList(HandbookItem handbook){
		   Criteria criteria = sessionFactory.getCurrentSession().createCriteria(HandbookCountry.class);
		   criteria.add(Restrictions.eq("handbook", handbook));
		   return criteria.list();
		   
	   }
	   

}
