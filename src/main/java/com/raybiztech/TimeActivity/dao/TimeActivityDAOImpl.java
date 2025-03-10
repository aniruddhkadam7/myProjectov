/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeActivity.dao;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.TimeActivity.business.EmpoloyeeHiveActivity;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityReport;
import com.raybiztech.TimeActivity.dto.EmpoloyeeHiveActivityTime;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.MonthYear;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.business.LeaveStatus;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectStatus;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.recruitment.business.Holidays;
import com.raybiztech.rolefeature.business.Permission;

/**
 * 
 * @author naresh
 */
@Component("timeActivityDAO")
public class TimeActivityDAOImpl extends DAOImpl implements TimeActivityDAO {

	private static final Logger logger = Logger
			.getLogger(TimeActivityDAOImpl.class);
	@Autowired
	LeaveManagementUtils leaveManagementUtils;
	@Autowired
	DAO dao;
	@Autowired
	ProjectService projectService;

	@Override
	public List<EmpoloyeeHiveActivity> getEmployeeHiveTimeActivities(
			String userName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				EmpoloyeeHiveActivity.class);
		criteria.add(Restrictions.eq("userName", userName));
		criteria.add(Restrictions.eq("date", new Date().previous()));
		return criteria.list();
	}

	@Override
	public List<EmpoloyeeHiveActivity> getEmployeeDayHiveSheet(
			String projectDate, String userName) {
		Date projDate = null;
		try {
			projDate = Date.parse(projectDate, "dd MMM yyyy");
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(
					TimeActivityDAOImpl.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				EmpoloyeeHiveActivity.class);
		criteria.add(Restrictions.eq("userName", userName));
		criteria.add(Restrictions.eq("date", projDate));
		return criteria.list();
	}

	@Override
	public Map<String, Object> getMonthlyHiveReportForManager(String hiveDate,
			Employee employee, Integer startIndex, Integer endIndex) {
		List<Integer> allMonthDays = new ArrayList<Integer>();
		for (int i = 1; i <= 31; ++i) {
			allMonthDays.add(i);
		}

		DecimalFormat df = new DecimalFormat("00.00");
		df.setMaximumFractionDigits(2);

		String h[] = hiveDate.split("/");

		MonthOfYear monthOfYear = MonthOfYear.valueOf(Integer.parseInt(h[0]));
		YearOfEra yearOfEra = YearOfEra.valueOf(Integer.parseInt(h[1]));

		DateRange monthPeriod = leaveManagementUtils
				.getConstructMonthPeriod(new MonthYear(monthOfYear, yearOfEra));

		String today = monthPeriod.getMinimum().toString("yyyy-MM-dd");
		String lastday = monthPeriod.getMaximum().toString("yyyy-MM-dd");

		String employeehqlQuery = " ";
		Query employeecreateQuery = null;

		Integer noOfRecords = null;
		Permission hiveActivityReportAll = dao.checkForPermission(
				"Hive Activity Report All", employee);
		Permission hierarchyHiveActivityReport = dao.checkForPermission(
				"Hierarchy Hive Activity Report", employee);
		if (hiveActivityReportAll.getView()
				&& !hierarchyHiveActivityReport.getView()) {
			employeehqlQuery = "SELECT userName,employeeId,firstName,lastName From Employee e where e.employeeId!=:Id and (e.statusName = 'Active' OR e.statusName = 'underNotice') ORDER BY e.employeeId ASC";

			employeecreateQuery = sessionFactory.getCurrentSession()
					.createQuery(employeehqlQuery);
			employeecreateQuery.setParameter("Id", Long.valueOf(1000));
			// employeecreateQuery.setParameter("Active", "Active");
			noOfRecords = employeecreateQuery.list().size();
			if (startIndex != null && endIndex != null) {
				employeecreateQuery.setFirstResult(startIndex);
				employeecreateQuery.setMaxResults(endIndex - startIndex);
			}

		} else {
			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());

			employeehqlQuery = "SELECT userName,employeeId,firstName,lastName From Employee e where (e.manager.employeeId IN(:Id) or e.employeeId IN(:Id)) and (e.statusName = 'Active' OR e.statusName = 'underNotice') ORDER BY e.employeeId ASC";
			employeecreateQuery = sessionFactory.getCurrentSession()
					.createQuery(employeehqlQuery);
			employeecreateQuery.setParameterList("Id", managerIds);
			// employeecreateQuery.setParameter("Id", employee.getEmployeeId());
			// employeecreateQuery.setParameter("Active", "Active");
			noOfRecords = employeecreateQuery.list().size();
			if (startIndex != null && endIndex != null) {
				employeecreateQuery.setFirstResult(startIndex);
				employeecreateQuery.setMaxResults(endIndex - startIndex);
			}
		}

		List<Object[]> employeeresult = employeecreateQuery.list();

		List<EmpoloyeeHiveActivityReport> ehvreportList = new ArrayList<EmpoloyeeHiveActivityReport>();

		for (Object[] employeeName : employeeresult) {
			String userName = (String) employeeName[0];
			EmpoloyeeHiveActivityReport ehvreport = new EmpoloyeeHiveActivityReport();

			ehvreport.setUserName((String) employeeName[0]);
			ehvreport.setId((Long) employeeName[1]);
			ehvreport.setFirstName((String) employeeName[2]);
			ehvreport.setLastName((String) employeeName[3]);

			String hqlQuery = "SELECT sum(e.hours),day(e.date),e.date,e.empId FROM EmpoloyeeHiveActivity e WHERE  e.date >= ' "
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
			Double totalTime = 0.00;
			double totalroundofftime = 0.00;
			for (Object[] row1 : result) {
				EmpoloyeeHiveActivityTime ehvActivitytm = new EmpoloyeeHiveActivityTime();
				/*
				 * String time = String.valueOf(row1[0]); if (!time.equals("0"))
				 * { time = time.format("%05.2f", Double.valueOf(time)); }
				 */

				ehvActivitytm.setHours(String.valueOf(Double.parseDouble(df
						.format((Double) row1[0]))));
				totalTime = totalTime
						+ Double.parseDouble(df.format((Double) row1[0]));
				totalroundofftime = Math.round(totalTime * 100.0) / 100.0;

				// ehvActivitytm.setHours(time);
				ehvActivitytm.setDayofMonth((Integer) row1[1]);
				ehvActivitytm.setProjectDate(String.valueOf(row1[2]));
				listOfHiveDays.add((Integer) row1[1]);
				ehvactreportList.add(ehvActivitytm);
			}

			List<Integer> remainingDays = new ArrayList<Integer>(allMonthDays);
			remainingDays.removeAll(listOfHiveDays);

			for (Integer day : remainingDays) {

				EmpoloyeeHiveActivityTime ehvActivitytm = new EmpoloyeeHiveActivityTime();
				ehvActivitytm.setHours("-");
				ehvActivitytm.setDayofMonth(day);
				ehvActivitytm.setProjectDate(null);
				ehvreport.setTotalHiveTime(String.valueOf(totalroundofftime));
				ehvactreportList.add(ehvActivitytm);
			}

			ehvreport.setActivityTimes(ehvactreportList);

			ehvreportList.add(ehvreport);

		}
		Map<String, Object> hivereportMap = new HashMap<String, Object>();
		hivereportMap.put("list", ehvreportList);
		hivereportMap.put("size", noOfRecords);

		return hivereportMap;
	}

	@Override
	public Map<String, Object> projectTimeSheet(String hiveDate,
			List<Long> empIds, String hiveProjectName) {

		List<Integer> allMonthDays = new ArrayList<Integer>();
		for (int i = 1; i <= 31; ++i) {
			allMonthDays.add(i);
		}

		DecimalFormat df = new DecimalFormat("00.00");
		df.setMaximumFractionDigits(2);

		String h[] = hiveDate.split("/");

		MonthOfYear monthOfYear = MonthOfYear.valueOf(Integer.parseInt(h[0]));
		YearOfEra yearOfEra = YearOfEra.valueOf(Integer.parseInt(h[1]));

		DateRange monthPeriod = leaveManagementUtils
				.getConstructMonthPeriod(new MonthYear(monthOfYear, yearOfEra));

		String today = monthPeriod.getMinimum().toString("yyyy-MM-dd");
		String lastday = monthPeriod.getMaximum().toString("yyyy-MM-dd");

		String employeehqlQuery = " ";
		Query employeecreateQuery = null;

		employeehqlQuery = "SELECT userName,employeeId,firstName,lastName From Employee e where (e.employeeId IN(:Id)) and (e.statusName = 'Active') ORDER BY e.employeeId ASC";
		employeecreateQuery = sessionFactory.getCurrentSession().createQuery(
				employeehqlQuery);
		employeecreateQuery.setParameterList("Id", empIds);

		List<Object[]> employeeresult = employeecreateQuery.list();
		Integer noOfRecords = employeeresult.size();

		List<EmpoloyeeHiveActivityReport> ehvreportList = new ArrayList<EmpoloyeeHiveActivityReport>();

		for (Object[] employeeName : employeeresult) {
			String userName = (String) employeeName[0];
			EmpoloyeeHiveActivityReport ehvreport = new EmpoloyeeHiveActivityReport();

			ehvreport.setUserName((String) employeeName[0]);
			ehvreport.setId((Long) employeeName[1]);
			ehvreport.setFirstName((String) employeeName[2]);
			ehvreport.setLastName((String) employeeName[3]);

			String hqlQuery = "SELECT sum(e.hours),day(e.date),e.date,e.empId,e.sprintName,e.taskId,e.startDate,e.endDate,e.projectIdentifier FROM EmpoloyeeHiveActivity e WHERE  e.date >= ' "
					+ today
					+ " '  and  e.date <= ' "
					+ lastday
					+ " '  \n"
					+ " and e.userName= '"
					+ userName
					+ "' and e.projectName= '"
					+ hiveProjectName
					+ "' group by day(e.date) order by day(e.date)";
			Query createQuery = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			List<Object[]> result = createQuery.list();

			List<EmpoloyeeHiveActivityTime> ehvactreportList = new ArrayList<EmpoloyeeHiveActivityTime>();
			List<Integer> listOfHiveDays = new ArrayList<Integer>();
			Double totalTime = 0.00;
			double totalroundofftime = 0.00;
			for (Object[] row1 : result) {
				EmpoloyeeHiveActivityTime ehvActivitytm = new EmpoloyeeHiveActivityTime();
				/*
				 * String time = String.valueOf(row1[0]); if (!time.equals("0"))
				 * { time = time.format("%05.2f", Double.valueOf(time)); }
				 */
				ehvreport.setProjectIdentifier(String.valueOf(row1[8]));
				ehvActivitytm.setHours(String.valueOf(Double.parseDouble(df
						.format((Double) row1[0]))));
				totalTime = totalTime
						+ Double.parseDouble(df.format((Double) row1[0]));
				totalroundofftime = Math.round(totalTime * 100.0) / 100.0;

				// ehvActivitytm.setHours(time);
				ehvActivitytm.setDayofMonth((Integer) row1[1]);
				ehvActivitytm.setProjectDate(String.valueOf(row1[2]));
				ehvActivitytm.setSprintName(String.valueOf(row1[4]));
				ehvActivitytm.setTaskId((Integer) row1[5]);
				ehvActivitytm.setStartDate(String.valueOf(row1[6]));
				ehvActivitytm.setEndDate(String.valueOf(row1[7]));
				listOfHiveDays.add((Integer) row1[1]);
				ehvactreportList.add(ehvActivitytm);
			}

			List<Integer> remainingDays = new ArrayList<Integer>(allMonthDays);
			remainingDays.removeAll(listOfHiveDays);

			for (Integer day : remainingDays) {

				EmpoloyeeHiveActivityTime ehvActivitytm = new EmpoloyeeHiveActivityTime();
				ehvActivitytm.setHours("-");
				ehvActivitytm.setDayofMonth(day);
				ehvActivitytm.setProjectDate(null);
				ehvreport.setTotalHiveTime(String.valueOf(totalroundofftime));
				ehvactreportList.add(ehvActivitytm);
			}

			ehvreport.setActivityTimes(ehvactreportList);

			ehvreportList.add(ehvreport);

		}
		Map<String, Object> hivereportMap = new HashMap<String, Object>();
		hivereportMap.put("list", ehvreportList);
		hivereportMap.put("size", noOfRecords);

		return hivereportMap;

	}

	@Override
	public EmpoloyeeHiveActivityReport getMonthlyHiveReportForEmployee(
			String hiveDate, String loggedInEmpId) {
		List<Integer> allMonthDays = new ArrayList<Integer>();
		for (int i = 1; i <= 31; ++i) {
			allMonthDays.add(i);
		}

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		String h[] = hiveDate.split("/");

		MonthOfYear monthOfYear = MonthOfYear.valueOf(Integer.parseInt(h[0]));
		YearOfEra yearOfEra = YearOfEra.valueOf(Integer.parseInt(h[1]));

		DateRange monthPeriod = leaveManagementUtils
				.getMonthPeriod(new MonthYear(monthOfYear, yearOfEra));

		String today = monthPeriod.getMinimum().toString("yyyy-MM-dd");
		String lastday = monthPeriod.getMaximum().toString("yyyy-MM-dd");

		Employee employee = dao.findBy(Employee.class,
				Long.parseLong(loggedInEmpId));

		String userName = employee.getUserName();
		EmpoloyeeHiveActivityReport ehvreport = new EmpoloyeeHiveActivityReport();

		ehvreport.setId(employee.getEmployeeId());
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
		Double totalHiveTime = 0.0;
		Double totalroundofftime = 0.00;
		List<EmpoloyeeHiveActivityTime> ehvactreportList = new ArrayList<EmpoloyeeHiveActivityTime>();
		List<Integer> listOfHiveDays = new ArrayList<Integer>();
		for (Object[] row1 : result) {
			EmpoloyeeHiveActivityTime ehvActivitytm = new EmpoloyeeHiveActivityTime();
			/*
			 * String time = String.valueOf(row1[0]); if (!time.equals("0")) {
			 * time = time.format("%05.2f", Double.valueOf(time)); }
			 */

			ehvActivitytm.setHours(String.valueOf(Double.parseDouble(df
					.format((Double) row1[0]))));
			totalHiveTime = totalHiveTime
					+ Double.parseDouble(df.format((Double) row1[0]));
			totalroundofftime = Math.round(totalHiveTime * 100.0) / 100.0;
			// ehvActivitytm.setHours(time);
			ehvActivitytm.setDayofMonth((Integer) row1[1]);
			ehvActivitytm.setProjectDate(String.valueOf(row1[2]));
			listOfHiveDays.add((Integer) row1[1]);
			ehvactreportList.add(ehvActivitytm);
		}

		List<Integer> remainingDays = new ArrayList<Integer>(allMonthDays);
		remainingDays.removeAll(listOfHiveDays);

		for (Integer day : remainingDays) {

			EmpoloyeeHiveActivityTime ehvActivitytm = new EmpoloyeeHiveActivityTime();
			ehvActivitytm.setHours("-");
			ehvActivitytm.setDayofMonth(day);
			ehvActivitytm.setProjectDate(null);
			ehvactreportList.add(ehvActivitytm);
		}
		ehvreport.setTotalHiveTime(String.valueOf(totalroundofftime));
		ehvreport.setActivityTimes(ehvactreportList);

		return ehvreport;
	}

	@Override
	public Map<String, Object> searchHiveTime(String hiveDate,
			Employee employee, Integer startIndex, Integer endIndex,
			String search) {

		List<Integer> allMonthDays = new ArrayList<Integer>();
		for (int i = 1; i <= 31; ++i) {
			allMonthDays.add(i);
		}

		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);

		String h[] = hiveDate.split("/");

		MonthOfYear monthOfYear = MonthOfYear.valueOf(Integer.parseInt(h[0]));
		YearOfEra yearOfEra = YearOfEra.valueOf(Integer.parseInt(h[1]));

		DateRange monthPeriod = leaveManagementUtils
				.getConstructMonthPeriod(new MonthYear(monthOfYear, yearOfEra));

		String today = monthPeriod.getMinimum().toString("yyyy-MM-dd");
		String lastday = monthPeriod.getMaximum().toString("yyyy-MM-dd");

		String employeehqlQuery = " ";
		Query employeecreateQuery = null;
		Integer noOfRecords = null;
		Permission hiveActivityReportAll = dao.checkForPermission(
				"Hive Activity Report All", employee);
		Permission hierarchyHiveActivityReport = dao.checkForPermission(
				"Hierarchy Hive Activity Report", employee);
		if (hiveActivityReportAll.getView()
				&& !hierarchyHiveActivityReport.getView()) {
			// if ("admin".equalsIgnoreCase(employee.getRole())) {

			try {
				Long empid = Long.valueOf(search);
				employeehqlQuery = "SELECT  userName,employeeId,firstName,lastName From Employee e where"
						+ " (e.employeeId like "
						+ empid
						+ ")"
						+ "and (e.statusName = 'Active' OR e.statusName = 'underNotice') ORDER BY e.employeeId ASC";
			} catch (NumberFormatException nfe) {

				employeehqlQuery = "SELECT  userName,employeeId,firstName,lastName From Employee e where concat(e.firstName, ' ', e.lastName) like '%"
						+ search
						+ "%' and (e.statusName = 'Active' OR e.statusName = 'underNotice') ORDER BY e.employeeId ASC";
			}
			employeecreateQuery = sessionFactory.getCurrentSession()
					.createQuery(employeehqlQuery);

			// employeecreateQuery.setParameter("Active", "Active");
			noOfRecords = employeecreateQuery.list().size();
			employeecreateQuery.setFirstResult(startIndex);
			employeecreateQuery.setMaxResults(endIndex - startIndex);

		} else {
			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());
			try {
				Long empid = Long.valueOf(search);
				employeehqlQuery = "SELECT  userName,employeeId,firstName,lastName From Employee e where  ((e.manager.employeeId IN(:Id) and (e.statusName = 'Active' OR e.statusName = 'underNotice'))) and e.employeeId!=1000 and (e.employeeId like '%"
						+ empid + "%') ORDER BY e.employeeId ASC";
			} catch (NumberFormatException nfe) {

				employeehqlQuery = "SELECT  userName,employeeId,firstName,lastName From Employee e where e.manager.employeeId IN(:Id) and concat(e.firstName, ' ', e.lastName) like '%"
						+ search
						+ "%' and (e.statusName = 'Active' OR e.statusName = 'underNotice') ORDER BY e.employeeId ASC";
			}
			employeecreateQuery = sessionFactory.getCurrentSession()
					.createQuery(employeehqlQuery);
			employeecreateQuery.setParameterList("Id", managerIds);
			// employeecreateQuery.setParameter("Id", employee.getEmployeeId());
			// employeecreateQuery.setParameter("Active", "Active");
			noOfRecords = employeecreateQuery.list().size();
			employeecreateQuery.setFirstResult(startIndex);
			employeecreateQuery.setMaxResults(endIndex - startIndex);
		}

		List<Object[]> employeeresult = employeecreateQuery.list();

		List<EmpoloyeeHiveActivityReport> ehvreportList = new ArrayList<EmpoloyeeHiveActivityReport>();

		for (Object[] employeeName : employeeresult) {
			String userName = (String) employeeName[0];
			EmpoloyeeHiveActivityReport ehvreport = new EmpoloyeeHiveActivityReport();

			ehvreport.setUserName((String) employeeName[0]);
			ehvreport.setId((Long) employeeName[1]);
			ehvreport.setFirstName((String) employeeName[2]);
			ehvreport.setLastName((String) employeeName[3]);

			String hqlQuery = "SELECT sum(e.hours),day(e.date),e.date,e.empId FROM EmpoloyeeHiveActivity e WHERE  e.date >= ' "
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
			Double totalTime = 0.00;
			Double totalroundofftime = 0.00;
			List<EmpoloyeeHiveActivityTime> ehvactreportList = new ArrayList<EmpoloyeeHiveActivityTime>();
			List<Integer> listOfHiveDays = new ArrayList<Integer>();
			for (Object[] row1 : result) {
				EmpoloyeeHiveActivityTime ehvActivitytm = new EmpoloyeeHiveActivityTime();

				/*
				 * String time = String.valueOf(row1[0]); if (!time.equals("0"))
				 * { time = time.format("%05.2f", Double.valueOf(time)); }
				 */

				ehvActivitytm.setHours(String.valueOf(Double.parseDouble(df
						.format((Double) row1[0]))));
				totalTime = totalTime
						+ Double.parseDouble(df.format((Double) row1[0]));
				totalroundofftime = Math.round(totalTime * 100.0) / 100.0;
				// ehvActivitytm.setHours(time);
				ehvActivitytm.setDayofMonth((Integer) row1[1]);
				ehvActivitytm.setProjectDate(String.valueOf(row1[2]));
				ehvActivitytm.setEmpId((Long) row1[3]);
				listOfHiveDays.add((Integer) row1[1]);
				ehvactreportList.add(ehvActivitytm);
			}

			List<Integer> remainingDays = new ArrayList<Integer>(allMonthDays);
			remainingDays.removeAll(listOfHiveDays);

			for (Integer day : remainingDays) {

				EmpoloyeeHiveActivityTime ehvActivitytm = new EmpoloyeeHiveActivityTime();
				ehvActivitytm.setHours("-");
				ehvActivitytm.setDayofMonth(day);
				ehvActivitytm.setProjectDate(null);
				ehvactreportList.add(ehvActivitytm);
			}

			ehvreport.setActivityTimes(ehvactreportList);
			ehvreport.setTotalHiveTime(String.valueOf(totalroundofftime));
			ehvreportList.add(ehvreport);

		}
		Map<String, Object> searchHiveMap = new HashMap<String, Object>();
		searchHiveMap.put("list", ehvreportList);
		searchHiveMap.put("size", noOfRecords);

		return searchHiveMap;

	}

	@Override
	public List<Project> getAllActiveProjectList() {
		System.out.println("IN getAllActiveProjectList() ");
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Project.class);
		criteria.add(Restrictions.eq("status", ProjectStatus.INPROGRESS));
		return criteria.list();
	}

	@Override
	public List<AllocationDetails> getAllAllocationDetails(Long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AllocationDetails.class);
		criteria.createAlias("project","project");
		criteria.add(Restrictions.eq("project.id", id));
		criteria.add(Restrictions.eq("isAllocated", true));
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.statusName", "Active"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	@Override
	public List<EmpoloyeeHiveActivity> getAllHiveActivityForEmployee(Long empId,Date start,Date end) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmpoloyeeHiveActivity.class);
		criteria.add(Restrictions.eq("empId", empId));
		criteria.add(Restrictions.ge("date", start));
		criteria.add(Restrictions.le("date", end));
		return criteria.list();
	}
	
	@Override
	public List<Holidays> getAllHolidayForCurrentMonth(Date start, Date end) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Holidays.class);
		criteria.add(Restrictions.ge("date", start));
		criteria.add(Restrictions.le("date", end));
		return criteria.list();
	}
	
	@Override
	public List<LeaveDebit> getAllApprovedLeave(Long empId,Date from, Date to) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(LeaveDebit.class);
		criteria.createAlias("employee","employee");
		//criteria.createAlias("period","period");
		criteria.add(Restrictions.eq("employee.employeeId", empId));
		criteria.add(Restrictions.ge("period.minimum", from));
		criteria.add(Restrictions.le("period.maximum", to));
		criteria.add(Restrictions.eq("status", LeaveStatus.Approved));
		return criteria.list();
	}


}
