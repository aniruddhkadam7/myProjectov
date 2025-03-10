/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisalmanagement.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.raybiztech.appraisalmanagement.business.AppraisalCycle;
import com.raybiztech.appraisalmanagement.business.AppraisalForm;
import com.raybiztech.appraisalmanagement.business.Designation;
import com.raybiztech.appraisalmanagement.business.DesignationKRAMapping;
import com.raybiztech.appraisalmanagement.business.FormStatus;
import com.raybiztech.appraisalmanagement.business.KPI;
import com.raybiztech.appraisalmanagement.business.KRA;
import com.raybiztech.appraisalmanagement.business.ReviewAudit;
import com.raybiztech.appraisalmanagement.dto.SearchQueryParamsInAppraisalForm;
import com.raybiztech.appraisalmanagement.dto.SearchQueryParamsInKRA;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAOImpl;
import com.raybiztech.appraisals.exceptions.DesignationNotAssginedException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.separation.builder.SeparationBuilder;

import org.hibernate.type.IntegerType;

/**
 *
 * @author naresh
 */
@Repository("appraisalDao")
public class AppraisalDaoImpl extends DAOImpl implements AppraisalDao {

	@Autowired
	SeparationBuilder separationBuilder;
	@Autowired
	SecurityUtils utils;
	@Autowired
	ProjectService projectService;

