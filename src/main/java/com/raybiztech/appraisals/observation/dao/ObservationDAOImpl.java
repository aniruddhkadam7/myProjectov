package com.raybiztech.appraisals.observation.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.observation.business.Observation;
import com.raybiztech.appraisals.observation.dto.SearchObservationDTO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.recruitment.utils.DateParser;

import java.text.ParseException;
import java.util.Calendar;

@Repository
public class ObservationDAOImpl extends DAOImpl implements ObservationDAO {

    Logger logger = Logger.getLogger(ObservationDAOImpl.class);
    @Autowired
    SecurityUtils securityUtils;

    @Override
    public Set<Observation> getEmployeeObservationsListUnderManager(
            List<Long> managerIds) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                Observation.class);
        criteria.createAlias("addedBy", "addedBy");
        criteria.setFetchMode("addedBy", FetchMode.JOIN);
        // criteria.add(Restrictions.eq("addedBy.employeeId",
        // employee.getEmployeeId()));
        criteria.add(Restrictions.in("addedBy.employeeId", managerIds));

        Set<Observation> alphaSet = new HashSet<Observation>(criteria.list());
        return alphaSet;
    }

    @Override
    public Map<String, Object> getEmployeeObservationsListUnderAdmin(
            Integer startIndex, Integer endIndex) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                Observation.class);
        Integer noOfRecords = criteria.list().size();
        criteria.setFirstResult(startIndex);
        criteria.setMaxResults(endIndex - startIndex);
        Set<Observation> alphaSet = new HashSet<Observation>(criteria.list());
        Map<String, Object> observationMap = new HashMap<String, Object>();
        observationMap.put("list", alphaSet);
        observationMap.put("size", noOfRecords);
        return observationMap;
    }

    @Override
    public List<Employee> managerReporties(Employee employee) {

        Criteria criteria = sessionFactory.getCurrentSession()
                .createCriteria(Employee.class)
                .add(Restrictions.eq("statusName", "Active"));
        criteria.createAlias("manager", "mgr");
        criteria.setFetchMode("mgr", FetchMode.JOIN);
        criteria.add(Restrictions.eq("mgr.employeeId", employee.getEmployeeId()));
        criteria.add(Restrictions.eq("role", "Manager"));
        return criteria.list();
    }

    // For search Observation
    @Override
    public Set<Observation> getEmployeeObservationsListUnderManager(
            List<Long> empids, SearchObservationDTO observation, Date minDate,
            Date maxDate) {

        Long loggedInEmployeeId = securityUtils
                .getLoggedEmployeeIdforSecurityContextHolder();

        Employee employee = findBy(Employee.class, loggedInEmployeeId);
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = DateParser.toDate(observation.getFromDate());
            toDate = DateParser.toDate(observation.getToDate());

        } catch (Exception e) {
        }

        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                Observation.class);
        criteria.createAlias("addedBy", "addedBy");
        criteria.setFetchMode("addedBy", FetchMode.JOIN);
        criteria.createAlias("employee", "employee");
        criteria.setFetchMode("employee", FetchMode.JOIN);
        // criteria.add(Restrictions.eq("addedBy.employeeId",
        // employee.getEmployeeId()));

        Criteria criteria2 = sessionFactory.getCurrentSession().createCriteria(
                Employee.class);
        criteria2.add(Restrictions.or(
                Restrictions.eq("employeeId", loggedInEmployeeId),
                Restrictions.in("manager.employeeId", empids)));

        criteria.add(Restrictions.or(
                Restrictions.eq("addedBy.employeeId", loggedInEmployeeId),
                Restrictions.in("employee", criteria2.list())));

        if (maxDate != null && minDate != null) {
            criteria.add(Restrictions.between("observationMonth", minDate,
                    maxDate));
        }
        // boolean flag = true;
        if (observation.getRating() != null) {
            criteria.add(Restrictions.eq("rating", observation.getRating()));
        }
        // if (observation.getSelectionDates() != null)
        // if (observation.getSelectionDates().equalsIgnoreCase("today")) {
        //
        // criteria.add(Restrictions.eq("date", new Date()));
        // } else if (observation.getSelectionDates().equalsIgnoreCase(
        // "yesterday")) {
        // criteria.add(Restrictions.eq("date", new Date().previous()));
        // } else if (observation.getSelectionDates().equalsIgnoreCase(
        // "custom")) {
        // criteria.add(Restrictions.between("date", fromDate, toDate));
        //
        // } else if (observation.getSelectionDates().equalsIgnoreCase(
        // "thisweek")) {
        // Date weekStart = getFirstDayOfWeek();
        //
        // Date weekend = weekStart.shift(new Duration(
        // com.raybiztech.date.TimeUnit.DAY, +6));
        //
        // criteria.add(Restrictions.between("date", weekStart, weekend));
        //
        // } else if (observation.getSelectionDates().equalsIgnoreCase(
        // "lastweek")) {
        // Date currentweekstart = getFirstDayOfWeek();
        // Date lastweekstart = currentweekstart.shift(new Duration(
        // com.raybiztech.date.TimeUnit.DAY, -7));
        // Date lastweekend = lastweekstart.shift(new Duration(
        // com.raybiztech.date.TimeUnit.DAY, +6));
        // criteria.add(Restrictions.between("date", lastweekstart,
        // lastweekend));
        //
        // }
        //
        // else if (observation.getSelectionDates().equalsIgnoreCase(
        // "lastmonth")) {
        // Date date = new Date(DayOfMonth.valueOf(1),
        // MonthOfYear.valueOf(new Date().getMonthOfYear()
        // .getValue()), YearOfEra.valueOf(new Date()
        // .getYearOfEra().getValue()));
        // Date lastMonthLastDate = date.shift(new Duration(
        // com.raybiztech.date.TimeUnit.DAY, -1));
        // Date lastMonthFirstDate = new Date(DayOfMonth.valueOf(1),
        // MonthOfYear.valueOf(lastMonthLastDate.getMonthOfYear()
        // .getValue()),
        // YearOfEra.valueOf(lastMonthLastDate.getYearOfEra()
        // .getValue()));
        //
        // criteria.add(Restrictions.between("date", lastMonthFirstDate,
        // lastMonthLastDate));
        // } else if (observation.getSelectionDates().equalsIgnoreCase(
        // "currentmonth")) {
        //
        // Date firstDate = new Date(DayOfMonth.valueOf(1),
        // MonthOfYear.valueOf(new Date().getMonthOfYear()
        // .getValue()), YearOfEra.valueOf(new Date()
        // .getYearOfEra().getValue()));
        //
        // Date lastDate = new Date(DayOfMonth.valueOf(31),
        // MonthOfYear.valueOf(new Date().getMonthOfYear()
        // .getValue()), YearOfEra.valueOf(new Date()
        // .getYearOfEra().getValue()));
        //
        // criteria.add(Restrictions.between("date", firstDate, lastDate));
        //
        // }

        /*
         * if (observation.getSelecteedMonthandYear() != null) {
         *
         * Date date = null; try { date = DateParser
         * .toDate(observation.getSelecteedMonthandYear()); } catch
         * (ParseException e) { e.printStackTrace(); }
         * criteria.add(Restrictions.eq("observationMonth", date));
         *
         * }
         */

        Conjunction conjunction = Restrictions.conjunction();
        if (observation != null) {

            String searchStr = observation.getSearchString();
            // if (observation.isCheckEmpId()) {
            // try {
            // Criterion empIdCriterion = Restrictions.eq("employee.employeeId",
            // Long.parseLong(searchStr));
            // conjunObservationDAOImplction.add(empIdCriterion);
            // } catch (NumberFormatException nfe) {
            // flag = false;
            // }
            // }
            // if (flag) {
            if (observation.isCheckEmpName()) {

                /*
                 * Criterion empFirstName = Restrictions.ilike(
                 * "employee.firstName", searchStr, MatchMode.ANYWHERE);
                 * Criterion empLastName =
                 * Restrictions.ilike("employee.lastName", searchStr,
                 * MatchMode.ANYWHERE);
                 * conjunction.add(Restrictions.or(empFirstName, empLastName));
                 */

                Criterion empFullName = Restrictions.ilike(
                        "employee.employeeFullName", searchStr,
                        MatchMode.ANYWHERE);

                conjunction.add(empFullName);
            }
            if (observation.isCheckAddedByName()) {

                /*
                 * Criterion addedByFirstName = Restrictions.ilike(
                 * "addedBy.firstName", searchStr, MatchMode.ANYWHERE);
                 * Criterion addedByLastName = Restrictions.ilike(
                 * "addedBy.lastName", searchStr, MatchMode.ANYWHERE);
                 * conjunction.add(Restrictions.or(addedByFirstName,
                 * addedByLastName));
                 */

                Criterion addedByFullName = Restrictions.ilike(
                        "addedBy.employeeFullName", searchStr,
                        MatchMode.ANYWHERE);

                conjunction.add(addedByFullName);

            }
            // }
        }

        criteria.add(conjunction);
        criteria.addOrder(Order.desc("id"));
        Set<Observation> alphaSet = new HashSet<Observation>(criteria.list());
        return alphaSet;
    }

    @Override
    public Map<String, Object> getEmployeeObservationsListUnderAdmin(
            Integer startIndex, Integer endIndex,
            SearchObservationDTO observation, Date minDate, Date maxDate) {
        Date fromDate = null;
        Date toDate = null;

        try {
            fromDate = DateParser.toDate(observation.getFromDate());
            toDate = DateParser.toDate(observation.getToDate());

        } catch (Exception e) {
        }
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                Observation.class);
        criteria.createAlias("addedBy", "addedBy");
        criteria.setFetchMode("addedBy", FetchMode.JOIN);
        criteria.createAlias("employee", "employee");
        criteria.setFetchMode("employee", FetchMode.JOIN);

        if (observation.getEmployeeStatus().equalsIgnoreCase("Active")) {
            criteria.add(Restrictions.eq("employee.statusName", "Active"));
        } else {
            criteria.add(Restrictions.eq("employee.statusName", "InActive"));
        }

        if (minDate != null && maxDate != null) {
            criteria.add(Restrictions.between("observationMonth", minDate,
                    maxDate));
        }
        if (observation.getRating() != null) {
            criteria.add(Restrictions.eq("rating", observation.getRating()));
        }
        // if (observation.getSelectionDates() != null)
        // if (observation.getSelectionDates().equalsIgnoreCase("today")) {
        //
        // criteria.add(Restrictions.eq("date", new Date()));
        // } else if (observation.getSelectionDates().equalsIgnoreCase(
        // "yesterday")) {
        // criteria.add(Restrictions.eq("date", new Date().previous()));
        // } else if (observation.getSelectionDates().equalsIgnoreCase(
        // "custom")) {
        // criteria.add(Restrictions.between("date", fromDate, toDate));
        //
        // } else if (observation.getSelectionDates().equalsIgnoreCase(
        // "thisweek")) {
        // Date weekStart = getFirstDayOfWeek();
        //
        // Date weekend = weekStart.shift(new Duration(
        // com.raybiztech.date.TimeUnit.DAY, +6));
        // criteria.add(Restrictions.between("date", weekStart, weekend));
        //
        // } else if (observation.getSelectionDates().equalsIgnoreCase(
        // "lastweek")) {
        // Date currentweekstart = getFirstDayOfWeek();
        // Date lastweekstart = currentweekstart.shift(new Duration(
        // com.raybiztech.date.TimeUnit.DAY, -7));
        // Date lastweekend = lastweekstart.shift(new Duration(
        // com.raybiztech.date.TimeUnit.DAY, +6));
        //
        // criteria.add(Restrictions.between("date", lastweekstart,
        // lastweekend));
        //
        // }
        //
        // else if (observation.getSelectionDates().equalsIgnoreCase(
        // "lastmonth")) {
        // Date date = new Date(DayOfMonth.valueOf(1),
        // MonthOfYear.valueOf(new Date().getMonthOfYear()
        // .getValue()), YearOfEra.valueOf(new Date()
        // .getYearOfEra().getValue()));
        // Date lastMonthLastDate = date.shift(new Duration(
        // com.raybiztech.date.TimeUnit.DAY, -1));
        // Date lastMonthFirstDate = new Date(DayOfMonth.valueOf(1),
        // MonthOfYear.valueOf(lastMonthLastDate.getMonthOfYear()
        // .getValue()),
        // YearOfEra.valueOf(lastMonthLastDate.getYearOfEra()
        // .getValue()));
        //
        // criteria.add(Restrictions.between("date", lastMonthFirstDate,
        // lastMonthLastDate));
        // } else if (observation.getSelectionDates().equalsIgnoreCase(
        // "currentmonth")) {
        //
        // Date firstDate = new Date(DayOfMonth.valueOf(1),
        // MonthOfYear.valueOf(new Date().getMonthOfYear()
        // .getValue()), YearOfEra.valueOf(new Date()
        // .getYearOfEra().getValue()));
        //
        // Date lastDate = new Date(DayOfMonth.valueOf(31),
        // MonthOfYear.valueOf(new Date().getMonthOfYear()
        // .getValue()), YearOfEra.valueOf(new Date()
        // .getYearOfEra().getValue()));
        //
        // criteria.add(Restrictions.between("date", firstDate, lastDate));
        //
        // }
        /*
         * if (observation.getSelecteedMonthandYear() != null) { Date date =
         * null; try { date = DateParser
         * .toDate(observation.getSelecteedMonthandYear()); } catch
         * (ParseException e) { e.printStackTrace(); }
         * criteria.add(Restrictions.eq("observationMonth", date));
         *
         * }
         */
        // boolean flag = true;
        
        Conjunction conjunction = Restrictions.conjunction();
        if (observation != null) {

            String searchStr = observation.getSearchString();
            // if (observation.isCheckEmpId()) {
            // try {
            // Criterion empIdCriterion = Restrictions.eq("employee.employeeId",
            // Long.parseLong(searchStr));
            // conjunction.add(empIdCriterion);
            // } catch (NumberFormatException nfe) {
            // flag = false;
            // }
            // }
            // if (flag) {
            if (observation.isCheckEmpName()) {

                /*
                 * Criterion empFirstName = Restrictions.ilike(
                 * "employee.firstName", searchStr, MatchMode.ANYWHERE);
                 * Criterion empLastName =
                 * Restrictions.ilike("employee.lastName", searchStr,
                 * MatchMode.ANYWHERE);
                 * conjunction.add(Restrictions.or(empFirstName, empLastName));
                 */

                Criterion empFullName = Restrictions.ilike(
                        "employee.employeeFullName", searchStr,
                        MatchMode.ANYWHERE);

                conjunction.add(empFullName);
            }
            if (observation.isCheckAddedByName()) {

                /*
                 * Criterion addedByFirstName = Restrictions.ilike(
                 * "addedBy.firstName", searchStr, MatchMode.ANYWHERE);
                 * Criterion addedByLastName = Restrictions.ilike(
                 * "addedBy.lastName", searchStr, MatchMode.ANYWHERE);
                 * conjunction.add(Restrictions.or(addedByFirstName,
                 * addedByLastName));
                 */

                Criterion addedByFullName = Restrictions.ilike(
                        "addedBy.employeeFullName", searchStr,
                        MatchMode.ANYWHERE);

                conjunction.add(addedByFullName);
            }
            // }
        }
        criteria.add(conjunction);
        criteria.addOrder(Order.desc("id"));
        //Avoid duplicate observation for same month
        List<Observation> observationList = new ArrayList<Observation>(criteria.list());
        List<Observation> uniqueObservations = new ArrayList<Observation>();
        List<Observation> removeList=new ArrayList<Observation>();
        
        for(Observation observations:observationList)
        {
        
    
        	int i = 0;

        	if(uniqueObservations.size() > 0)
        	{
        		for(Observation observation2 : uniqueObservations){
        			if(observation2.getEmployee().getEmployeeId().equals(observations.getEmployee().getEmployeeId())
                        && observation2.getObservationMonth().equals( observations.getObservationMonth()))
                        {
                            i=1;
                            if(observation2.getId()< observations.getId()){
                                removeList.add(observation2);
                                i = 0;
                            }
                        }
                    
            }
        }
        if(i == 0 ){
        	uniqueObservations.add(observations);
        }
        
        uniqueObservations.removeAll(removeList);
        
        }
        
        Integer noOfRecords = uniqueObservations.size();
        
        //criteria.setFirstResult(startIndex);
        //criteria.setMaxResults(endIndex - startIndex);
        
        
        
        Set<Observation> alphaSet = new HashSet<Observation>(uniqueObservations);
        Map<String, Object> observationMap = new HashMap<String, Object>();
        observationMap.put("list", alphaSet);
        observationMap.put("size", noOfRecords);
        return observationMap;
    }

    @Override
    public Map<String, Object> getEmployeeIndvisualObservations(
            Integer startIndex, Integer endIndex,
            SearchObservationDTO observation, Date minDate, Date maxDate) {
        Date fromDate = null;
        Date toDate = null;

        try {
            fromDate = DateParser.toDate(observation.getFromDate());
            toDate = DateParser.toDate(observation.getToDate());

        } catch (Exception e) {
        }
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                Observation.class);
        Long loggedInEmployeeId = securityUtils
                .getLoggedEmployeeIdforSecurityContextHolder();
        // criteria.createAlias("addedBy", "addedBy");
        // criteria.setFetchMode("addedBy", FetchMode.JOIN);
        criteria.createAlias("employee", "employee");
        criteria.setFetchMode("employee", FetchMode.JOIN);
        criteria.add(Restrictions.eq("employee.employeeId", loggedInEmployeeId));
        if (minDate != null && maxDate != null) {
            criteria.add(Restrictions.between("observationMonth", minDate,
                    maxDate));
        }
        Integer noOfRecords = criteria.list().size();
        // criteria.setFirstResult(startIndex);
        // criteria.setMaxResults(endIndex - startIndex);
        Set<Observation> alphaSet = new HashSet<Observation>(criteria.list());
        Map<String, Object> observationMap = new HashMap<String, Object>();
        observationMap.put("list", alphaSet);
        observationMap.put("size", noOfRecords);
        return observationMap;
    }

    @Override
    public List<Observation> getAllEmployeesUnderAdmin() {
        // TODO Auto-generated method stub

        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                Observation.class);
        criteria.addOrder(Order.desc("id"));
        Integer noOfRecords = criteria.list().size();

        return criteria.list();
    }

    private Date getFirstDayOfWeek() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date.getJavaDate());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

        return new Date(c.getTimeInMillis());
    }

    @Override
    public Map<String, Object> getAllMonthwiseObservation(Integer startIndex,
            Integer endIndex, Date fromDate, Date toDate, Long employeeId) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                Observation.class);
        criteria.createAlias("employee", "employee");
        criteria.add(Restrictions.eq("employee.employeeId", employeeId));
        criteria.add(Restrictions.between("observationMonth", fromDate, toDate));

        Integer noOfRecords = criteria.list().size();
        // criteria.setFirstResult(startIndex);
        // criteria.setMaxResults(endIndex - startIndex);
        Set<Observation> observationSet = new HashSet<Observation>(
                criteria.list());
        Map<String, Object> observationMap = new HashMap<String, Object>();
        observationMap.put("list", observationSet);
        observationMap.put("size", noOfRecords);

        return observationMap;
    }

    // To get all non rated employees of that particular month
    @Override
    public List<Employee> getNonRatedEmployees(
            SearchObservationDTO searchObservationDTO) {

        // To get all the active employees
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                Employee.class);
        criteria.add(Restrictions.eq("statusName", "Active")).add(
                Restrictions.ne("employeeId", 1000l));

        List<Employee> activeEmployees = criteria.list();

        Date date1 = null;
        try {
            date1 = DateParser.toDate(searchObservationDTO
                    .getSelecteedMonthandYear());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // To get the observations of that particular month
        Criteria criteria2 = sessionFactory.getCurrentSession().createCriteria(
                Observation.class);
        criteria2.createAlias("employee", "employee");
        criteria2.add(Restrictions.eq("observationMonth", date1));
        // criteria2.add(Restrictions.between("observationMonth",
        // fromDate,toDate));
        criteria2.add(Restrictions.eq("employee.statusName", "Active"));

        criteria2.setProjection(Projections.distinct(Projections
                .property("employee")));

        List<Employee> ratedEmployees = criteria2.list();

        // Removing from the list who are already rated for that particular
        // month
        activeEmployees.removeAll(ratedEmployees);

        // to filter the data based in employee name
        if (searchObservationDTO.isCheckEmpName()) {
            Criteria criteria3 = sessionFactory.getCurrentSession()
                    .createCriteria(Employee.class);
            /*
             * Criterion empFirstName = Restrictions.ilike("firstName",
             * searchObservationDTO.getSearchString(), MatchMode.ANYWHERE);
             * Criterion empLastName = Restrictions.ilike("lastName",
             * searchObservationDTO.getSearchString(), MatchMode.ANYWHERE);
             * criteria3.add(Restrictions.or(empFirstName, empLastName));
             * activeEmployees.retainAll(criteria3.list());
             */

            Criterion empFullName = Restrictions.ilike("employeeFullName",
                    searchObservationDTO.getSearchString(), MatchMode.ANYWHERE);
            criteria3.add(empFullName);
            activeEmployees.retainAll(criteria3.list());

        }
        // To filter the data based on manager name
        if (searchObservationDTO.isCheckAddedByName()) {
            Criteria criteria4 = sessionFactory.getCurrentSession()
                    .createCriteria(Employee.class);
            /*
             * criteria4.createAlias("manager", "manager"); Criterion
             * addedByFirstName = Restrictions.ilike( "manager.firstName",
             * searchObservationDTO.getSearchString(), MatchMode.ANYWHERE);
             * Criterion addedByLastName =
             * Restrictions.ilike("manager.lastName",
             * searchObservationDTO.getSearchString(), MatchMode.ANYWHERE);
             * criteria4.add(Restrictions.or(addedByFirstName,
             * addedByLastName)); activeEmployees.retainAll(criteria4.list());
             */
            criteria4.createAlias("manager", "manager");
            Criterion managerFullName = Restrictions.ilike(
                    "manager.employeeFullName",
                    searchObservationDTO.getSearchString(), MatchMode.ANYWHERE);
            criteria4.add(managerFullName);
            activeEmployees.retainAll(criteria4.list());

        }

        return activeEmployees;
    }

    // To check whether the employee has given the observation for that
    // particular month from the same manager
    @Override
    public Boolean checkForDuplication(Observation observation) {

        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                Observation.class);
        criteria.createAlias("employee", "employee");
        criteria.createAlias("addedBy", "addedBy");
        criteria.add(Restrictions.eq("employee", observation.getEmployee()));
        criteria.add(Restrictions.eq("addedBy", observation.getAddedBy()));
        criteria.add(Restrictions.eq("observationMonth",
                observation.getObservationMonth()));
        if (criteria.list().isEmpty()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

}