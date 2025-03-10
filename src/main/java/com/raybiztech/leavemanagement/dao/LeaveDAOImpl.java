package com.raybiztech.leavemanagement.dao;

import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.CarryForwardLeave;
import com.raybiztech.leavemanagement.business.LeaveCategory;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.business.LeaveSettingsLookup;
import com.raybiztech.leavemanagement.business.LeaveStatus;
import com.raybiztech.leavemanagement.business.LeaveType;
import com.raybiztech.leavemanagement.business.PayrollCutOffDates;
import com.raybiztech.leavemanagement.business.ProbationPeriod;
import com.raybiztech.leavemanagement.dto.SearchCriteriaDTO;
import com.raybiztech.leavemanagement.exceptions.LeaveCannotProcessException;




@Repository("leaveDAO")
public class LeaveDAOImpl extends DAOImpl implements LeaveDAO {
	
	
	
	@Autowired
	SecurityUtils securityUtils;

	@Override
	public Map<String, Object> getAllLeaves(Integer startIndex,
			Integer endIndex, Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "emp");
		criteria.setFetchMode("emp", FetchMode.JOIN);
		criteria.add(Restrictions.eq("emp.employeeId", employeeId));
		Integer allLeavesSize = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		criteria.addOrder(Order.desc("period.minimum"));
		List<LeaveDebit> leaveDebitsList = criteria.list();
		Map<String, Object> leaveDebitMap = new HashMap<String, Object>();

		leaveDebitMap.put("leaveDebitsList", leaveDebitsList);
		leaveDebitMap.put("size", allLeavesSize);

