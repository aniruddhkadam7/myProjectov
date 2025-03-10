/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.employeeprofile.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.raybiztech.achievement.business.Achievement;
import com.raybiztech.achievement.business.AchievementType;
import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.EmployeeBankInformation;
import com.raybiztech.appraisals.business.Finance;
import com.raybiztech.appraisals.business.Qualification;
import com.raybiztech.appraisals.business.QualificationLookUp;
import com.raybiztech.appraisals.business.VisaDetails;
import com.raybiztech.appraisals.business.VisaLookUp;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.dto.QualificationDTO;
import com.raybiztech.appraisals.dto.QualificationLookUpDTO;
import com.raybiztech.appraisals.dto.VisaDetailDTO;
import com.raybiztech.assetmanagement.business.Asset;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.lookup.business.BankNameLookup;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.invoice.business.Remittance;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.projectmanagement.invoice.lookup.DiscountTypeLookup;
import com.raybiztech.projectmanagement.invoice.lookup.DurationLookup;
import com.raybiztech.projectmanagement.invoice.lookup.InvoiceStatusLookup;
import com.raybiztech.projectmanagement.invoice.lookup.PaymentTermLookup;
import com.raybiztech.projectmanagement.invoice.lookup.TaxTypeLookup;
import com.raybiztech.projectmanagement.invoice.utility.HibernateSupressWaringsUtil;
import com.raybiztech.projectmanagement.invoice.utility.InvoiceLookUpKeyConstants;
import com.raybiztech.recruitment.business.Holidays;
import com.raybiztech.recruitment.dao.JobPortalDAOImpl;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.sms.business.SmsTemplates;
import com.raybiztech.supportmanagement.business.TicketsCategory;

/**
 * 
 * @author naresh
 */
@Repository("employeeProfileDAOIMPL")
public class EmployeeProfileDAOIMPL extends DAOImpl implements EmployeeProfileDAOI {

	Logger logger = Logger.getLogger(EmployeeProfileDAOIMPL.class);

