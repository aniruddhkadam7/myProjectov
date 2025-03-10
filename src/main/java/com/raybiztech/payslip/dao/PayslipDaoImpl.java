package com.raybiztech.payslip.dao;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.payslip.business.Payslip;
import com.raybiztech.payslip.exceptions.PayslipDoesNotExistException;
import com.raybiztech.payslip.utility.ClearDirectory;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.Milestone;

@Repository("payslipDaoImpl")
public class PayslipDaoImpl implements PayslipDao {

	Logger logger = Logger.getLogger(PayslipDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	ClearDirectory clearDirectory;

	@Override
	public void save(List<Payslip> list, String Month, String Year)
			throws IOException {

		for (Payslip payslip : list) {
			sessionFactory.getCurrentSession().save(payslip);

		}

		clearDirectory.clearPayslipDirectory();
	}

	@Override
	public Boolean checkDataExistsForMonthAndYear(String month, String year) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Payslip.class);

		@SuppressWarnings("unchecked")
		List<Payslip> list = criteria.add(
				Restrictions.and(Restrictions.eq("year", year),
						Restrictions.eq("month", month))).list();

		if (!list.isEmpty()) {

			return true;

		}
		return false;
	}

	// generate payslips

	@SuppressWarnings("unchecked")
	public Map<String, Object> getAllPayslipByMonthOfYear(String month,
			String year, int startIndex, int endIndex) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Payslip.class);
		criteria.createAlias("employee", "employee");

		criteria.add(Restrictions.eq("month", month).ignoreCase());

		criteria.add(Restrictions.eq("year", year));

		criteria.addOrder(Order.asc("employee.employeeId"));
		Integer totalRecords = criteria.list().size();

		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		List<Payslip> payslip = criteria.list();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("size", totalRecords);
		map.put("list", payslip);
		return map;
	}

	@Override
	public void deleteCheckedPayslips(List<Long> id) {

		Query query = sessionFactory.getCurrentSession().createQuery(
				"delete from Payslip where paySlipId in (:paySlipId)");
		query.setParameterList("paySlipId", id);
		query.executeUpdate();

	}

	@Override
	public List<Payslip> getallPayslip(String month, String year) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Payslip.class);

		@SuppressWarnings("unchecked")
		List<Payslip> list = criteria.add(
				Restrictions.and(Restrictions.eq("month", month).ignoreCase(),
						Restrictions.eq("year", year))).list();

		return list;

	}

	@Override
	public Boolean checkPayslipExist(String month, String year, String empId) {

		String fileName = empId + "_" + month + "_" + year + ".pdf";

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Payslip.class);
		@SuppressWarnings("unchecked")
		List<Payslip> payslipList = criteria.add(
				Restrictions.like("payslipFileName", fileName)).list();

		if (!payslipList.isEmpty()) {
			return true;
		} else {
			throw new PayslipDoesNotExistException("payslip doesn't Exist ");
		}

	}

	@Override
	public List<Object[]> getEmployeePayslipsForSelectedYear(String empid,
			String year) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Payslip.class);

		criteria.createAlias("employee", "employee");

		criteria.add(Restrictions.eq("employee.employeeId",
				Long.parseLong(empid)));
		criteria.add(Restrictions.like("year", year));
		criteria.add(Restrictions.eq("status", Boolean.TRUE));

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("employee.employeeId"));
		projectionList.add(Projections.property("month"));
		projectionList.add(Projections.property("year"));

		criteria.setProjection(projectionList);

		List<Object[]> rows = criteria.list();

		return rows;
	}

	@Override
	public List<Payslip> getPayslipDataForViewToEmployee(String empid,
			String month, String year) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Payslip.class);

		criteria.createAlias("employee", "employee");

		criteria.add(Restrictions.eq("employee.employeeId",
				Long.parseLong(empid)));
		criteria.add(Restrictions.eq("month", month));
		criteria.add(Restrictions.eq("year", year));

		return criteria.list();
	}

	@Override
	public Map<String, Object> searchEmployeeDetails(String searchString,
			String month, String year, Integer startIndex, Integer endIndex) {

		Query query = null;
		try {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Payslip ps where ps.employee.employeeId = :empId and ps.month =:month and ps.year=:year order by ps.employee.employeeId");
			query.setLong("empId", Long.parseLong(searchString));
			query.setString("month", month);
			query.setString("year", year);

		} catch (NumberFormatException nfe) {

			query = sessionFactory
					.getCurrentSession()
					.createQuery(
							"from Payslip ps where concat(ps.employee.firstName, ' ', ps.employee.lastName) like :fullName and ps.month =:month and ps.year=:year order by ps.employee.employeeId");
			query.setString("fullName", "%" + searchString + "%");
			query.setString("month", month);
			query.setString("year", year);

		}

		Integer totalRecords = query.list().size();

		logger.info("Total no of Records are:DAO  ############ " + totalRecords);

		query.setFirstResult(startIndex);
		query.setMaxResults(endIndex - startIndex);
		List<Payslip> payslip = query.list();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("size", totalRecords);
		map.put("list", payslip);
		return map;
	}

	@Override
	public Payslip checkDataExistForMonthYearAndEmployeeId(String month,
			String year, Long Empid) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Payslip.class);
		criteria.createAlias("employee", "employee");

		criteria.add(Restrictions.eq("employee.employeeId", Empid));
		criteria.add(Restrictions.eq("month", month).ignoreCase());
		criteria.add(Restrictions.eq("year", year).ignoreCase());

		Payslip payslip = (Payslip) criteria.uniqueResult();
		
		return payslip;

	}

	@Override
	public Audit getlatestDesignationForEmployee(Long employeeId) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Audit.class);
		criteria.add(Restrictions.eq("referenceId", employeeId));
		criteria.add(Restrictions.eq("tableName", "EMPLOYEE"));
		criteria.add(Restrictions.eq("columnName", "designation"));
		criteria.addOrder(Order.desc("id"));
		criteria.setMaxResults(1);
		return (Audit) criteria.uniqueResult();
		
		
		
	}

}
