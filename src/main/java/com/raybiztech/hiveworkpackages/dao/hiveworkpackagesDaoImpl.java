package com.raybiztech.hiveworkpackages.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.hiveworkpackages.business.Enumerations;
import com.raybiztech.hiveworkpackages.business.Journals;
import com.raybiztech.hiveworkpackages.business.WorkPackageJournals;
import com.raybiztech.hiveworkpackages.business.CustomOptions;
import com.raybiztech.hiveworkpackages.business.CustomValues;
import com.raybiztech.hiveworkpackages.business.CustomizableJournals;
import com.raybiztech.hiveworkpackages.business.statuses;
import com.raybiztech.hiveworkpackages.business.TimeEntries;
import com.raybiztech.hiveworkpackages.business.types;
import com.raybiztech.hiveworkpackages.business.versions;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.hiveworkpackages.business.work_packages;
import com.raybiztech.hiveworkpackages.dto.spentTimeDTO;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectStatus;
import com.raybiztech.projectmanagement.dao.AllocationDetailsDTO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

@Transactional
@Repository("hiveworkpackageDaoImpl")
public class hiveworkpackagesDaoImpl extends DAOImpl implements
		hiveworkpackagesDao {

	@Autowired
	public SessionFactory sessionFactory1;

	@Autowired
	public SessionFactory sessionFactory;

	@Transactional("hiveTransactionManager")
	@Override
	public Map<String, Object> getHivetasks(String employeeNmae) {
		final Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.DATE, -1);
	     System.out.println(cal.getTime());
		
		Map<String, Object> hivetasksmap = new HashMap<String, Object>();

		System.out.println("employee name" + employeeNmae);

		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				work_packages.class);
		criteria.createAlias("assigned_to_id", "user");
		criteria.add(Restrictions.ilike("user.login", employeeNmae));
		// criteria.add(Restrictions.ilike("user.login", employeeNmae,
		// MatchMode.ANYWHERE));
		criteria.add(Restrictions.ge("start_date", cal.getTime()));

		List<work_packages> hivetaskslist = criteria.list();

		hivetasksmap.put("list", hivetaskslist);
		hivetasksmap.put("size", hivetaskslist.size());

		return hivetasksmap;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<types> getTaskTypes() {
		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				types.class);
		criteria.add(Restrictions.eq("is_default", 1));
		criteria.add(Restrictions.and(Restrictions.eq("is_default", 1), 
				Restrictions.not(Restrictions.in("name", new String[] { "Risk", "Status Report"}))));
		List<types> typeList = criteria.list();
		return typeList;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<Enumerations> getPriorityList() {
		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(Enumerations.class);
		criteria.add(Restrictions.eq("type", "IssuePriority"));
		List<Enumerations> priorityList = criteria.list();
		return priorityList;
	}


	@Transactional("hiveTransactionManager")
	@Override
	public List<Enumerations> getactivitytypes() {

		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				Enumerations.class);
		criteria.add(Restrictions.eq("type", "TimeEntryActivity"));
		List<Enumerations> activityList = criteria.list();
		return activityList;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<statuses> getStatusList() {
		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				statuses.class);
		List<statuses> statusList = criteria.list();
		return statusList;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public <T extends Serializable> T findByUniqueProperty(Class<T> clazz,
			String propertyName, Serializable name) {
		return (T) sessionFactory1.getCurrentSession().createCriteria(clazz)
				.add(Restrictions.eq(propertyName, name)).uniqueResult();
	}

	@Transactional("hiveTransactionManager")
	@Override
	public <T extends Serializable> Serializable save(T object) {

		return sessionFactory1.getCurrentSession().save(object);
	}

	@Transactional("hiveTransactionManager")
	@Override
	public <T extends Serializable> void update(T object) {

		sessionFactory1.getCurrentSession().update(object);

	}

	@Override
	public List<String> getHiveTasksClientsForEmployee(Long employeeId,Integer startIndex, Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.ne("project.status", ProjectStatus.CLOSED));
		criteria.createAlias("project.client", "client");
		criteria.setFetchMode("project", FetchMode.JOIN);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		ProjectionList projectionList = Projections.projectionList();
		// projectionList.add(Projections.property("project.id").as("projectId"));
		// projectionList.add(Projections.property("project.projectName").as("projectName"));
		projectionList.add(Projections.groupProperty("client.name").as(
				"clientname"));

		// System.out.println("projectionslist" +projectionList);

		criteria.setProjection(projectionList);

		// criteria.add(Restrictions.like("employee.", employeeNmae));
		/*
		 * List<String> list = criteria.setResultTransformer(
		 * Transformers.aliasToBean(AllocationDetails.class)).list();
		 */
		// criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		
		Integer records =  criteria.list().size();
		
		if(startIndex!=null && endIndex!=null) {
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(endIndex-startIndex);
		}
		
		List<String> clientsList = criteria.list();
		
	//	Integer listSize =criteria.list().size();
		clientsList.add(Integer.toString(records));

		// System.out.println("clientsList"+ clientsList);
		// System.out.println("sizeof list"+clientsList.size());
		return clientsList;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<work_packages> getSprintTasksListForEmployee(Long id, String loginUserName) {
		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				work_packages.class);
		criteria.createAlias("assigned_to_id", "user");
		criteria.add(Restrictions.ilike("user.login", loginUserName));
		criteria.createAlias("fixed_version_id", "versions");
		criteria.add(Restrictions.eq("versions.id", id));
		List<work_packages> sprintTasks = criteria.list();
		return sprintTasks;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<CustomOptions> getCustomValues() {
		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				CustomOptions.class);
		criteria.add(Restrictions.in("custom_field_id", new Long[] { 1L, 21L,
				26L }));

		List<CustomOptions> severityList = criteria.list();
		return severityList;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<work_packages> getProjectTasksListForEmployee(String hiveProjectName,
			String loginUserName) {

		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				work_packages.class);
		criteria.createAlias("assigned_to_id", "user");
		criteria.add(Restrictions.ilike("user.login", loginUserName));
		criteria.createAlias("project_id", "projects");
		criteria.add(Restrictions.ilike("projects.name", hiveProjectName));
		criteria.add(Restrictions.isNull("fixed_version_id"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		List<work_packages> projectTasks = criteria.list();

		return projectTasks;
	}

	@Override
	public List<String> getHiveProjectsListForEmployee(Long employeeId,
			Long client) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.ne("project.status", ProjectStatus.CLOSED));
		criteria.createAlias("project.client", "client");
		criteria.setFetchMode("project", FetchMode.JOIN);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		criteria.add(Restrictions.eq("client.id", client));
		//criteria.add(Restrictions.isNotNull("project.hiveProjectName"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("project.projectName").as(
				"projectName"));
	
		criteria.setProjection(projectionList);

		//List<AllocationDetails> projectList = criteria.list();
		//System.out.println("Size***********"+projectList.size());
		
		List<String> projectList = criteria.list();
		return projectList;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<versions> getVersionsList(String projectName) {

		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				versions.class);
		criteria.createAlias("project_id", "project");
		criteria.add(Restrictions.ilike("project.name", projectName));
		criteria.add(Restrictions.eq("status", "open"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		List<versions> versionList = criteria.list();
		return versionList;
	}

	@Override
	public List<String> getHiveTasksClientsForAdmin(Integer startIndex,Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.ne("project.status", ProjectStatus.CLOSED));
		criteria.createAlias("project.client", "client");
		criteria.setFetchMode("project", FetchMode.JOIN);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("client.name").as(
				"clientname"));
		criteria.setProjection(projectionList);
		
		Integer records =  criteria.list().size();
		if(startIndex!=null && endIndex!=null) {
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(endIndex-startIndex);
		}
		List<String> clientsList = criteria.list();
		clientsList.add(Integer.toString(records));
		
		return clientsList;
	}

	@Override
	public List<String> getHiveTasksClientsForHierarchy(List<Long> managerId,
			Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.createAlias("employee", "employee");
		criteria.createAlias("project.projectManager", "projectmanager");
		criteria.add(Restrictions.or(
				Restrictions.in("projectmanager.employeeId", managerId),
				Restrictions.eq("employee.employeeId", employeeId)));
		criteria.createAlias("project.client", "client");
		criteria.setFetchMode("project", FetchMode.JOIN);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("client.name").as(
				"clientname"));
		criteria.setProjection(projectionList);
		List<String> clientsList = criteria.list();
		return clientsList;
	}

	@Override
	public List<String> getHiveProjectsListForAdmin(Long client) {
		//Long clientId  = 179L;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
		//criteria.add(Restrictions.eq("isAllocated", Boolean.TRUE));
				AllocationDetails.class);
		
		criteria.createAlias("project", "project");
		
		//criteria.add(Restrictions.isNotNull("project.hiveProjectName"));
		
		criteria.createAlias("project.client", "project.client");
		criteria.add(Restrictions.eq("project.client.id", client));	
		criteria.add(Restrictions.ne("project.status", ProjectStatus.CLOSED));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("project.projectName").as(
				"projectName"));
	
		criteria.setProjection(projectionList);

		//List<AllocationDetails> projectList = criteria.list();
		//System.out.println("Size***********"+projectList.size());
		
		List<String> projectList = criteria.list();
		return projectList;
		
		
	}

	@Override
	public List<String> getHiveProjectsListForHierarchy(
			List<Long> managerIds, Long employeeId, Long clientId) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.ne("project.status", ProjectStatus.CLOSED));
		criteria.createAlias("employee", "employee");
		criteria.createAlias("project.projectManager", "projectmanager");
		criteria.add(Restrictions.or(
				Restrictions.in("projectmanager.employeeId", managerIds),
				Restrictions.eq("employee.employeeId", employeeId)));
		criteria.createAlias("project.client", "client");
		criteria.setFetchMode("project", FetchMode.JOIN);
		criteria.add(Restrictions.eq("client.id", clientId));
		//criteria.add(Restrictions.isNotNull("project.hiveProjectName"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("project.projectName").as(
				"projectName"));
	
		criteria.setProjection(projectionList);

		List<String> projectList = criteria.list();
		
		
		return projectList;
	}
   
	@Transactional("hiveTransactionManager")
	@Override
	public List<work_packages> getSprintTasksListForAdmin(Long id) {
		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				work_packages.class);
		criteria.createAlias("fixed_version_id", "versions");
		criteria.add(Restrictions.eq("versions.id", id));
		List<work_packages> sprintTasks = criteria.list();
		return sprintTasks;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<work_packages> getSprintTasksListForHirerarchy(
			List<String> managerUserNames, Long id, String loginUserName) {
		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				work_packages.class);
		criteria.createAlias("assigned_to_id", "user");
		criteria.add(Restrictions.or(
				Restrictions.in("user.login", managerUserNames),
				Restrictions.ilike("user.login", loginUserName)));
		//criteria.add(Restrictions.ilike("user.login", loginUserName));
		criteria.createAlias("fixed_version_id", "versions");
		criteria.add(Restrictions.eq("versions.id", id));
		List<work_packages> sprintTasks = criteria.list();
		return sprintTasks;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<work_packages> getPrrojectTasksListForAdmin(
			String hiveProjectName) {
		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				work_packages.class);
		criteria.createAlias("project_id", "projects");
		criteria.add(Restrictions.ilike("projects.name", hiveProjectName));
		criteria.add(Restrictions.isNull("fixed_version_id"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		List<work_packages> projectTasks = criteria.list();

		return projectTasks;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<work_packages> getPrrojectTasksListForHierarchy(
			List<String> managerUserNames, String hiveProjectName,
			String loginUserName) {
		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(
				work_packages.class);
		criteria.createAlias("assigned_to_id", "user");
		criteria.add(Restrictions.or(
				Restrictions.in("user.login", managerUserNames),
				Restrictions.ilike("user.login", loginUserName)));
		//criteria.add(Restrictions.ilike("user.login", loginUserName));
		criteria.createAlias("project_id", "projects");
		criteria.add(Restrictions.ilike("projects.name", hiveProjectName));
		criteria.add(Restrictions.isNull("fixed_version_id"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		List<work_packages> projectTasks = criteria.list();

		return projectTasks;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public Map<String, Object> getWorkPackageJournal(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(Journals.class);
		criteria.add(Restrictions.eq("journable_id", id));
		criteria.addOrder(Order.desc("id"));
		map.put("listSize", criteria.list().size());
		Journals journal = (Journals) criteria.setMaxResults(1).uniqueResult();
		map.put("journal", journal);
		return map;
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<CustomValues> getCustomizedValues(Long id) {
		Criteria criteria =  sessionFactory1.getCurrentSession().createCriteria(CustomValues.class);
		criteria.add(Restrictions.eq("customizedId", id)).addOrder(Order.asc("id"));
		List<CustomValues> customizedValues = criteria.list();
		return customizedValues;
	}

	@Override
	public List<AllocationDetailsDTO> getProjectsAssignedToEmployee(Long empId) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.setFetchMode("project", FetchMode.JOIN);
		criteria.add(Restrictions.eq("project.status", ProjectStatus.valueOf("INPROGRESS")));
		criteria.add(Restrictions.eq("employee.employeeId", empId));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.rowCount(), "count")
				.add(Projections.groupProperty("project"), "project"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		List<AllocationDetailsDTO> list = criteria.setResultTransformer(
				Transformers.aliasToBean(AllocationDetailsDTO.class)).list();
		return criteria.list();
	}

	@Transactional("hiveTransactionManager")
	@Override
	public List<statuses> getworkPkgStatuses() {
		Criteria criteria = sessionFactory1.getCurrentSession().createCriteria(statuses.class);
		criteria.add(Restrictions.in("id", new Long[] { 1L,4L, 7L,
				9L, 11L, 12L, 13L, 14L, 15L, 17L,}));
		return criteria.list();
	}


}
