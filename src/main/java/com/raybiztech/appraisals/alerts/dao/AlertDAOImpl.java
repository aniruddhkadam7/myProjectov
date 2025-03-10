/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.alerts.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.raybiztech.TimeActivity.business.EmpoloyeeHiveActivity;
import com.raybiztech.TimeInOffice.business.TimeInOffice;
import com.raybiztech.appraisals.alerts.business.Alert;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.biometric.business.AttendanceStatus;
import com.raybiztech.biometric.business.BioAttendance;
import com.raybiztech.date.Date;

/**
 *
 * @author naresh
 */
@Component("alertDAO")
public class AlertDAOImpl extends DAOImpl implements AlertDAO {

    @Override
    public Boolean isHiveUpdated(String empEmail, Date date) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                EmpoloyeeHiveActivity.class);
        criteria.add(Restrictions.eq("mail", empEmail));
        criteria.add(Restrictions.eq("date", date));
        return criteria.list().isEmpty() ? true : false;
    }

    @Override
    public Map<String, Object> getAlerts(Long employeeId) {
        Map<String, Object> map = new HashMap<String, Object>();
        Criteria latestAlertcriteria = sessionFactory.getCurrentSession().createCriteria(
                Alert.class).add(Restrictions.eq("latestSatatus", Boolean.FALSE)).add(Restrictions.eq("employee.employeeId", employeeId));
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                Alert.class).addOrder(Order.desc("insertOn"));
        criteria.createAlias("employee", "emp");
        criteria.add(Restrictions.eq("emp.employeeId", employeeId));
        Integer sizeofAlerts = latestAlertcriteria.list().size();
        criteria.setFirstResult(0);
        criteria.setMaxResults(4);
        List<Alert> alerts = criteria.list();
        map.put("size", sizeofAlerts);
        map.put("alertsList", alerts);
        return map;
    }

    @Override
    public void getUpdateAllAlerts(Long employeeId) {
        Criteria latestAlertcriteria = sessionFactory.getCurrentSession().createCriteria(
                Alert.class).add(Restrictions.eq("latestSatatus", Boolean.FALSE)).add(Restrictions.eq("employee.employeeId", employeeId));
        List<Alert> alerts = latestAlertcriteria.list();
        for (Alert alert : alerts) {
            alert.setLatestSatatus(Boolean.TRUE);
            sessionFactory.getCurrentSession().saveOrUpdate(alert);
        }

    }

    @Override
    public Alert updateAlertDetails(Long employeeId, Long alertId) {
        Criteria latestAlertcriteria = sessionFactory.getCurrentSession().createCriteria(
                Alert.class);
        latestAlertcriteria.createAlias("employee", "emp");
        latestAlertcriteria.add(Restrictions.eq("emp.employeeId", employeeId));
        latestAlertcriteria.add(Restrictions.eq("id", alertId));
        return (Alert) latestAlertcriteria.uniqueResult();
    }

    @Override
    public Boolean isEmployeeActive(Long employeeId, Date date) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                BioAttendance.class);
        criteria.createAlias("employee", "emp");
        criteria.add(Restrictions.eq("emp.employeeId", employeeId));
        criteria.add(Restrictions.eq("attendanceDate", new Date().previous()));
        BioAttendance bioAttendance = (BioAttendance) criteria.uniqueResult();
        AttendanceStatus attendanceStatus = bioAttendance.getAttendanceStatus();
        return attendanceStatus.equals(AttendanceStatus.P) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public TimeInOffice isTimeInOfficeUpdated(Long employeeId, Date date) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                TimeInOffice.class);
        criteria.add(Restrictions.eq("empID", String.valueOf(employeeId)));
        criteria.add(Restrictions.eq("dt", date));
        return (TimeInOffice) criteria.uniqueResult();
    }

    @Override
    public Map<String, Object> getAllAlerts(Long employeeId, Integer startIndex, Integer endIndex) {
        Map<String, Object> map = new HashMap<String, Object>();
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                Alert.class).addOrder(Order.desc("insertOn"));
        criteria.createAlias("employee", "emp");
        criteria.add(Restrictions.eq("emp.employeeId", employeeId));
        Integer sizeofAlerts = criteria.list().size();
        criteria.setFirstResult(startIndex);
        criteria.setMaxResults(endIndex - startIndex);
        List<Alert> alerts = criteria.list();
        map.put("size", sizeofAlerts);
        map.put("alertsList", alerts);
        return map;
    }

    @Override
    public Boolean isHiveQuartzUpdatedYesterday() {
        String query = "From EmpoloyeeHiveActivity AS  eh  where eh.lastRunDate like '" +  new Date().toString("yyyy-MM-dd") + "%'";
        Query hql = sessionFactory.getCurrentSession().createQuery(query);
        return hql.list().isEmpty() ? false : true;
    }

    @Override
    public Boolean isTimeInOfficeQuartzUpdatedYesterday() {
        Date todayDate = new Date();
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                TimeInOffice.class);
        criteria.add(Restrictions.eq("insertedOn", todayDate));
        return criteria.list().isEmpty() ? false : true;
    }
}
