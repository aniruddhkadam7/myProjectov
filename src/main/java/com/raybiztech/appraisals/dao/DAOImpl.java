/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.achievement.business.Leadership;
import com.raybiztech.appraisalmanagement.business.FormStatus;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Finance;
import com.raybiztech.date.Date;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.Duration;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.TimeUnit;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.rolefeature.business.Feature;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.rolefeature.business.Role;
import com.raybiztech.rolefeature.business.URIAndFeatures;

@Component("dao")
@Transactional
public class DAOImpl implements DAO {

	@Autowired
	public SessionFactory sessionFactory;
	
	@Autowired
	public SessionFactory sessionFactory1;

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@Override
	public <T extends Serializable> Serializable save(T object) {

		return sessionFactory.getCurrentSession().save(object);
	}

	@Override
	public <T extends Serializable> void saveOrUpdate(T object) {

		sessionFactory.getCurrentSession().saveOrUpdate(object);
	}

	@Override
	public <T extends Serializable> void update(T object) {
		sessionFactory.getCurrentSession().update(object);
	}

	@Override
	public <T extends Serializable> T findByKRAName(Class<T> clazz,
			Serializable name) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("name", name)).uniqueResult();
	}

	@Override
	public <T extends Serializable> T findByKPIName(Class<T> clazz,
			Serializable name) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("kpiName", name)).uniqueResult();
	}

	@Override
	public <T extends Serializable> T findBy(Class<T> clazz, Serializable id) {
		return (T) sessionFactory.getCurrentSession().get(clazz, id);

	}

	@Override
	public <T extends Serializable> List<T> get(Class<T> clazz) {
		return sessionFactory.getCurrentSession().createCriteria(clazz).list();
	}

	@Override
	public <T extends Serializable> void delete(T object) {
		sessionFactory.getCurrentSession().delete(object);
	}

	@Override
	public <T extends Serializable> T findByDesignationName(Class<T> clazz,
			Serializable name) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("designationName", name)).uniqueResult();
	}

	@Override
	public <T extends Serializable> T findByEmployeeMailId(Class<T> clazz,
			String name) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("email", name)).uniqueResult();
	}

	@Override
	public <T> List<String> getByProperty(Class<T> clazz, String name) {
		return (List<String>) sessionFactory.getCurrentSession()
				.createCriteria(clazz)
				.setProjection(Projections.property(name)).list();
	}

	/*
	 * @Override public <T extends Serializable> T findByEmailName(Class<T>
	 * clazz, Serializable name) { return (T)
	 * sessionFactory.getCurrentSession().createCriteria(clazz)
	 * .add(Restrictions.eq("email", name)).uniqueResult(); }
	 */
	@Override
	public <T extends Serializable> List<T> findByManagerName(Class<T> clazz) {

		Criteria rolesList = sessionFactory.getCurrentSession()
				.createCriteria(Role.class)
				.add(Restrictions.eq("reportingMangerFlag", true));

		return sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.in("empRole", rolesList.list()))
				.add(Restrictions.ne("employeeId", 1000L))
				.add(Restrictions.eq("statusName", "Active")).list();
		// .add(Restrictions.or(Restrictions.eq("role", "Manager"),
		// Restrictions.eq("role", "admin")))

	}

	@Override
	public <T extends Serializable> T findByCategoryTypeName(Class<T> clazz,
			Serializable name) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("categoryType", name)).uniqueResult();
	}

	@Override
	public <T extends Serializable> List<T> findCandidateInterviewCycles(
			Class<T> clazz, Serializable id) {
		return sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("candidateId", id)).list();
	}

	@Override
	public <T extends Serializable> T findByPageName(Class<T> clazz,
			Serializable name) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("pageName", name)).uniqueResult();
	}

	@Override
	public <T extends Serializable> T findByEmployeeName(Class<T> clazz,
			String username) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("userName", username)).uniqueResult();
	}

	@Override
	public <T extends Serializable> T findByName(Class<T> clazz,
			Serializable name) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("name", name)).uniqueResult();
	}

	@Override
	public <T extends Serializable> T findByActiveEmployeeName(Class<T> clazz,
			String username) {
		return (T) sessionFactory
				.getCurrentSession()
				.createCriteria(clazz)
				.add(Restrictions.eq("userName", username))
				.add(Restrictions.or(Restrictions.eq("statusName", "Active"),
						Restrictions.eq("statusName", "underNotice")))
				.uniqueResult();
		// .add(Restrictions.eq("statusName", "Active")).uniqueResult();
	}

	@Override
	public <T extends Serializable> T findByJobVacancyByCode(Class<T> clazz,
			String jobCode) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq("jobCode", jobCode)).uniqueResult();
	}

	@Override
	public <T extends Serializable> T findByUniqueProperty(Class<T> clazz,
			String propertyName, Serializable name) {
		return (T) sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq(propertyName, name)).uniqueResult();
	}

	@Override
	public <T extends Serializable> List<T> getAllOfProperty(Class<T> clazz,
			String propertyName, Serializable name) {
		return sessionFactory.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq(propertyName, name)).list();
	}

	@Override
	public List<Long> getRolePermissions(Long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Permission.class);
		criteria.createAlias("role", "role");
		criteria.createAlias("feature", "feature");
		criteria.add(Restrictions.eq("role.roleId", id));
		criteria.setProjection(Projections.distinct(Projections
				.property("feature.featureId")));

		List<Long> idList = criteria.list();

		return idList;
	}

	@Override
	public List<URIAndFeatures> getUrlId(String url, String method) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				URIAndFeatures.class);
		criteria.add(Restrictions.eq("featureUrl", url));
		criteria.add(Restrictions.eq("urlMethod", method));
		return criteria.list();
	}

	@Override
	public Permission getPermissionType(Long id, Long uriid) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Permission.class);
		criteria.createAlias("role", "role");
		criteria.createAlias("feature", "feature");
		criteria.add(Restrictions.eq("role.roleId", id));
		criteria.add(Restrictions.eq("feature.featureId", uriid));

		return (Permission) criteria.uniqueResult();
	}

	@Override
	public List<CandidateInterviewCycle> getAllInterviewCylesUnderEmployee(
			Long employeeId) {

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(CandidateInterviewCycle.class);
		criteria.createCriteria("employeeList", "employeeList");
		criteria.add(Restrictions.eq("employeeList.employeeId", employeeId));
		return criteria.list();
	}

	@Override
	public Map<String, Date> getCustomDates(String datePeriod) {
		Date startDate = null;
		Date endDate = null;
		Map<String, Date> map = new HashMap<String, Date>();

		if (datePeriod.equals("Today")) {
			startDate = new Date();
			endDate = new Date();

		} else if (datePeriod.equals("Yesterday")) {
			startDate = new Date().previous();
			endDate = new Date().previous();
		} else if (datePeriod.equalsIgnoreCase("This week")) {
			startDate = getFirstDayOfWeek();
			endDate = startDate.shift(new Duration(
					com.raybiztech.date.TimeUnit.DAY, +6));

		} else if (datePeriod.equalsIgnoreCase("Last week")) {
			Date currentWeekStart = getFirstDayOfWeek();
			startDate = currentWeekStart.shift(new Duration(
					com.raybiztech.date.TimeUnit.DAY, -7));

			Date lastWeekStart = currentWeekStart.shift(new Duration(
					com.raybiztech.date.TimeUnit.DAY, -7));
			endDate = lastWeekStart.shift(new Duration(
					com.raybiztech.date.TimeUnit.DAY, 6));

		} else if (datePeriod.equalsIgnoreCase("Last month")) {

			Date date = new Date(
					DayOfMonth.valueOf(1),
					MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

			endDate = date.shift(new Duration(com.raybiztech.date.TimeUnit.DAY,
					-1));

			startDate = new Date(DayOfMonth.valueOf(1),
					MonthOfYear.valueOf(endDate.getMonthOfYear().getValue()),
					YearOfEra.valueOf(endDate.getYearOfEra().getValue()));

		} else if (datePeriod.equalsIgnoreCase("Current Month")) {

			startDate = new Date(
					DayOfMonth.valueOf(1),
					MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

			Calendar c = Calendar.getInstance();
			int monthMaxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);

			endDate = new Date(
					DayOfMonth.valueOf(monthMaxDays),
					MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

		} else if (datePeriod.equalsIgnoreCase("Current Year")) {

			startDate = new Date(DayOfMonth.valueOf(1), MonthOfYear.valueOf(0),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

			endDate = new Date(DayOfMonth.valueOf(31), MonthOfYear.valueOf(11),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

		} else if (datePeriod.equalsIgnoreCase("Last Year")) {

			startDate = new Date(DayOfMonth.valueOf(1), MonthOfYear.valueOf(0),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()))
					.shift(new Duration(TimeUnit.YEAR, -1));

			endDate = new Date(DayOfMonth.valueOf(31), MonthOfYear.valueOf(11),
					YearOfEra.valueOf(new Date().getYearOfEra().getValue()))
					.shift(new Duration(TimeUnit.YEAR, -1));

		}
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

	Date getFirstDayOfWeek() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date.getJavaDate());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
		c.add(Calendar.DAY_OF_MONTH, -dayOfWeek);

		return new Date(c.getTimeInMillis());
	}

	@Override
	public Permission checkForPermission(String featureName, Employee employee) {

		Criteria permissionCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Permission.class);
		permissionCriteria.add(Restrictions.eq("role", employee.getEmpRole()));
		permissionCriteria.createAlias("feature", "feature");
		permissionCriteria.add(Restrictions.eq("feature.name", featureName));

		return (Permission) permissionCriteria.uniqueResult();
	}

	@Override
	public List<Feature> getChildFeatures(Long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Feature.class);
		criteria.createAlias("referenceFeature", "feature");
		criteria.add(Restrictions.eq("feature.featureId", id));
		return criteria.list();
	}

	@Override
	public Map<String, List<Audit>> getPairValue(List<Audit> list) {
		Map<String, List<Audit>> mapOfAudits = new HashMap<String, List<Audit>>();
		List<Audit> audits = new ArrayList<Audit>();
		Audit audit = list.get(0);
		audits.add(audit);
		mapOfAudits.put(audit.getModifiedDate().toString(), audits);
		list.remove(0);
		for (Audit audit1 : list) {
			if (mapOfAudits.get(audit1.getModifiedDate().toString()) == null) {
				List<Audit> newAuditList = new ArrayList<Audit>();
				newAuditList.add(audit1);
				mapOfAudits.put(audit1.getModifiedDate().toString(),
						newAuditList);
			} else {
				List<Audit> tempList = mapOfAudits.get(audit1.getModifiedDate()
						.toString());
				tempList.add(audit1);
				mapOfAudits.put(audit1.getModifiedDate().toString(), tempList);
			}

		}

		return mapOfAudits;
	}

	@Override
	public Map<String, List<Audit>> getAudit(Long id, String tableName) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Audit.class);
		criteria.add(Restrictions.eq("referenceId", id));
		criteria.add(Restrictions.ilike("tableName", tableName));

		List<Audit> audits = criteria.list();
		// List<Audit> lists=new ArrayList<Audit>();
		Map<String, List<Audit>> map = null;
		if (!audits.isEmpty())
			map = getPairValue(audits);
		return map;
	}

	// Returns all the Manager who are reporting to loggedin Employee
	@Override
	public List<Employee> mangerUnderManager(Long empId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Employee.class);
		criteria.createAlias("manager", "employee");
		criteria.createAlias("empRole", "empRole");
		criteria.add(Restrictions.eq("employee.employeeId", empId));
		// .add(Restrictions.eq("role", "Manager"));
		criteria.add(Restrictions.eq("empRole.reportingMangerFlag", true));
		return criteria.list();
	}

	// Will return all the Pending Approval entity for employee and his recently
	// changed manager combination;

	// **In DB The approval property value must be like "Pending Approval" with
	// case sensitive if not Approved
	// and createdBy dateType must be Long.
	@Override
	public <T extends Serializable> List<T> updateHierarchyReportingManager(
			String oldmanager, String employee, Class<T> clazz,
			String approvalStatus, String managesList, String createdBy) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(clazz)
				.add(Restrictions.eq(approvalStatus, "Pending Approval"))
				.add(Restrictions.ilike(managesList, "%" + oldmanager));
		Criterion criterion1 = Restrictions.ilike(managesList, "%" + employee
				+ "," + oldmanager);
		Criterion criterion2 = Restrictions.and(
				Restrictions.ilike(managesList, oldmanager),
				Restrictions.eq(createdBy, Long.parseLong(employee)));
		Criterion criterion = Restrictions.or(criterion1, criterion2);
		criteria.add(criterion);
		return criteria.list();
	}

	@Override
	public <T extends Serializable> List<T> updateHierarchyReportingManagerForAppraisal(
			String oldmanager, String employee, Class<T> clazz,
			String managesList) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				clazz);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.or(
				Restrictions.eq("formStatus", FormStatus.PENDING),
				Restrictions.eq("formStatus", FormStatus.SUBMIT)));
		Criterion criterion1 = Restrictions.ilike(managesList, "%" + employee
				+ "," + oldmanager);

		Criterion criterion2 = Restrictions.and(Restrictions.ilike(managesList,
				oldmanager), Restrictions.eq("employee.employeeId",
				Long.parseLong(employee)));
		Criterion criterion = Restrictions.or(criterion1, criterion2);
		criteria.add(criterion);
		return criteria.list();
	}

	@Override
	public List<Employee> getReportiesUnderManager(List<Long> empIds) {

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Employee.class);
		criteria.createAlias("manager", "manager");
		criteria.add(Restrictions.in("manager.employeeId", empIds));
		// Set<Employee> reporteeSet = new HashSet<Employee>(criteria.list());
		criteria.add(Restrictions.eq("statusName", "Active"));
		criteria.addOrder(Order.asc("manager.employeeFullName"));

		return criteria.list();
	}

	@Override
	public Map<String, Object> getReporteeExportList(List<Long> empIds) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(Employee.class);
		criteria.createAlias("manager", "manager");
		criteria.add(Restrictions.in("manager.employeeId", empIds));
		criteria.add(Restrictions.eq("statusName", "Active"));
		criteria.addOrder(Order.asc("manager.employeeFullName"));

		List<Employee> employeeList = criteria.list();
		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("list", employeeList);

		return employeeMap;

	}

	@Override
	public Map<String, Object> getPaginationList(Criteria criteria,Integer fromIndex , Integer toIndex) {
		//this is the generic pagination List Method
		HashMap<String, Object> paginationMap = new HashMap<>();
		//here we set the Projection to get the Record Count
		criteria.setProjection(Projections.rowCount());
		Long recSize =(Long)criteria.uniqueResult();
		//Integer recordCount =Integer.parseInt(recSize.toString());
		paginationMap.put("listSize", recSize);
		//after getting the row count removing the projection by setting null 
		criteria.setProjection(null);
		
		//since we created projection so the ResultSet Structure changed to Object
		//to restore the  Structure we transform
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		//here we are setting the how many records to fetch from DB
		if(fromIndex!=null && toIndex!=null) {
			criteria.setFirstResult(fromIndex);
			criteria.setMaxResults(toIndex-fromIndex);
		}
		
		paginationMap.put("list", criteria.list());
		return paginationMap;
	}
	
	/*here this is the lookUp Method for Hrs */
	@Override
	public List<Employee> getHrLookUp() {
		Criteria roleWiseList = sessionFactory.getCurrentSession()
				.createCriteria(Employee.class);
		
		roleWiseList.createAlias("empRole", "empRole");
		
		return roleWiseList.add(Restrictions.or(Restrictions.eq("empRole.name","HR"),Restrictions.eq("empRole.name", "HR Manager")))
			
				.add(Restrictions.eq("statusName", "Active")).list();
	}
	
	@Override
	public List<CountryLookUp> getEmpCountries() {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(CountryLookUp.class);
         return criteria.list();
	}

	@Override
	public List<Project> mangerUnderProjectPeoples(Long managerId) {
	
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Project.class);
		criteria.createAlias("project.projectManager", "projectmanager");
		criteria.add(Restrictions.eq("projectmanager.employeeId", managerId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Project> allocationDetails = (List<Project>) criteria.list();
		

		
		
		return allocationDetails;
	}

	@Override
	public List<AllocationDetails> getAllocationDetialsForProjects(
			List<Long> projectIds) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(AllocationDetails.class);
		criteria.createAlias("project", "project")
		.setFetchMode("project", FetchMode.JOIN);
		criteria.createAlias("project.projectManager", "projectmanager");
		criteria.add(Restrictions.in("project.id", projectIds));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<AllocationDetails> allocationDetails = (List<AllocationDetails>) criteria.list();
		
		System.out.println("AlloctionDetials ******"+allocationDetails.size());
		
		return allocationDetails;
	}

	@Override
	public Employee getlastEmployeeId() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.desc("employeeId"));
		return (Employee) criteria.list().get(0);
	}
	
	@Override
	public List<Leadership> checkLeadershipDuplication(Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Leadership.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		return criteria.list();
	}

	
}
