package com.raybiztech.separation.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.loader.custom.Return;
import org.springframework.stereotype.Repository;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.raybiztech.TimeActivity.business.EmpoloyeeHiveActivity;
import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.biometric.business.AttendanceStatus;
import com.raybiztech.biometric.business.BioAttendance;
import com.raybiztech.date.Date;
import com.raybiztech.separation.business.ExitFeedBack;
import com.raybiztech.separation.business.PrimaryReason;
import com.raybiztech.separation.business.Separation;
import com.raybiztech.separation.business.SeparationComments;
import com.raybiztech.separation.business.SeparationStatus;

@Repository("separationDaoImpl")
public class SeparationDaoImpl extends DAOImpl implements SeparationDao {

	@Override
	public Map<String, Object> getResignationList(String multipleSearch,
			Integer startIndex, Integer endIndex, Date fromDate, Date toDate,
			String separationStatus,String empStatus) {
		Map<String, Object> map = new HashMap<String, Object>();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Separation.class);

		if (fromDate != null && toDate != null) {
			criteria.add(Restrictions.between("resignationDate", fromDate,
					toDate));
		}

		criteria.createAlias("employee", "employee");

		if (!multipleSearch.isEmpty())
			criteria.add(Restrictions.ilike("employee.employeeFullName",
					multipleSearch, MatchMode.ANYWHERE));

		if (!separationStatus.equalsIgnoreCase("ALL")) {
			SeparationStatus status = SeparationStatus
					.valueOf(separationStatus);
			criteria.add(Restrictions.eq("status", status));

		} 
		if(empStatus.equalsIgnoreCase("Active")){
			System.out.println("in active");
			criteria.add(Restrictions.eq("employee.statusName", "Active"));
		}
		if(empStatus.equalsIgnoreCase("InActive")){
			System.out.println("in Inactive");
			criteria.add(Restrictions.eq("employee.statusName", "InActive"));
		}
		/*else {

			criteria.add(Restrictions.ne("status", SeparationStatus.REVOKED));
			criteria.add(Restrictions.ne("status", SeparationStatus.RELIEVED));
		}*/

		criteria.addOrder(Order.desc("separationId"));

		/*
		 * Long records = (long) criteria.list().size(); if (startIndex != null
		 * && endIndex != null) { criteria.setFirstResult(startIndex);
		 * criteria.setMaxResults(endIndex - startIndex); } map.put("list",
		 * criteria.list()); map.put("size", records); return map;
		 */

