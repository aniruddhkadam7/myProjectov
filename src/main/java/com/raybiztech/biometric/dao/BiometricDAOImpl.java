package com.raybiztech.biometric.dao;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityReport;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityTime;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.biometric.business.AttendanceStatus;
import com.raybiztech.biometric.business.BioAttendance;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.leavemanagement.dao.LeaveDAOImpl;

@Repository("biometricDAO")
public class BiometricDAOImpl extends LeaveDAOImpl implements BiometricDAO {

	@Autowired
	DAO dao;
	
	Logger logger = Logger.getLogger(BiometricDAOImpl.class);

	@Override
	public Set<BioAttendance> getAttendances(Long employeeId,
			DateRange monthPeriod) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				BioAttendance.class);
		criteria.createAlias("employee", "employee");
		criteria.setFetchMode("employee", FetchMode.JOIN);
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		criteria.add(Restrictions.between("attendanceDate",
				monthPeriod.getMinimum(), monthPeriod.getMaximum()));

		return new HashSet<BioAttendance>(criteria.list());

	}

	@Override
	public SortedSet<BioAttendance> getAllEmployeeAttendances(
			DateRange monthPeriod) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				BioAttendance.class);
		criteria.createAlias("employee", "employee");
		criteria.setFetchMode("employee", FetchMode.JOIN);
		criteria.add(Restrictions.ne("employee.employeeId", Long.valueOf(1000)));
		criteria.addOrder(Order.asc("employee.employeeId"));
		criteria.add(Restrictions.between("attendanceDate",
				monthPeriod.getMinimum(), monthPeriod.getMaximum()));
		return new TreeSet<BioAttendance>(criteria.list());

	}

	@Override
	public List<Employee> getActiveEmployees() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.eq("statusName", "Active"));
		criteria.add(Restrictions.ne("employeeId", Long.valueOf(1000)));
		criteria.addOrder(Order.asc("employeeId"));
		return criteria.list();
	}

	@Override
	public List<Employee> getInActiveEmployees() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.eq("statusName", "InActive"));
		return criteria.list();
	}

	@Override
	public List<EmpoloyeeHiveActivityTime> getMonthlyHiveReportForEmployee(
			String employeeId, DateRange monthPeriod) {
		List<Integer> allMonthDays = new ArrayList<Integer>();
		for (int i = 1; i <= 31; ++i) {
			allMonthDays.add(i);
		}
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		String today = monthPeriod.getMinimum().toString("yyyy-MM-dd");
		String lastday = monthPeriod.getMaximum().toString("yyyy-MM-dd");

		Employee employee = dao.findBy(Employee.class,
				Long.parseLong(employeeId));

		String userName = employee.getUserName();
		EmpoloyeeHiveActivityReport ehvreport = new EmpoloyeeHiveActivityReport();

		ehvreport.setUserName(employee.getUserName());
		ehvreport.setFirstName(employee.getFirstName());
		ehvreport.setLastName(employee.getLastName());

		String hqlQuery = "SELECT sum(e.hours),day(e.date),e.date FROM EmpoloyeeHiveActivity e WHERE  e.date >= ' "
				+ today
				+ " '  and  e.date <= ' "
				+ lastday
				+ " '  \n"
				+ " and e.userName= '"
				+ userName
				+ "'  group by day(e.date) order by day(e.date)";
		Query createQuery = sessionFactory.getCurrentSession().createQuery(
				hqlQuery);
		List<Object[]> result = createQuery.list();

		List<EmpoloyeeHiveActivityTime> ehvactreportList = new ArrayList<EmpoloyeeHiveActivityTime>();
		List<Integer> listOfHiveDays = new ArrayList<Integer>();
		for (Object[] row1 : result) {
			EmpoloyeeHiveActivityTime ehvActivitytm = new EmpoloyeeHiveActivityTime();
			ehvActivitytm.setHours(String.valueOf(Double.parseDouble(df
					.format((Double) row1[0]))));
			ehvActivitytm.setDayofMonth((Integer) row1[1]);
			ehvActivitytm.setpDate((Date) row1[2]);
			listOfHiveDays.add((Integer) row1[1]);
			ehvactreportList.add(ehvActivitytm);
		}

		List<Integer> remainingDays = new ArrayList<Integer>(allMonthDays);
		remainingDays.removeAll(listOfHiveDays);

		for (Integer day : remainingDays) {

			EmpoloyeeHiveActivityTime ehvActivitytm = new EmpoloyeeHiveActivityTime();
			ehvActivitytm.setHours("-");
			ehvActivitytm.setDayofMonth(day);
			ehvActivitytm.setpDate(null);
			ehvactreportList.add(ehvActivitytm);
		}

		ehvreport.setActivityTimes(ehvactreportList);

		return ehvactreportList;
	}

	@Override
	public Map<String, Object> getStatusPaginatedEmployees(Integer startIndex,
			Integer endIndex, String status, String shiftid, String search) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);

		if (status.equalsIgnoreCase("Active")) {
			criteria.add(Restrictions.eq("statusName", "Active"));
		}
		if (status.equalsIgnoreCase("InActive")) {
			criteria.add(Restrictions.eq("statusName", "InActive"));
		}
		if (status.equalsIgnoreCase("underNotice")) {
			criteria.add(Restrictions.eq("statusName", "Active"));
			criteria.add(Restrictions.eq("underNotice", Boolean.TRUE));
		}
		/*
		 * criteria.add(Restrictions.or(Restrictions.eq("statusName", "Active"),
		 * Restrictions.eq("statusName", "underNotice")));
		 */
		criteria.add(Restrictions.ne("employeeId", Long.valueOf(1000)));
		criteria.addOrder(Order.asc("employeeId"));
		if (!shiftid.isEmpty()) {
			criteria.createAlias("timeSlot", "timeSlot");
			criteria.add(Restrictions.eq("timeSlot.id", Long.parseLong(shiftid)));
		}
		if (!search.isEmpty()) {
			criteria.add(Restrictions.ilike("employeeFullName", search,
					MatchMode.ANYWHERE));
		}
		Integer noOfActiveEmployees = criteria.list().size();

		if (startIndex != null && endIndex != null) {
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(endIndex - startIndex);
		}

		List<Employee> employeeList = criteria.list();
		Map<String, Object> activeEmployeeMap = new HashMap<String, Object>();
		activeEmployeeMap.put("employeeList", employeeList);
		activeEmployeeMap.put("size", noOfActiveEmployees);
		return activeEmployeeMap;
	}

	@Override
	public Map<String, Object> getPaginatedInActiveEmployees(
			Integer startIndex, Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.eq("statusName", "InActive"));
		criteria.addOrder(Order.asc("employeeId"));
		Integer noOfInActiveEmp = criteria.list().size();
		criteria.setMaxResults(startIndex);
		criteria.setFirstResult(endIndex - startIndex);
		List<Employee> inActiveEmpList = criteria.list();
		Map<String, Object> inActiveEmpMap = new HashMap<String, Object>();
		inActiveEmpMap.put("employeeList", inActiveEmpList);
		inActiveEmpMap.put("size", noOfInActiveEmp);
		return inActiveEmpMap;
	}

	@Override
	public Integer getTotalActiveEmployees() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.eq("statusName", "Active"));
		criteria.add(Restrictions.ne("employeeId", 1000L));
		criteria.setProjection(Projections.countDistinct("employeeId"));
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	public Integer getTotalInActiveEmployees() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.eq("statusName", "InActive"));
		criteria.setProjection(Projections.countDistinct("employeeId"));
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	public Map<String, Object> searchEmployees(String search,
			Integer startIndex, Integer endIndex, String status,String shiftid) {
		Query query = null;
	
		Long shiftids=null;
		if(!shiftid.isEmpty())
		{
			
			 shiftids=Long.parseLong(shiftid);
		}
		

		try {
			if(!shiftid.isEmpty())
			{
			
				Long empId = Long.parseLong(search);
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from Employee emp where emp.employeeId=:employeeId AND emp.statusName =:status AND  emp.timeSlot=:shiftids AND emp.employeeId!=1000L order by emp.employeeId");
				if (status.equalsIgnoreCase("underNotice")) {
					query.setString("status", "Active");

				} else {
					query.setString("status", status);
				}

				// query = sessionFactory
				// .getCurrentSession()
				// .createQuery(
				// "from Employee emp where emp.employeeId=:employeeId AND (emp.statusName = 'Active' OR emp.statusName = 'underNotice') AND emp.employeeId!=1000L order by emp.employeeId");
				//
				// query.setString("status", status);
				query.setLong("employeeId", empId);
				query.setLong("shiftids", shiftids);
				
			}
			else
			{
				
				Long empId = Long.parseLong(search);
				
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from Employee emp where emp.employeeId=:employeeId AND emp.statusName =:status  AND emp.employeeId!=1000L order by emp.employeeId");
				if (status.equalsIgnoreCase("underNotice")) {
					query.setString("status", "Active");

				} else {
					query.setString("status", status);
				}

				// query = sessionFactory
				// .getCurrentSession()
				// .createQuery(
				// "from Employee emp where emp.employeeId=:employeeId AND (emp.statusName = 'Active' OR emp.statusName = 'underNotice') AND emp.employeeId!=1000L order by emp.employeeId");
				//
				// query.setString("status", status);
				query.setLong("employeeId", empId);
			}
			

		} catch (NumberFormatException nfe) {
			System.out.println("In catch");

			if(!shiftid.isEmpty() )
			{
				
				
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from Employee emp where concat(emp.firstName, ' ', emp.lastName) like :fullName and emp.statusName =:status and  emp.timeSlot=:shiftids and emp.employeeId!=1000L order by emp.employeeId");
			}
			

			else
			{
				
				
				query = sessionFactory
						.getCurrentSession()
						.createQuery(
								"from Employee emp where concat(emp.firstName, ' ', emp.lastName) like :fullName and emp.statusName =:status  and emp.employeeId!=1000L order by emp.employeeId");
			}
			if (status.equalsIgnoreCase("underNotice")) {
				query.setString("status", "Active");

			} else {
				query.setString("status", status);
			}
			// query.setString("status", status);
			query.setString("fullName", "%" + search + "%");
			if(!shiftid.isEmpty())
			{
				query.setLong("shiftids", shiftids);
			}
			
		
		}
		/*
		 * Criteria criteria =
		 * sessionFactory.getCurrentSession().createCriteria( Employee.class);
		 * criteria.add(Restrictions.eq("statusName", status)); Criterion
		 * criterion = Restrictions.like("firstName", search,
		 * MatchMode.ANYWHERE); Criterion criterion2 =
		 * Restrictions.like("lastName", search, MatchMode.ANYWHERE);
		 * criteria.add(Restrictions.ne("employeeId", 1000L));
		 * criteria.addOrder(Order.asc("employeeId"));
		 * criteria.add(Restrictions.or(criterion, criterion2));
		 */
		Integer noOfSearchedEmp = query.list().size();
		if (startIndex != null && endIndex != null) {
		query.setFirstResult(startIndex);
		query.setMaxResults(endIndex - startIndex);
		}
		List<Employee> searchedEmpList = query.list();
		Map<String, Object> searchEmpMap = new HashMap<String, Object>();
		searchEmpMap.put("searchEmpList", searchedEmpList);
		searchEmpMap.put("size", noOfSearchedEmp);
		return searchEmpMap;
	}

	// @Override
	// public List<Employee> searchReportees(Long employeeId, String search,
	// Integer index, String status) {
	// Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
	// Employee.class);
	// criteria.add(Restrictions.eq("statusName", status));
	// Criterion criterion = Restrictions.like("firstName", search,
	// MatchMode.ANYWHERE);
	// Criterion criterion2 = Restrictions.like("lastName", search,
	// MatchMode.ANYWHERE);
	// criteria.createAlias("manager", "manager");
	// criteria.add(Restrictions.eq("manager.employeeId", employeeId));
	// criteria.add(Restrictions.ne("employeeId", Long.valueOf(1000)));
	// criteria.addOrder(Order.asc("employeeId"));
	// criteria.add(Restrictions.or(criterion, criterion2));
	// criteria.setFirstResult(index * 20);
	// criteria.setMaxResults(20);
	// return criteria.list();
	// }
	@Override
	public Map<String, Object> searchReportees(List<Long> employeeId,
			String search, Integer startIndex, Integer endIndex, String status,String shiftid) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.or(Restrictions.eq("statusName", "Active"),
				Restrictions.eq("statusName", "underNotice")));
		// Criterion criterion = Restrictions.like("firstName", search,
		// MatchMode.ANYWHERE);
		// Criterion criterion2 = Restrictions.like("lastName", search,
		// MatchMode.ANYWHERE);
		criteria.createAlias("manager", "manager");
		criteria.add(Restrictions.or(
				Restrictions.in("manager.employeeId", employeeId),
				Restrictions.in("employeeId", employeeId)));
		criteria.add(Restrictions.ne("employeeId", Long.valueOf(1000)));
		criteria.addOrder(Order.asc("employeeId"));
		// criteria.add(Restrictions.or(criterion, criterion2));
		criteria.add(Restrictions.ilike("employeeFullName", search,
				MatchMode.ANYWHERE));
		if (!shiftid.isEmpty()) {
			criteria.createAlias("timeSlot", "timeSlot");
			criteria.add(Restrictions.eq("timeSlot.id", Long.parseLong(shiftid)));
		}
		Integer noOfSearchedEmp = criteria.list().size();
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(startIndex - endIndex);
		List<Employee> searchEmpList = criteria.list();
		Map<String, Object> searchReporteeMap = new HashMap<String, Object>();
		searchReporteeMap.put("searchEmpList", searchEmpList);
		searchReporteeMap.put("size", noOfSearchedEmp);
		return searchReporteeMap;
	}

	@Override
	public Integer totalSearchEmployees(String search, String status) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.eq("statusName", status));
		Criterion criterion = Restrictions.like("firstName", search,
				MatchMode.ANYWHERE);
		Criterion criterion2 = Restrictions.like("lastName", search,
				MatchMode.ANYWHERE);
		criteria.add(Restrictions.ne("employeeId", 1000L));
		criteria.add(Restrictions.or(criterion, criterion2));
		criteria.setProjection(Projections.countDistinct("employeeId"));
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	public Integer totalSearchReportees(Long employeeId, String search,
			String status) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.eq("statusName", status));
		Criterion criterion = Restrictions.like("firstName", search,
				MatchMode.ANYWHERE);
		Criterion criterion2 = Restrictions.like("lastName", search,
				MatchMode.ANYWHERE);
		criteria.createAlias("manager", "manager");
		criteria.add(Restrictions.eq("manager.employeeId", employeeId));
		criteria.add(Restrictions.ne("employeeId", Long.valueOf(1000)));
		criteria.add(Restrictions.or(criterion, criterion2));
		criteria.setProjection(Projections.countDistinct("employeeId"));
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	public Integer getTotalActiveReportees(Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.eq("statusName", "Active"));
		criteria.createAlias("manager", "manager");
		criteria.add(Restrictions.eq("manager.employeeId", employeeId));
		criteria.add(Restrictions.ne("employeeId", Long.valueOf(1000)));
		criteria.setProjection(Projections.countDistinct("employeeId"));
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	public Integer getTotalInActiveReportees(Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.eq("statusName", "InActive"));
		criteria.createAlias("manager", "manager");
		criteria.add(Restrictions.eq("manager.employeeId", employeeId));
		criteria.setProjection(Projections.countDistinct("employeeId"));
		return ((Number) criteria.uniqueResult()).intValue();
	}

	@Override
	public List<Employee> getPaginatedReportees(Long employeeId, Integer index,
			String status) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.add(Restrictions.eq("statusName", status));
		criteria.createAlias("manager", "manager");
		Criterion criterion = Restrictions.eq("manager.employeeId", employeeId);
		Criterion criterion2 = Restrictions.eq("employeeId", employeeId);
		criteria.add(Restrictions.or(criterion, criterion2));
		criteria.add(Restrictions.ne("employeeId", Long.valueOf(1000)));
		criteria.addOrder(Order.asc("employeeId"));
		criteria.setFirstResult(index * 20);
		criteria.setMaxResults(20);
		return criteria.list();
	}

	@Override
	public Map<String, Object> getPaginatedReporteesForAttendance(
			List<Long> employeeId, Integer startIndex, Integer endIndex,
			String status, String shiftid, String search) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		if (status.equalsIgnoreCase("undernotice")) {
			criteria.add(Restrictions.eq("statusName", "Active"));
			criteria.add(Restrictions.eq("underNotice", Boolean.TRUE));
		} else {
			criteria.add(Restrictions.eq("statusName", status));
		}
		if (!shiftid.isEmpty()) {
			criteria.createAlias("timeSlot", "timeSlot");
			criteria.add(Restrictions.eq("timeSlot.id", Long.parseLong(shiftid)));
		}
		criteria.createAlias("manager", "manager");
		criteria.add(Restrictions.or(
				Restrictions.in("manager.employeeId", employeeId),
				Restrictions.in("employeeId", employeeId)));

		// criteria.add(Restrictions.in("employeeId", employeeId));
		// Criterion criterion = Restrictions.in("manager.employeeId",
		// employeeId);
		// Criterion criterion2 = Restrictions.in("employeeId", employeeId);
		// criteria.add(Restrictions.or(criterion, criterion2));

		if (!search.isEmpty()) {
			criteria.add(Restrictions.ilike("employeeFullName", search,
					MatchMode.ANYWHERE));
		}

		criteria.add(Restrictions.ne("manager.employeeId", Long.valueOf(1000)));
		criteria.addOrder(Order.asc("employeeId"));
		Integer noOfEmployees = criteria.list().size();

		if (startIndex != null && endIndex != null) {
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(endIndex - startIndex);
		}

		List<Employee> empList = criteria.list();

		Map<String, Object> reporteesMap = new HashMap<String, Object>();
		reporteesMap.put("employeeList", empList);
		reporteesMap.put("size", noOfEmployees);
		return reporteesMap;

	}

	@Override
	public List<BioAttendance> updateLateReporting() {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(BioAttendance.class)
				.add(Restrictions.eq("attendanceStatus", AttendanceStatus.P))
				.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.statusName", "Active"));
		criteria.add(Restrictions.isNull("lateReport"));
		try {
			Date fromdate = DateParser.toDate("01/02/2016");
			// Date todate = DateParser.toDate("25/10/2016");
			criteria.add(Restrictions.gt("attendanceDate", fromdate));
			// .add(Restrictions.ge("employee.employeeId", 1450L));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return criteria.list();
	}

	@Override
	public List<BioAttendance> getLateComingAttendance(DateRange monthPeriod,List<Date> holidays) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(BioAttendance.class);
		criteria.createAlias("employee", "employee");
		Criterion dateRange = Restrictions.between("attendanceDate",
				monthPeriod.getMinimum(), monthPeriod.getMaximum());
		if(holidays.size() > 0) {
			Criterion holidaysCriterion =Restrictions.not(Restrictions.in("attendanceDate", holidays));
			criteria.add(Restrictions.and(holidaysCriterion, dateRange));
		}
		else {
			criteria.add(dateRange);
		}
		criteria.add(Restrictions.eq("employee.statusName", "Active"));
		criteria.add(Restrictions.isNotNull("lateReport"));
		criteria.add(Restrictions.eq("lateReport", Boolean.TRUE));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	
}
