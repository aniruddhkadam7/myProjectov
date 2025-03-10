package com.raybiztech.supportmanagement.dao;

import com.itextpdf.tool.xml.svg.graphic.Circle;
import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.dto.EmpDepartmentDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.recruitment.business.Department;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.supportmanagement.business.SupportTickets;
import com.raybiztech.supportmanagement.business.TicketsCategory;
import com.raybiztech.supportmanagement.business.TicketsSubCategory;
import com.raybiztech.supportmanagement.business.Tracker;
import com.raybiztech.supportmanagement.dto.TicketsSubCategoryDTO;
import com.raybiztech.ticketmanagement.business.Ticket;
import com.raybiztech.ticketmanagement.business.TicketHistory;
import com.raybiztech.ticketmanagement.business.TicketStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("supportManagementDAOImpl")
public class SupportManagementDAOImpl extends DAOImpl implements SupportManagementDAO {

	Logger logger = Logger.getLogger(SupportManagementDAOImpl.class);

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DAO dao;

	// To get the department wise category llok up
	@Override
	public List<TicketsCategory> departmentCategoryList(Long deptId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TicketsCategory.class);
		criteria.createAlias("empDepartment", "empDepartment");
		criteria.add(Restrictions.eq("empDepartment.departmentId", deptId));
		return criteria.list();
	}

	// To get the sub category look up based on category selected
	@Override
	public List<TicketsSubCategory> subCategoryList(Long categoryId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TicketsSubCategory.class);
		criteria.createAlias("ticketsCategory", "ticketsCategory");
		criteria.add(Restrictions.eq("ticketsCategory.id", categoryId));
		return criteria.list();
	}

	// This is to get the tickets for a manager which has been raised by his/her
	// reportees
	@Override
	public Map<String, Object> getReporteeTickets(/* List<Long> empIds */String managerId, String ticketStatus,
			Long categoryId, Long subCategoryId, Integer startIndex, Integer endIndex, String progressStatus,
			String multiSearch, String searchByEmpName, String searchByAssigneeName, DateRange dateRange,
			Long departmentId, Long trackerID) {
		/*
		 * Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
		 * Employee.class); criteria.add(Restrictions.in("manager.employeeId", empIds));
		 * ProjectionList projectionList = Projections.projectionList();
		 * projectionList.add(Projections.property("employeeId"));
		 * 
		 * criteria.setProjection(projectionList);
		 */
		// This list will return all the reportee employee ids for the logged in
		// employee
		// List<Long> empIds = criteria.list();

		// From that list we are getting all the tickets raised by those
		// employees
		Criteria criteria2 = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria2.createAlias("ticketsSubCategory", "ticketsSubCategory");
		criteria2.createAlias("ticketsSubCategory.ticketsCategory", "category");
		criteria2.createAlias("category.empDepartment", "department");
		// criteria2.add(Restrictions.in("createdBy", criteria.list()));
		criteria2.add(Restrictions.ilike("managesList", managerId, MatchMode.ANYWHERE));
		if (!(ticketStatus.isEmpty() || ticketStatus.equalsIgnoreCase("All"))) {
			criteria2.add(Restrictions.eq("approvalStatus", ticketStatus));
		}
		if (categoryId != null) {
			criteria2.add(Restrictions.eq("category.id", categoryId));
		}
		if (subCategoryId != null) {
			criteria2.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
		}
		if (!(progressStatus.isEmpty() || progressStatus.equalsIgnoreCase("All"))) {
			criteria2.add(Restrictions.eq("status", progressStatus));
		}
		if (departmentId != null) {
			criteria2.add(Restrictions.eq("department.departmentId", departmentId));
		}
		if (trackerID != null) {
			criteria2.add(Restrictions.eq("tracker.id", trackerID));
		}

		// For Employee Name search
		if (searchByEmpName.equals("true")) {
			Criteria criteria3 = sessionFactory.getCurrentSession().createCriteria(Employee.class);
			Criterion empFirstName1 = Restrictions.ilike("firstName", multiSearch, MatchMode.ANYWHERE);
			Criterion empLastName1 = Restrictions.ilike("lastName", multiSearch, MatchMode.ANYWHERE);
			criteria3.add(Restrictions.or(empFirstName1, empLastName1));

			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("employeeId"));
			criteria3.setProjection(projections);
			criteria2.add(Restrictions.in("createdBy", criteria3.list()));
		}

		// For Assignee Name Search
		if (searchByAssigneeName.equals("true")) {
			criteria2.createAlias("assignee", "employee");
			Criterion empFirstName = Restrictions.ilike("employee.firstName", multiSearch, MatchMode.ANYWHERE);
			Criterion empLastName = Restrictions.ilike("employee.lastName", multiSearch, MatchMode.ANYWHERE);
			criteria2.add(Restrictions.or(empFirstName, empLastName));
		}

		// For other search
		if (searchByEmpName.equals("false") && searchByAssigneeName.equals("false")) {
			if (multiSearch != null) {
				try {
					criteria2.add(Restrictions.eq("id", Long.parseLong(multiSearch)));
				} catch (NumberFormatException ne) {
					Criterion subject = Restrictions.ilike("subject", multiSearch, MatchMode.ANYWHERE);
					Criterion priority = Restrictions.ilike("priority", multiSearch, MatchMode.ANYWHERE);
					Criterion criterion = Restrictions.or(subject, priority);
					criteria2.add(criterion);
				}
			}
		}

		// Custom date filter
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			Criterion criterion4 = Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.le("startDate", dateRange.getMaximum()));
			Criterion criterion5 = Restrictions.and(Restrictions.ge("endDate", dateRange.getMinimum()),
					Restrictions.le("endDate", dateRange.getMaximum()));

			Criterion criterion6 = Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
							Restrictions.le("endDate", dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
									Restrictions.le("endDate", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
											Restrictions.le("endDate", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("startDate", dateRange.getMinimum()),
											Restrictions.ge("endDate", dateRange.getMaximum())))));
			Criterion criterion7 = Restrictions.or(Restrictions.or(criterion4, criterion5), criterion6);

			criteria2.add(criterion7);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria2.add(Restrictions.ge("startDate", dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria2.add(Restrictions.le("endDate", dateRange.getMaximum()));
		}

		criteria2.addOrder(Order.desc("id"));
		/*
		 * Integer noOfRecords = criteria2.list().size(); if (startIndex != null &&
		 * endIndex != null) { criteria2.setFirstResult(startIndex);
		 * criteria2.setMaxResults(endIndex - startIndex); } List<SupportTickets> list =
		 * criteria2.list();
		 */
		Map<String, Object> criteriaMap = getPaginationList(criteria2, startIndex, endIndex);
		Map<String, Object> reporteeMap = new HashMap<String, Object>();
		reporteeMap.put("list", criteriaMap.get("list"));
		reporteeMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return reporteeMap;

	}

	// get individual tickets
	@Override
	public List<SupportTickets> getIndividualTickets(Long loggedEmpId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		/*
		 * Second second = SecondParser.toSecond(date.toString());
		 * logger.warn("Second....."+second);
		 */
		criteria.add(Restrictions.eq("createdBy", loggedEmpId));
		/* criteria.add(Restrictions.ilike("createdDate", second)); */
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	// get searched subCategory List
	@Override
	public Map<String, Object> getSearchSubCategoryList(TicketsSubCategoryDTO ticketsSubCategoryDto, Integer startIndex,
			Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TicketsSubCategory.class);
		criteria.createAlias("ticketsCategory", "ticketsCategory");
		criteria.createAlias("ticketsCategory.empDepartment", "empDepartment");
		criteria.add(Restrictions.eq("empDepartment.departmentId", ticketsSubCategoryDto.getDepartmentId()));
		if (ticketsSubCategoryDto.getCategoryId() != null) {
			criteria.add(Restrictions.eq("ticketsCategory.id", ticketsSubCategoryDto.getCategoryId()));
		}
		if (ticketsSubCategoryDto.getSubCategoryId() != null) {
			criteria.add(Restrictions.eq("id", ticketsSubCategoryDto.getSubCategoryId()));
		}
		criteria.addOrder(Order.desc("id"));
		/*
		 * Integer noOfRecords = criteria.list().size();
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); List<TicketsSubCategory> list = criteria.list();
		 */

		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> subticketmap = new HashMap<String, Object>();
		subticketmap.put("list", criteriaMap.get("list"));
		subticketmap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return subticketmap;
	}

	@Override
	public Map<String, Object> getTotalTicketsForAdmin(String ticketStatus, Long categoryId, Long subCategoryId,
			Integer startIndex, Integer endIndex, String progressStatus, String multiSearch, String searchByEmpName,
			String searchByAssigneeName, DateRange dateRange, Long departmentId, Long trackerID) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);

		criteria.createAlias("ticketsSubCategory", "ticketsSubCategory");
		criteria.createAlias("ticketsSubCategory.ticketsCategory", "category");
		criteria.createAlias("category.empDepartment", "department");
		if (!(ticketStatus.isEmpty() || ticketStatus.equalsIgnoreCase("All"))) {
			criteria.add(Restrictions.eq("approvalStatus", ticketStatus));
		}
		if (categoryId != null) {
			criteria.add(Restrictions.eq("category.id", categoryId));
		}
		if (subCategoryId != null) {
			criteria.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
		}
		if (!(progressStatus.isEmpty() || progressStatus.equalsIgnoreCase("All"))) {
			criteria.add(Restrictions.eq("status", progressStatus));
		}
		if (departmentId != null) {
			criteria.add(Restrictions.eq("department.departmentId", departmentId));
		}
		if (trackerID != null) {
			criteria.add(Restrictions.eq("tracker.id", trackerID));
		}

		// For Employee Name search
		if (searchByEmpName.equals("true")) {
			Criteria criteria2 = sessionFactory.getCurrentSession().createCriteria(Employee.class);
			criteria2.add(Restrictions.ilike("employeeFullName", multiSearch, MatchMode.ANYWHERE));
			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("employeeId"));
			criteria2.setProjection(projections);
			criteria.add(Restrictions.in("createdBy", criteria2.list()));
		}

		// For Assignee Name Search
		if (searchByAssigneeName.equals("true")) {
			criteria.createAlias("assignee", "employee");
			criteria.add(Restrictions.ilike("employee.employeeFullName", multiSearch, MatchMode.ANYWHERE));

		}

		// For other search
		if (searchByEmpName.equals("false") && searchByAssigneeName.equals("false")) {
			if (multiSearch != null) {
				try {
					criteria.add(Restrictions.eq("id", Long.parseLong(multiSearch)));
				} catch (NumberFormatException ne) {
					Criterion subject = Restrictions.ilike("subject", multiSearch, MatchMode.ANYWHERE);
					Criterion priority = Restrictions.ilike("priority", multiSearch, MatchMode.ANYWHERE);
					Criterion criterion = Restrictions.or(subject, priority);
					criteria.add(criterion);
				}
			}
		}

		// Custom date filter
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			Criterion criterion4 = Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.le("startDate", dateRange.getMaximum()));
			Criterion criterion5 = Restrictions.and(Restrictions.ge("endDate", dateRange.getMinimum()),
					Restrictions.le("endDate", dateRange.getMaximum()));

			Criterion criterion6 = Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
							Restrictions.le("endDate", dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
									Restrictions.le("endDate", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
											Restrictions.le("endDate", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("startDate", dateRange.getMinimum()),
											Restrictions.ge("endDate", dateRange.getMaximum())))));
			Criterion criterion7 = Restrictions.or(Restrictions.or(criterion4, criterion5), criterion6);

			criteria.add(criterion7);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria.add(Restrictions.ge("startDate", dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria.add(Restrictions.le("endDate", dateRange.getMaximum()));
		}
		criteria.addOrder(Order.desc("id"));
		/*
		 * Integer noOfRecords = criteria.list().size();
		 * 
		 * if (startIndex != null && endIndex != null) {
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); } List<SupportTickets> list = criteria.list();
		 */

		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> adminMap = new HashMap<String, Object>();
		adminMap.put("list", criteriaMap.get("list"));
		adminMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return adminMap;
	}

	// To get the department wise list
	@Override
	public Map<String, Object> getDepartmentWiseTickets(String departmentName, Long categoryId, Long subCategoryId,
			Integer startIndex, Integer endIndex, String progressStatus, String multiSearch, String searchByEmpName,
			String searchByAssigneeName, DateRange dateRange, Long departmentId, Long trackerID) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria.createAlias("ticketsSubCategory", "ticketsSubCategory");
		criteria.createAlias("ticketsSubCategory.ticketsCategory", "category");
		criteria.createAlias("category.empDepartment", "department");
		criteria.add(Restrictions.eq("approvalStatus", "Approved"));
		criteria.add(Restrictions.eq("department.departmentName", departmentName));
		if (categoryId != null) {
			criteria.add(Restrictions.eq("category.id", categoryId));
		}
		if (subCategoryId != null) {
			criteria.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
		}
		if (!(progressStatus.isEmpty() || progressStatus.equalsIgnoreCase("All"))) {
			criteria.add(Restrictions.eq("status", progressStatus));
		}
		if (departmentId != null) {
			criteria.add(Restrictions.eq("department.departmentId", departmentId));
		}
		if (trackerID != null) {
			// criteria.createAlias("category.empDepartment", "department");
			criteria.add(Restrictions.eq("tracker.id", trackerID));
		}

		// For Employee Name search
		if (searchByEmpName.equals("true")) {
			Criteria criteria2 = sessionFactory.getCurrentSession().createCriteria(Employee.class);
			Criterion empFirstName1 = Restrictions.ilike("firstName", multiSearch, MatchMode.ANYWHERE);
			Criterion empLastName1 = Restrictions.ilike("lastName", multiSearch, MatchMode.ANYWHERE);
			criteria2.add(Restrictions.or(empFirstName1, empLastName1));

			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("employeeId"));
			criteria2.setProjection(projections);
			criteria.add(Restrictions.in("createdBy", criteria2.list()));
		}

		// For Assignee Name Search
		if (searchByAssigneeName.equals("true")) {
			criteria.createAlias("assignee", "employee");
			Criterion empFirstName = Restrictions.ilike("employee.firstName", multiSearch, MatchMode.ANYWHERE);
			Criterion empLastName = Restrictions.ilike("employee.lastName", multiSearch, MatchMode.ANYWHERE);
			criteria.add(Restrictions.or(empFirstName, empLastName));
		}

		// For other search
		if (searchByEmpName.equals("false") && searchByAssigneeName.equals("false")) {
			if (multiSearch != null) {
				try {
					criteria.add(Restrictions.eq("id", Long.parseLong(multiSearch)));
				} catch (NumberFormatException ne) {
					Criterion subject = Restrictions.ilike("subject", multiSearch, MatchMode.ANYWHERE);
					Criterion priority = Restrictions.ilike("priority", multiSearch, MatchMode.ANYWHERE);
					Criterion criterion = Restrictions.or(subject, priority);
					criteria.add(criterion);
				}
			}
		}

		// Custom date filter
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			Criterion criterion4 = Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.le("startDate", dateRange.getMaximum()));
			Criterion criterion5 = Restrictions.and(Restrictions.ge("endDate", dateRange.getMinimum()),
					Restrictions.le("endDate", dateRange.getMaximum()));

			Criterion criterion6 = Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
							Restrictions.le("endDate", dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
									Restrictions.le("endDate", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
											Restrictions.le("endDate", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("startDate", dateRange.getMinimum()),
											Restrictions.ge("endDate", dateRange.getMaximum())))));
			Criterion criterion7 = Restrictions.or(Restrictions.or(criterion4, criterion5), criterion6);

			criteria.add(criterion7);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria.add(Restrictions.ge("startDate", dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria.add(Restrictions.le("endDate", dateRange.getMaximum()));
		}
		criteria.addOrder(Order.desc("id"));
		/*
		 * Integer noOfRecords = criteria.list().size(); if (startIndex != null &&
		 * endIndex != null) { criteria.setFirstResult(startIndex);
		 * criteria.setMaxResults(endIndex - startIndex); } List<SupportTickets> list =
		 * criteria.list();
		 */

		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> departmentMap = new HashMap<String, Object>();
		departmentMap.put("list", criteriaMap.get("list"));
		departmentMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return departmentMap;
	}

	@Override
	public List<TicketsCategory> getCategories(String departmentName) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TicketsCategory.class);
		criteria.createAlias("empDepartment", "empDepartment");
		criteria.add(Restrictions.eq("empDepartment.departmentName", departmentName));

		return criteria.list();
	}

	@Override
	public List<SupportTickets> getTicketList(Date date, TicketsSubCategory ticketsSubCategory) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria.createAlias("ticketsSubCategory", "ticketsSubCategory");
		// criteria.createAlias("ticketsWatchers", "ticketsWatcher");
		criteria.add(Restrictions.eq("startDate", date));
		criteria.add(Restrictions.eq("ticketsSubCategory.id", ticketsSubCategory.getId()));
		criteria.add(Restrictions.or(Restrictions.eq("approvalStatus", "Approved"),
				Restrictions.eq("approvalStatus", "Pending Approval")));
		// criteria.add(Restrictions.eq("ticketsWatcher.employee", empId));
		criteria.add(Restrictions.isNotNull("ticketWatchers"));
		return criteria.list();
	}

	@Override
	public Map<String, Object> searchTicketData(Long loggedInEmpId, Integer startIndex, Integer endIndex,
			String multiSearch) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);

		criteria.add(Restrictions.eq("createdBy", loggedInEmpId));

		try {
			criteria.add(Restrictions.eq("id", Long.parseLong(multiSearch)));
		} catch (NumberFormatException exception) {
			Criterion criterion = Restrictions.or(
					Restrictions.or(Restrictions.ilike("priority", multiSearch, MatchMode.ANYWHERE),
							Restrictions.ilike("description", multiSearch, MatchMode.ANYWHERE)),
					Restrictions.ilike("approvalStatus", multiSearch, MatchMode.ANYWHERE));

			criteria.add(Restrictions.or(Restrictions.or(Restrictions.ilike("status", multiSearch, MatchMode.ANYWHERE),
					Restrictions.ilike("subject", multiSearch, MatchMode.ANYWHERE)), criterion));
		}
		criteria.addOrder(Order.desc("id"));
		/*
		 * Integer noOfRecords = criteria.list().size();
		 * 
		 * if (startIndex != null && endIndex != null) {
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); }
		 * 
		 * List<SupportTickets> list = criteria.list();
		 */
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", criteriaMap.get("list"));
		map.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return map;

	}

	@Override
	public List<TicketsCategory> getAllCAtegories() {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(TicketsCategory.class);

		Boolean totalListFalg = false;
		Boolean seperateListFalg = false;

		Permission totalList = dao.checkForPermission("Ticket Configuration", employee);
		Permission departmentList = dao.checkForPermission("Departmentwise Category List", employee);

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

		criteria.addOrder(Order.desc("id"));

		return criteria.list();
	};

	@Override
	public List<EmpDepartment> departmentWiseList(Employee employee) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmpDepartment.class);
		criteria.add(Restrictions.eq("departmentName", employee.getDepartmentName()));
		return criteria.list();
	};

	@Transactional
	@Override
	public List<SupportTickets> getTicketHistorys(Long ticketId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(SupportTickets.class);
		criteria.add(Restrictions.eq("id", ticketId));
		return criteria.list();
	}

	@Override
	public List<EmpDepartment> getDepartmentNameList() {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmpDepartment.class);

		Boolean totalListFalg = false;
		Boolean seperateListFalg = false;

		Permission totalList = dao.checkForPermission("Ticket Configuration", employee);
		Permission departmentList = dao.checkForPermission("Departmentwise Category List", employee);

		if (totalList.getView() && !departmentList.getView())
			totalListFalg = true;
		else if (departmentList.getView() && totalList.getView())
			seperateListFalg = true;
		if (totalListFalg) {
			criteria.add(Restrictions.eq("supportManagementFlag", Boolean.TRUE));
		} else if (seperateListFalg) {
			criteria.add(Restrictions.eq("departmentName", employee.getDepartmentName()));

		}

		// Criteria criteria =
		// getSessionFactory().getCurrentSession().createCriteria(EmpDepartment.class);

		return criteria.list();
	}

	@Override
	public List<EmpDepartment> getDepartmentListForRaise() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmpDepartment.class);
		criteria.add(Restrictions.eq("supportManagementFlag", Boolean.TRUE));
		return criteria.list();
	}

	@Override
	public List<Tracker> getAllTracker() {
		return sessionFactory.getCurrentSession().createCriteria(Tracker.class).list();
	}

	@Override
	public Integer trackerInUse(Long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria.add(Restrictions.eq("tracker.id", id));
		return criteria.list().size();
	}

	@Override
	public List<SupportTickets> getAccessTicketsWhoseEndDateisInNextFiveDays() {

		Date date = new Date();
		int i = 0;
		while (i <= 5) {
			date = date.next();
			i++;
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria.add(Restrictions.between("accessEndDate", new Date(), date));
		return criteria.list();

	}

	// To get the tickets report Admin
	@Override
	public Map<String, Object> getTicketsReportForAdmin(DateRange dateRange, Long departmentId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria.createAlias("ticketsSubCategory", "ticketsSubCategory");
		criteria.createAlias("ticketsSubCategory.ticketsCategory", "category");
		criteria.createAlias("category.empDepartment", "department");
		if (departmentId != null) {
			criteria.add(Restrictions.eq("department.departmentId", departmentId));
		}
		// Custom date filter
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			Criterion criterion4 = Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.le("startDate", dateRange.getMaximum()));
			Criterion criterion5 = Restrictions.and(Restrictions.ge("endDate", dateRange.getMinimum()),
					Restrictions.le("endDate", dateRange.getMaximum()));

			Criterion criterion6 = Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
							Restrictions.le("endDate", dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
									Restrictions.le("endDate", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
											Restrictions.le("endDate", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("startDate", dateRange.getMinimum()),
											Restrictions.ge("endDate", dateRange.getMaximum())))));
			Criterion criterion7 = Restrictions.or(Restrictions.or(criterion4, criterion5), criterion6);

			criteria.add(criterion7);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria.add(Restrictions.ge("startDate", dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria.add(Restrictions.le("endDate", dateRange.getMaximum()));
		}
		criteria.addOrder(Order.desc("id"));

		Integer noOfRecords = criteria.list().size();
		List<SupportTickets> list = criteria.list();

		Map<String, Object> adminMap = new HashMap<String, Object>();
		adminMap.put("list", list);
		adminMap.put("size", noOfRecords);

		return adminMap;

	}

	// To get the tickets report for Manager
	@Override
	public Map<String, Object> getManagerTicketsReport(String managerId, DateRange dateRange, Long departmentId) {

		Criteria criteria2 = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria2.createAlias("ticketsSubCategory", "ticketsSubCategory");
		criteria2.createAlias("ticketsSubCategory.ticketsCategory", "category");
		criteria2.createAlias("category.empDepartment", "department");
		// criteria2.add(Restrictions.in("createdBy", criteria.list()));
		criteria2.add(Restrictions.ilike("managesList", managerId, MatchMode.ANYWHERE));

		if (departmentId != null) {
			criteria2.add(Restrictions.eq("department.departmentId", departmentId));
		}

		// Custom date filter
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			Criterion criterion4 = Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.le("startDate", dateRange.getMaximum()));
			Criterion criterion5 = Restrictions.and(Restrictions.ge("endDate", dateRange.getMinimum()),
					Restrictions.le("endDate", dateRange.getMaximum()));

			Criterion criterion6 = Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
							Restrictions.le("endDate", dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
									Restrictions.le("endDate", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
											Restrictions.le("endDate", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("startDate", dateRange.getMinimum()),
											Restrictions.ge("endDate", dateRange.getMaximum())))));
			Criterion criterion7 = Restrictions.or(Restrictions.or(criterion4, criterion5), criterion6);

			criteria2.add(criterion7);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria2.add(Restrictions.ge("startDate", dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria2.add(Restrictions.le("endDate", dateRange.getMaximum()));
		}

		criteria2.addOrder(Order.desc("id"));
		Integer noOfRecords = criteria2.list().size();
		List<SupportTickets> list = criteria2.list();
		Map<String, Object> reporteeReportMap = new HashMap<String, Object>();
		reporteeReportMap.put("list", list);
		reporteeReportMap.put("size", noOfRecords);

		return reporteeReportMap;

	}

	// To get the tickets report for Department
	@Override
	public Map<String, Object> getDepartmentWiseTicketsReport(String departmentName, DateRange dateRange,
			Long departmentId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria.createAlias("ticketsSubCategory", "ticketsSubCategory");
		criteria.createAlias("ticketsSubCategory.ticketsCategory", "category");
		criteria.createAlias("category.empDepartment", "department");
		criteria.add(Restrictions.eq("approvalStatus", "Approved"));
		criteria.add(Restrictions.eq("department.departmentName", departmentName));
		if (departmentId != null) {
			criteria.add(Restrictions.eq("department.departmentId", departmentId));
		}
		// Custom date filter
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			Criterion criterion4 = Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.le("startDate", dateRange.getMaximum()));
			Criterion criterion5 = Restrictions.and(Restrictions.ge("endDate", dateRange.getMinimum()),
					Restrictions.le("endDate", dateRange.getMaximum()));

			Criterion criterion6 = Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
							Restrictions.le("endDate", dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
									Restrictions.le("endDate", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
											Restrictions.le("endDate", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("startDate", dateRange.getMinimum()),
											Restrictions.ge("endDate", dateRange.getMaximum())))));
			Criterion criterion7 = Restrictions.or(Restrictions.or(criterion4, criterion5), criterion6);

			criteria.add(criterion7);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria.add(Restrictions.ge("startDate", dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria.add(Restrictions.le("endDate", dateRange.getMaximum()));
		}
		criteria.addOrder(Order.desc("id"));
		Integer noOfRecords = criteria.list().size();
		List<SupportTickets> list = criteria.list();
		Map<String, Object> departmentReportMap = new HashMap<String, Object>();
		departmentReportMap.put("list", list);
		departmentReportMap.put("size", noOfRecords);

		return departmentReportMap;

	}

	// To get the tickets list according to status for Manager
	@Override
	public Map<String, Object> getManagerTicketsDetails(String managerId, String filter, Long trackerId,
			Integer startIndex, Integer endIndex, Long categoryId, Long subCategoryId, DateRange dateRange,
			Long departmentId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria.createAlias("ticketsSubCategory", "ticketsSubCategory");
		criteria.createAlias("ticketsSubCategory.ticketsCategory", "category");
		criteria.createAlias("category.empDepartment", "department");
		criteria.add(Restrictions.ilike("managesList", managerId, MatchMode.ANYWHERE));

		// for total tickets
		if (filter.equalsIgnoreCase("All")) {

			if (categoryId != null) {
				criteria.add(Restrictions.eq("category.id", categoryId));
			}
			if (subCategoryId != null) {
				criteria.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
			}
			if (departmentId != null) {
				criteria.add(Restrictions.eq("department.departmentId", departmentId));
			}
			if (trackerId != null) {
				criteria.add(Restrictions.eq("tracker.id", trackerId));
			}
		}

		// for pending tickets
		if (filter.equalsIgnoreCase("pending")) {

			Criterion New = Restrictions.ilike("status", "New", MatchMode.ANYWHERE);
			Criterion InProgress = Restrictions.ilike("status", "In Progress", MatchMode.ANYWHERE);
			Criterion Feedback = Restrictions.ilike("status", "Feedback", MatchMode.ANYWHERE);
			Criterion Fixed = Restrictions.ilike("status", "Fixed", MatchMode.ANYWHERE);
			Criterion criterion = Restrictions.or(New, InProgress);
			criteria.add(Restrictions.or(Restrictions.or(Fixed, Feedback), criterion));

			if (categoryId != null) {
				criteria.add(Restrictions.eq("category.id", categoryId));
			}
			if (subCategoryId != null) {
				criteria.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
			}
			if (departmentId != null) {
				criteria.add(Restrictions.eq("department.departmentId", departmentId));
			}
			if (trackerId != null) {
				criteria.add(Restrictions.eq("tracker.id", trackerId));
			}
		}

		// for closed tickets
		if (filter.equalsIgnoreCase("closed")) {

			criteria.add(Restrictions.ilike("status", "closed", MatchMode.ANYWHERE));

			if (categoryId != null) {
				criteria.add(Restrictions.eq("category.id", categoryId));
			}
			if (subCategoryId != null) {
				criteria.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
			}
			if (departmentId != null) {
				criteria.add(Restrictions.eq("department.departmentId", departmentId));
			}
			if (trackerId != null) {
				criteria.add(Restrictions.eq("tracker.id", trackerId));
			}
		}

		// Custom date filter
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			Criterion criterion4 = Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.le("startDate", dateRange.getMaximum()));
			Criterion criterion5 = Restrictions.and(Restrictions.ge("endDate", dateRange.getMinimum()),
					Restrictions.le("endDate", dateRange.getMaximum()));

			Criterion criterion6 = Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
							Restrictions.le("endDate", dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
									Restrictions.le("endDate", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
											Restrictions.le("endDate", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("startDate", dateRange.getMinimum()),
											Restrictions.ge("endDate", dateRange.getMaximum())))));
			Criterion criterion7 = Restrictions.or(Restrictions.or(criterion4, criterion5), criterion6);

			criteria.add(criterion7);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria.add(Restrictions.ge("startDate", dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria.add(Restrictions.le("endDate", dateRange.getMaximum()));
		}

		criteria.addOrder(Order.desc("id"));
		/*
		 * Integer noOfRecords = criteria.list().size(); if (startIndex != null &&
		 * endIndex != null) { criteria.setFirstResult(startIndex);
		 * criteria.setMaxResults(endIndex - startIndex); } List<SupportTickets> list =
		 * criteria.list();
		 */
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> managerMap = new HashMap<String, Object>();
		managerMap.put("list", criteriaMap.get("list"));
		managerMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return managerMap;

	}

	// To get the tickets list according to status for Admin
	@Override
	public Map<String, Object> getTotalTicketsDetailsForAdmin(String filter, Long trackerId, Integer startIndex,
			Integer endIndex, Long categoryId, Long subCategoryId, DateRange dateRange, Long departmentId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria.createAlias("ticketsSubCategory", "ticketsSubCategory");
		criteria.createAlias("ticketsSubCategory.ticketsCategory", "category");
		criteria.createAlias("category.empDepartment", "department");

		// for total tickets
		if (filter.equalsIgnoreCase("All")) {

			if (categoryId != null) {
				criteria.add(Restrictions.eq("category.id", categoryId));
			}
			if (subCategoryId != null) {
				criteria.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
			}
			if (departmentId != null) {
				criteria.add(Restrictions.eq("department.departmentId", departmentId));
			}
			if (trackerId != null) {
				criteria.add(Restrictions.eq("tracker.id", trackerId));
			}
		}

		// for pending tickets
		if (filter.equalsIgnoreCase("pending")) {

			Criterion New = Restrictions.ilike("status", "New", MatchMode.ANYWHERE);
			Criterion InProgress = Restrictions.ilike("status", "In Progress", MatchMode.ANYWHERE);
			Criterion Feedback = Restrictions.ilike("status", "Feedback", MatchMode.ANYWHERE);
			Criterion Fixed = Restrictions.ilike("status", "Fixed", MatchMode.ANYWHERE);
			Criterion criterion = Restrictions.or(New, InProgress);
			criteria.add(Restrictions.or(Restrictions.or(Fixed, Feedback), criterion));

			if (categoryId != null) {
				criteria.add(Restrictions.eq("category.id", categoryId));
			}
			if (subCategoryId != null) {
				criteria.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
			}
			if (departmentId != null) {
				criteria.add(Restrictions.eq("department.departmentId", departmentId));
			}
			if (trackerId != null) {
				criteria.add(Restrictions.eq("tracker.id", trackerId));
			}
		}

		// for closed tickets
		if (filter.equalsIgnoreCase("closed")) {

			criteria.add(Restrictions.ilike("status", "closed", MatchMode.ANYWHERE));

			if (categoryId != null) {
				criteria.add(Restrictions.eq("category.id", categoryId));
			}
			if (subCategoryId != null) {
				criteria.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
			}
			if (departmentId != null) {
				criteria.add(Restrictions.eq("department.departmentId", departmentId));
			}
			if (trackerId != null) {
				criteria.add(Restrictions.eq("tracker.id", trackerId));
			}
		}

		// Custom date filter
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			Criterion criterion4 = Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.le("startDate", dateRange.getMaximum()));
			Criterion criterion5 = Restrictions.and(Restrictions.ge("endDate", dateRange.getMinimum()),
					Restrictions.le("endDate", dateRange.getMaximum()));

			Criterion criterion6 = Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
							Restrictions.le("endDate", dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
									Restrictions.le("endDate", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
											Restrictions.le("endDate", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("startDate", dateRange.getMinimum()),
											Restrictions.ge("endDate", dateRange.getMaximum())))));
			Criterion criterion7 = Restrictions.or(Restrictions.or(criterion4, criterion5), criterion6);

			criteria.add(criterion7);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria.add(Restrictions.ge("startDate", dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria.add(Restrictions.le("endDate", dateRange.getMaximum()));
		}
		criteria.addOrder(Order.desc("id"));
		/*
		 * Integer noOfRecords = criteria.list().size();
		 * 
		 * if (startIndex != null && endIndex != null) {
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); } List<SupportTickets> list = criteria.list();
		 */
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> adminMap = new HashMap<String, Object>();
		adminMap.put("list", criteriaMap.get("list"));
		adminMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return adminMap;

	}

	// To get the tickets list according to status for Department
	@Override
	public Map<String, Object> getDepartmentWiseTicketsDetails(String departmentName, String filter, Long trackerId,
			Integer startIndex, Integer endIndex, Long categoryId, Long subCategoryId, DateRange dateRange,
			Long departmentId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria.createAlias("ticketsSubCategory", "ticketsSubCategory");
		criteria.createAlias("ticketsSubCategory.ticketsCategory", "category");
		criteria.createAlias("category.empDepartment", "department");
		criteria.add(Restrictions.eq("approvalStatus", "Approved"));
		criteria.add(Restrictions.eq("department.departmentName", departmentName));

		// for total tickets
		if (filter.equalsIgnoreCase("All")) {

			if (categoryId != null) {
				criteria.add(Restrictions.eq("category.id", categoryId));
			}
			if (subCategoryId != null) {
				criteria.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
			}
			if (departmentId != null) {
				criteria.add(Restrictions.eq("department.departmentId", departmentId));
			}
			if (trackerId != null) {
				criteria.add(Restrictions.eq("tracker.id", trackerId));
			}
		}

		// for pending tickets
		if (filter.equalsIgnoreCase("pending")) {

			Criterion New = Restrictions.ilike("status", "New", MatchMode.ANYWHERE);
			Criterion InProgress = Restrictions.ilike("status", "In Progress", MatchMode.ANYWHERE);
			Criterion Feedback = Restrictions.ilike("status", "Feedback", MatchMode.ANYWHERE);
			Criterion Fixed = Restrictions.ilike("status", "Fixed", MatchMode.ANYWHERE);
			Criterion criterion = Restrictions.or(New, InProgress);
			criteria.add(Restrictions.or(Restrictions.or(Fixed, Feedback), criterion));

			if (categoryId != null) {
				criteria.add(Restrictions.eq("category.id", categoryId));
			}
			if (subCategoryId != null) {
				criteria.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
			}
			if (departmentId != null) {
				criteria.add(Restrictions.eq("department.departmentId", departmentId));
			}
			if (trackerId != null) {
				criteria.add(Restrictions.eq("tracker.id", trackerId));
			}
		}

		// for closed tickets
		if (filter.equalsIgnoreCase("closed")) {

			criteria.add(Restrictions.ilike("status", "closed", MatchMode.ANYWHERE));

			if (categoryId != null) {
				criteria.add(Restrictions.eq("category.id", categoryId));
			}
			if (subCategoryId != null) {
				criteria.add(Restrictions.eq("ticketsSubCategory.id", subCategoryId));
			}
			if (departmentId != null) {
				criteria.add(Restrictions.eq("department.departmentId", departmentId));
			}
			if (trackerId != null) {
				criteria.add(Restrictions.eq("tracker.id", trackerId));
			}
		}

		// Custom date filter
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			Criterion criterion4 = Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.le("startDate", dateRange.getMaximum()));
			Criterion criterion5 = Restrictions.and(Restrictions.ge("endDate", dateRange.getMinimum()),
					Restrictions.le("endDate", dateRange.getMaximum()));

			Criterion criterion6 = Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
							Restrictions.le("endDate", dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
									Restrictions.le("endDate", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("startDate", dateRange.getMinimum()),
											Restrictions.le("endDate", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("startDate", dateRange.getMinimum()),
											Restrictions.ge("endDate", dateRange.getMaximum())))));
			Criterion criterion7 = Restrictions.or(Restrictions.or(criterion4, criterion5), criterion6);

			criteria.add(criterion7);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria.add(Restrictions.ge("startDate", dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria.add(Restrictions.le("endDate", dateRange.getMaximum()));
		}
		criteria.addOrder(Order.desc("id"));
		/*
		 * Integer noOfRecords = criteria.list().size(); if (startIndex != null &&
		 * endIndex != null) { criteria.setFirstResult(startIndex);
		 * criteria.setMaxResults(endIndex - startIndex); } List<SupportTickets> list =
		 * criteria.list();
		 */
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> departmentMap = new HashMap<String, Object>();
		departmentMap.put("list", criteriaMap.get("list"));
		departmentMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return departmentMap;

	}

	@Override
	public List<String> getManagersListforPendingFoodTicket() {

		Date date = new Date();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(SupportTickets.class);
		criteria.createAlias("ticketsSubCategory", "ticketsSubCategory");
		criteria.createAlias("ticketsSubCategory.ticketsCategory", "category");
		criteria.add(Restrictions.eq("startDate", date));
		criteria.add(Restrictions.eq("category.mealType", Boolean.TRUE));
		criteria.add(Restrictions.eq("approvalStatus", "Pending Approval"));

		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.property("managesList"));
		criteria.setProjection(projections);
		List<String> managersList =criteria.list();

		return managersList;
	}

}