		Map<String, Object> separationMap = getPaginationList(criteria,
				startIndex, endIndex);
		map.put("size", separationMap.get("listSize"));
		map.put("list", separationMap.get("list"));
		return map;
	}

	@Override
	public Map<String, Object> getEmployeeResignationList(
			String multipleSearch, Integer startIndex, Integer endIndex,
			Date fromDate, Date toDate, Employee employee,
			String separationStatus) {
		Map<String, Object> map = new HashMap<String, Object>();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Separation.class);

		criteria.add(Restrictions.eq("employee", employee));
		if (fromDate != null && toDate != null) {
			criteria.add(Restrictions.between("resignationDate", fromDate,
					toDate));
		}

		criteria.createAlias("employee", "employee");
		if (!multipleSearch.isEmpty())
			criteria.add(Restrictions.ilike("employee.employeeFullName",
					multipleSearch, MatchMode.ANYWHERE));

		if (!separationStatus.equalsIgnoreCase("ALL")) {
			SeparationStatus status = SeparationStatus
					.valueOf(separationStatus);
			criteria.add(Restrictions.eq("status", status));
		}

		criteria.addOrder(Order.desc("separationId"));

		/*
		 * Long records = (long) criteria.list().size(); if (startIndex != null
		 * && endIndex != null) { criteria.setFirstResult(startIndex);
		 * criteria.setMaxResults(endIndex - startIndex); } map.put("list",
		 * criteria.list()); map.put("size", records); return map;
		 */

		Map<String, Object> separationMap = getPaginationList(criteria,
				startIndex, endIndex);
		map.put("size", separationMap.get("listSize"));
		map.put("list", separationMap.get("list"));
		return map;
	}

	@Override
	public Map<String, Object> getEmployeeResignationListUnderManager(
			List<Long> managersId, String multipleSearch, Integer startIndex,
			Integer endIndex, Date fromDate, Date toDate,
			String separationStatus, Employee employee) {
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Separation.class);
		if (fromDate != null && toDate != null) {
			criteria.add(Restrictions.between("resignationDate", fromDate,
					toDate));
		}
		criteria.createAlias("employee", "employee");
		criteria.createAlias("employee.manager", "manager");
		// criteria.setFetchMode("manager", FetchMode.JOIN);

		criteria.add(Restrictions.in("manager.employeeId", managersId));

		// criteria.add(Restrictions.ne("employee", employee));

		if (!multipleSearch.isEmpty())
			criteria.add(Restrictions.ilike("employee.employeeFullName",
					multipleSearch, MatchMode.ANYWHERE));

		if (!separationStatus.equalsIgnoreCase("ALL")) {
			SeparationStatus status = SeparationStatus
					.valueOf(separationStatus);
			criteria.add(Restrictions.eq("status", status));
		}

		else {
			criteria.add(Restrictions.ne("status", SeparationStatus.REVOKED));
			criteria.add(Restrictions.ne("status", SeparationStatus.RELIEVED));
		}
       
		criteria.addOrder(Order.desc("separationId"));

		/*
		 * if (startIndex != null && endIndex != null) {
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex
		 * - startIndex); } map.put("list", criteria.list()); map.put("size",
		 * criteria.list().size()); return map;
		 */

		Map<String, Object> separationMap = getPaginationList(criteria,
				startIndex, endIndex);
		map.put("size", separationMap.get("listSize"));
		map.put("list", separationMap.get("list"));
		return map;
	}

	@Override
	public Boolean isSepartionExists(Employee employee) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Separation.class);
		criteria.add(Restrictions.eq("employee", employee));
		criteria.add(Restrictions.eq("isRevoked", Boolean.FALSE));
		return criteria.list().isEmpty() ? Boolean.FALSE : Boolean.TRUE;
	}

	@Override
	public ExitFeedBack checkExitFeedBackForm(Long separationId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ExitFeedBack.class);
		criteria.createAlias("separation", "separation");
		criteria.add(Restrictions.eq("separation.separationId", separationId));
		// criteria.uniqueResult();
		ExitFeedBack exitFeedBack = (ExitFeedBack) criteria.uniqueResult();
		return exitFeedBack;
	}

	@Override
	public Separation getEmployeeSepartion(Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Separation.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		criteria.add(Restrictions.eq("isRevoked", false));
		Separation separation = (Separation) criteria.uniqueResult();
		return separation;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Separation> getRelivingEndDateEmployees() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Separation.class);
		criteria.add(Restrictions.and(Restrictions.eq("relievingDate", new Date()),
				Restrictions.eq("isRevoked", Boolean.FALSE)));
		return criteria.list();
	}

	@Override
	public Map<String, Object> getSeparationChartDetails(
			List<PrimaryReason> primaryReasons, Date from, Date to) {

		Map<String, Object> map = new HashMap<String, Object>();
		for (PrimaryReason primaryReason : primaryReasons) {
			Criteria criteria = sessionFactory.getCurrentSession()
					.createCriteria(Separation.class);
			criteria.add(Restrictions.eq("primaryReason", primaryReason));
			if (from != null && to != null) {
				criteria.add(Restrictions.between("resignationDate", from, to));
			}

			map.put(primaryReason.getReasonName(), criteria.list().size());
			sessionFactory.getCurrentSession().flush();
		}
		return map;
	}

	@Override
	public Map<String, Object> getInititateResignationList(
			String multipleSearch, Integer startIndex, Integer endIndex,
			Date fromDate, Date toDate, String separationStatus,
			Employee loggedEmployee) {
		Map<String, Object> map = new HashMap<String, Object>();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Separation.class);
		if (fromDate != null && toDate != null) {
			criteria.add(Restrictions.between("resignationDate", fromDate,
					toDate));
		}
		criteria.createAlias("employee", "employee");

		if (!multipleSearch.isEmpty())
			criteria.add(Restrictions.ilike("employee.employeeFullName",
					multipleSearch, MatchMode.ANYWHERE));

		if (!separationStatus.equalsIgnoreCase("ALL")) {
			SeparationStatus status = SeparationStatus
					.valueOf(separationStatus);
			criteria.add(Restrictions.eq("status", status));
		} else {
			criteria.add(Restrictions.ne("status", SeparationStatus.REVOKED));
			criteria.add(Restrictions.ne("status", SeparationStatus.RELIEVED));
		}

		criteria.add(Restrictions.eq("isprocessInitiated", Boolean.TRUE));
		criteria.add(Restrictions.ne("employee", loggedEmployee));
		criteria.addOrder(Order.desc("separationId"));

		/*
		 * Long records = (long) criteria.list().size(); if (startIndex != null
		 * && endIndex != null) { criteria.setFirstResult(startIndex);
		 * criteria.setMaxResults(endIndex - startIndex); } map.put("list",
		 * criteria.list()); map.put("size", records); return map;
		 */

		Map<String, Object> separationMap = getPaginationList(criteria,
				startIndex, endIndex);
		map.put("size", separationMap.get("listSize"));
		map.put("list", separationMap.get("list"));
		return map;
	}

	@Override
	public Map<String, Object> getInititateResignationListUnderManager(
			Employee employee, String multiplesearch, Integer startIndex,
			Integer endIndex, Date fromDate, Date toDate,
			String separationStatus) {
		Map<String, Object> map = new HashMap<String, Object>();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Separation.class);

		if (fromDate != null && toDate != null) {
			criteria.add(Restrictions.between("resignationDate", fromDate,
					toDate));
		}

		criteria.createAlias("employee", "employee");
		criteria.createAlias("employee.manager", "manager");
		criteria.add(Restrictions.ne("employee.employeeId",
				employee.getEmployeeId()));

		criteria.add(Restrictions.or(
				Restrictions.eq("manager.employeeId", employee.getEmployeeId()),
				Restrictions.eq("isprocessInitiated", Boolean.TRUE)));

		/* criteria.add(Restrictions.ne("employee", employee)); */

		if (!multiplesearch.isEmpty())
			criteria.add(Restrictions.ilike("employee.employeeFullName",
					multiplesearch, MatchMode.ANYWHERE));

		if (!separationStatus.equalsIgnoreCase("ALL")) {
			SeparationStatus status = SeparationStatus
					.valueOf(separationStatus);
			criteria.add(Restrictions.eq("status", status));

		} else {
			criteria.add(Restrictions.ne("status", SeparationStatus.REVOKED));
			criteria.add(Restrictions.ne("status", SeparationStatus.RELIEVED));
		}

		criteria.addOrder(Order.desc("separationId"));

		/*
		 * Long records = (long) criteria.list().size(); if (startIndex != null
		 * && endIndex != null) { criteria.setFirstResult(startIndex);
		 * criteria.setMaxResults(endIndex - startIndex); } map.put("list",
		 * criteria.list()); map.put("size", records); return map;
		 */

		Map<String, Object> separationMap = getPaginationList(criteria,
				startIndex, endIndex);
		map.put("size", separationMap.get("listSize"));
		map.put("list", separationMap.get("list"));
		return map;
	}

	@Override
	public Employee getDeliveryManagerOfEmployee(Employee employee) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.createAlias("manager", "manager");
		criteria.add(Restrictions.eq("manager.role", "deliverymanager"));
		Employee emp = (Employee) criteria.uniqueResult();
		return emp;

	}

	@Override
	public Boolean checkPIP(Long employeeId) {
		
		Criteria criteria= sessionFactory.getCurrentSession().createCriteria(ExitFeedBack.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		criteria.add(Restrictions.eq("isPIP", true));
		
		Boolean isPIP=false;
		
		if(criteria.list().size()>0)
		{
			isPIP=true;
		}
		
		return isPIP;
	}

	@Override
	public ExitFeedBack getEmployeePIPexitFeedBack(Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ExitFeedBack.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		// criteria.uniqueResult();
		ExitFeedBack exitFeedBack = (ExitFeedBack) criteria.uniqueResult();
		return exitFeedBack;
	}

	@Override
	public List<PIP> getEmployeePIPList(Long employeeId) {
		
		
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(PIP.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		
		return criteria.list();
	}

	@Override
	public Boolean isSepartionExistsForEmployee(Employee employee) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Separation.class);
		criteria.add(Restrictions.eq("employee", employee));
		return criteria.list().isEmpty() ? Boolean.FALSE : Boolean.TRUE;
	}
	
	@Override
	public Separation getEmployeeSepartionForLeave(Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Separation.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		criteria.addOrder(Order.desc("separationId"));
		Separation separation = (Separation) criteria.list().get(0);
		return separation;
	}
	/*@Override
	 public List<Long> AllEmpIds(){
		 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Separation.class);
                criteria.createAlias("employee", "employee");
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.property("employee.employeeId"));
			criteria.setProjection(projectionList);
			List<Long> list = criteria.list();
			
			return list;
	    }*/
}
