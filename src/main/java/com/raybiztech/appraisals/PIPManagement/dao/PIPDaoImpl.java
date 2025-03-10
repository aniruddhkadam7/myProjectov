package com.raybiztech.appraisals.PIPManagement.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.DateRange;

/**
 *
 * @author aprajita
 */

@Repository("pipDAOImpl")
public class PIPDaoImpl extends DAOImpl implements PIPDao {

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(PIPDaoImpl.class);

	/* To get all PIPList for admin */
	@Override
	public Map<String, Object> getAllPIPList(Integer startIndex,
			Integer endIndex, String multiSearch, String searchByEmployee,
			String searchByAdded, DateRange dateRange, String selectionStatus) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				PIP.class);
		criteria.createAlias("employee", "employee");
		if (searchByEmployee.equalsIgnoreCase("true")) {
			criteria.add(Restrictions.ilike("employee.employeeFullName",
					multiSearch, MatchMode.ANYWHERE));
		}

		if ("InActive".equalsIgnoreCase(selectionStatus)) 
		   {
            criteria.add(Restrictions
					.eq("employee.statusName", selectionStatus));
           }
		else{
 			
 			criteria.add(Restrictions
 					.eq("employee.statusName", "Active"));
 			
 	    	  if("PIP".equalsIgnoreCase(selectionStatus))
                 {
 	               criteria.add(Restrictions
 						.eq("PIPFlag", Boolean.TRUE));
 	        	
 				}
 	    	  
 	          if("Removed from PIP".equalsIgnoreCase(selectionStatus))
 	          
 	        	 {
 	        	criteria.add(Restrictions
 						.eq("PIPFlag", Boolean.FALSE));
 	        	 }
 	        	
 		     }
 	      
 		

		if (searchByAdded.equalsIgnoreCase("true")) {
			Criteria criteria2 = sessionFactory.getCurrentSession()
					.createCriteria(Employee.class);
			criteria2.add(Restrictions.ilike("employeeFullName", multiSearch,
					MatchMode.ANYWHERE));

			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("employeeId"));
			criteria2.setProjection(projections);
			criteria.add(Restrictions.in("createdBy", criteria2.list()));
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(Restrictions.or(
					Restrictions.between("startDate", dateRange.getMinimum(),
							dateRange.getMaximum()), Restrictions.between(
							"endDate", dateRange.getMinimum(),
							dateRange.getMaximum())), Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate",
							dateRange.getMinimum()), Restrictions.le("endDate",
							dateRange.getMaximum())), Restrictions.or(
							Restrictions.and(
									Restrictions.ge("startDate",
											dateRange.getMinimum()),
									Restrictions.le("endDate",
											dateRange.getMaximum())),
							Restrictions.and(
									Restrictions.le("startDate",
											dateRange.getMinimum()),
									Restrictions.ge("endDate",
											dateRange.getMaximum()))))));

		} else if (dateRange.getMinimum() != null
				&& dateRange.getMaximum() == null) {

			criteria.add(Restrictions.or(Restrictions.and(
					Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.isNull("startDate")), Restrictions.ge(
					"startDate", dateRange.getMinimum())));

		} else if (dateRange.getMinimum() == null
				&& dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(Restrictions.and(
					Restrictions.le("endDate", dateRange.getMaximum()),
					Restrictions.isNull("endDate")), Restrictions.le("endDate",
					dateRange.getMaximum())));
		}

		criteria.addOrder(Order.desc("id"));
		/*Integer noOfRecords = criteria.list().size();
		if(startIndex!=null && endIndex!=null){
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		}
		List<PIP> piplist = criteria.list();*/
		
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", criteriaMap.get("list"));
		map.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return map;
	}

	/* To get all PIPList for hierarchy wise */
	@Override
	public Map<String, Object> getAllHierarchyPIPList(List<Long> empId,
			Integer startIndex, Integer endIndex, String multiSearch,
			String searchByEmployee, String searchByAdded, DateRange dateRange,
			String selectionStatus) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				PIP.class);

		criteria.add(Restrictions.in("createdBy", empId));
		criteria.createAlias("employee", "employee");
		if (searchByEmployee.equalsIgnoreCase("true")) {
			criteria.add(Restrictions.ilike("employee.employeeFullName",
					multiSearch, MatchMode.ANYWHERE));
		}
		if ("InActive".equalsIgnoreCase(selectionStatus)) 
		   {
         criteria.add(Restrictions
					.eq("employee.statusName", selectionStatus));
        }
		else{
			
			criteria.add(Restrictions
					.eq("employee.statusName", "Active"));
			
	    	  if("PIP".equalsIgnoreCase(selectionStatus))
              {
	               criteria.add(Restrictions
						.eq("PIPFlag", Boolean.TRUE));
	        	
				}
	    	  
	          if("Removed from PIP".equalsIgnoreCase(selectionStatus))
	          
	        	 {
	        	criteria.add(Restrictions
						.eq("PIPFlag", Boolean.FALSE));
	        	 }
	        	
		     }
		
		if (searchByAdded.equalsIgnoreCase("true")) {
			Criteria criteria2 = sessionFactory.getCurrentSession()
					.createCriteria(Employee.class);
			criteria2.add(Restrictions.ilike("employeeFullName", multiSearch,
					MatchMode.ANYWHERE));

			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("employeeId"));
			criteria2.setProjection(projections);
			criteria.add(Restrictions.in("createdBy", criteria2.list()));

		}
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(Restrictions.or(
					Restrictions.between("startDate", dateRange.getMinimum(),
							dateRange.getMaximum()), Restrictions.between(
							"endDate", dateRange.getMinimum(),
							dateRange.getMaximum())), Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate",
							dateRange.getMinimum()), Restrictions.le("endDate",
							dateRange.getMaximum())), Restrictions.or(
							Restrictions.and(
									Restrictions.ge("startDate",
											dateRange.getMinimum()),
									Restrictions.le("endDate",
											dateRange.getMaximum())),
							Restrictions.and(
									Restrictions.le("startDate",
											dateRange.getMinimum()),
									Restrictions.ge("endDate",
											dateRange.getMaximum()))))));

		} else if (dateRange.getMinimum() != null
				&& dateRange.getMaximum() == null) {

			criteria.add(Restrictions.or(Restrictions.and(
					Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.isNull("startDate")), Restrictions.ge(
					"startDate", dateRange.getMinimum())));

		} else if (dateRange.getMinimum() == null
				&& dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(Restrictions.and(
					Restrictions.le("endDate", dateRange.getMaximum()),
					Restrictions.isNull("endDate")), Restrictions.le("endDate",
					dateRange.getMaximum())));
		}
		criteria.addOrder(Order.desc("id"));
		/*Integer noOfRecords = criteria.list().size();
		if(startIndex!=null && endIndex!=null){
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		}
		List<PIP> piplist = criteria.list();*/
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		Map<String, Object> hierarchymap = new HashMap<String, Object>();
		hierarchymap.put("list", criteriaMap.get("list"));
		hierarchymap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return hierarchymap;
	}

	@Override
	public Map<String, Object> getIndividualPIPList(Long empId,
			Integer startIndex, Integer endIndex) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				PIP.class);

		criteria.add(Restrictions.eq("employee.employeeId", empId));

		criteria.addOrder(Order.desc("id"));
		/*Integer noOfRecords = criteria.list().size();
		if(startIndex!=null && endIndex!=null){
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		}
		List<PIP> piplist = criteria.list();*/
		
		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);
		
		Map<String, Object> individualMap = new HashMap<String, Object>();
		individualMap.put("list", criteriaMap.get("list"));
		individualMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return individualMap;
	}

	/* to check the duplicate PIP details for the given time period */
	@Override
	public Boolean checkForDuplicatePIP(PIP pip, DateRange dateRange) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				PIP.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee", pip.getEmployee()));

		if (pip.getId() != null) {
			criteria.add(Restrictions.ne("id", pip.getId()));
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(Restrictions.or(
					Restrictions.between("startDate", dateRange.getMinimum(),
							dateRange.getMaximum()), Restrictions.between(
							"endDate", dateRange.getMinimum(),
							dateRange.getMaximum())), Restrictions.or(
					Restrictions.and(Restrictions.ge("startDate",
							dateRange.getMinimum()), Restrictions.le("endDate",
							dateRange.getMaximum())), Restrictions.or(
							Restrictions.and(
									Restrictions.ge("startDate",
											dateRange.getMinimum()),
									Restrictions.le("endDate",
											dateRange.getMaximum())),
							Restrictions.and(
									Restrictions.le("startDate",
											dateRange.getMinimum()),
									Restrictions.ge("endDate",
											dateRange.getMaximum()))))));

		} else if (dateRange.getMinimum() != null
				&& dateRange.getMaximum() == null) {

			criteria.add(Restrictions.or(Restrictions.and(
					Restrictions.ge("startDate", dateRange.getMinimum()),
					Restrictions.isNull("startDate")), Restrictions.ge(
					"startDate", dateRange.getMinimum())));

		} else if (dateRange.getMinimum() == null
				&& dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(Restrictions.and(
					Restrictions.le("endDate", dateRange.getMaximum()),
					Restrictions.isNull("endDate")), Restrictions.le("endDate",
					dateRange.getMaximum())));
		}

		if (criteria.list().isEmpty()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
}
