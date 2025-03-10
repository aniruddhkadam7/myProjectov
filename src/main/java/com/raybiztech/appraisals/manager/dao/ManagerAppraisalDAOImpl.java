package com.raybiztech.appraisals.manager.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.builder.KRABuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.dto.AppraisalDTO;

@Component("managerAppraisalDAO")
public class ManagerAppraisalDAOImpl extends DAOImpl implements
		ManagerAppraisalDAO {
	@Autowired
	KRABuilder kraBuilder;
	@Autowired
	DAO dao;
	@Autowired
	EmployeeBuilder employeeBuilder;

	public static Logger logger = Logger
			.getLogger(ManagerAppraisalDAOImpl.class);

	@Override
	public void saveKPIRatingTOKPIManagerAppraisalData(AppraisalDTO dto) {
		/*
		 * Employee employee = (Employee)
		 * getSessionFactory().getCurrentSession() .get(Employee.class,
		 * dto.getId());
		 * 
		 * // get digignationKras oject from employee DesignationKras
		 * designationKras = employee.getDesignationKras();
		 * 
		 * Map<KRA, Double> krasWithWeitage = new HashMap<KRA, Double>();
		 * 
		 * Iterator it = kraDto.entrySet().iterator(); while (it.hasNext()) {
		 * 
		 * Map.Entry pairs = (Map.Entry) it.next();
		 * 
		 * // get KRA object to convert KRADTO to KRA. KRA kra =
		 * kraBuilder.createKRAEntity((KRADTO) pairs.getKey());
		 * 
		 * // put KRA and weitage to specific map. krasWithWeitage.put(kra,
		 * (Double) pairs.getValue()); } // set kra and weightage to
		 * designationKras. designationKras.setKrasWithWeitage(krasWithWeitage);
		 * 
		 * // set designationKras to Employee.
		 * employee.setDesignationKras(designationKras);
		 * 
		 * // save or update Employee.
		 * getSessionFactory().getCurrentSession().saveOrUpdate(employee);
		 */
	}

	@Override
	public List<Employee> getEmployees(List<Long> managerEmployeeId) {
		// TODO Auto-generated method stub

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.createAlias("manager", "mgr");
		criteria.add(Restrictions.in("mgr.employeeId", managerEmployeeId));
		criteria.add(Restrictions.eq("statusName", "Active"));
		List<Employee> employeesList = criteria.list();
		logger.info("employee  list is " + employeesList);

		return employeesList;
	}

	@Override
	public List<Employee> getInActiveEmployees(Long managerId) {
		// TODO Auto-generated method stub
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.createAlias("manager", "mgr");
		// criteria.setFetchMode("mgr", FetchMode.JOIN);
		criteria.add(Restrictions.eq("mgr.employeeId", managerId));
		criteria.add(Restrictions.eq("statusName", "InActive"));
		List<Employee> employeesList = criteria.list();
		logger.info("InActive employee  list is " + employeesList);

		return employeesList;
	}

	@Override
	public List<Employee> getReporteesIncludingManager(Long managerId) {
		// TODO Auto-generated method stub

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.createAlias("manager", "mgr");
		criteria.add(Restrictions.eq("statusName", "Active"));
		Criterion criterion = Restrictions.eq("mgr.employeeId", managerId);
		Criterion criterion2 = Restrictions.eq("employeeId", managerId);
		criteria.add(Restrictions.or(criterion, criterion2));
		List<Employee> employeesList = criteria.list();

		return employeesList;
	}
}
