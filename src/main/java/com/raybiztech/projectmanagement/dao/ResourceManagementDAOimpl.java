package com.raybiztech.projectmanagement.dao;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTAbstractNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.biometric.business.AttendanceStatus;
import com.raybiztech.biometric.business.BioAttendance;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.DayOfMonth;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.MonthYear;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.business.LeaveStatus;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;
import com.raybiztech.projectmanagement.builder.ChangeRequestBuilder;
import com.raybiztech.projectmanagement.builder.ProjectNumbersBuilder;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.AllocationDetailsAudit;
import com.raybiztech.projectmanagement.business.AllocationEffort;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.business.ChangeRequest;
import com.raybiztech.projectmanagement.business.Client;
import com.raybiztech.projectmanagement.business.Country;
import com.raybiztech.projectmanagement.business.Milestone;
import com.raybiztech.projectmanagement.business.MilestoneAudit;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.business.ProjectAudit;
import com.raybiztech.projectmanagement.business.ProjectCheckList;
import com.raybiztech.projectmanagement.business.ProjectNumbers;
import com.raybiztech.projectmanagement.business.ProjectRequest;
import com.raybiztech.projectmanagement.business.ProjectRequestMail;
import com.raybiztech.projectmanagement.business.ProjectStatus;
import com.raybiztech.projectmanagement.business.ProjectType;
import com.raybiztech.projectmanagement.business.StatusReport;
import com.raybiztech.projectmanagement.dto.ChangeRequestDTO;
import com.raybiztech.projectmanagement.dto.ProjectNumbersDTO;
import com.raybiztech.projectmanagement.dto.ProjectRequestDTO;
import com.raybiztech.projectmanagement.exceptions.ProjectNumbersUpdationException;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.utility.HibernateSupressWaringsUtil;
import com.raybiztech.recruitment.business.Holidays;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.rolefeature.business.Permission;