	public String convertStringDatetoMysqlDate(String start) {
		SimpleDateFormat startFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try {
			date = startFormat.parse(start);
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(JobPortalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		String mysqlString = toFormat.format(date);
		return mysqlString;
	}

	@Override
	public List<Holidays> getUpcomingHolidays(String country) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Holidays.class).
				add(Restrictions.eq("country", country));
		//criteria.add(Restrictions.ge("date", new Date()));
		List<Holidays> holidaysList = criteria.list();

		return holidaysList;
	}
	@Override
	public List<Holidays> getOnlyUpcomingHolidays(String country) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Holidays.class);
			criteria.add(Restrictions.ge("date", new Date()));
			criteria.add(Restrictions.eq("country", country));
		List<Holidays> holidaysList = criteria.list();

		return holidaysList;
	}


	@Override
	public Map<String, Object> getUpcomingBirthdayAnniversaries(Integer startIndex, Integer endIndex) {
		Map<String, Object> map = new HashMap<String, Object>();
		String whereCondition = "";
		whereCondition += " and statusName= 'Active'";
		whereCondition += " and realDob is not  null";
		whereCondition += " and  ((month(e.realDob)=month(:now) and day(e.realDob)>=day(:now)) or month(e.realDob)>month(:now)) order by  month(e.realDob) asc,day(e.realDob) asc";
		String hql = " Select e.firstName,e.lastName,e.realDob,e.thumbPicture from Employee e";
		if (!whereCondition.equalsIgnoreCase("")) {
			hql += " where 1=1" + whereCondition;
		}
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		query.setDate("now", new java.util.Date());
		map.put("size", query.list().size());
		query.setFirstResult(startIndex);
		query.setMaxResults(endIndex - startIndex);
		map.put("birthdays", query.list());
		return map;

	}

	@Override
	public List<Employee> getUpcomingMarriedAnniversaries() {
		String statusName = "Active";
		String whereCondition = "";
		whereCondition += " and statusName= '" + statusName + "'";
		whereCondition += " and marriageDate is not  null";
		whereCondition += " order by  month(e.marriageDate) asc,day(e.marriageDate) asc";
		String hql = " Select e from Employee e";
		if (!whereCondition.equalsIgnoreCase("")) {
			hql += " where 1=1" + whereCondition;
		}
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		List<Employee> list = query.list();
		return list;
	}

	@Override
	public Boolean isemployeeUsernameexist(String userName) {
		Boolean flag = true;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Employee.class)
				.add(Restrictions.eq("userName", userName).ignoreCase());
		if (criteria.list().isEmpty()) {
			flag = false;
		}
		return flag;
	}

	@Override
	public Set<Holidays> getHolidays(DateRange month) {
		// TODO Auto-generated method stub
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Holidays.class);
		criteria.add(Restrictions.between("date", month.getMinimum(), month.getMaximum()));
		criteria.add(Restrictions.ne("country", "NULL"));
		System.out.println("list:" + criteria.list().size());
		return new HashSet<Holidays>(criteria.list());
	}

	@Override
	public Map<String, Object> getUpcomingProvisionalPeriod(Employee employee, Integer probationaryPeriod,
			Integer startIndex, Integer endIndex) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<Object[]> list = null;
		Integer size = 0;
		Permission probationaryEndDates = checkForPermission("ProbationaryEndDates", employee);
		Permission hierarchyProbationaryEndDates = checkForPermission("Hierarchy ProbationaryEndDates", employee);
		// if (employee.getRole().equalsIgnoreCase("admin")) {
		if (probationaryEndDates.getView() && !hierarchyProbationaryEndDates.getView()) {
			String sqlQuery = "SELECT `FirstName`,`LastName`,TIMESTAMPDIFF(MONTH,JoiningDate,now()),`JoiningDate`,DATE_ADD(JoiningDate, INTERVAL "
					+ probationaryPeriod + " MONTH) FROM `EMPLOYEE` WHERE TIMESTAMPDIFF(MONTH,JoiningDate,now())<= "
					+ probationaryPeriod + " and DATE_ADD(JoiningDate, INTERVAL " + probationaryPeriod
					+ " MONTH)>=now() and StatusName='Active' order by DATE_ADD(JoiningDate, INTERVAL "
					+ probationaryPeriod + " MONTH) asc";
			Query query = getSessionFactory().getCurrentSession().createSQLQuery(sqlQuery);
			size = query.list().size();
			query.setFirstResult(startIndex);
			if (endIndex >= size)
				endIndex = size;
			query.setMaxResults(endIndex);
			list = query.list();
		}

		// if (employee.getRole().equalsIgnoreCase("Manager")) {
		else if (probationaryEndDates.getView() && hierarchyProbationaryEndDates.getView()) {
			String sqlQuery = "SELECT `FirstName`,`LastName`,TIMESTAMPDIFF(MONTH,JoiningDate,now()),`JoiningDate`,DATE_ADD(JoiningDate, INTERVAL "
					+ probationaryPeriod + " MONTH) FROM `EMPLOYEE` WHERE TIMESTAMPDIFF(MONTH,JoiningDate,now())<= "
					+ probationaryPeriod + " and DATE_ADD(JoiningDate, INTERVAL " + probationaryPeriod
					+ " MONTH)>=now() and StatusName='Active' and manager=" + employee.getEmployeeId()
					+ " order by DATE_ADD(JoiningDate, INTERVAL " + probationaryPeriod + " MONTH) asc";
			Query query = getSessionFactory().getCurrentSession().createSQLQuery(sqlQuery);
			size = query.list().size();
			query.setFirstResult(startIndex);
			if (endIndex >= size)
				endIndex = size;
			query.setMaxResults(endIndex);
			list = query.list();
		}

		map.put("size", size);
		map.put("list", list);
		return map;
	}

	@Override
	public Finance getEmplopyeeFinanceInfo(Long empId) {

		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Finance.class);

		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", empId));
		return (Finance) criteria.uniqueResult();
	}

	@Override
	public void updateEmplopyeeFinanceInfo(Long empId) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(empId);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BankNameLookup> getBankNameList() {

		return sessionFactory.getCurrentSession().createCriteria(BankNameLookup.class).list();

	}

	@Override
	public Boolean isHolidayDateExist(Date holidaydate,String country) {
		// TODO Auto-generated method stub
		Boolean flag = false;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Holidays.class);
		criteria.add(Restrictions.eq("date", holidaydate));
        criteria.add(Restrictions.eq("country", country));
		if (criteria.list().isEmpty()) {
			flag = false;
		} else {
			flag = true;
		}
		return flag;

	}

	@Override
	public Boolean isHolidayDateExistUpdate(Date holidaydate,String country) {

		Boolean flag = false;
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Holidays.class);
		criteria.add(Restrictions.eq("date", holidaydate));
        criteria.add(Restrictions.eq("country", country));
		if (criteria.list().size() <= 1 || criteria.list().isEmpty()) {
			flag = false;
		} else {
			flag = true;
		}
		return flag;

	}

	@Override
	public List<QualificationLookUp> getQualiactionCategoryList() {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(QualificationLookUp.class);
		return criteria.list();

	}

	@Override
	public Qualification getAllQualification(Long empId) {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Qualification.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", empId));
		return (Qualification) criteria.uniqueResult();

	}

	@Override
	public Boolean checkForDuplicate(QualificationLookUpDTO qualificationLookUpDTO) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(QualificationLookUp.class);
		criteria.add(Restrictions.eq("qualificationName", qualificationLookUpDTO.getQualificationName()));
		return criteria.list().isEmpty() ? Boolean.FALSE : Boolean.TRUE;

	}

	@Override
	public Boolean checkForUse(Long id) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Qualification.class);
		criteria.add(Restrictions.ilike("qualificationDetails", id.toString(), MatchMode.ANYWHERE));

		return criteria.list().isEmpty() ? Boolean.FALSE : Boolean.TRUE;
	}

	@Override
	public List<Employee> getBirthdayEmployees() {

		String hql = "from Employee e where statusName= 'Active' and realDob is not null and (month(e.realDob)=month(:now) and day(e.realDob)=day(:now))";
		Query query = getSessionFactory().getCurrentSession().createQuery(hql);
		query.setDate("now", new java.util.Date());
		return query.list();

	}

	@Override
	public String getSMSTemplate() {
		SmsTemplates smsTemplates = (SmsTemplates) sessionFactory.getCurrentSession().createCriteria(SmsTemplates.class)
				.add(Restrictions.eq("type", "Birthday Wishes")).uniqueResult();
		return smsTemplates.getTemplate();
	}

	@Override
	public Map<String, List<Audit>> getEmployeeDetailAudit(Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Audit.class);
		criteria.add(Restrictions.eq("referenceId", employeeId));
		criteria.add(Restrictions.or(Restrictions.ilike("tableName", "EMPLOYEE"),
				Restrictions.ilike("tableName", "EMPLOYEE_FAMILYDETAILS")));
		List<Audit> audits = criteria.list();
		Map<String, List<Audit>> map = null;
		if (!audits.isEmpty())
			map = getPairValue(audits);
		return map;
	}

	@Override
	public Map<String, Object> getFinanceDetailsList(Integer startIndex, Integer endIndex, String employeeName) {
		
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Finance.class);
		criteria.createAlias("employee", "employee");
		/*criteria.add(Restrictions.eq("employee.statusName", "Active"));*/
		criteria.add(Restrictions.ilike("employee.employeeFullName", employeeName, MatchMode.ANYWHERE));
		/*
		 * Integer noOfRecords = criteria.list().size();
		 * 
		 * if (startIndex != null && endIndex != null) {
		 * criteria.setFirstResult(startIndex); criteria.setMaxResults(endIndex -
		 * startIndex); }
		 * 
		 * List<Finance> financeList = criteria.list();
		 */

		Map<String, Object> criteriaMap = getPaginationList(criteria, startIndex, endIndex);

		Map<String, Object> financeMap = new HashMap<String, Object>();
		financeMap.put("list", criteriaMap.get("list"));
		financeMap.put("size", Integer.parseInt((criteriaMap.get("listSize").toString())));

		return financeMap;
	}

	@Override
	public Map<String, Object> getCountryLookUps() {

		Map<String, Object> map = new HashMap<String, Object>();

		List<CountryLookUp> countries = HibernateSupressWaringsUtil
				.listAndCast(getSessionFactory().getCurrentSession().createCriteria(CountryLookUp.class));

		map.put(InvoiceLookUpKeyConstants.COUNTRIES, countries);

		return map;
	}

	@Override
	public List<VisaLookUp> getCountryChangeList(Long id) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VisaLookUp.class);
		criteria.createAlias("country", "country");
		criteria.add(Restrictions.eq("country.id", Integer.parseInt(id.toString())));
		return criteria.list();

	}

	@Override
	public List<VisaDetails> getEmployeeVisaDetailsList(Long loggedInEmpId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VisaDetails.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", loggedInEmpId));
		return criteria.list();
	}

	@Override
	public Boolean checkForDuplicateVisa(VisaDetailDTO visaDetailDTO, DateRange dateRange) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VisaDetails.class);
		criteria.createAlias("employee", "emp");
		criteria.add(Restrictions.eq("emp.employeeId",visaDetailDTO.getEmpId()));
		criteria.createAlias("visaLookUp", "visaLookUp");
		criteria.createAlias("visaLookUp.country", "country");
		criteria.add(Restrictions.eq("country.id", Integer.parseInt(visaDetailDTO.getCountryId().toString())));
		
		// criteria.add(Restrictions.eq("visaLookUp.id",
		// visaDetailDTO.getVisaTypeId()));
		/*
		 * try {
		 * 
		 * Date issueDate = DateParser.toDate(visaDetailDTO.getDateOfIssue()); Date
		 * expireDate = DateParser.toDate(visaDetailDTO.getDateOfExpire());
		 * 
		 * criteria.add(Restrictions.between("dateOfIssue", issueDate, expireDate));
		 * criteria.add(Restrictions.between("dateOfExpire", issueDate, expireDate));
		 * criteria.add(Restrictions.eq("dateOfIssue", issueDate));
		 * criteria.add(Restrictions.eq("dateOfExpire", expireDate)); } catch
		 * (ParseException e) { e.printStackTrace(); }
		 */

		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(
					Restrictions.or(Restrictions.between("dateOfIssue", dateRange.getMinimum(), dateRange.getMaximum()),
							Restrictions.between("dateOfExpire", dateRange.getMinimum(), dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("dateOfIssue", dateRange.getMinimum()),
									Restrictions.le("dateOfExpire", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("dateOfIssue", dateRange.getMinimum()),
											Restrictions.le("dateOfExpire", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("dateOfIssue", dateRange.getMinimum()),
											Restrictions.ge("dateOfExpire", dateRange.getMaximum()))))));

		} else if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {

			criteria.add(Restrictions.or(
					Restrictions.and(Restrictions.ge("dateOfIssue", dateRange.getMinimum()),
							Restrictions.isNull("dateOfIssue")),
					Restrictions.ge("dateOfIssue", dateRange.getMinimum())));

		} else if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(
					Restrictions.and(Restrictions.le("dateOfExpire", dateRange.getMaximum()),
							Restrictions.isNull("dateOfExpire")),
					Restrictions.le("dateOfExpire", dateRange.getMaximum())));
		}

		return criteria.list().isEmpty() ? Boolean.FALSE : Boolean.TRUE;

	}

	@Override
	public Boolean checkForDuplicateVisaForEdit(VisaDetailDTO editVisaDetailsDTO, DateRange dateRange) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(VisaDetails.class);
		criteria.createAlias("visaLookUp", "visaLookUp");
		criteria.createAlias("visaLookUp.country", "country");
		//criteria.createAlias("employee", "");
		criteria.createAlias("employee", "emp");
		criteria.add(Restrictions.ne("id", editVisaDetailsDTO.getId()));
		criteria.add(Restrictions.eq("emp.employeeId", editVisaDetailsDTO.getEmpId()));
		criteria.add(Restrictions.eq("country.id", Integer.parseInt(editVisaDetailsDTO.getCountryId().toString())));
		// criteria.add(Restrictions.eq("visaLookUp.id",
		// editVisaDetailsDTO.getVisaTypeId()));
		/*
		 * try {
		 * 
		 * Date issueDate = DateParser.toDate(editVisaDetailsDTO.getDateOfIssue()); Date
		 * expireDate = DateParser.toDate(editVisaDetailsDTO.getDateOfExpire());
		 * 
		 * criteria.add(Restrictions.eq("dateOfIssue", issueDate));
		 * criteria.add(Restrictions.eq("dateOfExpire", expireDate));
		 * 
		 * criteria.add(Restrictions.between("dateOfIssue", issueDate, expireDate));
		 * criteria.add(Restrictions.between("dateOfExpire", issueDate, expireDate)); }
		 * catch (ParseException e) { e.printStackTrace(); }
		 */
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(
					Restrictions.or(Restrictions.between("dateOfIssue", dateRange.getMinimum(), dateRange.getMaximum()),
							Restrictions.between("dateOfExpire", dateRange.getMinimum(), dateRange.getMaximum())),
					Restrictions.or(
							Restrictions.and(Restrictions.ge("dateOfIssue", dateRange.getMinimum()),
									Restrictions.le("dateOfExpire", dateRange.getMaximum())),
							Restrictions.or(
									Restrictions.and(Restrictions.ge("dateOfIssue", dateRange.getMinimum()),
											Restrictions.le("dateOfExpire", dateRange.getMaximum())),
									Restrictions.and(Restrictions.le("dateOfIssue", dateRange.getMinimum()),
											Restrictions.ge("dateOfExpire", dateRange.getMaximum()))))));

		} else if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {

			criteria.add(Restrictions.or(
					Restrictions.and(Restrictions.ge("dateOfIssue", dateRange.getMinimum()),
							Restrictions.isNull("dateOfIssue")),
					Restrictions.ge("dateOfIssue", dateRange.getMinimum())));

		} else if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(
					Restrictions.and(Restrictions.le("dateOfExpire", dateRange.getMaximum()),
							Restrictions.isNull("dateOfExpire")),
					Restrictions.le("dateOfExpire", dateRange.getMaximum())));
		}
		return criteria.list().isEmpty() ? Boolean.FALSE : Boolean.TRUE;
	}
}
