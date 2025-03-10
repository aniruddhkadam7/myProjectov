/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeInOffice.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.TimeInOffice.builder.TimeInOfficeBuilder;
import com.raybiztech.TimeInOffice.business.TimeInOffice;
import com.raybiztech.TimeInOffice.dto.SpentHoursDTO;
import com.raybiztech.TimeInOffice.dto.TimeInOfficeDTO;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.MonthYear;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.rolefeature.business.Permission;

/**
 *
 * @author hari
 */
@Component("timeInOfficeDAO")
public class TimeInOfficeDAOImpl extends DAOImpl implements TimeInOfficeDAO {

	@Autowired
	LeaveManagementUtils leaveManagementUtils;
	@Autowired
	TimeInOfficeBuilder timeInOfficeBuilder;
	@Autowired
	ProjectService projectService;
	final static Logger logger = Logger.getLogger(TimeInOfficeDAOImpl.class);

	@Override
	public Map<String, Object> getTimeInOfficeManagerReport(Employee employee,
			String date, Integer starIndex, Integer endIndex) {

		List<Integer> allMonthDays = new ArrayList<Integer>();
		for (int i = 1; i <= 31; ++i) {
			allMonthDays.add(i);
		}
		Integer noOfRecords = null;
		PeriodFormatter formatter = new PeriodFormatterBuilder()
				.minimumPrintedDigits(2).printZeroAlways().appendHours()
				.appendLiteral(":").appendMinutes().toFormatter();

		String h[] = date.split("/");
		MonthOfYear priviousMonthOfYear = MonthOfYear.valueOf((Integer.parseInt(h[0])-1));
		MonthOfYear monthOfYear = MonthOfYear.valueOf(Integer.parseInt(h[0]));
		YearOfEra yearOfEra = YearOfEra.valueOf(Integer.parseInt(h[1]));

		DateRange monthPeriod = leaveManagementUtils
				.getMonthPeriod(new MonthYear(monthOfYear, yearOfEra));
		String today = monthPeriod.getMinimum().toString("yyyy-MM-dd");
		String lastday = monthPeriod.getMaximum().toString("yyyy-MM-dd");

		String employeehqlQuery = " ";
		Query employeecreateQuery = null;
                Permission timeinOfficeReportAll = checkForPermission("Time in Office Report All",employee);
                Permission hierarchyTimeinOfficeReport = checkForPermission("Hierarchy Time in Office Report",employee);
		if (timeinOfficeReportAll.getView() && !hierarchyTimeinOfficeReport.getView()) {
			employeehqlQuery = "SELECT  employeeId,firstName,lastName From Employee e where  (e.statusName = 'Active' OR e.statusName = 'underNotice') and e.employeeId!=1000 ORDER BY e.employeeId ASC";
			employeecreateQuery = sessionFactory.getCurrentSession()
					.createQuery(employeehqlQuery);
			// employeecreateQuery.setParameter("Active", "Active");
			noOfRecords = employeecreateQuery.list().size();
			if (starIndex != null && endIndex != null){
			employeecreateQuery.setFirstResult(starIndex);
			employeecreateQuery.setMaxResults(endIndex - starIndex);
			}
		} else {
			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());
			employeehqlQuery = "SELECT  employeeId,firstName,lastName From Employee e where (e.manager.employeeId IN(:Id) and (e.statusName = 'Active' OR e.statusName = 'underNotice')) or e.employeeId=:mgrId";
			employeecreateQuery = sessionFactory.getCurrentSession()
					.createQuery(employeehqlQuery);
			employeecreateQuery.setParameterList("Id", managerIds);
			employeecreateQuery.setParameter("mgrId", employee.getEmployeeId());
			// employeecreateQuery.setParameter("Active", "Active");
			noOfRecords = employeecreateQuery.list().size();
			if (starIndex != null && endIndex != null){
			employeecreateQuery.setFirstResult(starIndex);
			employeecreateQuery.setMaxResults(endIndex - starIndex);
			}
		}

		List<Object[]> employeeresult = employeecreateQuery.list();
		List<TimeInOfficeDTO> timeInOfficeList = new ArrayList<TimeInOfficeDTO>();

