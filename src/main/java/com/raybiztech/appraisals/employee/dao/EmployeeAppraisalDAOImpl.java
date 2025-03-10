package com.raybiztech.appraisals.employee.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.AppraisalBuilder;
import com.raybiztech.appraisals.builder.KRABuilder;
import com.raybiztech.appraisals.business.Appraisal;
import com.raybiztech.appraisals.business.KPIRating;
import com.raybiztech.appraisals.business.KraWithWeightage;
import com.raybiztech.appraisals.business.Status;
import com.raybiztech.appraisals.dao.DAOImpl;

@Component("employeeAppraisalDAO")
public class EmployeeAppraisalDAOImpl extends DAOImpl implements
		EmployeeAppraisalDAO {
	private static Logger logger = Logger
			.getLogger(EmployeeAppraisalDAOImpl.class);
	@Autowired
	KRABuilder kraBuilder;
	@Autowired
	AppraisalBuilder appraisalBuilder;

	@Override
	public Appraisal getEmployeeAppraisalData(Long employeeId) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(Appraisal.class);
		criteria.createAlias("employee", "emp");
		criteria.setFetchMode("emp", FetchMode.JOIN);
		criteria.createAlias("cycle", "cycle");
		criteria.setFetchMode("cycle", FetchMode.JOIN);
		criteria.add(Restrictions.and(
				Restrictions.eq("emp.employeeId", employeeId),
				Restrictions.eq("cycle.status", Status.IN_PROCESS.toString())));
		Appraisal appraisal = (Appraisal) criteria.uniqueResult();
		logger.info("appraisal object inside getEmployeeAppraisalData() :"
				+ appraisal);
		return appraisal;
	}

	@Override
	public void saveKPIRating(KPIRating kpiRating) {
		Session session = getSessionFactory().getCurrentSession();
		logger.info("saving the kpirating");
		session.saveOrUpdate(kpiRating);
		logger.info("saved the kpirating");
	}
	
	@Override
	public List<KPIRating> getKPIRatingsForAnEmployee(Long employeeId, Long kpiId){
	    
	    Session session =getSessionFactory().getCurrentSession();
        Criteria criteria = session.createCriteria(KPIRating.class);
        
        criteria.createAlias("cycle", "cycle");
        
        criteria.setFetchMode("kpi", FetchMode.JOIN);
        criteria.add(Restrictions.eq("kpi.kpiId", kpiId));
        
        criteria.setFetchMode("employee", FetchMode.JOIN);
        criteria.add(Restrictions.eq("employee.employeeId", employeeId));
        
        criteria.setFetchMode("cycle", FetchMode.JOIN);
        
        criteria.add(Restrictions.eq("cycle.status", Status.IN_PROCESS.toString()));
        
        logger.info("kpi ratings for an employee :"+criteria.list());
	    return criteria.list();
	}

    @Override
    public List<KraWithWeightage> getKraWithWeightagesForADesignation(
            Long designationId) {
        
        Session session =getSessionFactory().getCurrentSession();
        Criteria criteria = session.createCriteria(KraWithWeightage.class);
        
        criteria.setFetchMode("designation", FetchMode.JOIN);
        criteria.add(Restrictions.eq("designation.designationKRAsId", designationId));
        
        return criteria.list();
    } 

}
