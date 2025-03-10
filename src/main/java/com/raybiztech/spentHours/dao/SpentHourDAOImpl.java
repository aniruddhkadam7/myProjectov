package com.raybiztech.spentHours.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.raybiztech.TimeInOffice.business.TimeInOffice;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.date.DateRange;
import com.raybiztech.spentHours.business.SpentTime;

@Repository("spentHourDAO")
public class SpentHourDAOImpl extends DAOImpl implements SpentHourDAO {

    @Override
    public List<TimeInOffice> getEmployeeSpentHours(String employeeId,
            DateRange monthPeriod) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                TimeInOffice.class);
        criteria.add(Restrictions.eq("empID", employeeId));
        criteria.add(Restrictions.between("dt", monthPeriod.getMinimum(),
                monthPeriod.getMaximum()));
        return criteria.list();
    }

    @Override
    public List<SpentTime> getSpentHours(DateRange monthPeriod) {
        // TODO Auto-generated method stub
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
                SpentTime.class);
        criteria.add(Restrictions.between("date", monthPeriod.getMinimum(),
                monthPeriod.getMaximum()));
        return criteria.list();
    }
}