		for (Object[] employeeName : employeeresult) {
			String employeeId = String.valueOf(employeeName[0]);

			TimeInOfficeDTO inOfficeDTO = new TimeInOfficeDTO();
			inOfficeDTO.setEmpName(String.valueOf(employeeName[1]) + " "
					+ String.valueOf(employeeName[2]));
			List<SpentHoursDTO> hours = new ArrayList<SpentHoursDTO>();
			inOfficeDTO.setEmpID(employeeId);
			String hqlQuery = "Select spentHours,day(te.dt) FROM TimeInOffice te WHERE  te.dt >= ' "
					+ today
					+ " '  and  te.dt <= ' "
					+ lastday
					+ " '  \n"
					+ " and te.empID= '"
					+ employeeId
					+ "'  order by date(te.dt)";
			Query createQuery = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			List<Integer> listOfTimeInOfficeDays = new ArrayList<Integer>();
			List<Object[]> result = createQuery.list();
			String sumOfHours = "0:0";
			for (Object[] row1 : result) {
				SpentHoursDTO spentHoursDTO = new SpentHoursDTO();
				String time = ((String) row1[0]).replaceAll("[h, ,m,r]", "");
				String timeSplit[] = time.split(":");
				if (!time.equals("0")) {
					time = time.format("%02d:%02d",
							Integer.valueOf(timeSplit[0]),
							Integer.valueOf(timeSplit[1]));
				} else
					time = "00:00";
				spentHoursDTO.setSpentHours(time);
				if (Integer.valueOf(timeSplit[0]) < 4) {
					spentHoursDTO.setColorForTime("red");
				}/*
				 * else if (Integer.valueOf(timeSplit[0]) >= 4 &&
				 * Integer.valueOf(timeSplit[0]) < 7) {
				 * spentHoursDTO.setColorForTime("gray"); }
				 */else {
					spentHoursDTO.setColorForTime("black");
				}
				spentHoursDTO.setSpentHours(time);
				Period period1 = formatter.parsePeriod(sumOfHours);
				Period period2 = null;
				if (time.equals("0")) {
					period2 = formatter.parsePeriod("0:00");
				} else {
					period2 = formatter.parsePeriod(time);
				}
				sumOfHours = formatter.print(period1.plus(period2));
				spentHoursDTO.setDayOfMonth(Integer.valueOf(String
						.valueOf(row1[1])));
				listOfTimeInOfficeDays.add((Integer) row1[1]);
				hours.add(spentHoursDTO);
			}
			String[] SumOfHoursSplit = sumOfHours.split(":");
			String totalHrs = String.valueOf((Integer
					.valueOf(SumOfHoursSplit[0]) + (Integer
					.valueOf(SumOfHoursSplit[1]) / 60)));
			String totalMints = String.valueOf((Integer
					.valueOf(SumOfHoursSplit[1]) % 60));
			inOfficeDTO.setTotalTimeInOfficeHours(totalHrs + ":" + totalMints);
			List<Integer> remainingDays = new ArrayList<Integer>(allMonthDays);
			remainingDays.removeAll(listOfTimeInOfficeDays);

			for (Integer day : remainingDays) {

				SpentHoursDTO spentHoursDTO = new SpentHoursDTO();
				spentHoursDTO.setSpentHours("-");
				spentHoursDTO.setColorForTime("hyphen");
				spentHoursDTO.setDayOfMonth(day);
				hours.add(spentHoursDTO);
			}

			inOfficeDTO.setInOfficeDTOs(hours);
			timeInOfficeList.add(inOfficeDTO);
		}
		
		// Privious Month Date range.
		MonthYear mon = new MonthYear(priviousMonthOfYear, yearOfEra);
		
		int priMin = monthPeriod.getMinimum().getDayOfMonth().getValue();
		int priLast = mon.getLastDay().getDayOfMonth().getValue();
		
		logger.warn(" last month Minimum Date range is = "+priMin+"  Last Day = "+priLast);
		
		// Current Month Date range.
		MonthYear monCurrent = new MonthYear(priviousMonthOfYear, yearOfEra);
				
		int currentMin =1;
		int currentLast = monthPeriod.getMaximum().getDayOfMonth().getValue();
		
		//List<Integer> daysList;
		ArrayList<Integer> daysList1 = new ArrayList<Integer>();
		
		int j =0;
		for(int i = priMin; i<= priLast; i++){
			Integer temp = (Integer)i;
			//daysList[j]=i;
			daysList1.add(temp);
			j++;
		}
		
		for(int i = currentMin; i<= currentLast; i++){
			//daysList[j]=i;
			Integer temp = (Integer)i;
			daysList1.add(temp);
			j++;
		}
		Map<String, Object> tiofcReportMap = new HashMap<String, Object>();
		tiofcReportMap.put("list", timeInOfficeList);
		tiofcReportMap.put("size", noOfRecords);
		tiofcReportMap.put("daysList",daysList1);
		
		