	@Override
	public AppraisalCycle getcurrentYearAppraisalCycle() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalCycle.class);
		criteria.add(Restrictions.eq("active", Boolean.TRUE));
		return (AppraisalCycle) criteria.uniqueResult();
	}

	@Override
	public DesignationKRAMapping getAllKrasUnderDesignation(
			String designationName, String departmentName) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				DesignationKRAMapping.class);
		criteria.createAlias("designation", "designation");
		criteria.createAlias("designation.empDepartment", "department");
		criteria.add(Restrictions.eq("designation.name", designationName));
		criteria.add(Restrictions.eq("department.departmentName",
				departmentName));
		criteria.createAlias("cycle", "cycle");
		criteria.add(Restrictions.eq("cycle.active", Boolean.TRUE));
		criteria.add(Restrictions.le("cycle.configurationPeriod.minimum",
				new Date()));
		List<DesignationKRAMapping> designationKRAMappings = criteria.list();
		Integer desgSize = designationKRAMappings.size();
		if (desgSize > 1) {
			return designationKRAMappings.get(desgSize - 1);
		} else {
			if (desgSize == 0) {
				throw new DesignationNotAssginedException();
			}
			return designationKRAMappings.get(0);
		}
	}

	@Override
	public AppraisalForm activeCycleApprailsalForm(Long employeeId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalForm.class);
		criteria.createAlias("employee", "employee");
		criteria.createAlias("appraisalCycle", "appraisalCycle");
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		criteria.add(Restrictions.eq("appraisalCycle.active", Boolean.TRUE));
		return (AppraisalForm) criteria.uniqueResult();
	}

	@Override
	public Map<String, Object> getAllAppraisalForm(Integer startIndex,
			Integer endIndex, Long cycleId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalForm.class);

		criteria.createAlias("appraisalCycle", "appraisalCycle");

		criteria.add(Restrictions.eq("appraisalCycle.id", cycleId));
		criteria.add(Restrictions.ne("formStatus", FormStatus.SAVE));
		// criteria.add(Restrictions.or(Restrictions.eq("formStatus",
		// FormStatus.PENDING), Restrictions.or(
		// Restrictions.eq("formStatus", FormStatus.SUBMIT),Restrictions.or(
		// Restrictions.eq("formStatus",
		// FormStatus.COMPLETED),Restrictions.or(Restrictions.eq("formStatus",
		// FormStatus.PENDINGAGREEMENT), Restrictions.eq("formStatus",
		// FormStatus.OPENFORDISCUSSION))))));
		criteria.createAlias("employee", "employee");
		criteria.addOrder(Order.asc("employee.employeeId"));

		Integer noOfRecotds = criteria.list().size();
		criteria.setMaxResults(endIndex - startIndex);
		criteria.setFirstResult(startIndex);

		List<AppraisalForm> appraisalForms = criteria.list();
		Map<String, Object> appraisalFormMap = new HashMap<String, Object>();
		appraisalFormMap.put("list", appraisalForms);
		appraisalFormMap.put("size", noOfRecotds);
		return appraisalFormMap;

	}

	@Override
	public List<AppraisalCycle> getActiveCyclesOtharthan(
			AppraisalCycle appraisalCycle) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalCycle.class);
		criteria.add(Restrictions.eq("active", Boolean.TRUE));
		criteria.add(Restrictions.ne("id", appraisalCycle.getId()));
		return criteria.list();
	}

	@Override
	public List<AppraisalCycle> getActiveCycles() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalCycle.class);
		criteria.add(Restrictions.eq("active", Boolean.TRUE));
		return criteria.list();
	}

	@Override
	public Map<String, Object> getManagerAppraisalForms(Integer startIndex,
			Integer endIndex, Long employeeID, Long cycleId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalForm.class);
		criteria.createAlias("employee", "employee");
		// criteria.createAlias("employee.manager", "manager");
		// criteria.add(Restrictions.eq("manager.employeeId", employeeID));
		criteria.add(Restrictions.ilike("managesList",
				String.valueOf(employeeID), MatchMode.ANYWHERE));
		criteria.createAlias("appraisalCycle", "appraisalCycle");

		criteria.add(Restrictions.eq("appraisalCycle.id", cycleId));
		criteria.add(Restrictions.ne("formStatus", FormStatus.SAVE));
		// criteria.add(Restrictions.or(Restrictions.eq("formStatus",
		// FormStatus.PENDING), Restrictions.or(
		// Restrictions.eq("formStatus", FormStatus.SUBMIT),Restrictions.or(
		// Restrictions.eq("formStatus",
		// FormStatus.COMPLETED),Restrictions.or(Restrictions.eq("formStatus",
		// FormStatus.PENDINGAGREEMENT), Restrictions.eq("formStatus",
		// FormStatus.OPENFORDISCUSSION))))));
		criteria.addOrder(Order.asc("employee.employeeId"));

		Integer noOfRecotds = criteria.list().size();
		criteria.setMaxResults(endIndex - startIndex);
		criteria.setFirstResult(startIndex);

		List<AppraisalForm> appraisalForms = criteria.list();
		Map<String, Object> appraisalFormMap = new HashMap<String, Object>();
		appraisalFormMap.put("list", appraisalForms);
		appraisalFormMap.put("size", noOfRecotds);
		return appraisalFormMap;
	}

	@Override
	public List<AppraisalCycle> getOverriddenCycles(
			AppraisalCycle appraisalCycle) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalCycle.class);

		Criterion criterion1 = Restrictions.between(
				"configurationPeriod.minimum", appraisalCycle
						.getConfigurationPeriod().getMinimum(), appraisalCycle
						.getConfigurationPeriod().getMaximum());
		Criterion criterion2 = Restrictions.between(
				"configurationPeriod.maximum", appraisalCycle
						.getConfigurationPeriod().getMinimum(), appraisalCycle
						.getConfigurationPeriod().getMaximum());

		Criterion criterion3 = Restrictions.and(Restrictions.le(
				"configurationPeriod.minimum", appraisalCycle
						.getConfigurationPeriod().getMinimum()), Restrictions
				.ge("configurationPeriod.maximum", appraisalCycle
						.getConfigurationPeriod().getMaximum()));

		Criterion criterion4 = Restrictions.and(Restrictions.ge(
				"configurationPeriod.minimum", appraisalCycle
						.getConfigurationPeriod().getMinimum()), Restrictions
				.le("configurationPeriod.maximum", appraisalCycle
						.getConfigurationPeriod().getMaximum()));

		criteria.add(Restrictions.or(Restrictions.or(criterion1, criterion2),
				Restrictions.or(criterion3, criterion4)));

		return criteria.list();
	}

	@Override
	public DesignationKRAMapping getAllKrasUnderCyclewithDesignation(
			Long cycleId, Long designationId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				DesignationKRAMapping.class);
		criteria.createAlias("designation", "designation");
		criteria.createAlias("cycle", "cycle");
		criteria.add(Restrictions.eq("designation.id", designationId));
		criteria.add(Restrictions.eq("cycle.id", cycleId));
		return (DesignationKRAMapping) criteria.uniqueResult();
	}

	@Override
	public List<AppraisalCycle> getCycles(String name) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalCycle.class);
		criteria.add(Restrictions.eq("name", name));
		return criteria.list();
	}

	@Override
	public List<AppraisalCycle> getOverriddenCycles(
			AppraisalCycle appraisalCycle, Long id) {

		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalCycle.class);

		Criterion criterion1 = Restrictions.between(
				"configurationPeriod.minimum", appraisalCycle
						.getConfigurationPeriod().getMinimum(), appraisalCycle
						.getConfigurationPeriod().getMaximum());
		Criterion criterion2 = Restrictions.between(
				"configurationPeriod.maximum", appraisalCycle
						.getConfigurationPeriod().getMinimum(), appraisalCycle
						.getConfigurationPeriod().getMaximum());

		Criterion criterion3 = Restrictions.and(Restrictions.le(
				"configurationPeriod.minimum", appraisalCycle
						.getConfigurationPeriod().getMinimum()), Restrictions
				.ge("configurationPeriod.maximum", appraisalCycle
						.getConfigurationPeriod().getMaximum()));

		Criterion criterion4 = Restrictions.and(Restrictions.ge(
				"configurationPeriod.minimum", appraisalCycle
						.getConfigurationPeriod().getMinimum()), Restrictions
				.le("configurationPeriod.maximum", appraisalCycle
						.getConfigurationPeriod().getMaximum()));

		criteria.add(Restrictions.or(Restrictions.or(criterion1, criterion2),
				Restrictions.or(criterion3, criterion4)));

		criteria.add(Restrictions.ne("id", id));

		return criteria.list();
	}

	@Override
	public Map<String, Object> getAllAppraisalFormByAdmin(
			SearchQueryParamsInAppraisalForm paramsInAppraisalForm,
			List<Long> appraisalCycleIds) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalForm.class);
		Map<String, Object> listOfApraisals = new HashMap<String, Object>();
		criteria.createAlias("employee", "employee");
		criteria.createAlias("appraisalCycle", "appraisalCycle");
		criteria.createAlias("formAvgRatings", "formAvgRatings");
		
		if(paramsInAppraisalForm.getEmpStatus().equalsIgnoreCase("Active"))
		{
			criteria.add(Restrictions.eq("employee.statusName", "Active"));
		}
		else
		{
			criteria.add(Restrictions.eq("employee.statusName", "InActive"));
		}
		
		if (appraisalCycleIds != null && appraisalCycleIds.size() != 0) {
			criteria.add(Restrictions
					.in("appraisalCycle.id", appraisalCycleIds));

		} else {
			criteria.add(Restrictions.eq("appraisalCycle.id",
					paramsInAppraisalForm.getCycleId()));
		}
		if (paramsInAppraisalForm.getCycleId() == -1
				&& appraisalCycleIds.size() == 0) {
			listOfApraisals.put("list", null);
			listOfApraisals.put("size", 0);
			return listOfApraisals;
		} else {
			if (!paramsInAppraisalForm.getAppraisalFormStatus().isEmpty()) {
				if (paramsInAppraisalForm.getAppraisalFormStatus()
						.equalsIgnoreCase("PENDING")) {
					criteria.add(Restrictions.or(
							Restrictions.eq("formStatus", FormStatus.SUBMIT),
							Restrictions.eq("formStatus", FormStatus.PENDING)));
				}

				else {
					criteria.add(Restrictions.eq("formStatus", FormStatus
							.valueOf(paramsInAppraisalForm
									.getAppraisalFormStatus())));
				}
			} else {
				criteria.add(Restrictions.ne("formStatus", FormStatus.SAVE));
			}
			if (!paramsInAppraisalForm.getDepartmentName().equalsIgnoreCase("")) {
				criteria.add(Restrictions.and(Restrictions.ilike(
						"formAvgRatings.departmentName",
						paramsInAppraisalForm.getDepartmentName(),
						MatchMode.ANYWHERE), Restrictions.eq(
						"formAvgRatings.level", 0)));
			}

			if (!paramsInAppraisalForm.getDesignationName()
					.equalsIgnoreCase("")) {
				criteria.add(Restrictions.and(Restrictions.ilike(
						"formAvgRatings.designationName",
						paramsInAppraisalForm.getDesignationName(),
						MatchMode.ANYWHERE), Restrictions.eq(
						"formAvgRatings.level", 0)));
			}
			if (paramsInAppraisalForm.getRole().equalsIgnoreCase("EmpName")) {
				criteria.add(Restrictions.ilike("employee.employeeFullName",
						paramsInAppraisalForm.getSearchString(),
						MatchMode.ANYWHERE));

			} else if (paramsInAppraisalForm.getRole().equalsIgnoreCase(
					"AddedBy")) {
				// criteria.add(Restrictions.and(Restrictions.ilike("formAvgRatings.employeeName",
				// paramsInAppraisalForm.getSearchString(),MatchMode.ANYWHERE),Restrictions.eq("formAvgRatings.level",
				// 1)));
				criteria.createAlias("employee.manager", "manager");
				criteria.add((Restrictions.ilike("manager.employeeFullName",
						paramsInAppraisalForm.getSearchString(),
						MatchMode.ANYWHERE)));
			}
			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			ProjectionList projections = Projections.projectionList();
			projections.add(Projections.property("id"));
			criteria.setProjection(projections);
			List<Long> appraisalIds = criteria.list();
			Criteria criteria2 = sessionFactory.getCurrentSession()
					.createCriteria(AppraisalForm.class);
			try {
				criteria2.add(Restrictions.in("id", appraisalIds));
				criteria2.addOrder(Order.asc("id"));

				if (paramsInAppraisalForm.getRatings().size() > 0) {
					criteria2.createAlias("formAvgRatings", "formAvgRatings");
					int len = paramsInAppraisalForm.getRatings().size();
					Disjunction objDis = Restrictions.disjunction();
					for (int i = 0; i < len; i++) {
						Double rating = paramsInAppraisalForm.getRatings().get(
								i) + 1.0;
						Criterion criterion = Restrictions.and(Restrictions.ge(
								"formAvgRatings.defaultAvgRating",
								paramsInAppraisalForm.getRatings().get(i)),
								Restrictions.lt(
										"formAvgRatings.defaultAvgRating",
										rating));
						objDis.add(criterion);
					}
					criteria2.add(Restrictions.and(objDis,
							Restrictions.eq("formAvgRatings.level", 1)));
				}
				listOfApraisals = getPaginationList(criteria2,
						paramsInAppraisalForm.getStartIndex(),
						paramsInAppraisalForm.getEndIndex());
				Long norec = (Long) listOfApraisals.remove("listSize");
				Integer appraisalListSize = Integer.parseInt(norec.toString());
				listOfApraisals.put("size", appraisalListSize);

				return listOfApraisals;
			} catch (SQLGrammarException e) {
				return listOfApraisals;
				// TODO: handle exception
			}
		}

	}

	//
	//
	//
	//
	// Query query = null;
	// if (role.equalsIgnoreCase("EmpName")) {
	// query = sessionFactory
	// .getCurrentSession()
	// .createQuery(
	// "from AppraisalForm   where concat(employee.firstName, ' ', employee.lastName) like :fullName and appraisalCycle.id=:cycleId");
	// } else if (role.equalsIgnoreCase("AddedBy")) {
	// query = sessionFactory
	// .getCurrentSession()
	// .createQuery(
	// "from AppraisalForm   where concat(employee.manager.firstName, ' ', employee.manager.lastName) like :fullName and appraisalCycle.id=:cycleId");
	// }
	// query.setString("fullName", "%" + searchstring + "%");
	// query.setString("cycleId", String.valueOf(cycleId));

	/*
	 * Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
	 * AppraisalForm.class); criteria.createAlias("appraisalCycle",
	 * "appraisalCycle");
	 * 
	 * criteria.add(Restrictions.eq("appraisalCycle.id", cycleId));
	 * criteria.add(Restrictions.or(Restrictions.eq("formStatus",
	 * FormStatus.PENDING), Restrictions.or( Restrictions.eq("formStatus",
	 * FormStatus.SUBMIT), Restrictions.eq("formStatus",
	 * FormStatus.COMPLETED)))); criteria.createAlias("employee", "employee");
	 * if (role.equalsIgnoreCase("EmpName")) {
	 * criteria.add(Restrictions.or(Restrictions.ilike( "employee.firstName",
	 * searchstring, MatchMode.ANYWHERE),
	 * Restrictions.ilike("employee.lastName", searchstring,
	 * MatchMode.ANYWHERE))); } else if (role.equalsIgnoreCase("AddedBy")) {
	 * criteria.createAlias("employee.manager", "manager");
	 * criteria.add(Restrictions.or(Restrictions.ilike( "manager.firstName",
	 * searchstring, MatchMode.ANYWHERE), Restrictions.ilike("manager.lastName",
	 * searchstring, MatchMode.ANYWHERE))); }
	 * criteria.addOrder(Order.asc("employee.employeeId"));
	 */
	// Integer noOfRecotds = /* criteria.list() */ query.list().size();
	/* criteria */
	// query.setMaxResults(endIndex - startIndex);
	/* criteria */
	// query.setFirstResult(startIndex);
	// List<AppraisalForm> appraisalForms = null;
	// if (searchstring.contains(" ")) {
	// appraisalForms = query.list();
	// } else {
	// appraisalForms = criteria.list();
	// }
	// Map<String, Object> appraisalFormMap = new HashMap<String, Object>();
	// appraisalFormMap.put("list", appraisalForms);
	// appraisalFormMap.put("size", noOfRecotds);
	// return appraisalFormMap;

	@Override
	public Map<String, Object> getAllAppraisalFormByManager(
			SearchQueryParamsInAppraisalForm paramsInAppraisalForm,
			List<Long> listOfManagers, List<Long> appraisalCycleIds) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalForm.class);
		criteria.createAlias("employee", "employee");
		criteria.createAlias("employee.manager", "manager");
		
		if(paramsInAppraisalForm.getEmpStatus().equalsIgnoreCase("Active"))
		{
			criteria.add(Restrictions.eq("employee.statusName", "Active"));
		}
		else
		{
			criteria.add(Restrictions.eq("employee.statusName", "InActive"));
		}

		// criteria.add(Restrictions.or(Restrictions.eq("employee.employeeId",
		// paramsInAppraisalForm.getEmployeeID()),
		// Restrictions.ilike("managesList",
		// String.valueOf(paramsInAppraisalForm.getEmployeeID()),
		// MatchMode.ANYWHERE)));
		criteria.add(Restrictions.or(
				Restrictions.eq("employee.employeeId",
						paramsInAppraisalForm.getEmployeeID()),
				Restrictions.in("manager.employeeId", listOfManagers)));
		criteria.createAlias("appraisalCycle", "appraisalCycle");

		criteria.createAlias("formAvgRatings", "formAvgRatings");

		if (appraisalCycleIds != null && appraisalCycleIds.size() != 0) {
			criteria.add(Restrictions
					.in("appraisalCycle.id", appraisalCycleIds));

		} else {
			criteria.add(Restrictions.eq("appraisalCycle.id",
					paramsInAppraisalForm.getCycleId()));
		}

		if (!paramsInAppraisalForm.getAppraisalFormStatus().isEmpty()) {
			if (paramsInAppraisalForm.getAppraisalFormStatus()
					.equalsIgnoreCase("PENDING")) {
				criteria.add(Restrictions.or(
						Restrictions.eq("formStatus", FormStatus.SUBMIT),
						Restrictions.eq("formStatus", FormStatus.PENDING)));
			} else
				criteria.add(Restrictions.eq("formStatus",
						FormStatus.valueOf(paramsInAppraisalForm
								.getAppraisalFormStatus())));
		} else
			criteria.add(Restrictions.ne("formStatus", FormStatus.SAVE));
		if (!paramsInAppraisalForm.getDepartmentName().equalsIgnoreCase("")) {
			criteria.add(Restrictions.and(Restrictions.ilike(
					"formAvgRatings.departmentName",
					paramsInAppraisalForm.getDepartmentName(),
					MatchMode.ANYWHERE), Restrictions.eq(
					"formAvgRatings.level", 0)));
		}

		if (!paramsInAppraisalForm.getDesignationName().equalsIgnoreCase("")) {

			criteria.add(Restrictions.and(Restrictions.ilike(
					"formAvgRatings.designationName",
					paramsInAppraisalForm.getDesignationName(),
					MatchMode.ANYWHERE), Restrictions.eq(
					"formAvgRatings.level", 0)));

		}

		if (paramsInAppraisalForm.getRole().equalsIgnoreCase("EmpName")) {
			criteria.add(Restrictions
					.ilike("employee.employeeFullName",
							paramsInAppraisalForm.getSearchString(),
							MatchMode.ANYWHERE));
		} else if (paramsInAppraisalForm.getRole().equalsIgnoreCase("AddedBy")) {
			// criteria.add(Restrictions.and(Restrictions.ilike("formAvgRatings.employeeName",
			// paramsInAppraisalForm.getSearchString(),MatchMode.ANYWHERE),Restrictions.eq("formAvgRatings.level",
			// 1)));
			criteria.add((Restrictions
					.ilike("manager.employeeFullName",
							paramsInAppraisalForm.getSearchString(),
							MatchMode.ANYWHERE)));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.property("id"));
		criteria.setProjection(projections);
		List<Long> appraisalIds = criteria.list();
		Map<String, Object> listOfApraisals = new HashMap<String, Object>();
		Criteria criteria2 = sessionFactory.getCurrentSession().createCriteria(
				AppraisalForm.class);
		try {
			criteria2.add(Restrictions.in("id", appraisalIds));
			criteria2.addOrder(Order.asc("id"));
			if (paramsInAppraisalForm.getRatings().size() > 0) {
				criteria2.createAlias("formAvgRatings", "formAvgRatings");
				int len = paramsInAppraisalForm.getRatings().size();
				Disjunction objDis = Restrictions.disjunction();
				for (int i = 0; i < len; i++) {
					Double rating = paramsInAppraisalForm.getRatings().get(i) + 1.0;
					Criterion criterion = Restrictions.and(Restrictions.ge(
							"formAvgRatings.defaultAvgRating",
							paramsInAppraisalForm.getRatings().get(i)),
							Restrictions.lt("formAvgRatings.defaultAvgRating",
									rating));
					objDis.add(criterion);
				}
				criteria2.add(Restrictions.and(objDis,
						Restrictions.eq("formAvgRatings.level", 1)));
			}

			listOfApraisals = getPaginationList(criteria2,
					paramsInAppraisalForm.getStartIndex(),
					paramsInAppraisalForm.getEndIndex());
			Long norec = (Long) listOfApraisals.remove("listSize");
			Integer appraisalListSize = Integer.parseInt(norec.toString());
			listOfApraisals.put("size", appraisalListSize);
			return listOfApraisals;
		} catch (SQLGrammarException e) {
			return listOfApraisals;
			// TODO: handle exception
		}
	}

	/*
	 * @Override public Map<String, Object> searchByManagerName (Integer
	 * startIndex, Integer endIndex, Long employeeID, Long cycleId, String
	 * searchstring) { Criteria criteria =
	 * sessionFactory.getCurrentSession().createCriteria( AppraisalForm.class);
	 * 
	 * criteria.createAlias("appraisalCycle", "appraisalCycle");
	 * 
	 * criteria.add(Restrictions.eq("appraisalCycle.id", cycleId));
	 * criteria.add(Restrictions.or(Restrictions.eq("formStatus",
	 * FormStatus.PENDING), Restrictions.or( Restrictions.eq("formStatus",
	 * FormStatus.SUBMIT), Restrictions.eq("formStatus",
	 * FormStatus.COMPLETED)))); criteria.createAlias("employee", "employee");
	 * criteria.createAlias("employee.manager", "manager");
	 * criteria.add(Restrictions.or(Restrictions .ilike("manager.firstName",
	 * searchstring, MatchMode.ANYWHERE), Restrictions.ilike("manager.lastName",
	 * searchstring, MatchMode.ANYWHERE)));
	 * criteria.addOrder(Order.asc("employee.employeeId"));
	 * 
	 * Integer noOfRecotds = criteria.list().size();
	 * criteria.setMaxResults(endIndex - startIndex);
	 * criteria.setFirstResult(startIndex);
	 * 
	 * List<AppraisalForm> appraisalForms = criteria.list(); Map<String, Object>
	 * appraisalFormMap = new HashMap<String, Object>();
	 * appraisalFormMap.put("list", appraisalForms);
	 * appraisalFormMap.put("size", noOfRecotds); return appraisalFormMap;
	 * 
	 * }
	 */
	@Override
	public List<KPI> getAllKpiForIndividualKra(Long kraId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				KPI.class);
		criteria.createAlias("kra", "kra");
		criteria.add(Restrictions.eq("kra.id", kraId));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public List<Designation> getDesignationsOfDept(Long deptId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				Designation.class);
		criteria.createAlias("empDepartment", "dept");
		criteria.add(Restrictions.eq("dept.departmentId", deptId));
		return criteria.list();
	}

	@Override
	public Boolean isDuplicateKRA(String kraName, Long departmentId,
			Long designationId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				KRA.class);
		criteria.createAlias("empDepartment", "empDepartment");
		criteria.createAlias("designation", "designation");
		criteria.add(Restrictions.ilike("name", kraName));
		criteria.add(Restrictions
				.eq("empDepartment.departmentId", departmentId));
		criteria.add(Restrictions.eq("designation.id", designationId));
		if (criteria.list().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<KRA> getDesignationWiseKRAs(Long departmentId,
			Long designationId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				KRA.class);
		criteria.createAlias("empDepartment", "empDepartment");
		criteria.createAlias("designation", "designation");
		criteria.add(Restrictions
				.eq("empDepartment.departmentId", departmentId));
		criteria.add(Restrictions.eq("designation.id", designationId));
		return criteria.list();
	}

	@Override
	public Map<String, Object> searchKRAData(
			SearchQueryParamsInKRA searchQueryParamsInKRA) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				KRA.class);
		criteria.createAlias("empDepartment", "empDepartment");
		criteria.createAlias("designation", "designation");
		if (searchQueryParamsInKRA.getDepartmentId() != null) {
			criteria.add(Restrictions.eq("empDepartment.departmentId",
					searchQueryParamsInKRA.getDepartmentId()));
		}
		if (searchQueryParamsInKRA.getDesignationId() != null) {
			criteria.add(Restrictions.eq("designation.id",
					searchQueryParamsInKRA.getDesignationId()));
		}
		if (searchQueryParamsInKRA.getMultipleSearch() != "") {
			Criterion karName = Restrictions.ilike("name",
					searchQueryParamsInKRA.getMultipleSearch(),
					MatchMode.ANYWHERE);
			Criterion description = (Restrictions.ilike("description",
					searchQueryParamsInKRA.getMultipleSearch(),
					MatchMode.ANYWHERE));
			criteria.add(Restrictions.or(karName, description));
		}

		criteria.addOrder(Order.desc("id"));
		Integer noOfRecords = criteria.list().size();
		criteria.setMaxResults(searchQueryParamsInKRA.getEndIndex()
				- searchQueryParamsInKRA.getStartIndex());
		criteria.setFirstResult(searchQueryParamsInKRA.getStartIndex());
		List<KRA> list = criteria.list();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("size", noOfRecords);
		return map;
	}

	@Override
	public Map<String, Object> getDesignationsUnderCycle(Integer startIndex,
			Integer endIndex, Long cycleId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				DesignationKRAMapping.class);
		criteria.createAlias("cycle", "cycle");
		criteria.add(Restrictions.eq("cycle.id", cycleId));
		Integer noOfRecotds = criteria.list().size();
		criteria.setMaxResults(endIndex - startIndex);
		criteria.setFirstResult(startIndex);

		List<DesignationKRAMapping> designationKRAMappings = criteria.list();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", designationKRAMappings);
		map.put("size", noOfRecotds);
		return map;
	}

	@Override
	public Map<String, Object> getEmployeeAppraisalForms(
			SearchQueryParamsInAppraisalForm paramsInAppraisalForm) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalForm.class);
		criteria.createAlias("employee", "employee");
		criteria.createAlias("appraisalCycle", "appraisalCycle");
		criteria.add(Restrictions.eq("employee.employeeId",
				paramsInAppraisalForm.getEmployeeID()));

		criteria.add(Restrictions.eq("appraisalCycle.id",
				paramsInAppraisalForm.getCycleId()));

		criteria.add(Restrictions.or(Restrictions.eq("formStatus",
				FormStatus.OPENFORDISCUSSION), Restrictions.or(
				Restrictions.eq("formStatus", FormStatus.PENDINGAGREEMENT),
				Restrictions.eq("formStatus", FormStatus.COMPLETED))));

		criteria.addOrder(Order.asc("employee.employeeId"));

		Integer noOfRecotds = criteria.list().size();
		if (paramsInAppraisalForm.getStartIndex() != null
				&& paramsInAppraisalForm.getEndIndex() != null) {
			criteria.setMaxResults(paramsInAppraisalForm.getEndIndex()
					- paramsInAppraisalForm.getStartIndex());
			criteria.setFirstResult(paramsInAppraisalForm.getStartIndex());
		}
		List<AppraisalForm> appraisalForms = criteria.list();
		Map<String, Object> appraisalFormMap = new HashMap<String, Object>();
		appraisalFormMap.put("list", appraisalForms);
		appraisalFormMap.put("size", noOfRecotds);
		return appraisalFormMap;
	}

	@Override
	public Double getDesignationKraPercentage(Long departmentId,
			Long designationId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				KRA.class);
		criteria.createAlias("empDepartment", "empDepartment");
		criteria.createAlias("designation", "designation");
		criteria.add(Restrictions
				.eq("empDepartment.departmentId", departmentId));
		criteria.add(Restrictions.eq("designation.id", designationId));
		criteria.setProjection(Projections.sum("designationKraPercentage"));
		return (Double) criteria.uniqueResult();
	}

	@Override
	public List<KRA> krasForIndividual(Employee employee) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				KRA.class);
		criteria.createAlias("designation", "designation");
		criteria.createAlias("empDepartment", "empDepartment");
		criteria.add(Restrictions.ilike("designation.name",
				employee.getDesignation()));
		criteria.add(Restrictions.ilike("empDepartment.departmentName",
				employee.getDepartmentName()));

		return criteria.list();
	}

	@Override
	public void copyTheCycleData(@RequestParam Long oldCycleId,
			@RequestParam Long newCycleId) {
		Query query = sessionFactory
				.getCurrentSession()
				.createSQLQuery(
						"call appraisalCycleUpdate(:oldCycleId,:newCycleId)")
				.addEntity(DesignationKRAMapping.class)
				.setParameter("oldCycleId", oldCycleId)
				.setParameter("newCycleId", newCycleId);
		query.executeUpdate();
	}

	@Override
	public Boolean getAppraisalCycleList(Long newCycleId) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				DesignationKRAMapping.class);
		criteria.createAlias("cycle", "cycle");

		criteria.add(Restrictions.eq("cycle.id", newCycleId));

		if (criteria.list().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<ReviewAudit> getReviewComments(Long appriasalFormId) {
		// TODO Auto-generated method stub

		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(ReviewAudit.class);
		criteria.createAlias("appraisalForm", "appraisalForm");
		criteria.add(Restrictions.eq("appraisalForm.id", appriasalFormId));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}

	@Override
	public List<Long> appraisalId(SearchQueryParamsInAppraisalForm appraisal,
			Date fromDate, Date todate) {
		Criteria criteria = getSessionFactory().getCurrentSession()
				.createCriteria(AppraisalCycle.class);
		if (fromDate != null && todate != null) {
			criteria.add(Restrictions.between("configurationPeriod.minimum",
					fromDate, todate));
		}
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.property("id"));
		criteria.setProjection(projections);
		List<Long> appraisalId = criteria.list();
		return criteria.list();
	}

	@Override
	public Map<String, Object> getNotSubmittedFormEmployees(
			SearchQueryParamsInAppraisalForm appraisal, List<Long> appraisalIds) {
		//search field for active / in active 
		String empStatus = appraisal.getEmpStatus();
		String queryStr = "";
		if (appraisalIds != null) {
			String appraisalCycleIds = appraisalIds.toString();
			appraisalCycleIds = appraisalCycleIds.replace("[", "(");
			appraisalCycleIds = appraisalCycleIds.replace("]", ")");
			queryStr = "select all from EMPLOYEE e LEFT JOIN AppraisalForm af ON e.EmployeeId = af.EmployeeId AND af.CYCLE_ID In "
					+ appraisalCycleIds
					+ " where (af.APPRAISALFORM_ID IS NULL AND e.StatusName like '"+empStatus+"' OR af.FormStatus like 'SAVE')";
		} else {
			queryStr = "select all from EMPLOYEE e LEFT JOIN AppraisalForm af ON e.EmployeeId = af.EmployeeId AND af.CYCLE_ID ="
					+ appraisal.getCycleId()
					+ " where (af.APPRAISALFORM_ID IS NULL AND (e.StatusName like '"+empStatus+"' and e.JoiningDate < (SELECT `START_DATE` FROM `AppraisalCycle` WHERE `CYCLE_ID` = "
					+ appraisal.getCycleId()
					+ ")) OR (af.FormStatus like 'SAVE' AND e.StatusName like '"+empStatus+"'))";
		}
		

		if (!appraisal.getDepartmentName().equalsIgnoreCase("")) {
			queryStr = queryStr + "AND e.DepartmentName like '"
					+ appraisal.getDepartmentName() + "'";
		}
		if (!appraisal.getDesignationName().equalsIgnoreCase("")) {
			queryStr = queryStr + "AND e.DESIGNATION like '"
					+ appraisal.getDesignationName() + "'";
		}
		if (appraisal.getRole().equalsIgnoreCase("EmpName")) {
			queryStr = queryStr + "AND e.EmployeeFullName like '%"
					+ appraisal.getSearchString() + "%'";
		}
		if (appraisal.getRole().equalsIgnoreCase("AddedBy")) {

			queryStr = queryStr
					+ "AND e.manager IN (SELECT EmployeeId FROM EMPLOYEE WHERE EmployeeFullName LIKE '%"
					+ appraisal.getSearchString() + "%')";
		}
		Employee employee = (Employee) utils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		if (employee.getRole().equalsIgnoreCase("Manager")) {
			queryStr = queryStr + "AND e.manager = " + employee.getEmployeeId();
		}
		if (employee.getRole().equalsIgnoreCase("Delivery Manager")) {
			List<Long> empIDs = projectService.mangerUnderManager(employee
					.getEmployeeId());
			empIDs.remove(employee.getEmployeeId());
			String ids = empIDs.toString();
			ids = ids.replace("[", "(");
			ids = ids.replace("]", ")");
			queryStr = queryStr + "AND (e.manager IN" + ids
					+ "OR e.EmployeeId IN " + ids + ")";
		}
		SQLQuery query = getSessionFactory().getCurrentSession()
				.createSQLQuery(queryStr.replaceFirst("all", "e.*"));
		query.addEntity(Employee.class);
		
	
		String countQuery = queryStr.replaceFirst("all", "COUNT(*) as count");

		SQLQuery queryCount = getSessionFactory().getCurrentSession()
				.createSQLQuery(countQuery);
		queryCount.addScalar("count", IntegerType.INSTANCE);

		Object object = queryCount.uniqueResult();
		Integer noOfRecords = (Integer) object;
		if (appraisal.getStartIndex() != null
				&& appraisal.getEndIndex() != null) {
			query.setMaxResults(appraisal.getEndIndex()
					- appraisal.getStartIndex());
			query.setFirstResult(appraisal.getStartIndex());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("list", query.list());
		map.put("size", noOfRecords);
		return map;
	}

	@Override
	public List<AppraisalForm> getLoggedInEmployeeAppraisalForm(
			Long employeeId, List<Long> cycleIds) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalForm.class);
		criteria.createAlias("employee", "employee");
		criteria.createAlias("appraisalCycle", "appraisalCycle");
		criteria.add(Restrictions.eq("employee.employeeId", employeeId));
		criteria.add(Restrictions.in("appraisalCycle.id", cycleIds));
	    criteria.add(Restrictions.or(Restrictions.eq("formStatus",
				FormStatus.OPENFORDISCUSSION), Restrictions.or(
				Restrictions.eq("formStatus", FormStatus.PENDINGAGREEMENT),
				Restrictions.eq("formStatus", FormStatus.COMPLETED))));
		return criteria.list();
	}

	@Override
	public List<Long> getAppraisalCycleIds() {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(
				AppraisalCycle.class);
		// criteria.add(Restrictions.isNotNull("id"));
		ProjectionList projections = Projections.projectionList();
		projections.add(Projections.property("id"));
		criteria.setProjection(projections);
		List<Long> cycleIds = criteria.list();
		return cycleIds;
	}

}