@Repository("resourceManagementDAO")
public class ResourceManagementDAOimpl extends DAOImpl implements
		ResourceManagementDAO {

	Logger logger = Logger.getLogger(ResourceManagementDAOimpl.class);
	@Autowired
	DAO dao;
	@Autowired
	ChangeRequestBuilder changeRequestBuilder;
	@Autowired
	ProjectNumbersBuilder projectNumbersBuilder;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	LeaveManagementUtils leaveManagementUtils;

	@Override
	public List<Project> activeProjects() {
		Criteria criteria = getSessionFactory()
				.getCurrentSession()
				.createCriteria(Project.class)
				.add(Restrictions.or(
						Restrictions.eq("status", ProjectStatus.NEW),
						Restrictions.eq("status", ProjectStatus.INPROGRESS)));

		return criteria.list();
	}

	public List<Employee> reports(Employee employee) {

		List<Employee> employees = new ArrayList<Employee>();

		if (employee.getRole().equalsIgnoreCase("Manager")) {
			Criteria criteria = sessionFactory.getCurrentSession()
					.createCriteria(Employee.class)
					.add(Restrictions.eq("statusName", "Active"));
			criteria.createAlias("manager", "mgr");
			criteria.setFetchMode("mgr", FetchMode.JOIN);
			criteria.add(Restrictions.eq("mgr.employeeId",
					employee.getEmployeeId()));
			employees = criteria.list();
			employees.add(employee);

		} else if (employee.getRole().equalsIgnoreCase("admin")) {
			Criteria criteria = getSessionFactory().getCurrentSession()
					.createCriteria(Employee.class)
					.add(Restrictions.eq("statusName", "Active"));
			employees = criteria.list();
		} else if (employee.getRole().equalsIgnoreCase("Employee")) {
			employees.add(employee);
		}

		return employees;
	}

	@Override
	public List<Employee> activeEmployeeList(Employee employee) {
		List<Employee> employees = new ArrayList<Employee>();
		// List<Employee> employees2 = new ArrayList<Employee>();
		// Set<Employee> set=null;
		Permission observationListPermission = dao.checkForPermission(
				"Observation List", employee);
		Permission hierarchyObservationList = dao.checkForPermission(
				"Hierarchy Observation List", employee);
		if (observationListPermission.getView()
				&& hierarchyObservationList.getView()) {
			Criteria criteria = sessionFactory.getCurrentSession()
					.createCriteria(Employee.class);

			// criteria.add(Restrictions.or(Restrictions.eq("statusName",
			// "UnderNotice"),Restrictions.eq("statusName", "Active")));

			criteria.add(Restrictions.eq("statusName", "Active"));

			criteria.createAlias("manager", "mgr");
			criteria.setFetchMode("mgr", FetchMode.JOIN);
			criteria.add(Restrictions.eq("mgr.employeeId",
					employee.getEmployeeId()));

			// criteria.add(Restrictions.in("mgr", emps));

			employees = criteria.list();

			// employees2=criteria2.list();
			// set=new HashSet<Employee>(criteria.list());
			// set.addAll(criteria2.list());
			// employees.addAll(set);

		} else if (observationListPermission.getView()
				&& !hierarchyObservationList.getView()) {
			Criteria criteria = getSessionFactory().getCurrentSession()
					.createCriteria(Employee.class);

			// criteria.add(Restrictions.or(Restrictions.eq("statusName",
			// "UnderNotice"),Restrictions.eq("statusName", "Active")));

			criteria.add(Restrictions.eq("statusName", "Active"));

			employees = criteria.list();
		}

		return employees;
	}

	@Override
	public List<Employee> getAllocationEmployees(Employee employee) {
		Criteria criteria2 = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria2.createAlias("project", "project");
		criteria2.createAlias("employee", "employee");
		criteria2.add(Restrictions.eq("project.projectManager", employee));
		criteria2.add(Restrictions.ne("employee", employee));
		criteria2.add(Restrictions.ne("employee.role", "Delivery Manager"));
		criteria2.add(Restrictions.eq("isAllocated", true));
		criteria2.setProjection(Projections.projectionList().add(
				Projections.property("employee")));
		return criteria2.list();
	}

	@Override
	public List<AllocationDetails> getProjectDetails(Long projectId) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.createAlias("project", "project")
				.setFetchMode("project", FetchMode.JOIN)
				.add(Restrictions.eq("project.id", projectId))
			     .addOrder(Order.desc("isAllocated"));
				//add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<AllocationDetails> getMilestonePeople(Long projectId,
			String planningEndDate) {
		// If planned end date is not there then I am taking current date
		Date date = new Date();
		if (planningEndDate != null) {
			try {
				date = DateParser.toDate(planningEndDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int i = 31;
		// checking 31 days before date
		while (i != 0) {
			date = date.previous();
			i = i - 1;
		}

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.createAlias("project", "project")
				.setFetchMode("project", FetchMode.JOIN)
				.add(Restrictions.eq("project.id", projectId));
		// If employee is deallocate within 31 days or allocated
		criteria.add(Restrictions.or(Restrictions.ge("period.maximum", date),
				Restrictions.eq("isAllocated", Boolean.TRUE)));
		criteria.add(Restrictions.eq("billable", Boolean.TRUE));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<AllocationDetails> getProjectDetailsForMilestone(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.createAlias("project", "project")
				.setFetchMode("project", FetchMode.JOIN)
				.add(Restrictions.eq("project.id", projectId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Project> activeProjectsForEmployee(Long employeeid) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class)
				.createAlias("projectManager", "mgr")
				.setFetchMode("mgr", FetchMode.JOIN);
		criteria.add(Restrictions.eq("mgr.employeeId", employeeid)).add(
				Restrictions.or(Restrictions.eq("status", ProjectStatus.NEW),
						Restrictions.eq("status", ProjectStatus.INPROGRESS)));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public Map<String, Object> getProjectDetailsEmployeeCount(
			List<Long> employeeid, Long empid, String projectStatus,
			String type, String health, Integer firstIndex, Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.createAlias("project.projectManager", "projectmanager");
		// for(Long id:employeeid){
		// criteria.add(Restrictions.in("projectmanager.employeeId",
		// employeeid));
		criteria.add(Restrictions.or(
				Restrictions.in("projectmanager.employeeId", employeeid),
				Restrictions.eq("employee.employeeId", empid)));

		// }

		if (!projectStatus.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.status",
					ProjectStatus.valueOf(projectStatus)));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.rowCount(), "count")
				.add(Projections.groupProperty("project"), "project"));

		criteria.addOrder(Order.asc("project.projectName"));

		if (!type.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.type",
					ProjectType.valueOf(type)));
		if (!health.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.health", health));

		List<AllocationDetailsDTO> list = criteria.setResultTransformer(
				Transformers.aliasToBean(AllocationDetailsDTO.class)).list();

		Criteria projectCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class);

		if (!projectStatus.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("status",
					ProjectStatus.valueOf(projectStatus)));

		if (!type.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("type",
					ProjectType.valueOf(type)));

		if (!health.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("health", health));

		projectCriteria.add(Restrictions.in("projectManager.employeeId",
				employeeid));

		projectCriteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);

		List<Project> projectsList = projectCriteria.list();
		List<Project> projectsSet = new ArrayList<Project>();
		for (Project prj : projectsList) {
			boolean flag = true;
			for (AllocationDetailsDTO allocationDetailsDTO : list) {
				if (prj.getId().equals(
						allocationDetailsDTO.getProject().getId())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				projectsSet.add(prj);
			}
		}

		for (Project addto : projectsSet) {
			AllocationDetailsDTO addto1 = new AllocationDetailsDTO();
			addto1.setProject(addto);
			addto1.setCount(0l);
			list.add(addto1);
		}
		// sorting latest first
		list = sortingProject(list);

		Integer noOfProjects = list.size();
		List<AllocationDetailsDTO> listData = null;
		if (noOfProjects < endIndex) {
			listData = list.subList(firstIndex, noOfProjects);
		} else {
			listData = list.subList(firstIndex, endIndex);
		}

		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("projectList", listData);
		projectMap.put("size", noOfProjects);
		return projectMap;

	}

	@Override
	public Map<String, Object> getProjectDetailsEmployeeCountAdmin(
			String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");

		if (!projectStatus.equalsIgnoreCase("All")) {
			criteria.setFetchMode("project", FetchMode.JOIN)
					.add(Restrictions.eq("project.status",
							ProjectStatus.valueOf(projectStatus)))
					.setProjection(
							Projections
									.projectionList()
									.add(Projections.rowCount(), "count")
									.add(Projections.groupProperty("project"),
											"project"));
			// criteria.addOrder(Order.asc("project.projectName"));

		} else {

			criteria.setFetchMode("project", FetchMode.JOIN).setProjection(
					Projections
							.projectionList()
							.add(Projections.rowCount(), "count")
							.add(Projections.groupProperty("project"),
									"project"));
			// criteria.addOrder(Order.asc("project.createdDate"));

		}
		if (!type.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.type",
					ProjectType.valueOf(type)));

		if (!health.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.health", health));

		List<AllocationDetailsDTO> list = criteria.setResultTransformer(
				Transformers.aliasToBean(AllocationDetailsDTO.class)).list();

		Criteria projectCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class);
		if (!projectStatus.equalsIgnoreCase("All")) {

			projectCriteria.add(Restrictions.eq("status",
					ProjectStatus.valueOf(projectStatus)));
		}

		if (!type.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("type",
					ProjectType.valueOf(type)));

		if (!health.equalsIgnoreCase("All")) {
			projectCriteria.add(Restrictions.eq("health", health));
		}

		projectCriteria
				.setResultTransformer(projectCriteria.DISTINCT_ROOT_ENTITY);

		List<Project> projectsList = projectCriteria.list();
		List<Project> projectsSet = new ArrayList<Project>();
		for (Project prj : projectsList) {
			boolean flag = true;
			for (AllocationDetailsDTO allocationDetailsDTO : list) {
				if (prj.getId().equals(
						allocationDetailsDTO.getProject().getId())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				projectsSet.add(prj);
			}
		}

		for (Project addto : projectsSet) {
			AllocationDetailsDTO addto1 = new AllocationDetailsDTO();
			addto1.setProject(addto);
			addto1.setCount(0l);
			list.add(addto1);
		}

		// sorting for on latest
		list = sortingProject(list);

		Integer noOfProjects = list.size();
		List<AllocationDetailsDTO> listData = null;
		if (noOfProjects < endIndex) {
			listData = list.subList(firstIndex, noOfProjects);
		} else {
			listData = list.subList(firstIndex, endIndex);
		}

		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("projectList", listData);
		projectMap.put("size", noOfProjects);
		return projectMap;		
	}

	@Override
	public Map<Project, AllocationDetails> getAllProjects_forEmployee(
			Long employeeId) {

		Employee employee = (Employee) sessionFactory.getCurrentSession().load(
				Employee.class, employeeId);
		Map<Project, AllocationDetails> map = employee.getAllocations();

		return map;
	}

	@Override
	public Map<Project, AllocationDetails> getAllProjects_UnderEmployee(
			Long employeeId, String isBillale, List<String> employeeStatus,
			DateRange date) {
		Employee employee = (Employee) sessionFactory.getCurrentSession().load(
				Employee.class, employeeId);
		Map<Project, AllocationDetails> map = new HashMap<Project, AllocationDetails>();
		for (Map.Entry<Project, AllocationDetails> entry : employee
				.getAllocations().entrySet()) {
			Project p = entry.getKey();

			AllocationDetails al = entry.getValue();

			if ((date.getMinimum().isBefore(al.getPeriod().getMinimum()) && date
					.getMaximum().isAfter(al.getPeriod().getMaximum()))
					|| (date.contains(al.getPeriod()))
					|| ((al.getPeriod().getMinimum()
							.isBefore(date.getMaximum()) || al.getPeriod()
							.getMinimum().equals(date.getMaximum())) && (al
							.getPeriod().getMaximum()
							.isAfter(date.getMinimum()) || al.getPeriod()
							.getMaximum().equals(date.getMinimum())))) {
				if (al != null) {
					
					if (isBillale.equals("true")
							&& employeeStatus.size() == 0) {
						if ((al.getIsAllocated().equals(true)
								|| al.getIsAllocated().equals(false)) && (al.getBillable().equals(true))) {
							map.put(p, al);

						}
					}
					
					if (isBillale.equals("false")
							&& employeeStatus.size() == 0) {
						if ((al.getIsAllocated().equals(true)
								|| al.getIsAllocated().equals(false)) && (al.getBillable().equals(false))) {
							map.put(p, al);
						}
					}
					
					if (employeeStatus.contains("true")
							|| employeeStatus.contains("false")) {
						for (String status : employeeStatus) {
							Boolean allocate = Boolean.valueOf(status
									.contains("true") ? true : false);
							if (al.getIsAllocated().equals(allocate)) {
								if (isBillale.equals("true")
										|| isBillale.equals("false")) {
									Boolean billable = Boolean
											.valueOf(isBillale);
									if (al.getBillable().equals(billable)) {

										map.put(p, al);

									}
								} else {

									map.put(p, al);

								}

							}
						}

					} else {
						if (isBillale.equals("true")
								|| isBillale.equals("false")
								|| isBillale.equals("onBench")) {
							Boolean billable = Boolean.valueOf(isBillale);
							if (al.getBillable().equals(billable)) {

								map.put(p, al);

							}

						} else {
							map.put(p, al);

						}
					}
				}
			}
		}
		return map;

		// return employee.getAllocations();
	}

	@Override
	public Map<String, Object> searchByEmployeeNameForManager(
			List<Long> employeeId, Integer firstIndex, Integer endIndex,
			String employeeName, String technology, String isBillable,
			List<String> employeeStatus, DateRange date) {
		Long loggedInEmpId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();
		
		List<Employee> employees = new ArrayList<Employee>();
		Set<Employee> employees1 = new HashSet<Employee>();
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(Employee.class)
				.add(Restrictions.or(Restrictions.eq("statusName", "Active"),
						Restrictions.eq("statusName", "underNotice")));
		// resourceimpl.getAllProjects_UnderEmployee(employees.);
		criteria.createAlias("manager", "mgr");
		criteria.setFetchMode("mgr", FetchMode.JOIN);
		criteria.add(Restrictions.or(Restrictions.eq("employeeId",loggedInEmpId), Restrictions.in("mgr.employeeId", employeeId)));
		/*criteria.add(Restrictions.in("mgr.employeeId", employeeId));*/
		if (!technology.isEmpty())
			criteria.add(Restrictions.ilike("technology", technology));

		Criterion firstNameCriterion = Restrictions.ilike("employeeFullName",
				employeeName, MatchMode.ANYWHERE);
		Criterion emailCriterion = Restrictions.or(Restrictions.or(Restrictions
				.ilike("email", employeeName, MatchMode.ANYWHERE), Restrictions
				.ilike("designation", employeeName, MatchMode.ANYWHERE)),
				Restrictions.ilike("departmentName", employeeName,
						MatchMode.ANYWHERE));

		try {
			Criterion empIdCriterion = Restrictions.eq("employeeId",
					Long.parseLong(employeeName));
			criteria.add(Restrictions.or(firstNameCriterion,
					Restrictions.or(empIdCriterion, emailCriterion)));
		} catch (NumberFormatException nfe) {
			criteria.add(Restrictions.or(firstNameCriterion, emailCriterion));
		}

		/*
		 * if (date.getMinimum() != null && date.getMaximum() != null) {
		 * criteria.createAlias("allocations", "allocation");
		 * 
		 * Criterion criterion4 = Restrictions.ge("allocation.period.minimum",
		 * date.getMinimum()); Criterion criterion5 =
		 * Restrictions.le("allocation.period.maximum", date.getMaximum());
		 * 
		 * 
		 * Criterion criterion1 = Restrictions.between(
		 * "allocation.period.minimum", date.getMinimum(), date.getMaximum());
		 * Criterion criterion2 = Restrictions.between(
		 * "allocation.period.minimum", date.getMinimum(), date.getMaximum());
		 * 
		 * criteria.add(criterion1); criteria.add(criterion2);
		 * 
		 * 
		 * criteria.add(criterion4); criteria.add(criterion5);
		 * 
		 * }
		 */
		employees = criteria.list();

		for (Employee e : employees) {

			for (Map.Entry<Project, AllocationDetails> entry : e
					.getAllocations().entrySet()) {
				Project p = entry.getKey();

				AllocationDetails al = entry.getValue();

				if (al != null && al.getPeriod() != null) {

					if ((date.getMinimum()
							.isBefore(al.getPeriod().getMinimum()) && date
							.getMaximum().isAfter(al.getPeriod().getMaximum()))
							|| (date.contains(al.getPeriod()))
							|| ((al.getPeriod().getMinimum()
									.isBefore(date.getMaximum()) || al
									.getPeriod().getMinimum()
									.equals(date.getMaximum())) && (al
									.getPeriod().getMaximum()
									.isAfter(date.getMinimum()) || al
									.getPeriod().getMaximum()
									.equals(date.getMinimum())))) {

						if (((employeeStatus.size() == 0 || employeeStatus
								.size() == 2) && isBillable.equals("All"))
								|| ((employeeStatus.size() == 0 || employeeStatus
										.size() == 2) && isBillable
										.equals("All"))
								|| ((employeeStatus.size() == 0 || employeeStatus
										.size() == 2) && isBillable == null)
								|| ((employeeStatus.size() == 0 || employeeStatus
										.size() == 2) && isBillable == null)) {
							employees1.add(e);

						} else {
							if (isBillable.equals("true")
									&& employeeStatus.size() == 0) {
								if ((al.getIsAllocated().equals(true)
										|| al.getIsAllocated().equals(false)) && (al.getBillable().equals(true))) {
									employees1.add(e);
								}
							}
							
							if (isBillable.equals("false")
									&& employeeStatus.size() == 0) {
								if ((al.getIsAllocated().equals(true)
										|| al.getIsAllocated().equals(false)) && (al.getBillable().equals(false))) {
									employees1.add(e);
								}
							}

							if ((employeeStatus.contains("true")
									|| employeeStatus.contains("false"))) {
								for (String status : employeeStatus) {
									Boolean allocate = Boolean.valueOf(status
											.equals("true") ? true : false);
									if (al.getIsAllocated().equals(allocate)) {
										if (isBillable.equals("true")
												|| isBillable.equals("false")) {
											Boolean billable = Boolean
													.valueOf(isBillable);
											if (al.getBillable().equals(
													billable)) {
												employees1.add(e);
											}
										} else {
											employees1.add(e);
										}
									}
								}

							}
						}
					}
				}
			}
		}
		
		
		
		List<Employee> employees2 = new ArrayList(employees1);
		Integer noOfEmplyoees = employees2.size();
		/*
		 * criteria.setFirstResult(firstIndex); criteria.setMaxResults(endIndex
		 * - firstIndex);
		 */
		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("employeeList", employees2);
		employeeMap.put("size", noOfEmplyoees);
		return employeeMap;
	}

	@Override
	public Map<String, Object> searchByEmployeeNameForAdmin(Long employeeId,
			Integer firstIndex, Integer endIndex, String employeeName,
			String technology, String isBillable, List<String> employeeStatus,
			DateRange date, List<String> departmentNames)
			throws HibernateException, ParseException {
		List<Employee> employees = new ArrayList<Employee>();
		List<String> projectSupportedDepartments = projectSupportedDepartments();
		Set<Employee> employees1 = new HashSet<Employee>();
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(Employee.class)
				.add(Restrictions.ne("employeeId", 1000L))
				.add(Restrictions.or(Restrictions.eq("statusName", "Active"),
						Restrictions.eq("statusName", "underNotice")));
		if (!technology.isEmpty())
			criteria.add(Restrictions.ilike("technology", technology));
		// To search the list of departments
		if (!departmentNames.isEmpty())
			criteria.add(Restrictions.in("departmentName", departmentNames));
		else{
			if(!isBillable.equals("onBench")){
				criteria.add(Restrictions.in("departmentName", projectSupportedDepartments));
			}
		}
		Criterion firstNameCriterion = Restrictions.ilike("employeeFullName",
				employeeName, MatchMode.ANYWHERE);
		Criterion emailCriterion = Restrictions.or(Restrictions.or(Restrictions
				.ilike("email", employeeName, MatchMode.ANYWHERE), Restrictions
				.ilike("designation", employeeName, MatchMode.ANYWHERE)),
				Restrictions.ilike("departmentName", employeeName,
						MatchMode.ANYWHERE));
		try {
			Criterion empIdCriterion = Restrictions.eq("employeeId",
					Long.parseLong(employeeName));
			criteria.add(Restrictions.or(firstNameCriterion,
					Restrictions.or(empIdCriterion, emailCriterion)));
		} catch (NumberFormatException nfe) {
			criteria.add(Restrictions.or(firstNameCriterion, emailCriterion));
		}
		
		List<String> projectnotSupportedDepartments = projectnotSupportedDepartments();
		employees = criteria.list();
		List<Long> empIds = new ArrayList<Long>();
		for(Employee employeeIds:employees)
		{
			empIds.add(employeeIds.getEmployeeId());
		}
		
		if(isBillable.equals("onBench"))
		{
			List<Employee> employeelist= getNotAllocatedEmployeeDepartmentEmployees(projectSupportedDepartments, empIds);
			employees = new ArrayList<Employee>();
			employees.addAll(employeelist);
			List<Employee> employeelist2= getNotAllocatedEmployeeDepartmentEmployees(projectnotSupportedDepartments, empIds);
			
			for(Employee emp:employeelist2)
			{
				if(date.contains(emp.getJoiningDate()))
				{
					employees1.add(emp);
				}
			}
		}
		for (Employee e : employees) {
			Boolean onBenchFlag = true;
			if (e.getAllocations().entrySet().isEmpty()) {
				if (isBillable.equals("onBench")) {
					employees1.add(e);
				}

			}

			for (Map.Entry<Project, AllocationDetails> entry : e
					.getAllocations().entrySet()) {
				Project p = entry.getKey();

				AllocationDetails al = entry.getValue();

				if (al != null && al.getPeriod() != null) {
					if ((date.getMinimum()
							.isBefore(al.getPeriod().getMinimum()) && date
							.getMaximum().isAfter(al.getPeriod().getMaximum()))
							|| (date.contains(al.getPeriod()))
							|| ((al.getPeriod().getMinimum()
									.isBefore(date.getMaximum()) || al
									.getPeriod().getMinimum()
									.equals(date.getMaximum())) && (al
									.getPeriod().getMaximum()
									.isAfter(date.getMinimum()) || al
									.getPeriod().getMaximum()
									.equals(date.getMinimum())))) {
						if (((employeeStatus.size() == 0 || employeeStatus
								.size() == 2) && isBillable.equals("All"))
								|| ((employeeStatus.size() == 0 || employeeStatus
										.size() == 2) && isBillable
										.equals("All"))
								|| ((employeeStatus.size() == 0 || employeeStatus
										.size() == 2) && isBillable == null)
								|| ((employeeStatus.size() == 0 || employeeStatus
										.size() == 2) && isBillable == null)) {
							employees1.add(e);

						} else {
							if (isBillable.equals("true")
									&& employeeStatus.size() == 0) {
								if ((al.getIsAllocated().equals(true)
										|| al.getIsAllocated().equals(false)) && (al.getBillable().equals(true))) {
									employees1.add(e);
								}
							}
							
							if (isBillable.equals("false")
									&& employeeStatus.size() == 0) {
								if ((al.getIsAllocated().equals(true)
										|| al.getIsAllocated().equals(false)) && (al.getBillable().equals(false))) {
									employees1.add(e);
								}
							}

							if ((employeeStatus.contains("true")
									|| employeeStatus.contains("false"))) {
								for (String status : employeeStatus) {
									Boolean allocate = Boolean.valueOf(status
											.equals("true") ? true : false);
									if (al.getIsAllocated().equals(allocate)) {
										if (isBillable.equals("true")
												|| isBillable.equals("false")) {
											Boolean billable = Boolean
													.valueOf(isBillable);
											if (al.getBillable().equals(
													billable)) {
												employees1.add(e);
											}
										} else {
											employees1.add(e);
										}
									}
								}

							}

							if (isBillable.equals("onBench")) {
								if (al.getBillable().equals(true)) {
									if (al.getIsAllocated()) {
										onBenchFlag = false;
									}
								}
							}
						}

					}
				}
			}
			if (onBenchFlag && isBillable.equals("onBench")) {
				employees1.add(e);
			}
		}

		List<Employee> employees2 = new ArrayList(employees1);
		Collections.sort(employees2);
		Integer noOfEmplyoees = employees2.size();
		if (firstIndex != null && endIndex != null) {
			criteria.setFirstResult(firstIndex);
			criteria.setMaxResults(endIndex - firstIndex);
		}
		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("employeeList", employees2);
		employeeMap.put("size", noOfEmplyoees);

		return employeeMap;
	}

	@Override
	public List<Employee> searchByEmployeeId(Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Employee.class)
				.add(Restrictions.eq("employeeId", employeeId));
		return criteria.list();
	}

	@Override
	public Boolean isEmployeeAllocatedTosameProject(Long employeeId,
			Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.createAlias("employee", "emp");
		criteria.createAlias("project", "prj");
		criteria.add(Restrictions.eq("emp.employeeId", employeeId));
		criteria.add(Restrictions.eq("prj.id", projectId));
		criteria.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		return criteria.list().isEmpty() ? true : false;
	}

	@Override
	public Map<String, Object> getProfilePaginationEmployeesData(
			Long loggedInEmpId, String selectionStatus, int startIndex,
			int endIndex) {
		List<Employee> employees = new ArrayList<Employee>();
		Integer noOfEmplyoees = null;
		Employee employee = (Employee) sessionFactory.getCurrentSession()
				.createCriteria(Employee.class)
				.add(Restrictions.eq("employeeId", loggedInEmpId))
				.uniqueResult();

		if (employee.getRole().equalsIgnoreCase("Manager")) {
			Criteria criteria = sessionFactory.getCurrentSession()
					.createCriteria(Employee.class);

			Criterion criterion = Restrictions.or(
					Restrictions.ilike("statusName", "Active"),
					Restrictions.ilike("statusName", "underNotice"));
			criteria.add(criterion);

			criteria.createAlias("manager", "mgr");
			criteria.setFetchMode("mgr", FetchMode.JOIN);
			criteria.add(Restrictions.eq("mgr.employeeId",
					employee.getEmployeeId()));

			noOfEmplyoees = criteria.list().size();
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(endIndex - startIndex);
			employees = criteria.list();

		} else if (employee.getRole().equalsIgnoreCase("admin")) {
			Criteria criteria = getSessionFactory().getCurrentSession()
					.createCriteria(Employee.class);

			Criterion criterion = Restrictions.or(
					Restrictions.ilike("statusName", "Active"),
					Restrictions.ilike("statusName", "underNotice"));
			criteria.add(criterion);

			noOfEmplyoees = criteria.list().size();
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(endIndex - startIndex);
			employees = criteria.list();
		} else if (employee.getRole().equalsIgnoreCase("Employee")) {
			Criteria criteria = getSessionFactory()
					.getCurrentSession()
					.createCriteria(Employee.class)
					.add(Restrictions.eq("employeeId", employee.getEmployeeId()));
			Criterion criterion = Restrictions.or(
					Restrictions.ilike("statusName", "Active"),
					Restrictions.ilike("statusName", "underNotice"));
			criteria.add(criterion);
			noOfEmplyoees = criteria.list().size();
			criteria.setFirstResult(startIndex);
			criteria.setMaxResults(endIndex - startIndex);
			employees = criteria.list();
		}

		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("employeeList", employees);
		employeeMap.put("size", noOfEmplyoees);
		return employeeMap;

	}

	@Override
	public List<Project> getAllProjects(String projectStatus) {
		Criteria criteria = sessionFactory
				.getCurrentSession()
				.createCriteria(Project.class)
				.add(Restrictions.eq("status",
						ProjectStatus.valueOf(projectStatus)));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public Map<String, Object> searchAllocationReportData(
			List<Long> employeeId, Long loggedInEmpId, String projectStatus,
			String type, String health, Integer firstIndex, Integer endIndex,
			DateRange dateRange, String multiSearch, Boolean intrnalOrNot) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project").setFetchMode("project",
				FetchMode.JOIN);
		criteria.createAlias("project.client", "client").setFetchMode("client",
				FetchMode.JOIN);
		criteria.createAlias("project.projectManager", "projectmanager")
				.setFetchMode("project", FetchMode.JOIN);

		// criteria.add(Restrictions.in("projectmanager.employeeId",
		// employeeId));
		criteria.add(Restrictions.or(
				Restrictions.in("projectmanager.employeeId", employeeId),
				Restrictions.eq("employee.employeeId", loggedInEmpId)));

		if (intrnalOrNot == true) {
			criteria.add(Restrictions.eq("project.internalOrNot", Boolean.TRUE));
		}

		if (!projectStatus.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.status",
					ProjectStatus.valueOf(projectStatus)));

		if (!type.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.type",
					ProjectType.valueOf(type)));
		if (!health.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.health", health));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.rowCount(), "count")
				.add(Projections.groupProperty("project"), "project"));
		if (multiSearch != null) {
			Criterion projectNameCriterion = Restrictions.ilike(
					"project.projectName", multiSearch, MatchMode.ANYWHERE);
			Criterion firstNameCriterion = Restrictions.ilike(
					"projectmanager.employeeFullName", multiSearch,
					MatchMode.ANYWHERE);

			Criterion client = Restrictions.ilike("client.name", multiSearch,
					MatchMode.ANYWHERE);

			Criterion pnorpmcri = Restrictions.or(projectNameCriterion,
					firstNameCriterion);

			Criterion pnorpmcriorClientname = Restrictions
					.or(pnorpmcri, client);
			criteria.add(pnorpmcriorClientname);

		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {
			Criterion criterion4 = Restrictions.or(Restrictions.and(
					Restrictions.ge("project.period.minimum",
							dateRange.getMinimum()),
					Restrictions.le("project.period.maximum",
							dateRange.getMaximum())), Restrictions.or(
					Restrictions.and(
							Restrictions.ge("project.period.minimum",
									dateRange.getMinimum()),
							Restrictions.le("project.period.minimum",
									dateRange.getMaximum())), Restrictions.or(
							Restrictions.and(Restrictions.ge(
									"project.period.maximum",
									dateRange.getMinimum()), Restrictions.le(
									"project.period.maximum",
									dateRange.getMaximum())), Restrictions.and(
									Restrictions.le("project.period.minimum",
											dateRange.getMinimum()),
									Restrictions.ge("project.period.maximum",
											dateRange.getMaximum())))));
			criteria.add(criterion4);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria.add(Restrictions.ge("project.period.minimum",
					dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria.add(Restrictions.le("project.period.maximum",
					dateRange.getMaximum()));
		}

		List<AllocationDetailsDTO> list = criteria.setResultTransformer(
				Transformers.aliasToBean(AllocationDetailsDTO.class)).list();

		Criteria projectCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class);
		if (intrnalOrNot) {
			projectCriteria.add(Restrictions.eq("internalOrNot", Boolean.TRUE));
		}
		projectCriteria.createAlias("client", "client");

		if (!projectStatus.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("status",
					ProjectStatus.valueOf(projectStatus)));
		if (!type.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("type",
					ProjectType.valueOf(type)));

		if (!health.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("health", health));

		projectCriteria.createAlias("projectManager", "projectmanager")
				.setFetchMode("projectmanager", FetchMode.JOIN);
		projectCriteria.add(Restrictions.in("projectmanager.employeeId",
				employeeId));

		if (multiSearch != null) {
			Criterion pNameCriterion = Restrictions.ilike("projectName",
					multiSearch, MatchMode.ANYWHERE);
			Criterion fNameCriterion = Restrictions
					.ilike("projectmanager.firstName", multiSearch,
							MatchMode.ANYWHERE);
			Criterion pnorpmcri = Restrictions.or(pNameCriterion,
					fNameCriterion);
			Criterion client = Restrictions.ilike("client.name", multiSearch,
					MatchMode.ANYWHERE);

			Criterion pnorpmcriorClientname = Restrictions
					.or(pnorpmcri, client);
			projectCriteria.add(pnorpmcriorClientname);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {
			Criterion pcriterion4 = Restrictions.or(Restrictions.and(
					Restrictions.ge("period.minimum", dateRange.getMinimum()),
					Restrictions.le("period.maximum", dateRange.getMaximum())),
					Restrictions.or(Restrictions.and(
							Restrictions.ge("period.minimum",
									dateRange.getMinimum()),
							Restrictions.le("period.minimum",
									dateRange.getMaximum())), Restrictions.or(
							Restrictions.and(
									Restrictions.ge("period.maximum",
											dateRange.getMinimum()),
									Restrictions.le("period.maximum",
											dateRange.getMaximum())),
							Restrictions.and(
									Restrictions.le("period.minimum",
											dateRange.getMinimum()),
									Restrictions.ge("period.maximum",
											dateRange.getMaximum())))));
			projectCriteria.add(pcriterion4);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			projectCriteria.add(Restrictions.ge("period.minimum",
					dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			projectCriteria.add(Restrictions.le("period.maximum",
					dateRange.getMaximum()));
		}
		Set<Project> projects = new HashSet<Project>(projectCriteria.list());
		List<Project> projectsList = new ArrayList<Project>(projects);
		// List<Project> projectsList = projectCriteria.list();
		List<Project> projectsSet = new ArrayList<Project>();
		for (Project prj : projectsList) {
			boolean flag = true;
			for (AllocationDetailsDTO allocationDetailsDTO : list) {
				if (prj.getId().equals(
						allocationDetailsDTO.getProject().getId())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				projectsSet.add(prj);
			}
		}

		for (Project addto : projectsSet) {
			AllocationDetailsDTO addto1 = new AllocationDetailsDTO();
			addto1.setProject(addto);
			addto1.setCount(0l);
			list.add(addto1);
		}
		// sorting latest first
		list = sortingProject(list);

		Integer noOfProjects = list.size();
		List<AllocationDetailsDTO> listData = null;
		if (noOfProjects < endIndex) {
			listData = list.subList(firstIndex, noOfProjects);
		} else {
			listData = list.subList(firstIndex, endIndex);
		}

		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("projectList", listData);
		projectMap.put("size", noOfProjects);
		return projectMap;
	}

	@Override
	public Map<String, Object> searchAllocationReportDataForAdmin(
			String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex, DateRange dateRange,
			String multiSearch, Boolean intrnalOrNot) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project").setFetchMode("project",
				FetchMode.JOIN);
		criteria.createAlias("project.client", "client").setFetchMode("client",
				FetchMode.JOIN);

		criteria.createAlias("project.projectManager", "projectmanager")
				.setFetchMode("project", FetchMode.JOIN);

		if (intrnalOrNot == true) {
			criteria.add(Restrictions.eq("project.internalOrNot", Boolean.TRUE));
		}

		if (!projectStatus.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.status",
					ProjectStatus.valueOf(projectStatus)));
		if (!type.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.type",
					ProjectType.valueOf(type)));
		if (!health.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.health", health));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.rowCount(), "count")
				.add(Projections.groupProperty("project"), "project"));

		if (multiSearch != null) {
			Criterion projectNameCriterion = Restrictions.ilike(
					"project.projectName", multiSearch, MatchMode.ANYWHERE);
			Criterion firstNameCriterion = Restrictions.ilike(
					"projectmanager.employeeFullName", multiSearch,
					MatchMode.ANYWHERE);
			Criterion client = Restrictions.ilike("client.name", multiSearch,
					MatchMode.ANYWHERE);

			Criterion pnorpmcri = Restrictions.or(projectNameCriterion,
					firstNameCriterion);

			Criterion pnorpmcriorClientname = Restrictions
					.or(pnorpmcri, client);
			criteria.add(pnorpmcriorClientname);

		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {
			Criterion criterion4 = Restrictions.or(Restrictions.and(
					Restrictions.ge("project.period.minimum",
							dateRange.getMinimum()),
					Restrictions.le("project.period.maximum",
							dateRange.getMaximum())), Restrictions.or(
					Restrictions.and(
							Restrictions.ge("project.period.minimum",
									dateRange.getMinimum()),
							Restrictions.le("project.period.minimum",
									dateRange.getMaximum())), Restrictions.or(
							Restrictions.and(Restrictions.ge(
									"project.period.maximum",
									dateRange.getMinimum()), Restrictions.le(
									"project.period.maximum",
									dateRange.getMaximum())), Restrictions.and(
									Restrictions.le("project.period.minimum",
											dateRange.getMinimum()),
									Restrictions.ge("project.period.maximum",
											dateRange.getMaximum())))));
			criteria.add(criterion4);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria.add(Restrictions.ge("project.period.minimum",
					dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria.add(Restrictions.le("project.period.maximum",
					dateRange.getMaximum()));
		}

		List<AllocationDetailsDTO> list = criteria.setResultTransformer(
				Transformers.aliasToBean(AllocationDetailsDTO.class)).list();

		Criteria projectCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class);
		if (intrnalOrNot) {
			projectCriteria.add(Restrictions.eq("internalOrNot", Boolean.TRUE));
		}
		projectCriteria.createAlias("client", "client");
		if (!projectStatus.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("status",
					ProjectStatus.valueOf(projectStatus)));
		projectCriteria.createAlias("projectManager", "projectmanager")
				.setFetchMode("projectmanager", FetchMode.JOIN);

		if (!type.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("type",
					ProjectType.valueOf(type)));
		if (!health.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("health", health));

		if (multiSearch != null) {
			Criterion pNameCriterion = Restrictions.ilike("projectName",
					multiSearch, MatchMode.ANYWHERE);
			Criterion fNameCriterion = Restrictions
					.ilike("projectmanager.firstName", multiSearch,
							MatchMode.ANYWHERE);
			Criterion client = Restrictions.ilike("client.name", multiSearch,
					MatchMode.ANYWHERE);

			Criterion pnorpmcri = Restrictions.or(pNameCriterion,
					fNameCriterion);

			Criterion pnorpmcriorClientname = Restrictions
					.or(pnorpmcri, client);
			projectCriteria.add(pnorpmcriorClientname);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {
			Criterion pcriterion4 = Restrictions.or(Restrictions.and(
					Restrictions.ge("period.minimum", dateRange.getMinimum()),
					Restrictions.le("period.maximum", dateRange.getMaximum())),
					Restrictions.or(Restrictions.and(
							Restrictions.ge("period.minimum",
									dateRange.getMinimum()),
							Restrictions.le("period.maximum",
									dateRange.getMaximum())), Restrictions.or(
							Restrictions.and(
									Restrictions.ge("period.maximum",
											dateRange.getMinimum()),
									Restrictions.le("period.maximum",
											dateRange.getMaximum())),
							Restrictions.and(
									Restrictions.le("period.minimum",
											dateRange.getMinimum()),
									Restrictions.ge("period.maximum",
											dateRange.getMaximum())))));
			projectCriteria.add(pcriterion4);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			projectCriteria.add(Restrictions.ge("period.minimum",
					dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			projectCriteria.add(Restrictions.le("period.maximum",
					dateRange.getMaximum()));
		}
		Set<Project> projects = new HashSet<Project>(projectCriteria.list());
		List<Project> projectsList = new ArrayList<Project>(projects);
		// List<Project> projectsList = projectCriteria.list();
		List<Project> projectsSet = new ArrayList<Project>();
		for (Project prj : projectsList) {
			boolean flag = true;
			for (AllocationDetailsDTO allocationDetailsDTO : list) {
				if (prj.getId().equals(
						allocationDetailsDTO.getProject().getId())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				projectsSet.add(prj);
			}
		}

		for (Project addto : projectsSet) {
			AllocationDetailsDTO addto1 = new AllocationDetailsDTO();
			addto1.setProject(addto);
			addto1.setCount(0l);
			list.add(addto1);
		}
		// sorting latest
		list = sortingProject(list);

		Integer noOfProjects = list.size();
		List<AllocationDetailsDTO> listData = null;
		if (noOfProjects < endIndex) {
			listData = list.subList(firstIndex, noOfProjects);
		} else {
			listData = list.subList(firstIndex, endIndex);
		}

		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("projectList", listData);
		projectMap.put("size", noOfProjects);
		return projectMap;
	}

	@Override
	public void deleteProject(Long projectid) {
		// delete AllocationDetails
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.createAlias("project", "project")
				.setFetchMode("project", FetchMode.JOIN)
				.add(Restrictions.eq("project.id", projectid));
		List<AllocationDetails> detailses = criteria.list();
		for (AllocationDetails allocationDetails : detailses) {
			sessionFactory.getCurrentSession().delete(allocationDetails);
		}
		// delete Project
		Criteria projectCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class)
				.add(Restrictions.eq("id", projectid));
		Project project = (Project) projectCriteria.uniqueResult();
		if (project != null) {
			sessionFactory.getCurrentSession().delete(project);
		}

	}

	@Override
	public List<AllocationDetails> getAllocationProject(Long employeeId, Long id) {
		Criteria projectCriteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class);
		// projectCriteria.createAlias("employee", "employee").setFetchMode(
		// "employee", FetchMode.JOIN);
		// projectCriteria.createAlias("project", "project").setFetchMode(
		// "project", FetchMode.JOIN);
		// projectCriteria.add(Restrictions.eq("employee.employeeId",
		// employeeId));
		// projectCriteria.add(Restrictions.eq("project.id", id));
		projectCriteria.add(Restrictions.eq("employee",
				dao.findBy(Employee.class, employeeId)));
		projectCriteria.add(Restrictions.eq("project",
				dao.findBy(Project.class, id)));
		return projectCriteria.list();
	}

	@Override
	public Map<String, Object> getAllMilestones_UnderProject(Long projectId,
			Integer firstIndex, Integer endIndex) {

		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Milestone.class);
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		map.put("size", criteria.list().size());
		map.put("list", criteria.list());

		return map;
	}

	@Override
	public Map<String, Object> getAllStatusReports_UnderProject(Long projectId,
			Integer firstIndex, Integer endIndex) {

		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				StatusReport.class);
		criteria.createAlias("project", "project").setFetchMode("project",
				FetchMode.JOIN);
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		map.put("size", criteria.list().size());
		map.put("list", criteria.list());
		return map;

	}

	@Override
	public List<Project> getAllProjectSearch(String searchStr) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Project.class);
		criteria.add(
				Restrictions.or(Restrictions.eq("status", ProjectStatus.NEW),
						Restrictions.eq("status", ProjectStatus.INPROGRESS)))

		.add(Restrictions.like("projectName", searchStr, MatchMode.ANYWHERE));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		criteria.setMaxResults(10);

		return criteria.list();
	}

	@Override
	public Map<String, Object> getProjectDetailsEmployeeCountForEmployee(
			Long employeeid, String projectStatus, String health, String type,
			Integer firstIndex, Integer endIndex) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.setFetchMode("project", FetchMode.JOIN);
		criteria.add(Restrictions.eq("employee.employeeId", employeeid));
		if (!projectStatus.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.status",
					ProjectStatus.valueOf(projectStatus)));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.rowCount(), "count")
				.add(Projections.groupProperty("project"), "project"));

		// criteria.addOrder(Order.desc("project.createdDate"));
		if (!type.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.type",
					ProjectType.valueOf(type)));

		if (!health.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.health", health));

		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		List<AllocationDetailsDTO> list = criteria.setResultTransformer(
				Transformers.aliasToBean(AllocationDetailsDTO.class)).list();
		// sorting latest first
		list = sortingProject(list);

		Integer noOfProjects = list.size();
		List<AllocationDetailsDTO> listData = null;
		if (noOfProjects < endIndex) {
			listData = list.subList(firstIndex, noOfProjects);
		} else {
			listData = list.subList(firstIndex, endIndex);
		}
		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("projectList", listData);
		projectMap.put("size", noOfProjects);
		return projectMap;

	}

	@Override
	public Map<String, Object> getAllClients(Integer startIndex,
			Integer endIndex,String selectionStatus) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<Client> clients = new ArrayList<Client>();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Client.class);
	    criteria.add(Restrictions.isNotNull("name"));
		if(selectionStatus.equalsIgnoreCase("Active")){
			criteria.add(Restrictions.eq("clientStatus",Boolean.TRUE));
		}else if(selectionStatus.equalsIgnoreCase("InActive")){
			//logger.warn("status"+selectionStatus);
			criteria.add(Restrictions.eq("clientStatus",Boolean.FALSE));
		}
		
		//Customised Ordering for the String to Int  
		criteria.addOrder(CustomizedOrderBy.sqlFormula("cast(ClientCode as UNSIGNED)"));
		//criteria.addOrder(Order.asc("clientCode"));
		map.put("totalClients", criteria.list().size());
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		clients = criteria.list();

		map.put("clients", clients);

		return map;
	}

	@Override
	public List<Country> getCountries() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Country.class);
		criteria.addOrder(Order.asc("name"));

		return criteria.list();
	}

	@Override
	public Map<String, Object> getBillingReports(Integer startIndex,
			Integer endIndex, String status) {

		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Milestone.class);
		criteria.add(Restrictions.eq("billable", Boolean.TRUE));
		if (!status.equalsIgnoreCase("All")) {
			criteria.add(Restrictions.eq("closed", Boolean.parseBoolean(status)));
			if (status.equalsIgnoreCase("TRUE")) {
				criteria.add(Restrictions.eq("invoiceStatus", Boolean.FALSE));
			}
		}
		// criteria.add(Restrictions.or(Restrictions.and(
		// Restrictions.le("planedDate", new Date()),
		// Restrictions.isNull("actualDate")), Restrictions.le(
		// "actualDate", new Date())));
		criteria.createAlias("project", "project");
		criteria.setFetchMode("project", FetchMode.SELECT);
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		map.put("size", criteria.list().size());
		// criteria.setFirstResult(startIndex);

		// criteria.setMaxResults(endIndex);

		map.put("reports", criteria.list());
		sessionFactory.getCurrentSession().clear();
		return map;
		// Criteria criteria = sessionFactory.getCurrentSession()
		// .createCriteria(Milestone.class) .add(Restrictions.eq("billable",
		// Boolean.TRUE)) .createAlias("project", "project")
		// .addOrder(Order.asc("project.projectName")); map.put("size",
		// criteria.list().size()); criteria.setFirstResult(startIndex);
		// criteria.setMaxResults(endIndex - startIndex); map.put("reports",
		// criteria.list()); return map;
	}

	@Override
	public Map<String, Object> searchClients(String search, Integer firstIndex,
			Integer endIndex,String selectionStatus) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Client> clients = new ArrayList<Client>();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Client.class);
		
		criteria.add(Restrictions.isNotNull("name"));
		criteria.createAlias("country", "country");

		//Customised Ordering for the String to Int  
		criteria.addOrder(CustomizedOrderBy.sqlFormula("cast(ClientCode as UNSIGNED)"));
	//	criteria.addOrder(Order.asc("clientCode"));
		
		/*
		 * Added to Handle the null / empty Search string while the Normal Client Pagination   
		   Previously Two Client Pagination List Services are Used .
		*/ 
		
		if(search!=null){
			if(!search.isEmpty()){
				Criterion byOrgName = Restrictions.or(
						Restrictions.like("organization", search, MatchMode.ANYWHERE),
						Restrictions.like("name", search, MatchMode.ANYWHERE));
				
				Criterion emailOrCountry = Restrictions.or(
						Restrictions.like("email", search, MatchMode.ANYWHERE),
						Restrictions.like("country.name", search, MatchMode.ANYWHERE));
				Criterion anyOne = Restrictions.or(byOrgName, emailOrCountry);
				Criterion result = Restrictions.or(
						Restrictions.like("personName", search, MatchMode.ANYWHERE),
						anyOne);
				criteria.add(result);
			}
		}
		
		if(selectionStatus.equalsIgnoreCase("Active")){
			criteria.add(Restrictions.eq("clientStatus",Boolean.TRUE));
		}else if(selectionStatus.equalsIgnoreCase("InActive")){
			//logger.warn("status"+selectionStatus);
			criteria.add(Restrictions.eq("clientStatus",Boolean.FALSE));
		}
	
		map.put("totalClients", criteria.list().size());
		criteria.setFirstResult(firstIndex);
		criteria.setMaxResults(endIndex - firstIndex);
		clients = criteria.list();
		map.put("clients", clients);

		return map;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Milestone> getUnclosedBillableMilestones() {

		return sessionFactory.getCurrentSession()
				.createCriteria(Milestone.class)
				.add(Restrictions.eq("billable", Boolean.TRUE))
				.add(Restrictions.eq("closed", Boolean.FALSE)).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MilestoneAudit> getAllMileStoneHistory(Long milestoneId) {

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(MilestoneAudit.class);

		criteria.add(Restrictions.eq("milestoneId", milestoneId));

		return criteria.list();

	}

	@Override
	public Map<String, Object> getBillingReportsOnSearch(Integer startIndex,
			Integer endIndex, String status, DateRange dateRange,
			String searchText, String client) {

		Map<String, Object> map = new HashMap<String, Object>();

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Milestone.class);

		criteria.add(Restrictions.eq("billable", Boolean.TRUE));
		criteria.add(Restrictions.eq("invoiceStatus", Boolean.FALSE));

		if (!status.equalsIgnoreCase("All")) {
			criteria.add(Restrictions.eq("closed", Boolean.parseBoolean(status)));
		}
		criteria.createAlias("project", "project").createAlias(
				"project.client", "client");
		
	//	System.out.println("client"+client);
		
		if (searchText != null && !searchText.equalsIgnoreCase("")) {
			System.out.println("in search text");
			//for new requirement i commented the below lines
			
			Criterion criterion1 = Restrictions
					.or(Restrictions.ilike("client.name", searchText,MatchMode.ANYWHERE), Restrictions.ilike("title",
							searchText, MatchMode.ANYWHERE));
			

			/*Criterion criterion1 = Restrictions
					.or(Restrictions.ilike("client.name", searchText,
							MatchMode.ANYWHERE), Restrictions.ilike("title",
							searchText, MatchMode.ANYWHERE));*/

			criteria.add(Restrictions.or(criterion1, Restrictions.ilike(
					"project.projectName", searchText, MatchMode.ANYWHERE)));
		}

	/*	if (client != null) {
			criteria.add(Restrictions.ilike("client.name", client,
					MatchMode.ANYWHERE));

		}*/
		
		if (client != null && !client.equalsIgnoreCase("")) {
			System.out.println("in client"+client);
			criteria.add(Restrictions.eq("client.name", client));
		}
		
		
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(Restrictions.or(Restrictions.between(
					"planedDate", dateRange.getMinimum(),
					dateRange.getMaximum()), Restrictions.between("actualDate",
					dateRange.getMinimum(), dateRange.getMaximum())),
					Restrictions.or(Restrictions.and(
							Restrictions.ge("planedDate",
									dateRange.getMinimum()),
							Restrictions.le("planedDate",
									dateRange.getMaximum())), Restrictions.or(
							Restrictions.and(
									Restrictions.ge("actualDate",
											dateRange.getMinimum()),
									Restrictions.le("actualDate",
											dateRange.getMaximum())),
							Restrictions.and(
									Restrictions.le("planedDate",
											dateRange.getMinimum()),
									Restrictions.ge("actualDate",
											dateRange.getMaximum()))))));

		} else if (dateRange.getMinimum() != null
				&& dateRange.getMaximum() == null) {

			criteria.add(Restrictions.or(
					Restrictions.and(
							Restrictions.ge("planedDate",
									dateRange.getMinimum()),
							Restrictions.isNull("actualDate")),
					Restrictions.ge("actualDate", dateRange.getMinimum())));

			/*
			 * criteria.add(Restrictions.or( Restrictions.ge("planedDate",
			 * dateRange.getMinimum()), Restrictions.ge("actualDate",
			 * dateRange.getMinimum())));
			 */

		} else if (dateRange.getMinimum() == null
				&& dateRange.getMaximum() != null) {

			criteria.add(Restrictions.or(
					Restrictions.and(
							Restrictions.le("planedDate",
									dateRange.getMaximum()),
							Restrictions.isNull("actualDate")),
					Restrictions.le("actualDate", dateRange.getMaximum())));

			/*
			 * criteria.add(Restrictions.or( Restrictions.le("planedDate",
			 * dateRange.getMaximum()), Restrictions.le("actualDate",
			 * dateRange.getMaximum())));
			 */
		}

		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);

		map.put("size", criteria.list().size());
		// criteria.setFirstResult(startIndex);
		// criteria.setMaxResults(endIndex - startIndex);
		map.put("reports", criteria.list());

		return map;
	}

	@Override
	public Map<String, Object> getProjectsOfClients(Integer startIndex,
			Integer endIndex, Long clientId, String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Project.class);
		criteria.add(Restrictions.eq("type", ProjectType.valueOf(type)));
		criteria.createAlias("client", "client").add(
				Restrictions.eq("client.id", clientId));
		map.put("size", criteria.list().size());
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(endIndex - startIndex);
		map.put("reports", criteria.list());
		return map;
	}

	@Override
	public Map<String, Object> getProjectDetailsEmployeeCountAdmin(
			Integer startIndex, Integer endIndex, String client, String type) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.setFetchMode("project", FetchMode.JOIN)
				.add(Restrictions.eq("project.type", ProjectType.valueOf(type)))
				.setProjection(
						Projections
								.projectionList()
								.add(Projections.rowCount(), "count")
								.add(Projections.groupProperty("project"),
										"project"));
		criteria.createAlias("project.client", "client");
		criteria.add(Restrictions.eq("client.name", client));
		// criteria.add(Restrictions.eq("project.status",
		// ProjectStatus.INPROGRESS));

		List<AllocationDetailsDTO> list = criteria.setResultTransformer(
				Transformers.aliasToBean(AllocationDetailsDTO.class)).list();

		Criteria projectCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class);

		if (type != null || type != "") {
			projectCriteria.add(Restrictions.eq("type",
					ProjectType.valueOf(type)));
		}

		projectCriteria.createAlias("client", "client");
		projectCriteria.add(Restrictions.eq("client.name", client));
		projectCriteria.addOrder(Order.asc("projectName"));

		// To avoid the duplications of records
		projectCriteria
				.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		// projectCriteria.add(Restrictions.eq("status",
		// ProjectStatus.INPROGRESS));
		List<Project> projectsList = projectCriteria.list();

		List<Project> projectsSet = new ArrayList<Project>();
		for (Project prj : projectsList) {
			boolean flag = true;
			for (AllocationDetailsDTO allocationDetailsDTO : list) {
				if (prj.getId().equals(
						allocationDetailsDTO.getProject().getId())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				projectsSet.add(prj);
			}
		}

		for (Project addto : projectsSet) {
			AllocationDetailsDTO addto1 = new AllocationDetailsDTO();
			addto1.setProject(addto);
			addto1.setCount(0l);
			list.add(addto1);
		}
		Integer noOfProjects = list.size();
		List<AllocationDetailsDTO> listData = null;
		if (noOfProjects < endIndex) {
			listData = list.subList(startIndex, noOfProjects);
		} else {
			listData = list.subList(startIndex, endIndex);
		}

		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("projectList", listData);
		projectMap.put("size", noOfProjects);
		return projectMap;
	}

	@Override
	public Map<String, Object> searchProjectDetailsEmployeeCountForEmployee(
			Long employeeId, String projectStatus, String type, String health,
			Integer firstIndex, Integer endIndex, String multiSearch,
			DateRange dateRange) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.setFetchMode("project", FetchMode.JOIN);
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		if (!projectStatus.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.status",
					ProjectStatus.valueOf(projectStatus)));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.rowCount(), "count")
				.add(Projections.groupProperty("project"), "project"));

		criteria.addOrder(Order.asc("project.projectName"));
		if (!type.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.type",
					ProjectType.valueOf(type)));
		if (!health.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.health", health));

		if (multiSearch != null) {
			Criterion projectNameCriterion = Restrictions.ilike(
					"project.projectName", multiSearch, MatchMode.ANYWHERE);
			Criterion firstNameCriterion = Restrictions.ilike(
					"projectmanager.employeeFullName", multiSearch,
					MatchMode.ANYWHERE);
			Criterion client = Restrictions.ilike("client.name", multiSearch,
					MatchMode.ANYWHERE);

			Criterion pnorpmcri = Restrictions.or(projectNameCriterion,
					firstNameCriterion);

			Criterion pnorpmcriorClientname = Restrictions
					.or(pnorpmcri, client);
			criteria.add(pnorpmcriorClientname);

		}
		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {
			Criterion criterion4 = Restrictions.and(
					Restrictions.ge("project.period.minimum",
							dateRange.getMinimum()),
					Restrictions.le("project.period.maximum",
							dateRange.getMaximum()));
			criteria.add(criterion4);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			criteria.add(Restrictions.ge("project.period.minimum",
					dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			criteria.add(Restrictions.le("project.period.maximum",
					dateRange.getMaximum()));
		}

		List<AllocationDetailsDTO> list = criteria.setResultTransformer(
				Transformers.aliasToBean(AllocationDetailsDTO.class)).list();

		Criteria projectCriteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class);
		projectCriteria.createAlias("client", "client");
		if (!projectStatus.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("status",
					ProjectStatus.valueOf(projectStatus)));
		projectCriteria.createAlias("projectManager", "projectmanager")
				.setFetchMode("projectmanager", FetchMode.JOIN);

		if (!type.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("type",
					ProjectType.valueOf(type)));
		if (!health.equalsIgnoreCase("All"))
			projectCriteria.add(Restrictions.eq("health", health));

		if (multiSearch != null) {
			Criterion pNameCriterion = Restrictions.ilike("projectName",
					multiSearch, MatchMode.ANYWHERE);
			Criterion fNameCriterion = Restrictions
					.ilike("projectmanager.firstName", multiSearch,
							MatchMode.ANYWHERE);
			Criterion client = Restrictions.ilike("client.name", multiSearch,
					MatchMode.ANYWHERE);
			Criterion pnorpmcri = Restrictions.or(pNameCriterion,
					fNameCriterion);
			Criterion pnorpmcriorClientname = Restrictions
					.or(pnorpmcri, client);
			projectCriteria.add(pnorpmcriorClientname);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() != null) {
			Criterion pcriterion4 = Restrictions.and(
					Restrictions.ge("period.minimum", dateRange.getMinimum()),
					Restrictions.le("period.maximum", dateRange.getMaximum()));
			projectCriteria.add(pcriterion4);
		}

		if (dateRange.getMinimum() != null && dateRange.getMaximum() == null) {
			projectCriteria.add(Restrictions.ge("period.minimum",
					dateRange.getMinimum()));
		}

		if (dateRange.getMinimum() == null && dateRange.getMaximum() != null) {
			projectCriteria.add(Restrictions.le("period.maximum",
					dateRange.getMaximum()));
		}
		Set<Project> projects = new HashSet<Project>(projectCriteria.list());
		List<Project> projectsList = new ArrayList<Project>(projects);
		// List<Project> projectsList = projectCriteria.list();
		List<Project> projectsSet = new ArrayList<Project>();
		for (Project prj : projectsList) {
			boolean flag = true;
			for (AllocationDetailsDTO allocationDetailsDTO : list) {
				if (prj.getId().equals(
						allocationDetailsDTO.getProject().getId())) {
					flag = false;
					break;
				}
			}
			if (flag) {
				projectsSet.add(prj);
			}
		}

		for (Project addto : projectsSet) {
			AllocationDetailsDTO addto1 = new AllocationDetailsDTO();
			addto1.setProject(addto);
			addto1.setCount(0l);
			list.add(addto1);
		}
		// sorting latest first
		list = sortingProject(list);

		Integer noOfProjects = list.size();
		List<AllocationDetailsDTO> listData = null;
		if (noOfProjects < endIndex) {
			listData = list.subList(firstIndex, noOfProjects);
		} else {
			listData = list.subList(firstIndex, endIndex);
		}

		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("projectList", listData);
		projectMap.put("size", noOfProjects);
		return projectMap;
	}

	@Override
	public List<AllocationDetails> getBillableResources(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.createAlias("project", "project")
				.setFetchMode("project", FetchMode.JOIN)
				.add(Restrictions.eq("project.id", projectId))
				.add(Restrictions.eq("billable", Boolean.TRUE))
				.add(Restrictions.eq("isAllocated", Boolean.TRUE));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Client> getclients() {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Client.class);
		criteria.addOrder(Order.asc("name"));

		return criteria.list();

	}

	@Override
	public Double getAllMilestonePercentageCount(Project project) {

		Double percentage = 0.0;
		Double parsedPercentage = 0.0;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Milestone.class);
		criteria.add(Restrictions.eq("project", project));
		criteria.add(Restrictions.eq("billable", Boolean.TRUE));
		criteria.add(Restrictions.isNull("changeRequest"));
		List<Milestone> milestonelist = criteria.list();

		if (project.getType().toString().equalsIgnoreCase("fixedbid")) {
			for (Milestone milestone : milestonelist) {
				String milestonePercentage = milestone.getMilestonePercentage();
				if ((!milestonePercentage.isEmpty())
						&& milestonePercentage != null
						&& milestonePercentage != "") {
					parsedPercentage += Double.valueOf(milestonePercentage);
					//logger.warn("parsedPercentage"+parsedPercentage);
				}
				/*if (milestone.isBillable()
						&& (milestonePercentage != null || milestonePercentage != "")) {
					percentage += parsedPercentage;
					logger.warn("milestone percentage"+percentage);
				}*/

			}

		}

		return parsedPercentage;
	}

	@Override
	public Double getRaisedCrPercentage(ChangeRequest changeRequest,
			Long milestoneId) {

		Double percentage = 0.0;
		Double parsedPercentage = 0.0;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Milestone.class);

		// here if control comes from edit of milestone milestone id will be
		// available so we are using those in Restrictions
		if (milestoneId != null) {
			criteria.add(Restrictions.ne("id", milestoneId));
		}
		criteria.add(Restrictions.eq("changeRequest", changeRequest));
		criteria.add(Restrictions.eq("billable", Boolean.TRUE));

		List<Milestone> milestonelist = HibernateSupressWaringsUtil
				.listAndCast(criteria);

		for (Milestone milestone : milestonelist) {
			String milestonePercentage = milestone.getMilestonePercentage();
			if(milestonePercentage != null){
			if ((!milestonePercentage.isEmpty()) && 
					 milestonePercentage != "") {
				parsedPercentage += Double.valueOf(milestonePercentage);
			}
			}
			/*if (milestone.isBillable()
					&& (milestonePercentage != null || milestonePercentage != "")) {
				percentage += parsedPercentage;
			}*/

		}

		return parsedPercentage;
	}

	@Override
	public List<ProjectNumbers> getProjectNumbers(Project projectId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectNumbers.class);
		criteria.add(Restrictions.eq("project", projectId));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public Map<String, Object> getChangeRequestDTOList(Long projectId,
			Integer firstIndex, Integer endIndex) {
		Map<String, Object> map = new HashMap<String, Object>();
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ChangeRequest.class);
		criteria.add(Restrictions.eq("projectId", projectId));
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		map.put("size", criteria.list().size());
		map.put("list", criteria.list());
		// List<ChangeRequest> changeRequests = criteria.list();
		// return changeRequestBuilder.toListDto(changeRequests);
		return map;

	}

	/*
	 * @Override public Map<String, Object>
	 * getAllStatusReports_UnderProject(Long projectId, Integer firstIndex,
	 * Integer endIndex) {
	 * 
	 * Map<String, Object> map = new HashMap<String, Object>(); Criteria
	 * criteria = sessionFactory.getCurrentSession().createCriteria(
	 * StatusReport.class); criteria.createAlias("project",
	 * "project").setFetchMode("project", FetchMode.JOIN);
	 * criteria.add(Restrictions.eq("project.id", projectId));
	 * criteria.addOrder(Order.desc("id"));
	 * criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
	 * map.put("size", criteria.list().size()); map.put("list",
	 * criteria.list()); return map;
	 * 
	 * }
	 */

	@Override
	public List<ChangeRequestDTO> getCRlookupForProjectNumbers(Long projectID) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ChangeRequest.class);
		criteria.add(Restrictions.eq("projectId", projectID));
		criteria.add(Restrictions.eq("numbersStatus", Boolean.FALSE));
		return criteria.list();
	}

	@Override
	public List<ChangeRequestDTO> getCRListForMilestone(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ChangeRequest.class);
		criteria.add(Restrictions.eq("projectId", projectId));
		criteria.add(Restrictions.eq("milestoneStatus", Boolean.FALSE));

		return criteria.list();
	}

	@Override
	public void updateCRNumbers(ProjectNumbersDTO dto) {

		Milestone milestone = dao.findByUniqueProperty(Milestone.class,
				"changeRequest",
				dao.findBy(ChangeRequest.class, dto.getChangeRequestId()));

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Invoice.class);
		criteria.add(Restrictions.eq("milsestone", milestone));

		Invoice invoice = (Invoice) criteria.uniqueResult();

		if (invoice == null) {
			dao.update(projectNumbersBuilder.toEntity(dto));
		} else {
			throw new ProjectNumbersUpdationException(
					"can't Update CR numbers, Already Invoice raised for this CR");
		}
	}

	// Here This method is written to prevent DEAD lock in session as we are
	// getting Error
	// during adding and updating a project
	@Override
	public <T extends Serializable> List<T> findManagers(Class<T> clazz) {
		List<T> managetlist = sessionFactory
				.getCurrentSession()
				.createCriteria(clazz)
				.add(Restrictions.or(Restrictions.eq("role", "Manager"),
						Restrictions.eq("role", "admin")))
				.add(Restrictions.ne("employeeId", 1000L)).list();
		sessionFactory.getCurrentSession().clear();
		return managetlist;
	}

	@Override
	public Map<String, Object> getAllProjectRequests(Integer firstIndex,
			Integer endIndex, String multiSearch) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectRequest.class);

		if (!multiSearch.isEmpty()) {
			criteria.add(Restrictions.ilike("projectName", multiSearch,
					MatchMode.ANYWHERE));
		}
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		/*List<ProjectRequestDTO> list = criteria.list();
		Integer noOfRecords = list.size();

		List<ProjectRequestDTO> listData = null;
		if (noOfRecords < endIndex) {
			listData = list.subList(firstIndex, noOfRecords);
		} else {
			listData = list.subList(firstIndex, endIndex);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectRequestList", listData);
		map.put("size", noOfRecords);
		return map;*/
		
		Map<String, Object> criteriaMap = getPaginationList(criteria, firstIndex, endIndex);
		Map<String, Object> requestListMap = new HashMap<String, Object>();
		requestListMap.put("projectRequestList", criteriaMap.get("list"));
		requestListMap.put("size", criteriaMap.get("listSize"));
		System.out.println("size 2187:" + criteriaMap.get("listSize"));
		return requestListMap;
	}

	@Override
	public Map<String, Object> getAllProjectRequestFor(List<Long> employeeIds,
			Integer firstIndex, Integer endIndex, String multiSearch) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectRequest.class);
		criteria.createAlias("projectManager", "employee");
		criteria.add(Restrictions.in("employee.employeeId", employeeIds));

		if (!multiSearch.isEmpty()) {
			criteria.add(Restrictions.ilike("projectName", multiSearch,
					MatchMode.ANYWHERE));
		}
		criteria.addOrder(Order.desc("employee.employeeId"));
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);

		List<ProjectRequestDTO> list = criteria.list();
		Integer noOfRecords = list.size();

		/*List<ProjectRequestDTO> listData = null;
		if (noOfRecords < endIndex) {
			listData = list.subList(firstIndex, noOfRecords);
		} else {
			listData = list.subList(firstIndex, endIndex);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("projectRequestList", listData);
		map.put("size", noOfRecords);
		return map;*/
		
		Map<String, Object> criteriaMap = getPaginationList(criteria, firstIndex, endIndex);
		Map<String, Object> requestListMap = new HashMap<String, Object>();
		requestListMap.put("projectRequestList", criteriaMap.get("list"));
		requestListMap.put("size", criteriaMap.get("listSize"));
		System.out.println("size 2225:" + criteriaMap.get("listSize"));
		return requestListMap;
		
	}

	@Override
	public Map<String, Object> getProjectDetailsForEmployeeProfile(
			Long employeeid, String projectStatus, String type,
			Integer firstIndex, Integer endIndex) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class);
		//for new requirement i commented below line
				//.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.setFetchMode("project", FetchMode.JOIN);
		criteria.add(Restrictions.eq("employee.employeeId", employeeid));
		if (!projectStatus.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.status",
					ProjectStatus.valueOf(projectStatus)));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.rowCount(), "count")
				.add(Projections.groupProperty("project"), "project"));

		criteria.addOrder(Order.asc("project.projectName"));
		if (!type.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.type",
					ProjectType.valueOf(type)));

		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		List<AllocationDetailsDTO> list = criteria.setResultTransformer(
				Transformers.aliasToBean(AllocationDetailsDTO.class)).list();

		Integer noOfProjects = list.size();
		List<AllocationDetailsDTO> listData = null;
		if (noOfProjects < endIndex) {
			listData = list.subList(firstIndex, noOfProjects);
		} else {
			listData = list.subList(firstIndex, endIndex);
		}

		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("projectList", listData);
		projectMap.put("size", noOfProjects);
		return projectMap;
	}

	@Override
	public List<ProjectAudit> getProjectHistory(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectAudit.class);
		criteria.add(Restrictions.eq("projectId", projectId));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public List<AllocationDetailsAudit> getAllocationHistory(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetailsAudit.class);
		criteria.add(Restrictions.eq("projectId", projectId));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public Integer getApprovedLeavesOfEmployeeBetweenDateRange(String fromDate,
			String todate, Employee empId) {

		Date startDate = null;
		Date endDate = null;

		try {
			startDate = DateParser.toDate(fromDate);
			if (todate != null)
				endDate = DateParser.toDate(todate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(LeaveDebit.class)
				.add(Restrictions.eq("employee", empId))
				.add(Restrictions.eq("status", LeaveStatus.Approved))
				.add(Restrictions.ge("period.minimum", startDate))
				.add(Restrictions.le("period.maximum", endDate));

		List<LeaveDebit> leaveDebits = criteria.list();
		int leaves = 0;
		for (LeaveDebit debit : leaveDebits) {
			leaves += debit.getNumberOfDays();
		}

		return leaves;
	}

	public List<AllocationDetailsDTO> sortingProject(
			List<AllocationDetailsDTO> list) {

		if (list != null) {
			Collections.sort(list, new Comparator<AllocationDetailsDTO>() {
				@Override
				public int compare(AllocationDetailsDTO o1,
						AllocationDetailsDTO o2) {
					int k = 0;
					if (o1.getProject()
							.getCreatedDate()
							.toString()
							.equalsIgnoreCase(
									o2.getProject().getCreatedDate().toString())) {

						k = (int) (o2.getProject().getId() - o1.getProject()
								.getId());
					} else {
						if (o1.getProject().getCreatedDate()
								.isAfter(o2.getProject().getCreatedDate())) {
							k = -1;
						}
						if (o1.getProject().getCreatedDate()
								.isBefore(o2.getProject().getCreatedDate())) {
							k = 1;
						}
					}
					return k;

				}
			});
		}

		return list;
	}

	@Override
	public Map<String, List<Audit>> getProjectAudit(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Audit.class);
		criteria.add(Restrictions.eq("referenceId", projectId));
		criteria.add(Restrictions.or(
				Restrictions.ilike("tableName", "PROJECT"),
				Restrictions.ilike("tableName", "ALLOCATIONDETAILS")));
		// Map<String,List<Audit>> mapOfAudits=new HashMap<String,
		// List<Audit>>();

		List<Audit> audits = criteria.list();
		// List<Audit> lists=new ArrayList<Audit>();
		Map<String, List<Audit>> map = null;
		if (!audits.isEmpty())
			map = getPairValue(audits);
		return map;

	}

	// Checks whether the milestone is already raised for this project
	@Override
	public Boolean isMilestoneExistsForProject(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Milestone.class);
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.eq("project.id", projectId));
		if (criteria.list().isEmpty())
			return false;
		else
			return true;

	}

	@Override
	public List<Invoice> getInvoiceListOfProject(Project projectId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Invoice.class);
		criteria.createAlias("milsestone", "milestone");
		criteria.add(Restrictions.eq("milestone.project", projectId));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public Integer allocationSizeOfProjectForEmployee(Project projectid) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.add(Restrictions.eq("project", projectid))
				.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		return criteria.list().size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BioAttendance> getBioAttendanceOfEmployeeBetweenDateRange(
			Date fromDate, Date toDate, Employee employee) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(BioAttendance.class)
				.add(Restrictions.between("attendanceDate", fromDate, toDate))
				.add(Restrictions.eq("attendanceStatus", AttendanceStatus.A))
				.add(Restrictions.eq("employee", employee));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Holidays> getHolidaysBetweenDates(Date fromDate, Date toDate) {

		return sessionFactory.getCurrentSession()
				.createCriteria(Holidays.class)
				.add(Restrictions.between("date", fromDate, toDate)).list();
	}

	@Override
	public Milestone getrecentlyInsertedMilestone(Project project) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Milestone.class);
		criteria.add(Restrictions.eq("project", project));
		criteria.addOrder(Order.desc("id"));
		criteria.setMaxResults(1);
		return (Milestone) criteria.uniqueResult();
	}

	@Override
	public List<AllocationDetails> getallocateresources() {

		Date date = new Date();
		int i = 0;
		while (i <= 5) {
			date = date.next();
			i++;
		}
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.add(Restrictions.eq("isAllocated", true));
		criteria.add(Restrictions.between("period.maximum", new Date(), date));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Project> getProjectWhoseEndDateisInNextFiveDays() {

		Date date = new Date();
		int i = 0;
		while (i <= 5) {
			date = date.next();
			i++;
		}
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class)
				.add(Restrictions.eq("status", ProjectStatus.INPROGRESS))
				.add(Restrictions.between("period.maximum", new Date(), date));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	@Override
	public List<Long> getAllocatedEmps(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.createAlias("project", "project");
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.setProjection(Projections.projectionList().add(
				Projections.property("employee.employeeId")));

		return criteria.list();
	}

	@Override
	public List<Long> getEmployeesForProjectHiveReport(Long projectId,
			String hivedate) {

		String h[] = hivedate.split("/");

		MonthOfYear monthOfYear = MonthOfYear.valueOf(Integer.parseInt(h[0]));
		YearOfEra yearOfEra = YearOfEra.valueOf(Integer.parseInt(h[1]));

		DateRange monthPeriod = leaveManagementUtils
				.getConstructMonthPeriod(new MonthYear(monthOfYear, yearOfEra));

		Date startDate = null;
		Date endDate = null;
		try {
			startDate = DateParser.toDate(monthPeriod.getMinimum().toString(
					"dd/MM/yyyy"));
			endDate = DateParser.toDate(monthPeriod.getMaximum().toString(
					"dd/MM/yyyy"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.createAlias("project", "project");
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.add(Restrictions.eq("employee.statusName", "Active"));
		criteria.add(Restrictions.ge("period.maximum", startDate));
		criteria.setProjection(Projections.projectionList().add(
				Projections.property("employee.employeeId")));

		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);

		return criteria.list();
	}

	public void deAllocateWhenInactive(Long id) {
		Date date = new Date();
		Query query = sessionFactory
				.getCurrentSession()
				.createQuery(
						"update AllocationDetails p set p.isAllocated= :allocation,"
								+ " p.period.maximum = :deallocatedDate where p.employee.employeeId = "
								+ id + " and p.isAllocated like "
								+ Boolean.TRUE);
		query.setParameter("allocation", Boolean.FALSE);
		query.setParameter("deallocatedDate", date);
		query.executeUpdate();
	}

	@Override
	public ProjectRequestMail getProjectRequestCCandBCC() {
		return (ProjectRequestMail) sessionFactory.getCurrentSession()
				.createCriteria(ProjectRequestMail.class).uniqueResult();
	}

	@SuppressWarnings({ "static-access", "unchecked" })
	@Override
	public List<Project> getProjectsWhoseStartDateInLastFiveDays() {

		Date currentDate = new Date();

		Date previousdate = new Date(DayOfMonth.valueOf(new Date()
				.getDayOfMonth().getValue() - 5),
				MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
				YearOfEra.valueOf(new Date().getYearOfEra().getValue()));

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Project.class);
		criteria.add(Restrictions.between("period.minimum", previousdate,
				currentDate));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public Employee getDeliveryManagerofProject(Project project) {
		//System.out.println("project id:"+project.getId());
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationDetails.class);
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.eq("project", project));
		criteria.add(Restrictions.like("employee.role", "Delivery Manager"));
		criteria.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		AllocationDetails details = (AllocationDetails) criteria.uniqueResult();
		return (details != null) ? details.getEmployee() : null;
	}

	@Override
	public Boolean checkForRequestId(Long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Project.class);
		criteria.add(Restrictions.eq("projectRequest.id", id));
		return criteria.list().isEmpty();
	}

	@Override
	public List<Project> getProjectList(Long clientId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Project.class);
		criteria.createAlias("client", "client");
		criteria.add(Restrictions.eq("client.id", clientId));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<Audit> getAuditForProjectRequest(Long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Audit.class);
		criteria.add(Restrictions.eq("referenceId", id));
		criteria.add(Restrictions.eq("tableName", "PROJECTREQUEST"));
		criteria.addOrder(Order.asc("modifiedDate"));

		return criteria.list();
	}

	@Override
	public List<ProjectCheckList> getChecklist() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectCheckList.class);
		criteria.add(Restrictions.eq("type", "project"));
		return criteria.list();
	}

	@Override
	public Boolean checkClientOrg(String organization) {

		Boolean isOrgExist;
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Client.class);
		criteria.add(Restrictions.eq("organization", organization));
		if (criteria.list().size() > 0) {
			isOrgExist = true;
		} else {
			isOrgExist = false;
		}

		return isOrgExist;
	}

	/*
	 * @Override public ProjectEffort getProjectEffort(String hiveProjectName) {
	 * 
	 * Criteria
	 * criteria=sessionFactory.getCurrentSession().createCriteria(ProjectEffort
	 * .class); criteria.add(Restrictions.eq("projectName", hiveProjectName));
	 * ProjectEffort projectEffort=(ProjectEffort) criteria.uniqueResult();
	 * return projectEffort; }
	 */

	@Override
	public List<AllocationEffort> getAllocationDetails(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationEffort.class);
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}


	@Override
	public List<AllocationEffort> getEmployeeRecords(Long projectId, Long empId) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AllocationEffort.class);
		criteria.createAlias("project", "project");
		criteria.createAlias("employee", "employee");
		criteria.add(Restrictions.and(Restrictions.eq("project.id", projectId),
				Restrictions.eq("employee.id", empId)));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder(Order.asc("id"));
		return criteria.list();
	}


	@Override
	public List<Project> getActiveProjects(Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(Project.class)
				.createAlias("projectManager", "mgr");
		criteria.add(Restrictions.eq("mgr.id", employeeId)).add(
				Restrictions.eq("status", ProjectStatus.INPROGRESS));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	@Override
	public ProjectNumbers getProjectNumbers(Milestone milestone) {
		//Creating Criteria
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				ProjectNumbers.class);
		//checking whether it is a cr or not
		if(milestone.getChangeRequest()!=null) {
			//if cr only loading cr numbers
			criteria.add(Restrictions.eq("changeRequest", milestone.getChangeRequest()));
		}else {
			// if it is normal fixed bid Milestone then loading Project Numbers
			criteria.add(Restrictions.eq("project", milestone.getProject()));
			criteria.add(Restrictions.isNull("changeRequest"));
		}
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		
		return (ProjectNumbers) criteria.uniqueResult();
		
	}

	@Override
	public Map<String,Object>getNonClosedMilestoneList(Employee employeeId,Integer fromIndex,Integer toIndex) {
		Criteria criteria =sessionFactory.getCurrentSession().createCriteria(Milestone.class);
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.eq("project.projectManager", employeeId));
		criteria.add(Restrictions.eq("closed", Boolean.FALSE));
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		Map<String, Object> milestoneList =new HashMap<String, Object>();
		milestoneList.put("list", criteria.list());
		milestoneList.put("size", criteria.list().size());
		return milestoneList;
	}

	@Override
	public List<Project> getAllActiveProjects_UnderClient(Long clientId) {
		Criteria criteria =sessionFactory.getCurrentSession().createCriteria(Project.class);
		criteria.createAlias("client", "client");
		criteria.add(Restrictions.eq("client.id", clientId));
		
		criteria.add(Restrictions.ne("status", ProjectStatus.CLOSED));
		
		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		
		//logger.warn("size"+criteria.list().size());
				
		return criteria.list();
	}

	
	@Override
	public Map<String, Object> getEmployeeProjectslist(Long employeeid,
			String projectStatus, String type, Integer firstIndex,
			Integer endIndex) {
		
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class);
				//criteria.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.setFetchMode("project", FetchMode.JOIN);
		criteria.add(Restrictions.eq("employee.employeeId", employeeid));
		if (!projectStatus.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.status",
					ProjectStatus.valueOf(projectStatus)));

		criteria.setProjection(Projections.projectionList()
				.add(Projections.rowCount(), "count")
				.add(Projections.groupProperty("project"), "project"));

		criteria.addOrder(Order.asc("project.projectName"));
		if (!type.equalsIgnoreCase("All"))
			criteria.add(Restrictions.eq("project.type",
					ProjectType.valueOf(type)));

		criteria.setResultTransformer(criteria.DISTINCT_ROOT_ENTITY);
		List<AllocationDetailsDTO> list = criteria.setResultTransformer(
				Transformers.aliasToBean(AllocationDetailsDTO.class)).list();

		Integer noOfProjects = list.size();
		List<AllocationDetailsDTO> listData = null;
		if (noOfProjects < endIndex) {
			listData = list.subList(firstIndex, noOfProjects);
		} else {
			listData = list.subList(firstIndex, endIndex);
		}

		Map<String, Object> projectMap = new HashMap<String, Object>();
		projectMap.put("projectList", listData);
		projectMap.put("size", noOfProjects);
		return projectMap;
	}

	/*@Override
	public List<Employee> EmployeesUnderProjectManager(Long empId) {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
		criteria.createAlias("projectManager", "employee");
		criteria.add(Restrictions.eq("employee.employeeId", "empId"));
	
		return criteria.list();
	}*/
	
	
	@Override
	public List<AllocationDetails> getEmployeeProjectDetails(Long projectId) {

		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(AllocationDetails.class)
				.createAlias("project", "project")
				.setFetchMode("project", FetchMode.JOIN)
				.add(Restrictions.eq("project.id", projectId))
				.add(Restrictions.eq("isAllocated", true));
			
				//.add(Restrictions.eq("isAllocated", Boolean.TRUE));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<EmpDepartment> getAllocationDepartments(
			List<String> deppartmnets) {
		Criteria criteria = sessionFactory.getCurrentSession()
				.createCriteria(EmpDepartment.class);
		if(!deppartmnets.isEmpty())
		{
		criteria.add(Restrictions.in("departmentName", deppartmnets));
		}
		
		return criteria.list();
	}
	@Override
	public List<String> projectSupportedDepartments() {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmpDepartment.class);
		criteria.add(Restrictions.eq("allocationSupportFlag", Boolean.TRUE));
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.property("departmentName"));
		criteria.setProjection(projections);
		List<String> departmentNames = criteria.list();
		return departmentNames;
	}
	@Override
	public List<String> projectnotSupportedDepartments() {
		
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(EmpDepartment.class);
		criteria.add(Restrictions.eq("allocationSupportFlag", Boolean.FALSE));
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.property("departmentName"));
		criteria.setProjection(projections);
		List<String> departmentNames = criteria.list();
		return departmentNames;
	}
	@Override
	public List<Employee> getNotAllocatedEmployeeDepartmentEmployees(List<String> departmentName,List<Long> employeeIds) {
	 Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Employee.class);
	 criteria.add(Restrictions.in("employeeId", employeeIds));
	 criteria.add(Restrictions.in("departmentName", departmentName));
		return criteria.list();
	}

	@Override
	public List<Milestone> getClosedMilesonesForProject(Long projectId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Milestone.class);
		criteria.add(Restrictions.eq("billable",Boolean.TRUE));
		criteria.createAlias("project", "project");
		criteria.add(Restrictions.eq("project.id", projectId));
		criteria.add(Restrictions.eq("closed", Boolean.TRUE));
		criteria.addOrder(Order.desc("id"));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public List<ChangeRequest> getCrsUnderProject(Long projectId) {
	Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ChangeRequest.class);
	criteria.add(Restrictions.eq("projectId", projectId));
		return criteria.list();
	}

	@Override
	public List<Milestone> getMilestonesUnderCr(Long id) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Milestone.class);
		criteria.createAlias("changeRequest", "changeRequest");
		criteria.add(Restrictions.eq("changeRequest.id", id));
		return criteria.list();
	}

	@Override
	public List<Project> getAllInprogressProjects() {
		Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(Project.class)
				.add(Restrictions.eq("status", ProjectStatus.INPROGRESS));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}
	
	
}