		return tiofcReportMap;
	}

	@Override
	public TimeInOfficeDTO getTimeInOfficeEmployeeReport(
			String loggedInEmployeeId, String date) {

		PeriodFormatter formatter = new PeriodFormatterBuilder()
				.minimumPrintedDigits(2).printZeroAlways().appendHours()
				.appendLiteral(":").appendMinutes().toFormatter();

		List<Integer> allMonthDays = new ArrayList<Integer>();
		for (int i = 1; i <= 31; ++i) {
			allMonthDays.add(i);
		}
		String h[] = date.split("/");
		MonthOfYear priviousMonthOfYear = MonthOfYear.valueOf((Integer.parseInt(h[0])-1));
		MonthOfYear monthOfYear = MonthOfYear.valueOf(Integer.parseInt(h[0]));
		YearOfEra yearOfEra = YearOfEra.valueOf(Integer.parseInt(h[1]));

		DateRange monthPeriod = leaveManagementUtils
				.getMonthPeriod(new MonthYear(monthOfYear, yearOfEra));
		String today = monthPeriod.getMinimum().toString("yyyy-MM-dd");
		String lastday = monthPeriod.getMaximum().toString("yyyy-MM-dd");

		String hqlQuery = "Select empName,spentHours,day(te.dt) FROM TimeInOffice te WHERE  te.dt >= ' "
				+ today
				+ " '  and  te.dt <= ' "
				+ lastday
				+ " '  \n"
				+ " and te.empID= '"
				+ loggedInEmployeeId
				+ "' order by date(te.dt)";
		Query createQuery = sessionFactory.getCurrentSession().createQuery(
				hqlQuery);
		TimeInOfficeDTO inOfficeDTO = new TimeInOfficeDTO();

		List<SpentHoursDTO> hours = new ArrayList<SpentHoursDTO>();
		List<Integer> listOfTimeInOfficeDays = new ArrayList<Integer>();
		List<Object[]> result = createQuery.list();
		String sumOfHours = "0:0";
		for (Object[] row1 : result) {
			SpentHoursDTO spentHoursDTO = new SpentHoursDTO();
			String time = ((String) row1[1]).replaceAll("[h, ,m,r]", "");
			String timeSplit[] = time.split(":");
			if (!time.equals("0")) {
				time = time.format("%02d:%02d", Integer.valueOf(timeSplit[0]),
						Integer.valueOf(timeSplit[1]));
			} else
				time = "00:00";
			spentHoursDTO.setSpentHours(time);
			if (Integer.valueOf(timeSplit[0]) < 4) {
				spentHoursDTO.setColorForTime("red");
			} /*
			 * else if (Integer.valueOf(timeSplit[0]) >= 4 &&
			 * Integer.valueOf(timeSplit[0]) < 7) {
			 * spentHoursDTO.setColorForTime("gray"); }
			 */else {
				spentHoursDTO.setColorForTime("black");
			}
			Period period1 = formatter.parsePeriod(sumOfHours);
			Period period2 = null;
			if (time.equals("0")) {
				period2 = formatter.parsePeriod("0:00");
			} else {
				period2 = formatter.parsePeriod(time);
			}
			sumOfHours = formatter.print(period1.plus(period2));
			spentHoursDTO
					.setDayOfMonth(Integer.valueOf(String.valueOf(row1[2])));
			listOfTimeInOfficeDays.add((Integer) row1[2]);
			hours.add(spentHoursDTO);
		}
		String[] SumOfHoursSplit = sumOfHours.split(":");
		String totalHrs = String
				.valueOf((Integer.valueOf(SumOfHoursSplit[0]) + (Integer
						.valueOf(SumOfHoursSplit[1]) / 60)));
		String totalMints = String
				.valueOf((Integer.valueOf(SumOfHoursSplit[1]) % 60));
		inOfficeDTO.setTotalTimeInOfficeHours(totalHrs + ":" + totalMints);

		List<Integer> remainingDays = new ArrayList<Integer>(allMonthDays);
		remainingDays.removeAll(listOfTimeInOfficeDays);

		for (Integer day : remainingDays) {

			SpentHoursDTO spentHoursDTO = new SpentHoursDTO();
			spentHoursDTO.setSpentHours("-");
			spentHoursDTO.setColorForTime("hyphen");
			spentHoursDTO.setDayOfMonth(day);
			hours.add(spentHoursDTO);
		}
		inOfficeDTO.setEmpID(loggedInEmployeeId);
		inOfficeDTO.setInOfficeDTOs(hours);
		
		// Privious Month Date range.
		MonthYear mon = new MonthYear(priviousMonthOfYear, yearOfEra);
		
		int priMin = monthPeriod.getMinimum().getDayOfMonth().getValue();
		int priLast = mon.getLastDay().getDayOfMonth().getValue();
		
		logger.warn(" last month Minimum Date range is = "+priMin+"  Last Day = "+priLast);
		
		// Current Month Date range.
		MonthYear monCurrent = new MonthYear(priviousMonthOfYear, yearOfEra);
				
		int currentMin =1;
		int currentLast = monthPeriod.getMaximum().getDayOfMonth().getValue();
		
		//List<Integer> daysList;
		ArrayList<Integer> daysList1 = new ArrayList<Integer>();
		
		int j =0;
		for(int i = priMin; i<= priLast; i++){
			Integer temp = (Integer)i;
			//daysList[j]=i;
			daysList1.add(temp);
			j++;
		}
		
		for(int i = currentMin; i<= currentLast; i++){
			//daysList[j]=i;
			Integer temp = (Integer)i;
			daysList1.add(temp);
			j++;
		}
		
		inOfficeDTO.setDayList(daysList1);
				
		
		return inOfficeDTO;

	}

	@Override
	public Map<String, Object> searchTimeInOffice(Employee employee,
			String date, Integer startIndex, Integer endIndex, String search) {

		List<Integer> allMonthDays = new ArrayList<Integer>();
		for (int i = 1; i <= 31; ++i) {
			allMonthDays.add(i);
		}
		Integer noOfRecords = null;
		PeriodFormatter formatter = new PeriodFormatterBuilder()
				.minimumPrintedDigits(2).printZeroAlways().appendHours()
				.appendLiteral(":").appendMinutes().toFormatter();

		String h[] = date.split("/");
		MonthOfYear monthOfYear = MonthOfYear.valueOf(Integer.parseInt(h[0]));
		YearOfEra yearOfEra = YearOfEra.valueOf(Integer.parseInt(h[1]));

		DateRange monthPeriod = leaveManagementUtils
				.getMonthPeriod(new MonthYear(monthOfYear, yearOfEra));
		String today = monthPeriod.getMinimum().toString("yyyy-MM-dd");
		String lastday = monthPeriod.getMaximum().toString("yyyy-MM-dd");

		String employeehqlQuery = " ";
		Query employeecreateQuery = null;
                 Permission timeinOfficeReportAll = checkForPermission("Time in Office Report All",employee);
                Permission hierarchyTimeinOfficeReport = checkForPermission("Hierarchy Time in Office Report",employee);
		if (timeinOfficeReportAll.getView() && !hierarchyTimeinOfficeReport.getView()) {
		
			try {
				Long empid = Long.valueOf(search);
				employeehqlQuery = "SELECT  employeeId,firstName,lastName From Employee e where (e.statusName = 'Active' OR e.statusName = 'underNotice') and e.employeeId!=1000 and (e.employeeId like '%"
						+ empid + "%') ORDER BY e.employeeId ASC";
			} catch (NumberFormatException nfe) {

				employeehqlQuery = "SELECT  employeeId,firstName,lastName From Employee e where (e.statusName = 'Active' OR e.statusName = 'underNotice') and e.employeeId!=1000 and (concat(e.firstName, ' ', e.lastName) like '%"
						+ search + "%') ORDER BY e.employeeId ASC";
			}
			employeecreateQuery = sessionFactory.getCurrentSession()
					.createQuery(employeehqlQuery);
			// employeecreateQuery.setParameter("Active", "Active");
			noOfRecords = employeecreateQuery.list().size();
			if (startIndex != null && endIndex != null){
			employeecreateQuery.setFirstResult(startIndex);
			employeecreateQuery.setMaxResults(endIndex - startIndex);
			}
		} else {
			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());
			try {
				Long empid = Long.valueOf(search);
				employeehqlQuery = "SELECT  employeeId,firstName,lastName From Employee e where  ((e.manager.employeeId IN(:Id) and (e.statusName = 'Active' OR e.statusName = 'underNotice'))) and e.employeeId!=1000 and (e.employeeId like '%"
						+ empid + "%') ORDER BY e.employeeId ASC";
			} catch (NumberFormatException nfe) {
				employeehqlQuery = "SELECT  employeeId,firstName,lastName From Employee e where (e.manager.employeeId IN(:Id) and (e.statusName = 'Active' OR e.statusName = 'underNotice')) and (concat(e.firstName, ' ', e.lastName) like '%"
						+ search + "%')";
			}
			employeecreateQuery = sessionFactory.getCurrentSession()
					.createQuery(employeehqlQuery);
			employeecreateQuery.setParameterList("Id", managerIds);
			//employeecreateQuery.setParameter("Id", employee.getEmployeeId());
			// employeecreateQuery.setParameter("Active", "Active");
			noOfRecords = employeecreateQuery.list().size();
			if (startIndex != null && endIndex != null){
			employeecreateQuery.setFirstResult(startIndex);
			employeecreateQuery.setMaxResults(endIndex - startIndex);
			}
		}

		List<Object[]> employeeresult = employeecreateQuery.list();
		List<TimeInOfficeDTO> timeInOfficeList = new ArrayList<TimeInOfficeDTO>();

		for (Object[] employeeName : employeeresult) {
			String employeeId = String.valueOf(employeeName[0]);

			TimeInOfficeDTO inOfficeDTO = new TimeInOfficeDTO();
			inOfficeDTO.setEmpName(String.valueOf(employeeName[1]) + " "
					+ String.valueOf(employeeName[2]));
			List<SpentHoursDTO> hours = new ArrayList<SpentHoursDTO>();
			inOfficeDTO.setEmpID(employeeId);
			String hqlQuery = "Select spentHours,day(te.dt) FROM TimeInOffice te WHERE  te.dt >= ' "
					+ today
					+ " '  and  te.dt <= ' "
					+ lastday
					+ " '  \n"
					+ " and te.empID= '"
					+ employeeId
					+ "'  order by date(te.dt)";
			Query createQuery = sessionFactory.getCurrentSession().createQuery(
					hqlQuery);
			List<Integer> listOfTimeInOfficeDays = new ArrayList<Integer>();
			List<Object[]> result = createQuery.list();
			String sumOfHours = "0:0";
			for (Object[] row1 : result) {
				SpentHoursDTO spentHoursDTO = new SpentHoursDTO();
				String time = ((String) row1[0]).replaceAll("[h, ,m,r]", "");
				String timeSplit[] = time.split(":");
				if (!time.equals("0")) {
					time = time.format("%02d:%02d",
							Integer.valueOf(timeSplit[0]),
							Integer.valueOf(timeSplit[1]));
				} else
					time = "00:00";
				spentHoursDTO.setSpentHours(time);
				if (Integer.valueOf(timeSplit[0]) < 4) {
					spentHoursDTO.setColorForTime("red");
				} /*
				 * else if (Integer.valueOf(timeSplit[0]) >= 4 &&
				 * Integer.valueOf(timeSplit[0]) < 7) {
				 * spentHoursDTO.setColorForTime("gray"); }
				 */else {
					spentHoursDTO.setColorForTime("black");
				}
				spentHoursDTO.setSpentHours(time);
				Period period1 = formatter.parsePeriod(sumOfHours);
				Period period2 = null;
				if (time.equals("0")) {
					period2 = formatter.parsePeriod("0:00");
				} else {
					period2 = formatter.parsePeriod(time);
				}
				sumOfHours = formatter.print(period1.plus(period2));
				spentHoursDTO.setDayOfMonth(Integer.valueOf(String
						.valueOf(row1[1])));
				listOfTimeInOfficeDays.add((Integer) row1[1]);
				hours.add(spentHoursDTO);
			}
			String[] SumOfHoursSplit = sumOfHours.split(":");
			String totalHrs = String.valueOf((Integer
					.valueOf(SumOfHoursSplit[0]) + (Integer
					.valueOf(SumOfHoursSplit[1]) / 60)));
			String totalMints = String.valueOf((Integer
					.valueOf(SumOfHoursSplit[1]) % 60));
			inOfficeDTO.setTotalTimeInOfficeHours(totalHrs + ":" + totalMints);
			List<Integer> remainingDays = new ArrayList<Integer>(allMonthDays);
			remainingDays.removeAll(listOfTimeInOfficeDays);

			for (Integer day : remainingDays) {

				SpentHoursDTO spentHoursDTO = new SpentHoursDTO();
				spentHoursDTO.setSpentHours("-");
				spentHoursDTO.setColorForTime("hyphen");
				spentHoursDTO.setDayOfMonth(day);
				hours.add(spentHoursDTO);
			}

			inOfficeDTO.setInOfficeDTOs(hours);
			timeInOfficeList.add(inOfficeDTO);
		}
		Map<String, Object> timeInOfcMap = new HashMap<String, Object>();
		timeInOfcMap.put("list", timeInOfficeList);
		timeInOfcMap.put("size", noOfRecords);

		return timeInOfcMap;

	}

	@Override
	public List<TimeInOffice> getweeklyTimeInOfficeList(
			String loggedInEmployeeId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				TimeInOffice.class);
		criteria.add(Restrictions.eq("empID", loggedInEmployeeId));
		criteria.addOrder(Order.desc("dt"));
		criteria.setMaxResults(5);

		return criteria.list();
	}
}