		return leaveDebitMap;

	}

	@Override
	public SortedSet<LeaveCategory> getAllLeaveCategories() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveCategory.class);
		return new TreeSet<LeaveCategory>(criteria.list());
	}

	@Override
	public <T extends Serializable> T getLeaveCategoryByName(Serializable name) {
		// TODO Auto-generated method stub
		return (T) sessionFactory.getCurrentSession()
				.createCriteria(LeaveCategory.class)
				.add(Restrictions.eq("name", name)).uniqueResult();
	}

	@Override
	public List<LeaveDebit> numberOfTimesApplied(LeaveDebit leave) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);

		criteria.createAlias("employee", "emp");
		criteria.setFetchMode("emp", FetchMode.JOIN);
		criteria.add(Restrictions.eq("emp.employeeId", leave.getEmployee()
				.getEmployeeId()));

		Criterion criterion1 = Restrictions.between("period.minimum", leave
				.getPeriod().getMinimum(), leave.getPeriod().getMaximum());
		
	//	System.out.println("period minimum"+ leave.getPeriod().getMinimum());
		//System.out.println("period maximum"+ leave.getPeriod().getMaximum());
		
		Criterion criterion2 = Restrictions.between("period.maximum", leave
				.getPeriod().getMinimum(), leave.getPeriod().getMaximum());

		Criterion criterion3 = Restrictions.and(Restrictions.le(
				"period.minimum", leave.getPeriod().getMinimum()), Restrictions
				.ge("period.maximum", leave.getPeriod().getMaximum()));

		Criterion criterion4 = Restrictions.and(Restrictions.ge(
				"period.minimum", leave.getPeriod().getMinimum()), Restrictions
				.le("period.maximum", leave.getPeriod().getMaximum()));

		criteria.add(Restrictions.or(Restrictions.or(criterion1, criterion2),
				Restrictions.or(criterion3, criterion4)));

		criteria.add(Restrictions.and(
				Restrictions.ne("status", LeaveStatus.Cancelled),
				Restrictions.ne("status", LeaveStatus.Rejected)));

		return criteria.list();
	}

	@Override
	public Map<String, Object> getReporteePendingLeaves(Integer startIndex,
			Integer endIndex, List<Long> managerEmployeeId, DateRange period ) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		
		System.out.println("logged in employee Idssss"+managerEmployeeId);
		criteria.createAlias("employee", "emp");
		criteria.setFetchMode("emp", FetchMode.JOIN);
		//criteria.createAlias("emo.projectManager", "projectManager");
		criteria.createAlias("emp.manager", "mgr");
		criteria.setFetchMode("mgr", FetchMode.JOIN);
		criteria.add(Restrictions.in("mgr.employeeId", managerEmployeeId));
		criteria.add(Restrictions.eq("status", LeaveStatus.PendingApproval));
		criteria.add(Restrictions.eq("emp.statusName", "Active"));
		Criterion criterion2 = Restrictions.between("period.minimum",
				period.getMinimum(), period.getMaximum());
		Criterion criterion3 = Restrictions.between("period.maximum",
				period.getMinimum(), period.getMaximum());
		Criterion criterion = Restrictions.or(criterion2, criterion3);
		criteria.add(criterion);
		Integer noOfReporteePendingLeaves = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		criteria.addOrder(Order.desc("period.minimum"));
		List<LeaveDebit> leaveDebitsList = criteria.list();
		Map<String, Object> reporteePendingLeavesMap = new HashMap<String, Object>();
		reporteePendingLeavesMap.put("leaveDebitList", leaveDebitsList);
		reporteePendingLeavesMap.put("size", noOfReporteePendingLeaves);
		return reporteePendingLeavesMap;
	}

	@Override
	public Map<String, Object> searchEmployees(Integer startIndex,
			Integer endIndex, List<Long> managerIds,
			SearchCriteriaDTO searchCriteriaDTO) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "emp");
		criteria.add(Restrictions.eq("emp.statusName", "Active"));
		criteria.setFetchMode("emp", FetchMode.JOIN);
		criteria.createAlias("emp.manager", "mgr");
		criteria.setFetchMode("mgr", FetchMode.JOIN);

		/*
		 * criteria.add(Restrictions.eq("mgr.employeeId",
		 * searchCriteriaDTO.getManagerId()));
		 */
		criteria.add(Restrictions.in("mgr.employeeId", managerIds));

		try {
			Criterion criterion2 = Restrictions.between("period.minimum",
					searchCriteriaDTO.getPeriod().getMinimum(),
					searchCriteriaDTO.getPeriod().getMaximum());
			Criterion criterion3 = Restrictions.between("period.maximum",
					searchCriteriaDTO.getPeriod().getMinimum(),
					searchCriteriaDTO.getPeriod().getMaximum());
			Criterion criterion = Restrictions.or(criterion2, criterion3);
			criteria.addOrder(Order.desc("period.minimum"));
			criteria.add(criterion);

		} catch (ParseException e) {
			throw new LeaveCannotProcessException("Invalid date format");
		}
		if (searchCriteriaDTO.getStatus() != null) {

			criteria.add(Restrictions.eq("status",
					searchCriteriaDTO.getStatus()));
		}
		if (searchCriteriaDTO.getMember() != null) {
			criteria.add(Restrictions.eq("emp.employeeId",
					searchCriteriaDTO.getMember()));
		}
		Integer noOfPendingLeaves = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		List<LeaveDebit> leaveDebitsList = criteria.list();
		Map<String, Object> empPendingLeavesMap = new HashMap<String, Object>();
		empPendingLeavesMap.put("leaveDebitList", leaveDebitsList);
		empPendingLeavesMap.put("size", noOfPendingLeaves);

		return empPendingLeavesMap;
	}

	@Override
	public LeaveSettingsLookup getLeaveSettings() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveSettingsLookup.class);

		return (LeaveSettingsLookup) criteria.uniqueResult();

	}

	@Override
	public Double getDebitedLeaves(Employee employee,
			LeaveCategory leaveCategory, DateRange financialPeriod) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId",
				employee.getEmployeeId()));
		criteria.createAlias("leaveCategory", "leaveCategory");
		criteria.add(Restrictions.eq("leaveCategory.name",
				leaveCategory.getName()));
		criteria.add(Restrictions.ge("leaveAppliedOn",
				financialPeriod.getMinimum()));
		criteria.add(Restrictions.lt("leaveAppliedOn",
				financialPeriod.getMaximum()));
		criteria.add(Restrictions.or(
				Restrictions.eq("status", LeaveStatus.PendingApproval),
				Restrictions.eq("status", LeaveStatus.Approved)));
		criteria.setProjection(Projections.sum("numberOfDays"));

		return (Double) criteria.uniqueResult();

	}

	@Override
	public Employee getEmployeeByUserName(String userName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.eq("userName", userName));

		return (Employee) criteria.uniqueResult();
	}

	@Override
	public List<Integer> getAllCreditedYears() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.setProjection(Projections.distinct(Projections
				.property("leaveAppliedOn")));
		criteria.addOrder(Order.desc("leaveAppliedOn"));
		return criteria.list();
	}

	@Override
	public List<LeaveDebit> getLeaveDebitedSet(Employee employee,
			DateRange period) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "emp");
		criteria.add(Restrictions.eq("emp.employeeId", employee.getEmployeeId()));
		criteria.add(Restrictions.ge("leaveAppliedOn", period.getMinimum()));
		criteria.add(Restrictions.le("leaveAppliedOn", period.getMaximum()));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("leaveCategory"));
		projectionList.add(Projections.groupProperty("status"));
		projectionList.add(Projections.sum("numberOfDays"));
		criteria.setProjection(projectionList);
		return criteria.list();

	}

	@Override
	public List<CarryForwardLeave> getAllEmployeeCarryForwardedLeaves(
			DateRange period) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				CarryForwardLeave.class);
		criteria.add(Restrictions.ge("leaveCreditedOn", period.getMinimum()));
		criteria.add(Restrictions.le("leaveCreditedOn", period.getMaximum()));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("employee"));
		projectionList.add(Projections.sum("daysCredited"));

		criteria.setProjection(projectionList);
		return criteria.list();

	}

	@Override
	public List<LeaveDebit> getAllEmployeeLeaveDebits(DateRange period) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.add(Restrictions.ge("leaveAppliedOn", period.getMinimum()));
		criteria.add(Restrictions.le("leaveAppliedOn", period.getMaximum()));
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("employee"));
		projectionList.add(Projections.groupProperty("leaveCategory"));
		projectionList.add(Projections.groupProperty("status"));
		projectionList.add(Projections.sum("numberOfDays"));
		criteria.setProjection(projectionList);
		return criteria.list();
	}

	@Override
	public Double getEmployeeCarryForwardedLeaves(Employee employee,
			DateRange financialPeriod) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				CarryForwardLeave.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId",
				employee.getEmployeeId()));
		criteria.add(Restrictions.ge("leaveCreditedOn",
				financialPeriod.getMinimum()));
		criteria.add(Restrictions.le("leaveCreditedOn",
				financialPeriod.getMaximum()));
		criteria.setProjection(Projections.sum("daysCredited"));

		return (Double) criteria.uniqueResult();
	}

	@Override
	public Double getAllDebitedLeaves(Employee employee,
			DateRange financialPeriod) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId",
				employee.getEmployeeId()));
		criteria.createAlias("leaveCategory", "leaveCategory");
		criteria.add(Restrictions.ne("leaveCategory.leaveType", LeaveType.LOP));
		criteria.add(Restrictions.ge("leaveAppliedOn",
				financialPeriod.getMinimum()));
		criteria.add(Restrictions.le("leaveAppliedOn",
				financialPeriod.getMaximum()));
		criteria.add(Restrictions.or(
				Restrictions.eq("status", LeaveStatus.PendingApproval),
				Restrictions.eq("status", LeaveStatus.Approved)));
		criteria.setProjection(Projections.sum("numberOfDays"));

		return (Double) criteria.uniqueResult();

	}

	@Override
	public Map<String, Object> getAllEmployeePendingLeaves(Integer startIndex,
			Integer endIndex, DateRange monthPeriod) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "emp");
		criteria.setFetchMode("emp", FetchMode.JOIN);
		criteria.add(Restrictions.eq("emp.statusName", "Active"));
		Criterion criterion2 = Restrictions.between("period.minimum",
				monthPeriod.getMinimum(), monthPeriod.getMaximum());
		Criterion criterion3 = Restrictions.between("period.maximum",
				monthPeriod.getMinimum(), monthPeriod.getMaximum());
		Criterion criterion = Restrictions.or(criterion2, criterion3);
		criteria.addOrder(Order.desc("period.minimum"));
		criteria.add(criterion);
		criteria.add(Restrictions.eq("status", LeaveStatus.PendingApproval));
		Integer noOfPendingLeaves = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		List<LeaveDebit> leaveDebitsList = criteria.list();
		Map<String, Object> empPendingLeavesMap = new HashMap<String, Object>();
		empPendingLeavesMap.put("leaveDebitList", leaveDebitsList);
		empPendingLeavesMap.put("size", noOfPendingLeaves);
		return empPendingLeavesMap;
	}

	@Override
	public List<LeaveDebit> getAllEmployeeMonthlyPendingLeaves(
			DateRange monthPeriod) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "emp");
		criteria.setFetchMode("emp", FetchMode.JOIN);
		criteria.add(Restrictions.eq("emp.statusName", "Active"));
		Criterion criterion2 = Restrictions.between("period.minimum",
				monthPeriod.getMinimum(), monthPeriod.getMaximum());
		Criterion criterion3 = Restrictions.between("period.maximum",
				monthPeriod.getMinimum(), monthPeriod.getMaximum());
		Criterion criterion = Restrictions.or(criterion2, criterion3);
		criteria.add(criterion);
		criteria.add(Restrictions.eq("status", LeaveStatus.PendingApproval));

		return criteria.list();
	}

	@Override
	public List<LeaveDebit> getReporteesPendingLeavesOfManager(
			Employee manager, DateRange monthPeriod) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "employee");
		criteria.setFetchMode("employee", FetchMode.JOIN);

		criteria.add(Restrictions.eq("employee.statusName", "Active"));
		criteria.add(Restrictions.eq("employee.manager", manager));

		Criterion criterion2 = Restrictions.between("period.minimum",
				monthPeriod.getMinimum(), monthPeriod.getMaximum());
		Criterion criterion3 = Restrictions.between("period.maximum",
				monthPeriod.getMinimum(), monthPeriod.getMaximum());
		Criterion criterion = Restrictions.or(criterion2, criterion3);

		criteria.add(Restrictions.eq("status", LeaveStatus.PendingApproval));
		criteria.add(criterion);

		return criteria.list();
	}

	@Override
	public List<LeaveDebit> getAllLeaveDebites(DateRange monthPeriod) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);

		Criterion criterion2 = Restrictions.between("period.minimum",
				monthPeriod.getMinimum(), monthPeriod.getMaximum());
		Criterion criterion3 = Restrictions.between("period.maximum",
				monthPeriod.getMinimum(), monthPeriod.getMaximum());
		Criterion criterion = Restrictions.or(criterion2, criterion3);
		criteria.add(criterion);

		return criteria.list();
	}

	@Override
	public Double getDebitedLeavesInNextYear(Employee employee,
			DateRange nextFinancialPeriod) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId",
				employee.getEmployeeId()));
		Criterion criterion2 = Restrictions.between("period.maximum",
				nextFinancialPeriod.getMinimum(),
				nextFinancialPeriod.getMaximum());
		criteria.add(Restrictions.or(
				Restrictions.eq("status", LeaveStatus.PendingApproval),
				Restrictions.eq("status", LeaveStatus.Approved)));
		criteria.add(criterion2);
		criteria.setProjection(Projections.sum("numberOfDays"));

		return (Double) criteria.uniqueResult();
	}

	@Override
	public Map<String, Object> searchEmployeesAsAdmin(Integer startIndex,
			Integer endIndex, SearchCriteriaDTO searchCriteriaDTO) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "emp");
		criteria.add(Restrictions.eq("emp.statusName", "Active"));
		criteria.setFetchMode("emp", FetchMode.JOIN);
		try {
			Criterion criterion2 = Restrictions.between("period.minimum",
					searchCriteriaDTO.getPeriod().getMinimum(),
					searchCriteriaDTO.getPeriod().getMaximum());
			Criterion criterion3 = Restrictions.between("period.maximum",
					searchCriteriaDTO.getPeriod().getMinimum(),
					searchCriteriaDTO.getPeriod().getMaximum());
			Criterion criterion4 = Restrictions.and(Restrictions.le(
					"period.minimum", searchCriteriaDTO.getPeriod()
							.getMinimum()), Restrictions.ge("period.maximum",
					searchCriteriaDTO.getPeriod().getMinimum()));

			Criterion criterion = Restrictions.or(
					Restrictions.or(criterion2, criterion3), criterion4);
			criteria.add(criterion);

		} catch (ParseException e) {
			throw new LeaveCannotProcessException("Invalid date format");

		}
		if (searchCriteriaDTO.getStatus() != null) {

			criteria.add(Restrictions.eq("status",
					searchCriteriaDTO.getStatus()));
		}
		if (searchCriteriaDTO.getMember() != null) {
			criteria.add(Restrictions.eq("emp.employeeId",
					searchCriteriaDTO.getMember()));
		}

		Integer noOfPendingLeaves = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		List<LeaveDebit> leaveDebitsList = criteria.list();
		Map<String, Object> empPendingLeavesMap = new HashMap<String, Object>();
		empPendingLeavesMap.put("leaveDebitList", leaveDebitsList);
		empPendingLeavesMap.put("size", noOfPendingLeaves);
		return empPendingLeavesMap;
	}

	/**
	 * @Override public List<LeaveDebit> searchEmployeesAsAdmin(
	 *           SearchCriteriaDTO searchCriteriaDTO) { method stub Criteria
	 *           criteria = sessionFactory.getCurrentSession().createCriteria(
	 *           LeaveDebit.class); criteria.createAlias("employee", "emp");
	 *           criteria.setFetchMode("emp", FetchMode.JOIN); try { Criterion
	 *           criterion2 = Restrictions.between("period.minimum",
	 *           searchCriteriaDTO.getPeriod().getMinimum(),
	 *           searchCriteriaDTO.getPeriod().getMaximum()); Criterion
	 *           criterion3 = Restrictions.between("period.maximum",
	 *           searchCriteriaDTO.getPeriod().getMinimum(),
	 *           searchCriteriaDTO.getPeriod().getMaximum()); Criterion
	 *           criterion = Restrictions.or(criterion2, criterion3);
	 *           criteria.add(criterion);
	 *
	 *           } catch (ParseException e) { throw new
	 *           LeaveCannotProcessException("Invalid date format");
	 *
	 *           } if (searchCriteriaDTO.getStatus() != null) {
	 *
	 *           criteria.add(Restrictions.eq("status",
	 *           searchCriteriaDTO.getStatus())); } if
	 *           (searchCriteriaDTO.getMember() != null) {
	 *           criteria.add(Restrictions.eq("emp.employeeId",
	 *           searchCriteriaDTO.getMember())); }
	 *
	 *           return criteria.list(); }
	 *
	 */
	@Override
	public Set<LeaveCategory> getEarnedCategories() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveCategory.class);
		criteria.add(Restrictions.eq("leaveType", LeaveType.EARNED));
		return new HashSet<LeaveCategory>(criteria.list());
	}

	@Override
	public List<LeaveDebit> getNextYearLeaveDebits(Employee employee,
			DateRange period) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId",
				employee.getEmployeeId()));
		Criterion criterion3 = Restrictions.between("period.maximum",
				period.getMinimum(), period.getMaximum());
		criteria.createAlias("leaveCategory", "leaveCategory");
		criteria.add(Restrictions.ne("leaveCategory.leaveType", LeaveType.LOP));
		criteria.add(Restrictions.or(
				Restrictions.eq("status", LeaveStatus.PendingApproval),
				Restrictions.eq("status", LeaveStatus.Approved)));
		criteria.add(criterion3);
		return criteria.list();
	}

	@Override
	public SortedSet<LeaveCategory> getlopCategories() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveCategory.class);
		criteria.add(Restrictions.eq("leaveType", LeaveType.LOP));
		return new TreeSet<LeaveCategory>(criteria.list());
	}

	@Override
	public Map<String, Object> getResorces(Integer startIndex,
			Integer endIndex, Long projectMangerId, DateRange period) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		
	
		criteria.createAlias("employee", "emp");
		criteria.setFetchMode("emp", FetchMode.JOIN);
		criteria.createAlias("emp.projectManager", "pm");
		criteria.setFetchMode("pm", FetchMode.JOIN);
		criteria.add(Restrictions.eq("pm.employeeId", projectMangerId));
		criteria.add(Restrictions.eq("status", LeaveStatus.PendingApproval));
		criteria.add(Restrictions.eq("emp.statusName", "Active"));
		Criterion criterion2 = Restrictions.between("period.minimum",
				period.getMinimum(), period.getMaximum());
		Criterion criterion3 = Restrictions.between("period.maximum",
				period.getMinimum(), period.getMaximum());
		Criterion criterion = Restrictions.or(criterion2, criterion3);
		criteria.add(criterion);
		Integer noOfReporteePendingLeaves = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		criteria.addOrder(Order.desc("period.minimum"));
		List<LeaveDebit> leaveDebitsList = criteria.list();
		Map<String, Object> reporteePendingLeavesMap = new HashMap<String, Object>();
		reporteePendingLeavesMap.put("leaveDebitList", leaveDebitsList);
		reporteePendingLeavesMap.put("size", noOfReporteePendingLeaves);
		return reporteePendingLeavesMap;
	}

	@Override
	public Map<String, Object> getResorcesList(Integer startIndex,
			Integer endIndex, Long projectMangerId,
			SearchCriteriaDTO searchCriteriaDTO) {
		
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				LeaveDebit.class);
		criteria.createAlias("employee", "emp");
		criteria.add(Restrictions.eq("emp.statusName", "Active"));
		criteria.setFetchMode("emp", FetchMode.JOIN);
		criteria.createAlias("emp.projectManager", "pm");
		criteria.setFetchMode("pm", FetchMode.JOIN);
		

		/*
		 * criteria.add(Restrictions.eq("mgr.employeeId",
		 * searchCriteriaDTO.getManagerId()));
		 */
		criteria.add(Restrictions.eq("pm.employeeId", projectMangerId));

		try {
			Criterion criterion2 = Restrictions.between("period.minimum",
					searchCriteriaDTO.getPeriod().getMinimum(),
					searchCriteriaDTO.getPeriod().getMaximum());
			Criterion criterion3 = Restrictions.between("period.maximum",
					searchCriteriaDTO.getPeriod().getMinimum(),
					searchCriteriaDTO.getPeriod().getMaximum());
			Criterion criterion = Restrictions.or(criterion2, criterion3);
			criteria.addOrder(Order.desc("period.minimum"));
			criteria.add(criterion);

		} catch (ParseException e) {
			throw new LeaveCannotProcessException("Invalid date format");
		}
		if (searchCriteriaDTO.getStatus() != null) {

			criteria.add(Restrictions.eq("status",
					searchCriteriaDTO.getStatus()));
		}
		if (searchCriteriaDTO.getMember() != null) {
			criteria.add(Restrictions.eq("emp.employeeId",
					searchCriteriaDTO.getMember()));
		}
		Integer noOfPendingLeaves = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		List<LeaveDebit> leaveDebitsList = criteria.list();
		Map<String, Object> empPendingLeavesMap = new HashMap<String, Object>();
		empPendingLeavesMap.put("leaveDebitList", leaveDebitsList);
		empPendingLeavesMap.put("size", noOfPendingLeaves);

		return empPendingLeavesMap;
		
	}

	@Override
	public CarryForwardLeave getCarryForwardLeave(Employee employee,Date creditedOn) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(CarryForwardLeave.class);
		criteria.add(Restrictions.eq("employee", employee));
		criteria.add(Restrictions.eq("leaveCreditedOn",creditedOn));
		return (CarryForwardLeave) criteria.uniqueResult();
	}

	@Override
	public List<ProbationPeriod> getProbationMonths() {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ProbationPeriod.class);
		return criteria.list();
	}
	@Override
	public List<PayrollCutOffDates> getCuttOffDates() {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(PayrollCutOffDates.class);
		return criteria.list();
	}
	
	
	

	
	
	

}
